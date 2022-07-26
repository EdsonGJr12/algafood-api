package com.algaworks.algafood.api.model.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoInput {
	
	@ApiModelProperty(example = "38400-000")
	@NotBlank
	private String cep;
	
	@ApiModelProperty(example = "Rua Floriano Peixoto")
	@NotBlank
	private String logradouro;
	
	@ApiModelProperty(example = "1500")
	@NotBlank
	private String numero;
	
	@ApiModelProperty(example = "Apto 901")
	private String complemento;
	
	@ApiModelProperty(example = "Centro")
	@NotBlank
	private String bairro;
	
	@NotNull
	@Valid
	private CidadeIdInput cidade;
}
