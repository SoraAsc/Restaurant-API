package br.com.diasmarcos.sistemaloja.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeasurementUnits {
    g(""), Kg("");

    private final String description;
}
