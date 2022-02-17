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
    private Long id;

    @Column(nullable = false)
    private String name; //Nome Do Produto

    @Column
    private String image; //Imagem

    @Column(nullable = false)
    private BigDecimal price; //Preço

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = Componentes.class)
    private List<Componentes> components; //Lista com os ingredientes e suas repectitivas quantidades.
}
