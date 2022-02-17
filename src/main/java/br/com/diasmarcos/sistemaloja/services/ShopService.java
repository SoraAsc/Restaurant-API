package br.com.diasmarcos.sistemaloja.services;

import br.com.diasmarcos.sistemaloja.dto.mapper.ShopMapper;
import br.com.diasmarcos.sistemaloja.dto.request.ComponentesDTO;
import br.com.diasmarcos.sistemaloja.dto.request.EstoquesDTO;
import br.com.diasmarcos.sistemaloja.dto.request.ProdutosDTO;
import br.com.diasmarcos.sistemaloja.dto.response.ControleVendaDTO;
import br.com.diasmarcos.sistemaloja.dto.response.MensagemBasicaDTO;
import br.com.diasmarcos.sistemaloja.entities.Componentes;
import br.com.diasmarcos.sistemaloja.entities.Estoques;
import br.com.diasmarcos.sistemaloja.entities.Ingredientes;
import br.com.diasmarcos.sistemaloja.entities.Produtos;
import br.com.diasmarcos.sistemaloja.repositories.EstoqueRepository;
import br.com.diasmarcos.sistemaloja.repositories.ProdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {

    @Autowired
    private ProdRepository prodRepository;
    @Autowired
    private EstoqueRepository estoqueRepository;


    private final ShopMapper shopMapper = ShopMapper.INSTANCE;

    //Lista todos os ingredientes em estoque, junto com sua quantidade
    public List<EstoquesDTO> listAllIngredientInStock() {
        return estoqueRepository.findAll().stream().map(shopMapper::stockToDTO).collect(Collectors.toList());
    }

    //Informa Se o Produto pode ser vendido ou não
    public ControleVendaDTO verifyIfCanBeSold(Long id) {
        Produtos product = prodRepository.findById(id).get();

        List<Componentes> components = product.getComponents().stream().map(
                comp -> comp).collect(Collectors.toList());
        List<Estoques> allStocks = estoqueRepository.findAll();

        for (Estoques stock : allStocks){
            Componentes component = components.stream().filter(
                    comp -> comp.getIngredient().getName().equals(stock.getIngredients().getName()) &&
                            comp.getUsedQuantity().compareTo(stock.getQuantity()) >= 0).findAny().orElse(null);

            if(component!=null) return ControleVendaDTO.builder().canBeSold(false).name(product.getName()).build();
        }
        return ControleVendaDTO.builder().canBeSold(true).name(product.getName()).build();
    }

    //Cria um novo Estoque
    public MensagemBasicaDTO createStock(EstoquesDTO stockDTO) {
        estoqueRepository.save(shopMapper.stockDTOToEntity(stockDTO));
        return MensagemBasicaDTO.builder().message("O estoque foi criado com sucesso!").build();
    }

    //Cria um novo Produto, usando os ingredientes em estoque
    public MensagemBasicaDTO createProduct(ProdutosDTO productDTO) {
        Produtos product = shopMapper.productDTOTOEntity(productDTO);
        List<Ingredientes> ingredientList = estoqueRepository.findAll().stream().map(estoque -> estoque.getIngredients()).collect(Collectors.toList());
        List<Componentes> componentList = new ArrayList<>();

        for (ComponentesDTO comp :productDTO.getComponents()){
            componentList.add( new Componentes(
                    null,
                    ingredientList.stream().filter(ing ->
                            ing.getName().equals(comp.getIngredientName())).findAny().orElse(null),
                    comp.getUsedQuantity()
            ) );
        }

        product.setComponents(componentList);
        prodRepository.save(product);
        return MensagemBasicaDTO.builder().message("O produto foi criado com sucesso!").build();
    }
}
