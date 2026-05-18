package frontend.src.controller;

import frontend.src.dao.UsuarioDAO;
import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Controlador para operaciones de usuario/perfil.
 */
public class UsuarioController {
    private static final int ANIO_MINIMO_FECHA_NACIMIENTO = 1900;
    private static final Pattern PATRON_NOMBRE = Pattern.compile(
        "^[a-zA-ZáéíóúñüÁÉÍÓÚÑÜàèìòùÀÈÌÒÙäëïöÄËÏÖ ]+$"
    );

    public static boolean actualizarUsuario(int idUsuario, String nombre, String primerApellido,
                                            String segundoApellido, LocalDate fechaNacimiento,
                                            String password) {
        if (idUsuario <= 0 || nombre == null || primerApellido == null || fechaNacimiento == null) {
            System.err.println("Error: datos de usuario invalidos");
            return false;
        }

        nombre = nombre.trim();
        primerApellido = primerApellido.trim();
        segundoApellido = segundoApellido != null ? segundoApellido.trim() : "";

        if (nombre.isEmpty() || primerApellido.isEmpty()) {
            System.err.println("Error: nombre y primer apellido son requeridos");
            return false;
        }

        if (!validarNombreYApellidos(nombre) || !validarNombreYApellidos(primerApellido)
                || (!segundoApellido.isEmpty() && !validarNombreYApellidos(segundoApellido))) {
            System.err.println("Error: nombre o apellidos contienen caracteres invalidos");
            return false;
        }

        if (fechaNacimiento.getYear() < ANIO_MINIMO_FECHA_NACIMIENTO) {
            System.err.println("Error: fecha no puede ser anterior a 1900");
            return false;
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            System.err.println("Error: fecha no puede ser futura");
            return false;
        }

        return UsuarioDAO.actualizar(idUsuario, nombre, primerApellido, segundoApellido, fechaNacimiento, password);
    }

    private static boolean validarNombreYApellidos(String texto) {
        return texto != null && !texto.isEmpty() && PATRON_NOMBRE.matcher(texto).matches();
    }
}
