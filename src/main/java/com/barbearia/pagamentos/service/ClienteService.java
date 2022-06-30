package com.barbearia.pagamentos.service;

import com.barbearia.pagamentos.client.AsaasClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final AsaasClient asaasClient;

    public String novo(){
        //asaasClient.novoCliente(ClienteDTO.builder().name("Integração").build());
        asaasClient.getCobranca("asdasdasdsa");

        return "SALVO";
    }

}
