package br.com.serverest.tests;

import br.com.serverest.models.Produto;
import br.com.serverest.models.Usuario;
import br.com.serverest.services.ApiClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

public abstract class BaseTest {
    protected ApiClient api;
    private final List<String> usuariosCriados = new ArrayList<>();
    private final List<String> tokensCriados = new ArrayList<>();
    private final Map<String, String> produtosCriados = new LinkedHashMap<>();

    @BeforeEach
    void preparar() {
        api = new ApiClient();
    }

    protected String cadastrar(Usuario usuario) {
        String id = api.cadastrarUsuario(usuario).then().statusCode(201).extract().path("_id");
        usuariosCriados.add(id);
        return id;
    }

    protected String autenticar(Usuario usuario) {
        cadastrar(usuario);
        String token = api.login(usuario.email(), usuario.password())
                .then().statusCode(200).extract().path("authorization");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Login de preparação não retornou token");
        }
        tokensCriados.add(token);
        return token;
    }

    protected String cadastrarProduto(Produto produto, String tokenAdmin) {
        String id = api.cadastrarProduto(produto, tokenAdmin)
                .then().statusCode(201).extract().path("_id");
        produtosCriados.put(id, tokenAdmin);
        return id;
    }

    protected String cadastrarCarrinho(Object carrinho, String token) {
        return api.cadastrarCarrinho(carrinho, token)
                .then().statusCode(201).extract().path("_id");
    }

    private void tentarLimpar(String acao, java.util.function.Supplier<Response> requisicao) {
        try {
            Response resposta = requisicao.get();
            if (resposta.statusCode() >= 400) {
                System.err.println("Limpeza: " + acao + " retornou HTTP " + resposta.statusCode());
            }
        } catch (Exception erro) {
            System.err.println("Limpeza: falhou " + acao + ": " + erro.getMessage());
        }
    }

    @AfterEach
    void limpar() {
        // Carrinhos podem impedir exclusão de usuários e produtos.
        for (String token : tokensCriados) {
            tentarLimpar("cancelar carrinho", () -> api.cancelarCompra(token));
        }
        produtosCriados.forEach((id, token) ->
                tentarLimpar("excluir produto " + id, () -> api.excluirProduto(id, token)));
        for (String id : usuariosCriados) {
            tentarLimpar("excluir usuário " + id, () -> api.excluirUsuario(id));
        }
        tokensCriados.clear();
        produtosCriados.clear();
        usuariosCriados.clear();
    }

    protected String idInexistente() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 16);
    }
}
