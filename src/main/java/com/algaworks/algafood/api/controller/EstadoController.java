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

import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.api.model.input.EstadoInput;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CadastroEstadoService cadastroEstadoService;
	
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public List<EstadoModel> listar() {
		return estadoRepository.findAll().stream()
				.map(estado -> modelMapper.map(estado, EstadoModel.class))
				.collect(Collectors.toList());
	}

	@GetMapping("/{estadoId}")
	public EstadoModel buscar(@PathVariable Long estadoId) {
		Estado estado = cadastroEstadoService.buscarOuFalhar(estadoId);
		return modelMapper.map(estado, EstadoModel.class);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
		Estado estado = modelMapper.map(estadoInput, Estado.class);
		estado = cadastroEstadoService.salvar(estado);
		return modelMapper.map(estado, EstadoModel.class);
	}

	@PutMapping("{estadoId}")
	public EstadoModel alterar(@PathVariable Long estadoId, @RequestBody @Valid EstadoInput estadoInput) {
		Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(estadoId);
		modelMapper.map(estadoInput, estadoAtual);
		estadoAtual = cadastroEstadoService.salvar(estadoAtual);
		return modelMapper.map(estadoAtual, EstadoModel.class);
	}

	@DeleteMapping("{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId) {
		cadastroEstadoService.excluir(estadoId);
	}

}
