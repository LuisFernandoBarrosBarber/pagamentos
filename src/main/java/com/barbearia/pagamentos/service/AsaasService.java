package com.barbearia.pagamentos.service;


import com.barbearia.pagamentos.model.asaasV2.AsaasCobranca;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsaasService {

    private final CobrancaService cService;

    public ResponseEntity newEvent(AsaasCobranca c) {
        log.info("NOVO EVENTO RECEBIDO DO ASAAS");
        ResponseEntity<String> response = ResponseEntity.ok("");
        try {
            cService.findByIdAsaas(c.getPayment().getId())
                    .ifPresentOrElse(
                            ce -> cService.update(ce, c.getPayment()),
                            () -> cService.nova(c.getPayment())
                    );
        } catch (Exception ex) {
            log.error("ERRO AO TENTAR ATUALIZAR COBRANCA ID: " + c.getPayment().getId(), ex);
            response = ResponseEntity.status(500).body(ex.getMessage());
        }

        return response;
    }
}
