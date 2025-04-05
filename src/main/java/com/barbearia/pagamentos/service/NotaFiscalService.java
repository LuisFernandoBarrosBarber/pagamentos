package com.barbearia.pagamentos.service;

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.dto.asaas.ConfigNotaFiscalAssinaturaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class NotaFiscalService {

    private final AsaasClient asaasClient;

    @Transactional
    public void newConfigNF(String idAssinatura) {
        ConfigNotaFiscalAssinaturaDTO dto = ConfigNotaFiscalAssinaturaDTO.builder().build();
        try {
            asaasClient.setNewConfigNotaFiscal(idAssinatura, dto);
        } catch (Exception e) {
            log.error("ERRO AO GERAR CONFIGURACAO DE NOTA FISCAL. ID ASSINATURA: " + idAssinatura, e);
        }
    }
}
