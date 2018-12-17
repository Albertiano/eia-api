/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.eiasiscon.produto.tributacao.icms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ModBCST {
	
    /**
     * Preço tabelado ou máximo sugerido.
     */
    TABELA("0", "0  –  Preço  tabelado  ou  máximo sugerido"),
    /**
     * Lista negativa (valor).
     */
    LISTA_NEGATIVA("1", "1 - Lista Negativa (valor)"),
    /**
     * Lista positiva (valor).
     */
    LISTA_POSITIVA("2", "2 - Lista Positiva (valor)"),
    /**
     * Lista neutra (valor).
     */
    LISTA_NEUTRA("3", "3 - Lista Neutra (valor)"),
    /**
     * Margem Valor Agregado (%).
     */
    OPERACAO("4", "4 - Margem Valor Agregado (%)"),
    /**
     * Pauta (valor).
     */
    PAUTA("5", "5 - Pauta (valor)");

    private ModBCST(String value, String description) {
        this.valor = value;
        this.descricao = description;
    }
    private String valor;
    private String descricao;

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return getDescricao();
    }
    
    @JsonCreator
    public static ModBCST create (@JsonProperty("valor") String value){
        for(ModBCST v : values()) {
            if(value.equals(v.getValor())) {
                return v;
            }
        }
        return null;
    }
}
