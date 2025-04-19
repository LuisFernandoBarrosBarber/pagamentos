package com.barbearia.pagamentos.dto.asaas;

import lombok.Builder;
import lombok.Data;

// https://docs.asaas.com/reference/criar-configuracao-para-emissao-de-notas-fiscais

@Builder
@Data
public class ConfigNotaFiscalAssinaturaDTO {
    @Builder.Default
    boolean updatePayment = false;

    @Builder.Default
    boolean receivedOnly = true;

    @Builder.Default
    String effectiveDatePeriod = "ON_PAYMENT_CONFIRMATION";

    @Builder.Default
    String municipalServiceId = "107761";

    @Builder.Default
    String municipalServiceCode = "17.02 | 8219-9/99 - Datilografia, digitação, estenografia, expediente, secretaria em geral, resposta audível, redação, edição, interpretação, revisão, tradução, apoio e infra- estrutura administrativa e congêneres.";

    @Builder.Default
    String municipalServiceName = "17.02 | 8219-9/99 - Datilografia, digitação, estenografia, expediente, secretaria em geral, resposta audível, redação, edição, interpretação, revisão, tradução, apoio e infra- estrutura administrativa e congêneres.";

    @Builder.Default
    TaxesNfDTO taxes = TaxesNfDTO.builder().build();
}
