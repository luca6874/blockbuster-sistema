package frontend.src.dao;

import frontend.src.model.UsuarioInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * UsuarioDAO - Data Access Object para usuarios.
 * 
 * Maneja todas las operaciones de BD relacionadas con usuarios (login, validación).
 * Usa PreparedStatement para seguridad contra SQL injection.
 * 
 * Tabla: usuarios
 * Columnas: id_usuario, username, correo, password
 */
public class UsuarioDAO {

    /**
     * Autentica un usuario contra la base de datos.
     * 
     * Busca en la tabla 'usuarios' una fila donde:
     * - username coincida exactamente
     * - password coincida exactamente
     * 
     * @param username el nombre de usuario
     * @param password la contraseña
     * @return UsuarioInfo si las credenciales son correctas, null si no existen
     */
    public static UsuarioInfo autenticar(String username, String password) {
        UsuarioInfo usuario = null;
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            // Usar PreparedStatement para evitar SQL injection
            String sql = "SELECT id_usuario, username, correo, password " +
                         "FROM usuarios " +
                         "WHERE username = ? AND password = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            // Si hay un resultado, mapear a UsuarioInfo
            if (rs.next()) {
                usuario = new UsuarioInfo();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPassword(rs.getString("password"));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.err.println("Error en autenticar: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar(conn);
        }

        // Retorna null si no se encontró usuario, o UsuarioInfo si se autenticó
        return usuario;
    }

    /**
     * Obtiene un usuario por ID (para usar después del login).
     * 
     * @param idUsuario el ID del usuario
     * @return UsuarioInfo si existe, null si no
     */
    public static UsuarioInfo obtenerPorId(int idUsuario) {
        UsuarioInfo usuario = null;
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            String sql = "SELECT id_usuario, username, correo, password " +
                         "FROM usuarios " +
                         "WHERE id_usuario = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new UsuarioInfo();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPassword(rs.getString("password"));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.err.println("Error en obtenerPorId: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar(conn);
        }

        return usuario;
    }
}
