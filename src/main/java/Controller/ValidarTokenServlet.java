package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import DAO.TokenServiceDAO;

@WebServlet("/UsuarioeEmpresa.html")


public class ValidarTokenServlet extends HttpServlet {
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	      try {
	    	  String token = TokenServiceDAO.gerarToken(); // Gera o token
		        
		        
		        String link = "http://localhost:8080/PDV/UsuarioeEmpresa.jsp?token=" + token; // Monta o link
		        
		        // Armazena o link na requisição para ser exibido na JSP
		        request.setAttribute("token", link);
		        
		        // Encaminha para a JSP
		        request.getRequestDispatcher("GerarLink.jsp").forward(request, response);
		} catch (Exception e) {
			// TODO: handle exception
		}
	    }
}


