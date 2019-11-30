package com.edi3.dao.impl.categories;

import com.edi3.core.categories.Department;
import com.edi3.dao.i.categories.DepartmentDao;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DepartmentDaoImpl implements DepartmentDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(Department department) {
        sessionFactory.getCurrentSession().save(department);
    }

    public void update(Department department) {
        sessionFactory.getCurrentSession().update(department);
    }

    public void delete(Department department) {
        sessionFactory.getCurrentSession().delete(department);
    }
}
