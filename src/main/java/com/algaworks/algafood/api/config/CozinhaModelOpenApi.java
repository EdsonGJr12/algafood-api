package com.algaworks.algafood.api.config;

import com.algaworks.algafood.api.model.CozinhaModel;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@ApiModel("CozinhasModel")
@Getter
@Setter
public class CozinhaModelOpenApi extends PagedModelOpenApi<CozinhaModel> {
	 
}
