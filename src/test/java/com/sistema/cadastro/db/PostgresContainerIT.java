package com.sistema.cadastro.db;

import com.sistema.cadastro.produto.Produto;
import com.sistema.cadastro.repository.ProdutoRepository;
import com.sistema.cadastro.TestcontainersConfiguration;
import org.junit.jupiter.api.Assertions; // <--- use isto
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;               // <--- traga o container
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TestcontainersConfiguration.class)
class PostgresContainerIT {

    @Autowired
    private ProdutoRepository repository;

    @Test
    void salvarEBuscar() {
        var p = new Produto();
        p.setNome("tv");
        p.setDescricao("50 polegadas");
        p.setQuantidade(2);
        p.setValor(2500.0);

        var salvo = repository.save(p);
        Assertions.assertNotNull(salvo.getId());

        Optional<Produto> achado = repository.findById(salvo.getId());
        Assertions.assertTrue(achado.isPresent());
        Assertions.assertEquals("tv", achado.get().getNome());
    }
}
