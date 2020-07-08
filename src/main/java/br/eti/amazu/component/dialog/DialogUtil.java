package br.eti.amazu.component.dialog;

import java.util.Iterator;
import java.util.ResourceBundle;

import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class DialogUtil {	
	
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
	
	/* MENSAGENS COM PARAMETROS
	 * Utilizado intensivamente no processo de internacionalizacao. Obtem o valor 
	* da mensagem do arquivo properties, passando a chave e uma lista de parametros.	
	 --------------------------------------------------------------------------------*/
	public static String getMessage(String key,String[] params){		
		
		ResourceBundle rs = ResourceBundle.getBundle("messages",
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
		FacesContext ctx = FacesContext.getCurrentInstance();				
		
		MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication()
			.getExpressionFactory().createMethodExpression(ctx.getELContext(), action, 
						String.class, new Class[0]);		
		
		return methodExpression;
	}
	
	/* Adiciona a mensagem no contexto. Recebe FacesMessage como parametro.
	  -------------------------------------------------------------------*/
	public static void setMessage(FacesMessage msg){
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	/* Obtem o tipo de mensagem lancada pelo FacesMessage.
	 ---------------------------------------------------*/
	public static String getSeverity(){
		Severity severity = FacesContext.getCurrentInstance().getMaximumSeverity();
		
		if(severity != null) {
			return severity.toString();
		}
		return null;		
	}
	
	/* Corrige algumas anomalias presentes nas mensagens.
	  -------------------------------------------------*/
	public static void normalizeMessages(){		
		Iterator<FacesMessage> it = FacesContext.getCurrentInstance().getMessages();		
	
		int i = 1;
		
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


