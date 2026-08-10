package br.com.infnet.notificacao.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.infnet.notificacao.domain.Notificacao;
import br.com.infnet.notificacao.dto.NotificacaoRequest;
import br.com.infnet.notificacao.service.NotificacaoService;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @PostMapping
    public ResponseEntity<Notificacao> criar(@Valid @RequestBody NotificacaoRequest request) {
        return ResponseEntity.ok(notificacaoService.criar(request));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacao>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacaoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/nao-lidas")
    public ResponseEntity<List<Notificacao>> listarNaoLidas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacaoService.listarNaoLidasPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/contagem")
    public ResponseEntity<Map<String, Long>> contarNaoLidas(@PathVariable Long usuarioId) {
        long total = notificacaoService.contarNaoLidas(usuarioId);
        return ResponseEntity.ok(Map.of("naoLidas", total));
    }

    @PatchMapping("/{id}/lida")
    public ResponseEntity<Notificacao> marcarComoLida(@PathVariable Long id) {
        return notificacaoService.marcarComoLida(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!notificacaoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        notificacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
