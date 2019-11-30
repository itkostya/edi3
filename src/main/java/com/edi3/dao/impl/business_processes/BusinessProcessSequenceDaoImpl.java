package com.edi3.dao.impl.business_processes;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.BusinessProcessSequence;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.User;
import com.edi3.dao.i.business_processes.BusinessProcessSequenceDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class BusinessProcessSequenceDaoImpl implements BusinessProcessSequenceDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(BusinessProcessSequence bps) {
        sessionFactory.getCurrentSession().save(bps);
    }

    public void update(BusinessProcessSequence bps) {
        sessionFactory.getCurrentSession().update(bps);
    }

    public void delete(BusinessProcessSequence bps) {
        sessionFactory.getCurrentSession().delete(bps);
    }

    public List<BusinessProcessSequence> getHistoryByDocumentList(AbstractDocumentEdi abstractDocumentEdi){
        Session session = sessionFactory.getCurrentSession();

        Query<BusinessProcessSequence> query = session.createQuery("select businessProcessSequence from BusinessProcessSequence businessProcessSequence " +
                "left join BusinessProcess as businessProcess on businessProcess.id = businessProcessSequence.businessProcess.id "+
                "left join AbstractDocumentEdi as abstractDocumentEdi on abstractDocumentEdi.id = businessProcess.document.id "+
                "where abstractDocumentEdi =:abstractDocumentEdi", BusinessProcessSequence.class);

        query.setParameter("abstractDocumentEdi", abstractDocumentEdi);
        return query.getResultList();

//        SELECT * FROM edi.bp_sequence as businessProcessSequence
//        left join edi.bp_business_process as businessProcess on businessProcessSequence.bp_id = businessProcess.ID
//        left join edi.doc_abstract_document_edi as abstractDocumentEdi on abstractDocumentEdi.ID = businessProcess.document_id
//        where abstractDocumentEdi.ID = 209;

    }

    public List<BusinessProcessSequence> getNotCompletedSequenceByDocumentAndUser(AbstractDocumentEdi abstractDocumentEdi, User user) {
        Session session = sessionFactory.getCurrentSession();

        Query<BusinessProcessSequence> query = session.createQuery("select businessProcessSequence from BusinessProcessSequence businessProcessSequence " +
                "left join BusinessProcess as businessProcess on businessProcess.id = businessProcessSequence.businessProcess.id " +
                "left join AbstractDocumentEdi as abstractDocumentEdi on abstractDocumentEdi.id = businessProcess.document.id " +
                "where abstractDocumentEdi =:abstractDocumentEdi and  businessProcessSequence.result is null and businessProcess.author =:user", BusinessProcessSequence.class);

        query.setParameter("abstractDocumentEdi", abstractDocumentEdi);
        query.setParameter("user", user);
        return query.getResultList();
    }

    public BusinessProcessSequence getBusinessProcessSequence(ExecutorTask executorTask){
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("select businessProcessSequence from BusinessProcessSequence businessProcessSequence " +
                "left join BusinessProcess as businessProcess on businessProcess.id = businessProcessSequence.businessProcess.id "+
                "where businessProcessSequence.executorTask=:executorTask");

        query.setParameter("executorTask", executorTask);
        return (BusinessProcessSequence)query.getSingleResult();
    }
}
