package com.sistema.cadastro.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CepClient {
    private final WebClient webClient;

    public CepClient(@Value("${external.cep.api.url:https://viacep.com.br/ws}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<CepResponse> buscarCep(String cep) {
        String cleanCep = cep.replaceAll("[^0-9]", "");

        return webClient.get()
                .uri("/{cep}/json", cleanCep)
                .retrieve()
                .bodyToMono(CepResponse.class)
                .filter(response -> response.getErro() == null)
                .switchIfEmpty(Mono.error(new RuntimeException("CEP não encontrado")));
    }
}
