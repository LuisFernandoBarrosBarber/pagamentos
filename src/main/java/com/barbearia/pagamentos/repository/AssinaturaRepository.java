package com.barbearia.pagamentos.repository;

import com.barbearia.pagamentos.entities.AssinaturaEntity;
import com.barbearia.pagamentos.entities.AssinaturaEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssinaturaRepository extends JpaRepository<AssinaturaEntity, AssinaturaEntityId> {
    int countAllByAtivoIsTrueAndIdCliente(Long idCliente);
}
