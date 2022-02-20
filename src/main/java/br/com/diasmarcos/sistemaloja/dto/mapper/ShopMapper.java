package br.com.diasmarcos.sistemaloja.dto.mapper;

import br.com.diasmarcos.sistemaloja.dto.request.EstoquesDTO;
import br.com.diasmarcos.sistemaloja.dto.request.ProdutosDTO;
import br.com.diasmarcos.sistemaloja.dto.response.RelatorioProdutoDTO;
import br.com.diasmarcos.sistemaloja.entities.Estoques;
import br.com.diasmarcos.sistemaloja.entities.Produtos;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShopMapper {

    ShopMapper INSTANCE = Mappers.getMapper(ShopMapper.class);

    //Converte a Entidade Estoques na classe EstoquesDTO
    EstoquesDTO stockToDTO(Estoques stock);

    //Converte a classe EstoquesDTO na Entidade Estoques
    Estoques stockDTOToEntity(EstoquesDTO stockDTO);

    //Converte a Entidade Produtos na classe ProdutosDTO
    ProdutosDTO productToDTO(Produtos product);

    //Converte a classe ProdutosDTO na Entidade Produtos
    Produtos productDTOTOEntity(ProdutosDTO productDTO);

    //Converte a Entidade Produtos na classe RelatorioProdutoDTO
    RelatorioProdutoDTO productToProductReportDTO(Produtos product);

}
