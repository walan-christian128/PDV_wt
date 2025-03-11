<%@ page import="DAO.TokenServiceDAO" %>
<%
String tokenRecebido = request.getParameter("token");
if (tokenRecebido == null || !TokenServiceDAO.validarToken(tokenRecebido)) {
    // Token inválido ou expirado, redireciona ou exibe uma mensagem de erro
    response.sendRedirect("LinkExpirado.html"); // Crie uma página de erro
    return;
}

%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Cadastro de Empresa / Usuario</title>
    <link rel="icon" href="img/2992655_click_computer_currency_dollar_money_icon.png">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
          crossorigin="anonymous">
</head>
<body
	style="background-image: url('img/Gemini_Generated_Image_97a36f97a36f97a3.jpg'); background-size: auto auto; background-position: center; margin: 0; padding: 0; height: 100vh; width: 100vw;">

<div class="container mt-4">
    <h1 class=""> Cadastro de Usuario/Empresa</h1>
    <form name="editar" action="CadastroUserEmpresa" method="get" class="container mb-3">
      
        <div class="mb-3">
            <label for="id" class="form-label">Nome:</label>
            <input type="text" class="form-control" name="nome" id="nome" 
                 required >
        </div>
        <div class="mb-3">
            <label for="descricao" class="form-label">Telefone:</label>
            <input type="text" class="form-control" id="telefone" name="telefone"
                   required>
        </div>
        <div class="mb-3">
            <label for="quantidade" class="form-label">Email:</label>
            <input type="email" class="form-control" id="email" name="email"
                  required>
        </div>
        <div class=" form-group ls-login-password mb-3">
            <label for="senha" class="form-label ">Senha:</label>
            <input type="password" class="form-control" id="senha" name="senha"
                    required>
        </div>
         <div class="mb-3">
            <label for="preco_compra" class="form-label">Empresa:</label>
            <input type="text" class="form-control" id="base" name="base"
                    required placeholder="DIGITAR NOME SEM ESPAÇOS E SEM CARACTERES ESPECIAIS">
        </div>
        
         <div class="mb-3">
            <label for="cnpj" class="form-label">CNPJ:</label>
            <input type="text" class="form-control" id="cnpj" name="cnpj"
                    >
        </div>
         <div class="mb-3">
            <label for="preco_compra" class="form-label">Endereço:</label>
            <input type="text" class="form-control" id="endereco" name="endereco"
                    >
        </div>
        <div class="mb-3">
    <label for="logo" class="form-label">Imagem:</label>
    <input type="file" class="form-control" id="logo" name="logo" accept="image/*">
</div>
        
       
       
    
        <input type="submit" class="btn btn-primary" value="Salva">
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"></script>
<script src="https://cdn.datatables.net/1.13.7/js/jquery.dataTables.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.11/jquery.mask.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.inputmask/5.0.6/jquery.inputmask.min.js"></script>

<script type="text/javascript">
    $(document).ready(function(){
     
      $('#cnpj').mask('00.000.000/0000-00');
      });
</script>
	<script
				src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.11/jquery.mask.min.js"></script>
			<script
				src="https://cdnjs.cloudflare.com/ajax/libs/jquery.inputmask/5.0.6/jquery.inputmask.min.js"></script>
				<script>
</body>
</html>
