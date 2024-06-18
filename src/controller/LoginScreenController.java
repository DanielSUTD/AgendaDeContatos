package controller;


import java.io.IOException;
import java.sql.SQLException;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import util.AlertMessage;

public class LoginScreenController {

    @FXML
    private TextField loginEmail;

    @FXML
    private Button loginEnterButton;

    @FXML
    private Hyperlink loginForgotPassword;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Button loginRegisterButton;

    private MyContacts main;

    // Setter para a classe principal MyContacts
    public void setMain(MyContacts main) {
        this.main = main;
    }

    public MyContacts getMain() {
		return main;
	}

	// Método para realizar o login
    public void login(ActionEvent event) {
        try {
            String email = loginEmail.getText().trim();
            String password = loginPassword.getText().trim();

            if (validateFields(email, password)) {
                authenticateUser(event, email, password);
            }
            clearFields();
        } catch (Exception e) {
            handleException(new AlertMessage(), e, "Erro ao tentar realizar o login!");
        }
    }

    private boolean validateFields(String email, String password) {
        AlertMessage alert = new AlertMessage();
        if (email.isEmpty() || password.isEmpty()) {
            alert.errorMessage("Email/Senha incorreta!");
            return false;
        }
        return true;
    }

    private void authenticateUser(ActionEvent event, String email, String password) throws IOException, SQLException {
        AlertMessage alert = new AlertMessage();
        UserDAO userDao = new UserDAO();
        User user = userDao.getUserByEmail(email);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            alert.successMessage("Login bem sucedido!");
            loadContactHomePage(event, user.getId());
        } else {
            alert.errorMessage("Email/Senha incorreta!");
        }
    }

    // Método para limpar os campos de texto
    public void clearFields() {
        loginEmail.setText("");
        loginPassword.setText("");
    }

    // Método para carregar a tela de contatos
    private void loadContactHomePage(ActionEvent event, int userID) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ContactPage.fxml"));
            Parent view = loader.load();

            ContactPageController contactPageController = loader.getController();
            contactPageController.setUserId(userID);

            Scene scene = new Scene(view);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            handleException(new AlertMessage(), e, "Erro ao carregar a página de contatos!");
        }
    }

    // Método para carregar a tela de cadastro
    public void signUpScreen(ActionEvent event) {
        try {
            loadScreen(event, "/view/SignUpScreen.fxml");
        } catch (IOException e) {
            handleException(new AlertMessage(), e, "Erro ao carregar a tela de cadastro!");
        }
    }

    // Método para carregar a tela de "Esqueceu a senha"
    public void forgotPassword(ActionEvent event) {
        try {
            loadScreen(event, "/view/ForgotPassword.fxml");
        } catch (IOException e) {
            handleException(new AlertMessage(), e, "Erro ao carregar a tela de recuperação de senha!");
        }
    }

    private void loadScreen(ActionEvent event, String fxmlPath) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    @FXML
    private void close(MouseEvent event) {
        // Obtém a janela atual (Stage) e a fecha
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Método para tratar exceções
    private void handleException(AlertMessage alert, Exception e, String message) {
        e.printStackTrace();
        alert.errorMessage(message + e.getMessage());
    }
}
