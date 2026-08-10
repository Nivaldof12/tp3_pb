package br.com.infnet.model.dto;

import java.time.LocalDateTime;

public class HistoricoRevisaoDTO<T> {

    private int numeroRevisao;
    private LocalDateTime dataRevisao;
    private String tipoOperacao;
    private T entidade;

    public HistoricoRevisaoDTO() {
    }

    public HistoricoRevisaoDTO(int numeroRevisao, LocalDateTime dataRevisao, String tipoOperacao, T entidade) {
        this.numeroRevisao = numeroRevisao;
        this.dataRevisao = dataRevisao;
        this.tipoOperacao = tipoOperacao;
        this.entidade = entidade;
    }

    public int getNumeroRevisao() {
        return numeroRevisao;
    }

    public void setNumeroRevisao(int numeroRevisao) {
        this.numeroRevisao = numeroRevisao;
    }

    public LocalDateTime getDataRevisao() {
        return dataRevisao;
    }

    public void setDataRevisao(LocalDateTime dataRevisao) {
        this.dataRevisao = dataRevisao;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(String tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public T getEntidade() {
        return entidade;
    }

    public void setEntidade(T entidade) {
        this.entidade = entidade;
    }
}
