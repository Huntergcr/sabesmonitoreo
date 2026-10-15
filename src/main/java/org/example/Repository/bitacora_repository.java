package org.example.Repository;

import org.example.Entity.bitacora_accesos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface bitacora_repository extends JpaRepository<bitacora_accesos, Long> {
}
