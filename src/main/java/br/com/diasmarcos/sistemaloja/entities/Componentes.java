package br.com.diasmarcos.sistemaloja.entities;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id",nullable = false)
    private Ingredientes ingredient; //Ingrediente

    @Column(nullable = false)
    private BigDecimal usedQuantity; //Quantidade usada do determinado ingrediente.
}
