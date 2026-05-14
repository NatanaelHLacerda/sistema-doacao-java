package item;

public class Item {

    private int id;
    private String nome;
    private String categoria;
    private String descricao;
    private int quantidade;
    private String imagemPath;

    public Item(String nome, String categoria, String descricao, int quantidade) {
        this(0, nome, categoria, descricao, quantidade, null);
    }

    public Item(int id, String nome, String categoria, String descricao, int quantidade) {
        this(id, nome, categoria, descricao, quantidade, null);
    }

    public Item(String nome, String categoria, String descricao, int quantidade, String imagemPath) {
        this(0, nome, categoria, descricao, quantidade, imagemPath);
    }

    public Item(int id, String nome, String categoria, String descricao, int quantidade, String imagemPath) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.imagemPath = imagemPath;
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

    public String getImagemPath() { return imagemPath; }
    public void setImagemPath(String imagemPath) { this.imagemPath = imagemPath; }

    @Override
    public String toString() {
        return "Item{id=" + id + ", nome='" + nome + "', quantidade=" + quantidade + "}";
    }
}
