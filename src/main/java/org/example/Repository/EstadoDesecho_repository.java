package org.example.Repository;

import org.example.Entity.EstadoDesecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoDesecho_repository extends JpaRepository<EstadoDesecho, Integer> {
    Optional<EstadoDesecho> findByCodigoTipo(String codigoTipo);
}
