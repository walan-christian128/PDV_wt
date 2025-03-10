<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%
String sucesso = (String) request.getAttribute("ok");
String erro = (String) request.getAttribute("erro");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Esqueci a senha</title>
<link rel="icon"
	href="img/2992655_click_computer_currency_dollar_money_icon.png">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
	crossorigin="anonymous">
</head>
<body
	style="background-image: url('img/Gemini_Generated_Image_97a36f97a36f97a3.jpg'); background-size: auto auto; background-position: center; margin: 0; padding: 0; height: 100vh; width: 100vw;">

	<div class="container mt-4">
		<h1 class="">Recuperação de senha</h1>
		<form name="editar" action="RecuperaSenhaServlet" method="get"
			class="container mb-3">
			<h2>Digite seu email:</h2>
			<div class="mb-3">
				<label for="id" class="form-label">Email:</label> <input type="text"
					class="form-control" name="email" id="email" required>
			</div>
			<div class="mb-3">
				<label for="id" class="form-label">Empresa:</label> <input
					type="text" class="form-control" name="empresa" id="empresa"
					required>
			</div>

			<div>
				<input type="submit" class="btn btn-primary" value="Enviar">
			</div>
			<div class="mb-3">
				<p style="background-color: white;">
					<%
					if (sucesso != null) {
					%>
				
				<div style="color: green;"><%=sucesso%></div>
				<%
				}
				%>

				<%
				if (erro != null) {
				%>
				<div style="color: red;"><%=erro%></div>
				<%
				}
				%></p>

			</div>

		</form>
	</div>

	<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"></script>
	<script src="https://cdn.datatables.net/1.13.7/js/jquery.dataTables.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.11/jquery.mask.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery.inputmask/5.0.6/jquery.inputmask.min.js"></script>
</body>
</html>
