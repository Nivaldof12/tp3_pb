package br.com.infnet.notificacao.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.infnet.notificacao.domain.Notificacao;
import br.com.infnet.notificacao.dto.NotificacaoRequest;
import br.com.infnet.notificacao.repository.NotificacaoRepository;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    public Notificacao criar(NotificacaoRequest request) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuarioId(request.getUsuarioId());
        notificacao.setTitulo(request.getTitulo());
        notificacao.setMensagem(request.getMensagem());
        notificacao.setTipo(request.getTipo());
        return notificacaoRepository.save(notificacao);
    }

    public List<Notificacao> listarPorUsuario(Long usuarioId) {
        return notificacaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId);
    }

    public List<Notificacao> listarNaoLidasPorUsuario(Long usuarioId) {
        return notificacaoRepository.findByUsuarioIdAndLidaFalseOrderByDataCriacaoDesc(usuarioId);
    }

    public long contarNaoLidas(Long usuarioId) {
        return notificacaoRepository.countByUsuarioIdAndLidaFalse(usuarioId);
    }

    public Optional<Notificacao> findById(Long id) {
        return notificacaoRepository.findById(id);
    }

    @Transactional
    public Optional<Notificacao> marcarComoLida(Long id) {
        return notificacaoRepository.findById(id).map(notificacao -> {
            notificacao.setLida(true);
            return notificacaoRepository.save(notificacao);
        });
    }

    public void deletar(Long id) {
        notificacaoRepository.deleteById(id);
    }
}
