package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import dao.ConnectDB;
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
    
    //Objetos SQL
    private Connection con;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    ObservableList<String> questionBoxList = FXCollections.observableArrayList(
            "Qual sua comida favorita?",
            "Qual seu jogo favorito?",
            "Qual seu herói favorito?"
    );

    public void searchPassword() {
        ConnectDB connect = new ConnectDB();
        AlertMessage alert = new AlertMessage();
        
        try {
        	
        	con = connect.getconnection();
        	
        	String email = forgotPsw_Email.getText().trim();
        	String password = forgotPsw_Password.getText().trim();
        	String selectedQuestion = forgotPsw_Question.getValue() != null ? forgotPsw_Question.getValue().trim() : "";
        	String answer = forgotPsw_Answer.getText().trim();
        	
        	if(email.isEmpty()) {
        		alert.errorMessage("Digite o email!");
        	} else if (selectedQuestion.isEmpty()) {
        		alert.errorMessage("Selecione uma pergunta de segurança!");
        	} else {
        	    String query = "SELECT EMAIL, ALTERNATIVA, RESPOSTA, SENHA FROM usuario WHERE EMAIL=?";
        		preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, email);

                resultSet = preparedStatement.executeQuery();
                
                //Se encontrar uma linha válida
                if(resultSet.next()) {
                	email = resultSet.getString(1);
                	String dbSelectedQuestion = resultSet.getString(2);
                	String dbAnswer = resultSet.getString(3);
                	String dbPassword = resultSet.getString(4);
                	
                	resultSet.close();
                	
                	//Verifica Resposta
                	 if(forgotPsw_Answer != null) {

                		 
                		 //Verifica se a resposta é igual a resposta que o usuário digitou
                		 if(dbAnswer.equals(answer)) {
                			 //Criptografando nova senha
                			 String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                			 
                			 String query_2 = "UPDATE usuario SET SENHA=? WHERE EMAIL=?";
                             preparedStatement = con.prepareStatement(query_2);
                             preparedStatement.setString(1, hashedPassword);
                             preparedStatement.setString(2, email);
                             preparedStatement.executeUpdate();
                             alert.successMessage("Senha atualizada!");
                             preparedStatement.close();
                		 } else {
                			 alert.errorMessage("Resposta errada!");
                		 }
                		 
                	 } else {
                		 alert.errorMessage("Resposta errada!");
                	 }
                	
                } else {
                	 alert.errorMessage("Registro não encontrado!");
                }
        	}
        	
        	clearFields();
        	
        } catch(Exception e) {
        	e.printStackTrace();
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
