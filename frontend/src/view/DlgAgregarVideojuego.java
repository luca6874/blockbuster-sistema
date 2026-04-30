package frontend.src.view;

import frontend.src.controller.Ventana;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

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
        this.setSize(650, 480);
        this.setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Ventana.CARD_WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPanel banner = new JPanel(null);
        banner.setBounds(0, 0, 650, 60);
        banner.setBackground(new Color(250, 250, 250));
        
        JLabel lblHeader = new JLabel("Agregar título");
        lblHeader.setBounds(25, 10, 300, 40);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 26));
        lblHeader.setForeground(new Color(50, 50, 50));
        banner.add(lblHeader);
        
        JSeparator separator = new JSeparator();
        separator.setBounds(0, 59, 650, 2);
        banner.add(separator);
        mainPanel.add(banner);

        lblImagen = new JLabel();
        lblImagen.setBounds(20, 80, 200, 260);
        lblImagen.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        lblImagen.setText("Sin imagen");
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblImagen);

        int colX = 240;
        int col1Y = 80;
        int col2Y = 140;
        int col3Y = 200;
        int col4Y = 260;

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

        JLabel lblGenero = new JLabel("Género");
        lblGenero.setBounds(colX, col2Y, 60, 20);
        lblGenero.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblGenero);
        txtGenero = new JTextField();
        txtGenero.setBounds(colX, col2Y + 20, 400, 30);
        txtGenero.setFont(new Font("Arial", Font.PLAIN, 12));
        txtGenero.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtGenero);

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

        JButton btnImagen = new JButton("Agregar");
        btnImagen.setBounds(50, 350, 150, 25);
        btnImagen.setBackground(new Color(255, 219, 153));
        btnImagen.setForeground(new Color(152, 33, 54));
        btnImagen.setFont(new Font("Arial", Font.BOLD, 12));
        btnImagen.setFocusPainted(false);
        btnImagen.setBorder(new LineBorder(new Color(255, 219, 153)));
        btnImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnImagen.addActionListener(e -> {
        });
        mainPanel.add(btnImagen);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBounds(260, 400, 110, 35);
        btnConfirmar.setBackground(new Color(110, 75, 80));
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
        btnCancelar.setBounds(380, 400, 110, 35);
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

    public String[] obtenerDatos() {
        return new String[]{
            txtTitulo.getText(),
            txtGenero.getText(),
            txtClasificacion.getText(),
            txtRenta.getText(),
            txtVenta.getText(),
            txtStock.getText(),
            "", 
            "", 
            txtAnio.getText(),
            (chkSoloRenta.isSelected() ? "Solo Renta" : (chkSoloVenta.isSelected() ? "Solo Venta" : "Venta/Renta"))
        };
    }
}