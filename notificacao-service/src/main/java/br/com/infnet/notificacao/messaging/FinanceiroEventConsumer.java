package br.com.infnet.notificacao.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.infnet.events.FinanceiroCriadoEvent;
import br.com.infnet.events.RabbitMQConstants;

@Component
public class FinanceiroEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(FinanceiroEventConsumer.class);

    @Autowired
    private NotificacaoFromFinanceiroProcessor processor;

    @RabbitListener(queues = RabbitMQConstants.QUEUE_NOTIFICACAO_FINANCEIRO)
    public void consumirFinanceiroCriado(FinanceiroCriadoEvent evento) {
        log.info("Evento recebido: FinanceiroCriado id={}, eventId={}, usuarioId={}",
                evento.getFinanceiroId(), evento.getEventId(), evento.getUsuarioId());
        processor.processar(evento);
    }
}
