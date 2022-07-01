package com.barbearia.pagamentos.repository;

import com.barbearia.pagamentos.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Long countAllByIdAndAtivoIsTrue(Long id);
}
