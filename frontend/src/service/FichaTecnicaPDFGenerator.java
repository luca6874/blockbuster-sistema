package frontend.src.service;

import frontend.src.model.VideojuegoInfo;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class FichaTecnicaPDFGenerator {
    private static final DateTimeFormatter FECHA_GENERACION = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final int ANCHO = 612;
    private static final int ALTO = 792;

    public void generar(VideojuegoInfo videojuego, File destino) throws Exception {
        if (videojuego == null) {
            throw new IllegalArgumentException("No hay un videojuego seleccionado.");
        }
        if (destino == null) {
            throw new IllegalArgumentException("No se selecciono una ruta de destino.");
        }

        File archivo = asegurarExtensionPdf(destino);
        File carpeta = archivo.getParentFile();
        if (carpeta != null && !carpeta.exists()) {
            Files.createDirectories(carpeta.toPath());
        }

        byte[] paginaJpeg = crearImagenPagina(videojuego);
        Files.write(archivo.toPath(), crearPdfDesdeJpeg(paginaJpeg));
    }

    private byte[] crearImagenPagina(VideojuegoInfo videojuego) throws Exception {
        BufferedImage pagina = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = pagina.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ANCHO, ALTO);
        g.setColor(new Color(246, 240, 241));
        g.fillRect(0, 0, ANCHO, 86);
        g.setColor(new Color(110, 60, 70));
        g.fillRect(0, 84, ANCHO, 3);

        g.setColor(new Color(55, 55, 55));
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Ficha tecnica del videojuego", 42, 42);
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("Generado: " + LocalDateTime.now().format(FECHA_GENERACION), 42, 62);

        g.setColor(new Color(110, 60, 70));
        g.setFont(new Font("Arial", Font.BOLD, 18));
        dibujarTextoAjustado(g, valor(videojuego.getTitulo()), 42, 128, 332);

        dibujarCaratula(g, videojuego.getImagenUrl(), 410, 116, 142, 202);

        int y = 174;
        y = dibujarFila(g, y, "ID del videojuego", valor(videojuego.getId()));
        y = dibujarFila(g, y, "Nombre", valor(videojuego.getTitulo()));
        y = dibujarFila(g, y, "Plataforma", valor(videojuego.getPlataforma()));
        y = dibujarFila(g, y, "Género", valor(videojuego.getGenero()));
        y = dibujarFila(g, y, "Clasificacion", valor(videojuego.getClasificacion()));
        y = dibujarFila(g, y, "Año de lanzamiento", videojuego.getAnioLanzamiento() > 0 ? String.valueOf(videojuego.getAnioLanzamiento()) : "-");
        y = dibujarFila(g, y, "Precio renta", formatoMoneda(videojuego.getPrecioRenta()));
        y = dibujarFila(g, y, "Precio compra", formatoMoneda(videojuego.getPrecioCompra()));
        y = dibujarFila(g, y, "Stock actual", String.valueOf(videojuego.getStock()));
        y = dibujarFila(g, y, "Puntos", String.valueOf(videojuego.getPuntos()));
        dibujarFila(g, y, "Estado", videojuego.isActivo() ? "Activo" : "Inactivo");

        g.setColor(new Color(235, 235, 235));
        g.fillRect(42, 694, 528, 1);
        g.setColor(new Color(100, 100, 100));
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        g.drawString("Documento generado desde el modulo de Videojuegos.", 42, 720);
        g.dispose();

        return convertirAJpeg(pagina);
    }

    private int dibujarFila(Graphics2D g, int y, String etiqueta, String valor) {
        g.setColor(new Color(70, 70, 70));
        g.setFont(new Font("Arial", Font.BOLD, 11));
        g.drawString(etiqueta + ":", 54, y);

        g.setColor(new Color(35, 35, 35));
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        int lineas = dibujarTextoAjustado(g, valor(valor), 210, y, 330);

        g.setColor(new Color(230, 230, 230));
        g.drawLine(54, y + 10 + ((lineas - 1) * 14), 558, y + 10 + ((lineas - 1) * 14));
        return y + 28 + ((lineas - 1) * 14);
    }

    private int dibujarTextoAjustado(Graphics2D g, String texto, int x, int y, int anchoMaximo) {
        FontMetrics metrics = g.getFontMetrics();
        List<String> lineas = partir(texto, metrics, anchoMaximo);
        int yLinea = y;
        for (String linea : lineas) {
            g.drawString(linea, x, yLinea);
            yLinea += 14;
        }
        return Math.max(1, lineas.size());
    }

    private void dibujarCaratula(Graphics2D g, String nombreImagen, int x, int y, int ancho, int alto) {
        g.setColor(new Color(245, 245, 245));
        g.fillRect(x, y, ancho, alto);
        g.setColor(new Color(210, 210, 210));
        g.setStroke(new BasicStroke(1f));
        g.drawRect(x, y, ancho, alto);

        BufferedImage imagen = cargarImagen(nombreImagen);
        if (imagen == null) {
            g.setColor(new Color(120, 120, 120));
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            centrarTexto(g, "Sin imagen", x, y + (alto / 2), ancho);
            return;
        }

        double escala = Math.min((double) ancho / imagen.getWidth(), (double) alto / imagen.getHeight());
        int anchoImg = Math.max(1, (int) (imagen.getWidth() * escala));
        int altoImg = Math.max(1, (int) (imagen.getHeight() * escala));
        int imgX = x + (ancho - anchoImg) / 2;
        int imgY = y + (alto - altoImg) / 2;
        g.drawImage(imagen.getScaledInstance(anchoImg, altoImg, Image.SCALE_SMOOTH), imgX, imgY, null);
    }

    private void centrarTexto(Graphics2D g, String texto, int x, int y, int ancho) {
        FontMetrics metrics = g.getFontMetrics();
        int textoX = x + (ancho - metrics.stringWidth(texto)) / 2;
        g.drawString(texto, textoX, y);
    }

    private BufferedImage cargarImagen(String nombreImagen) {
        if (nombreImagen == null || nombreImagen.trim().isEmpty()) {
            return null;
        }

        String nombre = nombreImagen.trim();
        
        // Intentar cargar desde classpath (recursos del JAR)
        try {
            String rutaRecurso = "/frontend/src/images/" + nombre;
            InputStream is = FichaTecnicaPDFGenerator.class.getResourceAsStream(rutaRecurso);
            if (is != null) {
                BufferedImage img = ImageIO.read(is);
                is.close();
                if (img != null) {
                    return img;
                }
            }
        } catch (Exception ignored) {
            // Silenciosamente fallar e intentar desde filesystem
        }
        
        // Fallback: intentar cargar desde filesystem (para desarrollo)
        try {
            File archivo = new File("frontend/src/images", nombre);
            if (archivo.exists()) {
                return ImageIO.read(archivo);
            }
        } catch (Exception ignored) {
            return null;
        }
        return null;
    }

    private byte[] convertirAJpeg(BufferedImage imagen) throws Exception {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.92f);
        }

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(salida)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(imagen, null, null), param);
        } finally {
            writer.dispose();
        }
        return salida.toByteArray();
    }

    private byte[] crearPdfDesdeJpeg(byte[] jpeg) throws Exception {
        List<byte[]> objetos = new ArrayList<>();
        objetos.add(ascii("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n"));
        objetos.add(ascii("2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n"));
        objetos.add(ascii("3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 " + ANCHO + " " + ALTO + "] /Resources << /XObject << /Im0 4 0 R >> >> /Contents 5 0 R >>\nendobj\n"));
        objetos.add(unir(
            ascii("4 0 obj\n<< /Type /XObject /Subtype /Image /Width " + ANCHO + " /Height " + ALTO + " /ColorSpace /DeviceRGB /BitsPerComponent 8 /Filter /DCTDecode /Length " + jpeg.length + " >>\nstream\n"),
            jpeg,
            ascii("\nendstream\nendobj\n")
        ));

        String contenido = "q\n" + ANCHO + " 0 0 " + ALTO + " 0 0 cm\n/Im0 Do\nQ\n";
        objetos.add(ascii("5 0 obj\n<< /Length " + contenido.getBytes(StandardCharsets.ISO_8859_1).length + " >>\nstream\n" + contenido + "endstream\nendobj\n"));

        ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        List<Integer> offsets = new ArrayList<>();
        pdf.write(ascii("%PDF-1.4\n"));
        for (byte[] objeto : objetos) {
            offsets.add(pdf.size());
            pdf.write(objeto);
        }

        int xref = pdf.size();
        pdf.write(ascii("xref\n"));
        pdf.write(ascii("0 " + (objetos.size() + 1) + "\n"));
        pdf.write(ascii("0000000000 65535 f \n"));
        for (Integer offset : offsets) {
            pdf.write(ascii(String.format("%010d 00000 n \n", offset)));
        }
        pdf.write(ascii("trailer\n"));
        pdf.write(ascii("<< /Size " + (objetos.size() + 1) + " /Root 1 0 R >>\n"));
        pdf.write(ascii("startxref\n" + xref + "\n%%EOF"));
        return pdf.toByteArray();
    }

    private List<String> partir(String texto, FontMetrics metrics, int anchoMaximo) {
        List<String> lineas = new ArrayList<>();
        String pendiente = texto == null || texto.trim().isEmpty() ? "-" : texto.trim();
        while (!pendiente.isEmpty()) {
            if (metrics.stringWidth(pendiente) <= anchoMaximo) {
                lineas.add(pendiente);
                break;
            }

            int corte = pendiente.length();
            while (corte > 1 && metrics.stringWidth(pendiente.substring(0, corte)) > anchoMaximo) {
                corte--;
            }
            int espacio = pendiente.lastIndexOf(' ', corte);
            if (espacio > 0) {
                corte = espacio;
            }
            lineas.add(pendiente.substring(0, corte).trim());
            pendiente = pendiente.substring(corte).trim();
        }
        return lineas;
    }

    private String valor(String valor) {
        return valor == null || valor.trim().isEmpty() ? "-" : valor.trim();
    }

    private String formatoMoneda(double valor) {
        return String.format("$%.2f", valor);
    }

    private File asegurarExtensionPdf(File archivo) {
        if (archivo.getName().toLowerCase().endsWith(".pdf")) {
            return archivo;
        }
        File carpeta = archivo.getParentFile();
        return carpeta == null ? new File(archivo.getName() + ".pdf") : new File(carpeta, archivo.getName() + ".pdf");
    }

    private byte[] ascii(String texto) {
        return texto.getBytes(StandardCharsets.ISO_8859_1);
    }

    private byte[] unir(byte[]... partes) throws Exception {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        for (byte[] parte : partes) {
            salida.write(parte);
        }
        return salida.toByteArray();
    }
}
