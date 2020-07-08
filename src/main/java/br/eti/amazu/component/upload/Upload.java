package br.eti.amazu.component.upload;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import javax.swing.ImageIcon;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import br.eti.amazu.component.dialog.DialogBean;
import br.eti.amazu.component.dialog.DialogType;
import lombok.Getter;
import lombok.Setter;
 
@Getter
@Setter
public class Upload implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private  String writherPath;
	private  String targetFile;	
	private  Integer imageHeight;
	private  Integer imageWidth;	
	private  UploadedFile file;
	private  StreamedContent scImage;
	private  String actionComplete;
	private  String update;
	private Boolean multiplo;	
	private String help = "";	
	private byte[] imageBytes;
	List<String> msgs;
	UploadMode uploadMode;
	DialogBean dialogBean;
			
	/*  writherPath = "C:\meuServidor\...." - Um local qualquer no servidor.
	 * targetFile = "nomeDeArquivo.jpg" -  imposicao do nome do qrquivo.
	 * imageHeight = Altura da imagem em pixels (se o arquivo for uma imagem).
	 * imageWidth = Largura da imagem em pixels (se o arquivo for uma imagem).
	 * actionComplete = uma acao a ser realizada apos o upload.
	 * updateActionComplete = qual componente sofrerah update apos o actionComplete. */
	public Upload(
		DialogBean dialogBean,  // .O dialogBean jah vem inicializado
		UploadMode uploadMode, //..	O modo do upoad: IMAGE_DATABASE ou FILE_SERVER			
		String writherPath,  // ...	"C:\meuServidor\...." - Um local qualquer no servidor			
		String targetFile, // .....	"nomeDeArquivo.jpg" -  imposicao do nome do arquivo.
		Integer imageHeight, //....	Altura da imagem em pixels (se o arquivo for uma imagem).
		Integer imageWidth, //.....	Largura da imagem em pixels (se o arquivo for uma imagem).
		String actionComplete, //..	uma acao a ser realizada apos o upload.
		String update, //..........	qual componente sofrerah update apos o actionComplete. 
		Boolean multiplo //.......	se serah escolhido mais de um arquivo para upload.
		){
	
		this.init(dialogBean, uploadMode, writherPath, targetFile, imageHeight, imageWidth, 
			actionComplete, update, multiplo);		
	}
	
	
	public Upload(DialogBean dialogBean, UploadMode uploadMode, 
			String whriterPath, String actionComplete, Boolean multiplo){	
	
		this.init(dialogBean, uploadMode, whriterPath, null, null, null, 
			actionComplete, null, multiplo);   
	}
	
	void init(DialogBean dialogBean,  UploadMode uploadMode, String writherPath, 
			String targetFile, Integer imageHeight, Integer imageWidth, String actionComplete,	
			String update, Boolean multiplo){	
		
		this.reset();
		
		//inicializando os atributos
		msgs = new ArrayList<String>();			
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
		this.actionComplete = actionComplete;
		this.update = update;		
		PrimeFaces.current().ajax().update("formUpload");
		PrimeFaces.current().executeScript("PF('dlgUpload').show()");
	}
	
	public void upload(FileUploadEvent event){
		
		try {
						
			file = event.getFile();	        			
			if(multiplo == false){
				if(targetFile != null){
			       if(!file.getFileName().equals(targetFile)){		   
			    	   
			    	   dialogBean.addMessage(this.getMessage("MAR001", 
			    			   new String[]{file.getFileName(),targetFile}), DialogType.ERROR);
			    	   
			    	   reset();
			    	   return;	    			
			       } 
				}			
	        
				if(file.getContentType().contains("image")){
					
					BufferedImage image = ImageIO.read(file.getInputStream());
					if(imageHeight != null && image.getHeight() > imageHeight ){
						
						msgs.add(this.getMessage("MAR014",
								new String[]{file.getFileName(),String.valueOf(imageHeight)}));    			
					}			
					if(imageWidth != null && image.getWidth() > imageWidth ){
						
						msgs.add(this.getMessage("MAR015", 
								new String[]{file.getFileName(),String.valueOf(imageWidth)}));    			
					}
				}
											
				if(!msgs.isEmpty()){				
					
					dialogBean.addMessage(this.getMessage("MAR012",
							new String[]{file.getFileName()}), DialogType.ERROR, msgs);
					
					reset();
					
				}else{
					if(uploadMode.equals(UploadMode.FILE_SERVER)){						 
						if(writherPath == null){
							dialogBean.addMessage(this.getMessage("MAR016"), DialogType.ERROR);
				    		reset();
				    		return;	
						}		
						
						executeUpload(event);	
						
						dialogBean.addActionMessage(
								this.getMessage("MAR010", new String[]{file.getFileName()}), 
								actionComplete,
								update
						);	
						
						PrimeFaces pf = PrimeFaces.current(); 	  
						pf.ajax().update("dialog");
						pf.ajax().update("gridLista");						
						pf.executeScript("PF('dlgUpload').hide()");
						pf.ajax().update("formUpload");
						reset();
						 
					}else{												
						InputStream is = event.getFile().getInputStream();
						
						//por garantia, e para padronizar, mantem a largura da foto em 85px;
						imageBytes = this.getBytes(this.getThumbnail(IOUtils.toByteArray(is), 85d));
						
						putScImage(event); //transforma o arquivo em uma imagem p/ o componente PrimeFaces
					}
				}
					
			}else{
				executeUpload(event);
			}

		} catch (Exception e) {
				
			dialogBean.addMessage(this.getMessage("MAR011", new String[]{file.getFileName()}), 
					DialogType.ERROR);
			
			reset();
		}		 
	}
		
	void executeUpload(FileUploadEvent event) throws Exception{			
		try {
	   copyFile(event.getFile().getFileName(), event.getFile().getInputStream(), writherPath);
	           
	    } catch (IOException e) {
	       dialogBean.addMessage(this.getMessage("MAR011", new String[]{file.getFileName()}), 
									DialogType.ERROR);
	       reset();
	    }
	}
			
	public void copyFile(String fileName, InputStream in, String destination) {   
		
		try {                
			
	       OutputStream out = new FileOutputStream(new File(destination + "/" + fileName));          
	       int read = 0;
	       byte[] bytes = new byte[1024];          
	       while ((read = in.read(bytes)) != -1) {
	          out.write(bytes, 0, read);
	       }                  
	       in.close();
	       out.flush();
	       out.close();          
	       Logger logger = Logger.getLogger(this.getClass());
	       logger.setLevel(Level.INFO);
	       logger.info(this.getMessage("MAR010", new String[]{fileName}));
	     		             
		} catch (IOException e) {
	       dialogBean.addMessage(this.getMessage("MAR011", 
	       new String[]{file.getFileName()}), DialogType.ERROR);
	       reset();
	    }
	}	
		
	void putScImage(FileUploadEvent event) throws Exception {
		file = event.getFile();
		scImage = DefaultStreamedContent.builder().contentType(file.getContentType()).name(file.getFileName())
				.stream(() -> {
					try {
						return file.getInputStream();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				}).build();
	}
		
	public void reset(){		
		msgs = new ArrayList<String>();			
		imageWidth = null;
		imageHeight = null;
		targetFile = null;
		writherPath = null;
		actionComplete = null;
		imageBytes = null;
		update = null;
		file = null;
		scImage = null;	
		help = "";		
		System.gc();		
	}
		
		
	/* MENSAGENS SEM PARAMETROS
	 * Utilizado intensivamente no processo de internacionalizacao. 
	 * Obtem o valor da mensagem do arquivo properties, passando a chave como parametro.	
	 ---------------------------------------------------------------------------------*/
	protected String getMessage(String key){
		
		ResourceBundle rs = ResourceBundle.getBundle("messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());	
		
		if(rs.containsKey(key)) return rs.getString(key);
		return key + ": invalid key";
	}
		
		
	/* MENSAGENS COM PARAMETROS
	 * Utilizado intensivamente no processo de internacionalizacao. Obtem o valor da mensagem
	 * do arquivo properties, passando a chave e uma lista de parametros.	
	 --------------------------------------------------------------------------------------*/
	protected String getMessage(String key,String[] params){	
		
		ResourceBundle rs = ResourceBundle.getBundle("messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());		
		
		String msg;		
		if(rs.containsKey(key)){
			msg = rs.getString(key);
			
		}else{
			msg = key + ": invalid key";
		}
			
		for (int i = 0; i < params.length; i++) {
			String param = params[i];
			String regex = "{" + i + "}";
			msg = msg.replace(regex, param);
		}		
		
		return msg;
		
	}	
		
	/* Recebe uma array de bytes e a largura de uma imagem. Devolve um 
	 * inputStream de uma imagem com a largura dela
	 * jah previamente definida no parametro (transformacao em escala).*/
	 InputStream getThumbnail(byte[] bytes, Double width){
		
		ImageIcon imageIcon = new ImageIcon(bytes);		
	    Image inImg = imageIcon.getImage();
	    
	    if(width == null) width = (double) inImg.getWidth(null);
	    double scale = width / inImg.getWidth(null);
	      
	    int scaledW = (int) (scale * inImg.getWidth(null));
	    int scaledH = (int) (scale * inImg.getHeight(null));
	    
	    if(scaledW <=0 )scaledW = 1;
	    if(scaledH <=0 )scaledH = 1;
	    
	    BufferedImage outImg = new BufferedImage(scaledW, 
								scaledH, BufferedImage.TYPE_INT_RGB);
	      
	    AffineTransform affineTransform = new AffineTransform();  
	    affineTransform.scale(scale, scale);
	      
	    Graphics2D g2D = outImg.createGraphics();  
	    g2D.drawImage(inImg, affineTransform, null); 
	    g2D.dispose();
	    
	    ByteArrayOutputStream b = new ByteArrayOutputStream(); 
	    
	    try { 
	        ImageIO.write(outImg, "jpg", b);	       
	        return new ByteArrayInputStream(b.toByteArray());
	        
	    } catch (IOException e) {  
	        e.printStackTrace(); 
	        
	    }finally{  
            try {  
                b.close();  
                
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
	    }   
	    
	    return null;  
	}
	 
	//Recebe um inputStream e devolve uma array de bytes.
	byte[] getBytes(InputStream is) throws IOException {

	    int len;
	    int size = 1024;
	    byte[] buf;

	    if (is instanceof ByteArrayInputStream) {
	      size = is.available();
	      buf = new byte[size];
	      len = is.read(buf, 0, size);	      
	      
	    } else {
	      ByteArrayOutputStream bos = new ByteArrayOutputStream();
	      buf = new byte[size];
	      
	      while ((len = is.read(buf, 0, size)) != -1)
	        bos.write(buf, 0, len);
	      buf = bos.toByteArray();
	    }	    
	    return buf;
	    
	}		

}

