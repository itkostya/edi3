package com.edi3.dao.i.categories;

import com.edi3.core.categories.User;
import com.edi3.dao.i.HibernateDAO;

import java.util.List;

public interface UserDao extends HibernateDAO<User> {
    public User getUserByLogin(String login);
    public User getUserById(Long id);
    public List<User> getUsers();
    public List<User> getCoworkers(String filterString);
    public boolean isDatabaseEmpty();
    public List<User> getNewUsers();
}
