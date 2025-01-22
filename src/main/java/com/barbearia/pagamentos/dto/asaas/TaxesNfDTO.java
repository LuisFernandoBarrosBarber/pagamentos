package com.barbearia.pagamentos.dto.asaas;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaxesNfDTO {

    @Builder.Default
    boolean retainIss = false;
    @Builder.Default
    int cofins = 0;
    @Builder.Default
    int csll = 0;
    @Builder.Default
    int inss = 0;
    @Builder.Default
    int ir = 0;
    @Builder.Default
    int pis = 0;
    @Builder.Default
    int iss = 0;
}
