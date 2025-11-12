package com.sistema.cadastro.bdd;

import io.cucumber.java.pt.*;
import io.cucumber.spring.CucumberContextConfiguration;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProdutoSteps {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    private ResponseEntity<Map> respostaCriacao;
    private ResponseEntity<List> respostaLista;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @Dado("que a API está no ar")
    public void apiNoAr() {
        // Verifica que o endpoint de listagem responde (mesmo com lista vazia)
        ResponseEntity<String> r = rest.getForEntity(baseUrl() + "/api/produtos", String.class);
        Assertions.assertThat(r.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Quando("eu cadastro um produto válido")
    public void cadastroProdutoValido() {
        Map<String, Object> body = new HashMap<>();
        body.put("nome", "Caderno");
        body.put("descricao", "200 folhas");
        body.put("quantidade", 10);
        body.put("valor", 12.5);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        respostaCriacao = rest.postForEntity(
                baseUrl() + "/api/produtos",
                new HttpEntity<>(body, headers),
                Map.class
        );
    }

    @Então("a resposta HTTP deve ser {int}")
    public void respostaHttpDeveSer(int status) {
        Assertions.assertThat(respostaCriacao.getStatusCodeValue()).isEqualTo(status);
    }

    @Então("o produto retornado deve ter id gerado")
    public void produtoRetornadoDeveTerId() {
        Map body = respostaCriacao.getBody();
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.get("id")).isNotNull();
    }

    @Quando("eu consulto a lista de produtos")
    public void consultoListaProdutos() {
        respostaLista = rest.getForEntity(baseUrl() + "/api/produtos", List.class);
        Assertions.assertThat(respostaLista.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Então("deve conter um produto com nome {string}")
    public void deveConterProdutoComNome(String nome) {
        List list = respostaLista.getBody();
        Assertions.assertThat(list).isNotNull();
        boolean found = false;

        for (Object o : list) {
            if (o instanceof Map<?, ?> m) {
                Object n = m.get("nome");
                if (n != null && n.toString().equals(nome)) {
                    found = true;
                    break;
                }
            }
        }

        Assertions.assertThat(found)
                .as("Lista deve conter produto com nome " + nome)
                .isTrue();
    }
}
