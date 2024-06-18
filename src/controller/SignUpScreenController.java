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

    // Lista de perguntas de segurança
    @FXML
    private ObservableList<String> questionBoxList = FXCollections.observableArrayList("Qual sua comida favorita?", "Qual seu jogo favorito?", "Qual seu herói favorito?");

 // Método para registrar um novo usuário
    public void signUp(ActionEvent event) throws IOException {
    	//Instanciando a mensagem de alerta
        AlertMessage alert = new AlertMessage();
        //Instanciando o Data Acess Object do Usuário
        UserDAO userDao = new UserDAO();

        try {
        	//Colocando as palavras nas caixas de texto no objeto String
            String name = signUpName.getText().trim();
            String email = signUpEmail.getText().trim();
            String password = signUpPassword.getText().trim();
            String confirmPassword = signUpConfirmPassword.getText().trim();
            String question = signUpSelectQuestion.getValue();
            String answer = signUpAnswer.getText().trim();

            //Verificando se existe alguma caixa de texto vazia
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || question.isEmpty() || answer.isEmpty()) {
                alert.errorMessage("Preencha todos os campos!");
                return;
            }

            //Verificando se existe a senha é menor que 8 caracteres
            if (password.length() < 8) {
                alert.errorMessage("A senha deve ter pelo menos 8 caracteres!");
                return;
            }
            
            //Verificando se as senhas são iguais
            if (!password.equals(confirmPassword)) {
                alert.errorMessage("Coloque senhas iguais nos campos!");
                return;
            }

            // Criptografando a senha
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            //Verificando se existe um usuário com aquele email
            User user = userDao.getUserByEmail(email);
            
            //Verificando se o Email já está em uso
            if (user != null) {
                alert.errorMessage("O email já está em uso!");
                return;
            }

            //Instanciando novo usuário na memória
            User newUser = new User(name, email, hashedPassword, question, answer);
            //Inserindo novo usuário no banco de dados
            userDao.insertUser(newUser);
            
            alert.successMessage("Conta cadastrada!");
            //Limpando caixas de texto
            clearFields();

        } catch (SQLException e) {
        	// Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
            alert.errorMessage("Erro ao cadastrar conta: " + e.getMessage());
        } catch (Exception e) {
        	// Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
            alert.errorMessage("Ocorreu um erro ao tentar cadastrar: " + e.getMessage());
        }
    }
    
    // Método para limpar os campos de texto
    public void clearFields() {
        signUpName.setText("");
        signUpEmail.setText("");
        signUpPassword.setText("");
        signUpConfirmPassword.setText("");
        signUpSelectQuestion.getSelectionModel().clearSelection();
        signUpAnswer.setText("");
    }

 // Método para retornar à tela de login
    public void loginScreen(ActionEvent event) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    // Método initialize para inicializar a lista de perguntas e selecionar uma pergunta padrão
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        signUpSelectQuestion.setValue("Selecione uma pergunta");
        signUpSelectQuestion.setItems(questionBoxList);
    }
}
