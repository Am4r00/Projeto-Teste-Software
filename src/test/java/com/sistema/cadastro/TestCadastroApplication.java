package com.sistema.cadastro;

import org.springframework.boot.SpringApplication;

public class TestCadastroApplication {

    public static void main(String[] args) {
        SpringApplication.from(CadastroApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}