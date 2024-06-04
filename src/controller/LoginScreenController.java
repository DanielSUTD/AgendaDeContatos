package controller;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;
import application.MyContacts;
import dao.ConnectDB;
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
    
    //Objetos SQL
    private Connection connectionSQL;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    private MyContacts main;

    public void setMain(MyContacts main) {
    	this.main = main;
	}
	
    
    //Login
    public void login(ActionEvent event)  throws IOException{
    	
    	ConnectDB connect = new ConnectDB();
    	AlertMessage alert = new AlertMessage();
    	
    	try {
    	 Connection con = connect.getconnection();
    	 
    	 String email = login_Email.getText().trim();
         String password = login_Password.getText().trim();
         
         if(email.isEmpty() || password.isEmpty()) {
        	 alert.errorMessage("Email/Senha incorreta!");
         }else {
        	 String query = "SELECT NOME, EMAIL, SENHA FROM usuario WHERE EMAIL=?";
        	 PreparedStatement preparedStatement = con.prepareStatement(query);
        	  
        	 preparedStatement.setString(1, email);

             ResultSet resultSet = preparedStatement.executeQuery();
                 //Verificando se existe um cursor apontando para uma linha válida
                 if(resultSet.next()){
                	 //Pegando senha criptografada
                	 String hashedPassword = resultSet.getString(3);
                  //Verificando se a senha que o usuário digitou é igual a senha do banco de dados
                  if (BCrypt.checkpw(password, hashedPassword)) {
                      alert.successMessage("Login bem sucedido!");
                      contactHomePage(event);
                  } else {
                      alert.errorMessage("Email/Senha incorreta!");
                  }
                  
                 } else {     
                	 alert.successMessage("Digite os dados corretos!");
                 }
                 
         }
         
         clearFields();
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public void clearFields() {
    	login_Email.setText("");
    	login_Password.setText("");
    }
    
    public void contactHomePage(ActionEvent event) throws IOException{
    	 Parent view = FXMLLoader.load(getClass().getResource("/view/ContactPage.fxml"));
         Scene scene = new Scene(view);
         Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
         window.setScene(scene);
         window.show();
    }
    
    public void signUpScreen(ActionEvent event) throws IOException{
    	Parent view= FXMLLoader.load(getClass().getResource("/view/SignUpScreen.fxml"));
        Scene scene= new Scene(view);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    public void forgotPassword(ActionEvent event)throws IOException{
    	Parent view = FXMLLoader.load(getClass().getResource("/view/ForgotPassword.fxml"));
    	Scene scene = new Scene(view);
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
    	window.show();
    }
    	  
    

}
