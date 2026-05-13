package auth;

public final class Sessao {

    private static Sessao instancia;

    private Usuario usuarioLogado;

    private Sessao() {}

    public static Sessao getInstancia() {
        if (instancia == null) {
            instancia = new Sessao();
        }
        return instancia;
    }

    public void login(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void logout() {
        this.usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean estaLogado() {
        return usuarioLogado != null;
    }
}
