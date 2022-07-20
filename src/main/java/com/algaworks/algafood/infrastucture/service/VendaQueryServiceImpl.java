package com.algaworks.algafood.infrastucture.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<VendaDiaria> query = builder.createQuery(VendaDiaria.class);
		Root<Pedido> root = query.from(Pedido.class);
		
		List<Predicate> predicates = new ArrayList<>();
		
		List<StatusPedido> status = List.of(StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE);
		Predicate predicateStatus = root.get("status").in(status);
		predicates.add(predicateStatus);
		
		if (filtro.getRestauranteId() != null) {
			Path<Object> restauranteId = root.get("restaurante").get("id");
			Predicate predicate = builder.equal(restauranteId, filtro.getRestauranteId());
			predicates.add(predicate);
		}
		
		if (filtro.getDataCriacaoInicio() != null) {
			Path<OffsetDateTime> dataCriacao = root.get("dataCriacao");
			Predicate predicate = builder.greaterThanOrEqualTo(dataCriacao, filtro.getDataCriacaoInicio());
			predicates.add(predicate);
		}
		
		if (filtro.getDataCriacaoFim() != null) {
			Path<OffsetDateTime> dataCriacao = root.get("dataCriacao");
			Predicate predicate = builder.lessThanOrEqualTo(dataCriacao, filtro.getDataCriacaoFim());
			predicates.add(predicate);
		}
		
		Path<Object> dataCriacaoPath = root.get("dataCriacao");
		Expression<Date> functioConvertTzDataCriacao = builder.function("convert_tz", 
				Date.class, dataCriacaoPath, builder.literal("+00:00"), builder.literal(timeOffset));
		
		Expression<Date> functionDateDataCriacao = builder.function("date", 
				Date.class, functioConvertTzDataCriacao);
		
		CompoundSelection<VendaDiaria> projection = builder.construct(VendaDiaria.class, 
				functionDateDataCriacao, 
				builder.count(root.get("id")),
				builder.sum(root.get("valorTotal")));
		
		query.select(projection);
		query.groupBy(functionDateDataCriacao);
		query.where(predicates.toArray(new Predicate[0]));
		
		return manager.createQuery(query).getResultList();
	}

}
