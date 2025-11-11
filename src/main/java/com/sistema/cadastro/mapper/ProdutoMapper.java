package com.sistema.cadastro.mapper;

import com.sistema.cadastro.dto.ProdutoRequestDTO;
import com.sistema.cadastro.produto.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    Produto toEntity(ProdutoRequestDTO dto);

    void updateProdutoFromDto(ProdutoRequestDTO dto, @MappingTarget Produto produto);

}
