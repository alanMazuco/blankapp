/**********************
 * Funcoes Principais
 ********************/
function mascara(o,f){
	v_obj=o;
    v_fun=f;
    setTimeout("execmascara()",1);
}
        
/*Executa os objetos*/
function execmascara(){
	v_obj.value=v_fun(v_obj.value);
}
/***************************
 * Fim de Funcoes Principais
 **************************/
        
/*Determina as expressoes regulares dos objetos*/
function leech(v){
	v=v.replace(/o/gi,"0");
    v=v.replace(/i/gi,"1");
    v=v.replace(/z/gi,"2");
    v=v.replace(/e/gi,"3");
    v=v.replace(/a/gi,"4");
    v=v.replace(/s/gi,"5");
    v=v.replace(/t/gi,"7");
    return v;
}
        
/*Apenas numeros 0123456789 */
function numero(v){
	return v.replace(/\D/g,"");
}

/*Numeros inteiros ou fracionarios 1.234512*/
function double(v){	
	var b = v.substring(v.length-1,v.length);
	v=v.replace(/[^\d\.]/g, "");
	var e = v.substring(0,v.length-1);	
	if(e.indexOf(".")>-1) {
		if(b.indexOf(".")>-1) v=e;		
	}
	return v;
}
        
/*Telefone (99) 99999-9999*/
function tel(v){
	v=v.replace(/\D/g,"");                          
    v=v.replace(/^(\d\d)(\d)/g,"($1)$2");    
    if(v.length >= 13) {
    	v=v.replace(/(\d{5})(\d)/,"$1-$2");
    	
    }else{
    	v=v.replace(/(\d{4})(\d)/,"$1-$2");
    }
    return v;
}
           
/*CPF  999.999.999-99*/
function cpf(v){
	v=v.replace(/\D/g,"");                                   
	v=v.replace(/(\d{3})(\d)/,"$1.$2");         
	v=v.replace(/(\d{3})(\d)/,"$1.$2");                                                                                                 
	v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2"); 
	return v;
}
        
/*CEP 99999-999*/
function cep(v){
	v=v.replace(/D/g,"");                            
    v=v.replace(/^(\d{5})(\d)/,"$1-$2"); 
	return v;
}
        
/*CNPJ 99.999.999/0001-00*/
function cnpj(v){
	v=v.replace(/\D/g,"");                              
	v=v.replace(/^(\d{2})(\d)/,"$1.$2");      
	v=v.replace(/^(\d{2})\.(\d{3})(\d)/,"$1.$2.$3"); 
	v=v.replace(/\.(\d{3})(\d)/,".$1/$2");              
	v=v.replace(/(\d{4})(\d)/,"$1-$2");                        
	return v;
}
         
/*Site http://www.site.com.br*/
function site(v){
	v=v.replace(/^http:\/\/?/,"");
	dominio=v;
	caminho="";
	
	if(v.indexOf("/")>-1)
		dominio=v.split("/")[0];
		caminho=v.replace(/[^\/]*/,"");
		dominio=dominio.replace(/[^\w\.\+-:@]/g,"");
		caminho=caminho.replace(/[^\w\d\+-@:\?&=%\(\)\.]/g,"");
		caminho=caminho.replace(/([\?&])=/,"$1");		
	if(caminho!="") dominio=dominio.replace(/\.+$/,"");	
	v="http://"+dominio+caminho;
	return v;
}

/*DATA 11/11/1111*/ 
function data(v){
    v=v.replace(/\D/g,""); 
    v=v.replace(/(\d{2})(\d)/,"$1/$2"); 
    v=v.replace(/(\d{2})(\d)/,"$1/$2"); 
    return v;
}

/*HORA 08:08*/
function hora(v){
	v=v.replace(/\D/g,""); 
	v=v.replace(/(\d{2})(\d)/,"$1:$2");  
	return v;
}
        
/*Moeda 5.33*/
function moeda(v){	
	v=v.replace(/\D/g,"");	
	v=v.replace(/^([0-9]{3}\.?){3}-[0-9]{2}$/,"$1.$2");
	v=v.replace(/(\d)(\d{2})$/,"$1.$2");	
	return v;
} 

/*Somente letras  abcd...*/
function letras(v){
	return v.replace(/\d/g,"");
}

/*Converte em maiusculas - permite numeros*/
function letrasMa(v){
	return v.toUpperCase();
}

/*Converte em minusculas - permite numeros*/
function letrasMi(v){
	return v.toLowerCase();
}

/*Converte em maiuscula o 1. caracter de nomes proprios*/
function firstName(v) {
    return v.toLowerCase().replace(/\b\w+\b/g, cnvrt);    
    function cnvrt() {
        if (arguments[arguments.length -2] == 0)
            return arguments[0].replace(/^[a-z]/, cnvrt2);
        else if (/^(do|dos|da|das|de)$/.test(arguments[0]) )
            return arguments[0];
        else
            return arguments[0].replace(/^[a-z]/, cnvrt2);
    }    
    function cnvrt2() {
        return arguments[0].toUpperCase();
    }
}