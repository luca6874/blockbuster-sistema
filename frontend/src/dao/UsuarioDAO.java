package frontend.src.dao;

import frontend.src.model.UsuarioInfo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

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
            String sql = "SELECT id_usuario, nombre, primer_apellido, segundo_apellido, " +
                         "username, correo, fecha_nacimiento, password " +
                         "FROM usuarios " +
                         "WHERE username = ? AND password = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            // Si hay un resultado, mapear a UsuarioInfo
            if (rs.next()) {
                usuario = mapearUsuario(rs);
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

            String sql = "SELECT id_usuario, nombre, primer_apellido, segundo_apellido, " +
                         "username, correo, fecha_nacimiento, password " +
                         "FROM usuarios " +
                         "WHERE id_usuario = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = mapearUsuario(rs);
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

    /**
     * Registra un nuevo usuario en la base de datos.
     * 
     * Validaciones:
     * - Verifica que username sea único
     * - Verifica que correo sea único
     * - Inserta el usuario si pasa validaciones
     * 
     * @param username el nombre de usuario
     * @param correo el correo del usuario
     * @param password la contraseña del usuario
     * @return UsuarioInfo si el registro fue exitoso, null si falla (usuario/correo duplicados o error BD)
     */
    public static UsuarioInfo registrar(String nombre, String primerApellido, String segundoApellido,
                                        String username, String correo, LocalDate fechaNacimiento, String password) {
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            // Validación 1: Verificar que username sea único
            if (existeUsername(conn, username)) {
                System.out.println("Error: Username ya existe");
                return null;
            }

            // Validación 2: Verificar que correo sea único
            if (existeCorreo(conn, correo)) {
                System.out.println("Error: Correo ya existe");
                return null;
            }

            // Validación 3: Insertar nuevo usuario
            String sql = "INSERT INTO usuarios " +
                         "(nombre, primer_apellido, segundo_apellido, username, correo, fecha_nacimiento, password) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, nombre);
            ps.setString(2, primerApellido);
            ps.setString(3, segundoApellido);
            ps.setString(4, username);
            ps.setString(5, correo);
            ps.setDate(6, Date.valueOf(fechaNacimiento));
            ps.setString(7, password);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtener el ID generado
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idUsuario = generatedKeys.getInt(1);
                    
                    UsuarioInfo usuario = new UsuarioInfo();
                    usuario.setIdUsuario(idUsuario);
                    usuario.setNombre(nombre);
                    usuario.setPrimerApellido(primerApellido);
                    usuario.setSegundoApellido(segundoApellido);
                    usuario.setUsername(username);
                    usuario.setCorreo(correo);
                    usuario.setFechaNacimiento(fechaNacimiento);
                    usuario.setPassword(password);

                    System.out.println("✓ Usuario registrado exitosamente: " + username + " (ID: " + idUsuario + ")");
                    return usuario;
                }
            }

            ps.close();

        } catch (Exception e) {
            System.err.println("Error en registrar: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar(conn);
        }

        return null;
    }

    private static UsuarioInfo mapearUsuario(ResultSet rs) throws Exception {
        UsuarioInfo usuario = new UsuarioInfo();
        Date fechaNacimiento = rs.getDate("fecha_nacimiento");

        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setPrimerApellido(rs.getString("primer_apellido"));
        usuario.setSegundoApellido(rs.getString("segundo_apellido"));
        usuario.setUsername(rs.getString("username"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setFechaNacimiento(fechaNacimiento != null ? fechaNacimiento.toLocalDate() : null);
        usuario.setPassword(rs.getString("password"));

        return usuario;
    }

    /**
     * Verifica si un username ya existe en la base de datos.
     * 
     * @param conn conexión a la BD (reutilizar de registrar())
     * @param username el username a verificar
     * @return true si existe, false si no
     */
    private static boolean existeUsername(Connection conn, String username) {
        try {
            String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                ps.close();
                return count > 0;
            }

            ps.close();
        } catch (Exception e) {
            System.err.println("Error en existeUsername: " + e.getMessage());
        }

        return false;
    }

    /**
     * Verifica si un correo ya existe en la base de datos.
     * 
     * @param conn conexión a la BD (reutilizar de registrar())
     * @param correo el correo a verificar
     * @return true si existe, false si no
     */
    private static boolean existeCorreo(Connection conn, String correo) {
        try {
            String sql = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correo);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                ps.close();
                return count > 0;
            }

            ps.close();
        } catch (Exception e) {
            System.err.println("Error en existeCorreo: " + e.getMessage());
        }

        return false;
    }
}
