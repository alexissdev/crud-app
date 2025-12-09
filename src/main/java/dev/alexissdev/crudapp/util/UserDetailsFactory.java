package dev.alexissdev.crudapp.util;

import dev.alexissdev.crudapp.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsFactory {

    /**
     * Creates a UserDetails instance using the provided User object.
     * The method maps the user's roles to GrantedAuthority and includes them
     * in the created UserDetails object.
     *
     * @param user the User object containing user information, must not be null
     * @return a UserDetails instance containing the user's credentials and granted authorities
     */

    public static UserDetails create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}
