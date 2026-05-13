package auth;

import home.Navegacao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField campoUsuario;
    @FXML private PasswordField campoSenha;
    @FXML private Label labelMensagem;

    private final AutenticacaoService autenticacaoService = new AutenticacaoService();

    @FXML
    private void onClickEntrar(ActionEvent event) {
        Usuario logado = autenticacaoService.autenticar(campoUsuario.getText(), campoSenha.getText());
        if (logado != null) {
            Sessao.getInstancia().login(logado);
            Navegacao.abrirCatalogo(stageDe(event));
        } else {
            mostrarErro("Usuário ou senha inválidos.");
        }
    }

    @FXML
    private void onClickIrParaCadastro(ActionEvent event) {
        Navegacao.abrirCadastro(stageDe(event));
    }

    private Stage stageDe(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private void mostrarErro(String msg) {
        labelMensagem.setStyle("-fx-text-fill: red;");
        labelMensagem.setText(msg);
    }
}
