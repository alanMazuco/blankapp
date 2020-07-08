package br.eti.amazu.infra.view.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.eti.amazu.infra.util.FacesUtil;

@FacesValidator("emailValidator")
public class EmailValidator implements Validator<Object> {	
	
	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value)
		throws ValidatorException {
		
		if(value != null  && !value.equals("") && !validateEmail((String) value)){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,null,null);
			msg.setSummary(FacesUtil.getMessage("MGL013"));
		    throw new ValidatorException(msg);
		}
		
	}
	
	public static boolean validateEmail(String email) {
		
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+"); 
		Matcher m = p.matcher(email);
		boolean matchFound = m.matches();

		if (!matchFound) return false;
		return true;
	}		
}