package org.example.Service;

import org.example.Entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;

// Indica a Spring que esta clase es un Bean de servicio para poder inyectarlo en otros componentes
@Service
public class horario_service {

    // Lee del archivo de configuración (application.properties) la hora de inicio; si no existe, toma por defecto "08:00"
    @Value("${app.security.horario.inicio:08:00}")
    private String horaInicioStr;

    // Lee del archivo de configuración la hora de fin; por defecto "17:00"
    @Value("${app.security.horario.fin:17:00}")
    private String horaFinStr;

    // Lee si se permite el acceso en fines de semana; por defecto es falso (bloqueados)
    @Value("${app.security.horario.permitir-fines-semana:false}")
    private boolean permitirFinesSemana;

    // Define la zona horaria oficial para México (Central Time)
    private static final ZoneId ZONA_MEXICO = ZoneId.of("America/Mexico_City");

    // Método principal que recibe al usuario y evalúa si tiene permiso de entrar en este momento
    public boolean esAccesoPermitido(Usuario usuario) {

        // 1. REGLA DE ROL: Si el usuario tiene un rol asignado y su bandera de "acceso fuera de horario" es verdadera, se le concede el acceso de inmediato
        if (usuario.getRol() != null && Boolean.TRUE.equals(usuario.getRol().getAccesoFueraHorario())) {
            return true;
        }

        // Obtiene la fecha y hora actual exacta basada en la zona horaria de México
        ZonedDateTime ahora = ZonedDateTime.now(ZONA_MEXICO);

        // Extrae el día de la semana actual (LUNES, MARTES, etc.)
        DayOfWeek dia = ahora.getDayOfWeek();

        // 2. REGLA DE FIN DE SEMANA: Si no está permitido el fin de semana y hoy es sábado o domingo, niega el acceso (retorna falso)
        if (!permitirFinesSemana && (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY)) {
            return false;
        }

        // Extrae únicamente la hora del día actual (ej. 14:35:20)
        LocalTime horaActual = ahora.toLocalTime();

        // Convierte el texto de la hora de inicio configurada a un objeto LocalTime operable
        LocalTime horaInicio = LocalTime.parse(horaInicioStr);

        // Convierte el texto de la hora de fin configurada a un objeto LocalTime operable
        LocalTime horaFin = LocalTime.parse(horaFinStr);

        // 3. REGLA DE HORARIO DIARIO: Verifica que la hora actual no sea anterior a la hora de inicio ni posterior a la hora de fin
        return !horaActual.isBefore(horaInicio) && !horaActual.isAfter(horaFin);
    }
}