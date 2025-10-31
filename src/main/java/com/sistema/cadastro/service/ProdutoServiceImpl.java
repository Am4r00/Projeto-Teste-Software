package com.sistema.cadastro.service;

import com.sistema.cadastro.dto.ProdutoRequestDTO;
import com.sistema.cadastro.entity.Produto;
import com.sistema.cadastro.mapper.ProdutoMapper;
import com.sistema.cadastro.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    public ProdutoServiceImpl(ProdutoRepository repository, ProdutoMapper produtoMapper) {
        this.repository = repository;
        this.mapper = produtoMapper;
    }

    @Override
    public Produto criarProduto(ProdutoRequestDTO produto) {
        Produto p = mapper.toEntity(produto);

        return repository.save(p);
    }

    @Override
    public List<Produto> listarProdutos() {
        return repository.findAll();
    }

    @Override
    public ResponseEntity<Produto> atualizarProduto(Long id, ProdutoRequestDTO novoProduto) {

        Produto p = repository.findById(id).orElse(null);

        if(p == null){
            return ResponseEntity.notFound().build();
        }

        mapper.updateProdutoFromDto(novoProduto, p);
        Produto produtoAtualizado = repository.save(p);
        return ResponseEntity.ok(produtoAtualizado);

    }

    @Override
    public ResponseEntity<Produto> deletarProduto(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
