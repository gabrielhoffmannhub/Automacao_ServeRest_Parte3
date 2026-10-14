package br.com.serverest.models;

import java.util.List;

public record Carrinho(List<ItemCarrinho> produtos) {
    public List<ItemCarrinho> getProdutos() { return produtos; }

    public static Carrinho comProduto(String idProduto, int quantidade) {
        return new Carrinho(List.of(new ItemCarrinho(idProduto, quantidade)));
    }
}
