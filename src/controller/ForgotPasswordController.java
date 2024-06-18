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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import javafx.scene.Node;
import util.AlertMessage;
import org.mindrot.jbcrypt.BCrypt;

public class ForgotPasswordController implements Initializable {

    @FXML
    private TextField forgotPswAnswer;

    @FXML
    private TextField forgotPswEmail;

    @FXML
    private Button forgotPswHomeButton;

    @FXML
    private PasswordField forgotPswPassword;

    @FXML
    private Button forgotPswProceedButton;

    @FXML
    private ComboBox<String> forgotPswQuestion;

    @FXML
    private ObservableList<String> questionBoxList = FXCollections.observableArrayList(
            "Qual sua comida favorita?",
            "Qual seu jogo favorito?",
            "Qual seu herói favorito?"
    );

    public void searchPassword() {
        UserDAO userDao = new UserDAO();
        AlertMessage alert = new AlertMessage();

        try {
            String email = forgotPswEmail.getText().trim();
            String password = forgotPswPassword.getText().trim();
            String selectedQuestion = forgotPswQuestion.getValue() != null ? forgotPswQuestion.getValue().trim() : "";
            String answer = forgotPswAnswer.getText().trim();

            if (validateFields(alert, email, selectedQuestion, answer, password)) {
                processPasswordReset(userDao, alert, email, selectedQuestion, answer, password);
            }
        } catch (SQLException e) {
            handleException(alert, e, "Erro ao acessar o banco de dados: ");
        } catch (Exception e) {
            handleException(alert, e, "Ocorreu um erro ao tentar atualizar a senha: ");
        }
    }

    private boolean validateFields(AlertMessage alert, String email, String selectedQuestion, String answer, String password) {
        if (email.isEmpty()) {
            alert.errorMessage("Digite o email!");
            return false;
        }
        if (selectedQuestion.isEmpty() || "Selecione uma pergunta".equals(selectedQuestion)) {
            alert.errorMessage("Selecione uma pergunta de segurança!");
            return false;
        }
        if (answer.isEmpty()) {
            alert.errorMessage("Digite a resposta para a pergunta de segurança!");
            return false;
        }
        if (password.isEmpty()) {
            alert.errorMessage("Digite a nova senha!");
            return false;
        }
        return true;
    }

    private void processPasswordReset(UserDAO userDao, AlertMessage alert, String email, String selectedQuestion, String answer, String password) throws SQLException {
        User user = userDao.getUserByEmail(email);

        if (user != null && user.getQuestion().equals(selectedQuestion) && user.getAnswer().equals(answer)) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            userDao.updatePassword(email, hashedPassword);
            alert.successMessage("Senha atualizada!");
            clearFields();
        } else {
        	String message = user == null ? "Usuário não encontrado!" : "Resposta errada!";
            alert.errorMessage(message);
        }
    }

    private void handleException(AlertMessage alert, Exception e, String message) {
        e.printStackTrace();
        alert.errorMessage(message + e.getMessage());
    }

    public void clearFields() {
        forgotPswEmail.setText("");
        forgotPswQuestion.getSelectionModel().clearSelection();
        forgotPswAnswer.setText("");
        forgotPswPassword.setText("");
    }

    public void homeScreen(ActionEvent event) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        forgotPswQuestion.setValue("Selecione uma pergunta");
        forgotPswQuestion.setItems(questionBoxList);
    }
}
