package br.com.diasmarcos.sistemaloja.controller;

import br.com.diasmarcos.sistemaloja.dto.request.EstoquesDTO;
import br.com.diasmarcos.sistemaloja.dto.response.MensagemBasicaDTO;
import br.com.diasmarcos.sistemaloja.exceptions.ShopDupException;
import br.com.diasmarcos.sistemaloja.exceptions.StockNotFoundException;
import br.com.diasmarcos.sistemaloja.services.EstoqueService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shop/stock")
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class EstoqueController {

    private EstoqueService estoqueService;
    //private ShopService shopService;

    //Cria um estoque — Create
    @PostMapping("/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MensagemBasicaDTO createStock(@RequestBody @Valid EstoquesDTO stockDTO){
        return estoqueService.createStock(stockDTO);
    }

    //Lista todos os estoques e seus ingredientes. - Get All
    @GetMapping
    public List<EstoquesDTO> listAllIngredientStock(){
        return estoqueService.listAllIngredientInStock();
    }

    //Mostra todas as informações de um determinado estoque — Get By ID
    @GetMapping("/inspect/{id}")
    public EstoquesDTO getStockById(@PathVariable Long id) throws StockNotFoundException {
        return estoqueService.getStockById(id);
    }

    //Atualiza completamente o Estoque especificado - Update By ID
    @PutMapping("/inspect/{id}")
    public MensagemBasicaDTO updateStockById(@PathVariable Long id,@RequestBody @Valid EstoquesDTO stockDTO)
            throws StockNotFoundException, ShopDupException {
        return estoqueService.updateStockById(id, stockDTO);
    }

    //Deleta um determinado estoque e os produtos que utilizam os seus ingredientes — Delete By ID
    @DeleteMapping("/inspect/{id}")
    public MensagemBasicaDTO deleteStock(@PathVariable Long id) throws StockNotFoundException {
        return estoqueService.deleteStock(id);
    }


}
