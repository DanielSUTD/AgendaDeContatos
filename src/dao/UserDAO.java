package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

public class UserDAO {
	
	// Método select para selecionar o usuário através de seu email
	public User getUserByEmail(String email) throws SQLException {
	    User user = null;

	    
	    String query = "SELECT ID_USUARIO, NOME, EMAIL, SENHA, ALTERNATIVA, RESPOSTA FROM usuario WHERE EMAIL=?";

	   
	    try (Connection con = new ConnectDB().getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(query)) {

	        
	        preparedStatement.setString(1, email);

	        
	        ResultSet resultSet = preparedStatement.executeQuery();

	        
	        if (resultSet.next()) {
	           
	            int userId = resultSet.getInt("ID_USUARIO");
	            String name = resultSet.getString("NOME");
	            String userEmail = resultSet.getString("EMAIL");
	            String hashedPassword = resultSet.getString("SENHA");
	            String question = resultSet.getString("ALTERNATIVA");
	            String answer = resultSet.getString("RESPOSTA");

	            
	            user = new User(userId, name, userEmail, hashedPassword, question, answer);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	   
	    return user;
	}
	
	//Método Select para selecionar o usuário através do seu ID!
    public User getUserById(int userID) throws SQLException {
        User user = null;
        
      
        String query = "SELECT ID_USUARIO, NOME, EMAIL, SENHA, ALTERNATIVA, RESPOSTA FROM usuario WHERE ID_USUARIO = ?";

   
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
        	
        	
            preparedStatement.setInt(1, userID);
            
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
         
            if (resultSet.next()) {
                int id = resultSet.getInt("ID_USUARIO");
                String name = resultSet.getString("NOME");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("SENHA");
                String question = resultSet.getString("ALTERNATIVA");
                String answer = resultSet.getString("RESPOSTA");
                
            
                user = new User(id, name, email, password, question, answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }
	
 // Método para inserir um novo usuário no banco de dados
    public void insertUser(User user) throws SQLException {
       
        String query = "INSERT INTO usuario (NOME, EMAIL, SENHA, ALTERNATIVA, RESPOSTA) VALUES (?, ?, ?, ?, ?)";
        
       
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getQuestion());
            preparedStatement.setString(5, user.getAnswer());

            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   
    public void updatePassword(String email, String hashedPassword) throws SQLException {
        
        String query = "UPDATE usuario SET SENHA=? WHERE EMAIL=?";
        
        
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, email);

            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para atualizar todos os dados de um usuário com base no ID do usuário
    public void updateUser(User user) throws SQLException {
        
        String query = "UPDATE usuario SET NOME = ?, EMAIL = ?, SENHA = ?, ALTERNATIVA = ?, RESPOSTA = ? WHERE ID_USUARIO = ?";
        
       
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getQuestion());
            preparedStatement.setString(5, user.getAnswer());
            preparedStatement.setInt(6, user.getId());

            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
