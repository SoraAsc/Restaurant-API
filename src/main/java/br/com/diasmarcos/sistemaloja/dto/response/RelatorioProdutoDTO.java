package br.com.diasmarcos.sistemaloja.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioProdutoDTO {

    private Long id;
    private String name; //Nome do produto
    private String image; //Imagem do produto
    private BigDecimal price; //Preço do produto

    private List<ComponentesResponseDTO> components; //Componentes usados no produto

    private BigDecimal productionCost; //Custo de produção
    private BigDecimal profit; //Lucro
}
