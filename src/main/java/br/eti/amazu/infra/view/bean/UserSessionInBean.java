package br.eti.amazu.infra.view.bean;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.eti.amazu.infra.util.FacesUtil;
import br.eti.amazu.infra.util.log.Log;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
public class UserSessionInBean implements Serializable {

	private static final long serialVersionUID = -2765013996497920460L;
	
	//Dados de usuario comum do sistema	
	private String pessoaLogged;
	private String userLogged;	
	private boolean usuarioDevLogado;		
	private Locale locale;
	transient FacesUtil facesUtil = new FacesUtil();
		
	@PostConstruct
	public void init(){			
		this.setLocale(facesUtil.getLocale()); //Obtem o idioma local.					
	}		
   
	/* Um botao de acao de uma pagina qualquer pode mudar o idioma,
	 * mas tem de passar um parametro para qual idioma a ser mudado.
	 *------------------------------------------------------------*/
	public void mudarIdioma(){
		String localeStr = facesUtil.getParam("locale");
				
		if(localeStr.length() == 2){
			locale = new Locale(localeStr);
			
		}else{			
			locale = new Locale(localeStr.substring(0,2), localeStr.substring(3));
		}

		facesUtil.setLocale(locale);		
		Log.setLogger(ConfigBean.class, facesUtil.getMessage("MGL064",new String[]{localeStr}), Level.INFO);		
	}
	
	/* *********************
	 * Licao 13 em diante...
	 * Utiliza o componente IdleMonitor do PrimeFaces para invalidar a sessão. 
	 ***********************/
	public void idleListener() {				
		facesUtil.redirect  ("/pages/errorpages/viewExpired");
		facesUtil.sessionInvalidate();			
	}
			
}

