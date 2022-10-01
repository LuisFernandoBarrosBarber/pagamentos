package com.barbearia.pagamentos.jobs;

import com.barbearia.pagamentos.entities.AssinaturaEntity;
import com.barbearia.pagamentos.model.asaas.AsaasCobrancaData;
import com.barbearia.pagamentos.repository.AssinaturaRepository;
import com.barbearia.pagamentos.repository.CobrancaRepository;
import com.barbearia.pagamentos.service.AssinaturaService;
import com.barbearia.pagamentos.service.CobrancaService;
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
public class JobSearchCobrancas {

    private static final String A_CADA_HORA = "59 59 */1 * * *";
    private final CobrancaRepository cRepo;
    private final AssinaturaRepository aRepo;
    private final AssinaturaService aService;
    private final CobrancaService cService;

    @Scheduled(cron = A_CADA_HORA)
    @Transactional
    public void refreshCobrancasInAssinaturas() {
        List<AssinaturaEntity> assinaturas =
                aRepo.findAllByAtivoIsTrue()
                        .toList();

        log.info("ENCONTRADAS " + assinaturas.size() + " ASSINATURAS ATIVAS PARA ATUALIZAR.");

        assinaturas
                .forEach(this::getCobrancasInAsaas);
    }

    private void getCobrancasInAsaas(AssinaturaEntity e) {
        try {
            Thread.sleep(1000L);
            aService.getCobrancasByAssinatura(e.getIdAssinatura())
                    .getData()
                    .stream()
                    .filter(this::isFaturaNotInDB)
                    .forEach(cService::nova);


        } catch (Exception ex) {
            log.error("ERRO AO TENTAR ATUALIZAR ASSINATURA ID: " + e.getIdAssinatura(), ex);
        }
    }

    private boolean isFaturaNotInDB(AsaasCobrancaData c) {
        return cRepo.findByIdCobranca(c.getId()).isEmpty();
    }
}
