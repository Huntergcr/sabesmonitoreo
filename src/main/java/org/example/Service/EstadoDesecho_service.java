package org.example.Service;

import org.example.Entity.EstadoDesecho;
import org.example.Repository.EstadoDesecho_repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstadoDesecho_service {

    private final EstadoDesecho_repository repository;

    public EstadoDesecho_service(EstadoDesecho_repository repository) {
        this.repository = repository;
    }

    public List<EstadoDesecho> obtenerTodos() {
        List<EstadoDesecho> lista = repository.findAll();

        // Si la tabla está vacía, insertamos registros iniciales para prueba visual
        if (lista.isEmpty()) {
            inicializarDatosDemo();
            lista = repository.findAll();
        }
        return lista;
    }

    private void inicializarDatosDemo() {
        EstadoDesecho plastico = new EstadoDesecho();
        plastico.setCodigoTipo("PLASTICO");
        plastico.setNombre("Plásticos y PET");
        plastico.setNivelLlenadoPorcentaje(45);
        plastico.setConteoItems(128);
        plastico.setUltimaActualizacion(LocalDateTime.now());
        repository.save(plastico);

        EstadoDesecho organico = new EstadoDesecho();
        organico.setCodigoTipo("ORGANICO");
        organico.setNombre("Residuos Orgánicos");
        organico.setNivelLlenadoPorcentaje(80);
        organico.setConteoItems(95);
        organico.setUltimaActualizacion(LocalDateTime.now());
        repository.save(organico);

        EstadoDesecho papel = new EstadoDesecho();
        papel.setCodigoTipo("PAPEL");
        papel.setNombre("Papel y Cartón");
        papel.setNivelLlenadoPorcentaje(20);
        papel.setConteoItems(42);
        papel.setUltimaActualizacion(LocalDateTime.now());
        repository.save(papel);
    }
}