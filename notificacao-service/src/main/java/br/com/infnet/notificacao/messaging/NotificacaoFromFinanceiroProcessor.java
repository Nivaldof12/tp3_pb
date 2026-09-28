package br.com.infnet.notificacao.messaging;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.infnet.events.FinanceiroCriadoEvent;
import br.com.infnet.notificacao.domain.TipoNotificacao;
import br.com.infnet.notificacao.dto.NotificacaoRequest;
import br.com.infnet.notificacao.service.NotificacaoService;

@Component
public class NotificacaoFromFinanceiroProcessor {

    @Autowired
    private NotificacaoService notificacaoService;

    public void processar(FinanceiroCriadoEvent evento) {
        NotificacaoRequest request = new NotificacaoRequest();
        request.setUsuarioId(evento.getUsuarioId());

        if ("DESPESA".equalsIgnoreCase(evento.getTipoFinanceiro())) {
            request.setTitulo("Nova despesa registrada");
            request.setMensagem(String.format("Despesa de R$ %s em %s foi registrada.",
                    evento.getValor(), evento.getCategoria()));
            request.setTipo(evento.getValor().compareTo(new BigDecimal("1000")) >= 0
                    ? TipoNotificacao.ALERTA
                    : TipoNotificacao.INFO);
        } else {
            request.setTitulo("Nova receita registrada");
            request.setMensagem(String.format("Receita de R$ %s em %s foi registrada.",
                    evento.getValor(), evento.getCategoria()));
            request.setTipo(TipoNotificacao.SUCESSO);
        }

        notificacaoService.criar(request);
    }
}
