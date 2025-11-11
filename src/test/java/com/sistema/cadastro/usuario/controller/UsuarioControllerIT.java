package com.sistema.cadastro.usuario.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.testcontainers.utility.TestcontainersConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsuarioControllerIT {

    static WireMockServer wireMock = new WireMockServer(options().dynamicPort());

    @org.springframework.beans.factory.annotation.Autowired
    TestRestTemplate rest;

    @org.springframework.test.context.DynamicPropertySource
    static void props(org.springframework.test.context.DynamicPropertyRegistry r) {
        wireMock.start();
        r.add("cep.base-url", wireMock::baseUrl);
    }

    @BeforeAll
    void stubs() {
        wireMock.stubFor(get(urlEqualTo("/01310100"))
                .willReturn(aResponse().withHeader("Content-Type","application/json")
                        .withBody("""
                {"cep":"01310100","logradouro":"Avenida Paulista","localidade":"São Paulo","uf":"SP"}
            """)));
    }

    @AfterAll void stop(){ wireMock.stop(); }

    @Test
    void postUsuarios_criaComSucesso() {
        var body = """
        {
          "nome":"João Silva",
          "email":"joao@example.com",
          "cpf":"12345678901",
          "cep":"01310100"
        }
        """;
        var resp = rest.postForEntity("/usuarios", body, String.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode()); // ou CREATED, conforme seu controller
    }
}
