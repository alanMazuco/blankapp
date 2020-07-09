package br.eti.amazu.component.dialog;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.eti.amazu.infra.util.FacesUtil;

@Named
public class DialogListener implements PhaseListener {		
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	DialogBean dialogBean;
				
	public void afterPhase(PhaseEvent event) {			
		
		String severity = new FacesUtil().getSeverity(); 
					
		if(event.getPhaseId() == PhaseId.RENDER_RESPONSE) {
			dialogBean.getLista().clear();
			dialogBean.setUpdate(null);
		}
		if(event.getPhaseId() == PhaseId.RESTORE_VIEW) dialogBean.setAjax(true);
		
		if(severity != null){			
			DialogUtil.normalizeMessages();			
			if(severity.substring(0,4).equals("ERRO")){										
				dialogBean.setActionButtonOkYes(null);
				dialogBean.setActionButtonNo(null);								
				dialogBean.setClosable(true);
				dialogBean.setTipoDialog("ERROR");								
				dialogBean.setHeader(DialogUtil.getMessage("CGL107"));
				PrimeFaces.current().executeScript("PF('dlg').show()");	
			}
		}		
	}	
	public void beforePhase(PhaseEvent event) {
		//not compliance
	}
	
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}	

}
