package frontend.src.model;

public class VideojuegoInfo {
    private final String id;
    private final String titulo;
    private final String plataforma;
    private final String genero;
    private final String clasificacion;
    private final double precioRenta;
    private final double precioCompra;
    private final int puntos;
    private final int stock;
    private final String imagenUrl;

    public VideojuegoInfo(
        String id,
        String titulo,
        String plataforma,
        String genero,
        String clasificacion,
        double precioRenta,
        double precioCompra,
        int puntos,
        int stock,
        String imagenUrl
    ) {
        this.id = id;
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.genero = genero;
        this.clasificacion = clasificacion;
        this.precioRenta = precioRenta;
        this.precioCompra = precioCompra;
        this.puntos = puntos;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public String getGenero() {
        return genero;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public double getPrecioRenta() {
        return precioRenta;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getStock() {
        return stock;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }
}
