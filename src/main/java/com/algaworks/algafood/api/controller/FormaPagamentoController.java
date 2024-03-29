package com.algaworks.algafood.api.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.algaworks.algafood.api.controller.openapi.FormaPagamentoControllerOpenApi;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

@RestController
@RequestMapping(path = "/formas-pagamento", produces = MediaType.APPLICATION_JSON_VALUE)
public class FormaPagamentoController implements FormaPagamentoControllerOpenApi {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;
    
    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamento;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping
    public ResponseEntity<List<FormaPagamentoModel>> listar(ServletWebRequest request) {
    	// Desabilita shallow E-tag para essa requisiçao
    	ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
    	
    	String eTag = "0";
    	
    	OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.buscarUlimaAtualizacao();
    	if (dataUltimaAtualizacao != null) {
    		eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
    	}
    	
    	//Ponto para verificar se continua ou não o processamento
    	boolean isNotModified = request.checkNotModified(eTag);
    	if (isNotModified) {
    		return null;
    	}
    	
        List<FormaPagamentoModel> formasPagamento = formaPagamentoRepository.findAll().stream()
        		.map(formaPagamento -> modelMapper.map(formaPagamento, FormaPagamentoModel.class))
        		.collect(Collectors.toList());
		
        return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
				.eTag(eTag)
				.body(formasPagamento);
    }
    
    @GetMapping("/{formaPagamentoId}")
    public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long formaPagamentoId) {
        FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
        FormaPagamentoModel formaPagamentoModel = modelMapper.map(formaPagamento, FormaPagamentoModel.class);
		return ResponseEntity.ok()
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate())
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
//				.cacheControl(CacheControl.noCache())
				.cacheControl(CacheControl.noStore())
				.body(formaPagamentoModel);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamento = modelMapper.map(formaPagamentoInput, FormaPagamento.class);
        
        formaPagamento = cadastroFormaPagamento.salvar(formaPagamento);
        
        return modelMapper.map(formaPagamento, FormaPagamentoModel.class);
    }
    
    @PutMapping("/{formaPagamentoId}")
    public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId,
            @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamentoAtual = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
        
        modelMapper.map(formaPagamentoInput, formaPagamentoAtual);
        
        formaPagamentoAtual = cadastroFormaPagamento.salvar(formaPagamentoAtual);
        
        return modelMapper.map(formaPagamentoAtual, FormaPagamentoModel.class);
    }
    
    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long formaPagamentoId) {
        cadastroFormaPagamento.excluir(formaPagamentoId);	
    }

} 