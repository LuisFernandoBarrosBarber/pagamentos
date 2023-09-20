package com.barbearia.pagamentos.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
class WebConfig implements Filter {

    List<String> acceptedTokens;

    @Autowired
    public WebConfig(AppConfigs configs) {
        this.acceptedTokens = configs.getAcceptedTokens();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {

        if (checkToken((RequestFacade) req)) {
            chain.doFilter(req, res);
        } else {
            log.info("SOLICITAÇÃO BLOQUEADA");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    private boolean checkToken(RequestFacade req) {
        return acceptedTokens
                .stream()
                .anyMatch(it -> it.equals(req.getHeader("Authorization"))
                        || it.equals(req.getHeader("asaas-access-token")));
    }

}
