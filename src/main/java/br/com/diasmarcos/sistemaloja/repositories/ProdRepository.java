package br.com.diasmarcos.sistemaloja.repositories;

import br.com.diasmarcos.sistemaloja.entities.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdRepository extends JpaRepository<Produtos, Long> {


}
