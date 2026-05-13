package auth;

import home.Navegacao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PerfilController {

    @FXML private Label         labelUsuarioHeader;
    @FXML private TextField     campoNovoNome;
    @FXML private PasswordField campoSenhaAtual;
    @FXML private PasswordField campoNovaSenha;
    @FXML private Label         labelMensagem;

    private final AutenticacaoService autenticacaoService = new AutenticacaoService();

    @FXML
    public void initialize() {
        Usuario logado = Sessao.getInstancia().getUsuarioLogado();
        if (logado != null) {
            labelUsuarioHeader.setText("@" + logado.getNomeUsuario());
            campoNovoNome.setText(logado.getNomeUsuario());
        }
    }

    @FXML
    private void onClickSalvar(ActionEvent event) {
        Usuario logado = Sessao.getInstancia().getUsuarioLogado();
        if (logado == null) {
            Navegacao.abrirLogin(stageDe(event));
            return;
        }

        ResultadoEdicao resultado = autenticacaoService.atualizarPerfil(
                logado.getId(),
                campoSenhaAtual.getText(),
                campoNovoNome.getText(),
                campoNovaSenha.getText()
        );

        switch (resultado) {
            case SUCESSO -> {
                String novoNome = Sessao.getInstancia().getUsuarioLogado().getNomeUsuario();
                labelUsuarioHeader.setText("@" + novoNome);
                mostrarFeedback("Perfil atualizado com sucesso.", true);
                limparCamposSenha();
            }
            case SENHA_INCORRETA ->
                mostrarFeedback("Senha atual incorreta.", false);
            case USUARIO_JA_EXISTE ->
                mostrarFeedback("Este nome de usuário já está em uso.", false);
            case DADOS_INVALIDOS ->
                mostrarFeedback("Preencha todos os campos obrigatórios.", false);
        }
    }

    @FXML
    private void onClickVoltar(ActionEvent event) {
        Navegacao.abrirCatalogo(stageDe(event));
    }

    private void mostrarFeedback(String mensagem, boolean sucesso) {
        labelMensagem.setText(mensagem);
        labelMensagem.getStyleClass().removeAll("label-error", "label-success");
        labelMensagem.getStyleClass().add(sucesso ? "label-success" : "label-error");
    }

    private void limparCamposSenha() {
        campoSenhaAtual.clear();
        campoNovaSenha.clear();
    }

    private Stage stageDe(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
