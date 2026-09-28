package br.com.infnet.events;

public final class RabbitMQConstants {

    private RabbitMQConstants() {
    }

    public static final String EXCHANGE_INFNET = "infnet.events.topic";

    public static final String QUEUE_NOTIFICACAO_FINANCEIRO = "notificacao.financeiro.queue";
    public static final String QUEUE_AUDITORIA_USUARIO = "auditoria.usuario.queue";

    public static final String DLX_INFNET = "infnet.events.dlx";
    public static final String DLQ_NOTIFICACAO = "notificacao.financeiro.dlq";

    public static final String RK_FINANCEIRO_CRIADO = "financeiro.criado";
    public static final String RK_FINANCEIRO_RECEITA = "financeiro.criado.receita";
    public static final String RK_FINANCEIRO_DESPESA = "financeiro.criado.despesa";
    public static final String RK_USUARIO_CRIADO = "usuario.criado";
}
