package com.barbearia.pagamentos.service;

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.dto.asaas.AssinaturaDTO;
import com.barbearia.pagamentos.entities.AssinaturaEntity;
import com.barbearia.pagamentos.model.Cliente;
import com.barbearia.pagamentos.model.asaas.AsaasAssinatura;
import com.barbearia.pagamentos.repository.AssinaturaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import javax.transaction.Transactional;

import static com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum.UNDEFINED;
import static com.barbearia.pagamentos.dto.asaas.enumerator.CycleEnum.MONTHLY;
import static java.time.LocalDateTime.now;

@Service
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class AssinaturaService {

    private final AssinaturaRepository repo;
    private final AsaasClient asaasClient;

    @Transactional
    public void assinar(Cliente c) {

        check(c);

        try {
            AsaasAssinatura assinatura = asaasClient.novaAssinatura(getDTO(c));
            save(assinatura, c);
        } catch (Exception e) {
            log.error("ERRO AO GERAR ASSINATURA DO CLIENTE " + c.getId(), e);
        }


    }

    private static AssinaturaDTO getDTO(Cliente c) {
        return AssinaturaDTO.builder()
                .billingType(UNDEFINED)
                .value(30)
                .customer(c.getIdAsaas())
                .cycle(MONTHLY)
                .description("Assinatura do plano Barbeiro Agenda.")
                .nextDueDate(LocalDate.now().plusDays(30))
                .build();
    }

    @Transactional
    public void save(AsaasAssinatura a, Cliente c) {
        AssinaturaEntity entity = new AssinaturaEntity();
        entity.setIdAssinatura(a.getId());
        entity.setIdCliente(c.getId());
        entity.setAtivo(true);
        entity.setCriadoEm(now());
        repo.save(entity);
    }

    private void check(Cliente c) {
        if (repo.countAllByAtivoIsTrueAndIdCliente(c.getId()) > 0) {
            throw new RuntimeException("Cliente já possuí assinatura");
        }
    }

}
