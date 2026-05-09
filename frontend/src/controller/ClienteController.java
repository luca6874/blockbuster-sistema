package frontend.src.controller;

import frontend.src.dao.ClienteDAO;
import frontend.src.model.ClienteInfo;
import java.util.List;

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
    public static boolean eliminarCliente(String id) {
        try {
            if (id == null || id.isEmpty()) {
                System.err.println("ID de cliente inválido");
                return false;
            }
            
            boolean resultado = ClienteDAO.eliminar(id);
            
            if (resultado) {
                System.out.println("Cliente eliminado: " + id);
            } else {
                System.err.println("No se pudo eliminar el cliente");
            }
            
            return resultado;
            
        } catch (Exception e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Valida los datos de un cliente antes de guardar.
     * 
     * Verifica:
     * - Nombre no vacío
     * - Email no vacío
     * - Teléfono no vacío (opcional)
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
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            System.err.println("Error: Nombre es requerido");
            return false;
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

        System.out.println("Validación exitosa para: " + cliente.getNombre());
        return true;
    }
}
