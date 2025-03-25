package DAO;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import Conexao.ConectionFactory;
import Model.Empresa;
import Model.PasswordUtil;
import Model.Usuario;

public class createData {
    private Connection con;



    public createData(String databaseName) {


        try {
            // Conectar ao MySQL sem especificar um banco de dados para criar o banco
            Connection tempCon = new ConectionFactory().getConnection("");
            if (tempCon == null) {
                System.err.println("Erro: Conexão temporária não foi estabelecida.");
                return;
            }

            // Verifica se o banco de dados já existe e cria se necessário
            createDatabase(tempCon, databaseName);

            // Fechar conexão temporária
            tempCon.close();

            // Conectar ao banco de dados recém-criado
            this.con = new ConectionFactory().getConnection(databaseName);
            if (this.con == null) {
                System.err.println("Erro: Conexão com o novo banco de dados não foi estabelecida.");
                return;
            }

            // Criar tabelas
            createTables();



        } catch (Exception e) {
            System.err.println("Erro ao tentar estabelecer conexão com o banco de dados.");
            e.printStackTrace();
        }
    }

    private void createDatabase(Connection tempCon, String databaseName) throws SQLException {
        Statement statement = tempCon.createStatement();
        try {
            // Verifica se o banco de dados já existe
            String checkDatabaseSQL = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + databaseName + "'";
            ResultSet rs = statement.executeQuery(checkDatabaseSQL);

            if (rs.next()) {
                System.out.println("Banco de dados " + databaseName + " já existe.");
            } else {
                // Criar banco de dados
                String createDatabaseSQL = "CREATE DATABASE " + databaseName;
                statement.executeUpdate(createDatabaseSQL);
                System.out.println("Banco de dados criado com sucesso!");
            }

            rs.close();
        } finally {
            statement.close();
        }
    }

    private void createTables() throws SQLException {
        Statement statement = con.createStatement();
        try {
            // Criação das tabelas
            String createTable_1 = "CREATE TABLE tb_clientes (" +
                    "id int NOT NULL AUTO_INCREMENT," +
                    "nome varchar(100) DEFAULT NULL," +
                    "rg varchar(30) DEFAULT NULL," +
                    "cpf varchar(20) DEFAULT NULL," +
                    "email varchar(200) DEFAULT NULL," +
                    "telefone varchar(30) DEFAULT NULL," +
                    "celular varchar(30) DEFAULT NULL," +
                    "cep varchar(100) DEFAULT NULL," +
                    "endereco varchar(255) DEFAULT NULL," +
                    "numero int DEFAULT NULL," +
                    "complemento varchar(200) DEFAULT NULL," +
                    "bairro varchar(200) DEFAULT NULL," +
                    "cidade varchar(100) DEFAULT NULL," +
                    "estado varchar(2) DEFAULT NULL," +
                    "PRIMARY KEY (id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
            statement.executeUpdate(createTable_1);

            String createTable_2 = "CREATE TABLE tb_fornecedores (" +
                    "id int NOT NULL AUTO_INCREMENT," +
                    "nome varchar(100) DEFAULT NULL," +
                    "cnpj varchar(100) DEFAULT NULL," +
                    "email varchar(200) DEFAULT NULL," +
                    "telefone varchar(30) DEFAULT NULL," +
                    "celular varchar(30) DEFAULT NULL," +
                    "cep varchar(100) DEFAULT NULL," +
                    "endereco varchar(255) DEFAULT NULL," +
                    "numero int DEFAULT NULL," +
                    "complemento varchar(200) DEFAULT NULL," +
                    "bairro varchar(100) DEFAULT NULL," +
                    "cidade varchar(100) DEFAULT NULL," +
                    "estado varchar(2) DEFAULT NULL," +
                    "PRIMARY KEY (id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
            statement.executeUpdate(createTable_2);

            String createTable_3 = "CREATE TABLE tb_funcionarios (" +
                    "id int NOT NULL AUTO_INCREMENT," +
                    "nome varchar(100) DEFAULT NULL," +
                    "rg varchar(30) DEFAULT NULL," +
                    "cpf varchar(20) DEFAULT NULL," +
                    "email varchar(200) DEFAULT NULL," +
                    "senha varchar(10) DEFAULT NULL," +
                    "cargo varchar(100) DEFAULT NULL," +
                    "nivel_acesso varchar(50) DEFAULT NULL," +
                    "telefone varchar(30) DEFAULT NULL," +
                    "celular varchar(30) DEFAULT NULL," +
                    "cep varchar(100) DEFAULT NULL," +
                    "endereco varchar(255) DEFAULT NULL," +
                    "numero int DEFAULT NULL," +
                    "complemento varchar(200) DEFAULT NULL," +
                    "bairro varchar(100) DEFAULT NULL," +
                    "cidade varchar(100) DEFAULT NULL," +
                    "estado varchar(2) DEFAULT NULL," +
                    "PRIMARY KEY (id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
            statement.executeUpdate(createTable_3);

            String createTable_4 = "CREATE TABLE tb_produtos (" +
                    "id int NOT NULL AUTO_INCREMENT," +
                    "descricao varchar(100) DEFAULT NULL," +
                    "preco_de_compra decimal(10,2) DEFAULT NULL," +
                    "preco_de_venda decimal(10,2) DEFAULT NULL," +
                    "qtd_estoque int DEFAULT NULL," +
                    "for_id int DEFAULT NULL," +
                    "PRIMARY KEY (id)," +
                    "KEY for_id (for_id)," +
                    "CONSTRAINT tb_produtos_ibfk_1 FOREIGN KEY (for_id) REFERENCES tb_fornecedores (id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
            statement.executeUpdate(createTable_4);

            String createTable_5 = "CREATE TABLE tb_vendas (" +
                    "id int NOT NULL AUTO_INCREMENT," +
                    "cliente_id int DEFAULT NULL," +
                    "data_venda datetime DEFAULT NULL," +
                    "total_venda decimal(10,2) DEFAULT NULL," +
                    "observacoes text," +
                    "desconto decimal (10,2)DEFAULT NULL,"+
                    "forma_pagamento varchar(200),"+
                    "idUsuario int DEFAULT NULL,"+
                    "PRIMARY KEY (id)," +
                    "KEY cliente_id (cliente_id)," +
                    "CONSTRAINT tb_vendas_ibfk_1 FOREIGN KEY (cliente_id) REFERENCES tb_clientes (id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
            statement.executeUpdate(createTable_5);

            String createTable_6 = "CREATE TABLE tb_itensvendas (" +
                    "id int NOT NULL AUTO_INCREMENT," +
                    "venda_id int DEFAULT NULL," +
                    "produto_id int DEFAULT NULL," +
                    "qtd int DEFAULT NULL," +
                    "subtotal decimal(10,2) DEFAULT NULL," +
                    "PRIMARY KEY (id)," +
                    "KEY venda_id (venda_id)," +
                    "KEY produto_id (produto_id)," +
                    "CONSTRAINT tb_itensvendas_ibfk_1 FOREIGN KEY (venda_id) REFERENCES tb_vendas (id)," +
                    "CONSTRAINT tb_itensvendas_ibfk_2 FOREIGN KEY (produto_id) REFERENCES tb_produtos (id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
            statement.executeUpdate(createTable_6);



            String createTable_7 = "CREATE TABLE tb_empresa (" +
                    "id INT auto_increment PRIMARY KEY," +
                    "nome VARCHAR(150)," +
                    "cnpj VARCHAR(20)," +
                    "endereco VARCHAR(255)," +
                    "logo LONGBLOB" +
                    ");";
            statement.executeUpdate(createTable_7);

            String createTable_8 = "CREATE TABLE tb_usuario (" +
                    "ID INT auto_increment PRIMARY KEY, " +
                    "NOME VARCHAR(150), " +
                    "TELEFONE VARCHAR(150), " +
                    "EMAIL VARCHAR(150), " +
                    "SENHA VARCHAR(300), " +
                    "empresaID INT DEFAULT NULL, " +
                    "CONSTRAINT fk_usuario_empresa FOREIGN KEY (empresaID) REFERENCES tb_empresa(id) ON DELETE CASCADE ON UPDATE CASCADE)";

            statement.executeUpdate(createTable_8);

            System.out.println("Tabelas criadas com sucesso!");
        } finally {
            statement.close();
        }
    }

    @SuppressWarnings("static-access")
    public void inserirEmpresaUsuario(Empresa emp, Usuario uso) throws SQLException {
        if (this.con == null) {
            System.err.println("Erro: Conexão com o banco de dados não foi estabelecida.");
            return;
        }

        // Inicia a transação
        con.setAutoCommit(false);

        String sqlEmpresa = "INSERT INTO tb_empresa (nome, cnpj, endereco, logo) VALUES (?, ?, ?, ?)";
        String sqlUsuario = "INSERT INTO tb_usuario (NOME, TELEFONE, EMAIL, SENHA, empresaID) VALUES (?, ?, ?, ?, ?)";

        try (
            PreparedStatement stmtEmpresa = con.prepareStatement(sqlEmpresa, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement stmtUsuario = con.prepareStatement(sqlUsuario)
        ) {
            PasswordUtil pass = new PasswordUtil();

            // Inserindo a empresa
            stmtEmpresa.setString(1, emp.getNome());
            stmtEmpresa.setString(2, emp.getCnpj());
            stmtEmpresa.setString(3, emp.getEndereco());

            if (emp.getLogo() != null) {
                ByteArrayInputStream bais = new ByteArrayInputStream(emp.getLogo());
                stmtEmpresa.setBinaryStream(4, bais, emp.getLogo().length);
            } else {
                stmtEmpresa.setNull(4, Types.BLOB);
            }



            stmtEmpresa.executeUpdate();

            // Obtendo o ID gerado da empresa
            ResultSet rs = stmtEmpresa.getGeneratedKeys();
            int empresaId = 0;
            if (rs.next()) {
                empresaId = rs.getInt(1);
            }
            rs.close();

            // Inserindo o usuário associado à empresa
            stmtUsuario.setString(1, uso.getNome());
            stmtUsuario.setString(2, uso.getTelefone());
            stmtUsuario.setString(3, uso.getEmail());
            stmtUsuario.setString(4, PasswordUtil.hashPassword(uso.getSenha()));
            stmtUsuario.setInt(5, empresaId); // Relacionando o usuário à empresa criada

            stmtUsuario.executeUpdate();

            // Commit da transação
            con.commit();
            System.out.println("Empresa e usuário inseridos com sucesso!");
        } catch (SQLException e) {
            // Rollback em caso de erro
            con.rollback();
            System.err.println("Erro ao inserir empresa e usuário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Retorna o auto-commit ao normal
            con.setAutoCommit(true);
        }
    }



}