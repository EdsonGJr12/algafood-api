package com.algaworks.algafood.api.exceptionhandler;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemObject {
	private String name;
	private String userMessage;
}
