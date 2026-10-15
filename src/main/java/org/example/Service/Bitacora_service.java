package org.example.Service;

import org.example.Entity.bitacora_accesos;
import org.example.Repository.bitacora_repository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class Bitacora_service {

    private final bitacora_repository bitacoraRepo;

    public Bitacora_service(bitacora_repository bitacoraRepo) {
        this.bitacoraRepo = bitacoraRepo;
    }

    public List<bitacora_accesos> obtenerUltimosAccesos() {
        List<bitacora_accesos> registros = bitacoraRepo.findAll();

        // Si no hay datos, creamos 2 o 3 registros demo para validar la vista
        if (registros.isEmpty()) {
            bitacora_accesos b1 = new bitacora_accesos();
            b1.setUsernameIntentado("admin@sabes.edu.mx");
            b1.setIpOrigen("192.168.1.50");
            b1.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            b1.setAccesoConcedido(true);
            b1.setMotivoBloqueo("OK");
            b1.setFechaHora(OffsetDateTime.now());
            bitacoraRepo.save(b1);

            bitacora_accesos b2 = new bitacora_accesos();
            b2.setUsernameIntentado("operador@sabes.edu.mx");
            b2.setIpOrigen("192.168.1.105");
            b2.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            b2.setAccesoConcedido(false);
            b2.setMotivoBloqueo("HORARIO_NO_PERMITIDO");
            b2.setFechaHora(OffsetDateTime.now().minusHours(2));
            bitacoraRepo.save(b2);

            registros = bitacoraRepo.findAll();
        }

        return registros;
    }
}