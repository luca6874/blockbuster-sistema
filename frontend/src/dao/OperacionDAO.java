package frontend.src.dao;

import frontend.src.model.OperacionInfo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * OperacionDAO - Data Access Object para operaciones de renta y compra.
 *
 * Maneja todas las operaciones de BD relacionadas con operaciones.
 * Incluye validación de stock y actualización de stock.
 * Usa PreparedStatement para seguridad.
 * 
 * 
 * @param coso parametro de entrada y @return es el resultado esperado. <-- documentacion y ajá
 */
public class OperacionDAO {

    public static List<OperacionInfo> obtenerTodos() {
        List<OperacionInfo> operaciones = new ArrayList<>();
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            String sql = "SELECT id_operacion, id_cliente, id_videojuego, id_usuario, tipo, " +
                         "monto, descuento, fecha_operacion, fecha_devolucion " +
                         "FROM operaciones " +
                         "ORDER BY fecha_operacion DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                operaciones.add(mapearOperacion(rs));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.err.println("Error en obtenerTodos operaciones: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar(conn);
        }

        return operaciones;
    }

    public static List<String[]> obtenerTodas() {
        List<String[]> operaciones = new ArrayList<>();
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            String sql = "SELECT o.id_operacion, " +
                         "CONCAT(c.nombre, ' ', c.primer_apellido, COALESCE(CONCAT(' ', c.segundo_apellido), '')) AS cliente, " +
                         "c.correo_electronico, " +
                         "v.nombre AS videojuego, v.plataforma, v.imagen, " +
                         "u.username AS usuario, " +
                         "o.tipo, o.monto, o.descuento, o.fecha_operacion, o.fecha_devolucion " +
                         "FROM operaciones o " +
                         "INNER JOIN clientes c ON o.id_cliente = c.id_cliente " +
                         "INNER JOIN videojuegos v ON o.id_videojuego = v.id_videojuego " +
                         "INNER JOIN usuarios u ON o.id_usuario = u.id_usuario " +
                         "ORDER BY o.fecha_operacion DESC, o.id_operacion DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Date fechaOperacion = rs.getDate("fecha_operacion");
                Date fechaDevolucion = rs.getDate("fecha_devolucion");
                double monto = rs.getDouble("monto");
                double descuento = rs.getDouble("descuento");
                double total = calcularTotalOperacion(monto, descuento);

                operaciones.add(new String[]{
                    rs.getString("cliente"),
                    rs.getString("videojuego"),
                    rs.getString("tipo"),
                    String.format("$%.2f", monto),
                    descuento > 0 ? String.format("$%.2f", descuento) : "N/A",
                    fechaOperacion != null ? fechaOperacion.toLocalDate().toString() : "N/A",
                    String.valueOf(calcularPuntosGanados(total)),
                    String.format("%05d", rs.getInt("id_operacion")),
                    fechaDevolucion != null ? fechaDevolucion.toLocalDate().toString() : "N/A",
                    "RENTA".equalsIgnoreCase(rs.getString("tipo")) ? "Activa" : "Completada",
                    rs.getString("correo_electronico") != null ? rs.getString("correo_electronico") : "",
                    rs.getString("plataforma") != null ? rs.getString("plataforma") : "Catalogo general",
                    rs.getString("imagen") != null ? rs.getString("imagen") : "",
                    rs.getString("usuario")
                });
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.err.println("Error en obtenerTodas operaciones: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar(conn);
        }

        return operaciones;
    }

    public static OperacionInfo obtenerPorId(int id) {
        OperacionInfo operacion = null;
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            String sql = "SELECT id_operacion, id_cliente, id_videojuego, id_usuario, tipo, " +
                         "monto, descuento, fecha_operacion, fecha_devolucion " +
                         "FROM operaciones " +
                         "WHERE id_operacion = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                operacion = mapearOperacion(rs);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.err.println("Error en obtenerPorId operacion: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar(conn);
        }

        return operacion;
    }

    public static List<OperacionInfo> obtenerPorCliente(int idCliente) {
        List<OperacionInfo> operaciones = new ArrayList<>();
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            String sql = "SELECT id_operacion, id_cliente, id_videojuego, id_usuario, tipo, " +
                         "monto, descuento, fecha_operacion, fecha_devolucion " +
                         "FROM operaciones " +
                         "WHERE id_cliente = ? " +
                         "ORDER BY fecha_operacion DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                operaciones.add(mapearOperacion(rs));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.err.println("Error en obtenerPorCliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar(conn);
        }

        return operaciones;
    }

    /**
     * Inserta una nueva operación en la BD.
     * 
     * IMPORTANTE:
     * 1. Valida que el stock del videojuego sea > 0
     * 2. Inserta la operación
     * 3. Decrementa el stock del videojuego en 1
     * 4. Si falla en algún paso, retorna false
     *
     * @param operacion La operación a insertar
     * @return true si fue exitoso, false si no
     */
    public static boolean insertar(OperacionInfo operacion) {
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false);

            // Paso 1: Validar que el stock > 0
            int stockActual = obtenerStockVideojuego(conn, operacion.getIdVideojuego());
            if (stockActual <= 0) {
                System.err.println("Error: No hay stock disponible para el videojuego ID: " + operacion.getIdVideojuego());
                conn.rollback();
                return false;
            }

            // Paso 2: Insertar la operación
            String sqlInsert = "INSERT INTO operaciones (id_cliente, id_videojuego, id_usuario, tipo, " +
                               "monto, descuento, fecha_operacion, fecha_devolucion) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setInt(1, operacion.getIdCliente());
            psInsert.setInt(2, operacion.getIdVideojuego());
            psInsert.setInt(3, operacion.getIdUsuario());
            psInsert.setString(4, operacion.getTipo());
            psInsert.setDouble(5, operacion.getMonto());
            psInsert.setDouble(6, operacion.getDescuento());
            psInsert.setDate(7, sqlDate(operacion.getFechaOperacion()));
            
            if (operacion.getFechaDevolucion() != null) {
                psInsert.setDate(8, sqlDate(operacion.getFechaDevolucion()));
            } else {
                psInsert.setNull(8, Types.DATE);
            }

            int filasInsertadas = psInsert.executeUpdate();
            psInsert.close();

            if (filasInsertadas == 0) {
                System.err.println("Error: No se pudo insertar la operacion");
                conn.rollback();
                return false;
            }

            // Paso 3: Actualizar stock del videojuego
            String sqlUpdate = "UPDATE videojuegos SET stock = stock - 1 WHERE id_videojuego = ? AND stock > 0 AND activo = TRUE";

            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, operacion.getIdVideojuego());

            int filasActualizadas = psUpdate.executeUpdate();
            psUpdate.close();

            if (filasActualizadas == 0) {
                System.err.println("Error: La operacion no se completo porque el stock no se actualizo");
                conn.rollback();
                return false;
            }

            // Paso 4: Sumar puntos automaticos al cliente por el total neto de la operacion.
            int puntosGanados = calcularPuntosGanados(calcularTotalOperacion(operacion.getMonto(), operacion.getDescuento()));
            String sqlPuntos = "UPDATE clientes SET puntos = COALESCE(puntos, 0) + ? WHERE id_cliente = ?";

            PreparedStatement psPuntos = conn.prepareStatement(sqlPuntos);
            psPuntos.setInt(1, puntosGanados);
            psPuntos.setInt(2, operacion.getIdCliente());

            int clientesActualizados = psPuntos.executeUpdate();
            psPuntos.close();

            if (clientesActualizados == 0) {
                System.err.println("Error: La operacion no se completo porque no se actualizaron los puntos del cliente");
                conn.rollback();
                return false;
            }

            // Paso 5: Recalcular lvl_fidelidad automaticamente segun los nuevos puntos
            String sqlRecalcularNivel = "SELECT puntos FROM clientes WHERE id_cliente = ?";
            PreparedStatement psSelectPuntos = conn.prepareStatement(sqlRecalcularNivel);
            psSelectPuntos.setInt(1, operacion.getIdCliente());
            ResultSet rsSelectPuntos = psSelectPuntos.executeQuery();
            
            if (rsSelectPuntos.next()) {
                int puntosActuales = rsSelectPuntos.getInt("puntos");
                int nuevoNivelInterno = NivelFidelidad.calcularNivelFidelidad(puntosActuales);
                int nuevoNivelBD = NivelFidelidad.convertirABD(nuevoNivelInterno);
                
                String sqlUpdateNivel = "UPDATE clientes SET lvl_fidelidad = ? WHERE id_cliente = ?";
                PreparedStatement psUpdateNivel = conn.prepareStatement(sqlUpdateNivel);
                psUpdateNivel.setInt(1, nuevoNivelBD);
                psUpdateNivel.setInt(2, operacion.getIdCliente());
                
                int nivelActualizado = psUpdateNivel.executeUpdate();
                psUpdateNivel.close();
                
                if (nivelActualizado == 0) {
                    System.err.println("Advertencia: No se pudo actualizar el nivel de fidelidad");
                    // No hacemos rollback aqui, solo advertencia
                }
                
                System.out.println("Nivel de fidelidad recalculado: " + puntosActuales + " puntos -> Nivel " + 
                                 NivelFidelidad.obtenerNombreNivel(nuevoNivelInterno));
            }
            
            rsSelectPuntos.close();
            psSelectPuntos.close();

            conn.commit();
            return true;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackError) {
                    System.err.println("Error al revertir operacion: " + rollbackError.getMessage());
                }
            }
            System.err.println("Error en insertar operación: " + e.getMessage());
            e.printStackTrace();
            return false;
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
    }

    public static boolean actualizar(OperacionInfo operacion) {
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            String sql = "UPDATE operaciones " +
                         "SET id_cliente = ?, id_videojuego = ?, id_usuario = ?, tipo = ?, " +
                         "    monto = ?, descuento = ?, fecha_operacion = ?, fecha_devolucion = ? " +
                         "WHERE id_operacion = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, operacion.getIdCliente());
            ps.setInt(2, operacion.getIdVideojuego());
            ps.setInt(3, operacion.getIdUsuario());
            ps.setString(4, operacion.getTipo());
            ps.setDouble(5, operacion.getMonto());
            ps.setDouble(6, operacion.getDescuento());
            ps.setDate(7, sqlDate(operacion.getFechaOperacion()));
            
            if (operacion.getFechaDevolucion() != null) {
                ps.setDate(8, sqlDate(operacion.getFechaDevolucion()));
            } else {
                ps.setNull(8, Types.DATE);
            }
            
            ps.setInt(9, operacion.getIdOperacion());

            int filasAfectadas = ps.executeUpdate();
            ps.close();

            return filasAfectadas > 0;

        } catch (Exception e) {
            System.err.println("Error en actualizar operación: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            ConexionBD.cerrar(conn);
        }
    }

    public static boolean eliminar(int id) {
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            String sql = "DELETE FROM operaciones WHERE id_operacion = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();
            ps.close();

            return filasAfectadas > 0;

        } catch (Exception e) {
            System.err.println("Error en eliminar operación: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            ConexionBD.cerrar(conn);
        }
    }

    /**
     * Cuenta la cantidad de videojuegos rentados por un cliente.
     * 
     * @param idClienteNumerico ID numérico del cliente (ej: 3)
     * @return Cantidad de juegos rentados, o 0 si no hay operaciones
     */
    public static int contarJuegosRentados(int idClienteNumerico) {
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            String sql = "SELECT COUNT(*) as total FROM operaciones " +
                         "WHERE id_cliente = ? AND UPPER(tipo) = 'RENTA'";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idClienteNumerico);
            ResultSet rs = ps.executeQuery();

            int total = 0;
            if (rs.next()) {
                total = rs.getInt("total");
            }

            rs.close();
            ps.close();

            return total;

        } catch (Exception e) {
            System.err.println("Error en contarJuegosRentados: " + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            ConexionBD.cerrar(conn);
        }
    }

    /**
     * Cuenta la cantidad de videojuegos comprados por un cliente.
     * 
     * @param idClienteNumerico ID numérico del cliente (ej: 3)
     * @return Cantidad de juegos comprados, o 0 si no hay operaciones
     */
    public static int contarJuegosComprados(int idClienteNumerico) {
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            String sql = "SELECT COUNT(*) as total FROM operaciones " +
                         "WHERE id_cliente = ? AND UPPER(tipo) = 'COMPRA'";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idClienteNumerico);
            ResultSet rs = ps.executeQuery();

            int total = 0;
            if (rs.next()) {
                total = rs.getInt("total");
            }

            rs.close();
            ps.close();

            return total;

        } catch (Exception e) {
            System.err.println("Error en contarJuegosComprados: " + e.getMessage());
            e.printStackTrace();
            return 0;
        } finally {
            ConexionBD.cerrar(conn);
        }
    }

    private static OperacionInfo mapearOperacion(ResultSet rs) throws Exception {
        OperacionInfo operacion = new OperacionInfo();
        operacion.setIdOperacion(rs.getInt("id_operacion"));
        operacion.setIdCliente(rs.getInt("id_cliente"));
        operacion.setIdVideojuego(rs.getInt("id_videojuego"));
        operacion.setIdUsuario(rs.getInt("id_usuario"));
        operacion.setTipo(rs.getString("tipo"));
        operacion.setMonto(rs.getDouble("monto"));
        operacion.setDescuento(rs.getDouble("descuento"));

        Date fechaOperacion = rs.getDate("fecha_operacion");
        operacion.setFechaOperacion(fechaOperacion != null ? fechaOperacion.toLocalDate() : null);

        Date fechaDevolucion = rs.getDate("fecha_devolucion");
        operacion.setFechaDevolucion(fechaDevolucion != null ? fechaDevolucion.toLocalDate() : null);

        return operacion;
    }

    /**
     * Obtiene el stock actual de un videojuego.
     * Método privado para uso interno del DAO.
     */
    private static int obtenerStockVideojuego(Connection conn, int idVideojuego) throws Exception {
        String sql = "SELECT stock FROM videojuegos WHERE id_videojuego = ? AND activo = TRUE";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idVideojuego);
        ResultSet rs = ps.executeQuery();

        int stock = 0;
        if (rs.next()) {
            stock = rs.getInt("stock");
        }

        rs.close();
        ps.close();

        return stock;
    }

    public static int calcularPuntosGanados(double totalOperacion) {
        return (int) (Math.max(0.0, totalOperacion) / 10);
    }

    private static double calcularTotalOperacion(double monto, double descuento) {
        return Math.max(0.0, monto - descuento);
    }

    /**
     * Convierte LocalDate a java.sql.Date
     */
    private static Date sqlDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.valueOf(localDate);
    }
}
