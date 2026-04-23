package frontend.src.mainpackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Diálogo para agregar un nuevo videojuego.
 */
public class DlgAgregarVideojuego extends JDialog {
    private JLabel lblImagen;
    private JTextField txtTitulo;
    private JTextField txtAnio;
    private JTextField txtClasificacion;
    private JTextField txtGenero;
    private JTextField txtRenta;
    private JTextField txtVenta;
    private JTextField txtStock;
    private JCheckBox chkSoloRenta;
    private JCheckBox chkSoloVenta;

    public DlgAgregarVideojuego(Ventana parent, Runnable onConfirm) {
        super(parent, "Agregar título", true);
        this.setUndecorated(true);
        this.setSize(893, 497);
        this.setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Ventana.CARD_WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // --- IMAGEN A LA IZQUIERDA ---
        lblImagen = new JLabel();
        lblImagen.setBounds(20, 20, 200, 260);
        lblImagen.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        lblImagen.setText("Sin imagen");
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblImagen);

        // --- CAMPOS A LA DERECHA ---
        int colX = 240;
        int col1Y = 20;
        int col2Y = 80;
        int col3Y = 140;
        int col4Y = 200;
        int col5Y = 260;

        JLabel lblTitulo = new JLabel("Título");
        lblTitulo.setBounds(colX, col1Y, 60, 20);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblTitulo);
        txtTitulo = new JTextField();
        txtTitulo.setBounds(colX, col1Y + 20, 200, 30);
        txtTitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        txtTitulo.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtTitulo);

        JLabel lblAnio = new JLabel("Año");
        lblAnio.setBounds(colX + 220, col1Y, 60, 20);
        lblAnio.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblAnio);
        txtAnio = new JTextField();
        txtAnio.setBounds(colX + 220, col1Y + 20, 80, 30);
        txtAnio.setFont(new Font("Arial", Font.PLAIN, 12));
        txtAnio.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtAnio);

        JLabel lblClasif = new JLabel("Clasificación");
        lblClasif.setBounds(colX + 320, col1Y, 80, 20);
        lblClasif.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblClasif);
        txtClasificacion = new JTextField();
        txtClasificacion.setBounds(colX + 320, col1Y + 20, 80, 30);
        txtClasificacion.setFont(new Font("Arial", Font.PLAIN, 12));
        txtClasificacion.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtClasificacion);

        // --- GÉNERO ---
        JLabel lblGenero = new JLabel("Género");
        lblGenero.setBounds(colX, col2Y, 60, 20);
        lblGenero.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblGenero);
        txtGenero = new JTextField();
        txtGenero.setBounds(colX, col2Y + 20, 480, 30);
        txtGenero.setFont(new Font("Arial", Font.PLAIN, 12));
        txtGenero.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtGenero);

        // --- PRECIOS Y STOCK ---
        JLabel lblRenta = new JLabel("Precio(Renta)");
        lblRenta.setBounds(colX, col3Y, 100, 20);
        lblRenta.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblRenta);
        txtRenta = new JTextField();
        txtRenta.setBounds(colX, col3Y + 20, 100, 30);
        txtRenta.setFont(new Font("Arial", Font.PLAIN, 12));
        txtRenta.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtRenta);

        JLabel lblVenta = new JLabel("Precio(Venta)");
        lblVenta.setBounds(colX + 140, col3Y, 100, 20);
        lblVenta.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblVenta);
        txtVenta = new JTextField();
        txtVenta.setBounds(colX + 140, col3Y + 20, 100, 30);
        txtVenta.setFont(new Font("Arial", Font.PLAIN, 12));
        txtVenta.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtVenta);

        JLabel lblStock = new JLabel("Stock");
        lblStock.setBounds(colX + 280, col3Y, 60, 20);
        lblStock.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblStock);
        txtStock = new JTextField();
        txtStock.setBounds(colX + 280, col3Y + 20, 80, 30);
        txtStock.setFont(new Font("Arial", Font.PLAIN, 12));
        txtStock.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtStock);

        // --- CHECKBOXES ---
        chkSoloRenta = new JCheckBox("Solo Renta");
        chkSoloRenta.setBounds(colX, col4Y + 10, 120, 25);
        chkSoloRenta.setBackground(Ventana.CARD_WHITE);
        chkSoloRenta.setFont(new Font("Arial", Font.PLAIN, 11));
        mainPanel.add(chkSoloRenta);

        chkSoloVenta = new JCheckBox("Solo Venta");
        chkSoloVenta.setBounds(colX + 140, col4Y + 10, 120, 25);
        chkSoloVenta.setBackground(Ventana.CARD_WHITE);
        chkSoloVenta.setFont(new Font("Arial", Font.PLAIN, 11));
        mainPanel.add(chkSoloVenta);

        // --- BOTONES ---
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(colX, col5Y + 20, 100, 35);
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 12));
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorderPainted(false);
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> {
            parent.setOscurecer(false);
            this.dispose();
            if (onConfirm != null) onConfirm.run();
        });
        mainPanel.add(btnAgregar);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBounds(colX + 120, col5Y + 20, 100, 35);
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
        mainPanel.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(colX + 240, col5Y + 20, 100, 35);
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
        mainPanel.add(btnCancelar);

        this.add(mainPanel);
    }

    public String[] obtenerDatos() {
        return new String[]{
            txtTitulo.getText(),
            txtGenero.getText(),
            txtClasificacion.getText(),
            txtRenta.getText(),
            txtVenta.getText(),
            txtStock.getText(),
            "", // imagen (vacía por defecto)
            "", // plataforma (vacía por defecto)
            txtAnio.getText(),
            (chkSoloRenta.isSelected() ? "Solo Renta" : (chkSoloVenta.isSelected() ? "Solo Venta" : "Venta/Renta"))
        };
    }
}
