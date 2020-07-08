package br.eti.amazu.infra.view.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.eti.amazu.infra.util.FacesUtil;

@FacesValidator("cpfValidator")
public class CpfValidator implements Validator<Object> {	
	
	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value)
			throws ValidatorException {
		
		if(value != null  && !value.equals("") && !validateCpf((String) value)){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,null,null);
			msg.setSummary(FacesUtil.getMessage("MGL006"));
			throw new ValidatorException(msg);
		}

	}
	
	boolean validateCpf(String cpf) {
		cpf = cpf.replace(".", "");
		cpf = cpf.replace("-", "");

		if (cpf.length() < 11) return false;

		if (cpf.equals("11111111111") || 
				cpf.equals("22222222222") ||
					cpf.equals("33333333333") || 
					cpf.equals("44444444444") ||
					cpf.equals("55555555555") || 
					cpf.equals("66666666666") ||
					cpf.equals("77777777777") || 
					cpf.equals("88888888888") ||
					cpf.equals("99999999999")) {

			return false;
		}

		String num = cpf.substring(0, 9);

		Integer primDig, segDig;
		int soma = 0, peso = 10;
		for (int i = 0; i < num.length(); i++) {
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
		}

		if (soma % 11 == 0 | soma % 11 == 1) {
			primDig = new Integer(0);

		} else {
			primDig = new Integer(11 - (soma % 11));
		}

		soma = 0;
		peso = 11;
		for (int i = 0; i < num.length(); i++) {
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
		}

		soma += primDig.intValue() * 2;
		if (soma % 11 == 0 | soma % 11 == 1) {
			segDig = new Integer(0);

		} else {
			segDig = new Integer(11 - (soma % 11));
		}

		String teste = primDig.toString() + segDig.toString();

		if (!teste.equals(cpf.substring(9, 11)))  return false;

		return true;

	}	
}