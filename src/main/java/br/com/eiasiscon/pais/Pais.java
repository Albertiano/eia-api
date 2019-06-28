package br.com.eiasiscon.pais;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import br.com.eiasiscon.base.BaseEntity;

@Document
public class Pais extends BaseEntity implements Serializable{
  
	private static final long serialVersionUID = 1L;
	
	private int cPais;
	@Field("xPais")
    private String xPais;

    public Pais(){
    }

    public Pais(int codigo, String nome){
        this.cPais = codigo;
        this.xPais = nome;
    }

    /**
     * @return the cPais
     */
    public int getcPais() {
        return cPais;
    }

    /**
     * @param cPais the cPais to set
     */
    public void setcPais(int cPais) {
        this.cPais = cPais;
    }

    /**
     * @return the xPais
     */
    public String getxPais() {
        return xPais;
    }

    /**
     * @param xPais the xPais to set
     */
    public void setxPais(String xPais) {
        this.xPais = xPais;
    }
 
	@Override
	public String toString() {
		if(this.getxPais()!=null){
			return getxPais();
		}
		return super.toString();
	}
}
