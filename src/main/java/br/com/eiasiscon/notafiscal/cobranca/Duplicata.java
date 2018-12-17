package br.com.eiasiscon.notafiscal.cobranca;

import java.math.BigDecimal;
import java.util.Date;

public class Duplicata {
	
	private String numero;
	private Date vencimento;
    private BigDecimal valor;
    
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Date getVencimento() {
		return vencimento;
	}
	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
