package frontend.src.controller;

import frontend.src.dao.VideojuegoDAO;
import frontend.src.model.VideojuegoInfo;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoController {
    public static final String[] GENEROS_VIDEOJUEGO = {
        "Acción",
        "Aventura",
        "RPG",
        "FPS",
        "Terror",
        "Deportes",
        "Peleas",
        "Estrategia",
        "Simulación",
        "Plataforma",
        "Survival",
        "Mundo Abierto",
        "Carreras",
        "Otro"
    };

    public static List<VideojuegoInfo> traerVideojuegosDeBD() {
        try {
            List<VideojuegoInfo> videojuegos = VideojuegoDAO.obtenerTodos();
            if (videojuegos == null) {
                return new ArrayList<>();
            }

            System.out.println("Videojuegos cargados: " + videojuegos.size());
            return videojuegos;
        } catch (Exception e) {
            System.err.println("Error al traer videojuegos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static VideojuegoInfo obtenerVideojuegoPorId(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                System.err.println("ID de videojuego invalido");
                return null;
            }

            return VideojuegoDAO.obtenerPorId(id);
        } catch (Exception e) {
            System.err.println("Error al obtener videojuego: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean agregarVideojuego(VideojuegoInfo videojuego) {
        try {
            if (!validarDatos(videojuego)) {
                return false;
            }

            boolean resultado = VideojuegoDAO.agregar(videojuego);
            if (resultado) {
                System.out.println("Videojuego agregado: " + videojuego.getTitulo());
            }
            return resultado;
        } catch (Exception e) {
            System.err.println("Error al agregar videojuego: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarVideojuego(VideojuegoInfo videojuego) {
        try {
            if (videojuego == null || videojuego.getId() == null || videojuego.getId().trim().isEmpty()) {
                System.err.println("No se puede actualizar videojuego sin ID");
                return false;
            }

            if (!validarDatos(videojuego)) {
                return false;
            }

            boolean resultado = VideojuegoDAO.actualizar(videojuego);
            if (resultado) {
                System.out.println("Videojuego actualizado: " + videojuego.getTitulo());
            }
            return resultado;
        } catch (Exception e) {
            System.err.println("Error al actualizar videojuego: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarVideojuego(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                System.err.println("ID de videojuego invalido");
                return false;
            }

            boolean resultado = VideojuegoDAO.eliminar(id);
            if (resultado) {
                System.out.println("Videojuego eliminado: " + id);
            }
            return resultado;
        } catch (Exception e) {
            System.err.println("Error al eliminar videojuego: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validarDatos(VideojuegoInfo videojuego) {
        if (videojuego == null) {
            System.err.println("Error: Videojuego nulo");
            return false;
        }

        if (videojuego.getTitulo() == null || videojuego.getTitulo().trim().isEmpty()) {
            System.err.println("Error: Titulo de videojuego requerido");
            return false;
        }

        if (!clasificacionValida(videojuego.getClasificacion())) {
            System.err.println("Error: Clasificacion invalida");
            return false;
        }

        if (videojuego.getStock() < 0) {
            System.err.println("Error: Stock debe ser numerico y no negativo");
            return false;
        }

        if (videojuego.getPrecioRenta() < 0 || videojuego.getPrecioCompra() < 0) {
            System.err.println("Error: Precios deben ser decimales no negativos");
            return false;
        }

        int anio = videojuego.getAnioLanzamiento();
        int anioMaximo = Year.now().getValue() + 1;
        if (anio != 0 && (anio < 1970 || anio > anioMaximo)) {
            System.err.println("Error: Anio de lanzamiento invalido");
            return false;
        }

        return true;
    }

    public static String normalizarClasificacion(String clasificacion) {
        if (clasificacion == null) {
            return "";
        }

        String valor = clasificacion.trim().toUpperCase();
        if (valor.startsWith("E")) {
            return "E";
        }
        if (valor.startsWith("T")) {
            return "T";
        }
        if (valor.startsWith("M")) {
            return "M";
        }
        return valor;
    }

    private static boolean clasificacionValida(String clasificacion) {
        String valor = normalizarClasificacion(clasificacion);
        return "E".equals(valor) || "T".equals(valor) || "M".equals(valor);
    }
}
