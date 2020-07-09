package br.eti.amazu.component.progressbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.PrimeFaces;

import br.eti.amazu.component.dialog.DialogBean;
import br.eti.amazu.component.dialog.DialogType;
import br.eti.amazu.infra.util.log.Log;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class ProgressBean implements Serializable {

	private static final long serialVersionUID = -3270934799065389564L;
	
	private static final String MESSAGE_PRB001 = "PRB001";
	
	private int value = 0; //Variavel que incrementa a cada iteracao. Inicia com zero.
	private int pSize = 1; //Tamanho total da progress.	
	private Integer progress;	
	private String status;
	private boolean cancelou;	
	private String actionAfterComplete;
	private String actionAfterCancel;
	private String actionButtonStart;
	private String messageComplete;
	private List<String> resultado;
	
	@Inject
	DialogBean dialogBean;
	
	public void init(String actionButtonStart, String actionAfterComplete, 
				String actionAfterCancel){	
		
		this.reset();
		
		this.actionAfterComplete = actionAfterComplete;
		this.actionAfterCancel = actionAfterCancel;
		this.actionButtonStart = actionButtonStart;
		this.messageComplete = null;		
				
		if(actionButtonStart != null){	
			this.actionButtonStart = "#{" + actionButtonStart + "}";		    
		}
		if(actionAfterComplete != null){	
			this.actionAfterComplete = "#{" + actionAfterComplete + "}";		    
		}
		if(actionAfterCancel != null){	
			this.actionAfterCancel = "#{" + actionAfterCancel + "}";		    
		}				
		
		Log.setLogger(this.getClass(), ProgressUtil.getMessage("PRB002"), Level.INFO);
	}


	public void finit(){		
		PrimeFaces.current().executeScript(
				"PF('pbAjax').cancel();PF('progressbar').hide();PF('startButton').enable();");
		
		Log.setLogger(this.getClass(), ProgressUtil.getMessage(MESSAGE_PRB001), Level.INFO);
		
		this.reset();	
	}
	
	public void onComplete() throws InterruptedException{
		
		Log.setLogger(this.getClass(), ProgressUtil.getMessage(MESSAGE_PRB001), Level.INFO);
		Thread.sleep(800);
		
		dialogBean.addActionMessage(
			messageComplete == null ? ProgressUtil.getMessage("MGL025") : messageComplete,	
				"progressBean.onAfterComplete", null,resultado);		
		
		reset();		
	}
	
	public void onCancel() throws InterruptedException{
			
		setCancelou(true);			
		Log.setLogger(this.getClass(), ProgressUtil.getMessage(MESSAGE_PRB001), Level.INFO);
		Thread.sleep(500);
		
		dialogBean.addActionMessage(ProgressUtil.getMessage("MGL024"),	
				"progressBean.onAfterCancel", null, resultado);	
	}
	
	public void onStart(){
		
		if(actionButtonStart !=null){
			MethodExpression me = ProgressUtil.getMethodExpression(actionButtonStart);
			FacesContext ctx = FacesContext.getCurrentInstance();
			me.invoke(ctx.getELContext(), null);
		}
	}
	
	public void onError(String msg) throws InterruptedException{
				
		setCancelou(true);		
		
		PrimeFaces.current().executeScript(
				"PF('pbAjax').cancel();PF('progressbar').hide();PF('startButton').enable();");
		
		Log.setLogger(this.getClass(), msg, Level.ERROR);			
		Thread.sleep(800);
		this.reset();
		
		dialogBean.addMessage(msg, DialogType.ERROR);
	}
	
	public void onInterrupt(String msg){		
		setCancelou(true);				
		PrimeFaces.current().executeScript("PF('pbAjax').cancel();");
		this.setProgress(100);		
		Log.setLogger(this.getClass(), msg + " - Interrompeu o programa", Level.INFO);
	}
	
	public void onAfterComplete(){
		if(actionAfterComplete !=null){
			MethodExpression me = ProgressUtil.getMethodExpression(actionAfterComplete);
			FacesContext ctx = FacesContext.getCurrentInstance();
			me.invoke(ctx.getELContext(), null);
		}		
	}
	
	public void onAfterCancel(){
		if(actionAfterCancel !=null){
			MethodExpression me = ProgressUtil.getMethodExpression(actionAfterCancel);
			FacesContext ctx = FacesContext.getCurrentInstance();
			me.invoke(ctx.getELContext(), null);
		}
	}
	
	void reset(){		
		setPSize(1);
		setValue(0);
		setProgress(0);
		setStatus("...");	
				
		this.actionAfterComplete = null;
		this.actionAfterCancel = null;
		this.actionButtonStart = null;
		this.messageComplete = null;
		setCancelou(false);
		setResultado(new ArrayList<String>());		
	}
	
		
	/*----------
	 * get/set
	 ----------*/
	public Integer getProgress() {		
		if(progress == null) progress = 0;
		
		else {			
			progress = (value*100)/pSize;			
			if(progress > 100){
				progress = 100;			
			}
		}
		
		return progress;
	}
	
}
