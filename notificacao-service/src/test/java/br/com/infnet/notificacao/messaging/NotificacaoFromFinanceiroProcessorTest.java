package br.com.infnet.notificacao.messaging;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import br.com.infnet.events.FinanceiroCriadoEvent;
import br.com.infnet.notificacao.domain.TipoNotificacao;
import br.com.infnet.notificacao.repository.NotificacaoRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NotificacaoFromFinanceiroProcessorTest {

    @Autowired
    private NotificacaoFromFinanceiroProcessor processor;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Test
    void deveCriarNotificacaoDeReceita() {
        FinanceiroCriadoEvent evento = new FinanceiroCriadoEvent(
                10L, 1L, "RECEITA", "Salario", "Pagamento", new BigDecimal("3000.00"), LocalDate.now());

        processor.processar(evento);

        assertThat(notificacaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(1L))
                .anyMatch(n -> n.getTipo() == TipoNotificacao.SUCESSO);
    }

    @Test
    void deveCriarAlertaParaDespesaAlta() {
        FinanceiroCriadoEvent evento = new FinanceiroCriadoEvent(
                11L, 2L, "DESPESA", "Carro", "Parcela", new BigDecimal("1500.00"), LocalDate.now());

        processor.processar(evento);

        assertThat(notificacaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(2L))
                .anyMatch(n -> n.getTipo() == TipoNotificacao.ALERTA);
    }
}
