package frontend.src.controller;

import frontend.src.dao.UsuarioDAO;
import frontend.src.model.UsuarioInfo;

/**
 * RegistroController - Controlador de registro de usuarios.
 * 
 * Coordina la lógica de negocio para registro.
 * Valida entrada del usuario y llama a DAO.
 * NO contiene SQL, solo lógica de validación.
 * NO muestra JOptionPane (eso es responsabilidad de la Vista).
 * 
 * Responsabilidades:
 * - Validar que campos no estén vacíos
 * - Validar que passwords coincidan
 * - Llamar a UsuarioDAO.registrar()
 * - Retornar UsuarioInfo o null
 */
public class RegistroController {
    public static final int PASSWORD_MIN_LENGTH = 6;

    /**
     * Valida y registra un nuevo usuario en la base de datos.
     * 
     * Pasos:
     * 1. Valida que todos los campos no sean nulos/vacíos
     * 2. Valida que las contraseñas coincidan
     * 3. Llama a UsuarioDAO.registrar()
     * 4. Retorna UsuarioInfo si registro es exitoso, null si falla
     * 
     * @param username el nombre de usuario ingresado
     * @param correo el correo del usuario
     * @param password la contraseña ingresada
     * @param passwordConfirm la confirmación de contraseña
     * @return UsuarioInfo si registro exitoso, null si falla
     */
    public static UsuarioInfo registrar(String username, String correo, String password, String passwordConfirm) {
        try {
            // Validación 1: campos no deben ser nulos
            if (username == null || correo == null || password == null || passwordConfirm == null) {
                System.err.println("Error: campos nulos");
                return null;
            }

            // Validación 2: campos no deben estar vacíos (incluyendo espacios)
            username = username.trim();
            correo = correo.trim();
            password = password.trim();
            passwordConfirm = passwordConfirm.trim();

            if (username.isEmpty() || correo.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                System.err.println("Error: campos vacíos");
                return null;
            }

            // Validación 3: las contraseñas deben coincidir
            if (!password.equals(passwordConfirm)) {
                System.err.println("Error: las contraseñas no coinciden");
                return null;
            }

            // Validación 4: la contraseña debe cumplir longitud mínima
            if (password.length() < PASSWORD_MIN_LENGTH) {
                System.err.println("Error: contraseña demasiado corta");
                return null;
            }

            // Validación 5: el correo debe tener formato válido básico
            if (!correo.contains("@")) {
                System.err.println("Error: correo inválido");
                return null;
            }

            // Validación 6: consultar BD (registrar)
            UsuarioInfo usuario = UsuarioDAO.registrar(username, correo, password);

            if (usuario != null) {
                System.out.println("✓ Registro exitoso para: " + usuario.getUsername());
            } else {
                System.out.println("✗ Registro fallido (username o correo duplicados)");
            }

            return usuario;

        } catch (Exception e) {
            System.err.println("Error en registrar: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida que un usuario sea válido después del registro.
     * 
     * @param usuario el usuario a validar
     * @return true si usuario no es nulo y tiene ID válido
     */
    public static boolean esValido(UsuarioInfo usuario) {
        return usuario != null && usuario.getIdUsuario() > 0;
    }
}
