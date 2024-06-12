package application;


import java.io.IOException;
import controller.LoginScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MyContacts extends Application {
	
   
    private Stage primaryStage;
   

    @Override
    public void start(Stage primaryStage) {
    	
    	//Armazenar o palco principal do aplicativo
        this.primaryStage = primaryStage; 
        
        //Exibir janela principal
        mainWindow();
    }

    //MAIN
    public void mainWindow() {
        try {
            //Carregando arquivo FXML
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/LoginScreen.fxml"));
            AnchorPane pane = loader.load();

            //Carregando e referenciado classe controller
            LoginScreenController mainWindowController = loader.getController();
            mainWindowController.setMain(this);

            //Nova cena
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}