package br.eti.amazu.infra.util.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log {

	public static final void setLogger(String str, String message, Level Level) {
		log(null,str,message,Level,null);
	}
	
	public static final void setLogger(Class<?> t, String message, Level Level) {
		log(t,null,message,Level,null);
	}
	
	public static final void setLogger(String str, String message, Level Level, Ansi ansi) {
		log(null,str,message,Level,ansi);
	}
	
	public static final void setLogger(Class<?> t, String message, Level Level, Ansi ansi) {
		log(t,null,message,Level,ansi);
	}
	
	static void log(Class<?> t, String str, String message, Level level, Ansi ansi) {		
		Logger logger = null;		
		if(t != null) logger = Logger.getLogger(t);
		if(str != null) logger = Logger.getLogger(str);		
		logger.setLevel(level);

		switch(level.toInt()){		
			case Level.TRACE_INT: 	logger.trace(message);
				return;
				
			case Level.DEBUG_INT: logger.debug(message);
				return;
				
			case Level.INFO_INT:
				if(ansi!=null){
					logger.info(ansi.getAnsi() + message + "\u001B[0m");
					
				}else{
					logger.info(message);
				}
				return;
				
			case Level.WARN_INT: logger.warn(message);
				return;
				
			case Level.ERROR_INT:
				if(message.startsWith("PW")){
					logger.error(ansi.getAnsi() + message + "\u001B[0m");					
				}else{
					logger.error(message);
				}
				return;
				
			case Level.FATAL_INT: logger.fatal(message);
		}		
	}
}
