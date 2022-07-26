package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.controller.openapi.PedidoControllerOpenApi;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.algaworks.algafood.infrastucture.repository.spec.PedidoSpecs;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController
@RequestMapping(path = "/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private EmissaoPedidoService emissaoPedido;
    
    @Autowired
    private ModelMapper modelMapper;
    
//    @GetMapping
//    public MappingJacksonValue listar(@RequestParam(required = false) Set<String> campos) {
//        List<Pedido> pedidos = pedidoRepository.findAll();
//        List<PedidoResumoModel> pedidosModel = pedidos.stream()
//        		.map(pedido -> modelMapper.map(pedido, PedidoResumoModel.class))
//        		.collect(Collectors.toList());
//        
//        MappingJacksonValue wrapper = new MappingJacksonValue(pedidosModel);
//        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
//        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());
//        
//        if (campos != null) {
//        	filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos));
//        }
//        
//        wrapper.setFilters(filterProvider);
//        
//        return wrapper;
//    }
    
    @ApiImplicitParams({
    	@ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por vírgula", name = "campos",
    			type = "string", paramType = "query")
    })
    @GetMapping
    public Page<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable) {
    	
    	pageable = traduzirPageable(pageable);
    	
        Page<Pedido> page = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro), pageable);
        
        List<PedidoResumoModel> pedidos = page.getContent().stream()
        		.map(pedido -> modelMapper.map(pedido, PedidoResumoModel.class))
        		.collect(Collectors.toList());
        
        return new PageImpl<>(pedidos, pageable, pedidos.size());
    }
    
    @GetMapping("/{pedidoId}")
    public PedidoModel buscar(@PathVariable String pedidoCodigo) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoCodigo);
        return modelMapper.map(pedido, PedidoModel.class);
    }  
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = modelMapper.map(pedidoInput, Pedido.class);

            // TODO pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(1L);

            novoPedido = emissaoPedido.emitir(novoPedido);

            return modelMapper.map(novoPedido, PedidoModel.class);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
    
    private Pageable traduzirPageable(Pageable apiPageable) {
    	var mapeamento = Map.of(
    			"codigo", "codigo",
    			"nomeCliente", "cliente.nome",
    			"restaurante.nome", "restaurante.nome",
    			"valorTotal", "valorTotal"
    	);
    	
    	return PageableTranslator.translate(apiPageable, mapeamento);
    	 
    }
} 