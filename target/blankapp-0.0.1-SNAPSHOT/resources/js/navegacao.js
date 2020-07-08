/*Esta funcao permite bloquear o botao 'return' dos browsers. Eh necessario
 * insercao do dlgButtonBack.xhtml (layout). Adaptado de Stackoverflow.com*/
(function(window) { 
	'use strict'; 
 	var noback = { 	 
		version: '0.0.1', 
		history_api : typeof history.pushState !== 'undefined', 		 
		init:function(){ 
			window.location.hash = '#no-back'; 
			noback.configure(); 
		}, 		 
		hasChanged:function(){ 
			if (window.location.hash == '#no-back' ){ 
				window.location.hash = '#app';
				PF('dlgButtonBack').show();
			} 
		}, 		 
		checkCompat: function(){ 
			if(window.addEventListener) { 
				window.addEventListener("hashchange", 
						noback.hasChanged, false); 
			}else if (window.attachEvent) { 
				window.attachEvent("onhashchange", 
						noback.hasChanged); 
			}else{ 
				window.onhashchange = 
						noback.hasChanged; 
			} 
		}, 		 
		configure: function(){ 
			if ( window.location.hash == '#no-back' ) { 
				if ( this.history_api ){ 
					history.pushState(null, '', '#app'); 
				}else{  
					window.location.hash = '#app';
					PF('dlgButtonBack').show();
				} 
			} 
			noback.checkCompat(); 
			noback.hasChanged(); 
		}
	}; 
	 
	if (typeof define === 'function' && define.amd) { 
		define( function() { return noback; } ); 
	}else if (typeof module === 'object' && module.exports) { 
		module.exports = noback; 
	}else { window.noback = noback; } 
	noback.init();
}(window)); 