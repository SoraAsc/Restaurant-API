package br.com.diasmarcos.sistemaloja.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutosDTO {

    private Long id;

    @NotEmpty
    private String name;

    private String image;

    @DecimalMin("0.25")
    private BigDecimal price;

    @Valid
    @NotEmpty
    private List<ComponentesDTO> components;


}
