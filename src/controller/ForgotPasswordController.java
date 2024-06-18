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

    // Lista de perguntas de segurança
    @FXML
    private ObservableList<String> questionBoxList = FXCollections.observableArrayList(
            "Qual sua comida favorita?",
            "Qual seu jogo favorito?",
            "Qual seu herói favorito?"
    );

    // Método para buscar e redefinir a senha
    public void searchPassword() {
        UserDAO userDao = new UserDAO();
        AlertMessage alert = new AlertMessage();

        try {
            String email = forgotPswEmail.getText().trim();
            String password = forgotPswPassword.getText().trim();
            String selectedQuestion = forgotPswQuestion.getValue() != null ? forgotPswQuestion.getValue().trim() : "";
            String answer = forgotPswAnswer.getText().trim();

            // Verifica se o email está vazio
            if (email.isEmpty()) {
                alert.errorMessage("Digite o email!");
                return;
            } else if (selectedQuestion.isEmpty()) {
                alert.errorMessage("Selecione uma pergunta de segurança!");
                return;
            }

            // Obtém o usuário pelo email
            User user = userDao.getUserByEmail(email);

            // Verifica se o usuário existe
            if (user != null) {
                String dbSelectedQuestion = user.getQuestion();
                String dbAnswer = user.getAnswer();

                // Verifica a resposta à pergunta de segurança
                if (dbAnswer != null && dbAnswer.equals(answer)) {
                    // Criptografa a nova senha
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                    // Atualiza a senha no banco de dados
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
        	// Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
            alert.errorMessage("Erro ao acessar o banco de dados: " + e.getMessage());
        } catch (Exception e) {
        	// Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
            alert.errorMessage("Ocorreu um erro ao tentar atualizar a senha." + e.getMessage());
        }
    }

    // Método para limpar os campos de texto
    public void clearFields() {
        forgotPswEmail.setText("");
        forgotPswQuestion.getSelectionModel().clearSelection();
        forgotPswAnswer.setText("");
        forgotPswPassword.setText("");
    }

    // Método para retornar à tela de login
    public void home(ActionEvent event) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // Método initialize para inicializar a lista de perguntas e selecionar uma pergunta padrão
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        forgotPswQuestion.setValue("Selecione uma pergunta");
        forgotPswQuestion.setItems(questionBoxList);
    }
}
