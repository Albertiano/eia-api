package br.com.eiasiscon.municipio;

import java.util.Collection;

import br.com.eiasiscon.municipio.Municipio;
import br.com.eiasiscon.municipio.UF;

public interface MunicipioRepositoryCustom {
	Collection<Municipio> findByUf(UF uf);
}
