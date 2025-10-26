package com.sistema.cadastro.dto;

import lombok.Data;

@Data
public class ProdutoResponseDTO {

    private String nome;
    private String descricao;
    private int quantidade;
    private double valor;

}
