package frontend.src.dao;

import frontend.src.model.VideojuegoInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoDAO {

    public static List<VideojuegoInfo> obtenerTodos() {
        List<VideojuegoInfo> videojuegos = new ArrayList<>();
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            String sql = "SELECT id_videojuego, nombre, plataforma, genero, clasificacion, " +
                         "anio_lanzamiento, precio_renta, precio_compra, stock, imagen, puntos " +
                         "FROM videojuegos " +
                         "WHERE activo = TRUE " +
                         "ORDER BY nombre ASC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                videojuegos.add(mapearVideojuego(rs));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.err.println("Error en obtenerTodos videojuegos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar(conn);
        }

        return videojuegos;
    }

    public static VideojuegoInfo obtenerPorId(String id) {
        VideojuegoInfo videojuego = null;
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            int idNumerico = extraerIdNumerico(id);
            String sql = "SELECT id_videojuego, nombre, plataforma, genero, clasificacion, " +
                         "anio_lanzamiento, precio_renta, precio_compra, stock, imagen, puntos " +
                         "FROM videojuegos " +
                         "WHERE id_videojuego = ? AND activo = TRUE";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idNumerico);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                videojuego = mapearVideojuego(rs);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.err.println("Error en obtenerPorId videojuego: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar(conn);
        }

        return videojuego;
    }

    public static boolean agregar(VideojuegoInfo videojuego) {
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            String sql = "INSERT INTO videojuegos (nombre, plataforma, genero, clasificacion, " +
                         "anio_lanzamiento, precio_renta, precio_compra, stock, imagen, puntos) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, videojuego.getTitulo());
            ps.setString(2, emptyToNull(videojuego.getPlataforma()));
            ps.setString(3, emptyToNull(videojuego.getGenero()));
            ps.setString(4, videojuego.getClasificacion());
            setYear(ps, 5, videojuego.getAnioLanzamiento());
            ps.setDouble(6, videojuego.getPrecioRenta());
            ps.setDouble(7, videojuego.getPrecioCompra());
            ps.setInt(8, videojuego.getStock());
            ps.setString(9, emptyToNull(videojuego.getImagenUrl()));
            ps.setInt(10, videojuego.getPuntos());

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error en agregar videojuego: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            ConexionBD.cerrar(conn);
        }
    }

    public static boolean actualizar(VideojuegoInfo videojuego) {
        Connection conn = null;

        try {
            conn = ConexionBD.conectar();

            int idNumerico = extraerIdNumerico(videojuego.getId());
            String sql = "UPDATE videojuegos " +
                         "SET nombre = ?, plataforma = ?, genero = ?, clasificacion = ?, " +
                         "    anio_lanzamiento = ?, precio_renta = ?, precio_compra = ?, " +
                         "    stock = ?, imagen = ?, puntos = ? " +
                         "WHERE id_videojuego = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, videojuego.getTitulo());
            ps.setString(2, emptyToNull(videojuego.getPlataforma()));
            ps.setString(3, emptyToNull(videojuego.getGenero()));
            ps.setString(4, videojuego.getClasificacion());
            setYear(ps, 5, videojuego.getAnioLanzamiento());
            ps.setDouble(6, videojuego.getPrecioRenta());
            ps.setDouble(7, videojuego.getPrecioCompra());
            ps.setInt(8, videojuego.getStock());
            ps.setString(9, emptyToNull(videojuego.getImagenUrl()));
            ps.setInt(10, videojuego.getPuntos());
            ps.setInt(11, idNumerico);

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error en actualizar videojuego: " + e.getMessage());
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

            int idNumerico = extraerIdNumerico(id);
            String sql = "UPDATE videojuegos " +
                         "SET activo = FALSE " +
                         "WHERE id_videojuego = ? AND activo = TRUE";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idNumerico);

            int filasAfectadas = ps.executeUpdate();
            ps.close();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error en eliminar videojuego: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            ConexionBD.cerrar(conn);
        }
    }

    private static VideojuegoInfo mapearVideojuego(ResultSet rs) throws Exception {
        return new VideojuegoInfo(
            "VID-" + String.format("%03d", rs.getInt("id_videojuego")),
            rs.getString("nombre"),
            rs.getString("plataforma"),
            rs.getString("genero"),
            rs.getString("clasificacion"),
            rs.getInt("anio_lanzamiento"),
            rs.getDouble("precio_renta"),
            rs.getDouble("precio_compra"),
            rs.getInt("puntos"),
            rs.getInt("stock"),
            rs.getString("imagen")
        );
    }

    private static int extraerIdNumerico(String id) {
        return Integer.parseInt(id.replace("VID-", "").replace("V", ""));
    }

    private static String emptyToNull(String valor) {
        return valor == null || valor.trim().isEmpty() ? null : valor.trim();
    }

    private static void setYear(PreparedStatement ps, int index, int anio) throws Exception {
        if (anio <= 0) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, anio);
        }
    }
}
