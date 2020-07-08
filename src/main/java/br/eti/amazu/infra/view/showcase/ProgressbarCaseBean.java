package br.eti.amazu.infra.view.showcase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.eti.amazu.component.progressbar.ProgressBean;
import br.eti.amazu.infra.util.FacesUtil;


@Named
@RequestScoped
public class ProgressbarCaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	ProgressBean progressBean;
	
	public void testarProgressbar(){		
		progressBean.init("progressbarCaseBean.iterar", null, null);
	}
	
	public void iterar(){		
		
		int i = 1; //pode aqui representar o index de uma linha da ArrayList
		int a = 500; //pode aqui representar o tamanho de uma ArrayList
		
		progressBean.setPSize(a);

		while (i<=a){ //enquanto o index for menor que o tamanho...		
	
			if (progressBean.isCancelou()){ //enquanto o usuário não cancelar...
				System.out.println("Interrompeu o processo!");				
				List<String> resultado = new ArrayList<String>();
				resultado.add(FacesUtil.getMessage("MGL063"));
				progressBean.setResultado(resultado);				
				return;
			}

			progressBean.setValue(i);//atualiza	a progressbar	
			progressBean.setStatus("iteration: " + i);
			timeout(10);//tempo em milissegundos			
			i++;
		}				
	}	
			
	//Fornece um timer para o funcionamento correto da iteracao.
	public void timeout(long timeMilisseconds) {
		long t0 = System.currentTimeMillis();

		while (System.currentTimeMillis() - t0 < timeMilisseconds) {
		}

	}	
	
}