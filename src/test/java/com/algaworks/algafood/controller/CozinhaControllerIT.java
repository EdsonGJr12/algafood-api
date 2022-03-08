package com.algaworks.algafood.controller;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceReader;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CozinhaControllerIT {
	
	private static final int ID_COZINHA_INEXISTENTE = 100;

	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;

	private int quantidadeCozinhasCadastradas;
	
	@Value("classpath:json/cozinhaCadastro.json")
	private Resource cozinhaCadastro;

	private Cozinha cozinhaAmericana;
	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		
		databaseCleaner.clearTables();
		prepararDados();
	}
	
	@Test
	public void deveRetornarStatus200QuandoConsultarCozinhas() {
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then() 
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveConterQuantidadeCorretaDeCozinhasCadastradasAoConsultarCozinhas() {
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then() 
			.body("", Matchers.hasSize(quantidadeCozinhasCadastradas))
			.body("nome", Matchers.hasItems("Tailandesa", "Americana"));
	}
	
	@Test
	public void deveRetornarStatus201QuandoCadastrarCozinha() {
		RestAssured.given()
			.body(ResourceReader.asString(this.cozinhaCadastro))
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then() 
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveRetornarStatus200ECorpoQuandoConsultarCozinhaExistente() {
		RestAssured.given()
			.pathParam("cozinhaId", cozinhaAmericana.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", Matchers.equalTo(cozinhaAmericana.getNome()));
		
	}
	
	@Test
	public void deveRetornarStatus404QuandoConsultarCozinhaInexistente() {
		RestAssured.given()
			.pathParam("cozinhaId", ID_COZINHA_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
		
	}
	
	private void prepararDados() {
		
		List<Cozinha> cozinhas = new ArrayList<>();
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Tailandesa");
		cozinhas.add(cozinha1);
		
		cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("Americana");
		cozinhas.add(cozinhaAmericana);
		
		cozinhaRepository.saveAll(cozinhas);
		
		this.quantidadeCozinhasCadastradas = cozinhas.size();
	}

}
