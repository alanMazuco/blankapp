package br.eti.amazu.infra.view.showcase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.omnifaces.cdi.ViewScoped;

import br.eti.amazu.component.dialog.DialogBean;
import br.eti.amazu.component.dialog.DialogType;
import br.eti.amazu.component.dialog.DialogUtil;
import br.eti.amazu.infra.util.FacesUtil;
import br.eti.amazu.infra.util.log.Ansi;
import br.eti.amazu.infra.util.log.Log;
import br.eti.amazu.infra.view.showcase.vo.Veiculo;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class MessageCaseBean implements Serializable{

	private static final long serialVersionUID = 1L;	
	private transient Veiculo veiculo = new Veiculo();	
	private String txtHello = "Hello Messages";
	private int contador = 0;
	
	@Inject
	DialogBean dialogBean;
			
	public void testarInfoClosable(){ //INFO_CLOSABLE	
		dialogBean.addMessage(DialogUtil.getMessage("MPF003"), DialogType.INFO_CLOSABLE);
	}	
	
	public void testarInfoNotClosable(){ //INFO_NOT_CLOSABLE	
		dialogBean.addMessage(DialogUtil.getMessage("MPF003"),	DialogType.INFO_NOT_CLOSABLE);
	}
	
	public void testarWarn(){ //WARN		
		dialogBean.addMessage(DialogUtil.getMessage("MPF006"), DialogType.WARN);
	}	
	
	public void testarError(){ //ERROR
		dialogBean.addMessage(DialogUtil.getMessage("MPF001"), DialogType.ERROR );
	}
	
	public void testarFatalError(){ //FATAL_ERROR
		dialogBean.addMessage(DialogUtil.getMessage("MPF002"), DialogType.FATAL_ERROR );
	}
	
	public void testarGrowlInfo(){ //GROWL_INFO
		dialogBean.addMessage("Apenas uma informação: A janela não é modal!!!",	
				DialogType.GROWL_INFO);
	}

	public void testarGrowlError(){ //GROWL_ERROR
		dialogBean.addMessage("Há um erro desconhecido - Mostrando em uma janela não modal!!!",
				DialogType.GROWL_ERROR );
	}
	
	public void testarActionMessage(){ //ACTION MESSAGE	
		dialogBean.addActionMessage(DialogUtil.getMessage("MPF004"), 
				"messageCaseBean.printarRetornoBotaoOk",  null);		
	}
		
	public void testarConfirmMessage(){ //CONFIRM MESSAGE
		dialogBean.addConfirmMessage(DialogUtil.getMessage("MPF007"), 
			"messageCaseBean.printarRetornoBotaoYes", 	
				"messageCaseBean.printarRetornoBotaoNo", null);
	}
	
	public void testarWarnConfirmMessage(){ //WARN CONFIRM MESSAGE
		dialogBean.addConfirmWarnMessage("Perigo imediato. Prosseguir assim mesmo?", 
				"messageCaseBean.printarRetornoBotaoYes", 
						"messageCaseBean.printarRetornoBotaoNo", null);
	}
	
	public Integer printarErro(){ //Printar Erro - ERROR - no bloco try-catch
		
		try{			
			throw new ArithmeticException("Divisão por zero não permitida.");
			
		}catch (Exception e){
			dialogBean.addMessage(e.getMessage(), DialogType.ERROR);
		}
		return null;
	}	
	
	public void testarMsgParametrizadas(){ //INFO_CLOSABLE - com parametros
		String[] params = new String[]{veiculo.getMarca(),veiculo.getModelo(), 
				veiculo.getCor(),veiculo.getAno()};	
		
		dialogBean.addMessage(DialogUtil.getMessage("MPF005", params), 
				DialogType.INFO_CLOSABLE,null);
	}

	
	public void testarMsgSemBundle(){ //INFO_CLOSABLE - sem bundle
		dialogBean.addMessage("Mensagem transmitida diretamente sem o uso de resource bundle. "+
			"Não pode ser internacionalizada.", DialogType.INFO_CLOSABLE);
	}
	
	public void testarMsgComLista(){ //INFO_CLOSABLE com lista
		List<String> lista = new ArrayList<>();		
		lista.add(DialogUtil.getMessage("MPF011"));
		lista.add(DialogUtil.getMessage("MPF012"));
		lista.add(DialogUtil.getMessage("MPF013"));	
		
		dialogBean.addMessage(DialogUtil.getMessage("CGL100"), DialogType.INFO_CLOSABLE, lista);
	}
	
	public void testarActionMessageWarnVarianteComLista(){ //ACTION MESSAGE + WARN + lista
		List<String> lista = new ArrayList<>();		
		lista.add(DialogUtil.getMessage("MPF011"));
		lista.add(DialogUtil.getMessage("MPF012"));
		lista.add(DialogUtil.getMessage("MPF013"));		
		
		dialogBean.addActionMessage(DialogUtil.getMessage("MPF004"), 
				"messageCaseBean.printarRetornoBotaoOk", DialogType.WARN,  null, lista);		
	}
	
	public void testarUpdate(){ // update - tambem mostrando uma ACTION MESSAGE
		contador++;
		txtHello = "Hello Messages - SOFREU UPDATE-" + contador;
		
		dialogBean.addActionMessage("Componente txtHello sofrerá update", null, 
				":formMessages:txtHello,:formMessages:panelContador");
	}
		
	public void testarRedirect(){ // redirecionando para outra pagina...
		dialogBean.addActionMessage("Você será direcionado para o Show Case", 
				"messageCaseBean.retornarHomeShowCase",null);
	}
	
	//Metodos usados para testar os botoes...
	public void printarRetornoBotaoOk(){
		Log.setLogger(this.getClass(), DialogUtil.getMessage("MPF008"), Level.INFO);
	}	
	
	public void printarRetornoBotaoYes(){
		Log.setLogger(this.getClass(), DialogUtil.getMessage("MPF009"), Level.INFO);
	}	
	
	public void printarRetornoBotaoNo(){
		Log.setLogger(this.getClass(), DialogUtil.getMessage("MPF010"), Level.INFO);
	}
	
	//metodo usado para testar o redirect.
	public void retornarHomeShowCase(){
		Log.setLogger(this.getClass(), "...faz alguma coisa e redireciona.", Level.INFO);
		new FacesUtil().redirect("/pages/showcase/homeShowCase");
	}

	public void testarLogs(){	
		
		Log.setLogger(this.getClass(), "Printando Level TRACE", Level.TRACE);
		Log.setLogger(this.getClass(), "Isto é um log com Level DEBUG", Level.DEBUG);		
		Log.setLogger(this.getClass(), "Imprimindo Level INFO", Level.INFO);
		
		Log.setLogger(this.getClass(), "Imprimindo Level INFO - preto negrito", Level.INFO, 
					Ansi.BLACK_BOLD);
		
		Log.setLogger(this.getClass(), "Imprimindo Level INFO - preto itálico", Level.INFO, 
					Ansi.BLACK_ITALIC);
		
		Log.setLogger(this.getClass(), "Imprimindo Level INFO - preto fundo amarelo", Level.INFO,
					Ansi.BLACK_YELLOWBG);
		
		Log.setLogger(this.getClass(), "Imprimindo Level INFO - azul", Level.INFO, Ansi.BLUE);
		Log.setLogger(this.getClass(), "Imprimindo Level INFO - ciano", Level.INFO, Ansi.CYAN);
		Log.setLogger(this.getClass(), "Imprimindo Level INFO - verde", Level.INFO, Ansi.GREEN);
		Log.setLogger(this.getClass(), "Imprimindo Level INFO - púrpura", Level.INFO, Ansi.PURPLE);
		Log.setLogger(this.getClass(), "Imprimindo Level INFO - vermelho", Level.INFO, Ansi.RED);
		
		Log.setLogger(this.getClass(), "Imprimindo Level INFO - vermelho negrito fundo amarelo",
					Level.INFO, Ansi.RED_BOLD_YELLOWBG);
		
		Log.setLogger(this.getClass(), "Imprimindo Level INFO - amarelo", Level.INFO, Ansi.YELLOW);		
		Log.setLogger(this.getClass(), "Log de Level WARN", Level.WARN);
		Log.setLogger(this.getClass(), "Penúltimo Level ERROR", Level.ERROR);
		Log.setLogger(this.getClass(), "Print com o último Level FATAL", Level.FATAL);		
	}	
	
}
