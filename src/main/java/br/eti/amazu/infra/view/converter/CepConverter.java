package br.eti.amazu.infra.view.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("cepConverter")
public class CepConverter implements Converter<Object> {

	//Converte o objeto da view para o backEnd.
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) 
				throws ConverterException {
		
		if (value.equals("")  || value == null || value.equals(null)){				
			return null; //isto devolve ao jsf o controle de campos em branco		
		}
				
		//Devolve a string sem formato
		return value!=null ? value.toString().replace("-", ""): null;		
	}  
	   
	//Converte o objeto do backEnd para a view.
    @Override
	public String getAsString(FacesContext context, UIComponent component, Object value) 
				throws ConverterException {
    	    	
    	//montando e devolvendo o cep formatado.
    	if (value.equals("")  || value == null || value.equals(null)){
    		return null;
    	}
    	return value.toString().substring(0, 5) + "-" + value.toString().substring(5);
    } 	    
}