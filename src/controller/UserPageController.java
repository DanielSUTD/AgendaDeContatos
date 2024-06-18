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


    //Lista com perguntas de ssegurança
    private ObservableList<String> questionBoxList = FXCollections.observableArrayList(
            "Qual sua comida favorita?",
            "Qual seu jogo favorito?",
            "Qual seu herói favorito?"
    );

    //Id do usuário atual
    private int userID;
    
    // Método para definir o ID do usuário e carregar seus dados
    public void setUserId(int userID) {
        this.userID = userID;
     // Carrega os dados do usuário com base no ID
        loadUserData();
    }

 // Método privado para carregar os dados do usuário
    private void loadUserData() {
    	// Instancia o DAO do usuário
        UserDAO userDAO = new UserDAO();
        try {
        	// Obtém o usuário do banco de dados pelo ID
            User user = userDAO.getUserById(userID);
            
            //Coloca cada dado do banco de dados no campo de texto correspondente!
            if (user != null) {
                userPageName.setText(user.getName());
                userPageEmail.setText(user.getEmail());
                userPageSelectQuestion.setValue(user.getQuestion());
                userPageAnswer.setText(user.getAnswer());
            }
        } catch (SQLException e) {
        	// Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
        }
    }

    // Método para atualizar as informações do usuário
    @FXML
    public void updateUserInformation(ActionEvent event) {
    	//Instanciando mensagem de alerta
        AlertMessage alert = new AlertMessage();

        
        try {
        	//Colocando cada caixa de texto em uma string!
            String name = userPageName.getText().trim();
            String email = userPageEmail.getText().trim();
            String password = userPagePassword.getText().trim(); 
            String question = userPageSelectQuestion.getValue() != null ? userPageSelectQuestion.getValue().trim() : "";
            String answer = userPageAnswer.getText().trim();
            

            //Criptografando senha
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            //Instanciando o usuário com seus novos dados!
            User updatedUser = new User(userID, name, email, hashedPassword, question, answer);
            
            UserDAO userDAO = new UserDAO();
            //Enviando dados para o banco de dados!
            userDAO.updateUser(updatedUser);

            //Mensagem
            alert.successMessage("Informações do usuário atualizadas com sucesso!");
        } catch (SQLException e) {
        	// Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
        } catch (Exception e) {
        	// Imprimindo o stack trace em caso de exceção
            e.printStackTrace();
        }
    }

 // Método para voltar à página anterior
    @FXML
    public void back(ActionEvent event) throws IOException {
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
    
   // Método initialize para inicializar a lista de perguntas
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userPageSelectQuestion.setItems(questionBoxList);
    }

}
