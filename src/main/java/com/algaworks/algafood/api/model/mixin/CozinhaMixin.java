package com.algaworks.algafood.api.model.mixin;

import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public abstract class CozinhaMixin {

	@JsonIgnoreProperties(value = "nome", allowGetters = true)
	private List<Restaurante> restaurantes;

}