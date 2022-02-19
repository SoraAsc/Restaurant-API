package br.com.diasmarcos.sistemaloja.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class IngredientNotFoundException extends Exception{

    public IngredientNotFoundException(String name){
        super("Não foi encontrado um ingrediente com o seguinte nome: "+name);
    }

}
