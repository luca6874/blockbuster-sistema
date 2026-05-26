package frontend.src.model;

public class OperacionTicketInfo {
    private final String idOperacion;
    private final String tipo;
    private final String cliente;
    private final String videojuego;
    private final String plataforma;
    private final String fechaOperacion;
    private final String fechaDevolucion;
    private final String monto;
    private final String descuento;
    private final String estado;

    public OperacionTicketInfo(
        String idOperacion,
        String tipo,
        String cliente,
        String videojuego,
        String plataforma,
        String fechaOperacion,
        String fechaDevolucion,
        String monto,
        String descuento,
        String estado
    ) {
        this.idOperacion = limpiar(idOperacion);
        this.tipo = limpiar(tipo);
        this.cliente = limpiar(cliente);
        this.videojuego = limpiar(videojuego);
        this.plataforma = limpiar(plataforma);
        this.fechaOperacion = limpiar(fechaOperacion);
        this.fechaDevolucion = limpiar(fechaDevolucion);
        this.monto = limpiar(monto);
        this.descuento = limpiar(descuento);
        this.estado = limpiar(estado);
    }

    public String getIdOperacion() {
        return idOperacion;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCliente() {
        return cliente;
    }

    public String getVideojuego() {
        return videojuego;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public String getFechaOperacion() {
        return fechaOperacion;
    }

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public String getMonto() {
        return monto;
    }

    public String getDescuento() {
        return descuento;
    }

    public String getEstado() {
        return estado;
    }

    public boolean esRenta() {
        return "RENTA".equalsIgnoreCase(tipo);
    }

    private static String limpiar(String valor) {
        return valor == null || valor.trim().isEmpty() ? "N/A" : valor.trim();
    }
}
