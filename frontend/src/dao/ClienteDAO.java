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
            sincronizarNivelesFidelidad();
            conn = ConexionBD.conectar();

            String sql = "SELECT id_cliente, nombre, primer_apellido, segundo_apellido, " +
                         "correo_electronico, fecha_nacimiento, telefono, lvl_fidelidad, puntos, foto " +
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
            sincronizarNivelesFidelidad();
            conn = ConexionBD.conectar();

            int idNumerico = Integer.parseInt(id.replace("CLI-", ""));

            String sql = "SELECT id_cliente, nombre, primer_apellido, segundo_apellido, " +
                         "correo_electronico, fecha_nacimiento, telefono, lvl_fidelidad, puntos, foto " +
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

    public static int sincronizarNivelesFidelidad() {
        int actualizados = 0;
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false);

            String sqlSelect = "SELECT id_cliente, COALESCE(puntos, 0) AS puntos, " +
                               "COALESCE(lvl_fidelidad, 0) AS lvl_fidelidad " +
                               "FROM clientes";

            PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
            ResultSet rs = psSelect.executeQuery();

            String sqlUpdate = "UPDATE clientes SET lvl_fidelidad = ? WHERE id_cliente = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);

            while (rs.next()) {
                int idCliente = rs.getInt("id_cliente");
                int puntos = rs.getInt("puntos");
                int nivelActual = rs.getInt("lvl_fidelidad");
                int nivelCalculado = NivelFidelidad.calcularNivelFidelidad(puntos);

                if (nivelActual != nivelCalculado) {
                    psUpdate.setInt(1, nivelCalculado);
                    psUpdate.setInt(2, idCliente);
                    actualizados += psUpdate.executeUpdate();
                }
            }

            rs.close();
            psSelect.close();
            psUpdate.close();
            conn.commit();

            if (actualizados > 0) {
                System.out.println("Niveles de fidelidad sincronizados: " + actualizados);
            }
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackError) {
                    System.err.println("Error al revertir sincronizacion de niveles: " + rollbackError.getMessage());
                }
            }
            System.err.println("Error al sincronizar niveles de fidelidad: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (Exception autoCommitError) {
                    System.err.println("Error al restaurar autocommit: " + autoCommitError.getMessage());
                }
            }
            ConexionBD.cerrar(conn);
        }

        return actualizados;
    }

    public static boolean agregar(ClienteInfo cliente) {
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            // Nuevos clientes inician como Bronce (nivel 1)
            int lvlFidelidad = NivelFidelidad.NIVEL_BRONCE;

            String sql = "INSERT INTO clientes (nombre, primer_apellido, segundo_apellido, " +
                         "correo_electronico, fecha_nacimiento, telefono, lvl_fidelidad, foto) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getPrimerApellido());
            ps.setString(3, emptyToNull(cliente.getSegundoApellido()));
            ps.setString(4, cliente.getEmail());
            setFechaNacimiento(ps, 5, cliente.getFechaNacimiento());
            ps.setString(6, emptyToNull(cliente.getTelefono()));
            ps.setInt(7, lvlFidelidad);
            ps.setString(8, emptyToNull(cliente.getFoto()));

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
            String sql = "UPDATE clientes " +
                         "SET nombre = ?, primer_apellido = ?, segundo_apellido = ?, correo_electronico = ?, " +
                         "    fecha_nacimiento = ?, telefono = ?, foto = ? " +
                         "WHERE id_cliente = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getPrimerApellido());
            ps.setString(3, emptyToNull(cliente.getSegundoApellido()));
            ps.setString(4, cliente.getEmail());
            setFechaNacimiento(ps, 5, cliente.getFechaNacimiento());
            ps.setString(6, emptyToNull(cliente.getTelefono()));
            ps.setString(7, emptyToNull(cliente.getFoto()));
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
        
        // Obtener lvl_fidelidad directamente de BD (1-4)
        int lvlFidelidad = rs.getInt("lvl_fidelidad");
        cliente.setLvlFidelidad(lvlFidelidad);
        cliente.setNivel(NivelFidelidad.obtenerNombreNivel(lvlFidelidad));
        
        cliente.setFrecuente(lvlFidelidad >= NivelFidelidad.NIVEL_BRONCE);
        cliente.setPuntos(rs.getInt("puntos"));
        cliente.setFoto(rs.getString("foto"));
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
