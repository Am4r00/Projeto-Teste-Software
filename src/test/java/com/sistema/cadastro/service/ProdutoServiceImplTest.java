package com.sistema.cadastro.service;


import com.sistema.cadastro.dto.ProdutoRequestDTO;
import com.sistema.cadastro.entity.Produto;
import com.sistema.cadastro.mapper.ProdutoMapper;
import com.sistema.cadastro.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class ProdutoServiceImplTest {

    @InjectMocks
    private ProdutoServiceImpl service;

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ProdutoMapper mapper;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testeCriarProduto(){
        ProdutoRequestDTO produtoDTO = new ProdutoRequestDTO("tv","50 polegadas",2,2500.0);

        Produto entidadeMapeadaSemId = new Produto();
        entidadeMapeadaSemId.setNome(produtoDTO.getNome());
        entidadeMapeadaSemId.setDescricao(produtoDTO.getDescricao());
        entidadeMapeadaSemId.setQuantidade(produtoDTO.getQuantidade());
        entidadeMapeadaSemId.setValor(produtoDTO.getValor());

        Produto produtoEntidadeParaRetorno = new Produto();
        produtoEntidadeParaRetorno.setId(1L);
        produtoEntidadeParaRetorno.setNome(produtoDTO.getNome());
        produtoEntidadeParaRetorno.setDescricao(produtoDTO.getDescricao());
        produtoEntidadeParaRetorno.setQuantidade(produtoDTO.getQuantidade());
        produtoEntidadeParaRetorno.setValor(produtoDTO.getValor());



        when(mapper.toEntity(any(ProdutoRequestDTO.class))).thenReturn(entidadeMapeadaSemId);
        when(repository.save(any(Produto.class))).thenReturn(produtoEntidadeParaRetorno);

        Produto produtoCriado = service.criarProduto(produtoDTO);

        assertNotNull(produtoCriado);
        assertEquals(produtoEntidadeParaRetorno.getId(), produtoCriado.getId());
        assertEquals(produtoDTO.getNome(), produtoCriado.getNome());
        assertEquals(produtoDTO.getDescricao(), produtoCriado.getDescricao());
        assertEquals(produtoDTO.getQuantidade(), produtoCriado.getQuantidade());
        assertEquals(produtoDTO.getValor(), produtoCriado.getValor());

    }
}
