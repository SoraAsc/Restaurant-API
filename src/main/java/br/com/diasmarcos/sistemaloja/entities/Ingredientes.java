package br.com.diasmarcos.sistemaloja.entities;

import br.com.diasmarcos.sistemaloja.enums.MeasurementUnits;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ingredientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; //Nome do Ingrediente

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MeasurementUnits unit; //Unidade de medida

    @Column(nullable = false)
    private BigDecimal price; //Preço Unitário
}
