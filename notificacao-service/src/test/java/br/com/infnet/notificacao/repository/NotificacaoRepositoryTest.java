package br.com.infnet.notificacao.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import br.com.infnet.notificacao.domain.Notificacao;
import br.com.infnet.notificacao.domain.TipoNotificacao;
import br.com.infnet.notificacao.dto.NotificacaoRequest;
import br.com.infnet.notificacao.service.NotificacaoService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NotificacaoRepositoryTest {

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Test
    void deveSalvarNotificacao() {
        NotificacaoRequest request = new NotificacaoRequest();
        request.setUsuarioId(1L);
        request.setTitulo("Alerta de despesa");
        request.setMensagem("Despesa alta registrada");
        request.setTipo(TipoNotificacao.ALERTA);

        Notificacao salva = notificacaoService.criar(request);

        assertThat(salva.getId()).isNotNull();
        assertThat(salva.getDataCriacao()).isNotNull();
        assertThat(notificacaoRepository.findById(salva.getId())).isPresent();
    }

    @Test
    void deveListarNotificacoesPorUsuario() {
        criarNotificacao(1L, "Info 1", TipoNotificacao.INFO);
        criarNotificacao(1L, "Info 2", TipoNotificacao.SUCESSO);
        criarNotificacao(2L, "Outro usuario", TipoNotificacao.INFO);

        assertThat(notificacaoService.listarPorUsuario(1L)).hasSize(2);
        assertThat(notificacaoService.listarPorUsuario(2L)).hasSize(1);
    }

    @Test
    void deveContarNotificacoesNaoLidas() {
        Notificacao n1 = criarNotificacao(5L, "N1", TipoNotificacao.ALERTA);
        criarNotificacao(5L, "N2", TipoNotificacao.INFO);

        assertThat(notificacaoService.contarNaoLidas(5L)).isEqualTo(2);

        notificacaoService.marcarComoLida(n1.getId());
        assertThat(notificacaoService.contarNaoLidas(5L)).isEqualTo(1);
    }

    @Test
    void deveMarcarNotificacaoComoLida() {
        Notificacao notificacao = criarNotificacao(3L, "Teste", TipoNotificacao.INFO);

        Notificacao atualizada = notificacaoService.marcarComoLida(notificacao.getId()).orElseThrow();

        assertThat(atualizada.isLida()).isTrue();
    }

    private Notificacao criarNotificacao(Long usuarioId, String titulo, TipoNotificacao tipo) {
        NotificacaoRequest request = new NotificacaoRequest();
        request.setUsuarioId(usuarioId);
        request.setTitulo(titulo);
        request.setMensagem("Mensagem de teste");
        request.setTipo(tipo);
        return notificacaoService.criar(request);
    }
}
