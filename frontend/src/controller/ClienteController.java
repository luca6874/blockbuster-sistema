package frontend.src.controller;

import frontend.src.dao.ClienteDAO;
import frontend.src.model.ClienteInfo;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * ClienteController - Controlador de clientes.
 * 
 * Coordina la lógica de negocio entre Vistas y DAOs.
 * NO contiene SQL, solo lógica y llamadas a DAO.
 * 
 * Responsabilidades:
 * - Validar datos de entrada
 * - Coordinar operaciones con ClienteDAO
 * - Procesar errores
 */
public class ClienteController {
    private static final int ANIO_MINIMO_FECHA_NACIMIENTO = 1900;
    
    // Patrón para validar nombres/apellidos: letras, espacios, acentos y ñ
    private static final Pattern PATRON_NOMBRE = Pattern.compile(
        "^[a-zA-ZáéíóúñüÁÉÍÓÚÑÜàèìòùÀÈÌÒÙäëïöÄËÏÖ ]+$"
    );

    /**
     * Obtiene la lista de todos los clientes desde la BD.
     * 
     * @return Lista de ClienteInfo, vacía si hay error
     */
    public static List<ClienteInfo> traerClientesDeBD() {
        try {
            List<ClienteInfo> clientes = ClienteDAO.obtenerTodos();
            
            if (clientes == null || clientes.isEmpty()) {
                System.out.println("No hay clientes en la BD");
                return clientes;
            }
            
            System.out.println("Clientes cargados: " + clientes.size());
            return clientes;
            
        } catch (Exception e) {
            System.err.println("Error al traer clientes: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    public static int sincronizarNivelesFidelidad() {
        try {
            return ClienteDAO.sincronizarNivelesFidelidad();
        } catch (Exception e) {
            System.err.println("Error al sincronizar niveles desde controller: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Obtiene un cliente específico por ID.
     * 
     * @param id el ID del cliente
     * @return ClienteInfo si existe, null si no
     */
    public static ClienteInfo obtenerClientePorId(String id) {
        try {
            if (id == null || id.isEmpty()) {
                System.err.println("ID de cliente inválido");
                return null;
            }
            
            ClienteInfo cliente = ClienteDAO.obtenerPorId(id);
            return cliente;
            
        } catch (Exception e) {
            System.err.println("Error al obtener cliente: " + e.getMessage());
            return null;
        }
    }

    /**
     * Guarda un nuevo cliente en la BD.
     * 
     * Valida datos antes de guardar.
     * 
     * @param cliente el ClienteInfo a guardar
     * @return true si fue exitoso, false si hubo error
     */
    public static boolean agregarCliente(ClienteInfo cliente) {
        try {
            // Validar datos
            if (!validarDatos(cliente)) {
                return false;
            }
            
            // Guardar en BD
            boolean resultado = ClienteDAO.agregar(cliente);
            
            if (resultado) {
                System.out.println("Cliente agregado: " + cliente.getNombre());
            } else {
                System.err.println("No se pudo agregar el cliente");
            }
            
            return resultado;
            
        } catch (Exception e) {
            System.err.println("Error al agregar cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza un cliente existente en la BD.
     * 
     * Valida datos antes de actualizar.
     * 
     * @param cliente el ClienteInfo con datos nuevos
     * @return true si fue exitoso, false si hubo error
     */
    public static boolean actualizarCliente(ClienteInfo cliente) {
        try {
            // Validar que exista ID
            if (cliente == null || cliente.getId() == null || cliente.getId().isEmpty()) {
                System.err.println("No se puede actualizar sin ID");
                return false;
            }
            
            // Validar datos
            if (!validarDatos(cliente)) {
                return false;
            }
            
            // Actualizar en BD
            boolean resultado = ClienteDAO.actualizar(cliente);
            
            if (resultado) {
                System.out.println("Cliente actualizado: " + cliente.getNombre());
            } else {
                System.err.println("No se pudo actualizar el cliente");
            }
            
            return resultado;
            
        } catch (Exception e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un cliente de la BD.
     * 
     * @param id el ID del cliente a eliminar
     * @return true si fue exitoso, false si hubo error
     */
    public static String eliminarCliente(String id) {
        try {
            if (id == null || id.isEmpty()) {
                System.err.println("ID de cliente inválido");
                return "ERROR";
            }
            
            boolean resultado = ClienteDAO.eliminar(id);
            
            if (resultado) {
                System.out.println("Cliente eliminado: " + id);
            } else {
                System.err.println("No se pudo eliminar el cliente");
            }
            
            return "OK";
            
        } catch (Exception e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
            return "ERROR";
        }
    }

    /**
     * Valida los datos de un cliente antes de guardar.
     * 
     * Verifica:
     * - Nombre no vacío
     * - Nombres/apellidos solo contienen letras, acentos y ñ
     * - Email no vacío
     * - Teléfono no vacío (opcional)
     * - Fecha no futura
     * 
     * @param cliente el ClienteInfo a validar
     * @return true si los datos son válidos, false si no
     */
    public static boolean validarDatos(ClienteInfo cliente) {
        if (cliente == null) {
            System.err.println("Error: Cliente nulo");
            return false;
        }

        // Validar nombre
        if (cliente.getNombres() == null || cliente.getNombres().trim().isEmpty()) {
            System.err.println("Error: Nombre es requerido");
            return false;
        }

        // NUEVA VALIDACIÓN: Nombre solo letras/acentos
        if (!validarNombreYApellidos(cliente.getNombres())) {
            System.err.println("Error: Nombre contiene caracteres inválidos");
            return false;
        }

        if (cliente.getPrimerApellido() == null || cliente.getPrimerApellido().trim().isEmpty()) {
            System.err.println("Error: Primer apellido es requerido");
            return false;
        }

        // NUEVA VALIDACIÓN: Primer apellido solo letras/acentos
        if (!validarNombreYApellidos(cliente.getPrimerApellido())) {
            System.err.println("Error: Primer apellido contiene caracteres inválidos");
            return false;
        }

        // NUEVA VALIDACIÓN: Segundo apellido solo letras/acentos (si está presente)
        if (cliente.getSegundoApellido() != null && !cliente.getSegundoApellido().trim().isEmpty()) {
            if (!validarNombreYApellidos(cliente.getSegundoApellido())) {
                System.err.println("Error: Segundo apellido contiene caracteres inválidos");
                return false;
            }
        }

        // Validar email
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            System.err.println("Error: Email es requerido");
            return false;
        }

        // Validar formato básico de email
        if (!cliente.getEmail().contains("@")) {
            System.err.println("Error: Email inválido");
            return false;
        }

        if (cliente.getTelefono() != null && !cliente.getTelefono().trim().isEmpty()
                && !cliente.getTelefono().matches("\\d{10}")) {
            System.err.println("Error: Teléfono debe tener 10 dígitos");
            return false;
        }

        // NUEVA VALIDACIÓN: Fecha no puede ser futura
        if (cliente.getFechaNacimiento() != null && !cliente.getFechaNacimiento().trim().isEmpty()) {
            if (!fechaNacimientoValida(cliente.getFechaNacimiento())) {
                System.err.println("Error: Fecha de nacimiento invalida");
                return false;
            }
            if (fechaNacimientoEsAnteriorA1900(cliente.getFechaNacimiento())) {
                System.err.println("Error: La fecha no puede ser anterior a 1900");
                return false;
            }
            if (fechaNacimientoEsFutura(cliente.getFechaNacimiento())) {
                System.err.println("Error: Fecha de nacimiento no puede ser futura");
                return false;
            }
        }

        System.out.println("Validación exitosa para: " + cliente.getNombre());
        return true;
    }

    private static boolean fechaNacimientoValida(String fecha) {
        return parseFechaNacimiento(fecha) != null;
    }

    private static LocalDate parseFechaNacimiento(String fecha) {
        if (fecha == null) {
            return null;
        }
        String valor = fecha.trim();
        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException ignored) {
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(valor, formatter);
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    /**
     * Valida que un nombre/apellido solo contenga letras, espacios, acentos y ñ.
     * No permite números, símbolos ni caracteres especiales.
     * 
     * @param texto el texto a validar
     * @return true si es válido, false si contiene caracteres inválidos
     */
    private static boolean validarNombreYApellidos(String texto) {
        if (texto == null || texto.isEmpty()) {
            return false;
        }
        return PATRON_NOMBRE.matcher(texto).matches();
    }

    /**
     * Verifica si una fecha de nacimiento es futura.
     * Maneja múltiples formatos (YYYY-MM-DD y dd/MM/yyyy).
     * 
     * @param fecha la fecha a validar
     * @return true si la fecha es futura, false si es pasada o actual
     */
    private static boolean fechaNacimientoEsFutura(String fecha) {
        LocalDate fechaParsed = parseFechaNacimiento(fecha);
        return fechaParsed != null && fechaParsed.isAfter(LocalDate.now());
    }

    private static boolean fechaNacimientoEsAnteriorA1900(String fecha) {
        LocalDate fechaParsed = parseFechaNacimiento(fecha);
        return fechaParsed != null && fechaParsed.getYear() < ANIO_MINIMO_FECHA_NACIMIENTO;
    }
}
