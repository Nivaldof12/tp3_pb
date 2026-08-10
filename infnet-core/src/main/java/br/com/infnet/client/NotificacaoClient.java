package br.com.infnet.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.infnet.client.dto.NotificacaoRequestDTO;

@FeignClient(name = "notificacao-service")
public interface NotificacaoClient {

    @PostMapping("/notificacoes")
    NotificacaoResponseDTO criar(@RequestBody NotificacaoRequestDTO request);

    @GetMapping("/notificacoes/usuario/{usuarioId}")
    List<NotificacaoResponseDTO> listarPorUsuario(@PathVariable("usuarioId") Long usuarioId);

    @GetMapping("/notificacoes/usuario/{usuarioId}/contagem")
    Map<String, Long> contarNaoLidas(@PathVariable("usuarioId") Long usuarioId);
}
