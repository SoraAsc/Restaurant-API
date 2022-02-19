package br.com.diasmarcos.sistemaloja.repositories;

import br.com.diasmarcos.sistemaloja.entities.Ingredientes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredienteRepository extends JpaRepository<Ingredientes, Long> {

    Ingredientes findByName(String name);

}
