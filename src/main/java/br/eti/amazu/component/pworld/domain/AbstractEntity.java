package br.eti.amazu.component.pworld.domain;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

/*
* Classe base para entidades que possuem um id.
* Sobrescrevendo corretamente os metodos hashCode() e equals().
*/
@EqualsAndHashCode
public abstract class AbstractEntity<T> implements Serializable {
		
	private static final long serialVersionUID = -1174554691036000631L;

	public abstract T getId();
}
