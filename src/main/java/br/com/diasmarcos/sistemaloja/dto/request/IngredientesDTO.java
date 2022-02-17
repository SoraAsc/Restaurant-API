package br.com.diasmarcos.sistemaloja.dto.request;

import br.com.diasmarcos.sistemaloja.enums.MeasurementUnits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientesDTO {

    private Long id;

    @NotEmpty
    private String name;

    @Enumerated(EnumType.STRING)
    private MeasurementUnits unit;

    @DecimalMin("0.05")
    private BigDecimal price;

}
