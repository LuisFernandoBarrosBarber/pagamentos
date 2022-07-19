package com.barbearia.pagamentos.repository;

import com.barbearia.pagamentos.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Long countAllByIdAndAtivoIsTrueAndIdAsaasNotNull(Long id);

    Optional<ClienteEntity> findByIdAndAtivoIsTrue(Long id);
}
