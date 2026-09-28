package br.com.infnet.model.Service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.infnet.events.RabbitMQConstants;
import br.com.infnet.events.UsuarioCriadoEvent;
import br.com.infnet.messaging.EventPublisher;
import br.com.infnet.model.Repository.UsuarioRepository;
import br.com.infnet.model.domain.Usuario;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        Usuario salvo = usuarioRepository.save(usuario);
        publicarEventoUsuarioCriado(salvo);
        return salvo;
    }

    private void publicarEventoUsuarioCriado(Usuario usuario) {
        try {
            UsuarioCriadoEvent evento = new UsuarioCriadoEvent(
                    usuario.getId(), usuario.getNome(), usuario.getEmail());
            eventPublisher.publicar(evento, RabbitMQConstants.RK_USUARIO_CRIADO);
        } catch (Exception ex) {
            log.warn("Falha ao publicar evento UsuarioCriado: {}", ex.getMessage());
        }
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Iterable<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
