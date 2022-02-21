package br.com.diasmarcos.sistemaloja.services;

import br.com.diasmarcos.sistemaloja.dto.mapper.ShopMapper;
import br.com.diasmarcos.sistemaloja.dto.request.EstoquesDTO;
import br.com.diasmarcos.sistemaloja.dto.response.MensagemBasicaDTO;
import br.com.diasmarcos.sistemaloja.entities.Estoques;
import br.com.diasmarcos.sistemaloja.entities.Ingredientes;
import br.com.diasmarcos.sistemaloja.entities.Produtos;
import br.com.diasmarcos.sistemaloja.exceptions.ShopDupException;
import br.com.diasmarcos.sistemaloja.exceptions.StockNotFoundException;
import br.com.diasmarcos.sistemaloja.exceptions.repositories.EstoqueRepository;
import br.com.diasmarcos.sistemaloja.exceptions.repositories.IngredienteRepository;
import br.com.diasmarcos.sistemaloja.exceptions.repositories.ProdRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EstoqueService {

    private EstoqueRepository estoqueRepository;
    private ProdRepository prodRepository;
    private IngredienteRepository ingredienteRepository;

    private final ShopMapper shopMapper = ShopMapper.INSTANCE;

    /**
     * Cria um novo Estoque
     * @param stockDTO O DTO referente a Entidade Estoques
     * @return Um Modelo padrão de mensagem.
     */
    public MensagemBasicaDTO createStock(EstoquesDTO stockDTO) throws ShopDupException, StockNotFoundException {
        verifyIfIngredientNameIsDup(stockDTO.getIngredients().getName(),null);
        estoqueRepository.save(shopMapper.stockDTOToEntity(stockDTO));
        return createBasicMessage(String.format("O estoque %s foi criado com sucesso!",
                stockDTO.getIngredients().getName()));
    }

    /**
     * Lista todos os ingredientes em estoque com sua quantidade
     * @return Uma lista com todos os elementos do DTO referente a entidade Estoques
     */
    public List<EstoquesDTO> listAllIngredientInStock() {
        return estoqueRepository.findAll().stream().map(shopMapper::stockToDTO).collect(Collectors.toList());
    }

    /**
     * Lista as informações de um ingrediente específico.
     * @param id ID da entidade Estoques
     * @return Um DTO referente a entidade Estoques
     * @throws StockNotFoundException Se o Estoque com o devido ID não existir
     */
    public EstoquesDTO getStockById(Long id) throws StockNotFoundException {
        return shopMapper.stockToDTO(verifyIfStockExist(id));
    }

    /**
     * Atualiza todos os campos do estoque.
     * @param id ID do Estoque
     * @param stockDTO DTO referente a Entidade Estoques
     * @return Um Modelo padrão de mensagem.
     * @throws StockNotFoundException Se o Estoque com o devido ID não existir
     * @throws ShopDupException Se o Nome do Ingrediente for duplicado.
     */
    public MensagemBasicaDTO updateStockById(Long id, EstoquesDTO stockDTO)
            throws StockNotFoundException, ShopDupException {
        Estoques stock = verifyIfIngredientNameIsDup(stockDTO.getIngredients().getName(),id);
        //Inserção dos IDS do campo anterior nos novos.
        stockDTO.setId(id);
        stockDTO.getIngredients().setId(stock.getIngredients().getId());

        estoqueRepository.save(shopMapper.stockDTOToEntity(stockDTO));
        return createBasicMessage("O Estoque de ID: "+stock.getId()+" foi atualizado com sucesso!");
    }

    /**
     * Deleta o Estoque especificado.
     * @param id ID da entidade Estoques
     * @return Um Modelo padrão de mensagem.
     * @throws StockNotFoundException Se o Estoque com o devido ID não existir
     */
    public MensagemBasicaDTO deleteStock(Long id) throws StockNotFoundException {
        Estoques stock = verifyIfStockExist(id);
        List<Produtos> allProducts = new ArrayList<>(); //Todos os produtos que precisam ser deletados

        StringBuilder prodNames = new StringBuilder(); //Guarda os nomes de todos os produtos que podem ser deletados
        getProductsWithIngredients(stock, allProducts, prodNames);
        prodRepository.deleteAll(allProducts);
        estoqueRepository.delete(stock);

        String message = (prodNames.length()>0 ?
                "Os seguintes produtos foram excluidos por falta de suplimento no estoque: ["+prodNames+"]." : "")
                + String.format(" O Estoque com o seguinte ingrediente “%s” foi deletado com sucesso!",
                stock.getIngredients().getName());

        return createBasicMessage(message);
    }

    // ---------------------------------------------- Verificação Estoque ---------------------------------------------- \\

    /**
     * Verifica se o Estoque existe e em caso de sucesso retorna um Estoque se não uma Exception.
     * @param id ID da entidade Estoques
     * @return Um dos elementos da entidade Estoques
     * @throws StockNotFoundException Se o Estoque com o devido ID não existir
     */
    private Estoques verifyIfStockExist(Long id) throws StockNotFoundException {
        return estoqueRepository.findById(id).orElseThrow( () -> new StockNotFoundException(id) );
    }

    /**
     * Velida se existe ou é duplicado e em caso de sucesso retorna um Estoque se não uma Exception.
     * @param name Nome do ingrediente
     * @param id ID da entidade Estoques
     * @return Retorna a entidade estoque
     * @throws StockNotFoundException Se o Estoque com o devido ID não existir
     * @throws ShopDupException Se o Nome do Ingrediente for duplicado.
     */
    private Estoques verifyIfIngredientNameIsDup(String name, Long id)
            throws StockNotFoundException, ShopDupException {
        Estoques stockToChange = null;
        if(id!=null) stockToChange = verifyIfStockExist(id);
        Ingredientes dupIngredient = ingredienteRepository.findByName(name);
        if(dupIngredient!=null
                && ( stockToChange==null || !dupIngredient.getId().equals(stockToChange.getIngredients().getId()) ) )
            throw new ShopDupException("Já existe um Ingrediente com o nome: "+name);
        return stockToChange;
    }

    /**
     * Pega todos os Produtos que tenham determinado ingrediente.
     * @param stock Entidade Estoques
     * @param allProducts Lista responsável por guardar todos os produtos que passem no critério, citado acima
     * @param prodNames Lista responsável por guardar os nomes de todos os produtos que passem no critério citado acima
     */
    private void getProductsWithIngredients(Estoques stock, List<Produtos> allProducts, StringBuilder prodNames) {
        for(Produtos prod : prodRepository.findAll()){
            List<String> allIngredientNames = prod.getComponents().stream()
                    .map(comp -> comp.getIngredient().getName()).toList();
            if(allIngredientNames.contains(stock.getIngredients().getName())){
                allProducts.add(prod);
                prodNames.append(prodNames.length() > 1 ? ", " : "").append(prod.getName());
            }
        }
    }

    /**
     * Cria uma mensagem básica de aviso/notificação para as requisições, que normalmente não retornam um corpo.
     * @param message A mensagem que deverá ser exibida.
     * @return Uma mensagem
     */
    private MensagemBasicaDTO createBasicMessage(String message){
        return MensagemBasicaDTO.builder().message(message).build();
    }
}
