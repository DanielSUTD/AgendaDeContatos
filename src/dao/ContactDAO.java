package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

public class ContactDAO {
	
	public void addContact(Contact contact) throws SQLException {
        String query = "INSERT INTO contato (NOME, TELEFONE, EMAIL, ID_USUARIO) VALUES (?, ?, ?, ?)";
        
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            preparedStatement.setString(1, contact.getName());
            preparedStatement.setString(2, contact.getPhone());
            preparedStatement.setString(3, contact.getEmail());
            preparedStatement.setInt(4, contact.getUserID());
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erro ao cadastrar contato: " + e.getMessage(), e);
        }
    }
	
	public ObservableList<Contact> getContacts(int userID) throws SQLException {
        String query = "SELECT ID_CONTATO, NOME, TELEFONE, EMAIL FROM contato WHERE ID_USUARIO = ?";
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                Contact contact = new Contact();
                contact.setId(resultSet.getInt("ID_CONTATO")); 
                contact.setName(resultSet.getString("NOME"));
                contact.setPhone(resultSet.getString("TELEFONE"));
                contact.setEmail(resultSet.getString("EMAIL"));
                contacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erro ao buscar contatos: " + e.getMessage(), e);
        }
        
        return contacts;
    }
	
	 public void updateContact(Contact contact) throws SQLException {
	        String query = "UPDATE contato SET NOME = ?, TELEFONE = ?, EMAIL = ? WHERE ID_CONTATO = ?";
	        
	        try (Connection con = new ConnectDB().getConnection();
	             PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            
	            preparedStatement.setString(1, contact.getName());
	            preparedStatement.setString(2, contact.getPhone());
	            preparedStatement.setString(3, contact.getEmail());
	            preparedStatement.setInt(4, contact.getId());
	            
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

}
