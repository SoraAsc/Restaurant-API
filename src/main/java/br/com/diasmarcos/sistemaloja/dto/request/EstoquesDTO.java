package br.com.diasmarcos.sistemaloja.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstoquesDTO {

    private Long id;

    @Valid
    @NotNull
    private IngredientesDTO ingredients;

    @DecimalMin("0.01")
    private BigDecimal quantity;

}
