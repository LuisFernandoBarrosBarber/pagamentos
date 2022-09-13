package com.barbearia.pagamentos.entities;

import com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum;
import com.barbearia.pagamentos.dto.asaas.enumerator.StatusCobranca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "cobranca_asaas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CobrancaEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String idAssinatura;

    @Column(nullable = false)
    private String idCobranca;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    private LocalDate vencimentoEm;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusCobranca status;

    @Column
    private LocalDateTime pagamentoEm;

    @Column
    private LocalDateTime lastUpdate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BillingTypeEnum tipoPagamento;

    @Column(nullable = false)
    private String invoiceUrl;

    @Column(nullable = false)
    private float valor;

}
