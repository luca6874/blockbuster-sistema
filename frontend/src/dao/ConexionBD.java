package frontend.src.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * ConexionBD - Conexión a la base de datos MySQL.
 * 
 * 
 * Credenciales:
 * - Host: localhost
 * - Puerto: 3306
 * - Base datos: blockbuster
 * - Usuario: root
 * - Contraseña: Kenseigod_7 <-- modifiquen esa contraseña con la que tienen en el workbench pa q les jale
 */
public class ConexionBD {
    
    private static final String URL = "jdbc:mysql://localhost:3306/blockbuster";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "Kenseigod_7";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Obtiene una conexión a la base de datos.
     * 
     * @return Connection activa a la BD
     * @throws Exception si hay error de conexión
     */
    public static Connection conectar() throws Exception {
        try {
            // Cargar driver JDBC
            Class.forName(DRIVER);
            
            // Retornar conexión
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (Exception e) {
            System.err.println("Error al conectar a la BD: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Cierra una conexión a la base de datos.
     * 
     * @param conexion la conexión a cerrar
     */
    public static void cerrar(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (Exception e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
