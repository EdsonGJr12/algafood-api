package com.algaworks.algafood.api.model.input;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.algaworks.algafood.validation.Multiplo;
import com.algaworks.algafood.validation.TaxaFrete;
import com.algaworks.algafood.validation.ValorZeroIncluiDescricao;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@ValorZeroIncluiDescricao(
		valorField = "taxaFrete", 
		descricaoField = "nome",
		descricaoObrigatoria = "Frete Grátis"
	)
public class RestauranteInput {
	
	@NotBlank
	private String nome;
	
	@NotNull
	@TaxaFrete
	@Multiplo(numero = 5)
	private BigDecimal taxaFrete;
	
	@Valid
	@NotNull 
	/*
	 * cozinha, id
	 * */
	private CozinhaIdInput cozinha; 

	/*
	 * cozinha, id
	 * */
	//	private Long cozinhaId;
	
	@NotNull
	@Valid
	private EnderecoInput endereco;


}
