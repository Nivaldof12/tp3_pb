package br.com.infnet.client.dto;

public class NotificacaoRequestDTO {

    private Long usuarioId;
    private String titulo;
    private String mensagem;
    private String tipo;

    public NotificacaoRequestDTO() {
    }

    public NotificacaoRequestDTO(Long usuarioId, String titulo, String mensagem, String tipo) {
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.tipo = tipo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
