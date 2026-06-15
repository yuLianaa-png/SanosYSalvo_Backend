// logica

package com.babygoat.auth_service.Service;

import com.babygoat.auth_service.Model.User;
import com.babygoat.auth_service.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository repository;

    public User register(User user) {
        return repository.save(user);
    }

    public Optional<User> validateLogin(String username, String password) {
        return repository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User findByUsernameOrNull(String username) {
        return repository.findByUsername(username)
                .orElse(null);
    }
}