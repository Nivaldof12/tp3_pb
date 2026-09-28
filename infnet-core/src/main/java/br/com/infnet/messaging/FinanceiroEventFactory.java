package br.com.infnet.messaging;

import br.com.infnet.events.FinanceiroCriadoEvent;
import br.com.infnet.events.RabbitMQConstants;
import br.com.infnet.model.domain.Financeiro;
import br.com.infnet.model.domain.TipoFinanceiro;

public final class FinanceiroEventFactory {

    private FinanceiroEventFactory() {
    }

    public static FinanceiroCriadoEvent criarEvento(Financeiro financeiro) {
        return new FinanceiroCriadoEvent(
                financeiro.getId(),
                financeiro.getUsuario().getId(),
                financeiro.getTipo().name(),
                financeiro.getCategoria(),
                financeiro.getDescricao(),
                financeiro.getValor(),
                financeiro.getData());
    }

    public static String routingKey(Financeiro financeiro) {
        if (financeiro.getTipo() == TipoFinanceiro.RECEITA) {
            return RabbitMQConstants.RK_FINANCEIRO_RECEITA;
        }
        return RabbitMQConstants.RK_FINANCEIRO_DESPESA;
    }
}
