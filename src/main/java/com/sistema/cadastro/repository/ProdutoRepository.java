package com.sistema.cadastro.repository;

import com.sistema.cadastro.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
