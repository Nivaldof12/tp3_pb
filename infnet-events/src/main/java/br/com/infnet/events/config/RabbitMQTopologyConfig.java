package br.com.infnet.events.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.infnet.events.RabbitMQConstants;

@Configuration
public class RabbitMQTopologyConfig {

    @Bean
    TopicExchange infnetEventsExchange() {
        return new TopicExchange(RabbitMQConstants.EXCHANGE_INFNET);
    }

    @Bean
    TopicExchange deadLetterExchange() {
        return new TopicExchange(RabbitMQConstants.DLX_INFNET);
    }

    @Bean
    Queue notificacaoFinanceiroQueue() {
        return QueueBuilder.durable(RabbitMQConstants.QUEUE_NOTIFICACAO_FINANCEIRO)
                .withArgument("x-dead-letter-exchange", RabbitMQConstants.DLX_INFNET)
                .withArgument("x-dead-letter-routing-key", RabbitMQConstants.RK_FINANCEIRO_CRIADO)
                .build();
    }

    @Bean
    Queue notificacaoDeadLetterQueue() {
        return QueueBuilder.durable(RabbitMQConstants.DLQ_NOTIFICACAO).build();
    }

    @Bean
    Queue auditoriaUsuarioQueue() {
        return QueueBuilder.durable(RabbitMQConstants.QUEUE_AUDITORIA_USUARIO).build();
    }

    @Bean
    Binding bindingNotificacaoFinanceiro(Queue notificacaoFinanceiroQueue, TopicExchange infnetEventsExchange) {
        return BindingBuilder.bind(notificacaoFinanceiroQueue)
                .to(infnetEventsExchange)
                .with("financeiro.criado.#");
    }

    @Bean
    Binding bindingAuditoriaUsuario(Queue auditoriaUsuarioQueue, TopicExchange infnetEventsExchange) {
        return BindingBuilder.bind(auditoriaUsuarioQueue)
                .to(infnetEventsExchange)
                .with(RabbitMQConstants.RK_USUARIO_CRIADO);
    }

    @Bean
    Binding bindingDeadLetter(Queue notificacaoDeadLetterQueue, TopicExchange deadLetterExchange) {
        return BindingBuilder.bind(notificacaoDeadLetterQueue)
                .to(deadLetterExchange)
                .with(RabbitMQConstants.RK_FINANCEIRO_CRIADO);
    }
}
