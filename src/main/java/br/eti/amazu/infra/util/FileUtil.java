package br.eti.amazu.infra.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Level;
import br.eti.amazu.infra.util.log.Log;

public class FileUtil {

	/* Recebe uma array de bytes e a largura de uma imagem. Devolve um 
	 * inputStream de uma imagem com a largura dela
	 * jah previamente definida no parametro (transformacao em escala).*/
	public static InputStream getThumbnail(byte[] bytes, Double width){
		
		ImageIcon imageIcon = new ImageIcon(bytes);		
	    Image inImg = imageIcon.getImage();
	    
	    if(width == null) width = (double) inImg.getWidth(null);
	    double scale = width / inImg.getWidth(null);
	      
	    int scaledW = (int) (scale * inImg.getWidth(null));
	    int scaledH = (int) (scale * inImg.getHeight(null));
	    
	    if(scaledW <=0 )scaledW = 1;
	    if(scaledH <=0 )scaledH = 1;
	    
	    
	    BufferedImage outImg = new BufferedImage(
				scaledW, scaledH, BufferedImage.TYPE_INT_RGB);
  
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
	public static byte[] getBytes(InputStream is) throws IOException {
	
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
		
	//Converte uma matriz de byte em string.
	public String encode(byte[] byteArray) {
		return Base64.encodeBase64String(byteArray);
	}
		
	//Converte uma string em uma matriz de byte.
	public InputStream decode(String str) {	
		byte[] byteArray = Base64.decodeBase64(str);
		return new ByteArrayInputStream(byteArray);
	}
		
	/*
	 * Recebe uma string contendo o caminho completo do arquivo e devolve o
	 * mesmo em matriz de bytes.*/
	public static byte[] getBytesFromFile(String path) {
		try {
			File file = new File(path);
			InputStream is = new FileInputStream(file);
			long length = file.length();

			if (length > Integer.MAX_VALUE) {				
				is.close();
				throw new IOException("File is too large!");
			}
			
			byte[] bytes = new byte[(int) length];			
			int offset = 0;
			int numRead = 0;			
			while (offset < bytes.length && (numRead = is.read(bytes, offset, 
					bytes.length - offset)) >= 0) {
				offset += numRead;
			}			
			
			if (offset < bytes.length) {
				is.close();
				throw new IOException("Could not completely read file " + file.getName());
			}
			
			is.close();
			return bytes;

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}		
		return null;
	}

	// Redimensiona o tamanho de uma imagem para exibicao.
	public static ImageIcon applyScale(ImageIcon imageIcon, int width,	int height) {		
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		
		image.getGraphics().drawImage(imageIcon.getImage(), 0, 0, width,
				height, 0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight(), null);	
		
		return new ImageIcon(image);
	}
		
	// Verifica se existe um determinado diretorio.
	public static boolean isExisteFileOrDir(String fullPath) {
		return new File(fullPath).exists();
	}

	// Cria um diretorio em um determinado local (fullPath).
	public static boolean criarDiretorio(String fullPath) {
		File dir = new File(fullPath);		
		if (dir.mkdir()) {
			return true;
			
		} else {
			return false;
		}
	}

	// Renomeia arquivos ou diretorios.
	public static void renomearFileOrDir(String oldFullDir,String newFullDir) 
			throws FileNotFoundException, IOException {
		
		File file = new File(oldFullDir);
		file.renameTo(new File(newFullDir));
	}

	// Exclui um arquivo.
	public static void excluirArquivo(String fullFileName) throws FileNotFoundException, IOException {
		File file = new File(fullFileName);
		file.delete();
	}

	// Exclui um diretorio.
	public static boolean excluirDiretorio(String fullPath) {
		File dir = new File(fullPath);		
		if (dir.isDirectory()) {
			String[] children = dir.list();			
			for (int i = 0; i < children.length; i++) {
				boolean success = excluirDiretorio(fullPath + "/" + children[i]);
				if (!success)  return false;
			}			
		}		
		return dir.delete();
	}

	/* Exclui diretorios vazios em um determinado path. Exemplo: se nFiles=0,
	 * exclui apenas os diretorios que nao possuem arquivos. se nFiles=3, exclui
	 * os diretorios vazios e aqueles que possuem menos de 3 arquivos.*/
	public static void excluirDiretoriosVazios(String path, int nFiles) {
		File fList[] = new File(path).listFiles();		
		for (File f : fList) {
			if (f.isDirectory()) {
				if (f.listFiles().length < nFiles) {
					
					for (File boo : f.listFiles()) {
						boo.delete();
					}
					
					f.delete();
				}
			}
		}
	}

	// Obtem o mimetype do arquivo.
	public static String getMimeType(String fullFile) throws java.io.IOException {		
		FileNameMap fileNameMap = URLConnection.getFileNameMap();	
		
		if(fileNameMap.getContentTypeFor(fullFile) == null && fullFile.contains(".mp3")){
			return "audio/mp3";
		}
		
		return fileNameMap.getContentTypeFor(fullFile);
	}


	// Faz a leitura do arquivo em FileInputStream.
	public static FileInputStream getInputStream(String fullFile) throws java.io.IOException {
		return new FileInputStream(fullFile);
	}

	// Obtem o caminho do leitor de pdf instalado no sistema.
	public static String obtemPathReader(String visualizarEditarPdf) {			
		return visualizarEditarPdf.equals("visualizar")? 
				"cmd /c start acrord32 ":"cmd /c start acrobat ";
	}

	// Obtem o caminho do arquivo.
	public static String obtemFullPath(String relativePath) {
		return new File(relativePath).getAbsolutePath();
	}

	// Obtem uma lista de ditetorios em um determinado path.
	public static List<String> listarDiretorios(String relativePath) {
		List<String> lista = new ArrayList<String>();
		File fList[] = new File(relativePath).listFiles();
		
		if (fList != null) {
			for (int i = 0; i < fList.length; i++) {
				if (fList[i].isDirectory()) {
					lista.add(fList[i].getName());				
				}
			}
		}		
		return lista;
	}

	// Obtem uma lista de arquivos (String) dentro de um diretorio.
	public static List<String> listarArquivos(String relativePath) {
		List<String> lista = new ArrayList<String>();
		File fList[] = new File(relativePath).listFiles();
		
		if (fList != null) {
			for (int i = 0; i < fList.length; i++) {
				if (fList[i].isFile()) lista.add(fList[i].getName());				
			}
		}		
		return lista;
	}

	//Lista diretorios dentro de um arquivo jar.
	public static List<String> listarDiretoriosInJar(String fullPathJar, String localPath) {		
		try {
			ZipFile zipFile = new ZipFile(fullPathJar);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			List<String> lista = new ArrayList<String>();
			String diretorio;
			
			while (entries.hasMoreElements()) {
				ZipEntry zipEntry = entries.nextElement();
				if (zipEntry.isDirectory() && zipEntry.getName().contains(localPath + "/")) {	
					
					diretorio = zipEntry.getName().substring(zipEntry.getName().indexOf(localPath) + 
							localPath.length() + 1);
					
					if (diretorio != null && diretorio.length() != 0) {
						diretorio = diretorio.substring(0, diretorio.lastIndexOf("/"));
						if (!diretorio.contains("/")) lista.add(diretorio);
					}
				}
			}			
			zipFile.close();
			return lista;

		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		return null;
	}
		
	//Lista diretorios dentro de um arquivo jar.
	public static List<String> listarArquivosInJar(String fullPathJar, String localPath) {		
		try {
			ZipFile zipFile = new ZipFile(fullPathJar);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			List<String> lista = new ArrayList<String>();
			String arquivo;
			
			while (entries.hasMoreElements()) {
				ZipEntry zipEntry = entries.nextElement();
				if (zipEntry.getName().contains(localPath + "/")) {
					if (!zipEntry.isDirectory()) {
						arquivo = zipEntry.getName().substring(zipEntry.getName().lastIndexOf("/") + 1);
						if(!lista.contains(arquivo)) lista.add(arquivo);
					}
				}
			}			
			zipFile.close();
			return lista;

		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		return null;
	}
		
	//Converte um array de bytes em arquivo
	public static void byteToFle(byte[] bFile, String dir){
	    try {
	 
		    //convert array of bytes into file
		    FileOutputStream fileOuputStream = new FileOutputStream(dir + "/testing.jpg"); 
		    fileOuputStream.write(bFile);
		    fileOuputStream.close();		    
		    Log.setLogger(FileUtil.class, "Done!", Level.INFO);
			    
	     }catch(Exception e){
	       e.printStackTrace();
	    }
	}
	
}
