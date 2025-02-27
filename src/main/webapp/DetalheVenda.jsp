<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="Model.Vendas"%>
<%@ page import="Model.ItensVenda"%>
<%@ page import="DAO.VendasDAO"%>
<%@ page import="Model.Produtos"%>
<%@ page import="DAO.ProdutosDAO"%>
<%
String empresa = (String) session.getAttribute("empresa");
if (empresa == null || empresa.isEmpty()) {
    throw new RuntimeException("O nome da empresa não está definido na sessão.");
}
List<Vendas> lista;
VendasDAO Vdao = new VendasDAO(empresa);
lista = Vdao.listarVendasdoDia();
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="utf-8">
<title>Detalhe Venda</title>
  
   <link rel="icon"
	href="img/2992664_cart_dollar_mobile_shopping_smartphone_icon.png">
   <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Q/jnqA9/ctw53zwTwj9tdG1x8czgkF+4hJbUBt1ZZbPr42N2zrgfmjjM+KAX1nbj" crossorigin="anonymous">
</head>
<body
	style="background-image: url('img/Gemini_Generated_Image_97a36f97a36f97a3.jpg'); background-size: auto auto; background-position: center; margin: 0; padding: 0; height: 100vh; width: 100vw;">
<%@ include file="index.html"%>
	<%@ include file="menu.jsp"%>
	<div class="container mt-4">
	<div class="row">
	<div class="col-md-6">
	<div class="table-container">
		<table id="VendaDiaria" class="table table-dark table-striped table-hover">
			<thead>
				<tr>
					<th>Descricão</th>
					<th>Quantidade</th>
					<th>Preço</th>
					<th>Subtotal</th>
					

				</tr>
			</thead>
			<tbody>
               <!-- Itera sobre os objetos ItensVenda -->
            <% List<ItensVenda> itensVendaList = (List<ItensVenda>)request.getAttribute("tableRows");
            if(itensVendaList != null) {
                for(ItensVenda item : itensVendaList) { %>
                    <tr>
                        <td><%= item.getProduto().getDescricao() %></td>
                        <td><%= item.getQtd() %></td>
                        <td><%= item.getProduto().getPreco_de_venda() %></td>
                        <td><%= item.getSubtotal() %></td>
                    </tr>
                <% }
            } %>
             
			</tbody>

		</table>
		
		<div>
		 <a  href="Home.jsp" type="button" class= "btn btn-danger">Voltar</a>
		 </div>
		
   </div>
   </div>

	</div>
	</div>
	
	
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"></script>
<script src="https://cdn.datatables.net/1.13.7/js/jquery.dataTables.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.11/jquery.mask.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.inputmask/5.0.6/jquery.inputmask.min.js"></script> 
        
           
            
</body>

</html>