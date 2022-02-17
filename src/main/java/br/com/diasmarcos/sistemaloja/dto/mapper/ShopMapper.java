package br.com.diasmarcos.sistemaloja.dto.mapper;

import br.com.diasmarcos.sistemaloja.dto.request.EstoquesDTO;
import br.com.diasmarcos.sistemaloja.dto.request.ProdutosDTO;
import br.com.diasmarcos.sistemaloja.entities.Estoques;
import br.com.diasmarcos.sistemaloja.entities.Produtos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShopMapper {

    ShopMapper INSTANCE = Mappers.getMapper(ShopMapper.class);

    EstoquesDTO stockToDTO(Estoques stock);

    Estoques stockDTOToEntity(EstoquesDTO stockDTO);

    ProdutosDTO productToDTO(Produtos product);

    Produtos productDTOTOEntity(ProdutosDTO productDTO);

}
