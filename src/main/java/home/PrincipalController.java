package home;

import auth.Sessao;
import auth.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PrincipalController {

    @FXML private Label labelBoasVindas;
    @FXML private TextField campoNome;
    @FXML private Label labelMensagem;

    private final SaudacaoService saudacaoService = new SaudacaoService();

    @FXML
    public void initialize() {
        Usuario logado = Sessao.getInstancia().getUsuarioLogado();
        if (logado != null) {
            labelBoasVindas.setText("@" + logado.getNomeUsuario());
        }
    }

    @FXML
    private void onClickSaudar() {
        labelMensagem.setText(saudacaoService.saudar(campoNome.getText()));
    }

    @FXML
    private void onClickSair(ActionEvent event) {
        Sessao.getInstancia().logout();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Navegacao.abrirLogin(stage);
    }
}
