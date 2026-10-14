package br.com.serverest.services;

import br.com.serverest.config.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;


public class ApiClient {
    private RequestSpecification request() {
        return RestAssured.given().baseUri(Config.BASE_URL).accept(ContentType.JSON);
    }

    private RequestSpecification autenticada(String token) {
        RequestSpecification req = request();
        return token == null ? req : req.header("Authorization", token);
    }

    public Response cadastrarUsuario(Object usuario) {
        return request().contentType(ContentType.JSON).body(usuario).when().post("/usuarios");
    }

    public Response buscarUsuario(String id) {
        return request().when().get("/usuarios/{id}", id);
    }

    public Response buscarUsuarioAutenticado(String id, String token) {
        return autenticada(token).when().get("/usuarios/{id}", id);
    }

    public Response listarUsuarios(Map<String, ?> filtros) {
        return request().queryParams(filtros).when().get("/usuarios");
    }

    public Response atualizarUsuario(String id, Object usuario) {
        return request().contentType(ContentType.JSON).body(usuario).when().put("/usuarios/{id}", id);
    }

    public Response excluirUsuario(String id) {
        return request().when().delete("/usuarios/{id}", id);
    }

    public Response login(String email, String senha) {
        return login(Map.of("email", email, "password", senha));
    }

    public Response login(Object credenciais) {
        return request().contentType(ContentType.JSON).body(credenciais).when().post("/login");
    }

    public Response cadastrarProduto(Object produto, String token) {
        return autenticada(token).contentType(ContentType.JSON).body(produto).when().post("/produtos");
    }

    public Response buscarProduto(String id) {
        return request().when().get("/produtos/{id}", id);
    }

    public Response listarProdutos(Map<String, ?> filtros) {
        return request().queryParams(filtros).when().get("/produtos");
    }

    public Response atualizarProduto(String id, Object produto, String token) {
        return autenticada(token).contentType(ContentType.JSON).body(produto).when().put("/produtos/{id}", id);
    }

    public Response excluirProduto(String id, String token) {
        return autenticada(token).when().delete("/produtos/{id}", id);
    }

    public Response cadastrarCarrinho(Object carrinho, String token) {
        return autenticada(token).contentType(ContentType.JSON).body(carrinho).when().post("/carrinhos");
    }

    public Response buscarCarrinho(String id) {
        return request().when().get("/carrinhos/{id}", id);
    }

    public Response listarCarrinhos(Map<String, ?> filtros) {
        return request().queryParams(filtros).when().get("/carrinhos");
    }

    public Response concluirCompra(String token) {
        return autenticada(token).when().delete("/carrinhos/concluir-compra");
    }

    public Response cancelarCompra(String token) {
        return autenticada(token).when().delete("/carrinhos/cancelar-compra");
    }
}
