package com.edi3.dao.impl.categories;

//import categories.Department;
//import categories.Position;
//import categories.User;
import com.edi3.core.categories.User;
import com.edi3.dao.HibernateDAO;
import com.edi3.dao.UserDao;
//import exсeption.UserNotFoundException;
//import hibernate.HibernateDAO;
//import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
//import org.hibernate.query.Query;

import java.util.List;

/*
 *
 */
public class UserImpl implements HibernateDAO<User>, UserDao {  // ???

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(User user) {
        Session session = sessionFactory.openSession();
        session.save(user);
        session.close();
    }

    public void update(User user) {
        Session session = sessionFactory.openSession();
        session.update(user);
        session.close();
    }

    public void delete(User user) {
        Session session = sessionFactory.openSession();
        session.delete(user);
        session.close();
    }

    public User getUserByLogin(String login) {
        return null;
//        Session session = HibernateUtil.getSession();
//        Query query = session.createQuery("from User where login =:paramLogin");
//        query.setParameter("paramLogin", login);
//        User user = (User) query.uniqueResult();
//        session.close();
//        return user;
    }

    public User getUserById(Long id) {
        return null;
//        Session session = HibernateUtil.getSession();
//        User user = session.get(User.class, id);
//        if (user == null) throw new UserNotFoundException(id);
//        session.close();
//        return user;
    }

    public List<User> getUsers() {
        return null;
//        Session session = HibernateUtil.getSession();
//        Query<User> query = session.createQuery("from User where role is not null and deletionMark = false", User.class);
//        List<User> list = query.getResultList();
//        session.close();
//        return list;
    }

    public List<User> getCoworkers(String filterString) {
        return null;
//        Session session = HibernateUtil.getSession();
//
//        CriteriaBuilder cb = session.getCriteriaBuilder();
//        CriteriaQuery<User> cq = cb.createQuery(User.class);
//        Root<User> userRoot = cq.from(User.class);
//        Join<User, Position> positionJoin = userRoot.join("position", JoinType.LEFT);
//        Join<User, Department> departmentJoin = userRoot.join("department", JoinType.LEFT);
//
//        cq.where((("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
//                        cb.or(
//                                cb.like(cb.lower(userRoot.get("fio")), "%" + filterString.toLowerCase() + "%"),
//                                cb.like(cb.lower(positionJoin.get("name")), "%" + filterString.toLowerCase() + "%"),
//                                cb.like(cb.lower(departmentJoin.get("name")), "%" + filterString.toLowerCase() + "%")
//                        )
//                )
//        );
//
//        cq.select(userRoot);
//        TypedQuery<User> q = session.createQuery(cq);
//        List<User> list = q.getResultList();
//
//        session.close();
//        return list;

    }

    public boolean isDatabaseEmpty(){
        return true;
        //return getUsers().isEmpty();
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
