package br.com.infnet.model.Controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.infnet.model.Service.FinanceiroService;
import br.com.infnet.model.domain.Financeiro;

@RestController
@RequestMapping("/financeiros")
public class FinanceiroController {

    @Autowired
    private FinanceiroService financeiroService;

    @PostMapping
    public ResponseEntity<Financeiro> criarFinanceiro(@Valid @RequestBody Financeiro financeiro) {
        return ResponseEntity.ok(financeiroService.salvar(financeiro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Financeiro> obterFinanceiro(@PathVariable Long id) {
        Optional<Financeiro> financeiro = financeiroService.findById(id);
        return financeiro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Financeiro>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(financeiroService.findByUsuarioId(usuarioId));
    }

    @GetMapping
    public ResponseEntity<Iterable<Financeiro>> listarFinanceiros() {
        return ResponseEntity.ok(financeiroService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Financeiro> atualizarFinanceiro(@PathVariable Long id, @Valid @RequestBody Financeiro financeiro) {
        if (!financeiroService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        financeiro.setId(id);
        return ResponseEntity.ok(financeiroService.salvar(financeiro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFinanceiro(@PathVariable Long id) {
        if (!financeiroService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        financeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
