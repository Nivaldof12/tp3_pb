package br.com.infnet.model.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.infnet.model.domain.Financeiro;
import br.com.infnet.model.domain.Usuario;
import br.com.infnet.model.dto.HistoricoRevisaoDTO;

@Service
public class HistoricoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<HistoricoRevisaoDTO<Usuario>> buscarHistoricoUsuario(Long id) {
        return buscarHistorico(Usuario.class, id);
    }

    @Transactional(readOnly = true)
    public List<HistoricoRevisaoDTO<Financeiro>> buscarHistoricoFinanceiro(Long id) {
        return buscarHistorico(Financeiro.class, id);
    }

    private <T> List<HistoricoRevisaoDTO<T>> buscarHistorico(Class<T> entityClass, Long id) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        @SuppressWarnings("unchecked")
        List<Object[]> revisoes = auditReader.createQuery()
                .forRevisionsOfEntity(entityClass, false, true)
                .add(AuditEntity.id().eq(id))
                .addOrder(AuditEntity.revisionNumber().asc())
                .getResultList();

        List<HistoricoRevisaoDTO<T>> historico = new ArrayList<>();

        for (Object[] revisao : revisoes) {
            @SuppressWarnings("unchecked")
            T entidade = (T) revisao[0];
            DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) revisao[1];
            RevisionType revisionType = (RevisionType) revisao[2];

            historico.add(new HistoricoRevisaoDTO<>(
                    revisionEntity.getId(),
                    converterDataRevisao(revisionEntity.getRevisionDate()),
                    traduzirTipoOperacao(revisionType),
                    entidade));
        }

        return historico;
    }

    private LocalDateTime converterDataRevisao(Date revisionDate) {
        return LocalDateTime.ofInstant(revisionDate.toInstant(), ZoneId.systemDefault());
    }

    private String traduzirTipoOperacao(RevisionType revisionType) {
        switch (revisionType) {
            case ADD:
                return "CRIACAO";
            case MOD:
                return "ATUALIZACAO";
            case DEL:
                return "EXCLUSAO";
            default:
                return revisionType.name();
        }
    }
}
