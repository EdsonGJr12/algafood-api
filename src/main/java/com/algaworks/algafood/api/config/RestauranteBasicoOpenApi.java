package com.algaworks.algafood.api.config;

import java.math.BigDecimal;

import com.algaworks.algafood.domain.model.Cozinha;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("RestauranteBasicoModel")
@Getter
@Setter
public class RestauranteBasicoOpenApi {
	
	@ApiModelProperty(example = "1")
	private Long id;
	
	@ApiModelProperty(example = "Thai Gourmet")
	private String nome;
	
	@ApiModelProperty(example = "12.00")
	private BigDecimal taxaFrete;
	private Cozinha cozinha;
}
