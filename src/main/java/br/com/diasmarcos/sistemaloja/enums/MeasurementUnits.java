package br.com.diasmarcos.sistemaloja.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeasurementUnits {
    //Massa
    mg("Miligrama"), g("Grama"), Kg("Quilograma"),
    //Volume
    tsp("Colher de Chá"), c("Xícara"), gallon("Galão"),
    ml("Mililitro"), l("Litro"),
    //Comprimento
    cm("Centímetro"), m("Metro"), inch("Polegada");


    private final String description;
}
