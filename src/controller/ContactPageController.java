package controller;

import java.io.IOException;
import java.sql.SQLException;
import dao.ContactDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import model.Contact;
import util.AlertMessage;


public class ContactPageController{

    @FXML
    private Button contactPage_Delete;

    @FXML
    private TextField contactPage_Email;

    @FXML
    private TableColumn<Contact, String> contactPage_EmailColumn;

    @FXML
    private TableColumn<Contact, Integer> contactPage_IDColumn;

    @FXML
    private TextField contactPage_Name;

    @FXML
    private TableColumn<Contact, String> contactPage_NameColumn;

    @FXML
    private TextField contactPage_Phone;

    @FXML
    private TableColumn<Contact, String> contactPage_PhoneColumn;

    @FXML
    private Button contactPage_SaveButton;

    @FXML
    private TableView<Contact> contactPage_Table;

    @FXML
    private Button contactPage_Update;
    
    //List para contatos
    private ObservableList<Contact> contacts = FXCollections.observableArrayList();
    
 // ID do usuário que está usando a aplicação
    private int userID;

 // Método para definir o ID do usuário e carregar a tabela de contatos
    public void setUserId(int userID) {
        this.userID = userID;
        table();
    }


    // Método para adicionar um novo contato
	@FXML
    public void addContact(ActionEvent event) {
    	 AlertMessage alert = new AlertMessage();  

    	    try {
    	        String name = contactPage_Name.getText().trim();
    	        String phone = contactPage_Phone.getText().trim();
    	        String email = contactPage_Email.getText().trim();

    	        // Verifica se os campos obrigatórios estão preenchidos
    	        if (name.isEmpty() || phone.isEmpty()) {
    	            alert.errorMessage("Preencha os campos necessários!");
    	            return;
    	        }
    	        
    	        //Instancia ContactDAO
    	        ContactDAO contactDAO = new ContactDAO();
    	        //Cria novo objeto Contact
    	        Contact contact = new Contact(name, phone, email, userID);
    	        

    	        //Adiciona contato no banco de dados!
    	        contactDAO.addContact(contact);  
    	        alert.successMessage("Contato adicionado com sucesso!");
    	        //Limpa caixa de texto
    	        clearFields();
    	        //Atualiza tabela
    	        table();
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	        alert.errorMessage("Ocorreu um erro ao adicionar o contato.");
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        alert.errorMessage("Erro inesperado ao adicionar o contato.");
    	    }
    }
    
	//Limpando campos de texto!
    private void clearFields() {
        contactPage_Name.setText("");
        contactPage_Phone.setText("");
        contactPage_Email.setText("");
    }
    
    // Método para carregar a tabela de contatos
    public void table() {
    	// Define as propriedades das colunas
    	 contactPage_IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
         contactPage_NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
         contactPage_PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
         contactPage_EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

         // Configura um evento de clique nas linhas da tabela
         contactPage_Table.setRowFactory(tv -> {
             TableRow<Contact> row = new TableRow<>();
             row.setOnMouseClicked(event -> {
                 if (event.getClickCount() == 1 && (!row.isEmpty())) {
                     Contact contact = row.getItem();
                     contactPage_Name.setText(contact.getName());
                     contactPage_Phone.setText(contact.getPhone());
                     contactPage_Email.setText(contact.getEmail());
                 }
             });
             return row;
         });

         
         ContactDAO contactDAO = new ContactDAO();
         try {
             contacts = contactDAO.getContacts(userID);
             contactPage_Table.setItems(contacts);
         } catch (SQLException e) {
             e.printStackTrace();
             new AlertMessage().errorMessage("Erro ao carregar contatos: " + e.getMessage());
         }
     }

    //Método para fechar a tela
    @FXML
    public void close(MouseEvent event) {
    	 Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
         window.close();
    }

    @FXML
    public void updateContact(ActionEvent event) {
        AlertMessage alert = new AlertMessage();

        try {
        	//Selecionando o contato
            Contact selectedContact = contactPage_Table.getSelectionModel().getSelectedItem();
            if (selectedContact == null) {
                alert.errorMessage("Selecione um contato para atualizar.");
                return;
            }

            String name = contactPage_Name.getText().trim();
            String phone = contactPage_Phone.getText().trim();
            String email = contactPage_Email.getText().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                alert.errorMessage("Preencha os campos necessários!");
                return;
            }

            //Selecionando o nome, telefone e email
            selectedContact.setName(name);
            selectedContact.setPhone(phone);
            selectedContact.setEmail(email);

            //Atualizando o contato
            ContactDAO contactDAO = new ContactDAO();
            contactDAO.updateContact(selectedContact);

            alert.successMessage("Contato atualizado com sucesso!");
            //Limpando caixas de texto
            clearFields();
            //Atualizando tabela
            table();
        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Ocorreu um erro ao atualizar o contato.");
        }
    }

    // Método para deletar um contato
    @FXML
    public void deleteContact(ActionEvent event) {
        AlertMessage alert = new AlertMessage();

        try {
        	//Selecionando um contato
            Contact selectedContact = contactPage_Table.getSelectionModel().getSelectedItem();
            if (selectedContact == null) {
                alert.errorMessage("Selecione um contato para deletar.");
                return;
            }
            
            //Através do ID eu deleto o contato!
            int contactId = selectedContact.getId();
            ContactDAO contactDAO = new ContactDAO();
            contactDAO.deleteContact(contactId);

            alert.successMessage("Contato deletado com sucesso!");
            //Limpando caixas de texto
            clearFields();
            //Atualizando tabela
            table();
        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Ocorreu um erro ao deletar o contato.");
        }
    }

 // Método para navegar para a página do usuário
    @FXML
    public void clickedUser(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserPage.fxml"));
        Parent view = loader.load();
        
        // Passando o ID do usuário para o UserPageController
        UserPageController userPageController = loader.getController();
        userPageController.setUserId(userID);
        
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}