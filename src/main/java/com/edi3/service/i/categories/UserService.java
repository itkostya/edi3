package com.edi3.service.i.categories;

import com.edi3.core.categories.User;

import java.util.List;

public interface UserService {
    boolean isUserExist(User user);
    boolean isPasswordCorrect(User user);
    List<User> getCoworkers(String filterString);
    public Integer getNewUsersCount();
    // new:
    boolean isDatabaseEmpty();
    List<User> getUsers();
    public User getUserByLogin(String login);
    public User getUserById(Long id);
}
