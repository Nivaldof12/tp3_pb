package br.com.infnet.model.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.infnet.model.Service.FinanceiroService;
import br.com.infnet.model.Service.HistoricoService;
import br.com.infnet.model.Service.UsuarioService;
import br.com.infnet.model.domain.Financeiro;
import br.com.infnet.model.domain.Usuario;
import br.com.infnet.model.dto.HistoricoRevisaoDTO;

@RestController
@RequestMapping("/historico")
public class HistoricoController {

    @Autowired
    private HistoricoService historicoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FinanceiroService financeiroService;

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<List<HistoricoRevisaoDTO<Usuario>>> obterHistoricoUsuario(@PathVariable Long id) {
        if (!usuarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(historicoService.buscarHistoricoUsuario(id));
    }

    @GetMapping("/financeiros/{id}")
    public ResponseEntity<List<HistoricoRevisaoDTO<Financeiro>>> obterHistoricoFinanceiro(@PathVariable Long id) {
        if (!financeiroService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(historicoService.buscarHistoricoFinanceiro(id));
    }
}
