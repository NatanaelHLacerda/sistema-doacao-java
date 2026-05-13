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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    private static final String[] CORES_THUMB = {
            "#2563eb", "#0ea5e9", "#16a34a", "#f59e0b",
            "#dc2626", "#a855f7", "#ec4899", "#0d9488"
    };

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

        StackPane thumb = new StackPane();
        thumb.getStyleClass().add("product-thumb");
        thumb.setStyle("-fx-background-color: " + corPara(item) + "; -fx-background-radius: 12 12 0 0;");
        Label inicial = new Label(iniciaisDe(item.getNome()));
        inicial.getStyleClass().add("product-thumb-label");
        thumb.getChildren().add(inicial);

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
        card.setPrefHeight(340);
        return card;
    }

    private String corPara(Item item) {
        int hash = Math.abs((item.getCategoria() + item.getNome()).hashCode());
        return CORES_THUMB[hash % CORES_THUMB.length];
    }

    private String iniciaisDe(String nome) {
        if (nome == null || nome.isBlank()) return "?";
        String[] partes = nome.trim().split("\\s+");
        if (partes.length == 1) {
            return partes[0].substring(0, Math.min(2, partes[0].length())).toUpperCase();
        }
        return ("" + partes[0].charAt(0) + partes[1].charAt(0)).toUpperCase();
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
