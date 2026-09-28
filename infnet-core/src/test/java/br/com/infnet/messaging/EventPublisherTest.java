package br.com.infnet.messaging;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import br.com.infnet.events.FinanceiroCriadoEvent;
import br.com.infnet.events.RabbitMQConstants;

@ExtendWith(MockitoExtension.class)
class EventPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private EventPublisher eventPublisher;

    @Test
    void deveEnviarEventoParaExchangeComRoutingKey() {
        FinanceiroCriadoEvent evento = new FinanceiroCriadoEvent(
                1L, 2L, "DESPESA", "Mercado", "Compras", new BigDecimal("50.00"), LocalDate.now());

        eventPublisher.publicar(evento, RabbitMQConstants.RK_FINANCEIRO_DESPESA);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConstants.EXCHANGE_INFNET),
                eq(RabbitMQConstants.RK_FINANCEIRO_DESPESA),
                eq(evento));
    }
}
