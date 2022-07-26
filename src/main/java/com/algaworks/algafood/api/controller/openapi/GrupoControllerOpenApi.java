package com.algaworks.algafood.api.controller.openapi;

import java.util.List;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Api(tags = "Grupos")
public interface GrupoControllerOpenApi {

	@ApiOperation("Listar grupos")
	List<GrupoModel> listar();

	@ApiOperation("Buscar um grupo por ID")
	@ApiResponses({
		@ApiResponse(responseCode = "400", description = "ID do grupo inválido", 
				content = { @Content(schema = @Schema(implementation = Problem.class)) }),
		@ApiResponse(responseCode = "404", description = "Grupo não encontrado", 
			content = { @Content(schema = @Schema(implementation = Problem.class)) })
	})
	GrupoModel buscar(Long grupoId);

	@ApiOperation("Cadastra um grupo")
    @ApiResponses({
    	@ApiResponse(responseCode = "201", description = "ID do grupo inválido")
    })
	GrupoModel adicionar(GrupoInput grupoInput);

	@ApiOperation("Atualiza um grupo por ID")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "Grupo atualizado"),
    	@ApiResponse(responseCode = "404", description = "Grupo não encontrado",
    			content = { @Content(schema = @Schema(implementation = Problem.class)) })
    })
	GrupoModel atualizar(Long grupoId, GrupoInput grupoInput);

	@ApiOperation("Exclui um grupo por ID")
    @ApiResponses({
    	@ApiResponse(responseCode = "204", description = "Grupo excluído"),
    	@ApiResponse(responseCode = "404", description = "Grupo não encontrado",
    			content = { @Content(schema = @Schema(implementation = Problem.class)) })
    })
	void remover(Long grupoId);

	@ApiOperation("Pesquisar permissões de um grupo")
	List<PermissaoModel> pesquisarPermissoes(Long grupoId);

	@ApiOperation("Remover permissão de um grupo")
	void removerPermissao(Long grupoId, Long permissaoId);

	@ApiOperation("Adicionar permissão de um grupo")
	void adicionarPermissao(Long grupoId, Long permissaoId);

}