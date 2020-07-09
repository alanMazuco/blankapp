package br.eti.amazu.infra.view.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;

import org.apache.commons.validator.routines.EmailValidator;

import br.eti.amazu.infra.util.FacesUtil;

@FacesValidator("emailValidator")
public class MailValidator implements Validator<Object> {	
	
	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value) {
		
		if(value != null  && !value.equals("") && !validateEmail((String) value)){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,null,null);
			msg.setSummary(new FacesUtil().getMessage("MGL013"));
			ctx.addMessage(component.getClientId(), msg);
		}		
	}
	
	public static boolean validateEmail(String email) {		
		return EmailValidator.getInstance().isValid(email);		
	}		
}