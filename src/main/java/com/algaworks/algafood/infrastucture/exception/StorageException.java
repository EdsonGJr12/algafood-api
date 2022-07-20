package com.algaworks.algafood.infrastucture.exception;

public class StorageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
