package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ContactPageController {

    @FXML
    private Button contactPage_Delete;

    @FXML
    private TextField contactPage_Email;

    @FXML
    private TableColumn<?, ?> contactPage_EmailColumn;

    @FXML
    private TableColumn<?, ?> contactPage_IDColumn;

    @FXML
    private TextField contactPage_Name;

    @FXML
    private TableColumn<?, ?> contactPage_NameColumn;

    @FXML
    private TextField contactPage_Phone;

    @FXML
    private TableColumn<?, ?> contactPage_PhoneColumn;

    @FXML
    private Button contactPage_SaveButton;

    @FXML
    private TableView<?> contactPage_Table;

    @FXML
    private Button contactPage_Update;

    @FXML
    void addContact(ActionEvent event) {

    }

    @FXML
    void close(MouseEvent event) {

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
