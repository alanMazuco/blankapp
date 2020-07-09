package br.eti.amazu.infra.view.showcase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.eti.amazu.infra.util.RegexUtil;
import br.eti.amazu.infra.util.log.Log;
import br.eti.amazu.infra.view.showcase.vo.ValueTest;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class ConvValidatorCaseBean implements Serializable{

	private static final long serialVersionUID = 8291712218885480537L;
	
	private ValueTest valueTest = new ValueTest();
	private List<ValueTest> valueTests = new ArrayList<>();

	@PostConstruct
	public void init(){		
		
		//setando os atributos do objeto nº 1.
		ValueTest valueTest1 = new ValueTest();
		valueTest1.setId(1L);
		valueTest1.setCep("11999900");
		valueTest1.setCnpj("99.999.999/0001-00");
		valueTest1.setCpf("999.999.000-99");	
		valueTest1.setDateValue(new Date());
		valueTest1.setDoubleValue(25.155678d);
		valueTest1.setMoeda(25.10d);
		valueTest1.setEmail("fulano@hot.com");
		valueTest1.setIntValue(50);
		valueTest1.setIntValue4(5000);
		valueTest1.setLongValue(1000L);
		valueTest1.setStrValue("O Poderoso Chefão");
		valueTest1.setStrValue6("tomate");
		valueTest1.setStrValueNoSpaceBlank("carro");
		valueTest1.setTel("(11)1111-1111");
		
		//setando os atributos do objeto nº 2.
		ValueTest valueTest2 = new ValueTest();
		valueTest2.setId(2L);
		valueTest2.setCep("22222222");
		valueTest2.setCnpj("88.888.888/0001-00");
		valueTest2.setCpf("111.111.111-11");	
		valueTest2.setDateValue(new Date());
		valueTest2.setDoubleValue(1.3333d);
		valueTest2.setMoeda(4.30d);
		valueTest2.setEmail("beltrano@gmail.com");
		valueTest2.setIntValue(10);
		valueTest2.setIntValue4(1000);
		valueTest2.setLongValue(7000L);
		valueTest2.setStrValue("Casa Blanca");
		valueTest2.setStrValue6("batata");
		valueTest2.setStrValueNoSpaceBlank("moto");
		valueTest2.setTel("(22)2222-2222");
		
		/* adiciona os dois objetos na lista.
		 * Esta lista serah utilizana em uma selectOne da view. */
		valueTests.add(valueTest1); 
		valueTests.add(valueTest2);
	}
	
	/*
	 * Este metodo comprova a conversao do objeto valueTest,(convertido por objectConverter)
	 * Para ratificar, exclua o conversor da pagina e tente novamente, vai dar erro.*/
	public void setarValueTest(javax.faces.event.AjaxBehaviorEvent event){      
		
	    Map<String, Object> map = event.getComponent().getAttributes();         
	    valueTest = (ValueTest) map.get("value");
	   
	    if(valueTest == null){
	    	valueTest = null;
	    	
	    }else{	    	
	    	//fazendo o teste para saber se houve conversao - pegando o valor de "strValue".
	    	String message = "Converteu, veja o valor de strValue : " + valueTest.getStrValue();
	    	Log.setLogger(this.getClass(), message, Level.INFO);
	    }	    
	} 
		
	
	/*--------
	 * get/set
	 ---------*/
	public String getLimitCharNotBlankSpace() {		
		return RegexUtil.getLimitCharNotBlankSpace();
	}	
	public String getLimitChar() {		
		return RegexUtil.getLimitChar();
	}	
	
}
