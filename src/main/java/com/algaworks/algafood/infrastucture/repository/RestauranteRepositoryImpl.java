package com.algaworks.algafood.infrastucture.repository;

import static com.algaworks.algafood.infrastucture.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.algaworks.algafood.infrastucture.repository.spec.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CustomizeRestauranteRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Repository
public class RestauranteRepositoryImpl implements CustomizeRestauranteRepository {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired @Lazy
	private RestauranteRepository restauranteRepository;

	@Override
	public List<Restaurante> findDynamicJpql(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

		StringBuilder jpql = new StringBuilder();
		jpql.append("from Restaurante where 0=0 ");

		Map<String, Object> parametros = new HashMap<String, Object>();

		if (StringUtils.hasLength(nome)) {
			jpql.append("and nome like :nome ");
			parametros.put("nome", nome);
		}

		if (taxaFreteInicial != null) {
			jpql.append("and taxaFrete >= :taxaInicial ");
			parametros.put("taxaFrete", taxaFreteInicial);
		}

		if (taxaFreteFinal != null) {
			jpql.append("and taxaFrete >= :taxaFinal ");
			parametros.put("taxaFrete", taxaFreteFinal);
		}

		TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);
		parametros.forEach(query::setParameter);

		return query.getResultList();
	}

	public List<Restaurante> findDynamicCriteria(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
		Root<Restaurante> root = criteria.from(Restaurante.class);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (StringUtils.hasLength(nome)) {
			Predicate predicate =  builder.like(root.get("nome"), "%" + nome + "%");
			predicates.add(predicate);
		}
		if (taxaFreteInicial != null) {
			Predicate predicate = builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial);
			predicates.add(predicate);
		}
		if(taxaFreteFinal != null) {
			Predicate predicate = builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal);
			predicates.add(predicate);
		}
		
		criteria.where(predicates.toArray(new Predicate[0]));
		
		TypedQuery<Restaurante> query = manager.createQuery(criteria);

		return query.getResultList();
	}

	@Override
	public List<Restaurante> findComFreteGratis(String nome) {
		return restauranteRepository.findAll(comFreteGratis()
				.and(comNomeSemelhante(nome)));
	}

}
