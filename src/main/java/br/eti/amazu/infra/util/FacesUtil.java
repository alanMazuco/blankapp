package br.eti.amazu.infra.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;

import br.eti.amazu.infra.exception.NullContextException;
import br.eti.amazu.infra.util.log.Log;

public class FacesUtil {	
		
	private static final String MSG_MESSAGES = "messages";
		
	private static final List<String> doResetEjb = Arrays.asList(
			"menubean",
			"conversationidgenerator", 
			"conversations", 
			"charset", 
			"lockstore", 
			"activeviewcontexts", 
			"activeviewmaps", 
			"logicalviewmap", 
			"weld"			
			);
		
	/* Lista todos os beans que estao gerenciados em sessao.
	 -----------------------------------------------------*/
	public List<String> listarBeansInSession(){
		List<String> listaBeansInSession = new ArrayList<>();		
		Map<String, Object> beanMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Iterator<?> it = beanMap.entrySet().iterator();		
		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, Object> par =  (Entry<String, Object>) it.next();	
			listaBeansInSession.add(par.getKey());
		}
		return listaBeansInSession;
	}
	
	/* Retira todos os beans da sessao, exceto beanNotReset, o que não serah resetado.
	 -------------------------------------------------------------------------------*/
	public void resetBeans(String beanNotReset){
		
		Map<String, Object> 
				beanMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		Iterator<?> it = beanMap.entrySet().iterator();	
		
		while (it.hasNext()) {			
			@SuppressWarnings("unchecked")
			Map.Entry<String, Object> par =  (Entry<String, Object>) it.next();			
						
			//Nao resetar o bean "marcado para nao ser resetado"
			//Nao resetar os beans default do EJB			
			if ( (beanNotReset!=null && par.getKey().toLowerCase().contains(beanNotReset.toLowerCase()))
					||
					doResetEjb.contains(par.getKey().toLowerCase())
					) {
				
				continue;	
			}				
	
			beanMap.remove(par.getKey());
			beanMap.remove(par.getValue());	
		}				
	}
			
	/* Invalidar a sessao.
	 -------------------*/
	public void sessionInvalidate() {		
		HttpSession session = (HttpSession) 
					FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		session.invalidate(); 
	}
	
	/* retorna o parametro existente no request (lancado pela pagina).
	 ---------------------------------------------------------------*/
	public String getParam(String param){		
		return FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get(param);
	}
			
	/* Recuperando o idioma
	 ---------------------*/
	public Locale getLocale(){			
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}
		
	/* Mudando o idioma
	 ----------------*/
	public void setLocale(Locale locale){		
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}
		
	/* Obtem uma lista de todos os arquivos messages.properties do sistema. 
	 * Esses arquivos teem de estar sob o diretorio source da app
	 ----------------------------------------------------------*/
	public List<String> getLocales(){
		List<String> locales = new ArrayList<>();		
		FacesContext ctx = FacesContext.getCurrentInstance();		
		Iterator<?> it = ctx.getApplication().getSupportedLocales();	
		
		locales.add(ctx.getApplication().getDefaultLocale().toString());	
		while (it.hasNext()) {
			locales.add(it.next().toString());
		}		
		return locales;		
	}
	
	/* MENSAGENS SEM PARAMETROS
	 * Utilizado intensivamente no processo de internacionalizacao. 
	 * Obtem o valor da mensagem do arquivo properties passando a chave.	
	 -----------------------------------------------------------------*/
	public String getMessage(String key){
		ResourceBundle rs = ResourceBundle.getBundle(MSG_MESSAGES,
				FacesContext.getCurrentInstance().getViewRoot().getLocale());		

		if(rs.containsKey(key)) return rs.getString(key);
		return key + ": invalid key";
	}
	/* MENSAGENS COM PARAMETROS
	 * Utilizado intensivamente no processo de internacionalizacao. Obtem o valor da 
  * mensagem do arquivo properties, passando a chave e uma lista de parametros.	
	 ---------------------------- ---------------------------------------------*/
	public String getMessage(String key,String[] parameters){				
		
		String result;
		
		ResourceBundle rs = ResourceBundle.getBundle(MSG_MESSAGES,
				FacesContext.getCurrentInstance().getViewRoot().getLocale());		
		
		result = rs.containsKey(key) ? rs.getString(key) : key + ": invalid key";
					
		for (int i = 0; i < parameters.length; i++) {
			String regex = "{" + i + "}";
			String param = parameters[i];			
			result = result.replace(regex, param);
		}		
		return result;
	}	
	
	/* Retorna a key de alguma string de mensagem.
	 -------------------------------------------*/
	public String getKey(String message){			
		ResourceBundle rs = ResourceBundle.getBundle(MSG_MESSAGES,
				FacesContext.getCurrentInstance().getViewRoot().getLocale());			
		
		List<String> keys = Collections.list(rs.getKeys());
		
		for(String key : keys) {			
			if(rs.getString(key).equals(message)) {
				return key;
			}
		}
		return null;
	}

	/* Utilizado para setar uma action em algum componente da pagina, dinamicamente.
	 -----------------------------------------------------------------------------*/
	public MethodExpression getMethodExpression(String action) {
		FacesContext ctx = FacesContext.getCurrentInstance();		
		
		return FacesContext.getCurrentInstance().getApplication()
						.getExpressionFactory().createMethodExpression(ctx.getELContext(), action, 
									String.class, new Class[0]);		
	}
	
	/* Adiciona a mensagem no contexto. Recebe FacesMessage como parametro.
	 --------------------------------------------------------------------*/
	public void setMessage(FacesMessage msg){
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/* Obtem o tipo de mensagem lancada pelo FacesMessage.
	 ---------------------------------------------------*/
	public String getSeverity(){
		Severity severity = FacesContext.getCurrentInstance().getMaximumSeverity();
		if(severity != null) return severity.toString();
		return null;
	}

	/* Retorna o caminho completo de algum recurso. Recebe o caminho do contexto web:
	 * Este param pode ser algo do tipo:"/WEB-INF/classes", por exemplo,
	 * e devolve algo do tipo: "C:/cursoJava/blankapp/webapp/WEB-INF/classes
	 ----------------------------------------------------------------------*/
	public String getFullPath(String path){		
		ServletContext servletContext =  (ServletContext)FacesContext.
				getCurrentInstance().getExternalContext().getContext();	
	
		return servletContext.getRealPath(path);
	}
	
	/* Retorna o nome do contexto precedido da barra "/"	 
	 -------------------------------------------------*/
	public String getContextName() {		
        return FacesContext.getCurrentInstance().getExternalContext().
										getRequestContextPath();
	}		
	
	/* Obtem o tipo de mensagem lancada pelo FacesMessage.
	 ---------------------------------------------------*/
	public boolean containsMsg(String key){
		List<FacesMessage> list = FacesContext.getCurrentInstance().getMessageList();		

		for(FacesMessage fm:list){
			if(fm.getSummary().contains(key) && fm.getSeverity().toString().contains("WARN"))
				return true;
		}		

		return false;
	}
	
	/* Obtem todas as mensagens setadas no FacesMessage.
	 -------------------------------------------------*/
	public List<FacesMessage> getMessageList(){
		return FacesContext.getCurrentInstance().getMessageList();
	}
	
	/* Obtem o tipo de mensagem do FacesMessage.
	 -----------------------------------------*/
	public Severity getMaximumSeverity(){
		return FacesContext.getCurrentInstance().getMaximumSeverity();
	}
	
	/* Retorna o viewRoot (recurso sendo visualizado no momento).
	 ----------------------------------------------------------*/
	public String getViewRoot(){
		return FacesContext.getCurrentInstance().getViewRoot().getViewId();
	}
	
	/* Redireciona para uma pagina qualquer.
	 -------------------------------------*/
	public void redirect(String url) {
		try {			
				
			if(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() == null) {
				throw new NullContextException("O contexto não pode ser nulo");
				
			}else {
				FacesContext.getCurrentInstance().getExternalContext().redirect(getContextName() + url + ".faces");
				FacesContext.getCurrentInstance().responseComplete();		
			}
			
		} catch (Exception e) {
			Log.setLogger(FacesUtil.class, e.getMessage(), Level.ERROR);
		}
	}
	
	/* Redireciona para uma pagina qualquer (forward).
	 -----------------------------------------------*/
	public void forward(String url){
		FacesContext ctx = FacesContext.getCurrentInstance();
		ctx.getApplication().getNavigationHandler().handleNavigation(ctx, null, url);
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	/* Obtem a url renderizada no momento.
	 -----------------------------------*/
	public String getUrlInView(){
		FacesContext ctx = FacesContext.getCurrentInstance();			
	
		return ctx.getViewRoot().getViewId()
				.substring(0, ctx.getViewRoot().getViewId().indexOf(".xhtml")); 
	}
	
	/*guardar um objeto na sessao
	 --------------------------*/
	public void putObjectInSession(String key, Object value){
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false); 
		session.setAttribute(key, value); 
	}
	
	/*recuperar um objeto da sessao
	 *  -------------------------*/
	public Object getObjectFromSession(String key){
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false); 
	  	Object value = session.getAttribute(key);        
	  	session.removeAttribute(key);
	  	return value;
	}
	
	/*apagar um objeto da sessao
	 *  ----------------------*/
	public void removeObjectTheSession(String key){
		FacesContext ctx = FacesContext.getCurrentInstance();

		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false);
        session.removeAttribute(key);        
	}		

}
