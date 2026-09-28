package br.com.infnet.model.Service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.infnet.client.NotificacaoClient;
import br.com.infnet.messaging.EventPublisher;
import br.com.infnet.model.Repository.FinanceiroRepository;
import br.com.infnet.model.Repository.UsuarioRepository;
import br.com.infnet.model.domain.Financeiro;
import br.com.infnet.model.domain.TipoFinanceiro;
import br.com.infnet.model.domain.Usuario;
import br.com.infnet.model.dto.HistoricoRevisaoDTO;

@SpringBootTest
@ActiveProfiles("test")
class HistoricoServiceTest {

    @MockBean
    private NotificacaoClient notificacaoClient;

    @MockBean
    private EventPublisher eventPublisher;

    @Autowired
    private HistoricoService historicoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    void deveRegistrarHistoricoDeCriacaoDeUsuario() {
        Usuario usuario = salvarUsuario("Ana Costa", "ana@email.com");
        var historico = historicoService.buscarHistoricoUsuario(usuario.getId());

        assertThat(historico).isNotEmpty();
        assertThat(historico.get(0).getTipoOperacao()).isEqualTo("CRIACAO");
        assertThat(historico.get(0).getEntidade().getNome()).isEqualTo("Ana Costa");
    }

    @Test
    void deveRegistrarHistoricoDeAtualizacaoDeUsuario() {
        Usuario usuario = salvarUsuario("Ana Costa", "ana2@email.com");

        transactionTemplate.execute(status -> {
            Usuario gerenciado = usuarioRepository.findById(usuario.getId()).orElseThrow();
            gerenciado.setNome("Ana Costa Atualizada");
            return null;
        });

        var historico = historicoService.buscarHistoricoUsuario(usuario.getId());

        assertThat(historico).hasSizeGreaterThanOrEqualTo(2);
        assertThat(historico.get(historico.size() - 1).getTipoOperacao()).isEqualTo("ATUALIZACAO");
        assertThat(historico.get(historico.size() - 1).getEntidade().getNome()).isEqualTo("Ana Costa Atualizada");
    }

    @Test
    void deveRegistrarHistoricoDeCriacaoDeFinanceiro() {
        Financeiro financeiro = salvarFinanceiro("ana3@email.com", new BigDecimal("800.00"));

        var historico = historicoService.buscarHistoricoFinanceiro(financeiro.getId());

        assertThat(historico).isNotEmpty();
        assertThat(historico.get(0).getTipoOperacao()).isEqualTo("CRIACAO");
        assertThat(historico.get(0).getEntidade().getValor()).isEqualByComparingTo(new BigDecimal("800.00"));
    }

    @Test
    void deveRegistrarHistoricoDeAtualizacaoDeFinanceiro() {
        Financeiro financeiro = salvarFinanceiro("ana4@email.com", new BigDecimal("800.00"));

        transactionTemplate.execute(status -> {
            Financeiro gerenciado = financeiroRepository.findById(financeiro.getId()).orElseThrow();
            gerenciado.setValor(new BigDecimal("950.00"));
            gerenciado.setDescricao("Mensalidade atualizada");
            return null;
        });

        var historico = historicoService.buscarHistoricoFinanceiro(financeiro.getId());

        assertThat(historico).hasSizeGreaterThanOrEqualTo(2);
        HistoricoRevisaoDTO<Financeiro> ultimaRevisao = historico.get(historico.size() - 1);
        assertThat(ultimaRevisao.getTipoOperacao()).isEqualTo("ATUALIZACAO");
        assertThat(ultimaRevisao.getEntidade().getValor()).isEqualByComparingTo(new BigDecimal("950.00"));
    }

    @Test
    void deveRegistrarHistoricoDeExclusaoDeFinanceiro() {
        Financeiro financeiro = salvarFinanceiro("ana5@email.com", new BigDecimal("800.00"));
        Long id = financeiro.getId();

        transactionTemplate.execute(status -> {
            financeiroRepository.deleteById(id);
            return null;
        });

        var historico = historicoService.buscarHistoricoFinanceiro(id);

        assertThat(historico).isNotEmpty();
        assertThat(historico.get(historico.size() - 1).getTipoOperacao()).isEqualTo("EXCLUSAO");
    }

    private Usuario salvarUsuario(String nome, String email) {
        return transactionTemplate.execute(status -> {
            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha("senha321");
            return usuarioRepository.save(usuario);
        });
    }

    private Financeiro salvarFinanceiro(String email, BigDecimal valor) {
        return transactionTemplate.execute(status -> {
            Usuario usuario = new Usuario();
            usuario.setNome("Usuario Teste");
            usuario.setEmail(email);
            usuario.setSenha("senha321");
            usuario = usuarioRepository.save(usuario);

            Financeiro financeiro = new Financeiro();
            financeiro.setUsuario(usuario);
            financeiro.setTipo(TipoFinanceiro.DESPESA);
            financeiro.setCategoria("Educacao");
            financeiro.setDescricao("Mensalidade");
            financeiro.setValor(valor);
            financeiro.setData(LocalDate.of(2026, 6, 1));
            return financeiroRepository.save(financeiro);
        });
    }
}
