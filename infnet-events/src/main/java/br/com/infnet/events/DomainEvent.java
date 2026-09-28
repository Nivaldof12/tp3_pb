package br.com.infnet.events;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String eventId;
    private final LocalDateTime ocorridoEm;
    private final String tipoEvento;

    protected DomainEvent(String tipoEvento) {
        this.eventId = UUID.randomUUID().toString();
        this.ocorridoEm = LocalDateTime.now();
        this.tipoEvento = tipoEvento;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getOcorridoEm() {
        return ocorridoEm;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }
}
