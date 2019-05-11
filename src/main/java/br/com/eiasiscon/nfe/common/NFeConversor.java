package br.com.eiasiscon.nfe.common;

import java.math.BigDecimal;

import br.com.eiasiscon.contato.Contato;
import br.com.eiasiscon.contato.IndIEDest;
import br.com.eiasiscon.contato.TpDoc;
import br.com.eiasiscon.empresa.Empresa;
import br.com.eiasiscon.notafiscal.NotaFiscal;
import br.com.eiasiscon.notafiscal.cobranca.Cobranca;
import br.com.eiasiscon.notafiscal.cobranca.Duplicata;
import br.com.eiasiscon.notafiscal.item.DetalheFiscal;
import br.com.eiasiscon.notafiscal.item.ItemNotaFiscal;
import br.com.eiasiscon.notafiscal.pagamento.DetPag;
import br.com.eiasiscon.notafiscal.transporte.ModFrete;
import br.com.eiasiscon.notafiscal.transporte.Transporte;
import br.com.eiasiscon.notafiscal.transporte.Veiculo;
import br.com.eiasiscon.produto.tributacao.cofins.CST_COFINS;
import br.com.eiasiscon.produto.tributacao.cofins.TpCalcCOFINS;
import br.com.eiasiscon.produto.tributacao.icms.CST_ICMS;
import br.com.eiasiscon.produto.tributacao.ipi.CST_IPI;
import br.com.eiasiscon.produto.tributacao.ipi.IPI;
import br.com.eiasiscon.produto.tributacao.ipi.TpCalcIPI;
import br.com.eiasiscon.produto.tributacao.pis.CST_PIS;
import br.com.eiasiscon.produto.tributacao.pis.TpCalcPIS;
import br.com.eiasiscon.util.ConversorBigDecimal;
import br.com.eiasiscon.util.ConversorDate;
import br.com.swconsultoria.nfe.schema_4.enviNFe.ObjectFactory;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnderEmi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEndereco;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TIpi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TIpi.IPINT;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TIpi.IPITrib;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Cobr;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Cobr.Dup;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Cobr.Fat;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Dest;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSOutr;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSQtde;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINSST;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS00;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS10;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS20;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS30;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS40;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS51;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS60;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS70;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMS90;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS.PISAliq;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS.PISNT;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS.PISOutr;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS.PISQtde;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PISST;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Prod;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Emit;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Ide;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Ide.NFref;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.InfAdic;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Pag;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Total;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Total.ICMSTot;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Transp;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Transp.Transporta;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Transp.Vol;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TUf;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TUfEmi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TVeiculo;

public class NFeConversor {

    private static String ambiente;

    public static TNFe getnFe(NotaFiscal nf, String ambient) {
        ambiente = ambient;
        TNFe nFe = new TNFe();
        nFe.setInfNFe(getInfNFe(nf));
        return nFe;
    }

    private static InfNFe getInfNFe(NotaFiscal nf) {
        InfNFe infNFe = new InfNFe();
        infNFe.setId("NFe" + nf.getChave());
        infNFe.setVersao(nf.getVersao().toString());

        infNFe.setIde(dadosDeIdentificacao(nf));
        infNFe.setEmit(dadosDoEmitente(nf));
        infNFe.setDest(dadosDoDestinatario(nf));
        int item = 0;
        for (ItemNotaFiscal i : nf.getItens()) {
        	item++;
        	i.setnItem(item);
            infNFe.getDet().add(dadosDoProduto(i));
        }
        infNFe.setTotal(totaisDaNFe(nf));
        infNFe.setTransp(dadosDoTransporte(nf));
        infNFe.setInfAdic(informacoesAdicionais(nf));
        
        infNFe.setPag(getPag(nf));

        infNFe.setCobr(getCobr(nf));

        return infNFe;
    }

    private static Ide dadosDeIdentificacao(NotaFiscal nf) {
        Ide ide = new Ide();
        ide.setCUF(nf.getEmitente().getMunicipio().getUF().getCUF());

        String dEmis = ConversorDate.retornaDataHora(nf.getDhEmi()).replaceAll("[^0-9]", "");
        
        ide.setCNF(dEmis.substring(0, 2) + dEmis.substring(8, 14));

        ide.setNatOp(nf.getNatOp().trim());
        ide.setMod(nf.getMod().toString());
        ide.setSerie(nf.getSerie().toString());
        ide.setNNF(nf.getNumero().toString());
        ide.setDhEmi(ConversorDate.retornaDataHoraNFe(nf.getDhEmi()));
        ide.setDhSaiEnt(ConversorDate.retornaDataHoraNFe(nf.getDhSaiEnt()));
        ide.setTpNF(nf.getTpNF().getValor());
        ide.setIdDest(nf.getIdDest().getValor());
        ide.setCMunFG(String.valueOf(nf.getEmitente().getMunicipio().getcMun()));
        ide.setTpImp(nf.getTpImp().getValor());
        ide.setTpEmis(nf.getTpEmis());
        ide.setCDV(nf.getChave().substring(43));
        ide.setTpAmb(ambiente);
        ide.setFinNFe(nf.getFinNFe().getValor());
        ide.setIndFinal(nf.getIndFinal().getValor());
        ide.setIndPres(nf.getIndPres().getValor());
        ide.setProcEmi(nf.getProcEmi());
        ide.setVerProc("0");
        if (nf.getNFRef() != null) {
            for (br.com.eiasiscon.notafiscal.ide.ref.NFref nfRef : nf.getNFRef()) {
                NFref nRef = new NFref();
                nRef.setRefNFe(nfRef.getRefNFe().trim());
                ide.getNFref().add(nRef);
            }
        }
        return ide;
    }

    private static Emit dadosDoEmitente(NotaFiscal nf) {
        Empresa em = nf.getEmitente();
        Emit emit = new Emit();
        if(em.getNumDoc()!=null){
        	if(em.getTpDoc().equals(TpDoc.CNPJ)){
        		emit.setCNPJ(em.getNumDoc().replaceAll("[^0-9]", ""));
        	}
        	if(em.getTpDoc().equals(TpDoc.CPF)){
        		emit.setCPF(em.getNumDoc().replaceAll("[^0-9]", ""));
        	}
        	
        }
        
        emit.setXNome(em.getNome().trim());
        if(em.getFantasia()!=null){
        	 emit.setXFant(em.getFantasia().trim());
        }
       
        TEnderEmi enderEmit = new TEnderEmi();
        enderEmit.setXLgr(em.getLogradouro().trim());
        enderEmit.setNro(em.getNumero().trim());
        enderEmit.setXBairro(em.getBairro().trim());
        if(em.getComplemento()!=null){
        	enderEmit.setXCpl(em.getComplemento().trim());
        }        
        enderEmit.setCMun(String.valueOf(em.getMunicipio().getcMun()));
        enderEmit.setXMun(em.getMunicipio().getxMun());
        enderEmit.setUF(TUfEmi.valueOf(em.getMunicipio().getUF().toString()));
        if (em.getCep() != null) {
            enderEmit.setCEP(em.getCep().replaceAll("[^0-9]", ""));
        }
        enderEmit.setCPais(String.valueOf(em.getPais().getcPais()));
        enderEmit.setXPais(em.getPais().getxPais());
        if (em.getFone() != null) {
            enderEmit.setFone(em.getFone().replaceAll("[^0-9]", ""));
        }
        emit.setEnderEmit(enderEmit);
        if(em.getIE()!=null){
        	emit.setIE(em.getIE().replaceAll("[^0-9]", ""));
        }
        
        emit.setCRT(em.getCrt().getValue());
        return emit;
    }

    private static Dest dadosDoDestinatario(NotaFiscal nf) {
        Contato c = nf.getDest();
        Dest dest = new Dest();
        if(c.getNumDoc()!=null){
        	if(c.getTpDoc() !=null) {
        		if(c.getTpDoc().equals(TpDoc.CNPJ)){
            		dest.setCNPJ(c.getNumDoc().replaceAll("[^0-9]", ""));
            	}
            	if(c.getTpDoc().equals(TpDoc.CPF)){
            		dest.setCPF(c.getNumDoc().replaceAll("[^0-9]", ""));
            	}
        	}        	
        }
        dest.setCNPJ(c.getNumDoc().replaceAll("[^0-9]", ""));
        dest.setXNome(c.getNome().trim());

        TEndereco enderDest = new TEndereco();
        enderDest.setXLgr(c.getLogradouro().trim());
        if(c.getNumero() == null) {
        	c.setNumero("");
        }
        enderDest.setNro(c.getNumero().trim());
        if(c.getBairro() == null) {
        	enderDest.setXBairro("");
        } else {
        	enderDest.setXBairro(c.getBairro().trim());
        }
        
        if(c.getComplemento()!=null){
        	enderDest.setXCpl(c.getComplemento().trim());
        }
        if(c.getComplemento()==null){
        	enderDest.setXCpl(null);
        }else if(c.getComplemento().isEmpty()){
        	enderDest.setXCpl(null);
        }
        enderDest.setCMun(String.valueOf(c.getMunicipio().getcMun()));
        enderDest.setXMun(c.getMunicipio().getxMun());
        enderDest.setUF(TUf.valueOf(c.getMunicipio().getUF().toString()));
        
        enderDest.setCEP(c.getCep().replaceAll("[^0-9]", ""));
        enderDest.setCPais(String.valueOf(c.getPais().getcPais()));
        enderDest.setXPais(c.getPais().getxPais());
        if (c.getFone() != null) {
            enderDest.setFone(c.getFone().replaceAll("[^0-9]", ""));
        }
        dest.setEnderDest(enderDest);
        
        if (c.getIE() != null) {
        	String ie = c.getIE();
        	if(ie.toUpperCase().equals("ISENTO")) {
        		dest.setIndIEDest(IndIEDest.ISENTO.getValue());
        	} else if(ie.replaceAll("[^0-9]", "").length() > 0) {
        		dest.setIndIEDest(IndIEDest.CONTRIBUINTE.getValue());
        		dest.setIE(ie.replaceAll("[^0-9]", ""));
        	} else {
        		dest.setIndIEDest(IndIEDest.NAO_CONTRIBUINTE.getValue());
        	}
        } else{
            dest.setIndIEDest(IndIEDest.NAO_CONTRIBUINTE.getValue());
        }
        
        if(c.getEmail()!=null){
        	dest.setEmail(c.getEmail().trim());
        }
        
        return dest;
    }

    private static Det dadosDoProduto(ItemNotaFiscal i) {
        Det det = new Det();
        det.setNItem(String.valueOf(i.getnItem()));

        /**
         * Dados do Produro
         */
        Prod prod = new Prod();
        prod.setCProd(i.getProduto().getCodigo());
        prod.setCEAN(i.getProduto().getcEan());
        if(i.getProduto().getcEan()==null){
            prod.setCEAN("");
        }
        prod.setXProd(i.getProduto().getDescricao().trim());
        prod.setNCM(i.getProduto().getNcm());
        prod.setCEST((i.getProduto().getCest()));
        prod.setCFOP(i.getDetFiscal().getCfop());
        prod.setUCom(i.getProduto().getUnidade().getSigla());
        prod.setQCom(ConversorBigDecimal.paraStringNFeQuant(i.getQuantidade()));
        prod.setVUnCom(ConversorBigDecimal.paraStringNFePreco(i.getPrecoVenda()));
        prod.setVProd(ConversorBigDecimal.paraStringNFeValor(i.getSubtotal()));
        prod.setUTrib(i.getProduto().getUtrib().getSigla());
        prod.setQTrib(ConversorBigDecimal.paraStringNFeQuant(i.getDetFiscal().getqTrib()));
        prod.setCEANTrib(i.getProduto().getcEanTrib());
        if(i.getProduto().getcEanTrib()==null){
            prod.setCEANTrib("");
        }
        prod.setVUnTrib(ConversorBigDecimal.paraStringNFePreco(i.getDetFiscal().getVuntrib()));
        prod.setIndTot("1");
        det.setProd(prod);

        /**
         * Impostos da NF-e
         */
        Imposto imposto = new Imposto();
        ICMS icms = getICMS(i);

        TIpi ipi = getIPI(i);

        PIS pis = getPIS(i);

        PISST pisST = getPISST(i);

        COFINS cofins = getCOFINS(i);

        COFINSST cofinsSt = getcofinsST(i);

        imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms));
        imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoIPI(ipi));
        imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));
        if (pisST != null) {
            imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPISST(pisST));
        }
        imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINS(cofins));
        if (cofinsSt != null) {
            imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINSST(cofinsSt));
        }

        det.setImposto(imposto);

        /**
         * Informações adicionais do Produto.
         */
        det.setInfAdProd(i.getIfAdic());

        return det;
    }

    private static ICMS getICMS(ItemNotaFiscal i) {
        DetalheFiscal d = i.getDetFiscal();
        ICMS icms = new ICMS();

        CST_ICMS ICMS_CST = d.getIcms().getCstICMS();
        switch (ICMS_CST) {
            case ICMS_00:
                ICMS00 _00 = new ICMS00();
                _00.setOrig(d.getIcms().getOrigem().getValor());
                _00.setCST(d.getIcms().getCstICMS().getValor());
                _00.setModBC(d.getIcms().getModBCICMS().getValor());
                _00.setVBC(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCICMS()));
                _00.setPICMS(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMS()));
                _00.setVICMS(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMS()));
                icms.setICMS00(_00);
                break;
            case ICMS_10:
                ICMS10 _10 = new ICMS10();
                _10.setOrig(d.getIcms().getOrigem().getValor());
                _10.setCST(d.getIcms().getCstICMS().getValor());
                _10.setModBC(d.getIcms().getModBCICMS().getValor());
                _10.setVBC(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCICMS()));
                _10.setPICMS(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMS()));
                _10.setVICMS(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMS()));
                _10.setModBCST(d.getIcms().getModBCST().getValor());
                _10.setPMVAST(d.getIcms().getpMVAST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpMVAST()) : null);
                _10.setPRedBCST(d.getIcms().getpRedBCST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCST()) : null);
                _10.setVBCST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCST()));
                _10.setPICMSST(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMSST()));
                _10.setVICMSST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMSST()));
                icms.setICMS10(_10);
                break;
            case ICMS_20:
                ICMS20 _20 = new ICMS20();
                _20.setOrig(d.getIcms().getOrigem().getValor());
                _20.setCST(d.getIcms().getCstICMS().getValor());
                _20.setModBC(d.getIcms().getModBCICMS().getValor());
                _20.setVBC(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCICMS()));
                _20.setPICMS(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMS()));
                _20.setVICMS(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMS()));
                _20.setVICMSDeson(d.getIcms().getvICMSDeson().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getvICMSDeson()) : null);
                _20.setMotDesICMS(d.getIcms().getMotDesICMS().getValor());
                icms.setICMS20(_20);
                break;
            case ICMS_30:
                ICMS30 _30 = new ICMS30();
                _30.setOrig(d.getIcms().getOrigem().getValor());
                _30.setCST(d.getIcms().getCstICMS().getValor());
                _30.setVICMSDeson(d.getIcms().getvICMSDeson().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getvICMSDeson()) : null);
                _30.setMotDesICMS(d.getIcms().getMotDesICMS().getValor());
                _30.setModBCST(d.getIcms().getModBCST().getValor());
                _30.setPMVAST(d.getIcms().getpMVAST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpMVAST()) : null);
                _30.setPRedBCST(d.getIcms().getpRedBCST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCST()) : null);
                _30.setVBCST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCST()));
                _30.setPICMSST(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMSST()));
                _30.setVICMSST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMSST()));
                icms.setICMS30(_30);
                break;
            case ICMS_40:
                ICMS40 _40 = new ICMS40();
                _40.setOrig(d.getIcms().getOrigem().getValor());
                _40.setCST(d.getIcms().getCstICMS().getValor());
                _40.setVICMSDeson(d.getIcms().getvICMSDeson().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMSDeson()) : null);
                _40.setMotDesICMS(d.getIcms().getMotDesICMS().getValor());
                icms.setICMS40(_40);
                break;
            case ICMS_41:
                ICMS40 _41 = new ICMS40();
                _41.setOrig(d.getIcms().getOrigem().getValor());
                _41.setCST(d.getIcms().getCstICMS().getValor());
                _41.setVICMSDeson(d.getIcms().getvICMSDeson().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMSDeson()) : null);
                _41.setMotDesICMS(d.getIcms().getMotDesICMS().getValor());
                icms.setICMS40(_41);
                break;
            case ICMS_50:
                ICMS40 _50 = new ICMS40();
                _50.setOrig(d.getIcms().getOrigem().getValor());
                _50.setCST(d.getIcms().getCstICMS().getValor());
                _50.setVICMSDeson(d.getIcms().getvICMSDeson().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getvICMSDeson()) : null);
                _50.setMotDesICMS(d.getIcms().getMotDesICMS().getValor());
                icms.setICMS40(_50);
                break;
            case ICMS_51:
                ICMS51 _51 = new ICMS51();
                _51.setOrig(d.getIcms().getOrigem().getValor());
                _51.setCST(d.getIcms().getCstICMS().getValor());
                _51.setPRedBC(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCICMS()));
                _51.setVBC(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCICMS()));
                _51.setPICMS(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMS()));
                _51.setVICMSOp(null);
                _51.setPDif(null);
                _51.setVICMSDif(null);
                _51.setVICMS(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMS()));
                icms.setICMS51(_51);
                break;
            case ICMS_60:
                ICMS60 _60 = new ICMS60();
                _60.setOrig(d.getIcms().getOrigem().getValor());
                _60.setCST(d.getIcms().getCstICMS().getValor());
                _60.setVBCSTRet(null);
                _60.setVICMSSTRet(null);
                icms.setICMS60(_60);
                break;
            case ICMS_70:
                ICMS70 _70 = new ICMS70();
                _70.setOrig(d.getIcms().getOrigem().getValor());
                _70.setCST(d.getIcms().getCstICMS().getValor());
                _70.setModBC(d.getIcms().getModBCICMS().getValor());
                _70.setPRedBC(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCICMS()));
                _70.setVBC(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCICMS()));
                _70.setPICMS(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMS()));
                _70.setVICMS(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMS()));
                _70.setModBCST(d.getIcms().getModBCST().getValor());
                _70.setPMVAST(d.getIcms().getpMVAST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpMVAST()) : null);
                _70.setPRedBCST(d.getIcms().getpRedBCST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCST()) : null);
                _70.setVBCST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCST()));
                _70.setPICMSST(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMSST()));
                _70.setVICMSST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMSST()));
                _70.setVICMSDeson(d.getIcms().getvICMSDeson().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getvICMSDeson()) : null);
                _70.setMotDesICMS(d.getIcms().getMotDesICMS().getValor());
                icms.setICMS70(_70);
                break;
            case ICMS_90:
                ICMS90 _90 = new ICMS90();
                _90.setOrig(d.getIcms().getOrigem().getValor());
                _90.setCST(d.getIcms().getCstICMS().getValor());
                _90.setModBC(d.getIcms().getModBCICMS().getValor());
                _90.setPRedBC(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCICMS()));
                _90.setVBC(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCICMS()));
                _90.setPICMS(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMS()));
                _90.setVICMS(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMS()));
                _90.setModBCST(d.getIcms().getModBCST().getValor());
                _90.setPMVAST(d.getIcms().getpMVAST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpMVAST()) : null);
                _90.setPRedBCST(d.getIcms().getpRedBCST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCST()) : null);
                _90.setVBCST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCST()));
                _90.setPICMSST(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMSST()));
                _90.setVICMSST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMSST()));
                _90.setVICMSDeson(d.getIcms().getvICMSDeson().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getvICMSDeson()) : null);
                _90.setMotDesICMS(d.getIcms().getMotDesICMS().getValor());
                icms.setICMS90(_90);
                break;
            case ICMS_Part10:
                ICMSPart p10 = new ICMSPart();
                p10.setOrig(d.getIcms().getOrigem().getValor());
                p10.setCST(d.getIcms().getCstICMS().getValor());
                p10.setModBC(d.getIcms().getModBCICMS().getValor());
                p10.setPRedBC(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCICMS()));
                p10.setVBC(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCICMS()));
                p10.setPICMS(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMS()));
                p10.setVICMS(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMS()));
                p10.setModBCST(d.getIcms().getModBCST().getValor());
                p10.setPMVAST(d.getIcms().getpMVAST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpMVAST()) : null);
                p10.setPRedBCST(d.getIcms().getpRedBCST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCST()) : null);
                p10.setVBCST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCST()));
                p10.setPICMSST(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMSST()));
                p10.setVICMSST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMSST()));
                p10.setPBCOp(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpBCOp()));
                p10.setUFST(TUf.valueOf(d.getIcms().getUFST().toString()));
                icms.setICMSPart(p10);
                break;
            case ICMS_Part90:
                ICMSPart p90 = new ICMSPart();
                p90.setOrig(d.getIcms().getOrigem().getValor());
                p90.setCST(d.getIcms().getCstICMS().getValor());
                p90.setModBC(d.getIcms().getModBCICMS().getValor());
                p90.setPRedBC(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCICMS()));
                p90.setVBC(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCICMS()));
                p90.setPICMS(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMS()));
                p90.setVICMS(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMS()));
                p90.setModBCST(d.getIcms().getModBCST().getValor());
                p90.setPMVAST(d.getIcms().getpMVAST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpMVAST()) : null);
                p90.setPRedBCST(d.getIcms().getpRedBCST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCST()) : null);
                p90.setVBCST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCST()));
                p90.setPICMSST(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMSST()));
                p90.setVICMSST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMSST()));
                p90.setPBCOp(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpBCOp()));
                p90.setUFST(TUf.valueOf(d.getIcms().getUFST().toString()));
                icms.setICMSPart(p90);
                break;
            case SN_101:
                ICMSSN101 _101 = new ICMSSN101();
                _101.setOrig(d.getIcms().getOrigem().getValor());
                _101.setCSOSN(d.getIcms().getCstICMS().getValor());
                _101.setPCredSN(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpCredSN()));
                _101.setVCredICMSSN(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvCredICMSSN()));
                icms.setICMSSN101(_101);
                break;
            case SN_102:
                ICMSSN102 _102 = new ICMSSN102();
                _102.setOrig(d.getIcms().getOrigem().getValor());
                _102.setCSOSN(d.getIcms().getCstICMS().getValor());
                icms.setICMSSN102(_102);
                break;
            case SN_103:
                ICMSSN102 _103 = new ICMSSN102();
                _103.setOrig(d.getIcms().getOrigem().getValor());
                _103.setCSOSN(d.getIcms().getCstICMS().getValor());
                icms.setICMSSN102(_103);
                break;
            case SN_300:
                ICMSSN102 _300 = new ICMSSN102();
                _300.setOrig(d.getIcms().getOrigem().getValor());
                _300.setCSOSN(d.getIcms().getCstICMS().getValor());
                icms.setICMSSN102(_300);
                break;
            case SN_400:
                ICMSSN102 _400 = new ICMSSN102();
                _400.setOrig(d.getIcms().getOrigem().getValor());
                _400.setCSOSN(d.getIcms().getCstICMS().getValor());
                icms.setICMSSN102(_400);
                break;
            case SN_201:
                ICMSSN201 _201 = new ICMSSN201();
                _201.setOrig(d.getIcms().getOrigem().getValor());
                _201.setCSOSN(d.getIcms().getCstICMS().getValor());
                _201.setModBCST(d.getIcms().getModBCST().getValor());
                _201.setPMVAST(d.getIcms().getpMVAST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpMVAST()) : null);
                _201.setPRedBCST(d.getIcms().getpRedBCST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCST()) : null);
                _201.setVBCST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCST()));
                _201.setPICMSST(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMSST()));
                _201.setVICMSST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMSST()));
                _201.setPCredSN(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpCredSN()));
                _201.setVCredICMSSN(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvCredICMSSN()));
                icms.setICMSSN201(_201);
                break;
            case SN_202:
                ICMSSN202 _202 = new ICMSSN202();
                _202.setOrig(d.getIcms().getOrigem().getValor());
                _202.setCSOSN(d.getIcms().getCstICMS().getValor());
                _202.setModBCST(d.getIcms().getModBCST().getValor());
                
                if(d.getIcms().getpMVAST() == null) {
                	_202.setPMVAST(null);
                } else {
                	_202.setPMVAST(d.getIcms().getpMVAST().compareTo(BigDecimal.ZERO) == 1
                            ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpMVAST()) : null);
                }
                
                if(d.getIcms().getpRedBCST() == null) {
                	_202.setPMVAST(null);
                } else {
                	_202.setPRedBCST(d.getIcms().getpRedBCST().compareTo(BigDecimal.ZERO) == 1
                            ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCST()) : null);
                }
                
                _202.setVBCST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCST()));
                _202.setPICMSST(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMSST()));
                _202.setVICMSST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMSST()));
                icms.setICMSSN202(_202);
                break;
            case SN_203:
                ICMSSN202 _203 = new ICMSSN202();
                _203.setOrig(d.getIcms().getOrigem().getValor());
                _203.setCSOSN(d.getIcms().getCstICMS().getValor());
                _203.setModBCST(d.getIcms().getModBCST().getValor());
                _203.setPMVAST(d.getIcms().getpMVAST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpMVAST()) : null);
                _203.setPRedBCST(d.getIcms().getpRedBCST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCST()) : null);
                _203.setVBCST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCST()));
                _203.setPICMSST(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMSST()));
                _203.setVICMSST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMSST()));
                icms.setICMSSN202(_203);
                break;
            case SN_500:
                ICMSSN500 _500 = new ICMSSN500();
                _500.setOrig(d.getIcms().getOrigem().getValor());
                _500.setCSOSN(d.getIcms().getCstICMS().getValor());
                icms.setICMSSN500(_500);
                break;
            case SN_900:
                ICMSSN900 _900 = new ICMSSN900();
                _900.setOrig(d.getIcms().getOrigem().getValor());
                _900.setCSOSN(d.getIcms().getCstICMS().getValor());
                if (d.getIcms().getModBCICMS() != null) {
                    _900.setModBC(d.getIcms().getModBCICMS().getValor());
                }
                _900.setPRedBC(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCICMS()));
                _900.setVBC(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCICMS()));
                _900.setPICMS(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMS()));
                _900.setVICMS(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMS()));
                if (d.getIcms().getModBCST() != null) {
                    _900.setModBCST(d.getIcms().getModBCST().getValor());
                }
                _900.setPMVAST(d.getIcms().getpMVAST().compareTo(BigDecimal.ZERO) == 1
                        ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpMVAST()) : null);
                if (d.getIcms().getpRedBCST() != null) {
                    _900.setPRedBCST(d.getIcms().getpRedBCST().compareTo(BigDecimal.ZERO) == 1
                            ? ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpRedBCST()) : null);
                }
                _900.setVBCST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvBCST()));
                _900.setPICMSST(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpICMSST()));
                _900.setVICMSST(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvICMSST()));
                _900.setPCredSN(ConversorBigDecimal.paraStringNFeAliq(d.getIcms().getpCredSN()));
                _900.setVCredICMSSN(ConversorBigDecimal.paraStringNFeValor(d.getIcms().getvCredICMSSN()));
                icms.setICMSSN900(_900);
                break;
            default:
                break;
        }
        return icms;
    }

    private static TIpi getIPI(ItemNotaFiscal i) {
        IPI ip = i.getDetFiscal().getIpi();
        TIpi ipi = new TIpi();
        ipi.setCNPJProd(ip.getCNPJProd());
        ipi.setCSelo(ip.getcSelo());
        ipi.setQSelo(ConversorBigDecimal.paraStringNFeQuant(ip.getqSelo()));
        if (ipi.getQSelo().equals("0")) {
            ipi.setQSelo(null);
        }
        ipi.setCEnq(ip.getcEnq());
        if(ip.getcEnq()==null){
            ipi.setCEnq("999");
        }
        CST_IPI CST_IPI = ip.getCstIPI();

        IPITrib ipiTrib = new IPITrib();
        TpCalcIPI tpCalc = ip.getTpCalcIPI();

        IPINT ipiNT = new IPINT();

        switch (CST_IPI) {
            case IPI_00:
                ipiTrib.setCST(ip.getCstIPI().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        ipiTrib.setVBC(ConversorBigDecimal.paraStringNFeValor(ip.getvBCIPI()));
                        ipiTrib.setPIPI(ConversorBigDecimal.paraStringNFeAliq(ip.getpIPI()));
                        break;
                    case UNIDADE:
                        ipiTrib.setQUnid(ConversorBigDecimal.paraStringNFeQuant(ip.getqUnid()));
                        ipiTrib.setVUnid(ConversorBigDecimal.paraStringNFePreco(ip.getvUnid()));
                        break;
                    default:
                        break;
                }
                ipiTrib.setVIPI(ConversorBigDecimal.paraStringNFeValor(ip.getvIPI()));
                ipi.setIPITrib(ipiTrib);
                break;
            case IPI_49:
                ipiTrib.setCST(ip.getCstIPI().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        ipiTrib.setVBC(ConversorBigDecimal.paraStringNFeValor(ip.getvBCIPI()));
                        ipiTrib.setPIPI(ConversorBigDecimal.paraStringNFeAliq(ip.getpIPI()));
                        break;
                    case UNIDADE:
                        ipiTrib.setQUnid(ConversorBigDecimal.paraStringNFeQuant(ip.getqUnid()));
                        ipiTrib.setVUnid(ConversorBigDecimal.paraStringNFePreco(ip.getvUnid()));
                        break;
                    default:
                        break;
                }
                ipiTrib.setVIPI(ConversorBigDecimal.paraStringNFeValor(ip.getvIPI()));
                ipi.setIPITrib(ipiTrib);
                break;
            case IPI_50:
                ipiTrib.setCST(ip.getCstIPI().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        ipiTrib.setVBC(ConversorBigDecimal.paraStringNFeValor(ip.getvBCIPI()));
                        ipiTrib.setPIPI(ConversorBigDecimal.paraStringNFeAliq(ip.getpIPI()));
                        break;
                    case UNIDADE:
                        ipiTrib.setQUnid(ConversorBigDecimal.paraStringNFeQuant(ip.getqUnid()));
                        ipiTrib.setVUnid(ConversorBigDecimal.paraStringNFePreco(ip.getvUnid()));
                        break;
                    default:
                        break;
                }
                ipiTrib.setVIPI(ConversorBigDecimal.paraStringNFeValor(ip.getvIPI()));
                ipi.setIPITrib(ipiTrib);
                break;
            case IPI_99:
                ipiTrib.setCST(ip.getCstIPI().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        ipiTrib.setVBC(ConversorBigDecimal.paraStringNFeValor(ip.getvBCIPI()));
                        ipiTrib.setPIPI(ConversorBigDecimal.paraStringNFeAliq(ip.getpIPI()));
                        break;
                    case UNIDADE:
                        ipiTrib.setQUnid(ConversorBigDecimal.paraStringNFeQuant(ip.getqUnid()));
                        ipiTrib.setVUnid(ConversorBigDecimal.paraStringNFePreco(ip.getvUnid()));
                        break;
                    default:
                        break;
                }
                ipiTrib.setVIPI(ConversorBigDecimal.paraStringNFeValor(ip.getvIPI()));
                ipi.setIPITrib(ipiTrib);
                break;
            case IPI_01:
                ipiNT.setCST(ip.getCstIPI().getValor());
                ipi.setIPINT(ipiNT);
                break;
            case IPI_02:
                ipiNT.setCST(ip.getCstIPI().getValor());
                ipi.setIPINT(ipiNT);
                break;
            case IPI_03:
                ipiNT.setCST(ip.getCstIPI().getValor());
                ipi.setIPINT(ipiNT);
                break;
            case IPI_04:
                ipiNT.setCST(ip.getCstIPI().getValor());
                ipi.setIPINT(ipiNT);
                break;
            case IPI_05:
                ipiNT.setCST(ip.getCstIPI().getValor());
                ipi.setIPINT(ipiNT);
                break;
            case IPI_51:
                ipiNT.setCST(ip.getCstIPI().getValor());
                ipi.setIPINT(ipiNT);
                break;
            case IPI_52:
                ipiNT.setCST(ip.getCstIPI().getValor());
                ipi.setIPINT(ipiNT);
                break;
            case IPI_53:
                ipiNT.setCST(ip.getCstIPI().getValor());
                ipi.setIPINT(ipiNT);
                break;
            case IPI_54:
                ipiNT.setCST(ip.getCstIPI().getValor());
                ipi.setIPINT(ipiNT);
                break;
            case IPI_55:
                ipiNT.setCST(ip.getCstIPI().getValor());
                ipi.setIPINT(ipiNT);
                break;

            default:
                break;
        }

        return ipi;
    }

    private static PIS getPIS(ItemNotaFiscal i) {
        PIS pis = new PIS();
        br.com.eiasiscon.produto.tributacao.pis.PIS p = i.getDetFiscal().getPis();
        CST_PIS cst = p.getCstPIS();
        PISAliq pA = new PISAliq();
        PISQtde pQ = new PISQtde();
        PISNT pN = new PISNT();
        PISOutr pO = new PISOutr();

        TpCalcPIS tpCalc = p.getTpCalcPis();

        switch (cst) {
            case PIS_01:
                pA.setCST(p.getCstPIS().getValor());
                pA.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                pA.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                pA.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISAliq(pA);
                break;
            case PIS_02:
                pA.setCST(p.getCstPIS().getValor());
                pA.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                pA.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                pA.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISAliq(pA);
                break;
            case PIS_03:
                pQ.setCST(p.getCstPIS().getValor());
                pQ.setQBCProd(ConversorBigDecimal.paraStringNFeQuant(p.getqBCProdPIS()));
                pQ.setVAliqProd(ConversorBigDecimal.paraStringNFePreco(p.getvAliqProdPIS()));
                pQ.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISQtde(pQ);
                break;
            case PIS_04:
                pN.setCST(p.getCstPIS().getValor());
                pis.setPISNT(pN);
                break;
            case PIS_05:
                pN.setCST(p.getCstPIS().getValor());
                pis.setPISNT(pN);
                break;
            case PIS_06:
                pN.setCST(p.getCstPIS().getValor());
                pis.setPISNT(pN);
                break;
            case PIS_07:
                pN.setCST(p.getCstPIS().getValor());
                pis.setPISNT(pN);
                break;
            case PIS_08:
                pN.setCST(p.getCstPIS().getValor());
                pis.setPISNT(pN);
                break;
            case PIS_09:
                pN.setCST(p.getCstPIS().getValor());
                pis.setPISNT(pN);
                break;
            case PIS_49:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_50:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_51:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_52:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_53:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_54:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_55:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_56:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_60:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_61:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_62:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_63:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_64:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;

                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_65:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;
                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_66:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;
                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_67:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;
                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_70:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;
                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_71:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;
                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_72:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;
                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_73:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;
                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_74:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;
                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_75:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;
                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_98:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;
                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;
            case PIS_99:
                pO.setCST(p.getCstPIS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPIS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPIS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPIS()));
                        break;
                    default:
                        break;
                }
                pO.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPIS()));
                pis.setPISOutr(pO);
                break;

            default:
                break;
        }

        return pis;
    }

    private static PISST getPISST(ItemNotaFiscal i) {
        br.com.eiasiscon.produto.tributacao.pis.PISST p = i.getDetFiscal().getPisST();
        if (p == null) {
            p = new br.com.eiasiscon.produto.tributacao.pis.PISST();
        }
        PISST pisST = new PISST();
        TpCalcPIS tpCalc = p.getTpCalcPISST();
        if (tpCalc == null) {
            tpCalc = TpCalcPIS.NA;
        }
        switch (tpCalc) {
            case ALIQUOTA:
                pisST.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCPISST()));
                pisST.setPPIS(ConversorBigDecimal.paraStringNFeAliq(p.getpPISST()));
                pisST.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPISST()));
                break;
            case UNIDADE:
                pisST.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCPISST()));
                pisST.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdPISST()));
                pisST.setVPIS(ConversorBigDecimal.paraStringNFeValor(p.getvPISST()));
                break;

            default:
                return null;
        }

        return pisST;
    }

    private static COFINS getCOFINS(ItemNotaFiscal i) {
        COFINS cofins = new COFINS();
        br.com.eiasiscon.produto.tributacao.cofins.COFINS p = i.getDetFiscal().getCofins();
        CST_COFINS cst = p.getCstCOFINS();
        COFINSAliq pA = new COFINSAliq();
        COFINSQtde pQ = new COFINSQtde();
        COFINSNT pN = new COFINSNT();
        COFINSOutr pO = new COFINSOutr();

        TpCalcCOFINS tpCalc = p.getTpCalcCofins();

        switch (cst) {
            case COFINS_01:
                pA.setCST(p.getCstCOFINS().getValor());
                pA.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                pA.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                pA.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSAliq(pA);
                break;
            case COFINS_02:
                pA.setCST(p.getCstCOFINS().getValor());
                pA.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                pA.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                pA.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSAliq(pA);
                break;
            case COFINS_03:
                pQ.setCST(p.getCstCOFINS().getValor());
                pQ.setQBCProd(ConversorBigDecimal.paraStringNFeQuant(p.getqBCProdCOFINS()));
                pQ.setVAliqProd(ConversorBigDecimal.paraStringNFePreco(p.getvAliqProdCOFINS()));
                pQ.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSQtde(pQ);
                break;
            case COFINS_04:
                pN.setCST(p.getCstCOFINS().getValor());
                cofins.setCOFINSNT(pN);
                break;
            case COFINS_05:
                pN.setCST(p.getCstCOFINS().getValor());
                cofins.setCOFINSNT(pN);
                break;
            case COFINS_06:
                pN.setCST(p.getCstCOFINS().getValor());
                cofins.setCOFINSNT(pN);
                break;
            case COFINS_07:
                pN.setCST(p.getCstCOFINS().getValor());
                cofins.setCOFINSNT(pN);
                break;
            case COFINS_08:
                pN.setCST(p.getCstCOFINS().getValor());
                cofins.setCOFINSNT(pN);
                break;
            case COFINS_09:
                pN.setCST(p.getCstCOFINS().getValor());
                cofins.setCOFINSNT(pN);
                break;
            case COFINS_49:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_50:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_51:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_52:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_53:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_54:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_55:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_56:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_60:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_61:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_62:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_63:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_64:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;

                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_65:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;
                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_66:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;
                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_67:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;
                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_70:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;
                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_71:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;
                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_72:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;
                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_73:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;
                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_74:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;
                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_75:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;
                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_98:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;
                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;
            case COFINS_99:
                pO.setCST(p.getCstCOFINS().getValor());
                switch (tpCalc) {
                    case ALIQUOTA:
                        pO.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINS()));
                        break;
                    case UNIDADE:
                        pO.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINS()));
                        pO.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINS()));
                        break;
                    default:
                        break;
                }
                pO.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINS()));
                cofins.setCOFINSOutr(pO);
                break;

            default:
                break;
        }

        return cofins;
    }

    private static COFINSST getcofinsST(ItemNotaFiscal i) {
    	br.com.eiasiscon.produto.tributacao.cofins.COFINSST p = i.getDetFiscal().getCofinsST();
        if (p == null) {
            p = new br.com.eiasiscon.produto.tributacao.cofins.COFINSST();
        }
        COFINSST cofinsSTST = new COFINSST();
        TpCalcCOFINS tpCalc = p.getTpCalcCOFINSST();
        if (tpCalc == null) {
            tpCalc = TpCalcCOFINS.NA;
        }
        switch (tpCalc) {
            case ALIQUOTA:
                cofinsSTST.setVBC(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINSST()));
                cofinsSTST.setPCOFINS(ConversorBigDecimal.paraStringNFeAliq(p.getpCOFINSST()));
                cofinsSTST.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINSST()));
                break;
            case UNIDADE:
                cofinsSTST.setQBCProd(ConversorBigDecimal.paraStringNFeValor(p.getvBCCOFINSST()));
                cofinsSTST.setVAliqProd(ConversorBigDecimal.paraStringNFeQuant(p.getvAliqProdCOFINSST()));
                cofinsSTST.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(p.getvCOFINSST()));
                break;

            default:
                return null;
        }
        return cofinsSTST;
    }

    private static Total totaisDaNFe(NotaFiscal nf) {
        br.com.eiasiscon.notafiscal.total.Total t = nf.getTotal();
        Total total = new Total();

        ICMSTot icmstot = new ICMSTot();
        icmstot.setVBC(ConversorBigDecimal.paraStringNFeValor(t.getVbc()));
        icmstot.setVICMS(ConversorBigDecimal.paraStringNFeValor(t.getVicms()));
        icmstot.setVBCST(ConversorBigDecimal.paraStringNFeValor(t.getVbcst()));
        icmstot.setVST(ConversorBigDecimal.paraStringNFeValor(t.getVst()));
        icmstot.setVProd(ConversorBigDecimal.paraStringNFeValor(t.getvProd()));
        icmstot.setVFrete(ConversorBigDecimal.paraStringNFeValor(t.getvFrete()));
        icmstot.setVSeg(ConversorBigDecimal.paraStringNFeValor(t.getvSeg()));
        icmstot.setVDesc(ConversorBigDecimal.paraStringNFeValor(t.getvDesc()));
        icmstot.setVII(ConversorBigDecimal.paraStringNFeValor(t.getVii()));
        icmstot.setVIPI(ConversorBigDecimal.paraStringNFeValor(t.getVipi()));
        icmstot.setVPIS(ConversorBigDecimal.paraStringNFeValor(t.getVpis()));
        icmstot.setVCOFINS(ConversorBigDecimal.paraStringNFeValor(t.getVcofins()));
        icmstot.setVOutro(ConversorBigDecimal.paraStringNFeValor(t.getvOutro()));
        icmstot.setVNF(ConversorBigDecimal.paraStringNFeValor(t.getVnf()));
        icmstot.setVICMSDeson("0.00");
        icmstot.setVFCP("0.00");
        icmstot.setVFCPST("0.00");
        icmstot.setVFCPSTRet("0.00");
        icmstot.setVIPIDevol("0.00");
        total.setICMSTot(icmstot);

        return total;
    }

    private static Transp dadosDoTransporte(NotaFiscal nf) {
        Transporte t = nf.getTransp();
        Transp transp = new Transp();
        if (t.getModFrete() == null) {
            t.setModFrete(ModFrete.SEM_FRETE);
        }
        transp.setModFrete(t.getModFrete().getValor());

        /**
         * Dados da Transportadora.
         */
        
        Transporta transporta = new Transporta();
        if (t.getTransporta() != null) {
            if(t.getTransporta().getNumDoc()!=null ){
                transporta.setCNPJ(t.getTransporta().getNumDoc().replaceAll("[^0-9]", ""));
            }
            transporta.setXNome(t.getTransporta().getNome());
            if (t.getTransporta().getIE() != null) {
                transporta.setIE(t.getTransporta().getIE()
                        .replaceAll("[^0-9]", ""));
            }
            if(t.getTransporta().getIE()==null){
            	transporta.setIE(null);
            }
            if(t.getTransporta().getLogradouro()==null){
                transporta.setXEnder(null);
            }else{
                transporta.setXEnder(t.getTransporta().getLogradouro());
            }
            
            transporta.setXMun(t.getTransporta().getMunicipio().getxMun());
            
            if (t.getTransporta().getMunicipio().getUF() != null ) {
            	transporta.setUF(TUf.valueOf(t.getTransporta().getMunicipio().getUF().toString()));
            }
            
            if(transporta.getXNome() != null) {
            	transp.setTransporta(transporta);
            }
			
		}


        /**
         * Dados do Veiculo.
         */
        TVeiculo veicTransp = new TVeiculo();
        if (t.getVeicTransp().getUf() != null) {
            veicTransp.setPlaca(t.getVeicTransp().getPlaca());
            veicTransp.setUF(TUf.valueOf(t.getVeicTransp().getUf().toString()));
            veicTransp.setRNTC(t.getVeicTransp().getRntc());
            if(t.getVeicTransp().getRntc() != null){
            	if(t.getVeicTransp().getRntc().isEmpty()){
                	veicTransp.setRNTC(null);
                }
            }
            transp.setVeicTransp(veicTransp);
        }
        if (t.getReboque() != null) {
            for (Veiculo v : t.getReboque()) {
                TVeiculo reboque = new TVeiculo();
                reboque.setPlaca(v.getPlaca());
                reboque.setUF(TUf.valueOf(v.getUf().toString()));
                reboque.setRNTC(v.getRntc());
                if(v.getRntc().isEmpty()){
                	reboque.setRNTC(null);
                }
                transp.getReboque().add(reboque);
            }
            if (t.getModFrete().equals(ModFrete.SEM_FRETE)) {
    			transp.setTransporta(null);
    			transp.setVeicTransp(null);
    		}
        }

        /**
         * Dados de Volumes.
         */
        Vol vol = new Vol();
        vol.setQVol(null);
        vol.setNVol(null);
        if(t.getVol()!=null) {
        	vol.setPesoL(ConversorBigDecimal.paraStringNFePeso(t.getVol().get(0).getPesoL()));
            vol.setPesoB(ConversorBigDecimal.paraStringNFePeso(t.getVol().get(0).getPesoB()));
        }        
        transp.getVol().add(vol);

        return transp;
    }

    private static InfAdic informacoesAdicionais(NotaFiscal nf) {
        InfAdic infAdic = new InfAdic();
        
        if(nf.getInfAdic()!=null){
        if (nf.getInfAdic().getInfAdFisco() != null) {
            infAdic.setInfAdFisco(nf.getInfAdic().getInfAdFisco().trim().replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", ""));
            if (infAdic.getInfAdFisco().isEmpty()) {
                infAdic.setInfAdFisco(null);
            }
        }
        if (nf.getInfAdic().getInfCpl() != null) {
            infAdic.setInfCpl(nf.getInfAdic().getInfCpl());
            if (infAdic.getInfCpl().isEmpty()) {
                infAdic.setInfCpl(null);
            }
        }
        }
        if (infAdic.getInfAdFisco() == null && infAdic.getInfCpl() == null) {
            return null;
        }
        return infAdic;
    }

    private static Pag getPag(NotaFiscal nf) {
    	Pag pag = new Pag();    	
    	for(DetPag d : nf.getPag().getDetPag()) {
    		Pag.DetPag detPag = new Pag.DetPag();
    		if(d.getIndPag() != null) {
    			detPag.setIndPag(d.getIndPag().getValor());
    		}
            detPag.setTPag(d.gettPag().getValor());
            detPag.setVPag(ConversorBigDecimal.paraStringNFeValor(d.getvPag()));
            
            pag.getDetPag().add(detPag);
    	}
        return pag;
    }
    
    private static Cobr getCobr(NotaFiscal nf) {
        Cobranca c = nf.getCobr();
        Cobr cobr = new Cobr();

        Fat fat = new Fat();
        if(c != null) {
        	fat.setNFat(c.getFat().getnFat());
            fat.setVOrig(ConversorBigDecimal.paraStringNFeValor(c.getFat().getvOrig()));
            fat.setVDesc(ConversorBigDecimal.paraStringNFeValor(c.getFat().getvDesc()));
            fat.setVLiq(ConversorBigDecimal.paraStringNFeValor(c.getFat().getvLiq()));
            if (!fat.getNFat().isEmpty()) {
                cobr.setFat(fat);
            }
            
            if (c.getDup() != null) {
                for (Duplicata d : c.getDup()) {
                    Dup dup = new Dup();
                    dup.setNDup(d.getNumero());
                    dup.setDVenc(ConversorDate.retornaDataNFe(d.getVencimento()));
                    dup.setVDup(ConversorBigDecimal.paraStringNFeValor(d.getValor()));
                    cobr.getDup().add(dup);
                }
            }
        }

        return cobr;
    }

}