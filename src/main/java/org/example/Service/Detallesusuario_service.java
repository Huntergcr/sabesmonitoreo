/*package org.example.Service;

import org.example.Entity.Usuario;
import org.example.Entity.bitacora_accesos;
import org.example.Repository.Usuario_repository;
import org.example.Repository.bitacora_repository;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collections;

@Service
public class Detallesusuario_service implements UserDetailsService {

    private final Usuario_repository usuarioRepo;
    private final horario_service horarioService;
    private final bitacora_repository bitacoraRepo;

    public Detallesusuario_service(Usuario_repository usuarioRepo,
                                    horario_service horarioService,
                                    bitacora_repository bitacoraRepo) {
        this.usuarioRepo = usuarioRepo;
        this.horarioService = horarioService;
        this.bitacoraRepo = bitacoraRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscar en la tabla usuarios por nombre
        Usuario usuario = usuarioRepo.findByNombre(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // 2. Verificar regla de horario laboral (08:00 a 17:00)
        if (!horarioService.esAccesoPermitido(usuario)) {
            // Registramos el intento denegado en la bitácora
            bitacora_accesos log = new bitacora_accesos();
            log.setUsernameIntentado(username);
            log.setUsuario(usuario);
            log.setIpOrigen("127.0.0.1"); // Luego se puede capturar vía HttpServletRequest
            log.setAccesoConcedido(false);
            log.setMotivoBloqueo("HORARIO_NO_PERMITIDO");
            log.setFechaHora(OffsetDateTime.now());
            bitacoraRepo.save(log);

            throw new LockedException("Acceso no permitido fuera del horario laboral.");
        }

        // 3. Extraer rol (ej. ROLE_ADMIN o ROLE_OPERADOR)
        String nombreRol = (usuario.getRol() != null) ? usuario.getRol().getNombre() : "USER";
        SimpleGrantedAuthority autoridad = new SimpleGrantedAuthority("ROLE_" + nombreRol);

        // 4. Retornar UserDetails con la contraseña almacenada en la base de datos
        return new User(
                usuario.getNombre(),
                usuario.getPasswordHash(), // El campo donde guardas la contraseña en la entidad
                Collections.singletonList(autoridad)
        );
    }
}*/