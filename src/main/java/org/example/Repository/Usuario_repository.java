package org.example.Repository;

import org.example.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Usuario_repository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNombre(String nombre);

    // Opcional por si también quieres buscar por correo:
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
