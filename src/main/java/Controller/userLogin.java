package Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import DAO.UsuarioDAO;
import DAO.VendasDAO;
import Model.PasswordUtil;
import Model.Usuario;

/**
 * Servlet implementation class userLogin
 */
@WebServlet(name = "Login.jsp", urlPatterns = { "/logar" })
public class userLogin extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	public userLogin() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
		rd.forward(request, response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    // Obtendo os parâmetros da requisição
	    String email = request.getParameter("email");
	    String senha = request.getParameter("senha");
	    String empresa = request.getParameter("empresa");

	    // Verificar se os parâmetros foram fornecidos
	    if (email == null || email.isEmpty() || senha == null || senha.isEmpty() || empresa == null || empresa.isEmpty()) {
	        request.setAttribute("erro", "Todos os campos devem ser preenchidos.");
	        RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
	        rd.forward(request, response);
	        return;
	    }

	    // Criar ou obter a sessão
	    HttpSession session = request.getSession();
	    session.setAttribute("empresa", empresa);

	    try {
	        // Criar o DAO para o usuário com base na empresa
	        UsuarioDAO dao = new UsuarioDAO(empresa);

	        
	        // Criar um objeto Usuario e buscar o ID no banco
	        Usuario usuarioObj = new Usuario();
	        usuarioObj.setEmail(email);
	        
	        usuarioObj.setSenha(senha);
	        
	        int usuarioID = dao.cidugoUsuario(usuarioObj,empresa); // Busca o ID do usuário

	        if (usuarioID > 0) { // Se encontrou o usuário no banco
	            session.setAttribute("usuarioID", usuarioID);
	            System.out.println("Usuário logado: " + usuarioID);
	            response.sendRedirect("Home.jsp"); // Redireciona após login bem-sucedido
	        } else {
	            request.setAttribute("erro", "Usuário, senha ou empresa incorretos.");
	            RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
	            rd.forward(request, response);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("erro", "Ocorreu um erro ao processar a solicitação.");
	        RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
	        
	        rd.forward(request, response);
	    }
	}


}