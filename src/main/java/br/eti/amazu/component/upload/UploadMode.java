package br.eti.amazu.component.upload;

public enum UploadMode implements ModeInterface{	
	FILE_SERVER ("FILE_SERVER"),         //Transferir arquivos para o servidor.
	IMAGE_DATABASE ("IMAGE_DATABASE");   //Transferir imagem para um campo binário do BD.
			
	private String upMode;	
	
	UploadMode(String upMode) {
		this.upMode = upMode;		
	}

	/*--------
	 * get/set
	 ---------*/
	@Override
	public String getUpMode() {
		return upMode;
	}
		
}