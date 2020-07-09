package br.eti.amazu.infra.view.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.eti.amazu.infra.util.FacesUtil;

@FacesConverter("dateConverter")
public class DateConverter implements Converter<Object> {
		
	@Override //Converte o objeto da view para o backEnd.
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
				
		//Controle de campos em branco
		if (value == null || value.equals("")) return null; 		
		
		/* pegar o label do componente
		 * Eh necessario setar no componente o atributo label na view. */
		Map<String, Object> map = component.getAttributes();
						
		DateFormat formata = new SimpleDateFormat("dd/MM/yyyy");
				
		try {			
			//isto força o formatador a lançar uma exception se a data for inválida
			formata.setLenient(false);					
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(formata.parse(value));						
			return calendar.getTime();
			
		} catch (ParseException e) {			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,null,null);
			msg.setSummary(new FacesUtil().getMessage("MGL044", new String[]{map.get("label").toString(),value}));
			new FacesUtil().setMessage(msg);
			return null;
		}		
	}  
	   	
    @Override //Converte o objeto do backEnd para a view.
	public String getAsString(FacesContext context, UIComponent component, Object value) {    	    
    	
    	if(value == null) return "";    	
    	Calendar calendar = new GregorianCalendar();
		calendar.setTime((Date) value);	
		SimpleDateFormat in= new SimpleDateFormat("dd/MM/yyyy"); 
		return in.format(calendar.getTime());
    } 
	    
}