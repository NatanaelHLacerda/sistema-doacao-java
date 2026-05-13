package doador;

import java.util.Objects;

public class Doador {

    private String nome;
    private String cpf;
    private String email;

    public Doador(String nome, String cpf, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doador)) return false;
        Doador outro = (Doador) o;
        return Objects.equals(cpf, outro.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public String toString() {
        return "Doador{" + "nome='" + nome + "', cpf='" + cpf + "'}";
    }
}
