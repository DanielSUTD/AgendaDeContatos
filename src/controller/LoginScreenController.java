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

    //MAIN
    public void setMain(MyContacts main) {
        this.main = main;
    }

    public void login(ActionEvent event) throws IOException {
    	//Instanciando mensagem
        AlertMessage alert = new AlertMessage();
        //Instanciando userDAO
        UserDAO userDao = new UserDAO();
        
        try {
            String email = login_Email.getText().trim();
            String password = login_Password.getText().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                alert.errorMessage("Email/Senha incorreta!");
            } else {
                User user = userDao.getUserByEmail(email);
                
                if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                    alert.successMessage("Login bem sucedido!");
                    contactHomePage(event, user.getId());
                } else {
                    alert.errorMessage("Email/Senha incorreta!");
                }
            }
            clearFields();
        } catch (IOException e) {
            e.printStackTrace();
            alert.errorMessage("Ocorreu um erro ao carregar a tela de contato.");
        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Ocorreu um erro ao tentar fazer login.");
        }
    }

    //Limpando caixas de texto
    public void clearFields() {
        login_Email.setText("");
        login_Password.setText("");
    }

    //TELA DA AGENDA DE CONTATOS
    public void contactHomePage(ActionEvent event, int userID) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ContactPage.fxml"));
        Parent view = loader.load();
        
        // Passando o ID do usuário para o ContactPageController
        ContactPageController contactPageController = loader.getController();
        contactPageController.setUserId(userID);
        
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    //TELA DE CADASTRO
    public void signUpScreen(ActionEvent event) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/SignUpScreen.fxml"));
        Scene scene = new Scene(view);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    //TELA DE "ESQUECEU A SENHA"
    public void forgotPassword(ActionEvent event) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/ForgotPassword.fxml"));
        Scene scene = new Scene(view);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
