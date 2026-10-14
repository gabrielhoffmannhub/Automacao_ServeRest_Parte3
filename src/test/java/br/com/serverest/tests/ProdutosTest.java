package br.com.serverest.tests;

import br.com.serverest.factories.ProdutoFactory;
import br.com.serverest.factories.UsuarioFactory;
import br.com.serverest.models.Produto;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class ProdutosTest extends BaseTest {
    @Test
    void deveCadastrarEConsultarProduto() {
        String admin = autenticar(UsuarioFactory.validoComPerfil("true"));
        Produto produto = ProdutoFactory.valido();
        String id = cadastrarProduto(produto, admin);
        api.buscarProduto(id).then().statusCode(200)
                .body("nome", equalTo(produto.nome()))
                .body("preco", equalTo(produto.preco()))
                .body("quantidade", equalTo(produto.quantidade()))
                .body(matchesJsonSchemaInClasspath("schemas/produto.json"));
    }

    @Test
    void naoDeveCadastrarProdutoComNomeDuplicado() {
        String admin = autenticar(UsuarioFactory.validoComPerfil("true"));
        Produto produto = ProdutoFactory.valido();
        cadastrarProduto(produto, admin);
        api.cadastrarProduto(produto, admin).then().statusCode(400)
                .body("message", equalTo("Já existe produto com esse nome"));
    }

    @Test
    void naoDeveCadastrarProdutoSemAutenticacao() {
        api.cadastrarProduto(ProdutoFactory.valido(), null)
                .then().statusCode(401);
    }

    @Test
    void naoDeveCadastrarProdutoComoUsuarioComum() {
        String tokenUsuario = autenticar(UsuarioFactory.validoComPerfil("false"));
        api.cadastrarProduto(ProdutoFactory.valido(), tokenUsuario).then().statusCode(403)
                .body("message", equalTo("Rota exclusiva para administradores"));
    }

    @Test
    void deveAtualizarProduto() {
        String admin = autenticar(UsuarioFactory.validoComPerfil("true"));
        String id = cadastrarProduto(ProdutoFactory.valido(), admin);
        Produto atualizado = ProdutoFactory.valido();
        api.atualizarProduto(id, atualizado, admin).then().statusCode(200)
                .body("message", equalTo("Registro alterado com sucesso"));
        api.buscarProduto(id).then().statusCode(200)
                .body("nome", equalTo(atualizado.nome()))
                .body("preco", equalTo(atualizado.preco()));
    }

    @Test
    void deveExcluirProduto() {
        String admin = autenticar(UsuarioFactory.validoComPerfil("true"));
        String id = cadastrarProduto(ProdutoFactory.valido(), admin);
        api.excluirProduto(id, admin).then().statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));
        api.buscarProduto(id).then().statusCode(400);
    }

    @Test
    void deveRetornarErroParaProdutoInexistente() {
        api.buscarProduto(idInexistente())
                .then().statusCode(400)
                .body("message", equalTo("Produto não encontrado"));
    }

    @Test
    void deveFiltrarProdutosPorNome() {
        String admin = autenticar(UsuarioFactory.validoComPerfil("true"));
        Produto produto = ProdutoFactory.valido();
        cadastrarProduto(produto, admin);
        api.listarProdutos(Map.of("nome", produto.nome())).then().statusCode(200)
                .body("quantidade", equalTo(1))
                .body("produtos.nome", hasItem(produto.nome()));
    }
}
