package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.controller.openapi.GrupoControllerOpenApi;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping(path = "/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

    @Autowired
    private GrupoRepository grupoRepository;
    
    @Autowired
    private CadastroGrupoService cadastroGrupo;
    
    @Autowired
    private ModelMapper modelMapper;
    
	@GetMapping
    public List<GrupoModel> listar() {
        return grupoRepository.findAll().stream()
        		.map(grupo -> modelMapper.map(grupo, GrupoModel.class))
        		.collect(Collectors.toList());
        
    }
    
	@GetMapping("/{grupoId}")
    public GrupoModel buscar(@PathVariable Long grupoId) {
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
        return modelMapper.map(grupo, GrupoModel.class);
    }
    
	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupo = modelMapper.map(grupoInput, Grupo.class);
        grupo = cadastroGrupo.salvar(grupo);
        return modelMapper.map(grupo, GrupoModel.class);
    }
    
	@PutMapping("/{grupoId}")
    public GrupoModel atualizar(@PathVariable Long grupoId,
            @RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupoAtual = cadastroGrupo.buscarOuFalhar(grupoId);
        modelMapper.map(grupoInput, grupoAtual);
        grupoAtual = cadastroGrupo.salvar(grupoAtual);
        return modelMapper.map(grupoAtual, GrupoModel.class);
    }
    
	@DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long grupoId) {
        cadastroGrupo.excluir(grupoId);	
    }   
    
	@GetMapping("/{grupoId}/permissoes")
	public List<PermissaoModel> pesquisarPermissoes(@PathVariable Long grupoId) {
		Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
		return grupo.getPermissoes().stream()
				.map(permissao -> modelMapper.map(permissao, PermissaoModel.class))
				.collect(Collectors.toList());
	}
	
	@DeleteMapping("/{grupoId}/permissoes/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		cadastroGrupo.desassociarPermissao(grupoId, permissaoId);
	}
	
	@PutMapping("/{grupoId}/permissoes/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void adicionarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		cadastroGrupo.adicionarPermissao(grupoId, permissaoId);
	}
} 