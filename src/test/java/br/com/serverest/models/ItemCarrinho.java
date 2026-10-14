package br.com.serverest.models;

public record ItemCarrinho(String idProduto, int quantidade) {
    public String getIdProduto() { return idProduto; }
    public int getQuantidade() { return quantidade; }
}
