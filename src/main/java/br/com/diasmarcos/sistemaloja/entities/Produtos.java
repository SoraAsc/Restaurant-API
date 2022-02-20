package br.com.diasmarcos.sistemaloja.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //ID do Produto

    @Column(nullable = false, unique = true)
    private String name; //Nome Do Produto

    @Column
    private String image; //Imagem

    @Column(nullable = false)
    private BigDecimal price; //Preço por unidade, Ex: 1kg = R$ 5 ou Ex: 1g = R$ 5

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true,
            cascade = CascadeType.ALL, targetEntity = Componentes.class)
    private List<Componentes> components; //Lista com os ingredientes e suas repectitivas quantidades.
}
