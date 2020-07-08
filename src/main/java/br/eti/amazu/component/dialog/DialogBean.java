package br.eti.amazu.component.dialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.MethodExpression;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;

import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
@Getter
@Setter
public class DialogBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	FacesMessage message; //...................A mensagem a ser impressa na tela.
	private boolean closable; //...............Se a janela conterá um botao "close".
	private String header; //..................A string na barra e titulo.	
	private String tipoDialog; //..............O tipo do dialog.
	private String actionButtonOkYes;  //..... A action do botao SIM ou OK.
	private String actionButtonNo; //..........A action do botao NAO
	private String update; //..................O id que sofreah update apos processar.
	private boolean ajax = true; //............Se o processamento  serah Ajax ou nao.
	private boolean confirmWarn = false; //....Se a mensagem eh tipo CONFIRM WARN ou nao.
	private List<String> lista = new ArrayList<>();//....A lista a ser exibida.

	/* 1) MESSAGE
	 *    Trata-se de um metodo com duas assinaturas:
	 *    - 1a ASSINATURA - 3 parametros (bundle, dialogType e lista)
	 *    - 2a ASSINATURA - 2 parametros (bundle, dialogType).
	 *    Ambas chamam o metodo setAddMessage...
	 -----------------------------------------*/
	public void addMessage(String bundle, DialogType dialogType, List<String> lista) {	
		this.setAddMessage(bundle, dialogType, lista);		
	}
	
	public void addMessage(String bundle, DialogType dialogType) {	
		this.setAddMessage(bundle, dialogType, null);		
	}
		
	private void setAddMessage(String bundle, DialogType dialogType, List<String> lista){		
		//Inicializam-se as variaveis...
		tipoDialog = dialogType.toString();
		actionButtonOkYes = null;
		actionButtonNo = null;		
		closable = true;
		confirmWarn = false;
		
		//Retiro a action da sessao
		this.removeObjectTheSession("actionButtonOkYes");
		
		//Inicializa a lista - ela pode vir nula, e a mensagem em DialogUtil.
		this.lista = new ArrayList<String>();
		if(lista != null) this.lista = lista;		
		setHeaderAndMessageInDialogType(dialogType, bundle);
		DialogUtil.setMessage(message);		
		
		/* Exibe o dialog. (Se o tipo da mensagem contem a string GROWL,
		 * exibe o dialog do tipo GROWL nao modal). */
		if(tipoDialog.contains("GROWL")){
			this.showGrowl();
			
		}else{			
			this.showDialog();
		}

	}	
	
	/* 2) ACTION MESSAGE
	 *  Trata-se de um metodo com tres assinaturas:
	 *  - 1a ASSINATURA - 5 parametros (bundle, actionButtonOk, dialogType, update e lista).
	 *  - 2a ASSINATURA - 4 parametros (bundle, actionButtonOk, update, lista).
	 *  - 3a ASSINATURA - 3 parametros (bundle, actionButtonOk, update).
	 *     
	 *  A primeira assinatura admite as variacoes de subtipos (INFO_CLOSABLE, WARN, etc).
	 *  Todas possuem o parametro update, que eh fundamental para o programador.
	 *  ...E ainda pode customizar uma lista para ser impressa no corpo da mensagem.
	 *  Todas chamam o metodo setAddActionMessage...
	 ---------------------------------------------*/
	public void addActionMessage(String bundle, String actionButtonOk, 
			DialogType dialogType, String update, List<String> lista) {			
		this.setActionMessage(bundle, actionButtonOk, dialogType, update, lista);		
	}
	
	public void addActionMessage(String bundle, String actionButtonOk, String update, 
			List<String> lista) {		
		this.setActionMessage(bundle,actionButtonOk, null, update, lista);		
	}
	
	public void addActionMessage(String bundle, String actionButtonOk, String update) {	
		this.setActionMessage(bundle,actionButtonOk, null, update, null);			
	}
	
	private void setActionMessage(String bundle, String actionButtonOkYes, 
			DialogType dialogType, String update, List<String> lista) {		
		
		//Define o tipo do dialog (perceba que ele pode vir nulo aqui).
		if(dialogType != null) {
			tipoDialog = dialogType.toString();
			
		}else{
			tipoDialog = "INFO_NOT_CLOSABLE"; //Consulte a classe TipoDialog.
		}
		
		//Inicializa variaveis.
		this.actionButtonOkYes = null;
		this.actionButtonNo = null;
		this.update = null;
		closable=false;			
		this.lista = new ArrayList<String>();
		if(lista != null) this.lista = lista;		
		header=DialogUtil.getMessage("CGL109");		
		
		//ACTION MESSAGE sempre do tipo SEVERITY_INFO
		message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle, null);	
		confirmWarn = false;
		//Definindo a acao do botao YES ou do botao OK
		if(actionButtonOkYes != null) {
			this.actionButtonOkYes = "#{" + actionButtonOkYes + "}";
			this.putObjectInSession("actionButtonOkYes", "#{" + actionButtonOkYes + "}");
		}		
		//Definindo a mensagem
		if(update != null) this.update = update;		
		if(dialogType != null) setHeaderAndMessageInDialogType(dialogType, bundle);		
		DialogUtil.setMessage(message);		

		//Exibe o dialog.	
		this.showDialog(); 	
	}
		
	/* 3) CONFIRM MESSAGE
	 *  Trata-se de um metodo com duas assinaturas:
	 *  - 1a ASSINATURA-5 parametros (bundle,actionButtonYes,actionButtonNo,update e lista).
	 *  - 2a ASSINATURA - 4 parametros (bundle, actionButtonYes, actionButtonNo, update).     
	 *  Perceba que o tipo de mensagem nao admite variacoes de subtipos.	
	 *  Todas chamam o metodo setAddConfirmMessage...
	 -------------------------------------------------*/
	public void addConfirmMessage(String bundle,String actionButtonYes,String actionButtonNo, 
			String update, List<String> lista){
		 this.setAddConfirmMessage(bundle,actionButtonYes,actionButtonNo,update,lista, false);	
	}
	
	public void addConfirmMessage(String bundle, String actionButtonYes, 
			String actionButtonNo, String update){
		 this.setAddConfirmMessage(bundle,actionButtonYes,actionButtonNo, update, null, false);	
	}	
	
	/* 4) WARN CONFIRM MESSAGE
	 *     Trata-se de um metodo com apenas umas assinatura:
	 *     - ASSINATURA - 4 parametros (bundle, actionButtonYes, actionButtonNo, update).	  	
	 *     Perceba que o tipo de mensagem nao admite variacoes de subtipos.	
	 *     Todas chamam o metodo setAddConfirmMessage...
	 -------------------------------------------------*/	
	public void addConfirmWarnMessage(String bundle, String actionButtonYes, 
			String actionButtonNo, String update){
		 this.setAddConfirmMessage(bundle,actionButtonYes,actionButtonNo, update, null, true);	
	}
	
	private void setAddConfirmMessage(String bundle, String actionButtonOkYes,
			String actionButtonNo, String update, List<String> lista, Boolean warn){				
		
		 //O que define se serah CONFIRM MESSAGE ou WARN CONFIRM MESSAGE eh o param "warn".
		
		//Inicializa variaveis
		this.actionButtonOkYes = null;
		this.actionButtonNo = null;
		this.update = null;
		closable=true;
		this.lista = new ArrayList<String>();
		if(lista != null) this.lista = lista;			

		//Definindo a mensagem
		message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle, null);
		header=DialogUtil.getMessage("CGL111");	
		
		//definindo a mensagem usando o parametro "warn".
		this.confirmWarn = warn; 
		//Setando as actions dos botoes.
		if(actionButtonNo != null) {
			this.actionButtonNo = "#{" + actionButtonNo + "}";	
			this.putObjectInSession("actionButtonNo", "#{" + actionButtonNo + "}");
		}
		
		if(actionButtonOkYes != null) {
			this.actionButtonOkYes = "#{" + actionButtonOkYes + "}";
			this.putObjectInSession("actionButtonOkYes", "#{" + actionButtonOkYes + "}");
		}	
		
		if(update != null) this.update = update;
		DialogUtil.setMessage(message);								
		showDialogConfirm(); //acionando o dialog
	}
	
	/*Action do botao OK ou YES.
	 -------------------------*/
	public void actionOkYes(){
		
		if(actionButtonOkYes==null){
			actionButtonOkYes = (String) this.getObjectFromSession("actionButtonOkYes");
		}
		
		if(actionButtonOkYes !=null){
			MethodExpression me = DialogUtil.getMethodExpression(actionButtonOkYes);			
			me.invoke(FacesContext.getCurrentInstance().getELContext(), null);				
		}
		this.update();
	}
	
	/*Action do botao NO.
	 ------------------*/
	public void actionNo(){		
		if(actionButtonNo==null){
			actionButtonNo = (String) this.getObjectFromSession("actionButtonNo");
		}		

		if(actionButtonNo !=null){
			MethodExpression me = DialogUtil.getMethodExpression(actionButtonNo);			
			me.invoke(FacesContext.getCurrentInstance().getELContext(), null);
		}
		this.update();
	}
		
	/*Apresenta o dialog.
	 * Lembrando que existe tres tipos de dialog:
	 * - dialog (existe uma xhtml para ele);
	 * - confirmDialog (tambem existe uma xhtml para ele).
	 * - dlgGrowl (estah dentro de dialog.xhtml).
	 ------------------------------------------*/
	void showDialog(){	
		PrimeFaces.current().ajax().update("dialog");
		PrimeFaces.current().ajax().update("gridLista");
		PrimeFaces.current().executeScript("PF('dlg').show()");
	}

	void showDialogConfirm(){
		PrimeFaces.current().ajax().update("dialogConfirm");
		PrimeFaces.current().ajax().update("gridListaConfirm");
		PrimeFaces.current().executeScript("PF('dlgConfirm').show()");
	}
	
	/*Apresenta o growl. 
	 -----------------*/
	void showGrowl(){	
		PrimeFaces.current().ajax().update("dialog");
		PrimeFaces.current().ajax().update("gridLista1");
		PrimeFaces.current().executeScript("PF('dlgGrowl').show()");
	}
	
	/*Update das acoes OK/YES/NO.
	 --------------------------*/
	void update(){
		if(this.update != null){
			String[] updates = update.split(",");
			List<String> listUpdates = new ArrayList<String>();
			for(String str:updates){
				listUpdates.add(str.trim());
			}
			PrimeFaces.current().ajax().update(listUpdates);
		}
	}
	
	//Metodo que define as SEVERITY das mensagens (subtipos)
	void setHeaderAndMessageInDialogType(DialogType dialogType, String bundle){		
		switch(dialogType){		
			case INFO_CLOSABLE:
				header=DialogUtil.getMessage("CGL110");
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle, null);
				break;				
				
			case INFO_NOT_CLOSABLE:	
				closable = false;
				header=DialogUtil.getMessage("CGL110");
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle, null);
				break;	
				
			case WARN:
				header=DialogUtil.getMessage("CGL112");
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, bundle, null);	
				break;		
				
			case ERROR:				
				header=DialogUtil.getMessage("CGL107");
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle, null);		
				break;	
				
			case FATAL_ERROR:
				header=DialogUtil.getMessage("CGL108");
				message = new FacesMessage(FacesMessage.SEVERITY_FATAL, bundle, null);	
				break;
				
			case GROWL_INFO:
				header=DialogUtil.getMessage("CGL110");
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle, null);
				break;
				
			case GROWL_ERROR:				
				header=DialogUtil.getMessage("CGL166");
				message = new FacesMessage(FacesMessage.SEVERITY_FATAL, bundle, null);		
				break;
		}		
	}
	
	/*guardar um objeto na sessao
	 --------------------------*/
	void putObjectInSession(String key, Object value){		

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().
				getExternalContext().getSession(true); 
		
        session.setAttribute(key, value); 
	}
	
	/*apagar um objeto da sessao
	 -------------------------*/
	void removeObjectTheSession(String key){		

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().
				getExternalContext().getSession(true);
		
        session.removeAttribute(key);        
	}
	
	/*recuperar um objeto da sessao
	 -----------------------------*/
	Object getObjectFromSession(String key){		

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().
				getExternalContext().getSession(true); 
		
        Object value = session.getAttribute(key);        
        session.removeAttribute(key);        
        return value;
	}
	
}
