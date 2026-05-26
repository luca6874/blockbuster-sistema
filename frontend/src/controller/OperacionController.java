package frontend.src.controller;

import frontend.src.dao.ClienteDAO;
import frontend.src.dao.OperacionDAO;
import frontend.src.dao.VideojuegoDAO;
import frontend.src.model.OperacionInfo;
import frontend.src.model.OperacionTicketInfo;
import frontend.src.model.VideojuegoInfo;
import frontend.src.service.TicketPDFGenerator;
import java.io.File;
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

    public static List<String[]> obtenerHistorialRentas() {
        return OperacionDAO.obtenerHistorialRentas();
    }

    public static List<String[]> obtenerHistorialCompras() {
        return OperacionDAO.obtenerHistorialCompras();
    }

    public static boolean marcarRentaComoDevuelta(String idOperacion) {
        try {
            int idNumerico = Integer.parseInt(idOperacion.replace("OP-", "").replace("-", "").trim());
            return OperacionDAO.marcarRentaComoDevuelta(idNumerico);
        } catch (Exception e) {
            System.err.println("Error al convertir ID de operacion: " + e.getMessage());
            return false;
        }
    }

    public static void generarTicketPDF(OperacionTicketInfo ticket, File destino) throws Exception {
        new TicketPDFGenerator().generar(ticket, destino);
    }

    public static int calcularPuntosGanados(double totalOperacion) {
        return OperacionDAO.calcularPuntosGanados(totalOperacion);
    }

    /**
     * Guarda una nueva operación en la base de datos.
     *
     * Pasos:
     * 1. Valida que el videojuego tenga stock disponible
     * 2. Valida las fechas según el tipo de operación
     * 3. Inserta la operación en BD
     * 4. Actualiza el stock del videojuego
     * 5. Suma puntos automaticos al cliente
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

        // Validación 5: Validaciones de fecha para RENTA
        if (tipo.equals("RENTA")) {
            // La fecha de devolución es obligatoria para rentas
            if (fechaDevolucion == null) {
                return "Error: La fecha de devolución es obligatoria para rentas.";
            }
            
            // La fecha de devolución debe ser posterior a la fecha de operación
            if (!fechaDevolucion.isAfter(fechaOperacion)) {
                return "Error: La fecha de devolución debe ser posterior a la fecha de renta.";
            }
        } else if (tipo.equals("COMPRA")) {
            // Para compras, la fecha de devolución debe ser null
            if (fechaDevolucion != null) {
                return "Error: Las compras no deben tener fecha de devolución.";
            }
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
