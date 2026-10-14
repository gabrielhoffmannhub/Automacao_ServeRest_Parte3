package br.com.serverest.tests;

import br.com.serverest.factories.UsuarioFactory;
import br.com.serverest.models.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Map;
import java.util.UUID;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class UsuariosTest extends BaseTest {
    @Test
    void deveCadastrarEConsultarUsuario() {
        Usuario usuario = UsuarioFactory.valido();
        String id = cadastrar(usuario);
        api.buscarUsuario(id).then().statusCode(200)
                .body("nome", equalTo(usuario.nome()))
                .body("email", equalTo(usuario.email()))
                .body("administrador", equalTo(usuario.administrador()))
                .body(matchesJsonSchemaInClasspath("schemas/usuario.json"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    void deveCadastrarUsuarioComAmbosPerfis(String administrador) {
        Usuario usuario = UsuarioFactory.validoComPerfil(administrador);
        String id = cadastrar(usuario);
        api.buscarUsuario(id).then().statusCode(200)
                .body("administrador", equalTo(administrador));
    }

    @Test
    void naoDevePermitirEmailDuplicado() {
        Usuario usuario = UsuarioFactory.valido();
        cadastrar(usuario);
        api.cadastrarUsuario(usuario).then().statusCode(400)
                .body("message", equalTo("Este email já está sendo usado"));
    }

    @Test
    void deveRejeitarUsuarioSemEmail() {
        Map<String, Object> incompleto = Map.of(
                "nome", "QA Sem Email", "password", "123456", "administrador", "false");
        api.cadastrarUsuario(incompleto).then().statusCode(400);
    }

    @Test
    void deveRetornarErroParaUsuarioInexistente() {
        api.buscarUsuario(idInexistente())
                .then().log().all().statusCode(400)
                .body("message", equalTo("Usuário não encontrado"));
    }

    @Test
    void deveAtualizarUsuario() {
        Usuario original = UsuarioFactory.valido();
        String id = cadastrar(original);
        Usuario atualizado = Usuario.builder()
                .nome("QA Atualizado")
                .email(original.email())
                .password(original.password())
                .administrador("false")
                .build();
        api.atualizarUsuario(id, atualizado).then().statusCode(200)
                .body("message", equalTo("Registro alterado com sucesso"));
        api.buscarUsuario(id).then().statusCode(200)
                .body("nome", equalTo("QA Atualizado"))
                .body("administrador", equalTo("false"));
    }

    @Test
    void deveExcluirUsuario() {
        String id = cadastrar(UsuarioFactory.valido());
        api.excluirUsuario(id).then().statusCode(200)
                .body("message", equalTo("Registro excluído com sucesso"));
        api.buscarUsuario(id).then().statusCode(400);
    }

    @Test
    void deveFiltrarUsuarioPorEmail() {
        Usuario usuario = UsuarioFactory.valido();
        cadastrar(usuario);
        api.listarUsuarios(Map.of("email", usuario.email())).then().statusCode(200)
                .body("quantidade", equalTo(1))
                .body("usuarios.email", hasItem(usuario.email()));
    }
}
