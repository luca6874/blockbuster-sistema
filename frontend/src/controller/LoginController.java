package frontend.src.controller;

import frontend.src.dao.UsuarioDAO;
import frontend.src.model.UsuarioInfo;

/**
 * LoginController - Controlador de autenticación.
 * 
 * Coordina la lógica de negocio para login.
 * Valida entrada del usuario y llama a DAO.
 * NO contiene SQL, solo lógica de validación.
 * NO muestra JOptionPane (eso es responsabilidad de la Vista).
 * 
 * Responsabilidades:
 * - Validar que campos no estén vacíos
 * - Validar formato básico
 * - Llamar a UsuarioDAO
 * - Procesar errores
 * - Retornar UsuarioInfo o null
 */
public class LoginController {

    /**
     * Valida y autentica un usuario contra la base de datos.
     * 
     * Pasos:
     * 1. Valida que username no esté vacío
     * 2. Valida que password no esté vacío
     * 3. Llama a UsuarioDAO.autenticar()
     * 4. Retorna UsuarioInfo si credenciales son correctas, null si no
     * 
     * @param username el nombre de usuario ingresado
     * @param password la contraseña ingresada
     * @return UsuarioInfo si autenticación es exitosa, null si falla
     */
    public static UsuarioInfo validarLogin(String username, String password) {
        try {
            // Validación 1: campos no deben ser nulos
            if (username == null || password == null) {
                System.err.println("Error: campos nulos");
                return null;
            }

            // Validación 2: campos no deben estar vacíos (incluyendo espacios)
            username = username.trim();
            password = password.trim();

            if (username.isEmpty() || password.isEmpty()) {
                System.err.println("Error: campos vacíos");
                return null;
            }

            // Validación 3: consultar BD
            UsuarioInfo usuario = UsuarioDAO.autenticar(username, password);

            if (usuario != null) {
                System.out.println("✓ Login exitoso para: " + usuario.getUsername());
            } else {
                System.out.println("✗ Credenciales incorrectas");
            }

            return usuario;

        } catch (Exception e) {
            System.err.println("Error en validarLogin: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Valida que un usuario sea válido después del login.
     * Útil para verificar si la sesión sigue siendo válida.
     * 
     * @param usuario el usuario a validar
     * @return true si usuario no es nulo y tiene ID válido
     */
    public static boolean esValido(UsuarioInfo usuario) {
        return usuario != null && usuario.getIdUsuario() > 0;
    }
}
