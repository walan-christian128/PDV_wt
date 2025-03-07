package Conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jakarta.servlet.http.HttpSession;

public class ConectionDataBases {
    private String databaseName;

    // Construtor que aceita o nome do banco de dados
    public ConectionDataBases(String databaseName) {
        this.databaseName = databaseName;
    }

    // Método para obter a conexão com o banco de dados
    public Connection getConectionDataBases() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Força o carregamento do driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC do MySQL não encontrado", e);
        }
        
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/distribuidora", "walan", "359483wa@");
    }

}