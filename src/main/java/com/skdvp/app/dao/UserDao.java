package com.skdvp.app.dao;

import com.skdvp.app.model.User;

import java.util.List;

public interface UserDao {

    User getUserByName(String name);

    /*==========================CRUD=================================*/

    List<User> showAllUsers();

    User showUser(Long id);


    void saveUser(User user);

    void updateUser(Long id, User updateUser);

    void removeUserById(Long id);

}
