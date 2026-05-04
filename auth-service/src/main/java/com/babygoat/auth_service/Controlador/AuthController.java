//endpoint postman

package com.babygoat.auth_service.Controlador;

import com.babygoat.auth_service.Model.Usuario;
import com.babygoat.auth_service.Repository.UsuarioRepository;
import com.babygoat.auth_service.Service.AuthService;
import com.babygoat.auth_service.security.JwtUtils;
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
    private UsuarioRepository repository;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(authService.registrar(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String usuario = credentials.get("usuario");
        String contrasena = credentials.get("contrasena");

        return authService.validarLogin(usuario, contrasena)
                .map(u -> {
                    String token = jwtUtils.generarToken(u.getUsuario()); // Usamos el nombre de usuario o email

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("usuario", u.getUsuario());
                    response.put("mensaje", "Login exitoso");

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/buscar/{usuario}")
    public ResponseEntity<Usuario> obtenerPorUsuario(@PathVariable String usuario) {
        System.out.println("Buscando al usuario: " + usuario);

        Usuario u = authService.buscarPorUsuario(usuario);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(u);
    }
}