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

    // Atributo para armazenar o palco principal do aplicativo
    private Stage primaryStage;

    // Método inicial do aplicativo JavaFX
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        
        mainWindow();
    }

    // Método principal para carregar e exibir a janela de login
    public void mainWindow() {
        try {
           
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/LoginScreen.fxml"));
            AnchorPane pane = loader.load();

            
            LoginScreenController mainWindowController = loader.getController();
            mainWindowController.setMain(this);

            
            Scene scene = new Scene(pane);
            
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método principal para lançar o aplicativo
    public static void main(String[] args) {
        launch(args);
    }
}
