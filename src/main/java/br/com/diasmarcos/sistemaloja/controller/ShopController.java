package br.com.diasmarcos.sistemaloja.controller;


import br.com.diasmarcos.sistemaloja.dto.request.EstoquesDTO;
import br.com.diasmarcos.sistemaloja.dto.request.ProdutosDTO;
import br.com.diasmarcos.sistemaloja.dto.response.ControleVendaDTO;
import br.com.diasmarcos.sistemaloja.dto.response.MensagemBasicaDTO;
import br.com.diasmarcos.sistemaloja.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/stock")
    public List<EstoquesDTO> listAllIngredientStock(){
        return shopService.listAllIngredientInStock();
    }

    @PostMapping("/stock/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MensagemBasicaDTO createStock(@RequestBody @Valid EstoquesDTO stockDTO){
        return shopService.createStock(stockDTO);
    }

    @GetMapping("/product/{id}")
    public ControleVendaDTO verifyIfCanBeSold(@PathVariable Long id)
    {
        return shopService.verifyIfCanBeSold(id);
    }


    @PostMapping("/product/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MensagemBasicaDTO createProduct(@RequestBody @Valid ProdutosDTO productDTO){
        return shopService.createProduct(productDTO);
    }
}
