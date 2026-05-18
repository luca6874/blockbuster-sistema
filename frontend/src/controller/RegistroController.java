package frontend.src.controller;

import frontend.src.dao.UsuarioDAO;
import frontend.src.model.UsuarioInfo;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Controlador de registro de usuarios.
 * Valida la entrada de la vista y delega la persistencia al DAO.
 */
public class RegistroController {
    public static final int PASSWORD_MIN_LENGTH = 6;
    
    // Patrón para validar nombres/apellidos: letras, espacios, acentos y ñ
    private static final Pattern PATRON_NOMBRE = Pattern.compile(
        "^[a-zA-ZáéíóúñüÁÉÍÓÚÑÜàèìòùÀÈÌÒÙäëïöÄËÏÖ ]+$"
    );

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

            // NUEVA VALIDACIÓN: Nombre y apellidos solo letras
            if (!validarNombreYApellidos(nombre)) {
                System.err.println("Error: nombre contiene caracteres inválidos");
                return null;
            }
            
            if (!validarNombreYApellidos(primerApellido)) {
                System.err.println("Error: primer apellido contiene caracteres inválidos");
                return null;
            }
            
            if (!validarNombreYApellidos(segundoApellido)) {
                System.err.println("Error: segundo apellido contiene caracteres inválidos");
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

            // NUEVA VALIDACIÓN: Fecha no puede ser futura
            if (!validarFechaNacimiento(fechaNacimientoParseada)) {
                System.err.println("Error: fecha de nacimiento no puede ser futura");
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

    /**
     * Valida que un nombre/apellido solo contenga letras, espacios, acentos y ñ.
     * No permite números, símbolos ni caracteres especiales.
     * 
     * Ejemplos válidos: "Luca", "José", "María Fernanda", "Muñoz"
     * Ejemplos inválidos: "Luca123", "@#$%", "43t3+32"
     * 
     * @param texto el texto a validar
     * @return true si es válido, false si contiene caracteres inválidos
     */
    private static boolean validarNombreYApellidos(String texto) {
        if (texto == null || texto.isEmpty()) {
            return false;
        }
        return PATRON_NOMBRE.matcher(texto).matches();
    }

    /**
     * Valida que la fecha de nacimiento no sea futura.
     * 
     * @param fechaNacimiento la fecha a validar
     * @return true si la fecha es válida (no es futura), false si es futura
     */
    private static boolean validarFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return false;
        }
        return !fechaNacimiento.isAfter(LocalDate.now());
    }
}
