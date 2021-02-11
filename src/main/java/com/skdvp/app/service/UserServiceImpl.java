package com.skdvp.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.skdvp.app.dao.UserDao;
import com.skdvp.app.model.Role;
import com.skdvp.app.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public UserServiceImpl(UserDao userDao, BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService, @Lazy UserService userService) {
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    // «Пользователь» – это просто Object. В большинстве случаев он может быть
    //  приведен к классу UserDetails.
    // Для создания UserDetails используется интерфейс UserDetailsService, с единственным методом:
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userService.getUserByName(s);
    }


    /*==========================CRUD=================================*/

    @Transactional
    @Override
    public List<User> showAllUsers() {
        return userDao.showAllUsers();
    }

    @Transactional
    @Override
    public User showUser(Long id) {
        return userDao.showUser(id);
    }

    @Transactional
    @Override
    public User saveUser(User user, Long idRole) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleService.getRoleById(idRole));
        user.setRoles(roleSet);
        userDao.saveUser(user);
        return user;
    }

    @Transactional
    @Override
    public void updateUser(Long id, User updateUser) {
        userDao.updateUser(id, updateUser);
    }

    @Transactional
    @Override
    public void removeUserById(Long id) {
        userDao.removeUserById(id);
    }


}
