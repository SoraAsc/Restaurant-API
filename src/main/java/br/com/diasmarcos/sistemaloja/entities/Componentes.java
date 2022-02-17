package br.com.diasmarcos.sistemaloja.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Componentes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Ingredientes.class, fetch = FetchType.LAZY)
    private Ingredientes ingredient; //Ingrediente

    @Column(nullable = false)
    private BigDecimal usedQuantity; //Quantidade usada do determinado ingrediente.
}
