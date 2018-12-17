package br.com.eiasiscon.nfe;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eiasiscon.configuracao.Configuracao;
import br.com.eiasiscon.configuracao.ConfiguracaoService;
import br.com.eiasiscon.empresa.Empresa;
import br.com.eiasiscon.empresa.EmpresaRepository;
import br.com.eiasiscon.nfe.javanfe.Assinar;
import br.com.eiasiscon.nfe.javanfe.Eventos;
import br.com.eiasiscon.notafiscal.NotaFiscal;
import br.com.eiasiscon.notafiscal.NotaFiscalRepository;
import br.com.eiasiscon.uploadfiles.StorageService;
import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoService;
import br.com.samuelweb.certificado.exception.CertificadoException;
import br.com.samuelweb.nfe.NfeWeb;
import br.com.samuelweb.nfe.dom.ConfiguracoesWebNfe;
import br.com.samuelweb.nfe.dom.Enum.StatusEnum;
import br.com.samuelweb.nfe.exception.NfeException;
import br.com.samuelweb.nfe.util.CertificadoUtil;
import br.com.samuelweb.nfe.util.ConstantesUtil;
import br.com.samuelweb.nfe.util.Estados;
import br.com.samuelweb.nfe.util.XmlUtil;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TProcEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TRetEnvEvento;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.procNFe.TNfeProc;
import br.inf.portalfiscal.nfe.schema_4.retConsReciNFe.TRetConsReciNFe;
import br.inf.portalfiscal.nfe.schema_4.retConsSitNFe.TRetConsSitNFe;
import br.inf.portalfiscal.nfe.schema_4.retConsStatServ.TRetConsStatServ;

@Service
public class NFeService {
	
	@Autowired
	private NotaFiscalRepository repository;
	@Autowired
	private EmpresaRepository empresaRepository;
	@Autowired
	private ConfiguracaoService configuracaoService;
	@Autowired
	private StorageService storageService;
	
	public byte[] GerarDanfe(String idNota) {
		NotaFiscal nf = repository.findById(idNota).get();
		NFeGeradorDanfe gen= new NFeGeradorDanfe();
		
		byte[] retorn = gen.gerarPDF(nf.getXml());
		return retorn;
	}
	
	private ConfiguracoesWebNfe getConfig(String empresaID) {
		Certificado certificado = new Certificado();
		ConfiguracoesWebNfe config = null;
		try {
			Empresa empresa = empresaRepository.findById(empresaID).get();
			Configuracao configuracao = configuracaoService.getConfiguracao(empresaID);
			
			certificado = CertificadoService.certificadoPfx(storageService.getPath(empresaID, configuracao.getCertificadoFile()), configuracao.getCertificadoSenha());  
			config = ConfiguracoesWebNfe.iniciaConfiguracoes(
					Estados.valueOf(empresa.getMunicipio().getUF().toString()),
	                ConstantesUtil.AMBIENTE.PRODUCAO,
	                certificado,
	                MethodHandles.lookup().lookupClass().getResource("/schemas").getPath(),
	                false);
		} catch (CertificadoException e) {
			e.printStackTrace();
		}
        
		return config;
	}
	
	public TRetConsStatServ statusServico(String empresaID) {
		TRetConsStatServ retorno = null;
		try {
            retorno = NfeWeb.statusServico(getConfig(empresaID), ConstantesUtil.NFE);
            System.out.println("Status:" + retorno.getCStat());
            System.out.println("Motivo:" + retorno.getXMotivo());
            System.out.println("Data:" + retorno.getDhRecbto());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }	
		return retorno;
	}
	
	public NFeDTO enviar(NotaFiscal nf) {
		String cnpj = nf.getEmpresa().getNumDoc().replaceAll("[^0-9,]", "");
		String uf = nf.getEmitente().getMunicipio().getUF().toString();
		
		NFeDTO retorno = new NFeDTO();		
		retorno.setUf(uf);
		retorno.setNumDoc(cnpj);
		
		ConfiguracoesWebNfe config = getConfig(nf.getEmpresa().getId());
		
		try {
            TNFe nfe = NFeConversor.getnFe(nf, config.getAmbiente());

            TEnviNFe enviNFe = new TEnviNFe();
            enviNFe.setVersao("4.00");
            enviNFe.setIdLote("1");
            enviNFe.setIndSinc("0");
            enviNFe.getNFe().add(nfe);
            
            enviNFe = NfeWeb.montaNfe(config, enviNFe, false);
            
            nf.setXml(XmlUtil.objectToXml(enviNFe));
            repository.save(nf);
            
            TRetEnviNFe retornoEnviNFe = NfeWeb.enviarNfe(config, enviNFe, ConstantesUtil.NFE);
            retorno.setData(retornoEnviNFe.getDhRecbto());
            retorno.setStatus(retornoEnviNFe.getCStat());
            retorno.setMotivo(retornoEnviNFe.getXMotivo());
            retorno.setSucesso(true);
            retorno.setIdNota(nf.getId());
            retorno.setEmpresaID(nf.getEmpresa().getId());
            
            if (!retornoEnviNFe.getCStat().equals(StatusEnum.LOTE_RECEBIDO.getCodigo())) {
            	retorno.setSucesso(false);
            	retorno.setData(nf.getXml());
                throw new NfeException("Status:" + retornoEnviNFe.getCStat() + " - Motivo:" + retornoEnviNFe.getXMotivo());
            }
            retorno.setRecibo(retornoEnviNFe.getInfRec().getNRec());
        } catch (NfeException | JAXBException e) {
        	retorno.setMotivo(e.getMessage());
            System.out.println("Erro:" + e.getMessage());
        }	
		return retorno;
	}
	
	public NFeDTO consultaRecibo(NFeDTO retornoEnvio) {
		NFeDTO retornar = new NFeDTO();
		
		try {
			ConfiguracoesWebNfe config = getConfig(retornoEnvio.getEmpresaID());
			
			TRetConsReciNFe retornoNfe;
	        while (true) {
	            retornoNfe = NfeWeb.consultaRecibo(config, retornoEnvio.getRecibo(), ConstantesUtil.NFE);
	            if (retornoNfe.getCStat().equals(StatusEnum.LOTE_EM_PROCESSAMENTO.getCodigo())) {
	                System.out.println("Lote Em Processamento, vai tentar novamente apos 2 Segundo.");
	                Thread.sleep(2000);
	                continue;
	            } else {
	                break;
	            }
	        }
	        
	        retornar.setSucesso(true);
	        retornar.setData(retornoNfe.getDhRecbto());
	        retornar.setStatus(retornoNfe.getProtNFe().get(0).getInfProt().getCStat());
	        retornar.setMotivo(retornoNfe.getProtNFe().get(0).getInfProt().getXMotivo());
	        retornar.setRecibo(retornoNfe.getProtNFe().get(0).getInfProt().getNProt());

	        if (!retornoNfe.getCStat().equals(StatusEnum.LOTE_PROCESSADO.getCodigo())) {
	        	retornar.setSucesso(false);
	            throw new NfeException("Status:" + retornoNfe.getCStat() + " - " + retornoNfe.getXMotivo());
	        }
	        if (!retornoNfe.getProtNFe().get(0).getInfProt().getCStat().equals(StatusEnum.AUTORIZADO.getCodigo()) && !retornoNfe.getProtNFe().get(0).getInfProt().getCStat().equals("204")) {
	        	retornar.setSucesso(false);
	        	retornar.setStatus(retornoNfe.getProtNFe().get(0).getInfProt().getCStat());
		        retornar.setMotivo(retornoNfe.getProtNFe().get(0).getInfProt().getXMotivo());
	            throw new NfeException("Status:" + retornoNfe.getProtNFe().get(0).getInfProt().getCStat() + " - " + retornoNfe.getProtNFe().get(0).getInfProt().getXMotivo());
	        }
	        
	        if(retornoNfe.getProtNFe().get(0).getInfProt().getCStat().equals("204")) {
	        	retornoEnvio.setRecibo(retornoNfe.getProtNFe().get(0).getInfProt().getXMotivo().replaceAll("[^0-9,]", "").substring(44));
	        	consultaRecibo(retornoEnvio);
	        } else {
	        	NotaFiscal nf = repository.findById(retornoEnvio.getIdNota()).get();
		        
		        TEnviNFe enviNFe = XmlUtil.xmlToObject(nf.getXml(), TEnviNFe.class);
		        
		        String xmlProc = XmlUtil.criaNfeProc(enviNFe, retornoNfe.getProtNFe().get(0));
		        nf.setXml(xmlProc);
		        nf.setSitNfe("Autorizada");
		        repository.save(nf);

		        retornar.setData(xmlProc);
	        }
	        
		} catch (NfeException | JAXBException | InterruptedException e) {
			retornar.setMotivo(e.getMessage());
            System.out.println("Erro:" + e.getMessage());
        }
				
		return retornar;
	}

	public NFeDTO cancelar(NFeDTO cancelamento) {
		NFeDTO retornar = new NFeDTO();
		
		try {			
			NotaFiscal nf = repository.findById(cancelamento.getIdNota()).get();
			
			String cnpj = nf.getEmpresa().getNumDoc().replaceAll("[^0-9,]", "");
			
			ConfiguracoesWebNfe config = getConfig(cancelamento.getEmpresaID());
			
			TNfeProc nfeProc = XmlUtil.xmlToObject(nf.getXml(), TNfeProc.class);
						
			String chave = nf.getChave().replaceAll("[^0-9,]", "");
            String protocolo = nfeProc.getProtNFe().getInfProt().getNProt();
            String nSeq = "1";
            if(nf.getProcEventoNFe() != null) {
            	nSeq = String.valueOf(nf.getProcEventoNFe().size() + 1);
            }            

            String id = "ID" + ConstantesUtil.EVENTO.CANCELAR + chave + "01";

            TEnvEvento enviEvento = new TEnvEvento();
            enviEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CANCELAMENTO);
            enviEvento.setIdLote("1");

            TEvento eventoCancela = new TEvento();
            eventoCancela.setVersao(ConstantesUtil.VERSAO.EVENTO_CANCELAMENTO);

            TEvento.InfEvento infoEvento = new TEvento.InfEvento();
            infoEvento.setId(id);
            infoEvento.setChNFe(chave);
            infoEvento.setCOrgao(String.valueOf(config.getEstado().getCodigoIbge()));
            infoEvento.setTpAmb(config.getAmbiente());
            infoEvento.setCNPJ(cnpj);

            infoEvento.setDhEvento(XmlUtil.dataNfe());
            infoEvento.setTpEvento(ConstantesUtil.EVENTO.CANCELAR);
            infoEvento.setNSeqEvento(nSeq);
            infoEvento.setVerEvento(ConstantesUtil.VERSAO.EVENTO_CANCELAMENTO);

            TEvento.InfEvento.DetEvento detEvento = new TEvento.InfEvento.DetEvento();
            detEvento.setVersao(ConstantesUtil.VERSAO.EVENTO_CANCELAMENTO);
            detEvento.setDescEvento("Cancelamento");
            detEvento.setNProt(protocolo);
            detEvento.setXJust(cancelamento.getMotivo());
            infoEvento.setDetEvento(detEvento);
            eventoCancela.setInfEvento(infoEvento);
            enviEvento.getEvento().add(eventoCancela);
            
            String xml = XmlUtil.objectToXml(enviEvento);
			xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
			xml = xml.replaceAll("<evento v", "<evento xmlns=\"http://www.portalfiscal.inf.br/nfe\" v");
			
			xml = Assinar.assinaNfe(config, xml, Assinar.EVENTO);
			System.out.println(xml);						
			String xmlRetEnvEvento = Eventos.enviarEvento(CertificadoUtil.iniciaConfiguracoes(config), xml, ConstantesUtil.EVENTO.CANCELAR, false, ConstantesUtil.NFE);

            TRetEnvEvento retorno = XmlUtil.xmlToObject(xmlRetEnvEvento, TRetEnvEvento.class);

            if (!StatusEnum.LOTE_EVENTO_PROCESSADO.getCodigo().equals(retorno.getCStat())) {
                throw new NfeException("Motivo:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo());
            }

            if (!StatusEnum.EVENTO_VINCULADO.getCodigo().equals(retorno.getRetEvento().get(0).getInfEvento().getCStat())) {
                throw new NfeException("Motivo: " + retorno.getRetEvento().get(0).getInfEvento().getCStat() + " - " + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
            }
            
            enviEvento = XmlUtil.xmlToObject(xml, TEnvEvento.class);
            // Cria TProcEventoNFe
            TProcEvento procEvento = new TProcEvento();
            procEvento.setVersao("1.00");
            procEvento.setEvento(enviEvento.getEvento().get(0));
            procEvento.setRetEvento(retorno.getRetEvento().get(0));
                        
            String xmlProcEventoNFe = XmlUtil.objectToXml(procEvento);
            
            if(nf.getProcEventoNFe() == null) { nf.setProcEventoNFe(new ArrayList<String>()); }
            nf.getProcEventoNFe().add(xmlProcEventoNFe);
            nf.setSitNfe("Cancelada");
            repository.save(nf);

            retornar.setIdNota(cancelamento.getIdNota());
            retornar.setSucesso(true);
            retornar.setStatus(retorno.getRetEvento().get(0).getInfEvento().getCStat());
            retornar.setMotivo(retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
	        retornar.setData(xmlProcEventoNFe);
		} catch (NfeException | JAXBException e) {
			retornar.setMotivo(e.getMessage());
            System.out.println("Erro:" + e.getMessage());
        }
				
		return retornar;
	}
	
	public NFeDTO consultar(NFeDTO consulta) {
		NFeDTO retornar = new NFeDTO();
		
		try {			
			NotaFiscal nf = repository.findById(consulta.getIdNota()).get();
			
			ConfiguracoesWebNfe config = getConfig(consulta.getEmpresaID());
			
			String chave = nf.getChave();
            TRetConsSitNFe retorno = NfeWeb.consultaXml(config, chave, ConstantesUtil.NFE);
                        
            String xmlRetorno = XmlUtil.objectToXml(retorno);
            
            retornar.setIdNota(consulta.getIdNota());
            retornar.setSucesso(true);
            retornar.setStatus(retorno.getCStat());
            retornar.setMotivo(retorno.getXMotivo());
	        retornar.setData(xmlRetorno);
		} catch (NfeException | JAXBException e) {
			retornar.setMotivo(e.getMessage());
            System.out.println("Erro:" + e.getMessage());
        }
				
		return retornar;
	}
		
}
