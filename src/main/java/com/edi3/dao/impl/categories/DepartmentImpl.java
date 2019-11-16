package com.edi3.dao.impl.categories;

import com.edi3.core.categories.Department;
import com.edi3.dao.DepartmentDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DepartmentImpl implements DepartmentDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(Department department) {
        Session session = sessionFactory.openSession();
        session.save(department);
        session.close();
    }

    public void update(Department department) {
        Session session = sessionFactory.openSession();
        session.update(department);
        session.close();
    }

    public void delete(Department department) {
        Session session = sessionFactory.openSession();
        session.delete(department);
        session.close();
    }
}
