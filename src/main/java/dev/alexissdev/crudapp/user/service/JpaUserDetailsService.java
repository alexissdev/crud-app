package dev.alexissdev.crudapp.user.service;

import dev.alexissdev.crudapp.user.User;
import dev.alexissdev.crudapp.util.UserDetailsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class JpaUserDetailsService
        implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JpaUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> possibleUser = userService.findByUsername(username);
        if (possibleUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        User user = possibleUser.get();

        return UserDetailsFactory.create(user);
    }
}
