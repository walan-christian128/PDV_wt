package DAO;

import Model.Clientes;
import Model.ItensVenda;
import Model.Produtos;
import Conexao.ConectionDataBases;
import Conexao.ConectionFactory;

import Model.Vendas;
import jakarta.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendasDAO {

	 private Connection con;
	 private ConectionDataBases connectionFactory;

	 public VendasDAO(String databaseName) {
	        // Inicialize a conexão com o banco de dados
	        this.connectionFactory = new ConectionDataBases(databaseName);
	        try {
	            this.con = connectionFactory.getConectionDataBases();
	        } catch (SQLException e) {
	            e.printStackTrace(); // Trate a exceção conforme necessário
	        }
	    }
	// Cadastrar Venda//

	public void cadastrarVenda(Vendas obj) {
		try {
			System.out.println("VendasDAO.cadastrarVenda - Cliente: " + obj.getCliente());

			String sql = "insert into tb_vendas(cliente_id,data_venda,total_venda,observacoes,lucro,desconto,forma_pagamento)values(?,?,?,?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(sql);

			if (obj.getCliente() != null) {
				// Define o cliente_id normalmente
				stmt.setInt(1, obj.getCliente().getId());
			} else {
				// Define NULL para cliente_id se não houver cliente
				stmt.setNull(1, java.sql.Types.INTEGER);
			}
			stmt.setString(2, obj.getData_venda());
			stmt.setDouble(3, obj.getTotal_venda());
			stmt.setString(4, obj.getObs());
			stmt.setDouble(5, obj.getLucro());
			stmt.setDouble(6, obj.getDesconto());
			stmt.setString(7, obj.getformaPagamento());

			stmt.execute();

			stmt.close();

		} catch (SQLException erro) {

		}

	}
	// Retorna a Ultima venda//

	public int retornaUltimaVenda() {

		try {
			int idvenda = 0;

			String sql = "select max(id)id from tb_vendas";
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				Vendas p = new Vendas();
				p.setId(rs.getInt("id"));

				idvenda = p.getId();

			}
			return idvenda;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	// Metodo que filtra venda por datas //
	public List<Vendas> listarVendasPorPeriodo(LocalDate data_inicio, LocalDate data_fim) {
		try {

			// 1 passo criar lista de Vendas//
			List<Vendas> lista = new ArrayList<>();

			String sql = "select v.id,date_format(v.data_venda,'%d/%m/%Y')as data_formatada,c.nome,v.total_venda,v.observacoes,v.lucro,v.desconto from tb_vendas as v "
					+ "inner join tb_clientes as c on(v.cliente_id = c.id)where v.data_venda BETWEEN? AND?";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, data_inicio.toString());
			stmt.setString(2, data_fim.toString());

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Vendas obj = new Vendas();
				Clientes c = new Clientes();

				obj.setId(rs.getInt("v.id"));
				obj.setData_venda(rs.getString("data_formatada"));
				c.setNome(rs.getString("c.nome"));
				obj.setTotal_venda(rs.getDouble("v.total_venda"));
				obj.setObs(rs.getString("v.observacoes"));
				obj.setLucro(rs.getDouble("v.lucro"));
				obj.setDesconto(rs.getDouble("v.desconto"));

				obj.setCliente(c);

				lista.add(obj);

			}

			return lista;

		} catch (SQLException e) {

			return null;

		}

	}

	 public List<Vendas> listarVendasdoDia() {
	        List<Vendas> lista = new ArrayList<>();
	        String sql = "select v.id, date_format(v.data_venda, '%d/%m/%Y %H:%i:%s') as data_formatada, "
	                   + "c.nome, v.total_venda, v.observacoes, v.lucro, v.desconto, v.forma_pagamento "
	                   + "from tb_vendas as v "
	                   + "left join tb_clientes as c on (v.cliente_id = c.id) "
	                   + "where date(v.data_venda) = ?";

	        try (PreparedStatement stmt = con.prepareStatement(sql)) {
	            // Obter a data atual do servidor
	            Date agora = new Date();
	            SimpleDateFormat dataEUA = new SimpleDateFormat("yyyy-MM-dd");
	            String datamysql = dataEUA.format(agora);
	            stmt.setString(1, datamysql);

	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    Vendas obj = new Vendas();
	                    Clientes c = new Clientes();

	                    obj.setId(rs.getInt("v.id"));
	                    obj.setData_venda(rs.getString("data_formatada"));
	                    c.setNome(rs.getString("c.nome"));
	                    obj.setTotal_venda(rs.getDouble("v.total_venda"));
	                    obj.setObs(rs.getString("v.observacoes"));
	                    obj.setLucro(rs.getDouble("v.lucro"));
	                    obj.setDesconto(rs.getDouble("v.desconto"));
	                    obj.setFormaPagamento(rs.getString("v.forma_pagamento"));

	                    obj.setCliente(c);

	                    lista.add(obj);
	                }
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return lista;
	    }

	public List<Vendas> totalPorPeriodo(Date data_inicio, Date data_fim) {
		List<Vendas> lista = new ArrayList<>();
		String sql = "SELECT SUM(total_venda) AS total_periodo, DATE_FORMAT(data_venda, '%d/%m/%Y') AS data_formatada "
				+ "FROM tb_vendas " + "WHERE data_venda BETWEEN ? AND ? " + "GROUP BY data_formatada";

		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			// Usar setDate para datas
			stmt.setDate(1, new java.sql.Date(data_inicio.getTime()));
			stmt.setDate(2, new java.sql.Date(data_fim.getTime()));

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Vendas obj = new Vendas();
					obj.setData_venda(rs.getString("data_formatada"));
					obj.setTotal_venda(rs.getDouble("total_periodo"));
					lista.add(obj); // Adicionar o objeto à lista
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erro: " + e.getMessage());
		}

		return lista; // Retorna a lista preenchida
	}

	// Metodo que calcula total da vendaa por data//
	public double retornaTotalVendaPorData(Date data_venda) {
	    try {
	        double totalvenda = 0;
	        String sql = "SELECT SUM(total_venda) as total FROM tb_vendas WHERE DATE_FORMAT(data_venda, '%d/%m/%Y') = ?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, new SimpleDateFormat("dd/MM/yyyy").format(data_venda));

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            totalvenda = rs.getDouble("total");
	        }
	      
	        return totalvenda;

	    } catch (SQLException e) {
	        throw new RuntimeException(e);
	    }
	}


	public double retornaTotalVendaPorDia(LocalDate data_venda) {
		try {
			double totalvenda = 0;

			String sql = "SELECT SUM(total_venda) as total FROM tb_vendas WHERE DATE(data_venda) = ?";

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, data_venda.toString());

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				totalvenda = rs.getDouble("total");

			}
		
			return totalvenda;

		} catch (SQLException e) {
			throw new RuntimeException(e);

		}

	}

	public void selVendas(String cpf, int idprod) {

		try {
			String sql = "select " + "cli.id, " + "cli.nome, " + "cli.cpf, " + "cli.endereco, " + "cli.numero, "
					+ "prod.id, " + "prod.descricao, " + "prod.preco_de_venda, " + "prod.preco_de_compra "
					+ "from tb_clientes as cli, " + "tb_produtos as prod " + " where cli.cpf like ? "
					+ " and prod.id = ? ";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cpf);
			stmt.setInt(2, idprod);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Clientes cli = new Clientes();
				Produtos prod = new Produtos();

				cli.setId(rs.getInt("cli.id"));
				cli.setNome(rs.getString("cli.nome"));
				cli.setCpf(rs.getString("cli.cpf"));
				cli.setBairro(rs.getString("cli.endereco"));
				;
				cli.setNumero(rs.getInt("cli.numero"));
				prod.setId(rs.getInt("prod.id"));
				prod.setDescricao(rs.getString("prod.descricao"));
				prod.setPreco_de_venda(rs.getDouble("prod.preco_de_venda"));
				prod.setPreco_de_compra(rs.getDouble("prod.preco_de_compra"));

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	public List<ItensVenda> maisVendidos(Date dataInicio, Date dataFim){
		List<ItensVenda> lista = new ArrayList<>();
		
		try {
			String sql =" SELECT DISTINCT "
					+ " SUM(ITENS.QTD)AS QUANTIDADE, "
					+ " PRODUTO.DESCRICAO "
					+ " FROM TB_ITENSVENDAS AS ITENS "
					+ " INNER JOIN TB_PRODUTOS AS PRODUTO ON PRODUTO.ID = ITENS.PRODUTO_ID "
					+ " INNER JOIN TB_VENDAS AS VENDAS  ON ITENS.VENDA_ID = VENDAS.ID "
					+ " WHERE  DATE(VENDAS.DATA_VENDA)  BETWEEN? AND? "
					+ " GROUP BY PRODUTO.DESCRICAO "
					+ " ORDER BY QUANTIDADE DESC ";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setDate(1, new java.sql.Date(dataInicio.getTime()));
			stmt.setDate(2, new java.sql.Date(dataFim.getTime()));
			
			ResultSet rs = stmt.executeQuery();
			 while(rs.next()){
				 Produtos produtos = new Produtos();
				 ItensVenda itesnvenda = new ItensVenda(); 
				 
				 itesnvenda.setQtd(rs.getInt("QUANTIDADE"));
				 produtos.setDescricao(rs.getString("DESCRICAO"));
				 
				 itesnvenda.setProduto(produtos);
				 
				 
				 lista.add(itesnvenda);
				
				 
				 
				 
			 }
			 
			
		} catch (SQLException e) {
			
		}
		return lista;
		
	}
	public double lucroVenda(int id) {
	    double totalVenda = 0;

	    String sql = "SELECT SUM(produto.preco_de_venda - produto.preco_de_compra) AS lucro_da_venda "
	               + "FROM tb_produtos AS produto "
	               + "INNER JOIN tb_itensvendas AS itens ON itens.produto_id = produto.id "
	               + "WHERE itens.venda_id = ?";

	    try (PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setInt(1, id);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) { // Só existe um resultado
	                totalVenda = rs.getDouble("lucro_da_venda");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Loga o erro no console
	    }

	    return totalVenda;
	}
	public double lucroPorPeriod(Date dataInicio, Date dataFim){
	
		double totalLucro = 0;
		
		try {
			String sql =  "   SELECT SUM(PRODUTO.PRECO_DE_VENDA - PRODUTO.PRECO_DE_COMPRA) AS LUCRO_DA_VENDA "
					    + "   FROM TB_PRODUTOS AS PRODUTO "
					    + "   INNER JOIN TB_ITENSVENDAS AS ITENS ON ITENS.PRODUTO_ID = PRODUTO.ID "
					    + "   INNER JOIN TB_VENDAS      AS VENDA ON VENDA.ID = ITENS.VENDA_ID "
					    + "   WHERE "
					    + "   DATE(VENDA.DATA_VENDA) BETWEEN ? AND ? ";
			
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setDate(1, new java.sql.Date(dataInicio.getTime()));
			stmt.setDate(2, new java.sql.Date(dataFim.getTime()));
			
			ResultSet rs = stmt.executeQuery();
			 if(rs.next()){
				 
				 totalLucro = rs.getDouble("LUCRO_DA_VENDA");
				 
				
				
				 
				 
				 
			 }
			 
			
		} catch (SQLException e) {
			
		}
		return totalLucro;
		
	}

	


}