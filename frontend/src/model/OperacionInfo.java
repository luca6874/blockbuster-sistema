package frontend.src.model;

import java.time.LocalDate;

/**
 * Modelo MVC para operaciones de renta y compra.
 * Representa una operación en la base de datos.
 */
public class OperacionInfo {
    private int idOperacion;
    private int idCliente;
    private int idVideojuego;
    private int idUsuario;
    private String tipo; // "RENTA" o "COMPRA"
    private double monto;
    private double descuento;
    private LocalDate fechaOperacion;
    private LocalDate fechaDevolucion;
    private boolean devuelto; // true si la renta fue devuelta

    // Constructor vacío (para DAO)
    public OperacionInfo() {
    }

    // Constructor completo
    public OperacionInfo(
        int idOperacion,
        int idCliente,
        int idVideojuego,
        int idUsuario,
        String tipo,
        double monto,
        double descuento,
        LocalDate fechaOperacion,
        LocalDate fechaDevolucion
    ) {
        this.idOperacion = idOperacion;
        this.idCliente = idCliente;
        this.idVideojuego = idVideojuego;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.monto = monto;
        this.descuento = descuento;
        this.fechaOperacion = fechaOperacion;
        this.fechaDevolucion = fechaDevolucion;
    }

    // Constructor para insertar (sin ID, lo genera la BD)
    public OperacionInfo(
        int idCliente,
        int idVideojuego,
        int idUsuario,
        String tipo,
        double monto,
        double descuento,
        LocalDate fechaOperacion,
        LocalDate fechaDevolucion
    ) {
        this.idCliente = idCliente;
        this.idVideojuego = idVideojuego;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.monto = monto;
        this.descuento = descuento;
        this.fechaOperacion = fechaOperacion;
        this.fechaDevolucion = fechaDevolucion;
    }

    // GETTERS
    public int getIdOperacion() {
        return idOperacion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdVideojuego() {
        return idVideojuego;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public double getDescuento() {
        return descuento;
    }

    public LocalDate getFechaOperacion() {
        return fechaOperacion;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    // SETTERS
    public void setIdOperacion(int idOperacion) {
        this.idOperacion = idOperacion;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdVideojuego(int idVideojuego) {
        this.idVideojuego = idVideojuego;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public void setFechaOperacion(LocalDate fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }
}
