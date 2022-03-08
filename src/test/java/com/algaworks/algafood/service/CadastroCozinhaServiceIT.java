package com.algaworks.algafood.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@SpringBootTest
public class CadastroCozinhaServiceIT {

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Test
	public void deveCadastrarCozinhaComSucesso() {

		// cenário
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");

		// ação
		novaCozinha = cadastroCozinha.salvar(novaCozinha);

		// validação
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}

	@Test
	public void deveFalharAoCadastrarCozinhaSemNome() {
		
		//cenário
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);

		//ação
		ConstraintViolationException erroEsperado = Assertions.assertThrows(ConstraintViolationException.class, () -> cadastroCozinha.salvar(novaCozinha));

		//validação
		assertThat(erroEsperado).isNotNull();

	}
	
	@Test
	public void deveFalharAoExcluirCozinhaEmUso() {
		Long idCozinhaEmUso = 1l;
		
		EntidadeEmUsoException erroEsperado = Assertions.assertThrows(EntidadeEmUsoException.class, () -> cadastroCozinha.excluir(idCozinhaEmUso));
		
		assertThat(erroEsperado).isNotNull();
	}
	
	@Test
	public void deveFalharAoExcluirEntidadeInexistente() {
		Long idCozinhaInexistente = 1564564566565165l;
		
		CozinhaNaoEncontradaException erroEsperado = Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> cadastroCozinha.excluir(idCozinhaInexistente));
		
		assertThat(erroEsperado).isNotNull();
	}

}
