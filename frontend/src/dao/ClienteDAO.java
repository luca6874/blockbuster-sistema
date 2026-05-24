package frontend.src.dao;

import frontend.src.model.ClienteInfo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * ClienteDAO - Data Access Object para clientes.
 *
 * Maneja todas las operaciones de BD relacionadas con clientes.
 * Usa PreparedStatement para seguridad y legibilidad.
 */
public class ClienteDAO {

    public static List<ClienteInfo> obtenerTodos() {
        List<ClienteInfo> clientes = new ArrayList<>();
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            String sql = "SELECT id_cliente, nombre, primer_apellido, segundo_apellido, " +
                         "correo_electronico, fecha_nacimiento, telefono, lvl_fidelidad, puntos " +
                         "FROM clientes " +
                         "ORDER BY nombre ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ClienteInfo cliente = mapearCliente(rs);
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

    public static ClienteInfo obtenerPorId(String id) {
        ClienteInfo cliente = null;
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            int idNumerico = Integer.parseInt(id.replace("CLI-", ""));

            String sql = "SELECT id_cliente, nombre, primer_apellido, segundo_apellido, " +
                         "correo_electronico, fecha_nacimiento, telefono, lvl_fidelidad, puntos " +
                         "FROM clientes " +
                         "WHERE id_cliente = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idNumerico);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cliente = mapearCliente(rs);
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

    public static boolean agregar(ClienteInfo cliente) {
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            // Convertir nivel a formato BD (0-2)
            int lvlFidelidadBD = NivelFidelidad.convertirABD(NivelFidelidad.NIVEL_BRONCE);

            String sql = "INSERT INTO clientes (nombre, primer_apellido, segundo_apellido, " +
                         "correo_electronico, fecha_nacimiento, telefono, lvl_fidelidad) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getPrimerApellido());
            ps.setString(3, emptyToNull(cliente.getSegundoApellido()));
            ps.setString(4, cliente.getEmail());
            setFechaNacimiento(ps, 5, cliente.getFechaNacimiento());
            ps.setString(6, emptyToNull(cliente.getTelefono()));
            ps.setInt(7, lvlFidelidadBD);

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

    public static boolean actualizar(ClienteInfo cliente) {
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            int idNumerico = Integer.parseInt(cliente.getId().replace("CLI-", ""));
            // Convertir nivel a formato BD (0-2) - obtener del objeto si tiene lvlFidelidad
            int nivelInterno = cliente.getLvlFidelidad() > 0 ? cliente.getLvlFidelidad() : 
                               NivelFidelidad.convertirDesdeBD(0);
            int lvlFidelidadBD = NivelFidelidad.convertirABD(nivelInterno);

            String sql = "UPDATE clientes " +
                         "SET nombre = ?, primer_apellido = ?, segundo_apellido = ?, correo_electronico = ?, " +
                         "    fecha_nacimiento = ?, telefono = ?, lvl_fidelidad = ? " +
                         "WHERE id_cliente = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getPrimerApellido());
            ps.setString(3, emptyToNull(cliente.getSegundoApellido()));
            ps.setString(4, cliente.getEmail());
            setFechaNacimiento(ps, 5, cliente.getFechaNacimiento());
            ps.setString(6, emptyToNull(cliente.getTelefono()));
            ps.setInt(7, lvlFidelidadBD);
            ps.setInt(8, idNumerico);

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

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("CLIENTE_CON_OPERACIONES");
        } catch (Exception e) {
            System.err.println("Error en eliminar: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            ConexionBD.cerrar(conn);
        }
    }

    private static ClienteInfo mapearCliente(ResultSet rs) throws Exception {
        ClienteInfo cliente = new ClienteInfo();
        cliente.setId("CLI-" + String.format("%03d", rs.getInt("id_cliente")));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setPrimerApellido(rs.getString("primer_apellido"));
        cliente.setSegundoApellido(rs.getString("segundo_apellido"));
        cliente.setEmail(rs.getString("correo_electronico"));
        cliente.setEstatus("Activo");
        
        // Obtener lvl_fidelidad de BD y convertirlo a nivel interno
        int lvlBD = rs.getInt("lvl_fidelidad");
        int nivelInterno = NivelFidelidad.convertirDesdeBD(lvlBD);
        cliente.setLvlFidelidad(nivelInterno);
        cliente.setNivel(NivelFidelidad.obtenerNombreNivel(nivelInterno));
        
        cliente.setFrecuente(lvlBD > 0);
        cliente.setPuntos(rs.getInt("puntos"));
        cliente.setTelefono(rs.getString("telefono"));

        Date fechaNacimiento = rs.getDate("fecha_nacimiento");
        cliente.setFechaNacimiento(fechaNacimiento != null ? fechaNacimiento.toString() : "");

        return cliente;
    }



    private static String emptyToNull(String valor) {
        return valor == null || valor.trim().isEmpty() ? null : valor.trim();
    }

    private static void setFechaNacimiento(PreparedStatement ps, int index, String fecha) throws Exception {
        Date sqlDate = parseFechaNacimiento(fecha);
        if (sqlDate == null) {
            ps.setNull(index, Types.DATE);
        } else {
            ps.setDate(index, sqlDate);
        }
    }

    private static Date parseFechaNacimiento(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return null;
        }

        String valor = fecha.trim();
        try {
            return Date.valueOf(LocalDate.parse(valor));
        } catch (DateTimeParseException ignored) {
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return Date.valueOf(LocalDate.parse(valor, formatter));
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }
}
