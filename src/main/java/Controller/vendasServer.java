package Controller;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import DAO.ClientesDAO;
import DAO.ProdutosDAO;
import DAO.RelNotaVenda;
import DAO.VendasDAO;
import DAO.itensVendaDAO;
import Model.Clientes;
import Model.ItensVenda;
import Model.Produtos;
import Model.Vendas;
import java.io.FileInputStream;
import java.io.InputStream;


/**
 * Servlet implementation class vendasServer
 */

@WebServlet(urlPatterns = { "/selecionarClienteProdutos", "/inserirItens", "/InseirVendaEintens", "/PeriodoVenda",
		"/dia", "/maisVendidos","/exibirRelatorio","/lucroVenda" ,"/lucroPeriodo","/desconto" })
@MultipartConfig

public class vendasServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private static final Logger LOGGER = Logger.getLogger(vendasServer.class.getName());

	double total, subtotal, preco, meuPreco;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public vendasServer() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Obtendo a sessão
		HttpSession session = request.getSession();
		String empresa = (String) session.getAttribute("empresa"); // Exemplo de atributo de sessão

		// Agora, você pode usar o valor da "empresa" em qualquer parte do seu código
		if (empresa != null) {
			System.out.println("Empresa selecionada: " + empresa);
		} else {
			System.out.println("Nenhuma empresa selecionada.");
		}

		String action = request.getServletPath();
		switch (action) {
		case "/selecionarClienteProdutos":
			try {
				selecionarClienteProd(request, response);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case "/inserirItens":
			inserirItens(request, response);
			break;
		case "/InseirVendaEintens":
			inserirVendas(request, response);
			break;
		case "/PeriodoVenda":
			vendaPorPeriodo(request, response);
			break;
		case "/dia":
			vendaPorDia(request, response);
			break;
		case "/maisVendidos":
			maisVendidos(request, response);
			break;
		case "/exibirRelatorio":
			gerarRelatorio(request, response);
			break;
		case "/lucroVenda":
			lucroVenda(request, response);
			break;
		case "/lucroPeriodo":
			lucroPeriodo(request, response);
			break;
		case "/desconto":
			descontoVenda(request, response);
			break;
		default:
			response.getWriter().append("Ação não reconhecida.");
			break;
		}
	}
	protected void descontoVenda(HttpServletRequest request, HttpServletResponse response) {
	    try {
	        // Obtém os parâmetros do formulário
	        String descontotela = request.getParameter("desconto");
	        String totalVenda = request.getParameter("totalVendaAtualizado");

	        if (descontotela != null && totalVenda != null) {
	            try {
	                // Conversão de strings para double
	                double descontoValor = Double.parseDouble(descontotela);
	                double vendaTela = Double.parseDouble(totalVenda);

	                // Calcula o valor final com desconto
	                double descontoFinal = vendaTela - descontoValor;

	                // Atribui o resultado como atributo na requisição
	                HttpSession session = request.getSession();
	                session.setAttribute("totalVendaAtualizado", descontoFinal);

	                

	                // Encaminha a requisição para o JSP
	                RequestDispatcher dispatcher = request.getRequestDispatcher("realizarVendas.jsp");
	                dispatcher.forward(request, response);

	            } catch (NumberFormatException e) {
	                e.printStackTrace();
	                response.getWriter().println("Erro: valores inválidos para cálculo.");
	            }
	        } else {
	            response.getWriter().println("Erro: parâmetros 'desconto' e 'totalVenda' não enviados.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
				response.getWriter().println("Erro interno no servidor: " + e.getMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}



	private void lucroPeriodo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String datalucroinicio = request.getParameter("dataIniciallucro");
		String datalucrofim = request.getParameter("dataFinallucro");
		
		HttpSession session = request.getSession();
		String empresa = (String) session.getAttribute("empresa");

		if (datalucroinicio != null && datalucrofim != null) {
			String fomatoData = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(fomatoData);

			try {
				Date datainicalFormata = sdf.parse(datalucroinicio);
				Date datafinalFormata = sdf.parse(datalucrofim);
				VendasDAO dao = new VendasDAO(empresa);
				

				double lucroPeriodo = dao.lucroPorPeriod(datainicalFormata, datafinalFormata);

				request.setAttribute("totalLucro", lucroPeriodo);

				RequestDispatcher dispatcher = request.getRequestDispatcher("Home.jsp");
				dispatcher.forward(request, response);

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}
	protected void lucroVenda(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String codigoVenda = request.getParameter("CodigoVenda");
		int idVenda = Integer.parseInt(codigoVenda);

		HttpSession session = request.getSession();
		String empresa = (String) session.getAttribute("empresa");

		try {
			

			VendasDAO dao = new VendasDAO(empresa);
			double lucroVenda = dao.lucroVenda(idVenda);

			request.setAttribute("lucro", lucroVenda);
			request.setAttribute("vendaCodigo", idVenda);
		
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("Home.jsp");
			dispatcher.forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			// Handle exception appropriately
		}
		
	}
	
	@SuppressWarnings("unused")
	protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    String empresa = (String) session.getAttribute("empresa");

	    // Verificar se o nome da empresa está disponível na sessão
	    if (empresa == null) {
	        response.getWriter().write("Empresa não fornecida.");
	        LOGGER.log(Level.WARNING, "Empresa não fornecida.");
	        return;
	    }

	    try {
	        // Instancia a classe responsável pela geração do relatório
	        RelNotaVenda relatorio = new RelNotaVenda(empresa);
	        
	        // Gera o relatório
	        JasperPrint jasperPrint = relatorio.gerarRelatorio("RelatoriosJasper/pdvRelComprovanteVenda.jrxml");
	        LOGGER.log(Level.INFO, "Relatório gerado com sucesso para a empresa: " + empresa);

	        // Define o tipo de resposta como PDF
	        response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition", "inline; filename=relatorio_venda.pdf");

	        // Exporta o relatório para o fluxo de saída HTTP
	        try (OutputStream outStream = response.getOutputStream()) {
	            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	            outStream.flush();
	            request.setAttribute("rel", outStream);  
	        }

	    } catch (JRException | SQLException | ClassNotFoundException e) {
	        LOGGER.log(Level.SEVERE, "Erro ao gerar relatório para a empresa: " + empresa, e);
	        response.getWriter().write("Erro ao gerar relatório: " + e.getMessage());
	    }
	}

	private void maisVendidos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String dataVendainicio = request.getParameter("dataVendainicio");
		String dataVendafim = request.getParameter("dataVendafim");
		
		HttpSession session = request.getSession();
		String empresa = (String) session.getAttribute("empresa");

		if (dataVendainicio != null && dataVendafim != null) {
			String fomatoData = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(fomatoData);

			try {
				Date datainicalFormata = sdf.parse(dataVendainicio);
				Date datafinalFormata = sdf.parse(dataVendafim);
				VendasDAO dao = new VendasDAO(empresa);
				

				ArrayList<ItensVenda> lista_2 = (ArrayList<ItensVenda>) dao.maisVendidos(datainicalFormata,
						datafinalFormata);

				request.setAttribute("maisVendidos", lista_2);

				RequestDispatcher dispatcher = request.getRequestDispatcher("Home.jsp");
				dispatcher.forward(request, response);

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	private void vendaPorDia(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String data = request.getParameter("data");

		HttpSession session = request.getSession();
		String empresa = (String) session.getAttribute("empresa");

		try {
			SimpleDateFormat dataVenda = new SimpleDateFormat("dd/MM/yyyy");
			Date dataVendaInf = dataVenda.parse(data);

			VendasDAO dao = new VendasDAO(empresa);
			double totalVenda = dao.retornaTotalVendaPorData(dataVendaInf);

			request.setAttribute("totalVenda2", totalVenda);
			request.setAttribute("data", data);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("Home.jsp");
			dispatcher.forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			// Handle exception appropriately
		}

	}

	private void vendaPorPeriodo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String dataInicial = request.getParameter("dataInicial");
		String dataFinal = request.getParameter("dataFinal");

		HttpSession session = request.getSession();
		String empresa = (String) session.getAttribute("empresa");

		if (dataInicial != null && dataFinal != null) {
			String fomatoData = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(fomatoData);

			try {
				Date datainicalFormata = sdf.parse(dataInicial);
				Date datafinalFormata = sdf.parse(dataFinal);
				VendasDAO dao = new VendasDAO(empresa);
				ArrayList<Vendas> lista_2 = (ArrayList<Vendas>) dao.totalPorPeriodo(datainicalFormata,
						datafinalFormata);
				request.setAttribute("periodo", lista_2);
				
				RequestDispatcher dispatcher = request.getRequestDispatcher("Home.jsp");
				dispatcher.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	private void inserirVendas(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		String empresa = (String) session.getAttribute("empresa");

		String idCli = request.getParameter("cliId");
		String dataVenda = request.getParameter("data");
		String totalVenda = request.getParameter("totalVendaAtualizado");
		String Obs = request.getParameter("observacao");
		String desconto = request.getParameter("desconto");
		String formaPagamento = request.getParameter("formaPagamento");

		Vendas obj = new Vendas();

		try {

			

			
			if (idCli != null && !idCli.isEmpty() && !idCli.equals("0")) {
				// Se houver ID do cliente, atribui ao objeto
				Clientes objCli = new Clientes();
				int cliId = Integer.parseInt(idCli);
				objCli.setId(cliId);
				obj.setCliente(objCli);
			} else {
				// Caso não haja ID do cliente, o objeto cliente será nulo
				obj.setCliente(null); // Deixe claro que não há cliente
			}

			// Definindo os outros campos da venda
			obj.setData_venda(dataVenda);
			obj.setTotal_venda(Double.parseDouble(totalVenda));
			obj.setObs(Obs);
			obj.setDesconto(Double.parseDouble(desconto));
			obj.setFormaPagamento(formaPagamento);

			VendasDAO dao = new VendasDAO(empresa);
			dao.cadastrarVenda(obj); // Aqui deve funcionar normalmente mesmo sem cliente

			// Capturando o ID da venda recém-criada
			obj.setId(dao.retornaUltimaVenda());

			// Processando os itens da venda (mantido igual)
			JSONArray itensArray = (JSONArray) session.getAttribute("itens");
			if (itensArray != null) {
				for (int i = 0; i < itensArray.length(); i++) {
					JSONObject linha = itensArray.getJSONObject(i);

					total = 0.0;
					

					String idProdVenda = linha.getString("idProd");
					String qtdProd = linha.getString("qtdProd");
					String subItens = linha.getString("subtotal");

					ProdutosDAO dao_produto = new ProdutosDAO(empresa);
					itensVendaDAO daoitem = new itensVendaDAO(empresa);
					Produtos objp = new Produtos();
					ItensVenda itens = new ItensVenda();

					itens.setVenda(obj); // Relaciona o item à venda
					objp.setId(Integer.parseInt(idProdVenda));
					itens.setProduto(objp);
					itens.setQtd(Integer.parseInt(qtdProd));
					itens.setSubtotal(Double.parseDouble(subItens));

					int qtd_estoque, qtd_comprada, qtd_atualizada;
					// Baixa no estoque
					qtd_estoque = dao_produto.retornaEstoqueAtual(objp.getId());
					qtd_comprada = Integer.parseInt(qtdProd);
					qtd_atualizada = qtd_estoque - qtd_comprada;

					dao_produto.baixarEstoque(objp.getId(), qtd_atualizada);

					// Cadastrar o item de venda
					daoitem.cadastraItem(itens);
				}

				total = 0.0;
			
				session.removeAttribute("totalVendaAtualizado");
				session.removeAttribute("itens");
				session.removeAttribute("desconto");
				session.removeAttribute("idProd");
				session.removeAttribute("desProd");

				response.sendRedirect("realizarVendas.jsp");
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	
		System.out.println("Cliente ID: " + idCli);
		System.out.println("Data Venda: " + dataVenda);
		System.out.println("Total Venda: " + totalVenda);
		System.out.println("Observação: " + Obs);
		System.out.println("Desconto: " + desconto);
		System.out.println("Forma de Pagamento: " + formaPagamento);

		HttpSession newSession = request.getSession(true);
		newSession.removeAttribute("totalVenda");
	}

	private void inserirItens(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		try {
			// Lendo os dados enviados pela requisição AJAX
			BufferedReader reader = request.getReader();
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject itemJson = new JSONObject(sb.toString());

			String idProd = itemJson.getString("idProd");
			String desProd = itemJson.getString("desProd");
			String qtdProd = itemJson.getString("qtdProd");
			String precoProd = itemJson.getString("precoProd");
			String precoMeu = itemJson.getString("compraProd");

			// Verificando se a quantidade não é nula
			if (qtdProd != null) {
				int qtdPrdo = Integer.parseInt(qtdProd);
				preco = Double.parseDouble(precoProd);
				meuPreco = Double.parseDouble(precoMeu);

				// Calculando o subtotal
				subtotal = qtdPrdo * preco;
				total += subtotal;

				request.setAttribute("idProd", idProd);
				request.setAttribute("desProd", desProd);
				request.setAttribute("qtdProd", qtdProd);
				request.setAttribute("subtotal", subtotal);
				request.setAttribute("totalVendaAtualizado", total);
				

				/* total = (Double) session.getAttribute("totalVenda"); */

				// Calculando o lucro para este item

				// Construindo a nova linha da tabela HTML
				String newRow = "<tr>" + "<td>" + idProd + "</td>" + "<td>" + desProd + "</td>" + "<td>" + qtdProd
						+ "</td>" + "<td>" + precoProd + "</td>" + "<td>" + subtotal + "</td>" + "</tr>";

				JSONObject newItem = new JSONObject();

				newItem.put("idProd", idProd);
				newItem.put("desProd", desProd);
				newItem.put("qtdProd", qtdProd);
				newItem.put("precoProd", precoProd);
				newItem.put("subtotal", String.valueOf(subtotal));
				newItem.put("totalVendaAtualizado", String.valueOf(total));

				JSONArray itens = (JSONArray) session.getAttribute("itens");

				if (itens == null) {
					itens = new JSONArray();
				}

				itens.put(newItem);

				// Atualizar a lista de itens na sessão
				session.setAttribute("itens", itens);
				session.setAttribute("totalVendaAtualizado", total);
			

				// Escrevendo a nova linha na resposta

				PrintWriter out = response.getWriter();
				out.println(newRow);
				
				session.removeAttribute("idProd");
				session.removeAttribute("desProd");
				session.removeAttribute("qtdProd");
				session.removeAttribute("precoProd");
				session.removeAttribute("compraProd");
			

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		      

	}

	private void selecionarClienteProd(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException {
		String cpfCli = request.getParameter("cliCpf");
		String idProdStr = request.getParameter("idProd");
		int idProd = Integer.parseInt(idProdStr);
		HttpSession session = request.getSession();
		String empresa = (String) session.getAttribute("empresa");
		

		

		try {
			
			Produtos prod = new Produtos();
			ProdutosDAO prodDAO = new ProdutosDAO(empresa);
			Clientes cli = new Clientes();
			ClientesDAO cliDAO = new ClientesDAO(empresa);

			cli = cliDAO.consultarClientesPorcpf(cpfCli);
			request.setAttribute("cliId", cli.getId());
			request.setAttribute("cliNome", cli.getNome());
			request.setAttribute("cliCpf", cli.getCpf());
			request.setAttribute("cliEndereco", cli.getEndereco());
			request.setAttribute("cliNumero", cli.getNumero());
			prod = prodDAO.consultarPorCodigo(idProd);
			request.setAttribute("idProd", prod.getId());
			request.setAttribute("desProd", prod.getDescricao());
			request.setAttribute("compraProd", prod.getPreco_de_compra());
			request.setAttribute("precoProd", prod.getPreco_de_venda());

			RequestDispatcher rd = request.getRequestDispatcher("realizarVendas.jsp");
			rd.forward(request, response);

		} catch (Exception e) {

		}

		session.isNew();

	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}