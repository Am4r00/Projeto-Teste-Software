package com.sistema.cadastro.service;

import com.sistema.cadastro.dto.ProdutoRequestDTO;
import com.sistema.cadastro.entity.Produto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProdutoService {

    Produto criarProduto(ProdutoRequestDTO produto);
    List<Produto> listarProdutos();
    Produto atualizarProduto(Long id, ProdutoRequestDTO produto);
    ResponseEntity<Produto> deletarProduto(Long id);

}
