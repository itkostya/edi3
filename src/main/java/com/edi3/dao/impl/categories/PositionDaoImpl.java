package com.edi3.dao.impl.categories;

import com.edi3.core.categories.Position;
import com.edi3.dao.i.categories.PositionDao;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PositionDaoImpl implements PositionDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(Position position) {
        sessionFactory.getCurrentSession().save(position);
    }

    public void update(Position position) {
        sessionFactory.getCurrentSession().update(position);
    }

    public void delete(Position position) {
        sessionFactory.getCurrentSession().delete(position);
    }
}
