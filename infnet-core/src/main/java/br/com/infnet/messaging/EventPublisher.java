package br.com.infnet.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.infnet.events.DomainEvent;
import br.com.infnet.events.RabbitMQConstants;

@Service
public class EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publicar(DomainEvent evento, String routingKey) {
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_INFNET, routingKey, evento);
        log.info("Evento publicado: tipo={}, id={}, routingKey={}",
                evento.getTipoEvento(), evento.getEventId(), routingKey);
    }
}
