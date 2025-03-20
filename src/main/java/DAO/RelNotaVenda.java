package DAO;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import Conexao.ConectionFactory;

public class RelNotaVenda {
    private static final Logger LOGGER = Logger.getLogger(RelNotaVenda.class.getName());
    @SuppressWarnings("unused")
	private String empresa;
    private Connection connection;

    public RelNotaVenda(String empresa) throws SQLException, ClassNotFoundException {
        this.empresa = empresa;
        this.connection = new ConectionFactory().getConnection(empresa);
    }
    public RelNotaVenda(String empresa, Connection connection) {
        this.empresa = empresa;
        this.connection = connection;
    }

    public JasperPrint gerarRelatorio(String layoutPath, Map<String, Object> parametros) throws JRException, IOException, SQLException {
        // Carrega o design do relatório a partir do ClassLoader
        InputStream inputStream = getClass().getResourceAsStream("/" + layoutPath);

        if (inputStream == null) {
            LOGGER.log(Level.SEVERE, "Arquivo JRXML não encontrado no classpath: " + layoutPath);
            throw new FileNotFoundException("Arquivo JRXML não encontrado no classpath: " + layoutPath);
        } else {
            LOGGER.log(Level.INFO, "Arquivo JRXML encontrado no classpath: " + layoutPath);
        }

        try {
            // Carrega o layout do relatório
            JasperDesign desenho = JRXmlLoader.load(inputStream);

            // Compila o relatório
            JasperReport relatorio = JasperCompileManager.compileReport(desenho);
            LOGGER.log(Level.INFO, "Relatório compilado com sucesso.");

            // Verifica se a conexão é válida antes de preencher o relatório
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Conexão com o banco de dados está fechada ou inválida.");
            }

            // Preenche o relatório com os parâmetros fornecidos
            JasperPrint impressao = JasperFillManager.fillReport(relatorio, parametros, connection);
            LOGGER.log(Level.INFO, "Relatório preenchido com sucesso.");

            return impressao;
        } finally {
            inputStream.close();
            connection.close(); // Fecha a conexão após o uso
        }
    }
}
