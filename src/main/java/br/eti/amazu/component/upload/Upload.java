package br.eti.amazu.component.upload;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import br.eti.amazu.component.dialog.DialogBean;
import br.eti.amazu.component.dialog.DialogType;
import br.eti.amazu.infra.util.FacesUtil;
import br.eti.amazu.infra.util.FileUtil;
import lombok.Getter;
import lombok.Setter;
 
@Getter
@Setter
public class Upload implements Serializable{
				
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_MAR011 = "MAR011";
	
	private  String writherPath;
	private  String targetFile;	
	private  Integer imageHeight;
	private  Integer imageWidth;	
	private Boolean multiplo;	
	private String help = "";	
	private byte[] imageBytes;
	private List<String> msgs;

	private transient StreamedContent scImage;
	transient FacesUtil facesUtil = new FacesUtil();
	
	UploadMode uploadMode;
	DialogBean dialogBean;
					
	/* writherPath = "C:\meuServidor\...." - Um local qualquer no servidor.
	 * targetFile = "nomeDeArquivo.jpg" -  imposicao do nome do qrquivo.
	 * imageHeight = Altura da imagem em pixels (se o arquivo for uma imagem).
	 * imageWidth = Largura da imagem em pixels (se o arquivo for uma imagem).*/
	public Upload(
		DialogBean dialogBean,  // .O dialogBean jah vem inicializado
		UploadMode uploadMode, //..	O modo do upoad: IMAGE_DATABASE ou FILE_SERVER			
		String writherPath,  // ...	"C:\meuServidor\...." - Um local qualquer no servidor			
		String targetFile, // .....	"nomeDeArquivo.jpg" -  imposicao do nome do arquivo.
		Integer imageHeight, //....	Altura da imagem em pixels (se o arquivo for uma imagem).
		Integer imageWidth, //.....	Largura da imagem em pixels (se o arquivo for uma imagem).
		Boolean multiplo //.......	se serah escolhido mais de um arquivo para upload.
		){
	
		this.init(dialogBean, uploadMode, writherPath, targetFile, imageHeight, imageWidth, 
			 multiplo);	
	}
	
	
	public Upload(DialogBean dialogBean, UploadMode uploadMode, 
			String whriterPath, Boolean multiplo){	
	
		this.init(dialogBean, uploadMode, whriterPath, null, null, null, 
			multiplo);   
	}	
	
	public void upload(FileUploadEvent event){
		
		try {		
				
			if(!multiplo.booleanValue()){
				proccessMultipleFiles(event);
					
			}else{
				executeUpload(event);
			}

		} catch (Exception e) {
				
			dialogBean.addMessage(facesUtil.getMessage(MESSAGE_MAR011, new String[]{event.getFile().getFileName()}), 
					DialogType.ERROR);
			
			reset();
		}		 
	}
	
	void init(DialogBean dialogBean,  UploadMode uploadMode, String writherPath, 
			String targetFile, Integer imageHeight, Integer imageWidth, 	
			Boolean multiplo){	
		
		this.reset();
		
		//inicializando os atributos
		msgs = new ArrayList<>();			
		this.uploadMode = uploadMode;
		
		this.multiplo = multiplo;
		if(uploadMode.equals(UploadMode.IMAGE_DATABASE)){
			this.multiplo = false;
			
		}else{
			if(this.multiplo==null) this.multiplo = false;
		}
		
		this.dialogBean = dialogBean;
		this.writherPath = writherPath;	
		this.targetFile = targetFile;
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
				
		PrimeFaces.current().ajax().update("formUpload");		
		PrimeFaces.current().executeScript("PF('dlgUpload').show()");		
	}
	
	void proccessMultipleFiles(FileUploadEvent event) {
		
		try {
		
			if(targetFile != null && !event.getFile().getFileName().equals(targetFile)){		   
	    	   
				dialogBean.addMessage(facesUtil.getMessage("MAR001", 
	    			   new String[]{event.getFile().getFileName(),targetFile}), DialogType.ERROR);
	    	   
				reset();
				return;	    			
			} 							
     
			if(event.getFile().getContentType().contains("image")){
				
				
				BufferedImage image = ImageIO.read(event.getFile().getInputStream());
					
				if(imageHeight != null && image.getHeight() > imageHeight ){					
					msgs.add(facesUtil.getMessage("MAR014",
						new String[]{event.getFile().getFileName(),String.valueOf(imageHeight)}));    			
				}
					
				if(imageWidth != null && image.getWidth() > imageWidth ){					
					msgs.add(facesUtil.getMessage("MAR015", 
						new String[]{event.getFile().getFileName(),String.valueOf(imageWidth)}));    			
				}
			}		
				
			this.validateComplete(event);					
					
		} catch (Exception e) {
			
			dialogBean.addMessage(facesUtil.getMessage(MESSAGE_MAR011, new String[]{event.getFile().getFileName()}), 
					DialogType.ERROR);
			
			reset();
		}
	}
	
	void validateComplete(FileUploadEvent event) throws IOException {
		
		PrimeFaces pf = PrimeFaces.current(); 
		
		if(!msgs.isEmpty()){				
			
			dialogBean.addMessage(facesUtil.getMessage("MAR012",
					new String[]{event.getFile().getFileName()}), DialogType.ERROR, msgs);
			
			reset();
			
		}else{
			if(uploadMode.equals(UploadMode.FILE_SERVER)){						 
				if(writherPath == null){
					dialogBean.addMessage(this.getMessage("MAR016"), DialogType.ERROR);
		    		reset();
		    		return;	
				}
				executeUpload(event);				
				dialogBean.addActionMessage(facesUtil.getMessage("MAR010", new String[]{event.getFile().getFileName()}), null, null);	
				
				pf.ajax().update("dialog");
				pf.ajax().update("gridLista");
				pf.ajax().update("formUpload");							
				pf.executeScript("PF('dlgUpload').hide()");		
														
				reset();
				
			}else{	
				 
				InputStream is = event.getFile().getInputStream();
				
				//por garantia, e para padronizar, mantem a largura da foto em 85px
				imageBytes = this.getBytes(new FileUtil().getThumbnail(IOUtils.toByteArray(is), 85d));
								
				putScImage(event); //transforma o arquivo em uma imagem p/ o componente PrimeFaces
				reset();
			}
		}
	}
		
	void executeUpload(FileUploadEvent event) throws IOException {		
		copyFile(event.getFile().getFileName(), event.getFile(), writherPath );
	}
			
	public void copyFile(String fileName, UploadedFile file, String destination ) throws IOException {  
			
		InputStream in = file.getInputStream();
		OutputStream out = new FileOutputStream(new File(destination.concat("/").concat(fileName)));
		IOUtils.copy(in,out);
		in.close();
		out.close();
	}	
	
	public static void close(Closeable c) throws IOException{
		if (c == null) return; 
		c.close();
	}
		
	public void putScImage(FileUploadEvent event) throws IOException {
		
		byte[] contents = IOUtils.toByteArray(event.getFile().getInputStream());
		scImage = 
				DefaultStreamedContent.builder()
                .name(event.getFile().getFileName())
                .contentType("application/octet-stream")
                .stream(() -> new ByteArrayInputStream(contents)).build();
	}
		
	public void reset(){		
		msgs = new ArrayList<>();			
		imageWidth = null;
		imageHeight = null;
		targetFile = null;
		writherPath = null;		
		imageBytes = null;
		scImage = null;	
		help = "";
	}
		
		
	/* MENSAGENS SEM PARAMETROS
	 * Utilizado intensivamente no processo de internacionalizacao. 
	 * Obtem o valor da mensagem do arquivo properties, passando a chave como parametro.	
	 ---------------------------------------------------------------------------------*/
	public String getMessage(String key){
		
		ResourceBundle rs = ResourceBundle.getBundle("messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());	
		
		if(rs.containsKey(key)) return rs.getString(key);
		return key + ": invalid key";
	}				
			 
	//Recebe um inputStream e devolve uma array de bytes.
	public byte[] getBytes(InputStream is) throws IOException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    int nRead;
	    byte[] data = new byte[1024];
	    while ((nRead = is.read(data, 0, data.length)) != -1) {
	        buffer.write(data, 0, nRead);
	    }
	 
	    buffer.flush();
	    return buffer.toByteArray();	    
	}		

}

