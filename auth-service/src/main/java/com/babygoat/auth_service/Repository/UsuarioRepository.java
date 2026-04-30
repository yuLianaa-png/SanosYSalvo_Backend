package com.babygoat.auth_service.Repository;

import com.babygoat.auth_service.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Metodo clave para buscar por nombre de usuario en el login
    Optional<Usuario> findByUsuario(String usuario);

    Optional<Usuario> findById(Long id);
}