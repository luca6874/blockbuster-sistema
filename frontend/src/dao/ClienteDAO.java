package frontend.src.dao;

import frontend.src.model.ClienteInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * ClienteDAO - Data Access Object para clientes.
 * 
 * Maneja todas las operaciones de BD relacionadas con clientes.
 * Usa PreparedStatement para seguridad y legibilidad.
 * 
 * Nota: ClienteInfo se usa como modelo, no hay duplicados de clase.
 */
public class ClienteDAO {

    /**
     * Obtiene TODOS los clientes de la BD.
     * 
     * @return Lista de ClienteInfo con todos los clientes
     */
    public static List<ClienteInfo> obtenerTodos() {
        List<ClienteInfo> clientes = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = ConexionBD.conectar();
            
            String sql = "SELECT id_cliente, nombre, primer_apellido, segundo_apellido, " +
                         "correo_electronico, telefono, lvl_fidelidad " +
                         "FROM clientes " +
                         "ORDER BY nombre ASC";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ClienteInfo cliente = new ClienteInfo();
                cliente.setId("CLI-" + String.format("%03d", rs.getInt("id_cliente")));
                cliente.setNombre(rs.getString("nombre") + " " + rs.getString("primer_apellido"));
                cliente.setEmail(rs.getString("correo_electronico"));
                cliente.setEstatus("Activo");  // Por defecto, todos activos
                cliente.setNivel(obtenerNivelFidelidad(rs.getInt("lvl_fidelidad")));
                cliente.setFrecuente(rs.getInt("lvl_fidelidad") > 0);
                cliente.setTelefono(rs.getString("telefono"));
                
                clientes.add(cliente);
            }
            
            rs.close();
            ps.close();
            
        } catch (Exception e) {
            System.err.println("Error en obtenerTodos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar(conn);
        }
        
        return clientes;
    }

    /**
     * Obtiene un cliente específico por su ID.
     * 
     * @param id el ID del cliente (formato "CLI-001")
     * @return ClienteInfo si existe, null si no
     */
    public static ClienteInfo obtenerPorId(String id) {
        ClienteInfo cliente = null;
        Connection conn = null;
        
        try {
            conn = ConexionBD.conectar();
            
            // Extraer número del ID "CLI-001" → 1
            int idNumerico = Integer.parseInt(id.replace("CLI-", ""));
            
            String sql = "SELECT id_cliente, nombre, primer_apellido, segundo_apellido, " +
                         "correo_electronico, telefono, lvl_fidelidad " +
                         "FROM clientes " +
                         "WHERE id_cliente = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idNumerico);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                cliente = new ClienteInfo();
                cliente.setId("CLI-" + String.format("%03d", rs.getInt("id_cliente")));
                cliente.setNombre(rs.getString("nombre") + " " + rs.getString("primer_apellido"));
                cliente.setEmail(rs.getString("correo_electronico"));
                cliente.setEstatus("Activo");
                cliente.setNivel(obtenerNivelFidelidad(rs.getInt("lvl_fidelidad")));
                cliente.setFrecuente(rs.getInt("lvl_fidelidad") > 0);
                cliente.setTelefono(rs.getString("telefono"));
            }
            
            rs.close();
            ps.close();
            
        } catch (Exception e) {
            System.err.println("Error en obtenerPorId: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar(conn);
        }
        
        return cliente;
    }

    /**
     * Agrega un nuevo cliente a la BD.
     * 
     * @param cliente el ClienteInfo a insertar
     * @return true si fue exitoso, false si hubo error
     */
    public static boolean agregar(ClienteInfo cliente) {
        Connection conn = null;
        
        try {
            conn = ConexionBD.conectar();
            
            // Separar nombre completo en nombre y apellido
            String[] partes = cliente.getNombre().split(" ", 2);
            String nombre = partes.length > 0 ? partes[0] : "";
            String apellido = partes.length > 1 ? partes[1] : "";
            
            // Obtener nivel de fidelidad numérico
            int lvlFidelidad = obtenerLvlFidelidad(cliente.getNivel());
            
            String sql = "INSERT INTO clientes (nombre, primer_apellido, segundo_apellido, " +
                         "correo_electronico, telefono, lvl_fidelidad) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, "");  // segundo apellido vacío por defecto
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getTelefono());
            ps.setInt(6, lvlFidelidad);
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            
            return filasAfectadas > 0;
            
        } catch (Exception e) {
            System.err.println("Error en agregar: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            ConexionBD.cerrar(conn);
        }
    }

    /**
     * Actualiza un cliente existente.
     * 
     * @param cliente el ClienteInfo con datos actualizados
     * @return true si fue exitoso, false si hubo error
     */
    public static boolean actualizar(ClienteInfo cliente) {
        Connection conn = null;
        
        try {
            conn = ConexionBD.conectar();
            
            // Extraer número del ID
            int idNumerico = Integer.parseInt(cliente.getId().replace("CLI-", ""));
            
            // Separar nombre
            String[] partes = cliente.getNombre().split(" ", 2);
            String nombre = partes.length > 0 ? partes[0] : "";
            String apellido = partes.length > 1 ? partes[1] : "";
            
            int lvlFidelidad = obtenerLvlFidelidad(cliente.getNivel());
            
            String sql = "UPDATE clientes " +
                         "SET nombre = ?, primer_apellido = ?, correo_electronico = ?, " +
                         "    telefono = ?, lvl_fidelidad = ? " +
                         "WHERE id_cliente = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getTelefono());
            ps.setInt(5, lvlFidelidad);
            ps.setInt(6, idNumerico);
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            
            return filasAfectadas > 0;
            
        } catch (Exception e) {
            System.err.println("Error en actualizar: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            ConexionBD.cerrar(conn);
        }
    }

    /**
     * Elimina un cliente de la BD.
     * 
     * @param id el ID del cliente a eliminar
     * @return true si fue exitoso, false si hubo error
     */
    public static boolean eliminar(String id) {
        Connection conn = null;
        
        try {
            conn = ConexionBD.conectar();
            
            int idNumerico = Integer.parseInt(id.replace("CLI-", ""));
            
            String sql = "DELETE FROM clientes WHERE id_cliente = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idNumerico);
            
            int filasAfectadas = ps.executeUpdate();
            ps.close();
            
            return filasAfectadas > 0;
            
        } catch (Exception e) {
            System.err.println("Error en eliminar: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            ConexionBD.cerrar(conn);
        }
    }

    /**
     * Convierte un número de nivel de fidelidad a string.
     * 0 → Bronce, 1 → Plata, 2 → Oro
     */
    private static String obtenerNivelFidelidad(int lvl) {
        switch (lvl) {
            case 0: return "Bronce";
            case 1: return "Plata";
            case 2: return "Oro";
            default: return "Bronce";
        }
    }

    /**
     * Convierte un string de nivel a número.
     * Bronce → 0, Plata → 1, Oro → 2
     */
    private static int obtenerLvlFidelidad(String nivel) {
        switch (nivel) {
            case "Bronce": return 0;
            case "Plata": return 1;
            case "Oro": return 2;
            default: return 0;
        }
    }
}
