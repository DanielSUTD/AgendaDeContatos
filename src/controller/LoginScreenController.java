package controller;

import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;
import application.MyContacts;
import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import util.AlertMessage;

public class LoginScreenController {

    @FXML
    private Button login_EnterButton;

    @FXML
    private TextField login_Email;

    @FXML
    private Hyperlink login_ForgotPassword;

    @FXML
    private PasswordField login_Password;

    @FXML
    private Button login_RegisterButton;

    private MyContacts main;

    // Setter para a classe principal MyContacts
    public void setMain(MyContacts main) {
        this.main = main;
    }

    // Método para realizar o login
    public void login(ActionEvent event) throws IOException {
        // Instancia uma mensagem de alerta
        AlertMessage alert = new AlertMessage();
        // Instancia UserDAO para acessar métodos de banco de dados relacionados ao usuário
        UserDAO userDao = new UserDAO();

        try {
            // Obtém os valores dos campos de email e senha
            String email = login_Email.getText().trim();
            String password = login_Password.getText().trim();

            // Verifica se os campos de email ou senha estão vazios
            if (email.isEmpty() || password.isEmpty()) {
                alert.errorMessage("Email/Senha incorreta!");
            } else {
                // Obtém o usuário pelo email
                User user = userDao.getUserByEmail(email);

                // Verifica se o usuário existe e se a senha está correta
                if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                    alert.successMessage("Login bem sucedido!");
                    // Chama o método para carregar a tela de contatos passando o ID do usuário
                    contactHomePage(event, user.getId());
                } else {
                    alert.errorMessage("Email/Senha incorreta!");
                }
            }
            // Limpa os campos de texto após a tentativa de login
            clearFields();
        } catch (IOException e) {
        	// Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
            alert.errorMessage("Ocorreu um erro ao carregar a tela de contato.");
        } catch (Exception e) {
        	// Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
            alert.errorMessage("Ocorreu um erro ao tentar fazer login.");
        }
    }

    // Método para limpar os campos de texto
    public void clearFields() {
        login_Email.setText("");
        login_Password.setText("");
    }

    // Método para carregar a tela de contatos
    public void contactHomePage(ActionEvent event, int userID) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ContactPage.fxml"));
        Parent view = loader.load();

        // Passa o ID do usuário para o ContactPageController
        ContactPageController contactPageController = loader.getController();
        contactPageController.setUserId(userID);

        // Cria uma nova cena e a define no palco atual
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // Método para carregar a tela de cadastro
    public void signUpScreen(ActionEvent event) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/SignUpScreen.fxml"));
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    // Método para carregar a tela de "Esqueceu a senha"
    public void forgotPassword(ActionEvent event) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/ForgotPassword.fxml"));
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
