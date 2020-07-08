package br.eti.amazu.blankapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
	
@WebServlet(name = "HelloServlet", urlPatterns = {"/oi", "/ola"})
public class HelloServlet extends HttpServlet{

	private static final long serialVersionUID = -7603622514092516565L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException{
		
		response.setContentType("text/html");
 
		// Escreverendo o conteudo da pagina.		
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h1>Hello World!</h1>");
		out.println("</body>");
		out.println("</html>");
	}
	
}
