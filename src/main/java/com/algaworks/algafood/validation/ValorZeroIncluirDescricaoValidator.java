package com.algaworks.algafood.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;

public class ValorZeroIncluirDescricaoValidator implements 
	ConstraintValidator<ValorZeroIncluiDescricao, Object> {
	
	private String valorField;
	private String descricaoField;
	private String descricaoObrigatoria;

	@Override
	public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
		this.valorField = constraintAnnotation.valorField();
		this.descricaoField = constraintAnnotation.descricaoField();
		this.descricaoObrigatoria = constraintAnnotation.descricaoObrigatoria();
	}

	@Override
	public boolean isValid(Object objetoValidacao, ConstraintValidatorContext context) {
		boolean isValid = true;
		
		try {
			BigDecimal valor = (BigDecimal) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), valorField)
					.getReadMethod().invoke(objetoValidacao);
			String descricao = (String) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), descricaoField)
					.getReadMethod().invoke(objetoValidacao);
			
			if(valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null) {
				isValid = descricao.toLowerCase().contains(this.descricaoObrigatoria.toLowerCase());
			}
			
			return isValid;
			
		} catch (Exception e) {
			throw new ValidationException(e);
		}  
	}

}
