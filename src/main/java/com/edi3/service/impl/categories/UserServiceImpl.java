package com.edi3.service.impl.categories;

import com.edi3.core.categories.User;
import com.edi3.dao.UserDao;
import com.edi3.dao.impl.categories.UserImpl;
import com.edi3.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao;
//    private UserImpl userDao;

    public boolean isUserExist(User user){
        return ( user != null && userDao.getUserByLogin(user.getLogin())!= null );
    }

    public boolean isPasswordCorrect(User user){
        return false;
//        User userInDatabase = userDao.getUserByLogin(user.getLogin());
//        return (Objects.nonNull(userInDatabase) &&
//                user.getPassword().equals(userInDatabase.getPassword())
//        );
    }

    public List<User> getCoworkers(String filterString){
        return userDao.getCoworkers(filterString);
    }

    public Integer getNewUsersCount(){
        List<User> userList = userDao.getNewUsers();
        return userList.size();
    }

    public void setUserDao(UserImpl userDao) {
        this.userDao = userDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public boolean isDatabaseEmpty() {
        return userDao.isDatabaseEmpty();  }

//    private UserDao userDao;
//
//    @Override
//    public UserDao getUserDao(){
//        return this.userDao;
//    }
//
//    public void setUserDao(UserDao userDao) {
//        this.userDao = userDao;
//    }
//
//    public boolean registerUser(User user) {
//        if (userDao.getUserByEmail(user.getEmail()) == null) {
//            userDao.save(user);
//            System.out.println(String.format("User with email %s created", user.getEmail()));
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    @Override
//    public boolean isPasswordCorrect(String login, String pass) {
//        return userDao.isPasswordCorrect(login, pass);
//    }
//
//    @Override
//    public User getUserById(Long id) {
//        return userDao.getUserById(id);
//    }
//
//    @Override
//    public User getUserByEmail(String email) {
//        return userDao.getUserByEmail(email);
//    }
}
