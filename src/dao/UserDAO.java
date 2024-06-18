package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

public class UserDAO {
	
	// Método select para selecionar o usuário através de seu email
	public User getUserByEmail(String email) throws SQLException {
	    // Objeto User para armazenar o resultado
	    User user = null;

	    // Consulta SQL para selecionar o usuário pelo email
	    String query = "SELECT ID_USUARIO, NOME, EMAIL, SENHA, ALTERNATIVA, RESPOSTA FROM usuario WHERE EMAIL=?";

	    // O bloco try-with-resources garante que a conexão e o PreparedStatement sejam fechados corretamente
	    try (Connection con = new ConnectDB().getConnection();
	         PreparedStatement preparedStatement = con.prepareStatement(query)) {

	        // Definindo o parâmetro (?) na consulta SQL com o email fornecido
	        preparedStatement.setString(1, email);

	        // Executando a consulta SQL
	        ResultSet resultSet = preparedStatement.executeQuery();

	        // Verificando se há um registro no resultado da consulta
	        if (resultSet.next()) {
	            // Obtendo os dados do usuário a partir do ResultSet
	            int userId = resultSet.getInt("ID_USUARIO");
	            String name = resultSet.getString("NOME");
	            String userEmail = resultSet.getString("EMAIL");
	            String hashedPassword = resultSet.getString("SENHA");
	            String question = resultSet.getString("ALTERNATIVA");
	            String answer = resultSet.getString("RESPOSTA");

	            // Criando um objeto User com os dados obtidos
	            user = new User(userId, name, userEmail, hashedPassword, question, answer);
	        }

	    } catch (SQLException e) {
	        // Imprimindo o stack trace em caso de exceção
	        e.printStackTrace();
	    }

	    // Retornando o objeto User (ou null se não encontrado)
	    return user;
	}
	
	//Método Select para selecionar o usuário através do seu ID!
    public User getUserById(int userID) throws SQLException {
    	// Objeto User para armazenar o resultado
        User user = null;
        
       //Consulta SQL para selecionar o usuário pelo email
        String query = "SELECT ID_USUARIO, NOME, EMAIL, SENHA, ALTERNATIVA, RESPOSTA FROM usuario WHERE ID_USUARIO = ?";

     // O bloco try-with-resources garante que a conexão e o PreparedStatement sejam fechados corretamente
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
        	
        	// Definindo o parâmetro (?) na consulta SQL com o ID fornecido
            preparedStatement.setInt(1, userID);
            
            //Executando a consulta SQL
            ResultSet resultSet = preparedStatement.executeQuery();
            
         // Verificando se há um registro no resultado da consulta
            if (resultSet.next()) {
                int id = resultSet.getInt("ID_USUARIO");
                String name = resultSet.getString("NOME");
                String email = resultSet.getString("EMAIL");
                String password = resultSet.getString("SENHA");
                String question = resultSet.getString("ALTERNATIVA");
                String answer = resultSet.getString("RESPOSTA");
                
             // Criando um objeto User com os dados obtidos
                user = new User(id, name, email, password, question, answer);
            }
        } catch (SQLException e) {
        	// Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
        }
        //Retorna o usuário!
        return user;
    }
	
 // Método para inserir um novo usuário no banco de dados
    public void insertUser(User user) throws SQLException {
        // Consulta SQL para inserir um novo usuário
        String query = "INSERT INTO usuario (NOME, EMAIL, SENHA, ALTERNATIVA, RESPOSTA) VALUES (?, ?, ?, ?, ?)";
        
        // O bloco try-with-resources garante que a conexão e o PreparedStatement sejam fechados corretamente
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            // Definindo os parâmetros (?) na consulta SQL com os dados do usuário
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getQuestion());
            preparedStatement.setString(5, user.getAnswer());

            // Executando a query de inserção no banco de dados
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
        }
    }

    // Método para atualizar a senha de um usuário com base no email
    public void updatePassword(String email, String hashedPassword) throws SQLException {
        // Consulta SQL para atualizar a senha do usuário com base no email
        String query = "UPDATE usuario SET SENHA=? WHERE EMAIL=?";
        
        // O bloco try-with-resources garante que a conexão e o PreparedStatement sejam fechados corretamente
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            // Definindo os parâmetros (?) na consulta SQL com a nova senha e o email
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, email);

            // Executando a query de atualização no banco de dados
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
        }
    }

    // Método para atualizar todos os dados de um usuário com base no ID do usuário
    public void updateUser(User user) throws SQLException {
        // Consulta SQL para atualizar os dados do usuário com base no ID do usuário
        String query = "UPDATE usuario SET NOME = ?, EMAIL = ?, SENHA = ?, ALTERNATIVA = ?, RESPOSTA = ? WHERE ID_USUARIO = ?";
        
        // O bloco try-with-resources garante que a conexão e o PreparedStatement sejam fechados corretamente
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            // Definindo os parâmetros (?) na consulta SQL com os novos dados do usuário
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getQuestion());
            preparedStatement.setString(5, user.getAnswer());
            preparedStatement.setInt(6, user.getId());

            // Executando a query de atualização no banco de dados
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
        }
    }
}
