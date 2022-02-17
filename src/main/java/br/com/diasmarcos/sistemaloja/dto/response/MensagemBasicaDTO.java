package br.com.diasmarcos.sistemaloja.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MensagemBasicaDTO {
    private String message;
}
