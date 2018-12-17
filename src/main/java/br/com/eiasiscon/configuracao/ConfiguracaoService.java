package br.com.eiasiscon.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eiasiscon.base.BaseService;

@Service
public class ConfiguracaoService extends BaseService<Configuracao, String> {
	
	@Autowired
	private ConfiguracaoRepository repository;
		
	@Autowired
	public void setJpaRepository(ConfiguracaoRepository jpa) {
		setJpa(jpa);
	}
	
	public Configuracao getConfiguracao(String empresa) {
		return repository.getConfiguracao(empresa);
	}
	
}

