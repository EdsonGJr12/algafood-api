package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/*
	 * id
	 */
	private Long id;

	@Column(nullable = false)
	private String nome;

	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;

	@ManyToOne
	@JoinColumn(name = "cozinha_id", nullable = false)
	/*
	 * cozinha, nome, id ... etc
	 */
	private Cozinha cozinha;

	@ManyToMany
	@JoinTable(name = "restaurante_forma_pagamento", joinColumns = @JoinColumn(name = "restaurante_id"), inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
	private Set<FormaPagamento> formasPagamento = new HashSet<>();

	@Embedded
	private Endereco endereco;

	@Column(nullable = false)
	@CreationTimestamp
	private OffsetDateTime dataCadastro;

	@Column(nullable = false)
	@UpdateTimestamp
	private OffsetDateTime dataAtualizacao;

	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "restaurante_usuario_responsavel", 
		joinColumns = @JoinColumn(name = "restaurante_id"), 
		inverseJoinColumns = @JoinColumn(name = "usuario_id"))
	private Set<Usuario> responsaveis = new HashSet<>();

	private Boolean ativo = Boolean.TRUE;

	private Boolean aberto = Boolean.TRUE;

	public void ativar() {
		this.ativo = true;
	}

	public void inativar() {
		this.ativo = false;
	}

	public void removerFormaPagamento(FormaPagamento formaPagamento) {
		this.formasPagamento.remove(formaPagamento);
	}

	public void adicionarFormaPagamento(FormaPagamento formaPagamento) {
		this.formasPagamento.add(formaPagamento);
	}

	public void adicionarProduto(Produto produto) {
		this.produtos.add(produto);
		produto.setRestaurante(this);
	}

	public void abrir() {
		this.aberto = true;
	}

	public void fechar() {
		this.aberto = false;
	}

	public boolean removerResponsavel(Usuario usuario) {
		return this.responsaveis.remove(usuario);
	}

	public boolean adicionarResponsavel(Usuario usuario) {
		return this.responsaveis.add(usuario);
	}

	public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
		return this.formasPagamento.contains(formaPagamento);
	}

	public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
		return !aceitaFormaPagamento(formaPagamento);
	}
}
