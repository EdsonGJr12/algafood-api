package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;
	
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public List<CozinhaModel> listar() {
		return cozinhaRepository.findAll().stream()
				.map(cozinha -> modelMapper.map(cozinha, CozinhaModel.class))
				.collect(Collectors.toList());
	}

	@GetMapping("/{cozinhaId}")
	public CozinhaModel buscar(@PathVariable Long cozinhaId) {
		Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
		return modelMapper.map(cozinha, CozinhaModel.class);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinha = modelMapper.map(cozinhaInput, Cozinha.class);
		cozinha = cadastroCozinhaService.salvar(cozinha);
		return modelMapper.map(cozinha, CozinhaModel.class);
	}

	@PutMapping("/{cozinhaId}")
	public CozinhaModel atualizar(@PathVariable Long cozinhaId, @RequestBody CozinhaInput cozinhaInput) {
		Cozinha cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
		modelMapper.map(cozinhaInput, cozinhaAtual);
		cozinhaAtual = cadastroCozinhaService.salvar(cozinhaAtual);
		return modelMapper.map(cozinhaAtual, CozinhaModel.class);
	}

	
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cadastroCozinhaService.excluir(cozinhaId);
	}

}
