package com.algaworks.algafood.domain.exception;

public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;

	public CidadeNaoEncontradaException(String message) {
		super(message);
	}
	
	public CidadeNaoEncontradaException(Long cidadeId) {
		this(String.format("Não existe uma cidade com código %d", cidadeId));
	}

}
