package com.barbearia.pagamentos.repository;

import com.barbearia.pagamentos.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Long countAllByIdAndAtivoIsTrueAndIdAsaasNotNull(Long id);

    Optional<ClienteEntity> findByIdAndAtivoIsTrue(Long id);

    Stream<ClienteEntity> findAllByAtivoIsTrue();

    @Query("SELECT c " +
            "FROM cliente_asaas c, assinatura_asaas a " +
            "WHERE c.id = a.idCliente AND c.ativo = true AND a.ativo = TRUE")
    Stream<ClienteEntity> findAllByAtivoIsTrueAndAssinaturaIsAtivo();
}
