package br.com.diasmarcos.sistemaloja.controller;

import br.com.diasmarcos.sistemaloja.dto.request.ProdutosDTO;
import br.com.diasmarcos.sistemaloja.dto.response.ControleVendaDTO;
import br.com.diasmarcos.sistemaloja.dto.response.MensagemBasicaDTO;
import br.com.diasmarcos.sistemaloja.dto.response.RelatorioProdutoDTO;
import br.com.diasmarcos.sistemaloja.exceptions.IngredientNotFoundException;
import br.com.diasmarcos.sistemaloja.exceptions.ProdNotFoundException;
import br.com.diasmarcos.sistemaloja.exceptions.ShopDupException;
import br.com.diasmarcos.sistemaloja.services.ProdutoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shop/product")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProdutoController {

    private ProdutoService produtoService;

    //Cria um produto — Create
    @PostMapping("/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MensagemBasicaDTO createProduct(@RequestBody @Valid ProdutosDTO productDTO)
            throws IngredientNotFoundException, ShopDupException, ProdNotFoundException {
        return produtoService.createProduct(productDTO);
    }

    //Lista um relatório de todos os produtos — Get All
    @GetMapping
    public List<RelatorioProdutoDTO> productReport(){
        return produtoService.productReport();
    }

    //Mostra todas as informações de um determinado produto — Get By ID
    @GetMapping("/inspect/{id}")
    public RelatorioProdutoDTO getProductById(@PathVariable Long id) throws ProdNotFoundException {
        return produtoService.getProductById(id);
    }

    //Mostra se é possível vender o produto atual — Get By ID
    @GetMapping("/check/{id}")
    public ControleVendaDTO verifyIfCanBeSold(@PathVariable Long id) throws ProdNotFoundException {
        return produtoService.verifyIfCanBeSold(id);
    }

    //Atualiza completamente o produto especificado — Update By ID
    @PutMapping("/inspect/{id}")
    public MensagemBasicaDTO updateProductById(
            @PathVariable Long id, @RequestBody @Valid ProdutosDTO productDTO)
            throws ProdNotFoundException, ShopDupException, IngredientNotFoundException {
        return produtoService.updateProductById(id,productDTO);
    }

    //Deleta um determinado produto e seus componentes — Delete By ID
    @DeleteMapping("/inspect/{id}")
    public MensagemBasicaDTO deleteProduct(@PathVariable Long id) throws ProdNotFoundException{
        return produtoService.deleteProduct(id);
    }

}
