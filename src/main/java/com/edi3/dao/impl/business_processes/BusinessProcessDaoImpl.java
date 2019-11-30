package com.edi3.dao.impl.business_processes;

import com.edi3.core.business_processes.BusinessProcess;
import com.edi3.dao.i.business_processes.BusinessProcessDao;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BusinessProcessDaoImpl implements BusinessProcessDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(BusinessProcess bp) {
        sessionFactory.getCurrentSession().save(bp);
    }

    public void update(BusinessProcess bp) {
        sessionFactory.getCurrentSession().update(bp);
    }

    public void delete(BusinessProcess bp) {
        sessionFactory.getCurrentSession().delete(bp);
    }
}
