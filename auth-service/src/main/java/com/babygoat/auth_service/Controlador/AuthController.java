//endpoint postman

package com.babygoat.auth_service.Controlador;

import com.babygoat.auth_service.Model.Usuario;
import com.babygoat.auth_service.Repository.UsuarioRepository;
import com.babygoat.auth_service.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioRepository repository;

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(authService.registrar(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String usuario = credentials.get("usuario");
        String contrasena = credentials.get("contrasena");

        return authService.validarLogin(usuario, contrasena)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build()); // 401 si falla
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}