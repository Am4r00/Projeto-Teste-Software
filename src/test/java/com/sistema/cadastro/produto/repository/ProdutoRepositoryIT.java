package com.sistema.cadastro.produto.repository;


import com.sistema.cadastro.produto.Produto;
import com.sistema.cadastro.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = NONE)
class ProdutoRepositoryIT {

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
        assertNotNull(salvo.getId());

        Optional<Produto> achado = repository.findById(salvo.getId());
        assertTrue(achado.isPresent());
        assertEquals("tv", achado.get().getNome());
    }
}
