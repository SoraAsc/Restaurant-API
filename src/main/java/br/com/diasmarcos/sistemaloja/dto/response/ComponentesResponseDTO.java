package br.com.diasmarcos.sistemaloja.dto.response;

import br.com.diasmarcos.sistemaloja.dto.request.IngredientesDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComponentesResponseDTO {

    private Long id;
    private IngredientesDTO ingredient; //Ingrediente
    private BigDecimal usedQuantity; //Quantidade usada do determinado ingrediente.
}
