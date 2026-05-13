package auth;

import shared.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAO {

    public void criarTabela() {
        String sql = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome_usuario TEXT NOT NULL UNIQUE,
                    senha        TEXT NOT NULL
                )
                """;
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela usuarios: " + e.getMessage());
        }
    }

    public boolean salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome_usuario, senha) VALUES (?, ?)";
        try (Connection conn = Database.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNomeUsuario());
            ps.setString(2, usuario.getSenha());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar usuario: " + e.getMessage());
            return false;
        }
    }

    public Usuario buscarPorNome(String nomeUsuario) {
        String sql = "SELECT id, nome_usuario, senha FROM usuarios WHERE nome_usuario = ?";
        try (Connection conn = Database.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomeUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome_usuario"),
                        rs.getString("senha")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuario: " + e.getMessage());
        }
        return null;
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT id, nome_usuario, senha FROM usuarios WHERE id = ?";
        try (Connection conn = Database.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome_usuario"),
                        rs.getString("senha")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuario por id: " + e.getMessage());
        }
        return null;
    }

    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome_usuario = ?, senha = ? WHERE id = ?";
        try (Connection conn = Database.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNomeUsuario());
            ps.setString(2, usuario.getSenha());
            ps.setInt(3, usuario.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuario: " + e.getMessage());
            return false;
        }
    }
}
