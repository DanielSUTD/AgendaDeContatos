package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    // Atributos constantes com os detalhes da conexão ao banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/mydb?useSSL=false&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "soutodrex123";

    // Atributo estático para armazenar a conexão
    private static Connection con = null;

    // Método para obter a conexão com o banco de dados
    public Connection getConnection() {
        try {
            // Verifica se a conexão é nula ou está fechada
            if (con == null || con.isClosed()) {
                // Carrega o driver JDBC do MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Estabelece a conexão com o banco de dados usando o DriverManager
                con = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            // Retorna a conexão estabelecida
            return con;
        } catch (SQLException e) {
            // Imprime o stack trace da exceção em caso de erro ao conectar ao banco de dados
            e.printStackTrace();
            // Lança uma nova RuntimeException com uma mensagem de erro
            throw new RuntimeException("Erro ao conectar ao banco de dados! " + e);
        } catch (ClassNotFoundException e) {
            // Imprime o stack trace da exceção em caso de erro ao carregar o driver JDBC
            e.printStackTrace();
            // Lança uma nova RuntimeException com uma mensagem de erro
            throw new RuntimeException("Driver JDBC não encontrado! " + e);
        }
    }
}
