package br.com.serverest.tests;

import br.com.serverest.factories.ProdutoFactory;
import br.com.serverest.factories.UsuarioFactory;
import br.com.serverest.models.Carrinho;
import br.com.serverest.models.Produto;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.UUID;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class CarrinhosTest extends BaseTest {
    @Test
    void deveCriarConsultarECancelarCarrinhoRestaurandoEstoque() {
        String admin = autenticar(UsuarioFactory.validoComPerfil("true"));
        Produto produto = ProdutoFactory.valido();
        String idProduto = cadastrarProduto(produto, admin);
        String comprador = autenticar(UsuarioFactory.validoComPerfil("false"));

        String idCarrinho = cadastrarCarrinho(Carrinho.comProduto(idProduto, 2), comprador);
        api.buscarCarrinho(idCarrinho).then().statusCode(200)
                .body("quantidadeTotal", equalTo(2))
                .body("precoTotal", equalTo(produto.preco() * 2))
                .body("produtos[0].idProduto", equalTo(idProduto))
                .body(matchesJsonSchemaInClasspath("schemas/carrinho.json"));
        api.buscarProduto(idProduto).then().statusCode(200)
                .body("quantidade", equalTo(produto.quantidade() - 2));

        api.cancelarCompra(comprador).then().statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso. Estoque dos produtos reabastecido"));
        api.buscarProduto(idProduto).then().statusCode(200)
                .body("quantidade", equalTo(produto.quantidade()));
        api.buscarCarrinho(idCarrinho).then().statusCode(400);
    }

    @Test
    void deveConcluirCompraESubtrairEstoque() {
        String admin = autenticar(UsuarioFactory.validoComPerfil("true"));
        Produto produto = ProdutoFactory.valido();
        String idProduto = cadastrarProduto(produto, admin);
        String comprador = autenticar(UsuarioFactory.validoComPerfil("false"));
        String idCarrinho = cadastrarCarrinho(Carrinho.comProduto(idProduto, 2), comprador);

        api.concluirCompra(comprador).then().statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));
        api.buscarCarrinho(idCarrinho).then().statusCode(400);
        api.buscarProduto(idProduto).then().statusCode(200)
                .body("quantidade", equalTo(produto.quantidade() - 2));
    }

    @Test
    void naoDeveCriarCarrinhoSemAutenticacao() {
        api.cadastrarCarrinho(Carrinho.comProduto("produto-ficticio", 1), null)
                .then().statusCode(401);
    }

    @Test
    void naoDevePermitirDoisCarrinhosParaMesmoUsuario() {
        String admin = autenticar(UsuarioFactory.validoComPerfil("true"));
        String idProduto = cadastrarProduto(ProdutoFactory.valido(), admin);
        String comprador = autenticar(UsuarioFactory.validoComPerfil("false"));
        cadastrarCarrinho(Carrinho.comProduto(idProduto, 1), comprador);
        api.cadastrarCarrinho(Carrinho.comProduto(idProduto, 1), comprador)
                .then().statusCode(400)
                .body("message", equalTo("Não é permitido ter mais de 1 carrinho"));
    }

    @Test
    void naoDeveCriarCarrinhoSemEstoqueSuficiente() {
        String admin = autenticar(UsuarioFactory.validoComPerfil("true"));
        Produto produto = ProdutoFactory.valido();
        String idProduto = cadastrarProduto(produto, admin);
        String comprador = autenticar(UsuarioFactory.validoComPerfil("false"));
        api.cadastrarCarrinho(Carrinho.comProduto(idProduto, produto.quantidade() + 1), comprador)
                .then().statusCode(400)
                .body("message", equalTo("Produto não possui quantidade suficiente"));
    }

    @Test
    void naoDeveCriarCarrinhoComProdutoInexistente() {
        String comprador = autenticar(UsuarioFactory.validoComPerfil("false"));
        api.cadastrarCarrinho(Carrinho.comProduto("inexistente-" + UUID.randomUUID(), 1), comprador)
                .then().statusCode(400)
                .body("message", equalTo("Produto não encontrado"));
    }

    @Test
    void deveFiltrarCarrinhosPeloUsuario() {
        String admin = autenticar(UsuarioFactory.validoComPerfil("true"));
        String idProduto = cadastrarProduto(ProdutoFactory.valido(), admin);
        var usuario = UsuarioFactory.validoComPerfil("false");
        String comprador = autenticar(usuario);
        String idCarrinho = cadastrarCarrinho(Carrinho.comProduto(idProduto, 1), comprador);
        api.listarCarrinhos(Map.of("_id", idCarrinho)).then().statusCode(200)
                .body("quantidade", equalTo(1))
                .body("carrinhos._id", hasItem(idCarrinho));
    }

    @Test
    void deveRetornarErroParaCarrinhoInexistente() {
        api.buscarCarrinho(idInexistente())
                .then().statusCode(400)
                .body("message", equalTo("Carrinho não encontrado"));
    }
}
