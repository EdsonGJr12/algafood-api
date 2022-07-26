package com.algaworks.algafood.api.config;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Pageable")
public class PageableModelOpenApi {
	
	@ApiModelProperty(example = "0", value = "Número da página(começa em 0)")
	private int page;
	
	@ApiModelProperty(example = "10", value = "Quantidade de elementos por página")
	private int size;
	
	@ApiModelProperty(example = "nome,asc", value = "Nome de propriedade para ordenação")
	private List<String> sort;
}
