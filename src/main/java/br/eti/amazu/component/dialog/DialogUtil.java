package br.eti.amazu.component.dialog;

import java.util.Iterator;
import java.util.ResourceBundle;

import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class DialogUtil {	
	
	private static final String MSG_MESSAGES = "messages";
	
	private DialogUtil() {
		//do not compliance
	}
	
	/* MENSAGENS SEM PARAMETROS
	 * Utilizado intensivamente no processo de internacionalizacao. 
	 * Obtem o valor da mensagem do arquivo properties, passando a chave como parametro.	
	 ---------------------------------------------------------------------------------*/
	public static String getMessage(String key){	
		
		ResourceBundle rs = ResourceBundle.getBundle(MSG_MESSAGES,
				getFacesContext().getViewRoot().getLocale());

		if(rs.containsKey(key)){
			return rs.getString(key);
		}
		return key + ": invalid key";
	}
	
	/* MENSAGENS COM PARAMETROS
	 * Utilizado intensivamente no processo de internacionalizacao. Obtem o valor da 
  * mensagem do arquivo properties, passando a chave e uma lista de parametros.	
	 ---------------------------- ---------------------------------------------*/
	public static String getMessage(String key,String[] params){				
		ResourceBundle rs = ResourceBundle.getBundle(MSG_MESSAGES,
				FacesContext.getCurrentInstance().getViewRoot().getLocale());		
		String msg;		
		if(rs.containsKey(key)){
			msg = rs.getString(key);
			
		}else{
			msg = key + ": invalid key";
		}
			
		for (int i = 0; i < params.length; i++) {
			String param = params[i];
			String regex = "{" + i + "}";
			msg = msg.replace(regex, param);
		}		
		return msg;
	}
	
	/* Utilizado para setar uma action em algum componente da pagina, dinamicamente.
	 -----------------------------------------------------------------------------*/
	public static MethodExpression getMethodExpression(String action) {					
		
		return FacesContext.getCurrentInstance().getApplication()
			.getExpressionFactory().createMethodExpression(getFacesContext().getELContext(), action, 
						String.class, new Class[0]);
	}
	
	/* Adiciona a mensagem no contexto. Recebe FacesMessage como parametro.
	  -------------------------------------------------------------------*/
	public static void setMessage(FacesMessage msg){
		getFacesContext().addMessage(null, msg);
	}
	
	/* Obtem o tipo de mensagem lancada pelo FacesMessage.
	 ---------------------------------------------------*/
	public static String getSeverity(){
		return getFacesContext().getMaximumSeverity().toString();
	}
	
	/* Corrige algumas anomalias presentes nas mensagens.
	  -------------------------------------------------*/
	public static void normalizeMessages(){		
		Iterator<FacesMessage> it = getFacesContext().getMessages();		
	
		int i = 1;
		
		if(it != null) {
			while(it.hasNext()){
				FacesMessage m = it.next();
				
				if(m.getSummary().contains("Please add <h:form>")) {
					it.remove();
					continue;
				}				
		
				if(i >=2) {
					it.remove();			
				}			
		
				i++;
			}	
		}
	}	
	
	static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

}