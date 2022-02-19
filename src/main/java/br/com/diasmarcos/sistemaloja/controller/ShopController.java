package br.com.diasmarcos.sistemaloja.controller;


import br.com.diasmarcos.sistemaloja.dto.request.EstoquesDTO;
import br.com.diasmarcos.sistemaloja.dto.request.ProdutosDTO;
import br.com.diasmarcos.sistemaloja.dto.response.ControleVendaDTO;
import br.com.diasmarcos.sistemaloja.dto.response.MensagemBasicaDTO;
import br.com.diasmarcos.sistemaloja.dto.response.RelatorioProdutoDTO;
import br.com.diasmarcos.sistemaloja.exceptions.IngredientNotFoundException;
import br.com.diasmarcos.sistemaloja.exceptions.ProdNotFoundException;
import br.com.diasmarcos.sistemaloja.exceptions.ShopDupException;
import br.com.diasmarcos.sistemaloja.exceptions.StockNotFoundException;
import br.com.diasmarcos.sistemaloja.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    // --------------------------------- Estoque ---------------------------------

    //Cria um novo estoque - Create
    @PostMapping("/stock/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MensagemBasicaDTO createStock(@RequestBody @Valid EstoquesDTO stockDTO){
        return shopService.createStock(stockDTO);
    }

    //Lista todos os estoques e seus ingredientes. - Get All
    @GetMapping("/stock")
    public List<EstoquesDTO> listAllIngredientStock(){
        return shopService.listAllIngredientInStock();
    }

    //Mostra todas as informações de um determinado estoque - Get By ID
    @GetMapping("/stock/inspect/{id}")
    public EstoquesDTO getStockById(@PathVariable Long id) throws StockNotFoundException{
        return shopService.getStockById(id);
    }

    //Atualiza completamente o Estoque especificado - Update By ID
    @PutMapping("/stock/inspect/{id}")
    public MensagemBasicaDTO updateStockById(@PathVariable Long id,@RequestBody @Valid EstoquesDTO stockDTO)
            throws StockNotFoundException, ShopDupException {
        return shopService.updateStockById(id, stockDTO);
    }

    //Deleta um determinado estoque e os produtos que utilizam os seus ingredientes. - Delete By ID
    @DeleteMapping("/stock/inspect/{id}")
    public MensagemBasicaDTO deleteStock(@PathVariable Long id) throws StockNotFoundException {
        return shopService.deleteStock(id);
    }

    // --------------------------------- Produto ---------------------------------

    //Cria um novo produto - Create
    @PostMapping("/product/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MensagemBasicaDTO createProduct(@RequestBody @Valid ProdutosDTO productDTO)
            throws IngredientNotFoundException {
        return shopService.createProduct(productDTO);
    }

    //Lista um relatório de todos os produtos. - Get All
    @GetMapping("/product")
    public List<RelatorioProdutoDTO> productReport(){
        return shopService.productReport();
    }

    //Mostra todas as informações de um determinado produto - Get By ID
    @GetMapping("/product/inspect/{id}")
    public ProdutosDTO getProductById(@PathVariable Long id) throws ProdNotFoundException{
        return shopService.getProductById(id);
    }

    //Mostra se é possível vender o produto atual. - Get By ID
    @GetMapping("/product/check/{id}")
    public ControleVendaDTO verifyIfCanBeSold(@PathVariable Long id) throws ProdNotFoundException {
        return shopService.verifyIfCanBeSold(id);
    }

    //Atualiza completamente o produto especificado - Update By ID
    @PutMapping("/product/inspect/{id}")
    public MensagemBasicaDTO updateProductById(
            @PathVariable Long id, @RequestBody @Valid ProdutosDTO productDTO)
            throws ProdNotFoundException, ShopDupException, IngredientNotFoundException {
        return shopService.updateProductById(id,productDTO);
    }

    //Deleta um determinado produto e seus componentes. - Delete By ID
    @DeleteMapping("/product/inspect/{id}")
    public MensagemBasicaDTO deleteProduct(@PathVariable Long id) throws ProdNotFoundException{
        return shopService.deleteProduct(id);
    }
}
