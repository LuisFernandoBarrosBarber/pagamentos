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
    String email;
    String mobilePhone;
    String address; // RUA
    String addressNumber;
    String province; // BAIRRO
    String postalCode;
    String city;
    boolean notificationDisabled;
}
