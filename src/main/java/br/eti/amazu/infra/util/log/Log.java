package br.eti.amazu.infra.util.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log {
	
	private Log() {
		//do not compliance
	}

	public static final void setLogger(String str, String message, Level level) {
		log(null,str,message,level,null);
	}
	
	public static final void setLogger(Class<?> t, String message, Level level) {
		log(t,null,message,level,null);
	}
	
	public static final void setLogger(String str, String message, Level level, Ansi ansi) {
		log(null,str,message,level,ansi);
	}
	
	public static final void setLogger(Class<?> t, String message, Level level, Ansi ansi) {
		log(t,null,message,level,ansi);
	}
	
	static void log(Class<?> t, String str, String message, Level level, Ansi ansi) {		
		
		Logger logger = Logger.getLogger("BlankApp");		
		
		if(t != null) logger = Logger.getLogger(t);
		
		if(str != null) logger = Logger.getLogger(str);	
		
		logger.setLevel(level);
		
		/*
		 * Standard Level	intLevel
			OFF		0
			FATAL	50000
			ERROR	40000
			WARN	30000
			INFO	20000
			DEBUG	10000 (WildFly desab)
			TRACE	5000 (WildFly desab)
			ALL	Integer.MAX_VALUE
		 */
		
		switch(level.toInt()){		
			case 5000: logger.trace(message);
				return;
				
			case 10000: logger.debug(message);
				return;
				
			case 20000:
				if(ansi!=null){
					logger.info(ansi.getAnsiStr() + message + "\u001B[0m");
					
				}else{
					logger.info(message);
				}
				return;
				
			case 30000: logger.warn(message);
				return;
				
			case 40000:
				if(message.startsWith("PW")){
					logger.error(ansi.getAnsiStr() + message + "\u001B[0m");					
				}else{
					logger.error(message);
				}
				return;
				
			case 50000: logger.fatal(message);
				return;
				
			default: //not compliance				
		}
	}	
}
