package item;

public class Item {

    private int id;
    private String nome;
    private String categoria;
    private String descricao;
    private int quantidade;

    public Item(String nome, String categoria, String descricao, int quantidade) {
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public Item(int id, String nome, String categoria, String descricao, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    @Override
    public String toString() {
        return "Item{id=" + id + ", nome='" + nome + "', quantidade=" + quantidade + "}";
    }
}
