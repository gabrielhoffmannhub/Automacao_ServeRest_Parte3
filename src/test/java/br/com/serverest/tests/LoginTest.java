package br.com.serverest.tests;

import br.com.serverest.factories.UsuarioFactory;
import br.com.serverest.models.Usuario;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.UUID;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginTest extends BaseTest {
    @Test
    void deveRealizarLoginComSucesso() {
        Usuario usuario = UsuarioFactory.valido();
        String id = cadastrar(usuario);
        String token = api.login(usuario.email(), usuario.password())
                .then().statusCode(200).extract().path("authorization");
        assertNotNull(token);
        assertFalse(token.isBlank());
        api.buscarUsuarioAutenticado(id, token).then().statusCode(200)
                .body("nome", equalTo(usuario.nome()))
                .body("email", equalTo(usuario.email()));
    }

    @Test
    void naoDeveAutenticarSenhaIncorreta() {
        Usuario usuario = UsuarioFactory.valido();
        cadastrar(usuario);
        api.login(usuario.email(), "senha-incorreta")
                .then().statusCode(401)
                .body("message", equalTo("Email e/ou senha inválidos"));
    }

    @Test
    void naoDeveAutenticarUsuarioInexistente() {
        api.login("nao-existe-" + UUID.randomUUID() + "@teste.com", "123456")
                .then().statusCode(401)
                .body("message", equalTo("Email e/ou senha inválidos"));
    }

    @Test
    void deveRejeitarLoginSemSenha() {
        api.login(Map.of("email", "exemplo@teste.com"))
                .then().statusCode(400);
    }
}
