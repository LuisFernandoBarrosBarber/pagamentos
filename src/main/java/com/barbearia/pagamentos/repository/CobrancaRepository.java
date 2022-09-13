package com.barbearia.pagamentos.repository;

import com.barbearia.pagamentos.dto.asaas.enumerator.StatusCobranca;
import com.barbearia.pagamentos.entities.CobrancaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface CobrancaRepository extends JpaRepository<CobrancaEntity, Long> {

    Stream<CobrancaEntity> getByIdAssinaturaAndAtivoIsTrue(String a);

    Stream<CobrancaEntity> getByIdAssinaturaAndAtivoIsTrueAndStatus(String a, StatusCobranca s);

    Stream<CobrancaEntity> findByStatusInAndAtivoIsTrue(List<StatusCobranca> s);

    Optional<CobrancaEntity> findFirstByAtivoIsTrueAndIdAssinaturaAndStatusOrderByVencimentoEm(String idAssinatura,
            StatusCobranca status);

}
