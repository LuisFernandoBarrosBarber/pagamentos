package com.barbearia.pagamentos.converter;

import com.barbearia.pagamentos.entities.ClienteEntity;
import com.barbearia.pagamentos.model.Cliente;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ClienteEntityToCliente implements Function<ClienteEntity, Cliente> {

    @Override
    public Cliente apply(ClienteEntity c) {
        return Cliente.builder()
                .id(c.getId())
                .idAsaas(c.getIdAsaas())
                .ativo(c.isAtivo())
                .criadoEm(c.getCriadoEm())
                .build();
    }
}
