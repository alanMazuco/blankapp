package br.eti.amazu.infra.util.log;

public enum Ansi {

	/* 0 - Reset / Normal
	 *	1 - Bold: treated as intensity under Windows console, user option in this plugin)
	 *	2 - Intensity faint: kind of supported :-) It resets the intensity to normal.
	 *	3 - Italic: on (treated as inverse under Windows console, user option in this plugin)
	 *	4 - Underline
	 *	7 - Negative
	 *	8 - Conceal
	 *	9 - Crossed-out
	 *	21 - Double underline
	 *	22 - Bold off (normal intensity)
	 *	23 - Italic off
	 *	24 - Underline off
	 *	27 - Negative off
	 *	28 - Conceal off
	 *	29 - Crossed-out off
	 *	30-37 - Set text color
	 *	38 - Set xterm-256 text color
	 *	39 - Default text color
	 *	40-47 - Set background color
	 *	48 - Set xterm-256 background color
	 *	49 - Default background color
	 *	51 - Framed
	 *	54 - Framed off
	 *	90-97 - Set foreground color, high intensity
	 *	100-107 - Set background color, high intensity*/			
	RED  ("\u001B[91m"), //.........................vermelho
	GREEN  ("\u001B[32m"), //.......................verde
	YELLOW ("\u001B[33m"), //.......................amarelo	
	BLUE ( "\u001B[94m"), //........................azul
	PURPLE ("\u001B[35m"), //.......................purpura
	CYAN  ("\u001B[96m"), //........................ciano
	RED_BOLD_YELLOWBG ("\u001B[1;3;4;103;91m"), //..vermelho negrito sublinhado fundo amarelo
	BLACK_ITALIC ("\u001B[3;30m"), //...............preto italico
	BLACK_BOLD ("\u001B[1;30m"), //.................preto negrito
	BLACK_YELLOWBG  ("\u001B[1;2;103;90m"); //......preto fundo amarelo

	private String ansi;	
	
	Ansi(String ansi) {
		this.ansi = ansi;		
	}

	/*--------
	 * get/set
	 ---------*/
	public String getAnsi() {
		return ansi;
	}
	public void setAnsi(String ansi) {
		this.ansi = ansi;
	}
	
}
