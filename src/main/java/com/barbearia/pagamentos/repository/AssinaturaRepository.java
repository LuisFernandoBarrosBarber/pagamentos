package com.barbearia.pagamentos.repository;

import com.barbearia.pagamentos.entities.AssinaturaEntity;
import com.barbearia.pagamentos.entities.AssinaturaEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssinaturaRepository extends JpaRepository<AssinaturaEntity, AssinaturaEntityId> {
    int countAllByAtivoIsTrueAndIdCliente(Long idCliente);

    Optional<AssinaturaEntity> getByIdClienteAndAtivoIsTrue(Long idCliente);

    Optional<AssinaturaEntity> findByAtivoIsTrueAndIdAssinatura(String idAssinatura);
}
