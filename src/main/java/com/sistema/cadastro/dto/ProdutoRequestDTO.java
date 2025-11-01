package com.sistema.cadastro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    private String descricao;
    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior que 0")
    private int quantidade;
    @Min(value = 0, message = "O preço não pode ser negativo")
    private double valor;

}
