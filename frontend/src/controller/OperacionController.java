package frontend.src.controller;

import frontend.src.dao.ClienteDAO;
import frontend.src.dao.OperacionDAO;
import frontend.src.dao.VideojuegoDAO;
import frontend.src.model.OperacionInfo;
import frontend.src.model.VideojuegoInfo;
import java.time.LocalDate;
import java.util.List;

/**
 * OperacionController - Controlador para operaciones de renta y compra.
 *
 * Maneja la lógica de negocio relacionada con operaciones.
 * Coordina entre vistas y DAO.
 */
public class OperacionController {

    public static List<String[]> obtenerTodas() {
        return OperacionDAO.obtenerTodas();
    }

    public static int calcularPuntosGanados(double totalOperacion) {
        return OperacionDAO.calcularPuntosGanados(totalOperacion);
    }

    /**
     * Guarda una nueva operación en la base de datos.
     *
     * Pasos:
     * 1. Valida que el videojuego tenga stock disponible
     * 2. Inserta la operación en BD
     * 3. Actualiza el stock del videojuego
     * 4. Suma puntos automaticos al cliente
     *
     * @param idCliente ID del cliente
     * @param idVideojuego ID del videojuego
     * @param idUsuario ID del usuario (empleado logueado)
     * @param tipo "RENTA" o "COMPRA"
     * @param monto Monto de la operación
     * @param descuento Descuento aplicado
     * @param fechaOperacion Fecha de la operación (generalmente hoy)
     * @param fechaDevolucion Fecha de devolución (null para compras)
     * @return Mensaje de resultado (éxito o error)
     */
    public static String guardarOperacion(
        int idCliente,
        int idVideojuego,
        int idUsuario,
        String tipo,
        double monto,
        double descuento,
        LocalDate fechaOperacion,
        LocalDate fechaDevolucion
    ) {
        // Validación 1: Verificar que el videojuego existe
        VideojuegoInfo videojuego = VideojuegoDAO.obtenerPorId(String.valueOf(idVideojuego));
        if (videojuego == null) {
            return "Error: El videojuego no existe.";
        }

        // Validación 2: Verificar que hay stock disponible
        if (videojuego.getStock() <= 0) {
            return "Error: No hay stock disponible para este videojuego.";
        }

        // Validación 3: Verificar que tipo sea válido
        if (!tipo.equals("RENTA") && !tipo.equals("COMPRA")) {
            return "Error: Tipo de operación inválido. Debe ser RENTA o COMPRA.";
        }

        // Validación 4: Verificar que monto sea válido
        if (monto <= 0) {
            return "Error: El monto debe ser mayor a 0.";
        }

        // Crear objeto operación
        OperacionInfo operacion = new OperacionInfo(
            idCliente,
            idVideojuego,
            idUsuario,
            tipo,
            monto,
            descuento,
            fechaOperacion,
            fechaDevolucion
        );

        // Insertar en BD
        boolean resultado = OperacionDAO.insertar(operacion);

        if (resultado) {
            int puntosGanados = calcularPuntosGanados(monto - descuento);
            return "Exito: Operacion registrada correctamente. Stock actualizado. Puntos ganados: " + puntosGanados + ".";
        } else {
            return "Error: No se pudo registrar la operacion.";
        }
    }

    /**
     * Obtiene el stock actual de un videojuego.
     *
     * @param idVideojuego ID del videojuego
     * @return Stock disponible, o -1 si no existe
     */
    public static int obtenerStockVideojuego(int idVideojuego) {
        VideojuegoInfo videojuego = VideojuegoDAO.obtenerPorId(String.valueOf(idVideojuego));
        if (videojuego == null) {
            return -1;
        }
        return videojuego.getStock();
    }

    /**
     * Valida si se puede registrar una operación.
     *
     * @param idVideojuego ID del videojuego
     * @param monto Monto de la operación
     * @param tipo Tipo de operación (RENTA o COMPRA)
     * @return Mensaje de validación (vacío si es válido, error si no)
     */
    public static String validarOperacion(int idVideojuego, double monto, String tipo) {
        // Validar videojuego existe
        VideojuegoInfo videojuego = VideojuegoDAO.obtenerPorId(String.valueOf(idVideojuego));
        if (videojuego == null) {
            return "Error: El videojuego no existe.";
        }

        // Validar stock
        if (videojuego.getStock() <= 0) {
            return "Error: No hay stock disponible.";
        }

        // Validar tipo
        if (!tipo.equals("RENTA") && !tipo.equals("COMPRA")) {
            return "Error: Tipo de operación inválido.";
        }

        // Validar monto
        if (monto <= 0) {
            return "Error: El monto debe ser mayor a 0.";
        }

        return ""; // Válido


        
    }


    public static String eliminarCliente(String id) {
    try {
        boolean eliminado = ClienteDAO.eliminar(id);

        if (eliminado) {
            return "OK";
        }

        return "ERROR";

    } catch (RuntimeException e) {

        if ("CLIENTE_CON_OPERACIONES".equals(e.getMessage())) {
            return "OPERACIONES";
        }

        return "ERROR";
    }
}




}
