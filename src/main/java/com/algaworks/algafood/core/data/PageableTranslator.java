package com.algaworks.algafood.core.data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableTranslator {
	
	public static Pageable translate(Pageable pageable, Map<String, String> fieldsMapping) {
		
		List<Sort.Order> newOrders =  pageable.getSort().stream()
			.filter(order -> fieldsMapping.containsKey(order.getProperty()))
			.map(order -> new Sort.Order(order.getDirection(), 
					fieldsMapping.get(order.getProperty())))
			.collect(Collectors.toList());
		
		System.out.println(newOrders);
		
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(newOrders));
	}
}	
