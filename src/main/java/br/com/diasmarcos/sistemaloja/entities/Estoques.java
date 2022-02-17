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
public class Estoques {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(targetEntity = Ingredientes.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Ingredientes ingredients;

    @Column(nullable = false)
    private BigDecimal quantity;

}
