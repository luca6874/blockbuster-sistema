package frontend.src.view;

import frontend.src.controller.Ventana;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.LineBorder;


public class DlgConfirmarEliminacionVideojuego extends JDialog {
    public DlgConfirmarEliminacionVideojuego(Ventana parent, String[] datosVideojuego, Runnable onConfirm) {
        super(parent, "Confirmar eliminación", true);
        this.setUndecorated(true);
        this.setSize(750, 450);
        this.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);
        panel.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        JLabel lblTitulo = new JLabel("Confirmar eliminación");
        lblTitulo.setBounds(30, 20, 400, 40);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(61, 0, 0));
        panel.add(lblTitulo);

        JLabel lblImagen = new JLabel();
        lblImagen.setBounds(60, 80, 240, 280);
        lblImagen.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        panel.add(lblImagen);

        if (datosVideojuego != null) {
            try {
                URL url = getClass().getResource("/frontend/src/images/" + datosVideojuego[6]);
                if (url != null) {
                    Image img = new ImageIcon(url).getImage().getScaledInstance(240, 280, Image.SCALE_SMOOTH);
                    lblImagen.setIcon(new ImageIcon(img));
                }
            } catch (Exception ignored) {}
        }

        JPanel detalle = new JPanel(null);
        detalle.setBackground(Ventana.CARD_WHITE);
        detalle.setBounds(330, 80, 360, 280);
        detalle.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        panel.add(detalle);

        JLabel lblNombre = new JLabel(datosVideojuego != null ? datosVideojuego[0] : "");
        lblNombre.setBounds(30, 16, 300, 24);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        detalle.add(lblNombre);

        JLabel lblPlataforma = new JLabel(datosVideojuego != null ? datosVideojuego[7] : "");
        lblPlataforma.setBounds(30, 42, 300, 18);
        lblPlataforma.setFont(new Font("Arial", Font.PLAIN, 12));
        detalle.add(lblPlataforma);

        JLabel lblGenero = new JLabel(datosVideojuego != null ? "Género: " + datosVideojuego[1] : "");
        lblGenero.setBounds(30, 70, 300, 18);
        lblGenero.setFont(new Font("Arial", Font.PLAIN, 12));
        detalle.add(lblGenero);

        JLabel lblClasificacion = new JLabel(datosVideojuego != null ? "Clasificación: " + datosVideojuego[2] : "");
        lblClasificacion.setBounds(30, 92, 300, 18);
        lblClasificacion.setFont(new Font("Arial", Font.PLAIN, 12));
        detalle.add(lblClasificacion);

        JLabel lblAnio = new JLabel(datosVideojuego != null ? "Año: " + datosVideojuego[8] : "");
        lblAnio.setBounds(30, 114, 300, 18);
        lblAnio.setFont(new Font("Arial", Font.PLAIN, 12));
        detalle.add(lblAnio);

        JLabel lblModo = new JLabel(datosVideojuego != null ? "Modo: " + datosVideojuego[9] : "");
        lblModo.setBounds(30, 136, 300, 18);
        lblModo.setFont(new Font("Arial", Font.PLAIN, 12));
        detalle.add(lblModo);

        JLabel lblPrecios = new JLabel(datosVideojuego != null ? datosVideojuego[3] + "   " + datosVideojuego[4] : "");
        lblPrecios.setBounds(30, 158, 300, 18);
        lblPrecios.setFont(new Font("Arial", Font.BOLD, 12));
        detalle.add(lblPrecios);

        JLabel lblStock = new JLabel("Stock: 10/25");
        lblStock.setBounds(30, 186, 300, 18);
        lblStock.setFont(new Font("Arial", Font.BOLD, 12));
        detalle.add(lblStock);

        JButton btnConfirmar = new JButton("Confirmar eliminación");
        btnConfirmar.setBounds(60, 385, 210, 40);
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
        btnCancelar.setBounds(280, 385, 120, 40);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setBorder(new LineBorder(new Color(152, 33, 54), 1));
        btnCancelar.setForeground(new Color(152, 33, 54));
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> {
            parent.setOscurecer(false);
            this.dispose();
        });
        panel.add(btnCancelar);

        this.add(panel);
    }
}