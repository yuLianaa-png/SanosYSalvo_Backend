// logica

package com.babygoat.auth_service.Service;

import com.babygoat.auth_service.Model.Usuario;
import com.babygoat.auth_service.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario registrar(Usuario usuario) {
        return repository.save(usuario);
    }

    public Optional<Usuario> validarLogin(String usuario, String contrasena) {
        return repository.findByUsuario(usuario)
                .filter(u -> u.getContrasena().equals(contrasena)); // Comparación simple
    }

    public Usuario buscarPorUsuario(String nombreUsuario) {
        return repository.findByUsuario(nombreUsuario)
                .orElse(null);
    }
}