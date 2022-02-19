package br.com.diasmarcos.sistemaloja.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ProdNotFoundException extends Exception {

    public ProdNotFoundException(Long id){
        super("Não foi encontrado nenhum produto com o seguinte ID: "+id);
    }

}
