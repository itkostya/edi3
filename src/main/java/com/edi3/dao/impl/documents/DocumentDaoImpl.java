package com.edi3.dao.impl.documents;

//import abstract_entity.AbstractDocumentEdi;
import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.exсeption.AbstractDocumentEdiNotFoundException;
import com.edi3.dao.i.documents.DocumentDao;
import com.edi3.dao.i.documents.DocumentNumerationDao;
//import exсeption.AbstractDocumentEdiNotFoundException;
//import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;

@Transactional
public class DocumentDaoImpl implements DocumentDao {

    private SessionFactory sessionFactory;
    private DocumentNumerationDao documentNumerationDao;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public DocumentNumerationDao getDocumentNumerationDao() {
        return documentNumerationDao;
    }

    public void setDocumentNumerationDao(DocumentNumerationDao documentNumerationDao) {
        this.documentNumerationDao = documentNumerationDao;
    }

    public void save(AbstractDocumentEdi abstractDocumentEdi) {
        System.out.println("DocumentDaoImpl save() begin");
        Session session = sessionFactory.getCurrentSession();
        // TODO - Check it
        abstractDocumentEdi.setNumber(documentNumerationDao.getNextNumber(abstractDocumentEdi, ""));
        System.out.println("DocumentDaoImpl() before save");
        try {
            session.save(abstractDocumentEdi);
            System.out.println("DocumentDaoImpl() after save");
        }catch (PersistenceException e){
            System.out.println("DocumentDaoImpl() in PersistenceException error");
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                if (e.getCause().getCause().getMessage().contains("Duplicate entry")) {
                    System.out.println("DocumentDaoImpl() in PersistenceException error 2");
                    session = sessionFactory.getCurrentSession();
                    abstractDocumentEdi.setNumber(documentNumerationDao.getNextNumberUsingMax(abstractDocumentEdi, ""));
                    session.save(abstractDocumentEdi);
                    System.out.println("DocumentDaoImpl() in PersistenceException error 3");
                }
            }
        }
        System.out.println("DocumentDaoImpl save() end");
    }

    @SuppressWarnings("unused")
    public void update(AbstractDocumentEdi abstractDocumentEdi) {
        sessionFactory.getCurrentSession().update(abstractDocumentEdi);
    }

    @SuppressWarnings("unused")
    public void delete(AbstractDocumentEdi abstractDocumentEdi) {
        sessionFactory.getCurrentSession().delete(abstractDocumentEdi);
    }

    public AbstractDocumentEdi getById(Long id){
        Session session = sessionFactory.getCurrentSession();
        AbstractDocumentEdi documentEdi = session.get(AbstractDocumentEdi.class,id);
        if (documentEdi == null) throw new AbstractDocumentEdiNotFoundException(id);

        return documentEdi;
    }

}
