package com.sistema.cadastro.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {

    @Test
    public void novoProdutoTeste(){
        Produto produto = new Produto();
        produto.setNome("Tv");
        produto.setDescricao("50 polegadas");
        produto.setQuantidade(2);
        produto.setValor(2500);

        assertEquals("Tv", produto.getNome());
        assertEquals("50 polegadas", produto.getDescricao());
        assertEquals(2, produto.getQuantidade());
        assertEquals(2500, produto.getValor());
    }

}
