package br.eti.amazu.infra.view.showcase.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Veiculo {
		
	private Integer id;
	private String marca;
	private String modelo;	
	private String cor;
	private String ano;
		
}

