package com.barbearia.pagamentos.service;

import com.barbearia.pagamentos.entities.CobrancaEntity;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancaData;
import com.barbearia.pagamentos.repository.CobrancaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class CobrancaService {

    private final CobrancaRepository repo;

    @Transactional
    public void nova(AsaasCobrancaData c) {
        repo.save(getEntity(c));
    }

    private CobrancaEntity getEntity(AsaasCobrancaData ca) {
        CobrancaEntity c = new CobrancaEntity();
        c.setIdCobranca(ca.getId());
        c.setAtivo(true);
        c.setCriadoEm(now());
        c.setIdAssinatura(ca.getSubscription());
        c.setVencimentoEm(ca.getDueDate());
        c.setStatus(ca.getStatus());
        return c;
    }

}
