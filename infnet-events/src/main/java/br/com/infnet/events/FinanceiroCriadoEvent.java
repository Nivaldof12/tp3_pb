package br.com.infnet.events;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FinanceiroCriadoEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    public static final String TIPO = "FinanceiroCriado";

    private Long financeiroId;
    private Long usuarioId;
    private String tipoFinanceiro;
    private String categoria;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;

    public FinanceiroCriadoEvent() {
        super(TIPO);
    }

    public FinanceiroCriadoEvent(Long financeiroId, Long usuarioId, String tipoFinanceiro,
            String categoria, String descricao, BigDecimal valor, LocalDate data) {
        super(TIPO);
        this.financeiroId = financeiroId;
        this.usuarioId = usuarioId;
        this.tipoFinanceiro = tipoFinanceiro;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    public Long getFinanceiroId() {
        return financeiroId;
    }

    public void setFinanceiroId(Long financeiroId) {
        this.financeiroId = financeiroId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipoFinanceiro() {
        return tipoFinanceiro;
    }

    public void setTipoFinanceiro(String tipoFinanceiro) {
        this.tipoFinanceiro = tipoFinanceiro;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
