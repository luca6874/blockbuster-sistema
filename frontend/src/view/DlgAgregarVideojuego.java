package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.controller.VideojuegoController;
import frontend.src.model.VideojuegoInfo;
import frontend.src.service.ImageManager;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class DlgAgregarVideojuego extends JDialog {
    private final Ventana parent;
    private final Runnable onConfirm;
    private JTextField txtTitulo;
    private JComboBox<String> comboPlataforma;
    private JTextField txtAnio;
    private JComboBox<String> comboClasificacion;
    private JComboBox<String> comboGenero;
    private JTextField txtRenta;
    private JTextField txtVenta;
    private JTextField txtStock;
    private String imagenActual;
    private JLabel lblImagen;

    public DlgAgregarVideojuego(Ventana parent, Runnable onConfirm) {
        super(parent, "Agregar titulo", true);
        this.parent = parent;
        this.onConfirm = onConfirm;
        this.setUndecorated(true);
        this.setSize(650, 520);
        this.setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Ventana.CARD_WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPanel banner = new JPanel(null);
        banner.setBounds(0, 0, 650, 60);
        banner.setBackground(new Color(250, 250, 250));

        JLabel lblHeader = new JLabel("Agregar titulo");
        lblHeader.setBounds(25, 10, 300, 40);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 26));
        lblHeader.setForeground(new Color(50, 50, 50));
        banner.add(lblHeader);

        JSeparator separator = new JSeparator();
        separator.setBounds(0, 59, 650, 2);
        banner.add(separator);
        mainPanel.add(banner);

        lblImagen = new JLabel("Sin imagen");
        lblImagen.setBounds(20, 80, 200, 260);
        lblImagen.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblImagen);

        JButton btnImagen = new JButton("Seleccionar imagen");
        btnImagen.setBounds(40, 360, 160, 35);
        btnImagen.setBackground(new Color(110, 75, 80));
        btnImagen.setForeground(Color.WHITE);
        btnImagen.setFocusPainted(false);
        btnImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnImagen.addActionListener(e -> seleccionarNuevaImagen());

        mainPanel.add(btnImagen);

                
        int colX = 240;
        int col1Y = 80;
        int col2Y = 140;
        int col3Y = 200;
       

        txtTitulo = crearCampo(mainPanel, "Titulo", colX, col1Y, 200);
        txtAnio = crearCampo(mainPanel, "Año", colX + 220, col1Y, 80);

        JLabel lblClasif = new JLabel("Clasificacion");
        lblClasif.setBounds(colX + 320, col1Y, 90, 20);
        lblClasif.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblClasif);
        comboClasificacion = new JComboBox<>(new String[]{"E", "T", "M"});
        comboClasificacion.setBounds(colX + 320, col1Y + 20, 80, 30);
        comboClasificacion.setBackground(Color.WHITE);
        mainPanel.add(comboClasificacion);

        comboPlataforma = crearComboPlataforma(mainPanel, "Plataforma", colX, col2Y, 180);
        comboGenero = crearComboGenero(mainPanel, "Genero", colX + 200, col2Y, 240);
        txtRenta = crearCampo(mainPanel, "Precio renta", colX, col3Y, 120);
        txtVenta = crearCampo(mainPanel, "Precio venta", colX + 140, col3Y, 120);
        txtStock = crearCampo(mainPanel, "Stock", colX + 280, col3Y, 90);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBounds(260, 455, 110, 35);
        btnConfirmar.setBackground(new Color(110, 75, 80));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 12));
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.addActionListener(e -> guardarVideojuego());
        mainPanel.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(380, 455, 110, 35);
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

    private JComboBox<String> crearComboPlataforma(JPanel panel, String label, int x, int y, int w) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, w, 20);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(lbl);

        JComboBox<String> combo = new JComboBox<>(new String[]{
            "Xbox 360",
            "Xbox One",
            "Xbox Series X/S",
            "PS3",
            "PS4",
            "PS5",
            "Nintendo Switch",
            "PC"
        });
        combo.setBounds(x, y + 20, w, 30);
        combo.setBackground(Color.WHITE);
        combo.setFont(new Font("Arial", Font.PLAIN, 12));
        combo.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        panel.add(combo);
        return combo;
    }

    private JComboBox<String> crearComboGenero(JPanel panel, String label, int x, int y, int w) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, w, 20);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(lbl);

        JComboBox<String> combo = new JComboBox<>(VideojuegoController.GENEROS_VIDEOJUEGO);
        combo.setBounds(x, y + 20, w, 30);
        combo.setBackground(Color.WHITE);
        combo.setFont(new Font("Arial", Font.PLAIN, 12));
        combo.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        panel.add(combo);
        return combo;
    }

    private void guardarVideojuego() {
        VideojuegoInfo videojuego = construirVideojuego(null);
        if (videojuego == null) {
            return;
        }

        boolean exito = VideojuegoController.agregarVideojuego(videojuego);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Videojuego agregado exitosamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
            parent.setOscurecer(false);
            this.dispose();
            if (onConfirm != null) {
                System.out.println("REFRESCANDO TABLA...");
                onConfirm.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar el videojuego. Revisa los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

        private void seleccionarNuevaImagen() {

        File archivoSeleccionado = ImageManager.seleccionarImagen(this);

        if (archivoSeleccionado == null) {
            return;
        }

        if (!ImageManager.validarImagen(archivoSeleccionado)) {

            JOptionPane.showMessageDialog(
                this,
                "El archivo debe ser una imagen válida",
                "Archivo inválido",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String nombreGuardado = ImageManager.guardarImagen(archivoSeleccionado);

        if (nombreGuardado == null) {

            JOptionPane.showMessageDialog(
                this,
                "Error al guardar la imagen",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        imagenActual = nombreGuardado;

        cargarImagen(nombreGuardado);
    }

    private void cargarImagen(String nombreImagen) {

        ImageIcon icono = ImageManager.cargarImagenPreview(
            nombreImagen,
            200,
            260
        );

        if (icono != null) {

            lblImagen.setText("");
            lblImagen.setIcon(icono);

        } else {

            lblImagen.setText("Sin imagen");
            lblImagen.setIcon(null);
        }
    }

    private VideojuegoInfo construirVideojuego(String id) {
        try {
            String titulo = txtTitulo.getText().trim();
            String clasificacion = VideojuegoController.normalizarClasificacion((String) comboClasificacion.getSelectedItem());
            int anio = parseEnteroOpcional(txtAnio.getText());
            double renta = parseDecimal(txtRenta.getText());
            double venta = parseDecimal(txtVenta.getText());
            int stock = parseEnteroRequerido(txtStock.getText());

            return new VideojuegoInfo(
                id,
                titulo,
                (String) comboPlataforma.getSelectedItem(),
                obtenerGeneroSeleccionado(),
                clasificacion,
                anio,
                renta,
                venta,
                stock,
                imagenActual
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock, precios y año deben tener formato numerico valido.", "Datos invalidos", JOptionPane.WARNING_MESSAGE);
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

    private String obtenerGeneroSeleccionado() {
        Object seleccionado = comboGenero.getSelectedItem();
        return seleccionado != null ? seleccionado.toString().trim() : "";
    }
}
