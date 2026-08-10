package br.com.infnet.model.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.infnet.client.NotificacaoClient;
import br.com.infnet.client.dto.NotificacaoRequestDTO;
import br.com.infnet.model.Repository.FinanceiroRepository;
import br.com.infnet.model.domain.Financeiro;
import br.com.infnet.model.domain.TipoFinanceiro;

@Service
public class FinanceiroService {

    private static final Logger log = LoggerFactory.getLogger(FinanceiroService.class);

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @Autowired
    private NotificacaoClient notificacaoClient;

    public Financeiro salvar(Financeiro financeiro) {
        Financeiro salvo = financeiroRepository.save(financeiro);
        enviarNotificacao(salvo);
        return salvo;
    }

    private void enviarNotificacao(Financeiro financeiro) {
        try {
            Long usuarioId = financeiro.getUsuario().getId();
            String titulo;
            String mensagem;
            String tipo;

            if (financeiro.getTipo() == TipoFinanceiro.DESPESA) {
                titulo = "Nova despesa registrada";
                mensagem = String.format("Despesa de R$ %s em %s foi registrada.",
                        financeiro.getValor(), financeiro.getCategoria());
                tipo = financeiro.getValor().compareTo(new BigDecimal("1000")) >= 0 ? "ALERTA" : "INFO";
            } else {
                titulo = "Nova receita registrada";
                mensagem = String.format("Receita de R$ %s em %s foi registrada.",
                        financeiro.getValor(), financeiro.getCategoria());
                tipo = "SUCESSO";
            }

            notificacaoClient.criar(new NotificacaoRequestDTO(usuarioId, titulo, mensagem, tipo));
        } catch (Exception ex) {
            log.warn("Nao foi possivel enviar notificacao ao microsservico: {}", ex.getMessage());
        }
    }

    public Optional<Financeiro> findById(Long id) {
        return financeiroRepository.findById(id);
    }

    public List<Financeiro> findByUsuarioId(Long usuarioId) {
        return financeiroRepository.findByUsuarioId(usuarioId);
    }

    public Iterable<Financeiro> listarTodos() {
        return financeiroRepository.findAll();
    }

    public void deletar(Long id) {
        financeiroRepository.deleteById(id);
    }
}
