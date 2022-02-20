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
    private Long id; //ID do Estoque

    @OneToOne(targetEntity = Ingredientes.class, fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Ingredientes ingredients; //Ingrediente

    @Column(nullable = false)
    private BigDecimal quantity; //Quantidade de ingredientes

}
