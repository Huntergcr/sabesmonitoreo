package org.example.Repository;

import org.example.Entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Rol_repository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombre(String nombre);
}
