package br.com.infnet.model.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.infnet.events.FinanceiroCriadoEvent;
import br.com.infnet.messaging.EventPublisher;
import br.com.infnet.messaging.FinanceiroEventFactory;
import br.com.infnet.model.Repository.FinanceiroRepository;
import br.com.infnet.model.domain.Financeiro;

@Service
public class FinanceiroService {

    private static final Logger log = LoggerFactory.getLogger(FinanceiroService.class);

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Transactional
    public Financeiro salvar(Financeiro financeiro) {
        Financeiro salvo = financeiroRepository.save(financeiro);
        publicarEventoFinanceiroCriado(salvo);
        return salvo;
    }

    private void publicarEventoFinanceiroCriado(Financeiro financeiro) {
        try {
            FinanceiroCriadoEvent evento = FinanceiroEventFactory.criarEvento(financeiro);
            String routingKey = FinanceiroEventFactory.routingKey(financeiro);
            eventPublisher.publicar(evento, routingKey);
        } catch (Exception ex) {
            log.warn("Falha ao publicar evento FinanceiroCriado: {}", ex.getMessage());
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
