package com.algaworks.algafood.infrastucture.repository.spec;

import java.util.ArrayList;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repository.filter.PedidoFilter;

public class PedidoSpecs {
	
	public static Specification<Pedido> usandoFiltro(PedidoFilter filtro) {
		return (root, query, builder) -> {
			
			if (query.getResultType().equals(Pedido.class)) {
				root.fetch("restaurante").fetch("cozinha");
				root.fetch("cliente");
			}
			
			
			var predicates = new ArrayList<Predicate>();
			
			if (filtro.getClienteId() != null) {
				var predicate = builder.equal(root.get("cliente"), 
						filtro.getClienteId());
				predicates.add(predicate);
			}
			
			if (filtro.getRestauranteId() != null) {
				var predicate = builder.equal(root.get("restaurante"), 
						filtro.getRestauranteId());
				predicates.add(predicate);
			}
			
			if (filtro.getDataCriacaoInicio() != null) {
				var predicate = builder.greaterThanOrEqualTo(root.get("dataCriacao"), 
						filtro.getDataCriacaoInicio());
				predicates.add(predicate);
			}
			
			if (filtro.getDataCriacaoFim() != null) {
				var predicate = builder.lessThanOrEqualTo(root.get("dataCriacao"), 
						filtro.getDataCriacaoFim());
				predicates.add(predicate);
			}
			
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
}
