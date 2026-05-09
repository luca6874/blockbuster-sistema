package frontend.src.model;

/**
 * Modelo MVC de Usuario.
 * Representa un usuario de la tabla 'usuarios' en la base de datos.
 * 
 * Los usuarios son admins/empleados que pueden hacer login.
 * NO son clientes (que solo aparecen en operaciones de renta/compra).
 */
public class UsuarioInfo {
    private int idUsuario;
    private String username;
    private String correo;
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

    // GETTERS
    
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }

    // SETTERS
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UsuarioInfo{" +
                "idUsuario=" + idUsuario +
                ", username='" + username + '\'' +
                ", correo='" + correo + '\'' +
                '}';
    }
}
