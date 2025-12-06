package dev.alexissdev.crudapp.user.service;

import dev.alexissdev.crudapp.role.Role;
import dev.alexissdev.crudapp.role.repositories.RoleRepository;
import dev.alexissdev.crudapp.user.User;
import dev.alexissdev.crudapp.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DefaultUserService
        implements UserService {

    private static final Logger LOGGER = Logger.getLogger(DefaultUserService.class.getName());
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public User save(User user) {
        List<Role> roles = new ArrayList<>();
        roleRepository.findByName(Role.USER).ifPresentOrElse(roles::add, () -> LOGGER.warning("User role not found."));
        if (user.isAdmin()) {
            roleRepository.findByName(Role.ADMIN).ifPresentOrElse(roles::add, () -> LOGGER.warning("Admin role not found."));
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> delete(Long id) {
        return findById(id).map(user -> {
            userRepository.delete(user);
            return user;
        });
    }
}
