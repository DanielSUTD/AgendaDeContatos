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

public class ContactPageController {

    @FXML
    private Button contactPageDeleteButton;

    @FXML
    private TextField contactPageEmail;

    @FXML
    private TableColumn<Contact, String> contactPageEmailColumn;

    @FXML
    private TableColumn<Contact, Integer> contactPageIDColumn;

    @FXML
    private TextField contactPageName;

    @FXML
    private TableColumn<Contact, String> contactPageNameColumn;

    @FXML
    private TextField contactPagePhone;

    @FXML
    private TableColumn<Contact, String> contactPagePhoneColumn;

    @FXML
    private Button contactPageSaveButton;

    @FXML
    private Button contactPageUpdateButton;

    @FXML
    private TableView<Contact> contactPageTable;

    private ObservableList<Contact> contacts = FXCollections.observableArrayList();
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
            if (!checkRequiredFields()) {
                alert.errorMessage("Preencha os campos necessários!");
                return;
            }

            String name = contactPageName.getText().trim();
            String phone = contactPagePhone.getText().trim();
            String email = contactPageEmail.getText().trim();

            ContactDAO contactDAO = new ContactDAO();
            Contact contact = new Contact(name, phone, email, userID);
            contactDAO.addContact(contact);
            alert.successMessage("Contato adicionado com sucesso!");

            clearFields();
            table();
        } catch (SQLException e) {
            handleException(alert, e, "Ocorreu um erro ao adicionar o contato.");
        } catch (Exception e) {
            handleException(alert, e, "Erro inesperado ao adicionar o contato.");
        }
    }

    // Método para verificar se os campos obrigatórios estão preenchidos
    private boolean checkRequiredFields() {
        return !contactPageName.getText().trim().isEmpty() && !contactPagePhone.getText().trim().isEmpty();
    }

    // Método para carregar a tabela de contatos
    private void table() {
        contactPageIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactPageNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactPagePhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        contactPageEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        contactPageTable.setRowFactory(tv -> {
            TableRow<Contact> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Contact contact = row.getItem();
                    populateFields(contact);
                }
            });
            return row;
        });

        ContactDAO contactDAO = new ContactDAO();
        try {
            contacts = contactDAO.getContacts(userID);
            contactPageTable.setItems(contacts);
        } catch (SQLException e) {
            handleException(new AlertMessage(), e, "Erro ao carregar contatos: ");
        }
    }

    // Método para preencher os campos de texto com as informações do contato selecionado
    private void populateFields(Contact contact) {
        contactPageName.setText(contact.getName());
        contactPagePhone.setText(contact.getPhone());
        contactPageEmail.setText(contact.getEmail());
    }

    // Método para limpar os campos de texto
    private void clearFields() {
        contactPageName.setText("");
        contactPagePhone.setText("");
        contactPageEmail.setText("");
    }

    // Método para atualizar um contato
    @FXML
    public void updateContact(ActionEvent event) {
        AlertMessage alert = new AlertMessage();
        try {
            Contact selectedContact = contactPageTable.getSelectionModel().getSelectedItem();
            if (selectedContact == null) {
                alert.errorMessage("Selecione um contato para atualizar.");
                return;
            }

            if (!checkRequiredFields()) {
                alert.errorMessage("Preencha os campos necessários!");
                return;
            }

            String name = contactPageName.getText().trim();
            String phone = contactPagePhone.getText().trim();
            String email = contactPageEmail.getText().trim();

            selectedContact.setName(name);
            selectedContact.setPhone(phone);
            selectedContact.setEmail(email);

            ContactDAO contactDAO = new ContactDAO();
            contactDAO.updateContact(selectedContact);

            alert.successMessage("Contato atualizado com sucesso!");
            clearFields();
            table();
        } catch (SQLException e) {
            handleException(alert, e, "Ocorreu um erro ao atualizar o contato.");
        }
    }

    // Método para deletar um contato
    @FXML
    public void deleteContact(ActionEvent event) {
        AlertMessage alert = new AlertMessage();
        try {
            Contact selectedContact = contactPageTable.getSelectionModel().getSelectedItem();
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
            handleException(alert, e, "Ocorreu um erro ao deletar o contato.");
        }
    }

    // Método para fechar a tela e redirecionar para a tela de login
    @FXML
    public void close(MouseEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
            Scene scene = new Scene(view);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para navegar para a página do usuário
    @FXML
    public void clickedUser(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserPage.fxml"));
        Parent view = loader.load();

        UserPageController userPageController = loader.getController();
        userPageController.setUserId(userID);

        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // Método para lidar com exceções e mostrar uma mensagem de erro ao usuário
    private void handleException(AlertMessage alert, Exception e, String message) {
        e.printStackTrace();
        alert.errorMessage(message + e.getMessage());
    }
}
