package com.sistema.cadastro.produto.service;


import com.sistema.cadastro.produto.Produto;
import com.sistema.cadastro.dto.ProdutoRequestDTO;

import com.sistema.cadastro.repository.ProdutoRepository;
import com.sistema.cadastro.service.ProdutoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class ProdutoServiceImplIT {

    @Autowired
    private ProdutoServiceImpl service; // bean real

    @Autowired
    private ProdutoRepository repository; // só pra validar

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    @Test
    void criarProduto_end2end() {
        var dto = new ProdutoRequestDTO("tv", "50 polegadas", 2, 2500.0);

        Produto criado = service.criarProduto(dto);

        assertNotNull(criado);
        assertNotNull(criado.getId());
        assertEquals("tv", criado.getNome());
        assertTrue(repository.existsById(criado.getId()));
    }
}
