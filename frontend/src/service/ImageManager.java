package frontend.src.service;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

/**
 * Gestor centralizado de imágenes para videojuegos y clientes.
 * Maneja carga, validación y almacenamiento de archivos de imagen.
 */
public class ImageManager {

    // Ruta donde se guardan las imágenes (relativa al proyecto)
    private static final String IMG_DIRECTORY = "frontend/src/images";
    
    // Formatos permitidos
    private static final String[] ALLOWED_EXTENSIONS = {"png", "jpg", "jpeg"};
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    
    // Mantiene registro de archivos usados para evitar sobrescrituras accidentales
    private static final Set<String> usedFilenames = new HashSet<>();

    /**
     * Abre un diálogo de selección de archivo de imagen.
     * @param parent componente padre para centrar el diálogo
     * @return archivo seleccionado o null si se cancela
     */
    public static File seleccionarImagen(Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar imagen");
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        
        // Filtro para archivos de imagen
        javax.swing.filechooser.FileNameExtensionFilter filtro = 
            new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes (PNG, JPG, JPEG)", 
                ALLOWED_EXTENSIONS
            );
        chooser.setFileFilter(filtro);
        
        int resultado = chooser.showOpenDialog(parent);
        return resultado == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;
    }

    /**
     * Valida que el archivo sea una imagen permitida.
     * @param archivo archivo a validar
     * @return true si es válido, false en caso contrario
     */
    public static boolean validarImagen(File archivo) {
        if (archivo == null || !archivo.exists()) {
            return false;
        }

        // Validar extensión
        String nombreArchivo = archivo.getName().toLowerCase();
        boolean extensionValida = false;
        for (String ext : ALLOWED_EXTENSIONS) {
            if (nombreArchivo.endsWith("." + ext)) {
                extensionValida = true;
                break;
            }
        }

        if (!extensionValida) {
            return false;
        }

        // Validar tamaño
        if (archivo.length() > MAX_FILE_SIZE) {
            return false;
        }

        // Validar que sea una imagen real
        try {
            BufferedImage img = ImageIO.read(archivo);
            return img != null && img.getWidth() > 0 && img.getHeight() > 0;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Copia una imagen a la carpeta de recursos del proyecto.
     * Genera un nombre único para evitar conflictos.
     * @param imagenOriginal archivo de imagen a copiar
     * @return nombre del archivo guardado, o null si hay error
     */
    public static String guardarImagen(File imagenOriginal) {
        if (!validarImagen(imagenOriginal)) {
            return null;
        }

        try {
            File directorioImagenes = new File(IMG_DIRECTORY);
            if (!directorioImagenes.exists()) {
                directorioImagenes.mkdirs();
            }

            // Generar nombre único
            String nombreUnico = generarNombreUnico(imagenOriginal.getName());
            File archivoDestino = new File(directorioImagenes, nombreUnico);

            // Copiar archivo
            Files.copy(
                imagenOriginal.toPath(),
                archivoDestino.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            );

            usedFilenames.add(nombreUnico);
            return nombreUnico;

        } catch (IOException e) {
            System.err.println("Error al guardar imagen: " + e.getMessage());
            return null;
        }
    }

    /**
     * Genera un nombre único para evitar conflictos de archivos.
     * @param nombreOriginal nombre del archivo original
     * @return nombre único generado
     */
    private static String generarNombreUnico(String nombreOriginal) {
        String extension = extraerExtension(nombreOriginal);
        String nombreBase = nombreOriginal.substring(0, nombreOriginal.lastIndexOf('.'))
            .replaceAll("[^a-zA-Z0-9._-]", "_");

        String nombreFinal = nombreBase + "." + extension;
        int contador = 1;

        File directorioImagenes = new File(IMG_DIRECTORY);
        while (new File(directorioImagenes, nombreFinal).exists() || 
               usedFilenames.contains(nombreFinal)) {
            nombreFinal = nombreBase + "_" + contador + "." + extension;
            contador++;
        }

        return nombreFinal;
    }

    /**
     * Extrae la extensión de un archivo.
     * @param nombreArchivo nombre del archivo
     * @return extensión sin el punto
     */
    private static String extraerExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        if (ultimoPunto > 0 && ultimoPunto < nombreArchivo.length() - 1) {
            return nombreArchivo.substring(ultimoPunto + 1).toLowerCase();
        }
        return "png";
    }

    /**
     * Carga una imagen para mostrar preview.
     * @param nombreImagen nombre del archivo de imagen
     * @param anchoMax ancho máximo para escalar
     * @param altoMax alto máximo para escalar
     * @return ImageIcon escalado, o null si no se puede cargar
     */
    public static ImageIcon cargarImagenPreview(String nombreImagen, int anchoMax, int altoMax) {
        if (nombreImagen == null || nombreImagen.trim().isEmpty()) {
            return null;
        }

        try {
            File archivo = new File(IMG_DIRECTORY, nombreImagen);
            if (!archivo.exists()) {
                return null;
            }

            BufferedImage img = ImageIO.read(archivo);
            if (img == null) {
                return null;
            }

            // Escalar manteniendo proporción
            int ancho = img.getWidth();
            int alto = img.getHeight();
            
            double escalaAncho = (double) anchoMax / ancho;
            double escalaAlto = (double) altoMax / alto;
            double escala = Math.min(escalaAncho, escalaAlto);
            
            int nuevoAncho = (int) (ancho * escala);
            int nuevoAlto = (int) (alto * escala);
            
            Image imgEscalada = img.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
            return new ImageIcon(imgEscalada);

        } catch (IOException e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina una imagen del almacenamiento.
     * @param nombreImagen nombre del archivo a eliminar
     * @return true si se eliminó correctamente
     */
    public static boolean eliminarImagen(String nombreImagen) {
        if (nombreImagen == null || nombreImagen.trim().isEmpty()) {
            return false;
        }

        try {
            Path directorioImagenes = new File(IMG_DIRECTORY).toPath().toAbsolutePath().normalize();
            Path archivo = directorioImagenes.resolve(nombreImagen).normalize();

            if (!archivo.startsWith(directorioImagenes)) {
                return false;
            }

            boolean eliminado = Files.deleteIfExists(archivo);
            if (eliminado) {
                usedFilenames.remove(nombreImagen);
            }
            return eliminado;
        } catch (Exception e) {
            System.err.println("Error al eliminar imagen: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si una imagen existe en el almacenamiento.
     * @param nombreImagen nombre del archivo a verificar
     * @return true si existe, false en caso contrario
     */
    public static boolean existeImagen(String nombreImagen) {
        if (nombreImagen == null || nombreImagen.trim().isEmpty()) {
            return false;
        }

        File archivo = new File(IMG_DIRECTORY, nombreImagen);
        return archivo.exists() && archivo.isFile();
    }
}
