package com.barbearia.pagamentos.jobs;

/*
 * ESTA CLASSE INATIVA CLIENTES NO BANCO DE DADOS, CASO ELA TENHA SIDO EXCLUÍDO NO ASAAS.
 * */

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.entities.ClienteEntity;
import com.barbearia.pagamentos.model.asaas.AsaasCliente;
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
public class JobUpdateClienteBanco {

    private static final String A_CADA_TRINTA_MINUTOS = "59 */30 * * * *";
    private final ClienteRepository repository;
    private final AsaasClient asaasClient;

    @Scheduled(cron = A_CADA_TRINTA_MINUTOS)
    @Transactional
    public void refreshCliente() {
        List<ClienteEntity> clientes = repository.findAllByAtivoIsTrue().toList();
        clientes.forEach(this::updateStatus);
    }

    private void updateStatus(ClienteEntity c) {
        AsaasCliente ac = getClienteInAsaas(c.getIdAsaas());
        if (ac != null && ac.isDeleted()) {
            log.info("INATIVANDO CLIENTE DE ID: {}", c.getIdAsaas());
            c.setAtivo(!ac.isDeleted());
        }

    }

    private AsaasCliente getClienteInAsaas(String idAsaas) {
        try {
            Thread.sleep(3000L);
            return asaasClient.getCliente(idAsaas);
        } catch (FeignException.NotFound e) {
            log.info("CLIENTE NÃO ENCONTRADO NO ASAAS: {}", idAsaas);
        } catch (Exception ex) {
            log.error("ERRO AO CONSULTAR CLIENTE ID: {}", idAsaas, ex);
        }
        return null;
    }
}
