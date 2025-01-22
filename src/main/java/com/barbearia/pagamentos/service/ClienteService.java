package com.barbearia.pagamentos.service;

import com.barbearia.pagamentos.client.AsaasClient;
import com.barbearia.pagamentos.configuration.excetion.ResourceNotFoundException;
import com.barbearia.pagamentos.converter.ClienteEntityToCliente;
import com.barbearia.pagamentos.dto.asaas.ClienteDTO;
import com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum;
import com.barbearia.pagamentos.entities.ClienteEntity;
import com.barbearia.pagamentos.model.Assinatura;
import com.barbearia.pagamentos.model.Cliente;
import com.barbearia.pagamentos.model.Cobranca;
import com.barbearia.pagamentos.model.asaas.AsaasCliente;
import com.barbearia.pagamentos.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.barbearia.pagamentos.dto.asaas.enumerator.BillingTypeEnum.PIX;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {

    private final AsaasClient asaasClient;
    private final ClienteRepository repo;
    private final ClienteEntityToCliente toCliente;
    private final CobrancaService cobrancaService;
    private final AssinaturaService assinaturaService;

    @Transactional
    public Cliente novo(String nome, Long id, String cpf, String barbeariaNome,
                        String cep, String rua, String bairro, String cidade, String numero, String email,
                        String telefone) {
        // DESABILITADO POR QUE ALGUNS CLIENTES ESTAVAM COLOCANDO CARACTER ESPECIAL NO NOME DA BARBEARIA
        // E O ASAAS NÃO ESTAVA ACEITANDO
        barbeariaNome = "Nome da barbearia";

        if (alreadyCadastrado(id)) {
            Cliente c = repo.findById(id).map(toCliente).orElse(Cliente.builder().build());

            asaasClient.atualizarCliente(c.getIdAsaas(), getDTO(nome, id, cpf, barbeariaNome, cep, rua, bairro,
                    cidade, numero, email, telefone));

            return c;
        }

        try {
            AsaasCliente c = asaasClient.novoCliente(getDTO(nome, id, cpf, barbeariaNome, cep, rua, bairro,
                    cidade, numero, email, telefone));
            ClienteEntity entity = save(id, c.getId());
            return toCliente.apply(entity);
        } catch (Exception e) {
            log.info(getDTO(nome, id, cpf, barbeariaNome, cep, rua, bairro, cidade, numero,
                    email, telefone).toString());
            log.error("ERRO AO SALVAR CLIENTE. CLIENTE ID: " + id, e);
            throw e;
        }
    }

    @Transactional
    public Assinatura updateAssinatura(Long id, float value, BillingTypeEnum fp) {
        testClienteExists(id);
        Assinatura a = assinaturaService.getByCliente(id);
        return assinaturaService.updateAssinatura(a.getId(), value, fp);
    }

    public Assinatura getAssinatura(List<Long> ids) {
        return assinaturaService.getByAllProfissionaisDaBarbearia(ids);
    }

    @Transactional
    public List<Cliente> getVenceEm(LocalDate dv) {
        return repo.findAllByAtivoIsTrueAndAssinaturaIsAtivo().filter(c -> {
                    List<Cobranca> cs = getCobrancasByCliente(c.getId());
                    return cs
                            .stream()
                            .anyMatch(co ->
                                    co.vencimentoEm.equals(dv)
                                            && co.tipoPagamento.equals(PIX)
                                            && cobrancaService.isOnlyUmaCobrancaPendente(cs));
                })
                .map(toCliente)
                .collect(Collectors.toList());
    }

    public List<Cobranca> getCobrancasByClientes(List<Long> ids) {
        Assinatura a = assinaturaService.getByAllProfissionaisDaBarbearia(ids);
        return cobrancaService.getByAssinatura(a.getId());
    }

    public List<Cobranca> getCobrancasByCliente(Long id) {
        Assinatura a = assinaturaService.getByCliente(id);
        return cobrancaService.getByAssinatura(a.getId());
    }

    public Cobranca getLastCobrancaPaga(Long id) {
        testClienteExists(id);
        Assinatura a = assinaturaService.getByCliente(id);
        return cobrancaService.getLastCobrancaPagaByAssinatura(a.getId());
    }

    public ClienteEntity save(Long id, String idAssas) {
        ClienteEntity c = new ClienteEntity();
        c.setAtivo(true);
        c.setCriadoEm(now());
        c.setId(id);
        c.setIdAsaas(idAssas);
        return repo.save(c);
    }

    public Cliente getById(Long id) {
        return repo.findById(id).map(toCliente).orElse(null);
    }

    public void inativar(Long id) {
        repo.findById(id).ifPresent(it -> it.setAtivo(false));
    }

    private boolean alreadyCadastrado(Long id) {
        return repo.countAllByIdAndAtivoIsTrueAndIdAsaasNotNull(id) > 0;
    }

    private void testClienteExists(Long id) {
        if (repo.findByIdAndAtivoIsTrue(id).isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado ou inativado.");
        }
    }

    private static ClienteDTO getDTO(String nome, Long id, String cpf, String barbeariaNome,
                                     String cep, String rua, String bairro, String cidade, String numero,
                                     String email, String telefone) {
        return ClienteDTO
                .builder()
                .name(nome)
                .externalReference(id.toString()).cpfCnpj(cpf)
                .company(barbeariaNome)
                .notificationDisabled(true)
                .province(bairro)
                .city(cidade)
                .postalCode(formatarCep(cep))
                .addressNumber(numero)
                .address(rua)
                .email(email)
                .mobilePhone(telefone)
                .build();
    }

    public static String formatarCep(String cep) {
        if (cep != null && cep.length() == 8) {
            return cep.substring(0, 5) + "-" + cep.substring(5);
        }
        throw new RuntimeException("ERRO AO FORMATAR CEP: " + cep);
    }
}
