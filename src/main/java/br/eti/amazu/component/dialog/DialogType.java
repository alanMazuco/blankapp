package br.eti.amazu.component.dialog;

/* Esta classe define os subtipos de mensagens referentes 
 * ao tipo MESSAGE. Os outros tres tipos (ACTION MESSAGE, 
 * CONFIRM MESSAGE e WARN CONFIRM  MESSAGE) 
 * dispensa o seu uso.*/
public enum DialogType {	
	
	INFO_CLOSABLE  (DialogUtil.getMessage("CGL110")), //....INFORMACAO
	INFO_NOT_CLOSABLE (DialogUtil.getMessage("CGL110")), //.INFORMACAO
	WARN  (DialogUtil.getMessage("CGL112")), //.............CUIDADO
	ERROR (DialogUtil.getMessage("CGL107")), //.............ERRO	
	FATAL_ERROR (DialogUtil.getMessage("CGL108")), //.......ERRO FATAL
	GROWL_INFO  (DialogUtil.getMessage("CGL167")), //.......INFORMACOES
	GROWL_ERROR  (DialogUtil.getMessage("CGL166")); //......INFORMACOES - ERRO
			
	private String dialog;	
	
	DialogType(String dialog) {
		this.dialog = dialog;		
	}

	/*--------
	 * get/set
	 ---------*/
	public String getDialog() {
		return dialog;
	}

	public void setDialog(String dialog) {
		this.dialog = dialog;
	}	

}
