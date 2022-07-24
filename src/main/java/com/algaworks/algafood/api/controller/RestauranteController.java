package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.model.view.RestauranteView;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.algaworks.algafood.validation.ValidacaoException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping
	public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		List<RestauranteModel> restaurantesModel = restaurantes.stream().map(restaurante -> modelMapper.map(restaurante, RestauranteModel.class))
				.collect(Collectors.toList());
		
		MappingJacksonValue wrapper = new MappingJacksonValue(restaurantesModel);
		
		wrapper.setSerializationView(RestauranteView.Resumo.class);
		
		if ("apenas-nome".equals(projecao)) {
			wrapper.setSerializationView(RestauranteView.ApenasNome.class);
		} else if ("completo".equals(projecao)) {
			wrapper.setSerializationView(null);
		} 
		
		return wrapper;
	}

//	@GetMapping
//	public List<RestauranteModel> listar() {
//		List<Restaurante> restaurantes = restauranteRepository.findAll();
//		return restaurantes.stream().map(restaurante -> modelMapper.map(restaurante, RestauranteModel.class))
//				.collect(Collectors.toList());
//	}
//	
//	@JsonView(RestauranteView.Resumo.class)
//	@GetMapping(params = "projecao=resumo")
//	public List<RestauranteModel> listarResumido() {
//		return listar();
//	}
//	
//	@JsonView(RestauranteView.ApenasNome.class)
//	@GetMapping(params = "projecao=apenas-nome")
//	public List<RestauranteModel> listarResumido2() {
//		return listar();
//	}

	@GetMapping("/teste")
	public List<Restaurante> buscaDinamicaCriteria(@RequestParam(required = false) String nome,
			@RequestParam(required = false) BigDecimal taxaFreteInicial,
			@RequestParam(required = false) BigDecimal taxaFreteFinal) {
		return restauranteRepository.findDynamicCriteria(nome, taxaFreteInicial, taxaFreteFinal);
	}

	@GetMapping("/teste/com-frete-gratis")
	public List<Restaurante> restaurantesComFreteGratis(String nome) {
		return restauranteRepository.findComFreteGratis(nome);
	}

	@GetMapping("/{restauranteId}")
	public RestauranteModel buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		return modelMapper.map(restaurante, RestauranteModel.class);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			Restaurante restaurante = modelMapper.map(restauranteInput, Restaurante.class);
			restaurante = cadastroRestauranteService.salvar(restaurante);
			return modelMapper.map(restaurante, RestauranteModel.class);
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId,
			@RequestBody @Valid RestauranteInput restauranteInput) {

		try {
			Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);
			restauranteAtual.setCozinha(new Cozinha());
			if (restauranteAtual.getEndereco() != null) {
				restauranteAtual.getEndereco().setCidade(new Cidade());
			}
			modelMapper.map(restauranteInput, restauranteAtual);

			Restaurante restauranteAlterado = cadastroRestauranteService.salvar(restauranteAtual);

			return modelMapper.map(restauranteAlterado, RestauranteModel.class);
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PatchMapping("/{restauranteId}")
	public Restaurante atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos,
			HttpServletRequest request) throws ValidacaoException {

		Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		merge(campos, restauranteAtual, request);
		validate(restauranteAtual, "restaurante");
		try {
			return cadastroRestauranteService.salvar(restauranteAtual);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.ativar(restauranteId);
	}

	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			cadastroRestauranteService.ativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.inativar(restauranteId);

	}

	@DeleteMapping("/inativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restauranteIds) {
		try {
			cadastroRestauranteService.inativar(restauranteIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
		cadastroRestauranteService.abrir(restauranteId);
	}

	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.fechar(restauranteId);
	}

	@GetMapping("/{restauranteId}/formas-pagamentos")
	public List<FormaPagamentoModel> pesquisarFormasPagamento(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		return restaurante.getFormasPagamento().stream()
				.map(formaPagamento -> modelMapper.map(formaPagamento, FormaPagamentoModel.class))
				.collect(Collectors.toList());
	}

	@DeleteMapping("/{restauranteId}/formas-pagamentos/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerFormaPagamento(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		cadastroRestauranteService.removerFormaPagamento(restauranteId, formaPagamentoId);
	}

	@PutMapping("/{restauranteId}/formas-pagamentos/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void adicionarFormaPagamento(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		cadastroRestauranteService.adicionarFormaPagamento(restauranteId, formaPagamentoId);
	}

	@GetMapping("/{restauranteId}/produtos")
	public List<ProdutoModel> pesquisarProdutos(@PathVariable Long restauranteId, 
			@RequestParam(required = false) boolean incluirInativos) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
		
		List<Produto> produtos = null;
		
		if (incluirInativos) {
			produtos = produtoRepository.findByRestaurante(restaurante);
		} else {
			produtos = produtoRepository.findAtivosByRestaurante(restaurante);
		}
		
		return produtos.stream()
				.map(produto -> modelMapper.map(produto, ProdutoModel.class))
				.collect(Collectors.toList());
	}

	@GetMapping("/{restauranteId}/produtos/{produtoId}")
	public ProdutoModel buscarProduto(@PathVariable Long restauranteId, 
			@PathVariable Long produtoId) {
		Produto produto = cadastroRestauranteService.buscarProdutoOuFalhar(restauranteId, produtoId);
		return modelMapper.map(produto, ProdutoModel.class);
	}

	@PutMapping("/{restauranteId}/produtos")
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoModel adicionarProduto(@PathVariable Long restauranteId,
			@RequestBody @Valid ProdutoInput produtoInput) {
		Produto novoProduto = modelMapper.map(produtoInput, Produto.class);
		novoProduto = cadastroRestauranteService.adicionarProduto(restauranteId, novoProduto);
		return modelMapper.map(novoProduto, ProdutoModel.class);
	}

	@PutMapping("/{restauranteId}/produtos/{produtoId}")
	public ProdutoModel atualizarProduto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@RequestBody @Valid ProdutoInput produtoInput) {
		Produto produtoAtual = cadastroRestauranteService.buscarProdutoOuFalhar(restauranteId, produtoId);
		modelMapper.map(produtoInput, produtoAtual);
		produtoAtual = cadastroRestauranteService.salvarProduto(produtoAtual);
		return modelMapper.map(produtoAtual, ProdutoModel.class);
	}

	private void validate(Restaurante restaurante, String objectName) throws ValidacaoException {
		BindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
		validator.validate(restaurante, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
	}

	private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDestino, HttpServletRequest request) {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

		try {
			Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);
			camposOrigem.forEach((atributo, valor) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, atributo);
				field.setAccessible(true);

				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			HttpInputMessage httpInputMessage = new ServletServerHttpRequest(request);
			throw new HttpMessageNotReadableException(e.getMessage(), e.getCause(), httpInputMessage);
		}

	}

}
