package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.net.URL;
import java.util.ResourceBundle;
import dao.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import model.User;
import util.AlertMessage;
import org.mindrot.jbcrypt.BCrypt;

public class UserPageController implements Initializable {

    @FXML
    private TextField userPageAnswer;

    @FXML
    private Button userPageBackButton;

    @FXML
    private TextField userPageEmail;

    @FXML
    private TextField userPageName;

    @FXML
    private PasswordField userPagePassword;

    @FXML
    private ComboBox<String> userPageSelectQuestion;

    @FXML
    private Button userPageUpdateButton;

    private ObservableList<String> questionBoxList = FXCollections.observableArrayList(
            "Qual sua comida favorita?",
            "Qual seu jogo favorito?",
            "Qual seu herói favorito?"
    );

    private int userID;

    // Método para definir o ID do usuário e carregar seus dados
    public void setUserId(int userID) {
        this.userID = userID;
        uploadUserInformation();
    }

    // Método privado para carregar os dados do usuário
    private void uploadUserInformation() {
        UserDAO userDAO = new UserDAO();
        try {
            User user = userDAO.getUserById(userID);
            if (user != null) {
                userPageName.setText(user.getName());
                userPageEmail.setText(user.getEmail());
                userPageSelectQuestion.setValue(user.getQuestion());
                userPageAnswer.setText(user.getAnswer());
            }
        } catch (SQLException e) {
            handleException(e, "Erro ao carregar os dados do usuário.");
        }
    }

    @FXML
    public void updateUserInformation(ActionEvent event) {
        AlertMessage alert = new AlertMessage();
        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(userID);
            if (user == null) {
                alert.errorMessage("Usuário não encontrado.");
                return;
            }

            updateFields(user);

            // Atualizar o usuário no banco de dados
            userDAO.updateUser(user);
            alert.successMessage("Informações do usuário atualizadas com sucesso!");
        } catch (SQLException e) {
            handleException(e, "Ocorreu um erro ao atualizar as informações do usuário.");
        } catch (Exception e) {
            handleException(e, "Erro inesperado ao atualizar as informações do usuário.");
        }
    }

    private void updateFields(User user) {
        if (!userPageName.getText().trim().isEmpty()) {
            user.setName(userPageName.getText().trim());
        }
        
        if (!userPageEmail.getText().trim().isEmpty()) {
            user.setEmail(userPageEmail.getText().trim());
        }
        
        if (!userPagePassword.getText().trim().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(userPagePassword.getText().trim(), BCrypt.gensalt());
            user.setPassword(hashedPassword);
        }
        
        if (userPageSelectQuestion.getValue() != null && !userPageSelectQuestion.getValue().trim().isEmpty()) {
            user.setQuestion(userPageSelectQuestion.getValue().trim());
        }
        
        if (!userPageAnswer.getText().trim().isEmpty()) {
            user.setAnswer(userPageAnswer.getText().trim());
        }
        
    }

    // Método para voltar à página anterior
    @FXML
    public void clickReturn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ContactPage.fxml"));
        Parent view = loader.load();

        ContactPageController contactPageController = loader.getController();
        contactPageController.setUserId(userID);

        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // Método para lidar com exceções e mostrar uma mensagem de erro ao usuário
    private void handleException(Exception e, String message) {
        e.printStackTrace();
        new AlertMessage().errorMessage(message + " " + e.getMessage());
    }
    
    // Método initialize para inicializar a lista de perguntas
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userPageSelectQuestion.setItems(questionBoxList);
    }
}
