package br.com.infnet.events;

public class UsuarioCriadoEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    public static final String TIPO = "UsuarioCriado";

    private Long usuarioId;
    private String nome;
    private String email;

    public UsuarioCriadoEvent() {
        super(TIPO);
    }

    public UsuarioCriadoEvent(Long usuarioId, String nome, String email) {
        super(TIPO);
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.email = email;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
