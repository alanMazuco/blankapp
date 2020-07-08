package br.eti.amazu.infra.view.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.eti.amazu.infra.util.FacesUtil;

@FacesValidator("cnpjValidator")
public class CnpjValidator implements Validator<Object> {	
	
	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value)
			throws ValidatorException {
		
		if(value != null  && !value.equals("") && !validateCnpj((String) value)){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,null,null);
			msg.setSummary(FacesUtil.getMessage("MGL005"));
			throw new ValidatorException(msg);
		}		
	}
	
	boolean validateCnpj(String cnpj) {
		cnpj = cnpj.replace('.', ' ');
		cnpj = cnpj.replace('/', ' ');
		cnpj = cnpj.replace('-', ' ');
		cnpj = cnpj.replaceAll(" ", "");
		int soma = 0, dig;

		if (cnpj.length() < 14) return false;

		String cnpj_calc = cnpj.substring(0, 12);

		char[] chr_cnpj = cnpj.toCharArray();
		
		for (int i = 0; i < 4; i++) {
			if (chr_cnpj[i] - 48 >=	0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
			}
		}
		
		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
			}
		}
		
		dig = 11 - (soma % 11);
		cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

		soma = 0;
		for (int i = 0; i < 5; i++) {
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
			}
		}
		
		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
			}
		}
		
		dig = 11 - (soma % 11);
		cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

		if (!cnpj.equals(cnpj_calc)) return false;

		return true;
	}
	
}