package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import org.hibernate.annotations.CreationTimestamp;

import com.algaworks.algafood.domain.exception.NegocioException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	private String codigo;
	
	private BigDecimal subtotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	
	@CreationTimestamp
	private OffsetDateTime dataCriacao;
	
	private OffsetDateTime dataConfirmacao;
	private OffsetDateTime dataCancelamento;
	private OffsetDateTime dataEntrega;
	
	@ManyToOne
	@JoinColumn(name = "usuario_cliente_id")
	private Usuario cliente;
	
	@ManyToOne
	private Restaurante restaurante;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private FormaPagamento formaPagamento;
	
	@Embedded
	private Endereco enderecoEntrega;
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	private List<ItemPedido> itens = new ArrayList<>();
	
	@Enumerated(EnumType.STRING)
	private StatusPedido status = StatusPedido.CRIADO;
	
	@PrePersist
	private void gerarPedido() {
		this.codigo = UUID.randomUUID().toString();
	}

	
	public void calcularValorTotal() {
		
		this.itens.forEach(ItemPedido::calcularPrecoTotal);
		
	    this.subtotal = getItens().stream()
	        .map(item -> item.getPrecoTotal())
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	    
	    this.valorTotal = this.subtotal.add(this.taxaFrete);
	}
	
	public void definirFrete() {
	   this.taxaFrete = this.restaurante.getTaxaFrete();	
	}

	public void atribuirPedidoAosItens() {
	    this.itens.forEach(item -> item.setPedido(this));
	}
	
	public void confirmar() {
		if (!this.status.equals(StatusPedido.CRIADO)) {
			throw new NegocioException(
					String.format("Status do pedido %d não pode ser alterado de %s para %s", 
							this.codigo, this.status, StatusPedido.CONFIRMADO));
		}
		this.status = StatusPedido.CONFIRMADO;
		this.dataConfirmacao = OffsetDateTime.now();
	}
	
	public void entregar() {
		if (!this.status.equals(StatusPedido.CONFIRMADO)) {
			throw new NegocioException(
					String.format("Status do pedido %d não pode ser confirmado pois o pedido deve estar %s ", 
							this.codigo, StatusPedido.CONFIRMADO));
		}
		this.status = StatusPedido.ENTREGUE;
		this.dataEntrega = OffsetDateTime.now();
	}
	
	public void cancelar() {
		if (!this.status.equals(StatusPedido.CRIADO)) {
			throw new NegocioException(
					String.format("Status do pedido %d não pode ser cancelado pois deve estar %s", 
							this.codigo, StatusPedido.CRIADO));
		}
		this.status = StatusPedido.CANCELADO;
		this.dataCancelamento = OffsetDateTime.now();
	}
}
