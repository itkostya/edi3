package com.edi3.dao.impl.categories;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.categories.UploadedFile;
import com.edi3.dao.i.categories.UploadedFileDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional
public class UploadedFileDaoImpl implements UploadedFileDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(UploadedFile uploadedFile) {
        sessionFactory.getCurrentSession().save(uploadedFile);
    }

    public void update(UploadedFile uploadedFile) {
        sessionFactory.getCurrentSession().update(uploadedFile);
    }

    public void delete(UploadedFile uploadedFile) {
        sessionFactory.getCurrentSession().delete(uploadedFile);
    }

    public UploadedFile getByFileNameAndDocument(String fileName, AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery((Objects.isNull(executorTask)?
                "from UploadedFile where fileName =:fileName and document.id =:abstractDocumentEdiId and executorTask is null" :
                "from UploadedFile where fileName =:fileName and document.id =:abstractDocumentEdiId and executorTask.id =:executorTaskId"));
        query.setParameter("fileName", fileName);
        query.setParameter("abstractDocumentEdiId", abstractDocumentEdi.getId());
        if (Objects.nonNull(executorTask)) query.setParameter("executorTaskId", executorTask.getId());

        return (UploadedFile) query.uniqueResult();
    }

    public List<UploadedFile> getListByDocument(AbstractDocumentEdi abstractDocumentEdi) {
        Session session = sessionFactory.getCurrentSession();

        Query<UploadedFile> query = session.createQuery("from UploadedFile where document.id =:abstractDocumentEdiId and executorTask is null", UploadedFile.class);
        query.setParameter("abstractDocumentEdiId", abstractDocumentEdi.getId());

        return query.getResultList();
    }

    public List<UploadedFile> getListByDocumentAndExecutorTask(AbstractDocumentEdi abstractDocumentEdi, ExecutorTask executorTask) {
        Session session =  sessionFactory.getCurrentSession();

        Query<UploadedFile> query = session.createQuery("from UploadedFile where document.id =:abstractDocumentEdiId and executorTask.id =:executorTaskId", UploadedFile.class);
        query.setParameter("abstractDocumentEdiId", abstractDocumentEdi.getId());
        query.setParameter("executorTaskId", executorTask.getId());

        return query.getResultList();
    }
}
