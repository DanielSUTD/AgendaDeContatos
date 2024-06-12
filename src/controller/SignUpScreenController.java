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
    private TextField signUp_Answer;

    @FXML
    private Button signUp_Button;

    @FXML
    private Button signUp_EnterButton;

    @FXML
    private PasswordField signUp_ConfirmPassword;

    @FXML
    private TextField signUp_Email;

    @FXML
    private TextField signUp_Name;

    @FXML
    private PasswordField signUp_Password;

    @FXML
    private ComboBox<String> signUp_SelectQuestion;

    @FXML
    private ObservableList<String> questionBoxList = FXCollections.observableArrayList("Qual sua comida favorita?", "Qual seu jogo favorito?", "Qual seu herói favorito?");

    public void signUp(ActionEvent event) throws IOException {
    	//Instanciando a mensagem de alerta
        AlertMessage alert = new AlertMessage();
        //Instanciando o Data Acess Object do Usuário
        UserDAO userDao = new UserDAO();

        try {
        	//Colocando as palavras nas caixas de texto no objeto String
            String name = signUp_Name.getText().trim();
            String email = signUp_Email.getText().trim();
            String password = signUp_Password.getText().trim();
            String confirmPassword = signUp_ConfirmPassword.getText().trim();
            String question = signUp_SelectQuestion.getValue();
            String answer = signUp_Answer.getText().trim();

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
            e.printStackTrace();
            alert.errorMessage("Erro ao cadastrar conta: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Ocorreu um erro ao tentar cadastrar: " + e.getMessage());
        }
    }


    public void clearFields() {
        signUp_Name.setText("");
        signUp_Email.setText("");
        signUp_Password.setText("");
        signUp_ConfirmPassword.setText("");
        signUp_SelectQuestion.getSelectionModel().clearSelection();
        signUp_Answer.setText("");
    }

    public void loginScreen(ActionEvent event) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        signUp_SelectQuestion.setValue("Selecione uma pergunta");
        signUp_SelectQuestion.setItems(questionBoxList);
    }
}
