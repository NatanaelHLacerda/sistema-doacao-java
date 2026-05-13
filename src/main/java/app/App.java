package app;

import auth.UsuarioDAO;
import item.ItemDAO;
import item.ItemSeeder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        new UsuarioDAO().criarTabela();
        new ItemDAO().criarTabela();
        ItemSeeder.popularSeVazio();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/auth/LoginView.fxml")
        );
        Parent root = loader.load();

        Scene scene = new Scene(root, 480, 560);
        scene.getStylesheets().add(
                getClass().getResource("/styles/app.css").toExternalForm()
        );
        stage.setTitle("Login - Sistema de Doação");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
