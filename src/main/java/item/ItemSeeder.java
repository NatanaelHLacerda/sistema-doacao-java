package item;

import java.util.List;

public final class ItemSeeder {

    private ItemSeeder() {}

    public static void popularSeVazio() {
        ItemService service = new ItemService();
        if (!service.listarTodos().isEmpty()) return;

        List<Item> exemplos = List.of(
                new Item("Camiseta básica", "Roupas", "Camiseta de algodão, tamanho M, várias cores disponíveis.", 12),
                new Item("Jaqueta de inverno", "Roupas", "Jaqueta acolchoada, ideal para baixas temperaturas.", 4),
                new Item("Calça jeans", "Roupas", "Calça jeans masculina, tamanho 42, pouco uso.", 6),
                new Item("Livro Java POO", "Livros", "Edição usada, em bom estado, ótimo para estudantes.", 3),
                new Item("Coleção de romances", "Livros", "Conjunto com 5 livros de autores nacionais.", 1),
                new Item("Dicionário inglês", "Livros", "Oxford Pocket, capa dura, 2 unidades novas.", 2),
                new Item("Mochila escolar", "Acessórios", "Mochila resistente, vários compartimentos.", 5),
                new Item("Tênis esportivo", "Calçados", "Tamanho 40, levemente usado, sem rasgos.", 2),
                new Item("Cobertor de casal", "Casa", "Cobertor de microfibra, lavado e higienizado.", 8),
                new Item("Conjunto de pratos", "Casa", "Jogo de 6 pratos rasos em porcelana branca.", 6),
                new Item("Liquidificador", "Eletrônicos", "Funcionando perfeitamente, 3 velocidades.", 1),
                new Item("Smartphone usado", "Eletrônicos", "Tela de 6 polegadas, bateria boa, com carregador.", 1),
                new Item("Brinquedo educativo", "Brinquedos", "Quebra-cabeça de madeira para crianças 3+.", 9),
                new Item("Carrinho de controle", "Brinquedos", "Funciona com pilhas, controle remoto incluso.", 2),
                new Item("Caixa de fraldas", "Higiene", "Pacote fechado, tamanho G, 40 unidades.", 3),
                new Item("Kit shampoo + sabonete", "Higiene", "Produtos novos, lacrados, hipoalergênicos.", 10),
                new Item("Cesta básica", "Alimentos", "Itens não perecíveis: arroz, feijão, óleo, açúcar.", 4),
                new Item("Pacote de fraldas geriátricas", "Higiene", "Pacote com 20 unidades, tamanho M.", 2)
        );

        for (Item item : exemplos) {
            service.cadastrar(item.getNome(), item.getCategoria(), item.getDescricao(), item.getQuantidade());
        }
    }
}
