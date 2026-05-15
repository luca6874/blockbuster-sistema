package frontend.src.model;

import java.time.LocalDate;

/**
 * Modelo MVC de Usuario.
 * Representa un usuario de la tabla 'usuarios' en la base de datos.
 * 
 * Los usuarios son admins/empleados que pueden hacer login.
 * NO son clientes (que solo aparecen en operaciones de renta/compra).
 */
public class UsuarioInfo {
    private int idUsuario;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String username;
    private String correo;
    private LocalDate fechaNacimiento;
    private String password;

    // Constructor vacío (para DAO)
    public UsuarioInfo() {
    }

    // Constructor completo
    public UsuarioInfo(int idUsuario, String username, String correo, String password) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.correo = correo;
        this.password = password;
    }

    public UsuarioInfo(int idUsuario, String nombre, String primerApellido, String segundoApellido,
                       String username, String correo, LocalDate fechaNacimiento, String password) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.username = username;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.password = password;
    }

    // GETTERS
    
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public String getCorreo() {
        return correo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getPassword() {
        return password;
    }

    public String getNombreCompletoVisible() {
        StringBuilder nombreCompleto = new StringBuilder();
        agregarParteNombre(nombreCompleto, nombre);
        agregarParteNombre(nombreCompleto, primerApellido);
        agregarParteNombre(nombreCompleto, segundoApellido);

        if (nombreCompleto.length() > 0) {
            return nombreCompleto.toString();
        }

        return username != null && !username.trim().isEmpty() ? username : "Sin usuario";
    }

    // SETTERS
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void agregarParteNombre(StringBuilder nombreCompleto, String parte) {
        if (parte != null && !parte.trim().isEmpty()) {
            if (nombreCompleto.length() > 0) {
                nombreCompleto.append(" ");
            }
            nombreCompleto.append(parte.trim());
        }
    }

    @Override
    public String toString() {
        return "UsuarioInfo{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", primerApellido='" + primerApellido + '\'' +
                ", segundoApellido='" + segundoApellido + '\'' +
                ", username='" + username + '\'' +
                ", correo='" + correo + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                '}';
    }
}
