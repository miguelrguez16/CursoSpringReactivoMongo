package com.example.springapirest.sso;

import com.example.springapirest.dao.UserDao;
import com.example.springapirest.services.com.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(CustomReactiveUserDetailsService.class);
    @Autowired
    private UserService userDao;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.info("Searching " + username);
        return userDao.findUserByUsername(username).cast(UserDetails.class);

    }
}