package com.udacity.ecommerce.security;

import java.util.Collections;

import com.udacity.ecommerce.model.persistence.User;
import com.udacity.ecommerce.model.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        UserBuilder userBuilder = null;

        if (user != null) {
            userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
            userBuilder.password(user.getPassword());
            userBuilder.roles("");
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return userBuilder.build();
    }
}