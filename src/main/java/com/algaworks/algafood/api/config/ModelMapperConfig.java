package com.algaworks.algafood.api.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.ItemPedidoInput;
import com.algaworks.algafood.domain.model.ItemPedido;
import com.algaworks.algafood.domain.model.Restaurante;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		var modelMapper =  new ModelMapper();
		
		modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
			.addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete)
			.addMapping(restaurante -> restaurante.getEndereco().getCidade().getEstado().getNome(), 
					(model, value) -> model.getEndereco().getCidade().setEstado((String) value));
		
		modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class)
	    	.addMappings(mapper -> mapper.skip(ItemPedido::setId)); 
		
		return modelMapper;
	}
}
