package br.com.infnet.model.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import br.com.infnet.client.NotificacaoClient;
import br.com.infnet.messaging.EventPublisher;
import br.com.infnet.model.domain.Financeiro;
import br.com.infnet.model.domain.TipoFinanceiro;
import br.com.infnet.model.domain.Usuario;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FinanceiroRepositoryTest {

    @MockBean
    private NotificacaoClient notificacaoClient;

    @MockBean
    private EventPublisher eventPublisher;

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNome("Carlos Souza");
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("senha789");
        usuario = usuarioRepository.save(usuario);
    }

    private Financeiro criarFinanceiro(TipoFinanceiro tipo, String categoria, BigDecimal valor, LocalDate data) {
        Financeiro financeiro = new Financeiro();
        financeiro.setUsuario(usuario);
        financeiro.setTipo(tipo);
        financeiro.setCategoria(categoria);
        financeiro.setDescricao("Lancamento de teste");
        financeiro.setValor(valor);
        financeiro.setData(data);
        return financeiro;
    }

    @Test
    void deveSalvarEConsultarFinanceiroPorId() {
        Financeiro financeiro = criarFinanceiro(
                TipoFinanceiro.RECEITA, "Salario", new BigDecimal("5000.00"), LocalDate.of(2026, 1, 15));
        Financeiro salvo = financeiroRepository.save(financeiro);

        Financeiro encontrado = financeiroRepository.findById(salvo.getId()).orElseThrow();

        assertThat(encontrado.getTipo()).isEqualTo(TipoFinanceiro.RECEITA);
        assertThat(encontrado.getUsuario().getId()).isEqualTo(usuario.getId());
        assertThat(encontrado.getDataCriacao()).isNotNull();
    }

    @Test
    void deveBuscarFinanceirosPorUsuario() {
        financeiroRepository.save(criarFinanceiro(
                TipoFinanceiro.RECEITA, "Salario", new BigDecimal("3000.00"), LocalDate.of(2026, 2, 1)));
        financeiroRepository.save(criarFinanceiro(
                TipoFinanceiro.DESPESA, "Aluguel", new BigDecimal("1200.00"), LocalDate.of(2026, 2, 5)));

        List<Financeiro> financeiros = financeiroRepository.findByUsuarioId(usuario.getId());

        assertThat(financeiros).hasSize(2);
    }

    @Test
    void deveBuscarFinanceirosPorUsuarioETipo() {
        financeiroRepository.save(criarFinanceiro(
                TipoFinanceiro.RECEITA, "Salario", new BigDecimal("3000.00"), LocalDate.of(2026, 3, 1)));
        financeiroRepository.save(criarFinanceiro(
                TipoFinanceiro.DESPESA, "Mercado", new BigDecimal("500.00"), LocalDate.of(2026, 3, 2)));

        List<Financeiro> despesas = financeiroRepository.findByUsuarioIdAndTipo(usuario.getId(), TipoFinanceiro.DESPESA);

        assertThat(despesas).hasSize(1);
        assertThat(despesas.get(0).getCategoria()).isEqualTo("Mercado");
    }

    @Test
    void deveBuscarFinanceirosPorPeriodo() {
        financeiroRepository.save(criarFinanceiro(
                TipoFinanceiro.RECEITA, "Salario", new BigDecimal("3000.00"), LocalDate.of(2026, 1, 1)));
        financeiroRepository.save(criarFinanceiro(
                TipoFinanceiro.DESPESA, "Conta", new BigDecimal("200.00"), LocalDate.of(2026, 3, 1)));

        List<Financeiro> financeiros = financeiroRepository.findByUsuarioIdAndDataBetween(
                usuario.getId(), LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        assertThat(financeiros).hasSize(1);
    }

    @Test
    void deveCalcularTotalPorTipo() {
        financeiroRepository.save(criarFinanceiro(
                TipoFinanceiro.RECEITA, "Salario", new BigDecimal("3000.00"), LocalDate.of(2026, 4, 1)));
        financeiroRepository.save(criarFinanceiro(
                TipoFinanceiro.RECEITA, "Freelance", new BigDecimal("1500.00"), LocalDate.of(2026, 4, 10)));

        BigDecimal total = financeiroRepository.calcularTotalPorTipo(usuario.getId(), TipoFinanceiro.RECEITA);

        assertThat(total).isEqualByComparingTo(new BigDecimal("4500.00"));
    }

    @Test
    void deveBuscarFinanceirosPorCategoria() {
        financeiroRepository.save(criarFinanceiro(
                TipoFinanceiro.DESPESA, "Transporte", new BigDecimal("150.00"), LocalDate.of(2026, 5, 1)));

        List<Financeiro> financeiros = financeiroRepository.findByCategoriaIgnoreCase("transporte");

        assertThat(financeiros).hasSize(1);
        assertThat(financeiros.get(0).getCategoria()).isEqualTo("Transporte");
    }
}
