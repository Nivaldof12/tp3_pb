package br.com.infnet.model.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import br.com.infnet.client.NotificacaoClient;
import br.com.infnet.model.domain.Usuario;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UsuarioRepositoryTest {

    @MockBean
    private NotificacaoClient notificacaoClient;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNome("Maria Silva");
        usuario.setEmail("maria@email.com");
        usuario.setSenha("senha123");
    }

    @Test
    void deveSalvarEConsultarUsuarioPorId() {
        Usuario salvo = usuarioRepository.save(usuario);

        Optional<Usuario> encontrado = usuarioRepository.findById(salvo.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Maria Silva");
        assertThat(encontrado.get().getDataCriacao()).isNotNull();
        assertThat(encontrado.get().getDataAtualizacao()).isNotNull();
    }

    @Test
    void deveBuscarUsuarioPorEmail() {
        usuarioRepository.save(usuario);

        Optional<Usuario> encontrado = usuarioRepository.findByEmail("maria@email.com");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEmail()).isEqualTo("maria@email.com");
    }

    @Test
    void deveVerificarExistenciaDeEmail() {
        usuarioRepository.save(usuario);

        assertThat(usuarioRepository.existsByEmail("maria@email.com")).isTrue();
        assertThat(usuarioRepository.existsByEmail("inexistente@email.com")).isFalse();
    }

    @Test
    void deveBuscarUsuariosPorNomeParcial() {
        usuarioRepository.save(usuario);

        Usuario outro = new Usuario();
        outro.setNome("Joao Santos");
        outro.setEmail("joao@email.com");
        outro.setSenha("senha456");
        usuarioRepository.save(outro);

        List<Usuario> resultado = usuarioRepository.findByNomeContainingIgnoreCase("maria");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Maria Silva");
    }

    @Test
    void deveAtualizarUsuario() {
        Usuario salvo = usuarioRepository.save(usuario);
        salvo.setNome("Maria Atualizada");
        usuarioRepository.save(salvo);

        Usuario atualizado = usuarioRepository.findById(salvo.getId()).orElseThrow();

        assertThat(atualizado.getNome()).isEqualTo("Maria Atualizada");
        assertThat(atualizado.getDataAtualizacao()).isAfterOrEqualTo(atualizado.getDataCriacao());
    }

    @Test
    void deveExcluirUsuario() {
        Usuario salvo = usuarioRepository.save(usuario);
        Long id = salvo.getId();

        usuarioRepository.deleteById(id);

        assertThat(usuarioRepository.findById(id)).isEmpty();
    }
}
