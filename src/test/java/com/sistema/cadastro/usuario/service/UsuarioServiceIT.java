package com.sistema.cadastro.usuario.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.sistema.cadastro.produto.Usuario;

import com.sistema.cadastro.repository.UsuarioRepository;
import com.sistema.cadastro.service.UsuarioService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.utility.TestcontainersConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsuarioServiceIT {

    static WireMockServer wireMock = new WireMockServer(options().dynamicPort());

    @DynamicPropertySource
    static void dynamicProps(DynamicPropertyRegistry registry) {
        wireMock.start();
        // Ajuste AQUI para o nome da propriedade que o seu CepClient usa como base-url
        // Exemplo:
        registry.add("https://viacep.com.br/ws", wireMock::baseUrl);
    }

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void stubCep() {
        // Ajuste o path conforme o seu CepClient chama (ex.: GET /{cep})
        wireMock.stubFor(get(urlEqualTo("/01310100"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                    {
                      "cep":"01310100",
                      "logradouro":"Avenida Paulista",
                      "localidade":"São Paulo",
                      "uf":"SP"
                    }
                    """)));
    }

    @AfterAll
    void stop() {
        wireMock.stop();
    }

    @BeforeEach
    void clean() {
        usuarioRepository.deleteAll();
    }

    @Test
    void criarUsuario_comCepRealViaWireMock() {
        var u = new Usuario();
        u.setNome("João Silva");
        u.setEmail("joao@example.com");
        u.setCpf("12345678901");
        u.setCep("01310100");

        var criado = usuarioService.criar(u);

        assertNotNull(criado.getId());
        assertEquals("João Silva", criado.getNome());
        // Se o service enriquece endereço a partir do CEP, você pode validar campos
        // assertEquals("Avenida Paulista", criado.getLogradouro() /* ajuste conforme seu modelo */);
    }
}
