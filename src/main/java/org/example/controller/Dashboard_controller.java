package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.Entity.Usuario;
import org.example.Entity.bitacora_accesos;
import org.example.Repository.Usuario_repository;
import org.example.Repository.bitacora_repository;
import org.example.Service.Bitacora_service;
import org.example.Service.EstadoDesecho_service;
import org.example.Service.horario_service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.util.Optional;

@Controller
public class Dashboard_controller {

    private final EstadoDesecho_service detectService;
    private final Bitacora_service bitacoraService;
    private final Usuario_repository usuarioRepo;
    private final horario_service horarioService;
    private final bitacora_repository bitacoraRepo;
    private final PasswordEncoder passwordEncoder;

    public Dashboard_controller(EstadoDesecho_service detectService,
                                Bitacora_service bitacoraService,
                                Usuario_repository usuarioRepo,
                                horario_service horarioService,
                                bitacora_repository bitacoraRepo,
                                PasswordEncoder passwordEncoder) {
        this.detectService = detectService;
        this.bitacoraService = bitacoraService;
        this.usuarioRepo = usuarioRepo;
        this.horarioService = horarioService;
        this.bitacoraRepo = bitacoraRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. Mostrar la pantalla de login limpia
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // Devuelve login.html
    }

    // 2. Procesar el formulario que envía login.html
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpServletRequest request,
                                HttpSession session) {

        String ipRemota = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        // Buscar por nombre en PostgreSQL
        Optional<Usuario> usuarioOpt = usuarioRepo.findByNombre(username);

        if (usuarioOpt.isEmpty()) {
            registrarEnBitacora(null, username, ipRemota, userAgent, false, "USUARIO_NO_EXISTE");
            return "redirect:/login?error=true";
        }

        Usuario usuario = usuarioOpt.get();

        // Validar contraseña BCrypt con el getter de tu entidad
        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            registrarEnBitacora(usuario, username, ipRemota, userAgent, false, "PASSWORD_INCORRECTO");
            return "redirect:/login?error=true";
        }

        // Validar restricción horaria (08:00 - 17:00 / bandera rol)
        if (!horarioService.esAccesoPermitido(usuario)) {
            registrarEnBitacora(usuario, username, ipRemota, userAgent, false, "HORARIO_NO_PERMITIDO");
            return "redirect:/login?error=true";
        }

        // Acceso permitido
        registrarEnBitacora(usuario, username, ipRemota, userAgent, true, "OK");
        session.setAttribute("usuarioLogueado", usuario);

        return "redirect:/dashboard";
    }

    // 3. Mostrar el dashboard con los datos
    @GetMapping({"/", "/dashboard"})
    public String verDashboard(HttpSession session, Model model) {
        // Protección de ruta: si no hay sesión, regresa al login
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        model.addAttribute("desechos", detectService.obtenerTodos());
        model.addAttribute("accesos", bitacoraService.obtenerUltimosAccesos());
        return "index"; // Devuelve index.html
    }

    // 4. Cerrar sesión
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private void registrarEnBitacora(Usuario usuario, String username, String ip, String ua, boolean concedido, String motivo) {
        bitacora_accesos log = new bitacora_accesos();
        log.setUsuario(usuario);
        log.setUsernameIntentado(username);
        log.setIpOrigen(ip);
        log.setUserAgent(ua);
        log.setAccesoConcedido(concedido);
        log.setMotivoBloqueo(motivo);
        log.setFechaHora(OffsetDateTime.now());
        bitacoraRepo.save(log);
    }
    // Muestra la vista exclusiva de la Bitácora de Accesos en una pestaña independiente
    @GetMapping("/bitacora")
    public String verBitacora(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login"; // Validación de seguridad
        }

        // Carga únicamente los registros de la bitácora para mostrarlos en su propia tabla
        model.addAttribute("accesos", bitacoraService.obtenerUltimosAccesos());
        return "bitacora"; // Renderiza la nueva plantilla bitacora.html
    }

}