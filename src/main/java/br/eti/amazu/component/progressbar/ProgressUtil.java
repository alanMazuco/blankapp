package br.eti.amazu.component.progressbar;

import java.util.ResourceBundle;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;

public class ProgressUtil {
	
	/* MENSAGENS SEM PARAMETROS
	 * Utilizado intensivamente no processo de internacionalizacao. 
	 * Obtem o valor da mensagem do arquivo properties, passando a chave como parametro.	
	 ---------------------------------------------------------------------------------*/
	public static String getMessage(String key){	
		
		ResourceBundle rs = ResourceBundle.getBundle("messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());

		if(rs.containsKey(key)){
			return rs.getString(key);
		}
		
		return key + ": invalid key";

	}
	
	/* Utilizado para setar uma action em algum componente da pagina, dinamicamente.
	 -----------------------------------------------------------------------------*/
	public static MethodExpression getMethodExpression(String action) {
		FacesContext ctx = FacesContext.getCurrentInstance();				
		
		MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication()
			.getExpressionFactory().createMethodExpression(ctx.getELContext(), action, 
					String.class, new Class[0]);		
		
		return methodExpression;
	}

}
