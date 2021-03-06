package br.com.diasmarcos.sistemaloja.services;

import br.com.diasmarcos.sistemaloja.dto.mapper.ShopMapper;
import br.com.diasmarcos.sistemaloja.dto.request.ComponentesDTO;
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
import br.com.diasmarcos.sistemaloja.exceptions.repositories.EstoqueRepository;
import br.com.diasmarcos.sistemaloja.exceptions.repositories.IngredienteRepository;
import br.com.diasmarcos.sistemaloja.exceptions.repositories.ProdRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProdutoService {

    private ProdRepository prodRepository;
    private EstoqueRepository estoqueRepository;
    private IngredienteRepository ingredienteRepository;

    private final ShopMapper shopMapper = ShopMapper.INSTANCE;

    /**
     * Cria um Produto, usando os ingredientes em estoque
     * @param productDTO DTO referente a entidade Produtos
     * @return Um Modelo padrão de mensagem.
     * @throws IngredientNotFoundException Se o Ingrediente com o devido ID não existir
     */
    public MensagemBasicaDTO createProduct(ProdutosDTO productDTO) throws
            IngredientNotFoundException, ShopDupException, ProdNotFoundException {
        verifyIfProductNameIsDup(productDTO.getName(), null);
        Produtos product = shopMapper.productDTOTOEntity(productDTO);
        List<Componentes> componentList = new ArrayList<>();
        getProductComponents(productDTO, componentList);
        product.setComponents(componentList);
        prodRepository.save(product);
        return MensagemBasicaDTO.builder().message("O produto foi criado com sucesso!").build();
    }

    /**
     * Exibe um relatório sobre os produtos.
     * @return Uma Lista com informações sobre o Produto
     */
    public List<RelatorioProdutoDTO> productReport() {
        List<RelatorioProdutoDTO> allProductsReport = prodRepository.findAll().stream()
                .map(shopMapper::productToProductReportDTO).collect(Collectors.toList());

        for (RelatorioProdutoDTO productReport: allProductsReport){
            getProductPrices(productReport);
        }
        return allProductsReport;
    }

    /**
     * Informa Se o Produto pode ser vendido ou não.
     * @param id ID da entidade Produtos
     * @return Um DTO que exibe se determinado produto pode ser vendido ou não
     * @throws ProdNotFoundException Se o Produto com o devido ID não existir
     */
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

    /**
     * Exibe um relatório sobre um produto específico.
     * @param id ID da entidade Produtos
     * @return Informações sobre o Produto
     * @throws ProdNotFoundException Se o Produto com o devido ID não existir
     */
    public RelatorioProdutoDTO getProductById(Long id) throws ProdNotFoundException{
        RelatorioProdutoDTO productReport = shopMapper.productToProductReportDTO(verifyIfProductExist(id));
        getProductPrices(productReport);
        return productReport;
    }

    /**
     * Atualiza todos os campos da entidade Produto.
     * @param id ID da entidade Produtos
     * @param productDTO DTO referente a entidade Produtos
     * @return Um Modelo padrão de mensagem.
     * @throws ProdNotFoundException Se o Produto com o devido ID não existir
     * @throws ShopDupException Se o Nome do Produto for duplicado.
     * @throws IngredientNotFoundException Se o Ingrediente com o devido ID não existir
     */
    public MensagemBasicaDTO updateProductById(Long id, ProdutosDTO productDTO)
            throws ProdNotFoundException, ShopDupException, IngredientNotFoundException {
        verifyIfProductNameIsDup(productDTO.getName(), id);
        productDTO.setId(id);
        List<Componentes> componentList = new ArrayList<>();
        getProductComponents(productDTO,componentList);

        Produtos product = shopMapper.productDTOTOEntity(productDTO);
        product.setComponents(componentList);
        prodRepository.save(product);
        return createBasicMessage("O Produto "+product.getName()+" foi atualizado com sucesso!");
    }

    /**
     * Deleta um produto especificado.
     * @param id ID da entidade Produtos
     * @return Um Modelo Padrão de Mensagem
     * @throws ProdNotFoundException Se o Produto com o devido ID não existir
     */
    public MensagemBasicaDTO deleteProduct(Long id) throws ProdNotFoundException {
        Produtos product = verifyIfProductExist(id);
        prodRepository.delete(product);
        return createBasicMessage(String.format("O produto “%s” foi deletado com sucesso!",product.getName()));
    }

    /**
     * Cria uma mensagem básica de aviso/notificação para as requisições, que normalmente não retornam um corpo.
     * @param message A mensagem que deverá ser exibida.
     * @return Uma mensagem
     */
    private MensagemBasicaDTO createBasicMessage(String message){
        return MensagemBasicaDTO.builder().message(message).build();
    }

    /**
     * Popula no objeto informações a respeito do ganho e custo de criação do Produto.
     * @param productReport Um DTO contendo informações a respeito do Produto
     */
    private void getProductPrices(RelatorioProdutoDTO productReport) {
        BigDecimal productionCost = productReport.getComponents().stream()
                .map(comp -> comp.getIngredient().getPrice()
                        .multiply(comp.getUsedQuantity())).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        productReport.setProductionCost(productionCost);
        productReport.setProfit(productReport.getPrice().subtract(productionCost) );
    }

    // ---------------------------------------------- Verificação Produto ---------------------------------------------- \\

    /**
     * Verifica se o Produto existe e em caso de sucesso retorna um Produto se não uma Exception.
     * @param id ID da entidade Produtos
     * @return Um dos elementos da entidade Produtos
     * @throws ProdNotFoundException Se o Produto com o devido ID não existir
     */
    private Produtos verifyIfProductExist(Long id) throws ProdNotFoundException {
        return prodRepository.findById(id).orElseThrow( () -> new ProdNotFoundException(id));
    }

    /**
     * Velida se existe ou é duplicado e em caso de sucesso retorna um Produto se não uma Exception.
     * @param name Nome do Produto
     * @param id ID da entidade Produtos
     * @throws ProdNotFoundException Se o Produto com o devido ID não existir
     * @throws ShopDupException Se o Nome do Produto for duplicado
     */
    private void verifyIfProductNameIsDup(String name, Long id)
            throws ProdNotFoundException, ShopDupException {
        Produtos productToChange = null;
        if(id!=null) productToChange = verifyIfProductExist(id);
        Produtos dupProduct = prodRepository.findByName(name);
        if(dupProduct!=null &&
                ( productToChange == null || !dupProduct.getId().equals(productToChange.getId()) ) )
            throw new ShopDupException("Já existe um Produto com o nome: "+name);
    }

    /**
     * Transforma as informações do Componente DTO na Entidade Componente.
     * @param productDTO O DTO referente a Entidade Produtos
     * @param componentList Uma lista responsável por guardar todos os Componentes do Produto
     * @throws IngredientNotFoundException Se o Ingrediente com o devido ID não existir
     */
    private void getProductComponents(ProdutosDTO productDTO, List<Componentes> componentList) throws IngredientNotFoundException {
        for (ComponentesDTO comp : productDTO.getComponents()) {
            Ingredientes ing = ingredienteRepository.findByName(comp.getIngredientName());
            if(ing==null) throw new IngredientNotFoundException(comp.getIngredientName());
            componentList.add(
                    Componentes.builder()
                            .ingredient(ing)
                            .usedQuantity(comp.getUsedQuantity())
                            .build()
            );
        }
    }

}
