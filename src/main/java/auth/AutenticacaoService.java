package auth;

public class AutenticacaoService {

    private final UsuarioDAO usuarioDAO;

    public AutenticacaoService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario autenticar(String nomeUsuario, String senha) {
        if (nomeUsuario == null || nomeUsuario.isBlank()) return null;
        if (senha == null || senha.isBlank()) return null;

        Usuario usuarioNoBanco = usuarioDAO.buscarPorNome(nomeUsuario);
        if (usuarioNoBanco == null) return null;

        if (usuarioNoBanco.getSenha().equals(senha)) {
            return usuarioNoBanco;
        }
        return null;
    }

    public boolean cadastrar(String nomeUsuario, String senha) {
        if (nomeUsuario == null || nomeUsuario.isBlank()) return false;
        if (senha == null || senha.isBlank()) return false;
        if (usuarioDAO.buscarPorNome(nomeUsuario) != null) return false;

        return usuarioDAO.salvar(new Usuario(nomeUsuario, senha));
    }

    public ResultadoEdicao atualizarPerfil(int usuarioId,
                                            String senhaAtual,
                                            String novoNomeUsuario,
                                            String novaSenha) {

        if (novoNomeUsuario == null || novoNomeUsuario.isBlank()) {
            return ResultadoEdicao.DADOS_INVALIDOS;
        }
        if (senhaAtual == null || senhaAtual.isBlank()) {
            return ResultadoEdicao.DADOS_INVALIDOS;
        }

        Usuario usuarioAtual = usuarioDAO.buscarPorId(usuarioId);
        if (usuarioAtual == null) {
            return ResultadoEdicao.DADOS_INVALIDOS;
        }

        if (!usuarioAtual.getSenha().equals(senhaAtual)) {
            return ResultadoEdicao.SENHA_INCORRETA;
        }

        Usuario outroComMesmoNome = usuarioDAO.buscarPorNome(novoNomeUsuario);
        if (outroComMesmoNome != null && outroComMesmoNome.getId() != usuarioId) {
            return ResultadoEdicao.USUARIO_JA_EXISTE;
        }

        String senhaParaSalvar = (novaSenha == null || novaSenha.isBlank())
                ? usuarioAtual.getSenha()
                : novaSenha;

        usuarioAtual.setNomeUsuario(novoNomeUsuario.trim());
        usuarioAtual.setSenha(senhaParaSalvar);

        if (!usuarioDAO.atualizar(usuarioAtual)) {
            return ResultadoEdicao.DADOS_INVALIDOS;
        }

        Sessao.getInstancia().login(usuarioAtual);
        return ResultadoEdicao.SUCESSO;
    }
}
