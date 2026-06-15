//endpoint postman

package com.babygoat.auth_service.Controller;

import com.babygoat.auth_service.Model.User;
import com.babygoat.auth_service.Repository.UserRepository;
import com.babygoat.auth_service.Security.JwtUtils;
import com.babygoat.auth_service.Service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository repository;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        return authService.validateLogin(username, password)
                .map(user -> {
                    String token = jwtUtils.generateToken(user.getUsername());

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("username", user.getUsername());
                    response.put("message", "Login successful");

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/search/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        User user = authService.findByUsernameOrNull(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}