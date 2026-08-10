package br.com.infnet.model.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.infnet.model.domain.Financeiro;
import br.com.infnet.model.domain.TipoFinanceiro;

@Repository
public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {

    List<Financeiro> findByUsuarioId(Long usuarioId);

    List<Financeiro> findByUsuarioIdAndTipo(Long usuarioId, TipoFinanceiro tipo);

    List<Financeiro> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDate inicio, LocalDate fim);

    @Query("SELECT SUM(f.valor) FROM Financeiro f WHERE f.usuario.id = :usuarioId AND f.tipo = :tipo")
    BigDecimal calcularTotalPorTipo(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoFinanceiro tipo);

    List<Financeiro> findByCategoriaIgnoreCase(String categoria);
}
