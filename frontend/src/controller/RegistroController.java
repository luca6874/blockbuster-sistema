package frontend.src.controller;

import frontend.src.dao.UsuarioDAO;
import frontend.src.model.UsuarioInfo;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Controlador de registro de usuarios.
 * Valida la entrada de la vista y delega la persistencia al DAO.
 */
public class RegistroController {
    public static final int PASSWORD_MIN_LENGTH = 6;

    public static UsuarioInfo registrar(String nombre, String primerApellido, String segundoApellido,
                                        String fechaNacimiento, String username, String correo,
                                        String password, String passwordConfirm) {
        try {
            if (nombre == null || primerApellido == null || segundoApellido == null || fechaNacimiento == null ||
                username == null || correo == null || password == null || passwordConfirm == null) {
                System.err.println("Error: campos nulos");
                return null;
            }

            nombre = nombre.trim();
            primerApellido = primerApellido.trim();
            segundoApellido = segundoApellido.trim();
            fechaNacimiento = fechaNacimiento.trim();
            username = username.trim();
            correo = correo.trim();
            password = password.trim();
            passwordConfirm = passwordConfirm.trim();

            if (nombre.isEmpty() || primerApellido.isEmpty() || segundoApellido.isEmpty() ||
                fechaNacimiento.isEmpty() || username.isEmpty() || correo.isEmpty() ||
                password.isEmpty() || passwordConfirm.isEmpty()) {
                System.err.println("Error: campos vacios");
                return null;
            }

            if (!password.equals(passwordConfirm)) {
                System.err.println("Error: las contrasenas no coinciden");
                return null;
            }

            if (password.length() < PASSWORD_MIN_LENGTH) {
                System.err.println("Error: contrasena demasiado corta");
                return null;
            }

            if (!correo.contains("@")) {
                System.err.println("Error: correo invalido");
                return null;
            }

            LocalDate fechaNacimientoParseada;
            try {
                fechaNacimientoParseada = LocalDate.parse(fechaNacimiento);
            } catch (DateTimeParseException e) {
                System.err.println("Error: fecha de nacimiento invalida");
                return null;
            }

            UsuarioInfo usuario = UsuarioDAO.registrar(
                    nombre,
                    primerApellido,
                    segundoApellido,
                    username,
                    correo,
                    fechaNacimientoParseada,
                    password
            );

            if (usuario != null) {
                System.out.println("Registro exitoso para: " + usuario.getUsername());
            } else {
                System.out.println("Registro fallido (username o correo duplicados)");
            }

            return usuario;
        } catch (Exception e) {
            System.err.println("Error en registrar: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean esValido(UsuarioInfo usuario) {
        return usuario != null && usuario.getIdUsuario() > 0;
    }
}
