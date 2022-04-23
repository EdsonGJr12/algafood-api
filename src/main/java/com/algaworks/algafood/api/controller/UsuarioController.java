package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.model.input.SenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CadastroUsuarioService cadastroUsuario;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping
    public List<UsuarioModel> listar() {
        return usuarioRepository.findAll().stream()
        		.map(usuario -> modelMapper.map(usuario, UsuarioModel.class))
        		.collect(Collectors.toList());
        
    }
    
    @GetMapping("/{usuarioId}")
    public UsuarioModel buscar(@PathVariable Long usuarioId) {
        Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);
        return modelMapper.map(usuario, UsuarioModel.class);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioInput) {
        Usuario usuario = modelMapper.map(usuarioInput, Usuario.class);
        usuario = cadastroUsuario.salvar(usuario);
        
        return modelMapper.map(usuario, UsuarioModel.class);
    }
    
    @PutMapping("/{usuarioId}")
    public UsuarioModel atualizar(@PathVariable Long usuarioId,
            @RequestBody @Valid UsuarioInput usuarioInput) {
        Usuario usuarioAtual = cadastroUsuario.buscarOuFalhar(usuarioId);
        modelMapper.map(usuarioInput, usuarioAtual);
        usuarioAtual = cadastroUsuario.salvar(usuarioAtual);
        
        return modelMapper.map(usuarioAtual, UsuarioModel.class);
    }
    
    @PutMapping("/{usuarioId}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInput senha) {
        cadastroUsuario.alterarSenha(usuarioId, senha.getSenhaAtual(), senha.getNovaSenha());
    }            
}  