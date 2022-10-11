package com.barbearia.pagamentos.jobs;

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.entities.AssinaturaEntity;
import com.barbearia.pagamentos.entities.ClienteEntity;
import com.barbearia.pagamentos.model.asaas.AsaasAssinatura;
import com.barbearia.pagamentos.model.asaas.AsaasCliente;
import com.barbearia.pagamentos.repository.AssinaturaRepository;
import com.barbearia.pagamentos.repository.ClienteRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@EnableScheduling
@Component
@Slf4j
@RequiredArgsConstructor
public class JobUpdateCliente {

    private static final String A_CADA_MEIA_HORA = "59 */30 * * * *";
    private final ClienteRepository repository;
    private final AsaasClient asaasClient;

    @Scheduled(cron = A_CADA_MEIA_HORA)
    @Transactional
    public void refreshCliente() {
        List<ClienteEntity> clientes = repository.findAllByAtivoIsTrue().toList();
        clientes.forEach(this::updateStatus);
    }

    private void updateStatus(ClienteEntity c) {
        try {
            Thread.sleep(1000L);
            AsaasCliente ac = asaasClient.getCliente(c.getIdAsaas());
            if (ac.isDeleted()) {
                log.info("INATIVANDO CLIENTE DE ID: " + c.getIdAsaas());
                c.setAtivo(!ac.isDeleted());
            }
        } catch (FeignException.NotFound e) {
            log.info("CLIENTE NÃO ENCONTRADO NO ASAAS: " + c.getIdAsaas());
        } catch (Exception ex) {
            log.error("ERRO AO CONSULTAR CLIENTE ID: " + c.getIdAsaas(), ex);
        }
    }
}
