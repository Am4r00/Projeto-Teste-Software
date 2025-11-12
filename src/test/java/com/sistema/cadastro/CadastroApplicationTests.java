package com.sistema.cadastro;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import static org.junit.Assert.assertNotNull;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CadastroApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Disabled("Ignorar durante o desenvolvimento")
    void carregandoContexto() {
       assertNotNull(context);
    }
}