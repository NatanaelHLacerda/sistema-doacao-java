package item;

import auth.Sessao;
import home.Navegacao;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.util.Locale;
import java.util.Map;

public class CatalogoController {

    @FXML private Label      labelUsuario;
    @FXML private Label      labelContador;
    @FXML private TextField  campoBusca;
    @FXML private ScrollPane scrollGrade;
    @FXML private FlowPane   grade;
    @FXML private VBox       estadoVazio;
    @FXML private Label      labelFeedback;

    private final ItemService itemService = new ItemService();

    private final ObservableList<Item> listaObservavel = FXCollections.observableArrayList();
    private FilteredList<Item> listaFiltrada;

    private static final Map<String, String> EMOJI_POR_CATEGORIA = Map.ofEntries(
            Map.entry("roupas", "👕"),
            Map.entry("calçados", "👟"),
            Map.entry("calcados", "👟"),
            Map.entry("livros", "📚"),
            Map.entry("acessórios", "🎒"),
            Map.entry("acessorios", "🎒"),
            Map.entry("casa", "🏠"),
            Map.entry("eletrônicos", "📱"),
            Map.entry("eletronicos", "📱"),
            Map.entry("brinquedos", "🧸"),
            Map.entry("higiene", "🧼"),
            Map.entry("alimentos", "🍞")
    );

    private static final Map<String, String[]> GRADIENTE_POR_CATEGORIA = Map.ofEntries(
            Map.entry("roupas",      new String[]{"#fb7185", "#be123c"}),
            Map.entry("calçados",    new String[]{"#a78bfa", "#6d28d9"}),
            Map.entry("calcados",    new String[]{"#a78bfa", "#6d28d9"}),
            Map.entry("livros",      new String[]{"#60a5fa", "#1d4ed8"}),
            Map.entry("acessórios",  new String[]{"#fbbf24", "#b45309"}),
            Map.entry("acessorios",  new String[]{"#fbbf24", "#b45309"}),
            Map.entry("casa",        new String[]{"#34d399", "#047857"}),
            Map.entry("eletrônicos", new String[]{"#22d3ee", "#0e7490"}),
            Map.entry("eletronicos", new String[]{"#22d3ee", "#0e7490"}),
            Map.entry("brinquedos",  new String[]{"#f472b6", "#a21caf"}),
            Map.entry("higiene",     new String[]{"#5eead4", "#0f766e"}),
            Map.entry("alimentos",   new String[]{"#fb923c", "#b45309"})
    );

    private static final String[] GRADIENTE_PADRAO = {"#94a3b8", "#475569"};

    @FXML
    public void initialize() {
        if (Sessao.getInstancia().getUsuarioLogado() != null) {
            labelUsuario.setText("@" + Sessao.getInstancia().getUsuarioLogado().getNomeUsuario());
        }
        configurarBusca();
        carregarItens();
    }

    private void configurarBusca() {
        listaFiltrada = new FilteredList<>(listaObservavel, p -> true);

        campoBusca.textProperty().addListener((obs, antigo, novo) -> {
            listaFiltrada.setPredicate(item -> {
                if (novo == null || novo.isBlank()) return true;
                String filtro = novo.toLowerCase();
                return item.getNome().toLowerCase().contains(filtro)
                        || item.getCategoria().toLowerCase().contains(filtro);
            });
        });

        listaFiltrada.addListener((ListChangeListener<Item>) change -> renderizar());
    }

    private void carregarItens() {
        listaObservavel.setAll(itemService.listarTodos());
        renderizar();
    }

    private void renderizar() {
        grade.getChildren().clear();

        int total = listaFiltrada.size();
        labelContador.setText(total + (total == 1 ? " item" : " itens"));

        boolean vazio = total == 0;
        estadoVazio.setVisible(vazio);
        estadoVazio.setManaged(vazio);
        scrollGrade.setVisible(!vazio);
        scrollGrade.setManaged(!vazio);

        for (Item item : listaFiltrada) {
            grade.getChildren().add(criarCard(item));
        }
    }

    private Node criarCard(Item item) {
        VBox card = new VBox();
        card.getStyleClass().add("product-card");
        card.setPrefWidth(240);
        card.setMaxWidth(240);
        card.setMinWidth(240);

        StackPane thumb = criarThumb(item);

        VBox corpo = new VBox();
        corpo.getStyleClass().add("product-body");

        HBox badges = new HBox(6);
        Label badgeCat = new Label(item.getCategoria());
        badgeCat.getStyleClass().add("badge-category");
        Label badgeQty = new Label("Qtd: " + item.getQuantidade());
        badgeQty.getStyleClass().add("badge-qty");
        badges.getChildren().addAll(badgeCat, badgeQty);

        Label titulo = new Label(item.getNome());
        titulo.getStyleClass().add("product-title");
        titulo.setWrapText(true);

        String desc = item.getDescricao();
        if (desc == null || desc.isBlank()) {
            desc = "Sem descrição.";
        }
        Label descricao = new Label(desc);
        descricao.getStyleClass().add("product-description");
        descricao.setWrapText(true);
        descricao.setMaxHeight(48);

        Region spacer = new Region();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        HBox acoes = new HBox(6);
        acoes.setAlignment(Pos.CENTER_RIGHT);
        Button btnEditar = new Button("Editar");
        btnEditar.getStyleClass().addAll("btn-icon", "btn-icon-edit");
        btnEditar.setOnAction(e -> {
            Stage stage = (Stage) card.getScene().getWindow();
            Navegacao.abrirFormularioItem(stage, item);
        });

        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().addAll("btn-icon", "btn-icon-delete");
        btnExcluir.setOnAction(e -> confirmarEExcluir(item));

        acoes.getChildren().addAll(btnEditar, btnExcluir);

        corpo.getChildren().addAll(badges, titulo, descricao, spacer, acoes);
        VBox.setVgrow(corpo, javafx.scene.layout.Priority.ALWAYS);

        card.getChildren().addAll(thumb, corpo);
        card.setPrefHeight(360);
        return card;
    }

    private StackPane criarThumb(Item item) {
        StackPane thumb = new StackPane();
        thumb.getStyleClass().add("product-thumb");
        thumb.setPrefHeight(160);
        thumb.setMinHeight(160);
        thumb.setMaxHeight(160);

        if (ImagemStorage.existe(item.getImagemPath())) {
            ImageView iv = new ImageView(new Image(
                    new File(item.getImagemPath()).toURI().toString(),
                    240, 160, true, true, true));
            iv.setFitWidth(240);
            iv.setFitHeight(160);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);

            Rectangle clip = new Rectangle(240, 160);
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            iv.setClip(clip);

            thumb.setStyle("-fx-background-color: #e2e8f0; -fx-background-radius: 12 12 0 0;");
            thumb.getChildren().add(iv);
        } else {
            String[] cores = gradientePara(item.getCategoria());
            thumb.setStyle(
                    "-fx-background-color: linear-gradient(to bottom right, "
                    + cores[0] + ", " + cores[1] + ");"
                    + " -fx-background-radius: 12 12 0 0;");
            Label icone = new Label(emojiPara(item.getCategoria()));
            icone.getStyleClass().add("product-thumb-emoji");
            icone.setStyle("-fx-font-size: 64px;");
            thumb.getChildren().add(icone);
        }
        return thumb;
    }

    private String emojiPara(String categoria) {
        if (categoria == null) return "📦";
        return EMOJI_POR_CATEGORIA.getOrDefault(
                categoria.toLowerCase(Locale.ROOT).trim(), "📦");
    }

    private String[] gradientePara(String categoria) {
        if (categoria == null) return GRADIENTE_PADRAO;
        return GRADIENTE_POR_CATEGORIA.getOrDefault(
                categoria.toLowerCase(Locale.ROOT).trim(), GRADIENTE_PADRAO);
    }

    private void confirmarEExcluir(Item item) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar exclusão");
        alerta.setHeaderText("Excluir \"" + item.getNome() + "\"?");
        alerta.setContentText("Esta ação não pode ser desfeita.");
        alerta.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                if (itemService.excluir(item.getId())) {
                    listaObservavel.remove(item);
                    mostrarFeedback("Item excluído com sucesso.", true);
                } else {
                    mostrarFeedback("Erro ao excluir item.", false);
                }
            }
        });
    }

    @FXML
    private void onClickNovoItem(ActionEvent event) {
        Navegacao.abrirFormularioItem(stageDe(event), null);
    }

    @FXML
    private void onClickPerfil(ActionEvent event) {
        Navegacao.abrirPerfil(stageDe(event));
    }

    @FXML
    private void onClickSair(ActionEvent event) {
        Sessao.getInstancia().logout();
        Navegacao.abrirLogin(stageDe(event));
    }

    private Stage stageDe(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private void mostrarFeedback(String mensagem, boolean sucesso) {
        labelFeedback.setText(mensagem);
        labelFeedback.setStyle(sucesso
                ? "-fx-text-fill: #16a34a;"
                : "-fx-text-fill: #dc2626;");
    }
}
