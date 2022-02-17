package br.com.diasmarcos.sistemaloja.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComponentesDTO {

    private Long id;

    @NotEmpty
    private String ingredientName;

    @DecimalMin("0.01")
    private BigDecimal usedQuantity;

}
