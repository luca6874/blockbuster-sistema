package frontend.src.service;

import frontend.src.model.OperacionTicketInfo;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TicketPDFGenerator {
    private static final DateTimeFormatter FECHA_GENERACION = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void generar(OperacionTicketInfo ticket, File destino) throws Exception {
        if (ticket == null) {
            throw new IllegalArgumentException("No hay datos de operación para generar el ticket.");
        }
        if (destino == null) {
            throw new IllegalArgumentException("No se selecciono una ruta de destino.");
        }

        File archivo = asegurarExtensionPdf(destino);
        File carpeta = archivo.getParentFile();
        if (carpeta != null && !carpeta.exists()) {
            Files.createDirectories(carpeta.toPath());
        }

        Files.write(archivo.toPath(), crearPdf(ticket));
    }

    private byte[] crearPdf(OperacionTicketInfo ticket) {
        String contenido = crearContenidoPagina(ticket);
        List<String> objetos = new ArrayList<>();
        objetos.add("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");
        objetos.add("2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");
        objetos.add("3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 420 640] /Resources << /Font << /F1 4 0 R /F2 5 0 R >> >> /Contents 6 0 R >>\nendobj\n");
        objetos.add("4 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n");
        objetos.add("5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold >>\nendobj\n");
        objetos.add("6 0 obj\n<< /Length " + contenido.getBytes(StandardCharsets.ISO_8859_1).length + " >>\nstream\n" + contenido + "\nendstream\nendobj\n");

        StringBuilder pdf = new StringBuilder();
        List<Integer> offsets = new ArrayList<>();
        pdf.append("%PDF-1.4\n");
        for (String objeto : objetos) {
            offsets.add(pdf.length());
            pdf.append(objeto);
        }

        int xref = pdf.length();
        pdf.append("xref\n");
        pdf.append("0 ").append(objetos.size() + 1).append("\n");
        pdf.append("0000000000 65535 f \n");
        for (Integer offset : offsets) {
            pdf.append(String.format("%010d 00000 n \n", offset));
        }
        pdf.append("trailer\n");
        pdf.append("<< /Size ").append(objetos.size() + 1).append(" /Root 1 0 R >>\n");
        pdf.append("startxref\n").append(xref).append("\n%%EOF");

        return pdf.toString().getBytes(StandardCharsets.ISO_8859_1);
    }

    private String crearContenidoPagina(OperacionTicketInfo ticket) {
        StringBuilder contenido = new StringBuilder();
        contenido.append("q\n");
        contenido.append("0.95 0.90 0.91 rg\n");
        contenido.append("0 590 420 50 re f\n");
        contenido.append("0.45 0.24 0.29 rg\n");
        contenido.append("0 588 420 2 re f\n");
        contenido.append("Q\n");

        texto(contenido, "F2", 18, 36, 606, "BRIARBUSTER");
        texto(contenido, "F1", 10, 36, 592, "Ticket / comprobante de operación");
        texto(contenido, "F1", 9, 282, 606, "Generado: " + LocalDateTime.now().format(FECHA_GENERACION));

        linea(contenido, 36, 558, 348);
        texto(contenido, "F2", 13, 36, 536, "Operación " + ticket.getIdOperacion());

        int y = 506;
        y = fila(contenido, y, "Tipo", ticket.getTipo());
        y = fila(contenido, y, "Cliente", ticket.getCliente());
        y = fila(contenido, y, "Videojuego", ticket.getVideojuego());
        y = fila(contenido, y, "Plataforma", ticket.getPlataforma());
        y = fila(contenido, y, "Fecha de operación", ticket.getFechaOperacion());
        if (ticket.esRenta()) {
            y = fila(contenido, y, "Fecha de devolución", ticket.getFechaDevolucion());
        }
        y = fila(contenido, y, "Monto", ticket.getMonto());
        y = fila(contenido, y, "Descuento", ticket.getDescuento());
        y = fila(contenido, y, "Estado actual", ticket.getEstado());

        linea(contenido, 36, y - 8, 348);
        texto(contenido, "F1", 9, 36, y - 34, "Este comprobante fue generado desde el modulo de Rentas y Compras.");
        texto(contenido, "F1", 9, 36, y - 48, "Conserve este ticket para cualquier aclaracion.");

        return contenido.toString();
    }

    private int fila(StringBuilder contenido, int y, String etiqueta, String valor) {
        texto(contenido, "F2", 10, 52, y, etiqueta + ":");
        for (String linea : partir(valor, 38)) {
            texto(contenido, "F1", 10, 176, y, linea);
            y -= 14;
        }
        return y - 6;
    }

    private List<String> partir(String texto, int maximo) {
        List<String> lineas = new ArrayList<>();
        String pendiente = texto == null ? "N/A" : texto.trim();
        if (pendiente.length() <= maximo) {
            lineas.add(pendiente);
            return lineas;
        }

        while (pendiente.length() > maximo) {
            int corte = pendiente.lastIndexOf(' ', maximo);
            if (corte <= 0) {
                corte = maximo;
            }
            lineas.add(pendiente.substring(0, corte).trim());
            pendiente = pendiente.substring(corte).trim();
        }
        if (!pendiente.isEmpty()) {
            lineas.add(pendiente);
        }
        return lineas;
    }

    private void texto(StringBuilder contenido, String fuente, int tamano, int x, int y, String texto) {
        contenido.append("BT /").append(fuente).append(" ").append(tamano).append(" Tf ")
                .append(x).append(" ").append(y).append(" Td (")
                .append(escapar(texto)).append(") Tj ET\n");
    }

    private void linea(StringBuilder contenido, int x, int y, int ancho) {
        contenido.append("q\n");
        contenido.append("0.70 0.70 0.70 RG\n");
        contenido.append(x).append(" ").append(y).append(" m ")
                .append(x + ancho).append(" ").append(y).append(" l S\n");
        contenido.append("Q\n");
    }

    private String escapar(String texto) {
        String limpio = texto == null ? "" : texto;
        limpio = limpio.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
        return limpiarCaracteresPdf(limpio);
    }

    private String limpiarCaracteresPdf(String texto) {
        return texto
                .replace("á", "a").replace("Á", "A")
                .replace("é", "e").replace("É", "E")
                .replace("í", "i").replace("Í", "I")
                .replace("ó", "o").replace("Ó", "O")
                .replace("ú", "u").replace("Ú", "U")
                .replace("ñ", "n").replace("Ñ", "N")
                .replace("¿", "").replace("¡", "");
    }

    private File asegurarExtensionPdf(File archivo) {
        if (archivo.getName().toLowerCase().endsWith(".pdf")) {
            return archivo;
        }
        return new File(archivo.getParentFile(), archivo.getName() + ".pdf");
    }
}
