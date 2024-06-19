package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

   
    private static final String URL = "jdbc:mysql://localhost:3306/AGENDA?useSSL=false&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "soutodrex123";

    
    private static Connection con = null;

    // Método para obter a conexão com o banco de dados
    public Connection getConnection() {
        try {
            
            if (con == null || con.isClosed()) {
                
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                con = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar ao banco de dados! " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Driver JDBC não encontrado! " + e);
        }
    }
}
