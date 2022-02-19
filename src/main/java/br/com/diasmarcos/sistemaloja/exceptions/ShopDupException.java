package br.com.diasmarcos.sistemaloja.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ShopDupException extends Exception{

    public ShopDupException(String message){
        super(message);
    }

}
