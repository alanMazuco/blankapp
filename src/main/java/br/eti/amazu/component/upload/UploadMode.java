package br.eti.amazu.component.upload;

public enum UploadMode {	
	FILE_SERVER ("FILE_SERVER"),         //Transferir arquivos para o servidor.
	IMAGE_DATABASE ("IMAGE_DATABASE");   //Transferir imagem para um campo binário do BD.
			
	private String uploadMode;	
	
	UploadMode(String uploadMode) {
		this.uploadMode = uploadMode;		
	}

	/*--------
	 * get/set
	 ---------*/
	public String getUploadMode() {
		return uploadMode;
	}
	public void setUploadMode(String uploadMode) {
		this.uploadMode = uploadMode;
	}
	
}