package com.edi3.service;

import com.edi3.core.categories.User;

import java.util.List;

public interface UserService {
    boolean isUserExist(User user);
    boolean isPasswordCorrect(User user);
    List<User> getCoworkers(String filterString);
    public Integer getNewUsersCount();
    // new:
    boolean isDatabaseEmpty();
}
