package home;

import item.Item;
import item.ItemFormController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Navegacao {

    private Navegacao() {}

    public static void abrirLogin(Stage stage) {
        trocarCena(stage, "/auth/LoginView.fxml", "Login - Sistema de Doação", 480, 560);
    }

    public static void abrirCadastro(Stage stage) {
        trocarCena(stage, "/auth/CadastroView.fxml", "Cadastro - Sistema de Doação", 480, 560);
    }

    public static void abrirCatalogo(Stage stage) {
        trocarCena(stage, "/item/CatalogoView.fxml", "Catálogo de Doações", 900, 620);
    }

    public static void abrirPerfil(Stage stage) {
        trocarCena(stage, "/auth/PerfilView.fxml", "Meu Perfil - Sistema de Doação", 640, 620);
    }

    public static void abrirFormularioItem(Stage stage, Item itemParaEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Navegacao.class.getResource("/item/ItemFormView.fxml"));
            Parent root = loader.load();

            ItemFormController controller = loader.getController();
            controller.inicializar(stage, itemParaEditar);

            aplicarRoot(stage, root, itemParaEditar == null ? "Novo Item" : "Editar Item", 560, 560);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void trocarCena(Stage stage, String fxml, String titulo, int w, int h) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegacao.class.getResource(fxml));
            Parent root = loader.load();
            aplicarRoot(stage, root, titulo, w, h);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void aplicarRoot(Stage stage, Parent root, String titulo, int w, int h) {
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(root, w, h);
            scene.getStylesheets().add(
                    Navegacao.class.getResource("/styles/app.css").toExternalForm());
            stage.setScene(scene);
        } else {
            scene.setRoot(root);
        }
        stage.setTitle(titulo);
    }
}
