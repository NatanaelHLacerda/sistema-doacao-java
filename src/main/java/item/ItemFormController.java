package item;

import home.Navegacao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class ItemFormController {

    @FXML private Label     labelTitulo;
    @FXML private TextField campoNome;
    @FXML private TextField campoCategoria;
    @FXML private TextField campoQuantidade;
    @FXML private TextArea  campoDescricao;
    @FXML private Label     labelErro;

    @FXML private StackPane containerPreview;
    @FXML private ImageView previewImagem;
    @FXML private Label     placeholderImagem;
    @FXML private Button    btnRemoverImagem;

    private final ItemService itemService = new ItemService();

    private Stage stage;
    private Item  itemEmEdicao;
    private String imagemPathAtual;

    public void inicializar(Stage stage, Item item) {
        this.stage = stage;
        this.itemEmEdicao = item;

        if (item != null) {
            labelTitulo.setText("Editar Item");
            campoNome.setText(item.getNome());
            campoCategoria.setText(item.getCategoria());
            campoQuantidade.setText(String.valueOf(item.getQuantidade()));
            campoDescricao.setText(item.getDescricao() != null ? item.getDescricao() : "");
            aplicarImagem(item.getImagemPath());
        } else {
            labelTitulo.setText("Novo Item");
            aplicarImagem(null);
        }
    }

    @FXML
    private void onClickEscolherImagem(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecionar imagem do item");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));

        File escolhido = chooser.showOpenDialog(stage);
        if (escolhido == null) return;

        try {
            Path destino = ImagemStorage.copiarParaArmazenamento(escolhido);
            aplicarImagem(destino.toString());
            labelErro.setText("");
        } catch (Exception e) {
            mostrarErro("Não foi possível carregar a imagem: " + e.getMessage());
        }
    }

    @FXML
    private void onClickRemoverImagem(ActionEvent event) {
        aplicarImagem(null);
    }

    private void aplicarImagem(String caminho) {
        if (caminho != null && ImagemStorage.existe(caminho)) {
            imagemPathAtual = caminho;
            previewImagem.setImage(new Image(new File(caminho).toURI().toString()));
            previewImagem.setVisible(true);
            placeholderImagem.setVisible(false);
            btnRemoverImagem.setDisable(false);
        } else {
            imagemPathAtual = null;
            previewImagem.setImage(null);
            previewImagem.setVisible(false);
            placeholderImagem.setVisible(true);
            btnRemoverImagem.setDisable(true);
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
                    quantidade,
                    imagemPathAtual
            );
        } else {
            itemEmEdicao.setNome(campoNome.getText().trim());
            itemEmEdicao.setCategoria(campoCategoria.getText().trim());
            itemEmEdicao.setDescricao(campoDescricao.getText());
            itemEmEdicao.setQuantidade(quantidade);
            itemEmEdicao.setImagemPath(imagemPathAtual);
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
