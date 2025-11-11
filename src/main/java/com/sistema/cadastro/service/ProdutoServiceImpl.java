package com.sistema.cadastro.service;

import com.sistema.cadastro.dto.ProdutoRequestDTO;
import com.sistema.cadastro.produto.Produto;
import com.sistema.cadastro.mapper.ProdutoMapper;
import com.sistema.cadastro.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
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

        if (p == null) {
            throw new NullPointerException("Deve ser passado algum produto");
        }

        return repository.save(p);
    }

    @Override
    public List<Produto> listarProdutos() {
        return repository.findAll();
    }

    @Override
    public Produto atualizarProduto(Long id, ProdutoRequestDTO novoProduto) {

        Produto p = repository.findById(id).orElse(null);

        if(p == null){
            throw new EntityNotFoundException("Produto não encontrado");
        }

        mapper.updateProdutoFromDto(novoProduto, p);
        return repository.save(p);
    }

    @Override
    public void deletarProduto(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
