package com.barbearia.pagamentos.dto.asaas;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClienteDTO {
    String name;
    String externalReference;
    String cpfCnpj;
    String company;
}
