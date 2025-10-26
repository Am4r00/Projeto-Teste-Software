package com.sistema.cadastro.controller;

import com.sistema.cadastro.dto.ProdutoRequestDTO;
import com.sistema.cadastro.entity.Produto;
import com.sistema.cadastro.service.ProdutoServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    ProdutoServiceImpl service;

    public ProdutoController(ProdutoServiceImpl service) {
        this.service = service;
    }

    @PostMapping()
    public Produto criar(@RequestBody ProdutoRequestDTO dto){
        return service.criarProduto(dto);
    }

    @GetMapping()
    public List<Produto> listar(){
        return service.listarProdutos();
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto){
        return service.atualizarProduto(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Produto> deletar(@PathVariable Long id){
        return service.deletarProduto(id);
    }
}