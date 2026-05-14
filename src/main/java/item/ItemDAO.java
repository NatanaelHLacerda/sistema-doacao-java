package item;

import shared.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS itens (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome        TEXT    NOT NULL,
                    categoria   TEXT    NOT NULL,
                    descricao   TEXT,
                    quantidade  INTEGER NOT NULL DEFAULT 1,
                    imagem_path TEXT
                )
                """;
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            migrarColunaImagem(conn);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela itens: " + e.getMessage());
        }
    }

    private void migrarColunaImagem(Connection conn) {
        try (Statement check = conn.createStatement();
             ResultSet rs = check.executeQuery("PRAGMA table_info(itens)")) {
            boolean possui = false;
            while (rs.next()) {
                if ("imagem_path".equalsIgnoreCase(rs.getString("name"))) {
                    possui = true;
                    break;
                }
            }
            if (!possui) {
                try (Statement alter = conn.createStatement()) {
                    alter.execute("ALTER TABLE itens ADD COLUMN imagem_path TEXT");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao migrar coluna imagem_path: " + e.getMessage());
        }
    }

    public boolean salvar(Item item) {
        String sql = "INSERT INTO itens (nome, categoria, descricao, quantidade, imagem_path) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getNome());
            ps.setString(2, item.getCategoria());
            ps.setString(3, item.getDescricao());
            ps.setInt(4, item.getQuantidade());
            ps.setString(5, item.getImagemPath());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar item: " + e.getMessage());
            return false;
        }
    }

    public List<Item> listarTodos() {
        List<Item> lista = new ArrayList<>();
        String sql = "SELECT id, nome, categoria, descricao, quantidade, imagem_path FROM itens ORDER BY nome";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Item(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("categoria"),
                        rs.getString("descricao"),
                        rs.getInt("quantidade"),
                        rs.getString("imagem_path")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar itens: " + e.getMessage());
        }
        return lista;
    }

    public boolean atualizar(Item item) {
        String sql = "UPDATE itens SET nome=?, categoria=?, descricao=?, quantidade=?, imagem_path=? WHERE id=?";
        try (Connection conn = Database.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getNome());
            ps.setString(2, item.getCategoria());
            ps.setString(3, item.getDescricao());
            ps.setInt(4, item.getQuantidade());
            ps.setString(5, item.getImagemPath());
            ps.setInt(6, item.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar item: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM itens WHERE id=?";
        try (Connection conn = Database.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir item: " + e.getMessage());
            return false;
        }
    }
}
