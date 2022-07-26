package com.algaworks.algafood.api.exceptionhandler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@ApiModel("Objeto problema")
@Getter
@Builder
public class ProblemObject {
	
	@ApiModelProperty(example = "preco")
	private String name;
	
	@ApiModelProperty(example = "O preço é obrigatório")
	private String userMessage;
}
