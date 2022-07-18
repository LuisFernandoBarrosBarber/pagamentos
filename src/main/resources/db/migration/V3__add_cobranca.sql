CREATE TABLE cobranca_asaas
(
    id             INT          NOT NULL AUTO_INCREMENT,
    id_assinatura  VARCHAR(255) NOT NULL,
    id_cobranca    VARCHAR(255) NOT NULL,
    ativo          BOOLEAN      NOT NULL,
    criado_em      datetime     NOT NULL,
    vencimento_em  datetime     NOT NULL,
    status         VARCHAR(100) NOT NULL,
    pagamento_em   datetime NULL,
    invoice_url    VARCHAR(500) NOT NULL,
    valor          FLOAT        NOT NULL,
    tipo_pagamento VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);
