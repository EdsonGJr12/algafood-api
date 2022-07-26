package com.algaworks.algafood.api.model;

import lombok.Getter;
import lombok.Setter;

//@ApiModel(value = "Cidade", description = "Representa uma cidade")
@Setter
@Getter
public class CidadeModel {

//	@ApiModelProperty(value = "ID da cidade")
	private Long id;
	private String nome;
	private EstadoModel estado;

}