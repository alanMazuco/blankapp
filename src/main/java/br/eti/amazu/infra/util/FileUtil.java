package br.eti.amazu.infra.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Level;

import br.eti.amazu.infra.util.log.Log;

public class FileUtil {
	
	private static final String MSG_FALHA = "Falha na operação!";

	/* Recebe uma array de bytes e a largura de uma imagem. Devolve um 
	 * inputStream de uma imagem com a largura dela
	 * jah previamente definida no parametro (transformacao em escala).*/
	public InputStream getThumbnail(byte[] bytes, Double width) throws IOException {
		
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
			
		ImageIO.write(outImg, "jpg", b);	       
		return new ByteArrayInputStream(b.toByteArray());
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
	public byte[] getBytesFromFile(String path) throws IOException {	
		return Files.readAllBytes(Paths.get(path));
	}

	// Redimensiona o tamanho de uma imagem para exibicao.
	public ImageIcon applyScale(ImageIcon imageIcon, int width,	int height) {		
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		
		image.getGraphics().drawImage(imageIcon.getImage(), 0, 0, width,
				height, 0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight(), null);	
		
		return new ImageIcon(image);
	}
		
	// Verifica se existe um determinado diretorio.
	public boolean isExisteFileOrDir(String fullPath) {		
		return Files.exists(Paths.get(fullPath));
	}

	// Cria um diretorio em um determinado local (fullPath).
	public boolean criarDiretorio(String fullPath) {
				
		try {
			Files.createDirectory(Paths.get(fullPath));
			return true;
			
		} catch (IOException e) {
			Log.setLogger(FileUtil.class, MSG_FALHA, Level.ERROR);
		}
		return false;
	}

	// Renomeia arquivos ou diretorios.
	public void renomearFileOrDir(String oldFullDir,String newFullDir) {
		
		File file = new File(oldFullDir);
		if(!file.renameTo(new File(newFullDir))) {
			Log.setLogger(FileUtil.class, MSG_FALHA, Level.ERROR);
		}
	}

	// Exclui um arquivo.
	public void excluirArquivo(String fullFileName) {
		
		Path path = Paths.get(fullFileName);

		try {
		    Files.delete(path);
		    
		} catch (IOException e) {
			Log.setLogger(FileUtil.class, MSG_FALHA, Level.ERROR);
		}
	}

	// Exclui um diretorio recursivamente.
	public void excluirDiretorio(String fullPath) {
		
		Path directory = Paths.get(fullPath);
		try {
			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
				
				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
			
		} catch (IOException e) {
			Log.setLogger(FileUtil.class, MSG_FALHA, Level.ERROR);
		}
	}
	
	// Obtem o mimetype do arquivo.
	public String getMimeType(String fullFile) {		
		FileNameMap fileNameMap = URLConnection.getFileNameMap();	
		
		if(fullFile.contains(".mp3")){
			return "audio/mp3";
		}
		
		return fileNameMap.getContentTypeFor(fullFile);
	}

	// Faz a leitura do arquivo em FileInputStream.
	public FileInputStream getInputStream(String fullFile) throws java.io.IOException {
		return new FileInputStream(fullFile);
	}

	// Obtem o caminho do leitor de pdf instalado no sistema.
	public String obtemPathReader(String visualizarEditarPdf) {			
		return visualizarEditarPdf.equals("visualizar")? 
				"cmd /c start acrord32 ":"cmd /c start acrobat ";
	}

	// Obtem o caminho do arquivo.
	public String obtemFullPath(String relativePath) {
		return new File(relativePath).getAbsolutePath();
	}

	// Obtem uma lista de ditetorios em um determinado path.
	public List<String> listarDiretorios(String relativePath) {
		List<String> lista = new ArrayList<>();
		File[] fList = new File(relativePath).listFiles();
		
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
	public List<String> listarArquivos(String relativePath) {
		List<String> lista = new ArrayList<>();
		File[] fList = new File(relativePath).listFiles();
		
		if (fList!= null) {
			for (int i = 0; i < fList.length; i++) {
				if (fList[i].isFile()) lista.add(fList[i].getName());				
			}
		}		
		return lista;
	}

	//Lista diretorios dentro de um arquivo jar.
	public List<String> listarDiretoriosInJar(String fullPathJar, String localPath) {		
		
		List<String> lista = new ArrayList<>();
		
		try (
				ZipFile zipFile = new ZipFile(fullPathJar);
				)				
			{
			
			Collections.list(zipFile.entries()).stream().map(ZipEntry::getName).forEach(lista::add);
			
			List<String> result = new ArrayList<>();
			String diretorio;

			for(String str : lista) {
				if (str.contains(localPath + '/')) {	
					
					diretorio = str.substring(str.indexOf(localPath) + 
							localPath.length() + 1);
					
					if (!diretorio.isEmpty() && diretorio.lastIndexOf('/') > 0) {						
						result.add(diretorio.substring(0, diretorio.lastIndexOf('/')));
					}
				}
			}
			return result;
			

		} catch (Exception e) {
			Log.setLogger(FileUtil.class, e.getMessage(), Level.ERROR);			
		}		
		return new ArrayList<>();
	}
		
	//Lista arquivos dentro de um arquivo jar.
	public List<String> listarArquivosInJar(String fullPathJar, String localPath) {		
		
		List<String> lista = new ArrayList<>();
		
		try (
				ZipFile zipFile = new ZipFile(fullPathJar);
				)		
		    {
			
			Collections.list(zipFile.entries()).stream().map(ZipEntry::getName).forEach(lista::add);			
			List<String> result = new ArrayList<>();

			for(String str : lista) {				
				if (str.contains(localPath + '/') ) {
					result.add(str.substring(str.lastIndexOf('/') + 1));
				}
			}
			return result;
			
		} catch (Exception e) {
			Log.setLogger(FileUtil.class, e.getMessage(), Level.ERROR);
		}		
		return new ArrayList<>();
	}
		
	//Converte um array de bytes em arquivo
	public void byteToFle(byte[] bFile, String dir){
	    
		try (
				FileOutputStream fileOuputStream = new FileOutputStream(dir + "/testing.jpg"); 
				)
		
		{ 		   		    
		    fileOuputStream.write(bFile);		    
		    Log.setLogger(FileUtil.class, "Done!", Level.INFO);
			    
	     }catch(Exception e){
	    	 Log.setLogger(FileUtil.class, e.getMessage(), Level.ERROR);
	    }
	}
	
}
