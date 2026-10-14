package br.com.serverest.models;

public record Produto(String nome, int preco, String descricao, int quantidade) {
    public String getNome() { return nome; }
    public int getPreco() { return preco; }
    public String getDescricao() { return descricao; }
    public int getQuantidade() { return quantidade; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String nome;
        private int preco = 120;
        private String descricao = "Produto gerado para testes";
        private int quantidade = 10;

        public Builder nome(String valor) { nome = valor; return this; }
        public Builder preco(int valor) { preco = valor; return this; }
        public Builder descricao(String valor) { descricao = valor; return this; }
        public Builder quantidade(int valor) { quantidade = valor; return this; }
        public Produto build() { return new Produto(nome, preco, descricao, quantidade); }
    }
}
