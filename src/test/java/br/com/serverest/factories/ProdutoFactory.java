package br.com.serverest.factories;

import br.com.serverest.models.Produto;
import net.datafaker.Faker;
import java.util.UUID;

public final class ProdutoFactory {
    private static final Faker FAKER = new Faker();
    private ProdutoFactory() {}

    public static Produto valido() {
        return Produto.builder()
                .nome("QA-" + FAKER.commerce().productName() + "-" + UUID.randomUUID())
                .descricao("Produto exclusivo para automacao")
                .preco(120)
                .quantidade(10)
                .build();
    }
}
