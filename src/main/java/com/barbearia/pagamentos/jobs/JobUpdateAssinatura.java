package com.barbearia.pagamentos.jobs;

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.entities.AssinaturaEntity;
import com.barbearia.pagamentos.entities.CobrancaEntity;
import com.barbearia.pagamentos.model.asaas.AsaasAssinatura;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancaData;
import com.barbearia.pagamentos.repository.AssinaturaRepository;
import com.barbearia.pagamentos.repository.CobrancaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.barbearia.pagamentos.dto.asaas.enumerator.StatusCobranca.*;
import static java.time.LocalDateTime.now;

@EnableScheduling
@Component
@Slf4j
@RequiredArgsConstructor
public class JobUpdateAssinatura {

    private static final String A_CADA_MEIA_HORA = "59 */30 * * * *";
    private final AssinaturaRepository repository;
    private final AsaasClient asaasClient;

    @Scheduled(cron = A_CADA_MEIA_HORA)
    @Transactional
    public void refreshAssinatura() {
        List<AssinaturaEntity> assinaturas = repository.findAllByAtivoIsTrue().toList();
        assinaturas.forEach(this::updateStatus);
    }

    private void updateStatus(AssinaturaEntity a) {
        try {
            Thread.sleep(1000L);
            AsaasAssinatura aa = asaasClient.getAssinatura(a.getIdAssinatura());
            if (aa.isDeleted()) {
                log.info("INATIVANDO ASSINATURA DE ID: " + a.getIdAssinatura());
                a.setAtivo(!aa.isDeleted());
            }
        } catch (FeignException.NotFound ignored) {
            log.info("ASSINATURA NÃO ENCONTRADO NO ASAAS: " + a.getIdAssinatura());
        } catch (Exception ex) {
            log.error("ERRO AO CONSULTAR ASSINATURA ID: " + a.getIdAssinatura(), ex);
        }
    }
}
