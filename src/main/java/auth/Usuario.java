package auth;

import java.util.Objects;

public class Usuario {

    private int id;
    private String nomeUsuario;
    private String senha;

    public Usuario(String nomeUsuario, String senha) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
    }

    public Usuario(int id, String nomeUsuario, String senha) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario outro = (Usuario) o;
        return Objects.equals(nomeUsuario, outro.nomeUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeUsuario);
    }

    @Override
    public String toString() {
        return "Usuario{nomeUsuario='" + nomeUsuario + "'}";
    }
}
