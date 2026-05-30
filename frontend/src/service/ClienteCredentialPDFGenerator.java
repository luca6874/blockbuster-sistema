package frontend.src.service;
import frontend.src.model.ClienteInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.geom.Ellipse2D;

public class ClienteCredentialPDFGenerator {
    
    private static final int ANCHO = 540;
    private static final int ALTO = 300;

    public void generar(ClienteInfo cliente, File destino) throws Exception {

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente invalido");
        }

        if (destino == null) {
            throw new IllegalArgumentException("Destino invalido");
        }

        byte[] imagen = crearCredencial(cliente);

        Files.write(destino.toPath(), crearPdfDesdeJpeg(imagen));
    }

    private byte[] crearCredencial(ClienteInfo cliente) throws Exception {

        BufferedImage card = new BufferedImage(
            ANCHO,
            ALTO,
            BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g = card.createGraphics();

        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        // Fondo
        g.setColor(new Color(28, 28, 35));
        g.fillRoundRect(0, 0, ANCHO, ALTO, 30, 30);

        // Barra superior
        g.setColor(new Color(152, 33, 54));
        g.fillRoundRect(0, 0, ANCHO, 70, 30, 30);

        // Titulo
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.drawString("BLOCKBUSTER MEMBER", 25, 45);

     
        // Fondo foto
        g.setColor(new Color(60, 60, 70));

        g.fillRoundRect(
            35,
            90,
            120,
            120,
            20,
            20
        );
        

        // Foto
        dibujarFotoCliente(g, cliente, 40, 95, 110);

        //nombre
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));

        g.drawString(
            cliente.getNombre(),
            190,
            130
        );

        //id
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        g.drawString(
            "ID: " + cliente.getId(),
            190,
            165
        );

        //Nivel
        g.drawString(
            "Nivel: " + cliente.getNivel(),
            190,
            195
        );

        //Telefono
        g.drawString(
            "Tel: " + cliente.getTelefono(),
            190,
            225
        );

        g.dispose();

        return convertirAJpeg(card);
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

   private void dibujarFotoCliente(Graphics2D g, ClienteInfo cliente, int x, int y, int size) {

            try {

                if (cliente.getFoto() != null && !cliente.getFoto().trim().isEmpty()) {

                    File archivoImagen = new File(
                        "frontend/src/images",
                        cliente.getFoto()
                    );

                    BufferedImage imagen = ImageIO.read(archivoImagen);

                    if (imagen != null) {

                        // Clip circular
                        Ellipse2D.Double clip = new Ellipse2D.Double(x, y, size, size);

                        g.setClip(clip);

                        g.drawImage(
                            imagen,
                            x,
                            y,
                            size,
                            size,
                            null
                        );

                        g.setClip(null);

                        // borde
                        g.setColor(new Color(110, 60, 70));
                        g.drawOval(x, y, size, size);

                        return;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Placeholder
            g.setColor(new Color(220, 220, 220));
            g.fillOval(x, y, size, size);

            g.setColor(new Color(120, 120, 120));
            g.setFont(new Font("Arial", Font.BOLD, 42));
            g.drawString("U", x + (size / 2) - 14, y + (size / 2) + 16);
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
