package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectDB {

	 private static final String url = "jdbc:mysql://localhost:3306/mydb";
	 private static final String user = "root";
	 private static final String password = "soutodrex123";
	 private static Connection con;
	 
	 public Connection getconnection() {
		 try {
			if(con == null) {
				con = DriverManager.getConnection(url, user, password);
				return con;
			}else {
				return con;
			}
			 
		 }catch(SQLException e) {
			 e.printStackTrace();
			 return null;
		 }
		 
		 
	 }
	 
}
