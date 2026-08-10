package br.com.infnet.web;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.infnet.client.NotificacaoClient;
import br.com.infnet.client.NotificacaoResponseDTO;
import br.com.infnet.model.Service.FinanceiroService;
import br.com.infnet.model.Service.UsuarioService;
import br.com.infnet.model.domain.Usuario;

@Controller
@RequestMapping("/app")
public class AppController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FinanceiroService financeiroService;

    @Autowired
    private NotificacaoClient notificacaoClient;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "app/index";
    }

    @GetMapping("/usuarios/{id}")
    public String detalheUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));
        model.addAttribute("usuario", usuario);
        model.addAttribute("financeiros", financeiroService.findByUsuarioId(id));
        model.addAttribute("notificacoes", buscarNotificacoes(id));
        model.addAttribute("naoLidas", contarNaoLidas(id));
        return "app/usuario";
    }

    @GetMapping("/notificacoes/{usuarioId}")
    public String notificacoes(@PathVariable Long usuarioId, Model model) {
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));
        model.addAttribute("usuario", usuario);
        model.addAttribute("notificacoes", buscarNotificacoes(usuarioId));
        return "app/notificacoes";
    }

    private List<NotificacaoResponseDTO> buscarNotificacoes(Long usuarioId) {
        try {
            return notificacaoClient.listarPorUsuario(usuarioId);
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    private long contarNaoLidas(Long usuarioId) {
        try {
            return notificacaoClient.contarNaoLidas(usuarioId).getOrDefault("naoLidas", 0L);
        } catch (Exception ex) {
            return 0L;
        }
    }
}
