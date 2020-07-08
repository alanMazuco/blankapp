package br.eti.amazu.infra.view.vo;

import java.util.ArrayList;
import java.util.List;

import br.eti.amazu.component.pworld.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Theme extends AbstractEntity<Object> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String value;
	private List<Theme> themes;

	public Theme(Long id, String name, String value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public Long getId() {
		return id;
	}	

	public List<Theme> getThemes() {
		themes = new ArrayList<>();
		themes.add(new Theme(0L, "Aristo", "aristo"));
		themes.add(new Theme(1L, "Nova-Light", "nova-light"));
		themes.add(new Theme(2L, "Nova-Dark", "nova-dark"));
		themes.add(new Theme(3L, "Nova-Colored", "nova-colored"));
		themes.add(new Theme(4L, "Luna-Blue", "luna-blue"));
		themes.add(new Theme(5L, "Luna-Amber", "luna-amber"));
		themes.add(new Theme(6L, "Luna-Green", "luna-green"));
		themes.add(new Theme(7L, "Luna-Pink", "luna-pink"));
		themes.add(new Theme(8L, "Omega", "omega"));
		return themes;
	}
}