package com.barbearia.pagamentos.configuration.excetion;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ErrorUtils {

    public String extract(Exception e) {
        String tratado = e.getMessage().split(":", 3)[2];
        try {
            ErroAsaas[] erros = new ObjectMapper().readValue(tratado, ErroAsaas[].class);
            return erros[0].getErrors().get(0).getDescription();
        } catch (Exception ex) {
            log.error("ERRO DESCONHECIDO DO ASAAS ABAIXO: ");
            log.error(ex.getMessage());
            return "Erro do Asass.";
        }
    }
}
