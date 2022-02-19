package br.com.diasmarcos.sistemaloja.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StockNotFoundException extends Exception{

    public StockNotFoundException(Long id){
        super("Nenhum estoque foi encontrado, com o seguinte ID: "+id);
    }

}
