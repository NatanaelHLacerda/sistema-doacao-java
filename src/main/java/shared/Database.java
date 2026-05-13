package shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {

    private static final String URL = "jdbc:sqlite:doacao.db";

    private Database() {}

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
