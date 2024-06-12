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
    private TextField forgotPsw_Answer;

    @FXML
    private TextField forgotPsw_Email;

    @FXML
    private Button forgotPsw_HomeButton;

    @FXML
    private PasswordField forgotPsw_Password;

    @FXML
    private Button forgotPsw_ProceedButton;

    @FXML
    private ComboBox<String> forgotPsw_Question;

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
            String email = forgotPsw_Email.getText().trim();
            String password = forgotPsw_Password.getText().trim();
            String selectedQuestion = forgotPsw_Question.getValue() != null ? forgotPsw_Question.getValue().trim() : "";
            String answer = forgotPsw_Answer.getText().trim();

            if (email.isEmpty()) {
                alert.errorMessage("Digite o email!");
                return;
            } else if (selectedQuestion.isEmpty()) {
                alert.errorMessage("Selecione uma pergunta de segurança!");
                return;
            }

            User user = userDao.getUserByEmail(email);

            if (user != null) {
                String dbSelectedQuestion = user.getQuestion();
                String dbAnswer = user.getAnswer();

                // Verifica Resposta
                if (dbAnswer != null && dbAnswer.equals(answer)) {
                    // Criptografando nova senha
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                    // Atualizando a senha
                    userDao.updatePassword(email, hashedPassword);
                    alert.successMessage("Senha atualizada!");
                } else {
                    alert.errorMessage("Resposta errada!");
                }
            } else {
                alert.errorMessage("Usuário não encontrado!");
            }

            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Erro ao acessar o banco de dados: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Ocorreu um erro ao tentar atualizar a senha." + e.getMessage());
        }
    }


    public void clearFields() {
        forgotPsw_Email.setText("");
        forgotPsw_Question.getSelectionModel().clearSelection();
        forgotPsw_Answer.setText("");
        forgotPsw_Password.setText("");
    }

    public void home(ActionEvent event) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        forgotPsw_Question.setValue("Selecione uma pergunta");
        forgotPsw_Question.setItems(questionBoxList);
    }
}
