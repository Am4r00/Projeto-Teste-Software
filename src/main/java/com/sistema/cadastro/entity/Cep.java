package com.sistema.cadastro.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cep")
@Data
public class Cep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cep;
    private String logradouro;
    private String localidade;
    private String uf;
    private String estado;

}
