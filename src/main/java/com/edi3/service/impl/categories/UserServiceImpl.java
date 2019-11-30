package com.edi3.service.impl.categories;

import com.edi3.core.categories.User;
import com.edi3.dao.i.categories.UserDao;
import com.edi3.service.i.categories.UserService;

import java.util.List;
import java.util.Objects;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public boolean isUserExist(User user){
        return ( user != null && userDao.getUserByLogin(user.getLogin())!= null );
    }

    public boolean isPasswordCorrect(User user){
        User userInDatabase = userDao.getUserByLogin(user.getLogin());
        return (Objects.nonNull(userInDatabase) &&
                user.getPassword().equals(userInDatabase.getPassword())
        );
    }

    public List<User> getCoworkers(String filterString){
        return userDao.getCoworkers(filterString);
    }

    public Integer getNewUsersCount(){
        List<User> userList = userDao.getNewUsers();
        return userList.size();
    }

    public boolean isDatabaseEmpty() {
        return userDao.isDatabaseEmpty();
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public User getUserByLogin(String login) {
        return userDao.getUserByLogin(login);
    }

    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    // Getters, setters

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }
}
