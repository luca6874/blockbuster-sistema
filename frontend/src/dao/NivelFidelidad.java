package frontend.src.dao;

/**
 * Clase centralizada para gestionar la lógica de niveles de fidelidad.
 * 
 * Define las reglas de cálculo automático de niveles según puntos acumulados.
 * Todas las referencias a nombres de nivel deben pasar por aquí.
 * 
 * Niveles internos (Java): 1-4
 * - 1: Bronce (0-499 puntos)
 * - 2: Plata (500-1499 puntos)
 * - 3: Oro (1500-2999 puntos)
 * - 4: Diamante (3000+ puntos)
 * 
 * Niveles BD (compatibilidad): 0-2
 * - 0: Bronce
 * - 1: Plata
 * - 2: Oro
 * (La BD no tiene Diamante todavía)
 */
public class NivelFidelidad {
    
    // Constantes de límites de puntos
    private static final int PUNTOS_PLATA = 500;
    private static final int PUNTOS_ORO = 1500;
    private static final int PUNTOS_DIAMANTE = 3000;
    
    // Constantes de niveles
    public static final int NIVEL_BRONCE = 1;
    public static final int NIVEL_PLATA = 2;
    public static final int NIVEL_ORO = 3;
    public static final int NIVEL_DIAMANTE = 4;

    /**
     * Calcula automáticamente el nivel de fidelidad según los puntos acumulados.
     * 
     * @param puntos puntos totales acumulados del cliente
     * @return nivel de fidelidad (1-4)
     */
    public static int calcularNivelFidelidad(int puntos) {
        if (puntos >= PUNTOS_DIAMANTE) {
            return NIVEL_DIAMANTE;
        } else if (puntos >= PUNTOS_ORO) {
            return NIVEL_ORO;
        } else if (puntos >= PUNTOS_PLATA) {
            return NIVEL_PLATA;
        } else {
            return NIVEL_BRONCE;
        }
    }

    /**
     * Obtiene el nombre del nivel de fidelidad.
     * 
     * @param nivel nivel de fidelidad (1-4)
     * @return nombre del nivel (Bronce, Plata, Oro, Diamante)
     */
    public static String obtenerNombreNivel(int nivel) {
        switch (nivel) {
            case NIVEL_BRONCE:
                return "Bronce";
            case NIVEL_PLATA:
                return "Plata";
            case NIVEL_ORO:
                return "Oro";
            case NIVEL_DIAMANTE:
                return "Diamante";
            default:
                return "Bronce";
        }
    }

    /**
     * Obtiene el rango de puntos para un nivel específico.
     * Útil para mostrar información al usuario.
     * 
     * @param nivel nivel de fidelidad (1-4)
     * @return string con el rango de puntos del nivel
     */
    public static String obtenerRangoPuntos(int nivel) {
        switch (nivel) {
            case NIVEL_BRONCE:
                return "0 - 499 puntos";
            case NIVEL_PLATA:
                return "500 - 1499 puntos";
            case NIVEL_ORO:
                return "1500 - 2999 puntos";
            case NIVEL_DIAMANTE:
                return "3000+ puntos";
            default:
                return "0 - 499 puntos";
        }
    }
    
    /**
     * Obtiene el color asociado a un nivel para UI.
     * Formato RGB como array: [R, G, B]
     * 
     * @param nivel nivel de fidelidad (1-4)
     * @return array [R, G, B] del color del nivel
     */
    public static int[] obtenerColorNivel(int nivel) {
        switch (nivel) {
            case NIVEL_BRONCE:
                return new int[]{205, 127, 50};    // Bronce
            case NIVEL_PLATA:
                return new int[]{192, 192, 192};   // Plata
            case NIVEL_ORO:
                return new int[]{255, 215, 0};     // Oro
            case NIVEL_DIAMANTE:
                return new int[]{64, 224, 208};    // Diamante (turquesa)
            default:
                return new int[]{205, 127, 50};    // Default: Bronce
        }
    }

    // ============= MÉTODOS DE COMPATIBILIDAD CON BD (0-2) =============

    /**
     * Convierte nivel BD (0-2) a nivel interno (1-4).
     * IMPORTANTE: Usa nombre del nivel para la conversión, no el número directo.
     * 
     * @param lvlBD nivel de BD (0-2)
     * @return nivel interno (1-4)
     */
    public static int convertirDesdeBD(int lvlBD) {
        String nombre = convertirNivelBDaNombre(lvlBD);
        return convertirNombreANivel(nombre);
    }

    /**
     * Convierte nivel interno (1-4) a nivel BD (0-2).
     * 
     * @param nivelInterno nivel interno (1-4)
     * @return nivel BD (0-2), o 0 si es Diamante (no existe en BD aún)
     */
    public static int convertirABD(int nivelInterno) {
        String nombre = obtenerNombreNivel(nivelInterno);
        return convertirNombreANivelBD(nombre);
    }

    /**
     * Convierte nivel BD (número) a nombre.
     */
    private static String convertirNivelBDaNombre(int lvlBD) {
        switch (lvlBD) {
            case 0:
                return "Bronce";
            case 1:
                return "Plata";
            case 2:
                return "Oro";
            default:
                return "Bronce";
        }
    }

    /**
     * Convierte nombre a nivel BD.
     */
    private static int convertirNombreANivelBD(String nombre) {
        if (nombre == null) {
            return 0;
        }
        
        switch (nombre) {
            case "Bronce":
                return 0;
            case "Plata":
                return 1;
            case "Oro":
                return 2;
            case "Diamante":
                return 2; // Diamante mapea a Oro por compatibilidad
            default:
                return 0;
        }
    }

    /**
     * Convierte nombre a nivel interno.
     */
    private static int convertirNombreANivel(String nombre) {
        if (nombre == null) {
            return NIVEL_BRONCE;
        }
        
        switch (nombre) {
            case "Bronce":
                return NIVEL_BRONCE;
            case "Plata":
                return NIVEL_PLATA;
            case "Oro":
                return NIVEL_ORO;
            case "Diamante":
                return NIVEL_DIAMANTE;
            default:
                return NIVEL_BRONCE;
        }
    }
}
