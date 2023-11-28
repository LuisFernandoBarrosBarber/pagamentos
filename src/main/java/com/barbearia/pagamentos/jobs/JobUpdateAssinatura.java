package com.barbearia.pagamentos.jobs;

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.entities.AssinaturaEntity;
import com.barbearia.pagamentos.model.Cobranca;
import com.barbearia.pagamentos.repository.AssinaturaRepository;
import com.barbearia.pagamentos.service.ClienteService;
import com.barbearia.pagamentos.service.CobrancaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static java.time.LocalDateTime.now;

@EnableScheduling
@Component
@Slf4j
@RequiredArgsConstructor
public class JobUpdateAssinatura {

    private static final String A_CADA_MEIA_HORA = "59 */30 * * * *";
    private final AssinaturaRepository repository;
    private final AsaasClient asaasClient;
    private final CobrancaService cService;
    private final ClienteService clService;

    @Scheduled(cron = A_CADA_MEIA_HORA)
    @Transactional
    public void refreshAssinatura() {
        List<AssinaturaEntity> assinaturas = repository.findAllByAtivoIsTrue().toList();
        assinaturas
                .stream().filter(this::isVencidaHaMuitosDias)
                .forEach(this::inativaVencidas);
        assinaturas
                .stream().filter((this::assinaturaNuncaPagaEAtrasada))
                .forEach(this::inativaVencidas);
    }

    private void inativaVencidas(AssinaturaEntity a) {

        String idClienteAsaas = clService.getById(a.getIdCliente()).getIdAsaas();

        try {
            log.info("INATIVANDO CLIENTE DE ID: " + a.getIdCliente());
// POR SEGURANÇA, AS 03 LINHAS ABAIXO FORAM COMENTADAS
//            asaasClient.deleteCliente(idClienteAsaas);
//            a.setAtivo(false);
//            clService.inativar(a.getIdCliente());
            Thread.sleep(1000L);
        } catch (Exception ignored) {
            log.error("ERRO AO INATIVAR CLIENTE.");
        }
    }

    private boolean isVencidaHaMuitosDias(AssinaturaEntity a) {
        LocalDateTime dtLastPagamento = cService.getLastCobrancaPagaByAssinatura(a.getIdAssinatura()).getPagamentoEm();
        return dtLastPagamento != null && dtLastPagamento.isBefore(now().minusDays(60));
    }

    private boolean assinaturaNuncaPagaEAtrasada(AssinaturaEntity a) {
        List<Cobranca> cs = cService.getByAssinatura(a.getIdAssinatura());

        boolean temCobrancaPaga = cs.stream()
                .anyMatch(it -> it.pagamentoEm != null);

        LocalDate dtLastVencimento = cs.stream()
                .min(Comparator.comparing(Cobranca::getVencimentoEm))
                .get().getVencimentoEm();

        return !temCobrancaPaga && dtLastVencimento.isBefore(LocalDate.now().minusDays(60));
    }
}
