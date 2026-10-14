package br.com.serverest.factories;

import br.com.serverest.models.Usuario;
import net.datafaker.Faker;
import java.util.UUID;

public final class UsuarioFactory {
    private static final Faker FAKER = new Faker();
    private UsuarioFactory() {}

    public static Usuario valido() { return validoComPerfil("true"); }

    public static Usuario validoComPerfil(String administrador) {
        String email = "qa-" + UUID.randomUUID() + "@teste.com";
        return Usuario.builder()
                .nome(FAKER.name().fullName())
                .email(email)
                .administrador(administrador)
                .build();
    }
}
