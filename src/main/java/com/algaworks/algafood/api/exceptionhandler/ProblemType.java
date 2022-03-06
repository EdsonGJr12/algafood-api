package com.algaworks.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
	RECURSO_NAO_ENCONTRADO("Recurso não encontrado", "/recurso-nao-encontrado"), 
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"), 
	RECURSO_EM_USO("Recurso em uso", "/recurso-em-uso"),
	MENSAGEM_INCOMPREENSIVEL("Mensagem incompreensível", "/mensagem-incompreensivel"), 
	PARAMETRO_INVALIDO("Parâmetro inválido", "/parametro-invalido"), 
	ERRO_SISTEMA("Erro interno do sistema", "/erro-interno"), 
	DADOS_INVALIDOS("Erro validação de dados de entrada", "/dados-invalidos");
	
	private String title;
	private String uri;
	
	ProblemType(String title, String path) {
		this.title = title;
		this.uri = "https://algaafood.com.br" + path;
	}
}
