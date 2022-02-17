package br.com.diasmarcos.sistemaloja.repositories;

import br.com.diasmarcos.sistemaloja.entities.Estoques;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoques, Long> {



}
