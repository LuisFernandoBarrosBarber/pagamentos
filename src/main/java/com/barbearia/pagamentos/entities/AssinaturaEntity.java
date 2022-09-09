package com.barbearia.pagamentos.entities;

import com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

@Entity(name = "assinatura_asaas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(AssinaturaEntityId.class)
public class AssinaturaEntity implements Serializable {

    @Id
    private Long idCliente;

    @Id
    private String idAssinatura;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingTypeEnum formaPagamento;
}
