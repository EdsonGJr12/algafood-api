package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@ApiModel("Problema")
@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class Problem {

	@ApiModelProperty(example = "400")
	private Integer status;

	@ApiModelProperty(example = "2019-12-01T18:09:02.70844Z", position = 5, dataType = "date-time", value = "Data e hora do erro")
	private OffsetDateTime timestamp;

	private String type;

	@ApiModelProperty(example = "Dados inválidos")
	private String title;

	@ApiModelProperty(example = "Um ou mais campos estão inválidos")
	private String detail;

	@ApiModelProperty(example = "Um ou mais campos estão inválidos")
	private String userMessage;

	@ApiModelProperty(example = "Campos que geraram o erro")
	private List<ProblemObject> objects;
}
