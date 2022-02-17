package br.com.diasmarcos.sistemaloja.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ControleVendaDTO {

    private String name; //Nome do Produto
    private Boolean canBeSold; //Estado verdadeiro ou falso, caso o produto possa ser vendido ou não.

}
