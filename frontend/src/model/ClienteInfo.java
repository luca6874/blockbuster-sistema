package frontend.src.model;

/**
 * Modelo MVC de Cliente.
 * Representa un cliente en la base de datos.
 * Compatible 100% con código existente (lectura).
 * Ahora soporta modificaciones también.
 */
public class ClienteInfo {
    private String id;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String email;
    private String estatus;
    private String nivel;
    private boolean frecuente;
    private String telefono;
    private String fechaNacimiento;

    // Constructor vacío (para DAO)
    public ClienteInfo() {
    }

    // Constructor completo (mantiene compatibilidad)
    public ClienteInfo(String id, String nombre, String email, String estatus, String nivel, boolean frecuente, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.estatus = estatus;
        this.nivel = nivel;
        this.frecuente = frecuente;
        this.telefono = telefono;
    }

    // GETTERS (mantiene exactamente lo que tenía antes)
    
    public String getId() {
        return id;
    }

    public String getNombre() {
        StringBuilder nombreCompleto = new StringBuilder(nombre != null ? nombre : "");
        if (primerApellido != null && !primerApellido.trim().isEmpty()) {
            nombreCompleto.append(" ").append(primerApellido.trim());
        }
        if (segundoApellido != null && !segundoApellido.trim().isEmpty()) {
            nombreCompleto.append(" ").append(segundoApellido.trim());
        }
        return nombreCompleto.toString().trim();
    }

    public String getNombres() {
        return nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
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

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    // SETTERS (nuevos, no rompen nada)
    
    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public void setFrecuente(boolean frecuente) {
        this.frecuente = frecuente;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
