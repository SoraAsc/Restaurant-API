package br.com.diasmarcos.sistemaloja.services;

import br.com.diasmarcos.sistemaloja.dto.mapper.ShopMapper;
import br.com.diasmarcos.sistemaloja.dto.request.ComponentesDTO;
import br.com.diasmarcos.sistemaloja.dto.request.EstoquesDTO;
import br.com.diasmarcos.sistemaloja.dto.request.ProdutosDTO;
import br.com.diasmarcos.sistemaloja.dto.response.ControleVendaDTO;
import br.com.diasmarcos.sistemaloja.dto.response.MensagemBasicaDTO;
import br.com.diasmarcos.sistemaloja.dto.response.RelatorioProdutoDTO;
import br.com.diasmarcos.sistemaloja.entities.Componentes;
import br.com.diasmarcos.sistemaloja.entities.Estoques;
import br.com.diasmarcos.sistemaloja.entities.Ingredientes;
import br.com.diasmarcos.sistemaloja.entities.Produtos;
import br.com.diasmarcos.sistemaloja.exceptions.IngredientNotFoundException;
import br.com.diasmarcos.sistemaloja.exceptions.ProdNotFoundException;
import br.com.diasmarcos.sistemaloja.exceptions.ShopDupException;
import br.com.diasmarcos.sistemaloja.exceptions.StockNotFoundException;
import br.com.diasmarcos.sistemaloja.repositories.EstoqueRepository;
import br.com.diasmarcos.sistemaloja.repositories.IngredienteRepository;
import br.com.diasmarcos.sistemaloja.repositories.ProdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {

    @Autowired
    private ProdRepository prodRepository;
    @Autowired
    private EstoqueRepository estoqueRepository;
    @Autowired
    private IngredienteRepository ingredienteRepository;

    private final ShopMapper shopMapper = ShopMapper.INSTANCE;

    // ---------------------------------------------- Estoque ---------------------------------------------- \\

    //Cria um novo Estoque
    public MensagemBasicaDTO createStock(EstoquesDTO stockDTO) {
        estoqueRepository.save(shopMapper.stockDTOToEntity(stockDTO));
        return MensagemBasicaDTO.builder().message("O estoque foi criado com sucesso!").build();
    }

    //Lista todos os ingredientes em estoque, junto com sua quantidade
    public List<EstoquesDTO> listAllIngredientInStock() {
        return estoqueRepository.findAll().stream().map(shopMapper::stockToDTO).collect(Collectors.toList());
    }

    public EstoquesDTO getStockById(Long id) throws StockNotFoundException {
        return shopMapper.stockToDTO(verifyIfStockExist(id));
    }

    public MensagemBasicaDTO updateStockById(Long id, EstoquesDTO stockDTO)
            throws StockNotFoundException, ShopDupException {
        stockDTO.setId(id);
        Estoques stock = verifyIfIngredientNameIsDup(stockDTO.getIngredients().getName(),id);
        stockDTO.getIngredients().setId(stock.getIngredients().getId());

        estoqueRepository.save(shopMapper.stockDTOToEntity(stockDTO));
        return MensagemBasicaDTO.builder().message("O Estoque de ID: "
                +stock.getId()+" foi atualizado com sucesso!").build();
    }

    public MensagemBasicaDTO deleteStock(Long id) throws StockNotFoundException {
        Estoques stock = verifyIfStockExist(id);
        List<Produtos> allProducts = new ArrayList<>(); //Todos os produtos que precisam ser deletados
        StringBuilder prodNames = new StringBuilder();
        for(Produtos prod : prodRepository.findAll()){
            List<String> allIngredientNames = prod.getComponents().stream()
                    .map(comp -> comp.getIngredient().getName()).toList();
            if(allIngredientNames.contains(stock.getIngredients().getName())){
                allProducts.add(prod);
                prodNames.append(prodNames.length() > 1 ? ", " : "").append(prod.getName());
            }
        }
        prodRepository.deleteAll(allProducts);
        estoqueRepository.delete(stock);

        String message = (prodNames.length()>0
                ? "Os seguintes produtos foram excluidos por falta de suplimento no estoque: ["+prodNames+"]"
                : "")+ String.format("O Estoque com o seguinte ingrediente “%s” foi deletado com sucesso!",
                stock.getIngredients().getName());

        return MensagemBasicaDTO.builder().message(message).build();
    }

    // ---------------------------------------------- Produto ---------------------------------------------- \\

    //Cria um novo Produto, usando os ingredientes em estoque
    public MensagemBasicaDTO createProduct(ProdutosDTO productDTO) throws IngredientNotFoundException {
        Produtos product = shopMapper.productDTOTOEntity(productDTO);
        List<Componentes> componentList = new ArrayList<>();
        for (ComponentesDTO comp :productDTO.getComponents()) {
            Ingredientes ing = ingredienteRepository.findByName(comp.getIngredientName());
            if(ing==null) throw new IngredientNotFoundException(comp.getIngredientName());
            componentList.add(
                    Componentes.builder()
                            .ingredient(ing)
                            .usedQuantity(comp.getUsedQuantity())
                            .build()
            );
        }
        product.setComponents(componentList);
        prodRepository.save(product);
        return MensagemBasicaDTO.builder().message("O produto foi criado com sucesso!").build();
    }

    //Exibe um relatório sobre os produtos.
    public List<RelatorioProdutoDTO> productReport() {
        List<RelatorioProdutoDTO> allProductsReport = prodRepository.findAll().stream()
                .map(shopMapper::productToProductReportDTO).collect(Collectors.toList());

        for (RelatorioProdutoDTO productReport: allProductsReport){
            BigDecimal productionCost = productReport.getComponents().stream()
                    .map(comp -> comp.getIngredient().getPrice()
                            .multiply(comp.getUsedQuantity())).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

            productReport.setProductionCost(productionCost);
            productReport.setProfit(productReport.getPrice().subtract(productionCost) );
        }
        return allProductsReport;
    }

    //Informa Se o Produto pode ser vendido ou não
    public ControleVendaDTO verifyIfCanBeSold(Long id) throws ProdNotFoundException {
        Produtos product = verifyIfProductExist(id);

        List<Componentes> components = product.getComponents();
        List<Estoques> allStocks = estoqueRepository.findAll();

        for (Estoques stock : allStocks){
            Componentes component = components.stream().filter(
                    comp -> comp.getIngredient().getName().equals(stock.getIngredients().getName()) &&
                            comp.getUsedQuantity().compareTo(stock.getQuantity()) >= 0).findAny().orElse(null);

            if(component!=null) return ControleVendaDTO.builder().canBeSold(false).name(product.getName()).build();
        }
        return ControleVendaDTO.builder().canBeSold(true).name(product.getName()).build();
    }

    public ProdutosDTO getProductById(Long id) throws ProdNotFoundException{
        return shopMapper.productToDTO( verifyIfProductExist(id) ); //Change
    }

    public MensagemBasicaDTO updateProductById(Long id, ProdutosDTO productDTO)
            throws ProdNotFoundException, ShopDupException, IngredientNotFoundException {
        verifyIfProductNameIsDup(productDTO.getName(), id);
        productDTO.setId(id);
        List<Componentes> componentList = new ArrayList<>();
        for (ComponentesDTO comp :productDTO.getComponents()) {
            Ingredientes ing = ingredienteRepository.findByName(comp.getIngredientName());
            if(ing==null) throw new IngredientNotFoundException(comp.getIngredientName());
            componentList.add(
                    Componentes.builder()
                            .ingredient(ing)
                            .usedQuantity(comp.getUsedQuantity())
                            .build()
            );
        }
        Produtos product = shopMapper.productDTOTOEntity(productDTO);
        product.setComponents(componentList);
        prodRepository.save(product);
        return MensagemBasicaDTO.builder().message("O Produto "
                +product.getName()+" foi atualizado com sucesso!").build();
    }

    //Deleta um produto especificado.
    public MensagemBasicaDTO deleteProduct(Long id) throws ProdNotFoundException {
        Produtos product = verifyIfProductExist(id);
        prodRepository.delete(product);
        return MensagemBasicaDTO.builder()
                .message(String.format("O produto “%s” foi deletado com sucesso!",
                        product.getName()))
                .build();
    }

    // ---------------------------------------------- Verificações ---------------------------------------------- \\

    private Estoques verifyIfStockExist(Long id) throws StockNotFoundException {
        return estoqueRepository.findById(id).orElseThrow( () -> new StockNotFoundException(id) );
    }

    private Estoques verifyIfIngredientNameIsDup(String name, Long id)
            throws StockNotFoundException, ShopDupException {
        Estoques stockToChange = verifyIfStockExist(id);
        Ingredientes dupIngredient = ingredienteRepository.findByName(name);
        if(dupIngredient!=null
                && ( !dupIngredient.getId().equals(stockToChange.getIngredients().getId()) ) )
            throw new ShopDupException("Já existe um Ingrediente com o nome: "+name);
        return stockToChange;
    }

    private Produtos verifyIfProductExist(Long id) throws ProdNotFoundException {
        return prodRepository.findById(id).orElseThrow( () -> new ProdNotFoundException(id));
    }

    private void verifyIfProductNameIsDup(String name, Long id)
            throws ProdNotFoundException, ShopDupException {
        Produtos productToChange = verifyIfProductExist(id);
        Produtos dupProduct = prodRepository.findByName(name);
        if(dupProduct!=null &&
                ( !dupProduct.getId().equals(productToChange.getId()) ) )
            throw new ShopDupException("Já existe um Produto com o nome: "+name);

    }
}
