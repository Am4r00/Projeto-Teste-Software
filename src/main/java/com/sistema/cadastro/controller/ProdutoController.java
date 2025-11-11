package com.sistema.cadastro.controller;

import com.sistema.cadastro.dto.ProdutoRequestDTO;
import com.sistema.cadastro.produto.Produto;
import com.sistema.cadastro.service.ProdutoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Produto> criar(@Valid @RequestBody ProdutoRequestDTO dto){
        try {
            Produto p = service.criarProduto(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(p);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    public ResponseEntity<List<Produto>> listar(){
        return ResponseEntity.ok(service.listarProdutos());
    }

    @PutMapping("{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id,@Valid @RequestBody ProdutoRequestDTO dto){
        try {
            return ResponseEntity.ok(service.atualizarProduto(id,dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Produto> deletar(@PathVariable Long id){
        try {
            service.deletarProduto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}