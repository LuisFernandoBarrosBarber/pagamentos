package com.barbearia.pagamentos.configuration.excetion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErroAsaas {
    private List<DataErroAsaas> errors;
}
