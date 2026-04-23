package frontend.src.mainpackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.net.URL;

/**
 * Diálogo de confirmación para eliminar un videojuego.
 */
public class DlgConfirmarEliminacionVideojuego extends JDialog {
    public DlgConfirmarEliminacionVideojuego(Ventana parent, String[] datosVideojuego, Runnable onConfirm) {
        super(parent, "Confirmar eliminación", true);
        this.setUndecorated(true);
        this.setSize(690, 411);
        this.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);
        panel.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        JLabel lblTitulo = new JLabel("Confirmar eliminación");
        lblTitulo.setBounds(24, 16, 300, 30);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(61, 0, 0));
        panel.add(lblTitulo);

        JLabel lblImagen = new JLabel();
        lblImagen.setBounds(24, 62, 250, 250);
        lblImagen.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        panel.add(lblImagen);

        if (datosVideojuego != null) {
            try {
                URL url = getClass().getResource("/frontend/src/images/" + datosVideojuego[6]);
                if (url != null) {
                    Image img = new ImageIcon(url).getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                    lblImagen.setIcon(new ImageIcon(img));
                }
            } catch (Exception ignored) {}
        }

        JPanel detalle = new JPanel(null);
        detalle.setBackground(Ventana.CARD_WHITE);
        detalle.setBounds(290, 62, 370, 300);
        detalle.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        panel.add(detalle);

        JLabel lblNombre = new JLabel(datosVideojuego != null ? datosVideojuego[0] : "");
        lblNombre.setBounds(16, 16, 340, 24);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        detalle.add(lblNombre);

        JLabel lblPlataforma = new JLabel(datosVideojuego != null ? datosVideojuego[7] : "");
        lblPlataforma.setBounds(16, 42, 340, 18);
        lblPlataforma.setFont(new Font("Arial", Font.PLAIN, 12));
        detalle.add(lblPlataforma);

        JLabel lblGenero = new JLabel(datosVideojuego != null ? "Género: " + datosVideojuego[1] : "");
        lblGenero.setBounds(16, 70, 340, 18);
        lblGenero.setFont(new Font("Arial", Font.PLAIN, 12));
        detalle.add(lblGenero);

        JLabel lblClasificacion = new JLabel(datosVideojuego != null ? "Clasificación: " + datosVideojuego[2] : "");
        lblClasificacion.setBounds(16, 92, 340, 18);
        lblClasificacion.setFont(new Font("Arial", Font.PLAIN, 12));
        detalle.add(lblClasificacion);

        JLabel lblAnio = new JLabel(datosVideojuego != null ? "Año: " + datosVideojuego[8] : "");
        lblAnio.setBounds(16, 114, 340, 18);
        lblAnio.setFont(new Font("Arial", Font.PLAIN, 12));
        detalle.add(lblAnio);

        JLabel lblModo = new JLabel(datosVideojuego != null ? "Modo: " + datosVideojuego[9] : "");
        lblModo.setBounds(16, 136, 340, 18);
        lblModo.setFont(new Font("Arial", Font.PLAIN, 12));
        detalle.add(lblModo);

        JLabel lblPrecios = new JLabel(datosVideojuego != null ? datosVideojuego[3] + "   " + datosVideojuego[4] : "");
        lblPrecios.setBounds(16, 158, 340, 18);
        lblPrecios.setFont(new Font("Arial", Font.BOLD, 12));
        detalle.add(lblPrecios);

        JLabel lblStock = new JLabel("Stock: 10/25");
        lblStock.setBounds(16, 186, 340, 18);
        lblStock.setFont(new Font("Arial", Font.BOLD, 12));
        detalle.add(lblStock);

        JButton btnConfirmar = new JButton("Confirmar eliminación");
        btnConfirmar.setBounds(24, 336, 210, 40);
        btnConfirmar.setBackground(new Color(152, 33, 54));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 12));
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.addActionListener(e -> {
            parent.setOscurecer(false);
            this.dispose();
            if (onConfirm != null) onConfirm.run();
        });
        panel.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(250, 336, 100, 40);
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> {
            parent.setOscurecer(false);
            this.dispose();
        });
        panel.add(btnCancelar);

        this.add(panel);
    }
}
