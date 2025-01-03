package Conexao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConectionFactory {
    public Connection getConnection(String databaseName) {
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/MyDB");
            // Retorne a conexão do DataSource
            Connection connection = ds.getConnection();
            connection.setCatalog(databaseName); // Define o banco de dados dinamicamente
            return connection;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + databaseName, e);
        }
    }
}
