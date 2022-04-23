package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

@Service
public class CadastroProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;

	public Produto buscarOuFalhar(Long restauranteId, Long produtoId) {
		return produtoRepository.findById(restauranteId, produtoId)
				.orElseThrow(() -> new ProdutoNaoEncontradoException(restauranteId, produtoId));
	}

}
