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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.AlertMessage;
import org.mindrot.jbcrypt.BCrypt;

public class SignUpScreenController implements Initializable{

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
    
    
    ObservableList<String> questionBoxList = FXCollections.observableArrayList("Qual sua comida favorita?","Qual seu jogo favorito?","Qual seu herói favorito?");
    
    //Objetos SQL
    private Connection connectionSQL;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public void signUp(ActionEvent event) throws IOException {
        AlertMessage alert = new AlertMessage();
        ConnectDB connect = new ConnectDB();

        try {
            Connection con = connect.getconnection();

            String name = signUp_Name.getText().trim();
            String email = signUp_Email.getText().trim();
            String password = signUp_Password.getText().trim();
            String confirmPassword = signUp_ConfirmPassword.getText().trim();
            String question = signUp_SelectQuestion.getValue().toString();
            String answer = signUp_Answer.getText().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || question.isEmpty() || answer.isEmpty()) {
                alert.errorMessage("Preencha todos os campos!");
            }else {
                if (password.length() < 8) {
                    alert.errorMessage("A senha deve ter pelo menos 8 caracteres!");
                } else if (!password.equals(confirmPassword)) {
                    alert.errorMessage("Coloque senhas iguais nos campos!");
                } else {
                	//Criptografando a senha
                	String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                	
                    // Verificação do email
                    String query = "SELECT * FROM usuario WHERE EMAIL = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, email);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        alert.errorMessage("O email já está em uso!");
                    } else {
                        String query_2 = "INSERT INTO usuario (NOME, EMAIL, SENHA, ALTERNATIVA, RESPOSTA) VALUES(?,?,?,?,?)";
                        preparedStatement = con.prepareStatement(query_2);

                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, email);
                        preparedStatement.setString(3, hashedPassword);
                        preparedStatement.setString(4, question);
                        preparedStatement.setString(5, answer);

                        preparedStatement.execute();

                        alert.successMessage("Conta cadastrada!");
                    }
                }
            }
            
           
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        clearFields();
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
        Parent view= FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        Scene scene= new Scene(view);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
      
     }

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		  signUp_SelectQuestion.setValue("Selecione uma pergunta");
		  signUp_SelectQuestion.setItems(questionBoxList);
		
	}

}
