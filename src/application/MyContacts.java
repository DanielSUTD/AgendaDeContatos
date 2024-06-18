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
        // Armazenar o palco principal do aplicativo
        this.primaryStage = primaryStage;

        // Exibir a janela principal
        mainWindow();
    }

    // Método principal para carregar e exibir a janela de login
    public void mainWindow() {
        try {
            // Carregando o arquivo FXML da tela de login
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/LoginScreen.fxml"));
            AnchorPane pane = loader.load();

            // Carregando e referenciando a classe controller da tela de login
            LoginScreenController mainWindowController = loader.getController();
            mainWindowController.setMain(this);

            // Criando uma nova cena com o layout carregado
            Scene scene = new Scene(pane);
            // Configurando a cena no palco principal
            primaryStage.setScene(scene);
            // Definindo o estilo do palco principal como não decorado (sem barra de título padrão)
            primaryStage.initStyle(StageStyle.UNDECORATED);
            // Exibindo o palco principal
            primaryStage.show();

        } catch (IOException e) {
            // Imprimindo o stack trace em caso de erro ao carregar o arquivo FXML
            e.printStackTrace();
        }
    }

    // Método principal para lançar o aplicativo
    public static void main(String[] args) {
        // Método estático launch da classe Application para iniciar o aplicativo JavaFX
        launch(args);
    }
}
