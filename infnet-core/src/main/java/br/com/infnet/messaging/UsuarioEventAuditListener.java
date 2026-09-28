package br.com.infnet.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.infnet.events.RabbitMQConstants;
import br.com.infnet.events.UsuarioCriadoEvent;

@Component
public class UsuarioEventAuditListener {

    private static final Logger log = LoggerFactory.getLogger(UsuarioEventAuditListener.class);

    @RabbitListener(queues = RabbitMQConstants.QUEUE_AUDITORIA_USUARIO)
    public void auditarUsuarioCriado(UsuarioCriadoEvent evento) {
        log.info("[Auditoria] Usuario criado via evento: id={}, nome={}, email={}, eventId={}",
                evento.getUsuarioId(), evento.getNome(), evento.getEmail(), evento.getEventId());
    }
}
