package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RestauranteModel {
	
	private Long id;
	private String nome;
	private BigDecimal precoFrete;
	
	/*
	 * nome, cozinha
	 * */
	private String cozinhaNome;
	
	/*
	 * id, cozinha
	 * */
	private String cozinhaId;
	
	private Boolean ativo;
	
	private Boolean aberto;
	
	private EnderecoModel endereco;
}
