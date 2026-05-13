module doacao {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens app to javafx.fxml;
    opens auth to javafx.fxml;
    opens home to javafx.fxml;
    opens item to javafx.fxml;

    exports app;
    exports auth;
    exports home;
    exports doador;
    exports shared;
    exports item;
}
