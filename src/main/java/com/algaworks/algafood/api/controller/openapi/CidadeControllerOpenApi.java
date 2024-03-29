package com.algaworks.algafood.api.controller.openapi;

import java.util.List;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Api(tags = "Cidades")
public interface CidadeControllerOpenApi {

	@ApiOperation("Lista as cidades")
	List<CidadeModel> listar();

	@ApiOperation("Busca uma cidade por ID")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "ID da  cidade inválido", 
				content = { @Content(schema = @Schema(implementation = Problem.class)) }),
		@ApiResponse(responseCode = "404", description = "Cidade não encontrada", 
			content = { @Content(schema = @Schema(implementation = Problem.class)) })
	})
	CidadeModel buscar(Long cidadeId);

	@ApiOperation("Cadastra uma cidade")
	CidadeModel adicionar(CidadeInput cidadeInput);

	@ApiOperation("Atualiza uma cidade por ID")
	CidadeModel alterar(Long cidadeId, CidadeInput cidadeInput);

	@ApiOperation("Exclui uma cidade por ID")
	void remover(Long cidadeId);

}