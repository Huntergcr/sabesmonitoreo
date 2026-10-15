package org.example.controller;

import jakarta.servlet.http.HttpSession;
import org.example.Entity.Rol;
import org.example.Entity.Usuario;
import org.example.Repository.Rol_repository;
import org.example.Repository.Usuario_repository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

// @Controller indica a Spring que esta clase maneja peticiones web y devuelve vistas (HTML).
@Controller
public class Usuario_controller {

    // Repositorio para consultar y guardar usuarios en la base de datos PostgreSQL.
    private final Usuario_repository usuarioRepo;

    // Repositorio para obtener los roles disponibles (ej. Administrador, Operador, etc.).
    private final Rol_repository rolRepository;

    // Codificador para encriptar las contraseñas con BCrypt antes de almacenarlas.
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias mediante el constructor de la clase.
    public Usuario_controller(Usuario_repository usuarioRepo,
                              Rol_repository rolRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Muestra el formulario web para dar de alta a un nuevo usuario.
    @GetMapping("/usuarios/nuevo")
    public String mostrarFormularioRegistro(HttpSession session, Model model) {

        // Validación de seguridad: si no hay un usuario logueado en la sesión, se le redirige al login.
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        // Se envía un objeto Usuario vacío a la vista para capturar los datos del formulario.
        model.addAttribute("usuario", new Usuario());

        // Se cargan todos los roles desde la base de datos para mostrarlos en un menú desplegable (<select>).
        model.addAttribute("roles", rolRepository.findAll());

        // Retorna la plantilla HTML del formulario de registro.
        return "registro-usuario";
    }

    // Procesa los datos enviados desde el formulario de alta por método POST.
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@RequestParam("nombre") String nombre,
                                 @RequestParam("apellido") String apellido,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 @RequestParam("idRol") Integer idRol,
                                 HttpSession session) {

        // Validación de sesión activa por seguridad.
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        // Se instancia una nueva entidad Usuario para mapear los datos del formulario.
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setEmail(email);

        // IMPORTANTE: La contraseña se cifra con BCrypt por seguridad antes de guardarla en PostgreSQL.
        nuevoUsuario.setPasswordHash(passwordEncoder.encode(password));

        // Se define el usuario como activo por defecto y se asigna la fecha de creación actual.
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());

        // Se busca el rol seleccionado en la base de datos y se le asigna al usuario.
        Rol rol = rolRepository.findById(idRol).orElse(null);
        nuevoUsuario.setRol(rol);

        // Se guarda el nuevo usuario en la base de datos PostgreSQL.
        usuarioRepo.save(nuevoUsuario);

        // Redirige al dashboard indicando éxito en la operación.
        return "redirect:/dashboard?exito=true";
    }
}