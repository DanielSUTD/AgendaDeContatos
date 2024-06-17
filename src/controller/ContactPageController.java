package controller;

import java.sql.SQLException;
import dao.ContactDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
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
    
    private ObservableList<Contact> contacts = FXCollections.observableArrayList();
    
    private int userID;

    public void setUserId(int userID) {
        this.userID = userID;
        table();
    }


	@FXML
    public void addContact(ActionEvent event) {
    	 AlertMessage alert = new AlertMessage();  

    	    try {
    	        String name = contactPage_Name.getText().trim();
    	        String phone = contactPage_Phone.getText().trim();
    	        String email = contactPage_Email.getText().trim();

    	        if (name.isEmpty() || phone.isEmpty()) {
    	            alert.errorMessage("Preencha os campos necessários!");
    	            return;
    	        }
    	        
    	        ContactDAO contactDAO = new ContactDAO();
    	        Contact contact = new Contact(name, phone, email, userID);
    	        

    	        contactDAO.addContact(contact);  
    	        alert.successMessage("Contato adicionado com sucesso!");
    	        clearFields();
    	        table();
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	        alert.errorMessage("Ocorreu um erro ao adicionar o contato.");
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        alert.errorMessage("Erro inesperado ao adicionar o contato.");
    	    }
    }
    
    private void clearFields() {
        contactPage_Name.setText("");
        contactPage_Phone.setText("");
        contactPage_Email.setText("");
    }
    
    public void table() {
    	 contactPage_IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
         contactPage_NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
         contactPage_PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
         contactPage_EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

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

    @FXML
    public void close(MouseEvent event) {
    	 Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
         window.close();
    }

    @FXML
    public void updateContact(ActionEvent event) {
        AlertMessage alert = new AlertMessage();

        try {
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

            selectedContact.setName(name);
            selectedContact.setPhone(phone);
            selectedContact.setEmail(email);

            ContactDAO contactDAO = new ContactDAO();
            contactDAO.updateContact(selectedContact);

            alert.successMessage("Contato atualizado com sucesso!");
            clearFields();
            table();
        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Ocorreu um erro ao atualizar o contato.");
        }
    }

    @FXML
    public void deleteContact(ActionEvent event) {
        AlertMessage alert = new AlertMessage();

        try {
            Contact selectedContact = contactPage_Table.getSelectionModel().getSelectedItem();
            if (selectedContact == null) {
                alert.errorMessage("Selecione um contato para deletar.");
                return;
            }
            
            int contactId = selectedContact.getId();
            ContactDAO contactDAO = new ContactDAO();
            contactDAO.deleteContact(contactId);

            alert.successMessage("Contato deletado com sucesso!");
            clearFields();
            table();
        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Ocorreu um erro ao deletar o contato.");
        }
    }

    @FXML
    public void user(MouseEvent event) {

    }
}