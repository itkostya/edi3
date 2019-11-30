package com.edi3.dao.impl.categories;

import com.edi3.core.categories.Department;
import com.edi3.core.categories.Position;
import com.edi3.core.categories.User;
import com.edi3.core.exсeption.UserNotFoundException;
import com.edi3.dao.i.categories.UserDao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Objects;

@Transactional
public class UserDaoImpl implements UserDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    public void update(User user) {
        sessionFactory.getCurrentSession().update(user);
    }

    public void delete(User user) {
        sessionFactory.getCurrentSession().delete(user);
    }

    public User getUserByLogin(String login) {
//        System.out.println("UserDaoImpl getUserByLogin()");
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User where login =:paramLogin");
        query.setParameter("paramLogin", login);
        return (User) query.uniqueResult();
    }

    public User getUserById(Long id) {
//        System.out.println("UserDaoImpl getUserById()");
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        if (user == null) throw new UserNotFoundException(id);
        return user;
    }

    public List<User> getUsers() {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("from User where role is not null and deletionMark = false", User.class);
        List<User> list = query.getResultList();
        session.close();
        return list;
    }

    public List<User> getCoworkers(String filterString) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> userRoot = cq.from(User.class);
        Join<User, Position> positionJoin = userRoot.join("position", JoinType.LEFT);
        Join<User, Department> departmentJoin = userRoot.join("department", JoinType.LEFT);
        cq.where((("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                        cb.or(
                                cb.like(cb.lower(userRoot.get("fio")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(positionJoin.get("name")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(departmentJoin.get("name")), "%" + filterString.toLowerCase() + "%")
                        )
                )
        );

        cq.select(userRoot);
        TypedQuery<User> q = session.createQuery(cq);
        return q.getResultList();
    }

    public boolean isDatabaseEmpty(){
        return getUsers().isEmpty();
    }

    public List<User> getNewUsers(){
        return null;
//        Session session = HibernateUtil.getSession();
//        Query<User> query = session.createQuery("from User where role is null and deletionMark = false", User.class);
//        List<User> list = query.getResultList();
//        session.close();
//        return list;
    }
}
