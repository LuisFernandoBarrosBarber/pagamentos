package com.barbearia.pagamentos.jobs;

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.entities.CobrancaEntity;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancaData;
import com.barbearia.pagamentos.repository.CobrancaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import static com.barbearia.pagamentos.dto.asaas.enumerator.StatusCobranca.*;
import static java.time.LocalDateTime.now;

@EnableScheduling
@Component
@Slf4j
@RequiredArgsConstructor
public class JobUpdatePagamento {

    private static final String A_CADA_MINUTO = "59 */1 * * * *";

    private final CobrancaRepository repository;
    private final AsaasClient asaasClient;

    @Scheduled(cron = A_CADA_MINUTO)
    @Transactional
    public void refreshCobrancas() {
        List<CobrancaEntity> cobrancas =
                repository.findByStatusInAndAtivoIsTrue(Arrays.asList(PENDING, OVERDUE, CONFIRMED)).toList();
        log.info("ENCONTRADAS " + cobrancas.size() + " COBRANCAS PENDENTES PARA PROCESSAR.");
        cobrancas.forEach(this::updateStatus);
    }

    private void updateStatus(CobrancaEntity e) {
        try {
            Thread.sleep(1000L);
            AsaasCobrancaData c = asaasClient.getCobranca(e.getIdCobranca());
            e.setStatus(c.getStatus());
            e.setPagamentoEm(getDataPagamento(c));
            e.setTipoPagamento(c.getBillingType());
            e.setAtivo(!c.isDeleted());
        } catch (FeignException.NotFound ignored) {
            log.info("COBRANCA NÃO ENCONTRADO NO ASAAS: " + e.getIdAssinatura());
        } catch (Exception ex) {
            log.error("ERRO AO TENTAR ATUALIZAR COBRANCA ID: " + e.getIdCobranca(), ex);
        }
        e.setLastUpdate(now());
    }

    private LocalDateTime getDataPagamento(AsaasCobrancaData c) {
        return c.getPaymentDate() != null ?
                c.getPaymentDate().atTime(LocalTime.from(now())) :
                null;
    }
}
