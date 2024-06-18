package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

public class ContactDAO {

    // Método para inserir um novo contato no banco de dados
    public void addContact(Contact contact) throws SQLException {
        // Consulta SQL para inserir um novo contato
        String query = "INSERT INTO contato (NOME, TELEFONE, EMAIL, ID_USUARIO) VALUES (?, ?, ?, ?)";
        
        // O bloco try-with-resources garante que a conexão e o PreparedStatement sejam fechados corretamente
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            // Definindo os parâmetros (?) na consulta SQL com os dados do contato
            preparedStatement.setString(1, contact.getName());
            preparedStatement.setString(2, contact.getPhone());
            preparedStatement.setString(3, contact.getEmail());
            preparedStatement.setInt(4, contact.getUserID());
            
            // Executando a query de inserção no banco de dados
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
        }
    }
    
    // Método para selecionar vários contatos com base no ID do usuário
    public ObservableList<Contact> getContacts(int userID) throws SQLException {
        // Consulta SQL para selecionar os contatos do usuário
        String query = "SELECT ID_CONTATO, NOME, TELEFONE, EMAIL FROM contato WHERE ID_USUARIO = ?";
        // Lista para armazenar os contatos
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        
        // O bloco try-with-resources garante que a conexão e o PreparedStatement sejam fechados corretamente
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            // Definindo o parâmetro (?) na consulta SQL com o ID do usuário
            preparedStatement.setInt(1, userID);
            
            // Executando a query de seleção no banco de dados
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // Processando os resultados da consulta
            while (resultSet.next()) {
                // Criando um novo objeto Contact e definindo seus atributos com os valores do ResultSet
                Contact contact = new Contact();
                contact.setId(resultSet.getInt("ID_CONTATO")); 
                contact.setName(resultSet.getString("NOME"));
                contact.setPhone(resultSet.getString("TELEFONE"));
                contact.setEmail(resultSet.getString("EMAIL"));
                // Adicionando o contato à lista
                contacts.add(contact);
            }
        } catch (SQLException e) {
            // Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
        }
        // Retornando a lista de contatos
        return contacts;
    }
    
    // Método para atualizar todos os dados de um contato com base no ID do contato
    public void updateContact(Contact contact) throws SQLException {
        // Consulta SQL para atualizar os dados do contato
        String query = "UPDATE contato SET NOME = ?, TELEFONE = ?, EMAIL = ? WHERE ID_CONTATO = ?";
        
        // O bloco try-with-resources garante que a conexão e o PreparedStatement sejam fechados corretamente
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            // Definindo os parâmetros (?) na consulta SQL com os novos dados do contato
            preparedStatement.setString(1, contact.getName());
            preparedStatement.setString(2, contact.getPhone());
            preparedStatement.setString(3, contact.getEmail());
            preparedStatement.setInt(4, contact.getId());
            
            // Executando a query de atualização no banco de dados
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
        }
    }
    
    // Método para deletar um contato com base no ID do contato
    public void deleteContact(int contactId) throws SQLException {
        // Consulta SQL para deletar o contato
        String query = "DELETE FROM contato WHERE ID_CONTATO = ?";
        
        // O bloco try-with-resources garante que a conexão e o PreparedStatement sejam fechados corretamente
        try (Connection con = new ConnectDB().getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            
            // Definindo o parâmetro (?) na consulta SQL com o ID do contato
            preparedStatement.setInt(1, contactId);
            
            // Executando a query de deleção no banco de dados
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
        }
    }
}
