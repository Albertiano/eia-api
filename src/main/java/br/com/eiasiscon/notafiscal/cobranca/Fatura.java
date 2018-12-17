package br.com.eiasiscon.notafiscal.cobranca;

import java.math.BigDecimal;

public class Fatura {

	private String nFat;
	private BigDecimal vOrig;
	private BigDecimal vDesc;
	private BigDecimal vLiq;
	
	public String getnFat() {
		return nFat;
	}
	public void setnFat(String nFat) {
		this.nFat = nFat;
	}
	public BigDecimal getvOrig() {
		return vOrig;
	}
	public void setvOrig(BigDecimal vOrig) {
		this.vOrig = vOrig;
	}
	public BigDecimal getvDesc() {
		return vDesc;
	}
	public void setvDesc(BigDecimal vDesc) {
		this.vDesc = vDesc;
	}
	public BigDecimal getvLiq() {
		return vLiq;
	}
	public void setvLiq(BigDecimal vLiq) {
		this.vLiq = vLiq;
	}
	
}
