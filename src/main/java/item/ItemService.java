package item;

import java.util.List;

public class ItemService {

    private final ItemDAO itemDAO;

    public ItemService() {
        this.itemDAO = new ItemDAO();
    }

    public boolean cadastrar(String nome, String categoria, String descricao, int quantidade) {
        if (nome == null || nome.isBlank()) return false;
        if (categoria == null || categoria.isBlank()) return false;
        if (quantidade <= 0) return false;

        Item item = new Item(nome.trim(), categoria.trim(), descricao, quantidade);
        return itemDAO.salvar(item);
    }

    public boolean atualizar(Item item) {
        if (item.getNome() == null || item.getNome().isBlank()) return false;
        if (item.getCategoria() == null || item.getCategoria().isBlank()) return false;
        if (item.getQuantidade() <= 0) return false;

        return itemDAO.atualizar(item);
    }

    public boolean excluir(int id) {
        return itemDAO.excluir(id);
    }

    public List<Item> listarTodos() {
        return itemDAO.listarTodos();
    }
}
