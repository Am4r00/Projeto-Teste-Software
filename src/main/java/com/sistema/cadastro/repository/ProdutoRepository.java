package com.sistema.cadastro.repository;

import com.sistema.cadastro.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
