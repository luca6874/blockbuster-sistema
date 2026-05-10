package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.controller.VideojuegoController;
import frontend.src.model.VideojuegoInfo;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class DlgEdicionVideojuego extends JDialog {
    private final Ventana parent;
    private final VideojuegoInfo videojuegoOriginal;
    private final Runnable onConfirm;
    private JLabel lblImagen;
    private JTextField txtTitulo;
    private JTextField txtPlataforma;
    private JTextField txtAnio;
    private JComboBox<String> comboClasificacion;
    private JTextField txtGenero;
    private JTextField txtRenta;
    private JTextField txtVenta;
    private JTextField txtStock;
    private JTextField txtPuntos;
    private JTextField txtImagen;

    public DlgEdicionVideojuego(Ventana parent, VideojuegoInfo videojuego, Runnable onConfirm) {
        super(parent, "Edicion del videojuego", true);
        this.parent = parent;
        this.videojuegoOriginal = videojuego;
        this.onConfirm = onConfirm;
        this.setUndecorated(true);
        this.setSize(650, 540);
        this.setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Ventana.CARD_WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPanel banner = new JPanel(null);
        banner.setBounds(0, 0, 650, 60);
        banner.setBackground(new Color(250, 250, 250));

        JLabel lblHeader = new JLabel("Edicion del videojuego");
        lblHeader.setBounds(25, 10, 400, 40);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 26));
        lblHeader.setForeground(new Color(50, 50, 50));
        banner.add(lblHeader);

        JSeparator separator = new JSeparator();
        separator.setBounds(0, 59, 650, 2);
        banner.add(separator);
        mainPanel.add(banner);

        lblImagen = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblImagen.setBounds(20, 100, 200, 260);
        lblImagen.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        mainPanel.add(lblImagen);

        int colX = 240;
        int col1Y = 90;
        int col2Y = 150;
        int col3Y = 210;
        int col4Y = 270;
        int col5Y = 330;

        txtTitulo = crearCampo(mainPanel, "Titulo", colX, col1Y, 200);
        txtAnio = crearCampo(mainPanel, "Anio", colX + 220, col1Y, 80);

        JLabel lblClasif = new JLabel("Clasificacion");
        lblClasif.setBounds(colX + 320, col1Y, 90, 20);
        lblClasif.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblClasif);
        comboClasificacion = new JComboBox<>(new String[]{"E", "T", "M"});
        comboClasificacion.setBounds(colX + 320, col1Y + 20, 80, 30);
        comboClasificacion.setBackground(Color.WHITE);
        mainPanel.add(comboClasificacion);

        txtPlataforma = crearCampo(mainPanel, "Plataforma", colX, col2Y, 180);
        txtGenero = crearCampo(mainPanel, "Genero", colX + 200, col2Y, 240);
        txtRenta = crearCampo(mainPanel, "Precio renta", colX, col3Y, 120);
        txtVenta = crearCampo(mainPanel, "Precio venta", colX + 140, col3Y, 120);
        txtStock = crearCampo(mainPanel, "Stock", colX + 280, col3Y, 90);
        txtPuntos = crearCampo(mainPanel, "Puntos", colX, col4Y, 120);
        txtImagen = crearCampo(mainPanel, "Imagen/path", colX, col5Y, 300);

        cargarDatos(videojuego);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBounds(260, 470, 110, 35);
        btnConfirmar.setBackground(new Color(110, 75, 80));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 12));
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.addActionListener(e -> guardarCambios());
        mainPanel.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(380, 470, 110, 35);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setBorder(new LineBorder(new Color(110, 75, 80)));
        btnCancelar.setForeground(new Color(110, 75, 80));
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> {
            parent.setOscurecer(false);
            this.dispose();
        });
        mainPanel.add(btnCancelar);

        this.add(mainPanel);
    }

    private JTextField crearCampo(JPanel panel, String label, int x, int y, int w) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, w, 20);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(lbl);

        JTextField txt = new JTextField();
        txt.setBounds(x, y + 20, w, 30);
        txt.setFont(new Font("Arial", Font.PLAIN, 12));
        txt.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        panel.add(txt);
        return txt;
    }

    private void cargarDatos(VideojuegoInfo videojuego) {
        if (videojuego == null) {
            return;
        }

        txtTitulo.setText(videojuego.getTitulo());
        txtPlataforma.setText(videojuego.getPlataforma());
        txtGenero.setText(videojuego.getGenero());
        comboClasificacion.setSelectedItem(VideojuegoController.normalizarClasificacion(videojuego.getClasificacion()));
        txtAnio.setText(videojuego.getAnioLanzamiento() > 0 ? String.valueOf(videojuego.getAnioLanzamiento()) : "");
        txtRenta.setText(String.valueOf(videojuego.getPrecioRenta()));
        txtVenta.setText(String.valueOf(videojuego.getPrecioCompra()));
        txtStock.setText(String.valueOf(videojuego.getStock()));
        txtPuntos.setText(String.valueOf(videojuego.getPuntos()));
        txtImagen.setText(videojuego.getImagenUrl() != null ? videojuego.getImagenUrl() : "");
        cargarImagen(videojuego.getImagenUrl());
    }

    private void guardarCambios() {
        VideojuegoInfo videojuego = construirVideojuego();
        if (videojuego == null) {
            return;
        }

        boolean exito = VideojuegoController.actualizarVideojuego(videojuego);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Videojuego actualizado exitosamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
            parent.setOscurecer(false);
            this.dispose();
            if (onConfirm != null) {
                onConfirm.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el videojuego. Revisa los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private VideojuegoInfo construirVideojuego() {
        try {
            String titulo = txtTitulo.getText().trim();
            String clasificacion = VideojuegoController.normalizarClasificacion((String) comboClasificacion.getSelectedItem());
            int anio = parseEnteroOpcional(txtAnio.getText());
            double renta = parseDecimal(txtRenta.getText());
            double venta = parseDecimal(txtVenta.getText());
            int stock = parseEnteroRequerido(txtStock.getText());
            int puntos = parseEnteroOpcional(txtPuntos.getText());

            return new VideojuegoInfo(
                videojuegoOriginal.getId(),
                titulo,
                txtPlataforma.getText().trim(),
                txtGenero.getText().trim(),
                clasificacion,
                anio,
                renta,
                venta,
                puntos,
                stock,
                txtImagen.getText().trim()
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock, precios, puntos y anio deben tener formato numerico valido.", "Datos invalidos", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private int parseEnteroRequerido(String valor) {
        return Integer.parseInt(valor.trim());
    }

    private int parseEnteroOpcional(String valor) {
        return valor == null || valor.trim().isEmpty() ? 0 : Integer.parseInt(valor.trim());
    }

    private double parseDecimal(String valor) {
        return valor == null || valor.trim().isEmpty() ? 0.0 : Double.parseDouble(valor.trim().replace("$", ""));
    }

    private void cargarImagen(String nombreImagen) {
        if (nombreImagen == null || nombreImagen.trim().isEmpty()) {
            lblImagen.setIcon(null);
            lblImagen.setText("Sin imagen");
            return;
        }

        try {
            URL url = getClass().getResource("/frontend/src/images/" + nombreImagen);
            if (url != null) {
                Image img = new ImageIcon(url).getImage().getScaledInstance(200, 260, Image.SCALE_SMOOTH);
                lblImagen.setText("");
                lblImagen.setIcon(new ImageIcon(img));
            } else {
                lblImagen.setIcon(null);
                lblImagen.setText("Sin imagen");
            }
        } catch (Exception ex) {
            lblImagen.setIcon(null);
            lblImagen.setText("Sin imagen");
        }
    }
}
