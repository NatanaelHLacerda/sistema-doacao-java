package auth;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import home.Navegacao;

public class CadastroController {

    @FXML private TextField campoUsuario;
    @FXML private PasswordField campoSenha;
    @FXML private Label labelMensagem;

    private final AutenticacaoService autenticacaoService = new AutenticacaoService();

    @FXML
    private void onClickCadastrar() {
        boolean ok = autenticacaoService.cadastrar(campoUsuario.getText(), campoSenha.getText());
        if (ok) {
            mostrar("Cadastro realizado. Volte ao login.", "green");
        } else {
            mostrar("Dados inválidos ou usuário já existe.", "red");
        }
    }

    @FXML
    private void onClickVoltar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Navegacao.abrirLogin(stage);
    }

    private void mostrar(String msg, String cor) {
        labelMensagem.setStyle("-fx-text-fill: " + cor + ";");
        labelMensagem.setText(msg);
    }
}
