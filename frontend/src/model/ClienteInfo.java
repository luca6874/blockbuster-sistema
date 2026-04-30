package frontend.src.model;

public class ClienteInfo {
    private final String id;
    private final String nombre;
    private final String email;
    private final String estatus;
    private final String nivel;
    private final boolean frecuente;
    private final String telefono;

    public ClienteInfo(String id, String nombre, String email, String estatus, String nivel, boolean frecuente, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.estatus = estatus;
        this.nivel = nivel;
        this.frecuente = frecuente;
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getEstatus() {
        return estatus;
    }

    public String getNivel() {
        return nivel;
    }

    public boolean isFrecuente() {
        return frecuente;
    }

    public String getTelefono() {
        return telefono;
    }
}
