package com.algaworks.algafood.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;

@Data
@Entity
public class Grupo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;

	@ManyToMany
	@JoinTable(name = "grupo_permissao", 
		joinColumns = @JoinColumn(name="grupo_id"),
		inverseJoinColumns = @JoinColumn(name="permissao_id"))
	private Set<Permissao> permissoes = new HashSet<>();
	
	public boolean removerPermissao(Permissao permissao) {
	    return this.permissoes.remove(permissao);
	}

	public boolean adicionarPermissao(Permissao permissao) {
	    return this.permissoes.add(permissao);
	}
}
