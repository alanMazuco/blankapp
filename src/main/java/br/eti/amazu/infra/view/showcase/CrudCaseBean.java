package br.eti.amazu.infra.view.showcase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.eti.amazu.component.dialog.DialogBean;
import br.eti.amazu.component.dialog.DialogType;
import br.eti.amazu.infra.util.FacesUtil;
import br.eti.amazu.infra.view.showcase.vo.Veiculo;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class CrudCaseBean implements Serializable{

	/*Para manter os dados na memoria e simplificar o entendimento,
	 * O proprio bean funcionarah como repositorio de dados e controlador.*/	
	private static final long serialVersionUID = 1L;
	private transient Veiculo veiculo;
	private transient List<Veiculo> listaVeiculos;
	transient FacesUtil facesUtil = new FacesUtil();
	
	@Inject
	DialogBean dialogBean;
			
	public void listarVeiculos(){	
		this.listar();
	}
	
	public void novoVeiculo(){		
		veiculo = new Veiculo();
		this.openDlgVeiculo();
	}
	
	public void  editarVeiculo(Veiculo veiculo){
		this.veiculo = veiculo;
		this.openDlgVeiculo();
	}
	
	public void salvarVeiculo(){
		if (veiculo.getId() == null){
			
			//recupera o id do ultimo veiculo.
			if(listaVeiculos == null){
				listaVeiculos = new ArrayList<>();
			}
			int id = listaVeiculos.size() + 1;			
			veiculo.setId(id);			
			this.incluir(veiculo);		
		}else{
			this.alterar(veiculo);
		}			
		dialogBean.addActionMessage(facesUtil.getMessage("MGL025"), 
				"crudCaseBean.closeDlgVeiculo",":formListaVeiculos:tableVeiculos");
	}
	
	public void closeDlgVeiculo(){
		PrimeFaces.current().executeScript("PF('dlgVeiculo').hide()");
	}
	
	public void openDlgVeiculo(){
		PrimeFaces.current().ajax().update("formVeiculo");
		PrimeFaces.current().executeScript("PF('dlgVeiculo').show()");
	}
	
	public void confirmarExclusao(Veiculo veiculo){		
		this.veiculo = veiculo;
		String[] params ={veiculo.getMarca()+ "/" +veiculo.getModelo()+ "-" + veiculo.getAno()};
		dialogBean.addConfirmMessage(facesUtil.getMessage("MGL038",params), 
				"crudCaseBean.excluirVeiculo", null, ":formListaVeiculos:tableVeiculos");
	}
		
	public void excluirVeiculo(){
		this.excluir();
		String[] params = {veiculo.getMarca()+ "/" + veiculo.getModelo()+"-" +veiculo.getAno()};
		dialogBean.addMessage(facesUtil.getMessage("MGL039",params), DialogType.INFO_CLOSABLE);
	}
		
	/*-------
	 * DAO
	 -------*/	
	public void redirectListaVeiculos(){
		facesUtil.redirect("/pages/showcase/primefaces/crud/veiculos");
	}
	
	public void listar(){
		if(listaVeiculos == null){
			listaVeiculos = new ArrayList<>();
		}		
		this.redirectListaVeiculos();
	}
	
	void incluir(Veiculo veiculo){
		listaVeiculos.add(veiculo);		
	}
	
	void alterar(Veiculo veiculo){
		//Não faz nada, nesse exemplo...
	}	
	
	void excluir(){			
		listaVeiculos.remove(veiculo);	//Remove o veiculo da lista.		
	}
	
}		
