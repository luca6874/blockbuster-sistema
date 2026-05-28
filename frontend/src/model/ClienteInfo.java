package frontend.src.model;

/**
 * Modelo MVC de Cliente.
 * Representa un cliente en la base de datos.
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
    private int puntos;
    private int lvlFidelidad;  // Nivel de fidelidad (1-4)
    private String foto;  // Nombre del archivo de foto del perfil

    // Constructor vacío (para DAO)
    public ClienteInfo() {
    }

    // Constructor completo 
    public ClienteInfo(String id, String nombre, String email, String estatus, String nivel, boolean frecuente, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.estatus = estatus;
        this.nivel = nivel;
        this.frecuente = frecuente;
        this.telefono = telefono;
    }

    // GETTERS 
    
    public String getId() {
        return id;
    }

    /**
     * Obtiene el ID numérico del cliente desde el formato visual "CLI-XXX".
     * Ejemplo: "CLI-003" retorna 3
     * 
     * @return ID numérico, o -1 si no se puede parsear
     */
    public int getIdNumerico() {
        try {
            if (id != null && id.contains("-")) {
                // Formato: "CLI-003" -> extrae "003" -> retorna 3
                return Integer.parseInt(id.split("-")[1]);
            } else if (id != null) {
                // Si no tiene guión, intenta parsear directamente
                return Integer.parseInt(id);
            }
        } catch (Exception e) {
            System.err.println("Error al parsear ID numerico: " + e.getMessage());
        }
        return -1;
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

    public int getPuntos() {
        return puntos;
    }

    public int getLvlFidelidad() {
        return lvlFidelidad;
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

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setLvlFidelidad(int lvlFidelidad) {
        this.lvlFidelidad = lvlFidelidad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
