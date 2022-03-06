package com.algaworks.algafood.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = { ValorZeroIncluirDescricaoValidator.class  })
public @interface ValorZeroIncluiDescricao {

	String valorField();

	String descricaoField();

	String descricaoObrigatoria();
	
	String message() default "Descrição obrigatória inválida";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}