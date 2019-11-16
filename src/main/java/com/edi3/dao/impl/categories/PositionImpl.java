package com.edi3.dao.impl.categories;

import com.edi3.core.categories.Position;
import com.edi3.dao.PositionDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class PositionImpl implements PositionDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(Position position) {
        Session session = sessionFactory.openSession();
        session.save(position);
        session.close();
    }

    public void update(Position position) {
        Session session = sessionFactory.openSession();
        session.update(position);
        session.close();
    }

    public void delete(Position position) {
        Session session = sessionFactory.openSession();
        session.delete(position);
        session.close();
    }
}
