package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import dao.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.AlertMessage;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

public class SignUpScreenController implements Initializable {

    @FXML
    private TextField signUpAnswer;

    @FXML
    private PasswordField signUpConfirmPassword;

    @FXML
    private TextField signUpEmail;

    @FXML
    private Button signUpEnterButton;

    @FXML
    private TextField signUpName;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private Button signUpRegisterButton;

    @FXML
    private ComboBox<String> signUpSelectQuestion;

    @FXML
    private ObservableList<String> questionBoxList = FXCollections.observableArrayList(
        "Qual sua comida favorita?", 
        "Qual seu jogo favorito?", 
        "Qual seu herói favorito?"
    );

    public void signUp(ActionEvent event) throws IOException {
        AlertMessage alert = new AlertMessage();
        UserDAO userDao = new UserDAO();

        try {
            String name = signUpName.getText().trim();
            String email = signUpEmail.getText().trim();
            String password = signUpPassword.getText().trim();
            String confirmPassword = signUpConfirmPassword.getText().trim();
            String question = signUpSelectQuestion.getValue();
            String answer = signUpAnswer.getText().trim();

            if (!validateFields(alert, name, email, password, confirmPassword, question, answer)) {
            	return;
            }

            if (isEmailInUse(alert, userDao, email)) {
            	return;
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            User newUser = new User(name, email, hashedPassword, question, answer);
            userDao.insertUser(newUser);

            alert.successMessage("Conta cadastrada!");
            clearFields();

        } catch (SQLException e) {
            handleException(alert, e, "Erro ao cadastrar conta: ");
        } catch (Exception e) {
            handleException(alert, e, "Ocorreu um erro ao tentar cadastrar: ");
        }
    }

    private boolean validateFields(AlertMessage alert, String name, String email, String password, 
    String confirmPassword, String question, String answer) {
        boolean checkFields = checkEmptyFields(alert, name, email, password, confirmPassword, question, answer);
        boolean checkPassword = checkPasswordLength(alert, password);
        boolean checkPasswordMatch = checkPasswordMatch(alert, password, confirmPassword);
        
        return checkFields && checkPassword && checkPasswordMatch;
    }

    private boolean checkEmptyFields(AlertMessage alert, String name, String email, String password, 
    String confirmPassword, String question, String answer) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || 
            confirmPassword.isEmpty() || "Selecione uma pergunta".equals(question) || answer.isEmpty()) {
            alert.errorMessage("Preencha todos os campos!");
            return false;
        }
        return true;
    }

    private boolean checkPasswordLength(AlertMessage alert, String password) {
        if (password.length() < 8) {
            alert.errorMessage("A senha deve ter pelo menos 8 caracteres!");
            return false;
        }
        return true;
    }

    private boolean checkPasswordMatch(AlertMessage alert, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            alert.errorMessage("Coloque senhas iguais nos campos!");
            return false;
        }
        return true;
    }

    private boolean isEmailInUse(AlertMessage alert, UserDAO userDao, String email) throws SQLException {
        User user = userDao.getUserByEmail(email);
        if (user != null) {
            alert.errorMessage("O email já está em uso!");
            return true;
        }
        return false;
    }

    private void handleException(AlertMessage alert, Exception e, String message) {
        e.printStackTrace();
        alert.errorMessage(message + e.getMessage());
    }

    public void clearFields() {
        signUpName.setText("");
        signUpEmail.setText("");
        signUpPassword.setText("");
        signUpConfirmPassword.setText("");
        signUpSelectQuestion.getSelectionModel().clearSelection();
        signUpAnswer.setText("");
    }

    public void loginScreen(ActionEvent event) throws IOException {
        loadScreen(event, "/view/LoginScreen.fxml");
    }

    private void loadScreen(ActionEvent event, String fxmlPath) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        signUpSelectQuestion.setValue("Selecione uma pergunta");
        signUpSelectQuestion.setItems(questionBoxList);
    }
}
