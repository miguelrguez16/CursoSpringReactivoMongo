package com.example.springapirest.services.imp;


import com.example.springapirest.dao.RoleDao;
import com.example.springapirest.dao.UserDao;
import com.example.springapirest.documents.Role;
import com.example.springapirest.documents.User;
import com.example.springapirest.services.com.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public Flux<User> findAllUsers() {
        return userDao.findAll();
    }

    @Override
    public Mono<User> findUserById(String id) {
        return userDao.findById(id);
    }

    @Override
    public Mono<User> findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public Mono<User> saveUser(User user) {
        return userDao.save(user);
    }

    @Override
    public Mono<Void> deleteUser(User user) {
        return userDao.delete(user);
    }

    @Override
    public Flux<Role> findAllRoles() {
        return roleDao.findAll();
    }

    @Override
    public Mono<Role> findRoleById(String id) {
        return roleDao.findById(id);
    }

    @Override
    public Mono<Role> findRoleByType(String type) {
        return roleDao.findRoleByType(type);
    }
    @Override
    public Mono<Role> saveRole(Role role) {
        return roleDao.save(role);
    }

    @Override
    public Mono<Void> deleteRoler(Role role) {
        return roleDao.delete(role);
    }

}
