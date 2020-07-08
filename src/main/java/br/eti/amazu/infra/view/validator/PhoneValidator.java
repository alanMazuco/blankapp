package br.eti.amazu.infra.view.validator;

import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.eti.amazu.infra.util.FacesUtil;

@FacesValidator("phoneValidator")
public class PhoneValidator implements Validator<Object> {	
	
	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value)
			throws ValidatorException {
		
		if(value != null  && !value.equals("")){
			
			if(value.toString().length() < 13){
				
				/* pegar o label do componente 
				 * Eh necessario setar no componente o atributo label na view). */
				Map<String, Object> map = new HashMap<String, Object>();
				map = component.getAttributes();
				System.out.println(map.get("label"));
				
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,null,null);
				msg.setSummary(map.get("label") + ": " + FacesUtil.getMessage("CGL164"));
				throw new ValidatorException(msg);
			}
			
		}		
	}	

}