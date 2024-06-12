package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import model.Contact;

public class ContactPageController {

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

    @FXML
    void addContact(ActionEvent event) {

    }

    @FXML
    void close(MouseEvent event) {
    	 Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
         window.close();
    }

    @FXML
    void deleteContact(ActionEvent event) {

    }

    @FXML
    void updateContact(ActionEvent event) {

    }

    @FXML
    void user(MouseEvent event) {

    }

}