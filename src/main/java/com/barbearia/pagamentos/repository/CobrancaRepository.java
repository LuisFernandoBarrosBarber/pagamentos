package com.barbearia.pagamentos.repository;

import com.barbearia.pagamentos.dto.asaas.enumerator.StatusCobranca;
import com.barbearia.pagamentos.entities.CobrancaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface CobrancaRepository extends JpaRepository<CobrancaEntity, Long> {

    Stream<CobrancaEntity> getByIdAssinaturaAndAtivoIsTrue(String a);

    Stream<CobrancaEntity> findByStatusAndAtivoIsTrue(StatusCobranca s);

}
