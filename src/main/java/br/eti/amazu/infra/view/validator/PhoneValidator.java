package br.eti.amazu.infra.view.validator;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;

import org.apache.log4j.Level;

import br.eti.amazu.infra.util.FacesUtil;
import br.eti.amazu.infra.util.log.Log;

@FacesValidator("phoneValidator")
public class PhoneValidator implements Validator<Object> {	
	
	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value) {
		
		if(value != null  && !value.equals("") && value.toString().length() < 13){
				
			/* pegar o label do componente 
			 * Eh necessario setar no componente o atributo label na view). */
			Map<String, Object> map = component.getAttributes();
			Log.setLogger(this.getClass(), map.get("label").toString(), Level.INFO);
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,null,null);
			msg.setSummary(map.get("label") + ": " + new FacesUtil().getMessage("CGL164"));
			ctx.addMessage(component.getClientId(), msg);			
		}		
	}	

}