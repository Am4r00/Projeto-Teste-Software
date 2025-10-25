package com.sistema.cadastro.service;

import com.sistema.cadastro.dto.ProdutoRequestDTO;
import com.sistema.cadastro.entity.Produto;
import com.sistema.cadastro.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    ProdutoRepository repository;

    public ProdutoServiceImpl(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Produto criarProduto(ProdutoRequestDTO produto) {
        Produto p = new Produto();
        p.setNome(produto.getNome());
        p.setDescricao(produto.getDescricao());
        p.setQuantidade(produto.getQuantidade());
        p.setValor(produto.getValor());

        return repository.save(p);
    }

    @Override
    public List<Produto> listarProdutos() {
        return repository.findAll();
    }

    @Override
    public Produto atualizarProduto(Long id, ProdutoRequestDTO novoProduto) {

        Produto p = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
            p.setNome(novoProduto.getNome());
            p.setDescricao(novoProduto.getDescricao());
            p.setQuantidade(novoProduto.getQuantidade());
            p.setValor(novoProduto.getValor());

            return repository.save(p);
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
