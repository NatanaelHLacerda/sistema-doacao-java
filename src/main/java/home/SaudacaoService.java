package home;

public class SaudacaoService {

    public String saudar(String nome) {
        if (nome == null || nome.isBlank()) {
            return "Digite seu nome.";
        }
        return "Olá, " + nome + "! Bem-vindo ao sistema de doação.";
    }
}
