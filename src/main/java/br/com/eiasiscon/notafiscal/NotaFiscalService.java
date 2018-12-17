package br.com.eiasiscon.notafiscal;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.eiasiscon.base.BaseService;
import br.com.eiasiscon.municipio.UF;
import br.com.eiasiscon.nfe.NFeChaveAcesso;
import br.com.eiasiscon.nfe.NFeService;
import br.com.eiasiscon.notafiscal.item.DetalheFiscal;
import br.com.eiasiscon.notafiscal.item.ItemNotaFiscal;
import br.com.eiasiscon.produto.Produto;
import br.com.eiasiscon.produto.ProdutoRepository;
import br.com.eiasiscon.produto.tributacao.Destino;
import br.com.eiasiscon.produto.tributacao.Tributacao;
import br.com.eiasiscon.produto.tributacao.cofins.COFINS;
import br.com.eiasiscon.produto.tributacao.cofins.COFINSST;
import br.com.eiasiscon.produto.tributacao.icms.ICMS;
import br.com.eiasiscon.produto.tributacao.icms.ModBC;
import br.com.eiasiscon.produto.tributacao.icms.ModBCST;
import br.com.eiasiscon.produto.tributacao.ipi.IPI;
import br.com.eiasiscon.produto.tributacao.ipi.TpCalcIPI;
import br.com.eiasiscon.produto.tributacao.pis.PIS;
import br.com.eiasiscon.produto.tributacao.pis.PISST;

@Service
public class NotaFiscalService extends BaseService<NotaFiscal, String> {
	
	@Autowired
	private NotaFiscalRepository repository;
	@Autowired
	private ProdutoRepository repositoryProd;
	@Autowired
	private NFeService serviceNFe;
	
	@Autowired
	public void setJpaRepository(NotaFiscalRepository jpa) {
		setJpa(jpa);
	}
	
	public Page<NotaFiscal> find(String query, String empresa, Pageable pageable) {
		Page<NotaFiscal>  entities = repository.find(query, empresa, pageable);
		return entities;
	}
	
	public byte[] exportar(String id) {
		try {
			NotaFiscal nf = repository.findById(id).get();

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
			ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

			if (nf.getSitNfe().equals("Autorizada")) {
				File fileToZip = new File(nf.getChave().concat("-procNFe.xml"));
				zipOutputStream.putNextEntry(new ZipEntry(fileToZip.getName()));
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(nf.getXml().getBytes());
				IOUtils.copy(byteArrayInputStream, zipOutputStream);
				
				File pdfToZip = new File(nf.getChave().concat(".pdf"));
				zipOutputStream.putNextEntry(new ZipEntry(pdfToZip.getName()));
				ByteArrayInputStream byteArrayInputStreamPdf = new ByteArrayInputStream(serviceNFe.GerarDanfe(nf.getId()));
				IOUtils.copy(byteArrayInputStreamPdf, zipOutputStream);
			}

			if (nf.getSitNfe().equals("Cancelada")) {
				File fileToZip = new File(nf.getChave().concat("-procNFe.xml"));
				zipOutputStream.putNextEntry(new ZipEntry(fileToZip.getName()));
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(nf.getXml().getBytes());
				IOUtils.copy(byteArrayInputStream, zipOutputStream);

				File fileToZipCancel = new File(nf.getChave().concat("-procEvento.xml"));
				zipOutputStream.putNextEntry(new ZipEntry(fileToZipCancel.getName()));
				ByteArrayInputStream byteArrayInputStreamCancel = new ByteArrayInputStream(
						nf.getProcEventoNFe().get(0).getBytes());
				IOUtils.copy(byteArrayInputStreamCancel, zipOutputStream);
			}

			zipOutputStream.closeEntry();

			if (zipOutputStream != null) {
				zipOutputStream.finish();
				zipOutputStream.flush();
				IOUtils.closeQuietly(zipOutputStream);
			}

			IOUtils.closeQuietly(bufferedOutputStream);
			IOUtils.closeQuietly(byteArrayOutputStream);
			try {
				Files.write(Paths.get(System.getProperty("user.home") + "/nota.zip"),
						byteArrayOutputStream.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}

			return byteArrayOutputStream.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] gerarZip(Date ini, Date fim, String empresa) {
		try {
		
		List<NotaFiscal> list = repository.findByEmissao(ini, fim, empresa);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);
        
        for (NotaFiscal nf : list) {
        	
        	if(nf.getSitNfe().equals("Autorizada")) {
        		File fileToZip = new File(nf.getChave().concat("-procNFe.xml"));
                zipOutputStream.putNextEntry(new ZipEntry(fileToZip.getName()));
                
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(nf.getXml().getBytes());

                IOUtils.copy(byteArrayInputStream, zipOutputStream);
        	}
        	
        	if(nf.getSitNfe().equals("Cancelada")) {
        		File fileToZip = new File(nf.getChave().concat("-procNFe.xml"));
                zipOutputStream.putNextEntry(new ZipEntry(fileToZip.getName()));
                
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(nf.getXml().getBytes());

                IOUtils.copy(byteArrayInputStream, zipOutputStream);
                
        		File fileToZipCancel = new File(nf.getChave().concat("-procEvento.xml"));
                zipOutputStream.putNextEntry(new ZipEntry(fileToZipCancel.getName()));
                
                ByteArrayInputStream byteArrayInputStreamCancel = new ByteArrayInputStream(nf.getProcEventoNFe().get(0).getBytes());

                IOUtils.copy(byteArrayInputStreamCancel, zipOutputStream);
        	}
                        
        }
        zipOutputStream.closeEntry();
        
        if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
        }
        
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);
        try {
			Files.write(Paths.get(System.getProperty("user.home") +"/notas.zip"), byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return byteArrayOutputStream.toByteArray();
        
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int getMaxSerie(String empresa) {
		return repository.maxSerie(empresa);
	}
	
	public int getProximoNumero(String empresa) {
		return repository.maxNumero(empresa) + 1;
	}
	
	public NotaFiscal duplicar(String id) {
		NotaFiscal entity = repository.findById(id).get();
		entity.setId(null);
		entity.setDhEmi(new Date());
		entity.setDhSaiEnt(new Date());
		entity.setSerie(getMaxSerie(entity.getEmpresa().getId()));
		entity.setNumero(getProximoNumero(entity.getEmpresa().getId()));
		entity.setChave(NFeChaveAcesso.getChave(entity));
		entity.setSitNfe("Digitação");
		entity.setXml(null);
		entity.setProcEventoNFe(null);
		return repository.save(entity);
	}
	
	public ItemNotaFiscal getItem(String idProd, BigDecimal quant, BigDecimal vUn, UF uf) {
		BigDecimal subtotal = quant.multiply(vUn).setScale(2, RoundingMode.HALF_UP);
		ItemNotaFiscal i = new ItemNotaFiscal();
		i.setProduto(repositoryProd.findById(idProd).get());
		i.setQuantidade(quant);
		i.setPrecoVenda(vUn);
		Produto p = i.getProduto();
		if (p.getPesoBruto() != null) {
			i.setPesoBruto(p.getPesoBruto().multiply(quant).setScale(2, RoundingMode.HALF_UP));
		}
		if (p.getPesoLiquido() != null) {
			i.setPesoLiquido(p.getPesoLiquido().multiply(quant).setScale(2, RoundingMode.HALF_UP));
		}
		i.setSubtotal(subtotal);
		i.setvFrete(BigDecimal.ZERO);
		i.setvDesc(BigDecimal.ZERO);
		i.setvSeg(BigDecimal.ZERO);
		i.setvOutro(BigDecimal.ZERO);
		i.setNoValorNota(false);
		
		Destino t = getDestino(p.getTributacao(), uf);		
		DetalheFiscal d  = new DetalheFiscal();
		d.setCfop(t.getCfop());
		d.setExtipi(p.getExtipi());
		d.setGenero(p.getGenero());
		d.setcEan(p.getcEan());
		d.setcEanTrib(p.getcEanTrib());
		if(p.getUtrib()!=null){
			d.setUtrib(p.getUtrib().getSigla());
		}		
		d.setqTrib(quant);
		d.setVuntrib(vUn);
		
		/**************   IPI  Inicio ****************************/
		IPI ipi = t.getIpi();
		if (ipi.getTpCalcIPI() == TpCalcIPI.ALIQUOTA) {
			ipi.setvBCIPI(i.getSubtotal());
			ipi.setvIPI(calculoPorcentagem(ipi.getvBCIPI(), ipi.getpIPI()));
		}
		
		if (ipi.getTpCalcIPI() == TpCalcIPI.UNIDADE) {
			ipi.setqUnid(quant.multiply(ipi.getqUnid()));;
			ipi.setvIPI(ipi.getqUnid().multiply(ipi.getvUnid()));
		}
		d.setIpi(ipi);
		/**************   IPI   Fim ****************************/
		
		/**************   ICMS Inicio **************************/
		ICMS icms = t.getIcms();
		icms.setvICMS(BigDecimal.ZERO);
		icms.setvICMSST(BigDecimal.ZERO);
		icms.setvBCST(BigDecimal.ZERO);
		if (icms.getModBCICMS() == ModBC.OPERACAO) {
			icms.setvBCICMS(i.getSubtotal());
			icms.setvICMS(calculoPorcentagem(icms.getvBCICMS(), icms.getpICMS()));
		}		
		if(ipi.getvIpiBcICMS()){
			icms.setvBCICMS(icms.getvBCICMS().add(ipi.getvIPI()));
			icms.setvICMS(calculoPorcentagem(icms.getvBCICMS(), icms.getpICMS()));
		}

		if (icms.getModBCST() == ModBCST.OPERACAO) {
			BigDecimal vAgreg = calculoPorcentagem(subtotal, icms.getpMVAST());

			icms.setvBCST(subtotal.add(vAgreg));
			icms.setvICMSST(calculoPorcentagem(icms.getvBCST(),
			icms.getpICMSST()).subtract(icms.getvICMS()));
		}
		d.setIcms(icms);
		/**************   ICMS Fim  **************************/
		
		PIS pis = t.getPis();
		d.setPis(pis);
		
		PISST pisST = t.getPisST();
		d.setPisST(pisST);
		
		COFINS cofins = t.getCofins();
		d.setCofins(cofins);
		
		COFINSST cofinsST = t.getCofinsST();
		d.setCofinsST(cofinsST);
		
		i.setDetFiscal(d);

		return i;
	}
	
	private BigDecimal calculoPorcentagem(BigDecimal valor, BigDecimal aliquota) {
		if(valor == null) {
			valor = BigDecimal.ZERO;
		}
		if(aliquota == null) {
			aliquota = BigDecimal.ZERO;
		}
		BigDecimal result = valor.multiply(aliquota).divide(
				new BigDecimal("100"), MathContext.DECIMAL128);
		return result;
	}
	
	private Destino getDestino(Tributacao trib, UF uf){
		for(Destino d: trib.getDestinos()){
		if(d.getEstado().equals(uf)){
			return d;
		}
	}	
	return null;
	}
}

