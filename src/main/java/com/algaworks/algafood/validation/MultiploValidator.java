package com.algaworks.algafood.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MultiploValidator implements ConstraintValidator<Multiplo, Number> {
	
	private int numero;

	@Override
	public void initialize(Multiplo constraintAnnotation) {
		numero = constraintAnnotation.numero();
	}

	@Override
	public boolean isValid(Number value, ConstraintValidatorContext context) {
		boolean isValido = true;
		
		if(value != null) {
			BigDecimal valorDecimal = BigDecimal.valueOf(value.doubleValue());
			BigDecimal multiploDecimal = BigDecimal.valueOf(this.numero);
			BigDecimal resto = valorDecimal.remainder(multiploDecimal);
			
			isValido = BigDecimal.ZERO.compareTo(resto) == 0;
		}
		
		return isValido;
	}
}
