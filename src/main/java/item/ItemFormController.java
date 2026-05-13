package item;

import home.Navegacao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ItemFormController {

    @FXML private Label     labelTitulo;
    @FXML private TextField campoNome;
    @FXML private TextField campoCategoria;
    @FXML private TextField campoQuantidade;
    @FXML private TextArea  campoDescricao;
    @FXML private Label     labelErro;

    private final ItemService itemService = new ItemService();

    private Stage stage;
    private Item  itemEmEdicao;

    public void inicializar(Stage stage, Item item) {
        this.stage = stage;
        this.itemEmEdicao = item;

        if (item != null) {
            labelTitulo.setText("Editar Item");
            campoNome.setText(item.getNome());
            campoCategoria.setText(item.getCategoria());
            campoQuantidade.setText(String.valueOf(item.getQuantidade()));
            campoDescricao.setText(item.getDescricao() != null ? item.getDescricao() : "");
        } else {
            labelTitulo.setText("Novo Item");
        }
    }

    @FXML
    private void onClickSalvar(ActionEvent event) {
        int quantidade;
        try {
            quantidade = Integer.parseInt(campoQuantidade.getText().trim());
        } catch (NumberFormatException e) {
            mostrarErro("Quantidade deve ser um número inteiro.");
            return;
        }

        boolean sucesso;

        if (itemEmEdicao == null) {
            sucesso = itemService.cadastrar(
                    campoNome.getText(),
                    campoCategoria.getText(),
                    campoDescricao.getText(),
                    quantidade
            );
        } else {
            itemEmEdicao.setNome(campoNome.getText().trim());
            itemEmEdicao.setCategoria(campoCategoria.getText().trim());
            itemEmEdicao.setDescricao(campoDescricao.getText());
            itemEmEdicao.setQuantidade(quantidade);
            sucesso = itemService.atualizar(itemEmEdicao);
        }

        if (sucesso) {
            voltarParaCatalogo();
        } else {
            mostrarErro("Preencha nome, categoria e quantidade corretamente.");
        }
    }

    @FXML
    private void onClickCancelar(ActionEvent event) {
        voltarParaCatalogo();
    }

    private void voltarParaCatalogo() {
        Navegacao.abrirCatalogo(stage);
    }

    private void mostrarErro(String mensagem) {
        labelErro.setText(mensagem);
    }
}
