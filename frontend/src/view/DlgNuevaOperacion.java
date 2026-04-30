package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.model.VideojuegoInfo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Diálogo para crear una nueva operación de renta o compra.
 */
public class DlgNuevaOperacion extends JDialog {
    private JComboBox<String> comboCliente;
    private JTextField txtVideojuegoSeleccionado;
    private JButton btnSeleccionarVideojuego;
    private JComboBox<String> comboTipo;
    private JTextField txtMonto;
    private JTextField txtDescuento;
    private JTextField txtPuntos;
    private JTextField txtFechaRenta;
    private JTextField txtFechaVencimiento;
    private VideojuegoInfo videojuegoSeleccionado;

    public DlgNuevaOperacion(Ventana parent, boolean dummy) {
        super(parent, "Nueva Operación", true);
        this.setUndecorated(true);
        this.setSize(600, 500);
        this.setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Ventana.CARD_WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // --- HEADER ---
        JPanel header = new JPanel(null);
        header.setBackground(new Color(110, 60, 70));
        header.setBounds(0, 0, 600, 40);
        mainPanel.add(header);

        JLabel lblTitulo = new JLabel("Nueva Operación de Renta/Compra");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBounds(15, 10, 500, 20);
        header.add(lblTitulo);

        // --- CAMPOS ---
        int colX = 20;
        int col1Y = 50;
        int col2Y = 110;
        int col3Y = 170;
        int col4Y = 230;
        int col5Y = 290;
        int col6Y = 350;

        // Cliente
        JLabel lblCliente = new JLabel("Cliente");
        lblCliente.setBounds(colX, col1Y, 100, 20);
        lblCliente.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblCliente);
        comboCliente = new JComboBox<>(new String[]{"Luis sornoza", "Gustavo Cerati", "Juan García", "María López"});
        comboCliente.setBounds(colX, col1Y + 20, 550, 30);
        comboCliente.setFont(new Font("Arial", Font.PLAIN, 12));
        comboCliente.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(comboCliente);

        // Videojuego
        JLabel lblVideojuego = new JLabel("Videojuego");
        lblVideojuego.setBounds(colX, col2Y, 100, 20);
        lblVideojuego.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblVideojuego);
        
        JPanel panelVideojuego = new JPanel(null);
        panelVideojuego.setBackground(Ventana.CARD_WHITE);
        panelVideojuego.setBounds(colX, col2Y + 20, 550, 30);
        panelVideojuego.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        
        txtVideojuegoSeleccionado = new JTextField("Selecciona un videojuego...");
        txtVideojuegoSeleccionado.setBounds(0, 0, 470, 30);
        txtVideojuegoSeleccionado.setFont(new Font("Arial", Font.PLAIN, 12));
        txtVideojuegoSeleccionado.setEditable(false);
        txtVideojuegoSeleccionado.setBorder(null);
        txtVideojuegoSeleccionado.setBackground(Ventana.CARD_WHITE);
        panelVideojuego.add(txtVideojuegoSeleccionado);
        
        btnSeleccionarVideojuego = new JButton("Seleccionar");
        btnSeleccionarVideojuego.setBounds(470, 0, 80, 30);
        btnSeleccionarVideojuego.setBackground(new Color(110, 60, 70));
        btnSeleccionarVideojuego.setForeground(Color.WHITE);
        btnSeleccionarVideojuego.setFocusPainted(false);
        btnSeleccionarVideojuego.setBorderPainted(false);
        btnSeleccionarVideojuego.setFont(new Font("Arial", Font.BOLD, 10));
        btnSeleccionarVideojuego.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSeleccionarVideojuego.addActionListener(e -> abrirSelectorVideojuego());
        panelVideojuego.add(btnSeleccionarVideojuego);
        
        mainPanel.add(panelVideojuego);

        // Tipo
        JLabel lblTipo = new JLabel("Tipo de Operación");
        lblTipo.setBounds(colX, col3Y, 100, 20);
        lblTipo.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblTipo);
        comboTipo = new JComboBox<>(new String[]{"Renta", "Compra"});
        comboTipo.setBounds(colX, col3Y + 20, 550, 30);
        comboTipo.setFont(new Font("Arial", Font.PLAIN, 12));
        comboTipo.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(comboTipo);

        // Monto
        JLabel lblMonto = new JLabel("Monto ($)");
        lblMonto.setBounds(colX, col4Y, 250, 20);
        lblMonto.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblMonto);
        txtMonto = new JTextField();
        txtMonto.setBounds(colX, col4Y + 20, 250, 30);
        txtMonto.setFont(new Font("Arial", Font.PLAIN, 12));
        txtMonto.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtMonto);

        // Descuento
        JLabel lblDescuento = new JLabel("Descuento (%)");
        lblDescuento.setBounds(colX + 280, col4Y, 250, 20);
        lblDescuento.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblDescuento);
        txtDescuento = new JTextField();
        txtDescuento.setBounds(colX + 280, col4Y + 20, 250, 30);
        txtDescuento.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDescuento.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtDescuento);

        // Puntos
        JLabel lblPuntos = new JLabel("Puntos");
        lblPuntos.setBounds(colX, col5Y, 100, 20);
        lblPuntos.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblPuntos);
        txtPuntos = new JTextField();
        txtPuntos.setBounds(colX, col5Y + 20, 250, 30);
        txtPuntos.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPuntos.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtPuntos);

        // Fecha Renta
        JLabel lblFechaRenta = new JLabel("Fecha Renta (DD/MM/YYYY)");
        lblFechaRenta.setBounds(colX + 280, col5Y, 250, 20);
        lblFechaRenta.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblFechaRenta);
        txtFechaRenta = new JTextField();
        txtFechaRenta.setBounds(colX + 280, col5Y + 20, 250, 30);
        txtFechaRenta.setFont(new Font("Arial", Font.PLAIN, 12));
        txtFechaRenta.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtFechaRenta);

        // Fecha Vencimiento
        JLabel lblFechaVencimiento = new JLabel("Fecha Vencimiento (DD/MM/YYYY)");
        lblFechaVencimiento.setBounds(colX, col6Y, 250, 20);
        lblFechaVencimiento.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblFechaVencimiento);
        txtFechaVencimiento = new JTextField();
        txtFechaVencimiento.setBounds(colX, col6Y + 20, 250, 30);
        txtFechaVencimiento.setFont(new Font("Arial", Font.PLAIN, 12));
        txtFechaVencimiento.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(txtFechaVencimiento);

        // --- BOTONES ---
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(340, 410, 100, 30);
        btnGuardar.setBackground(new Color(152, 33, 54));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardar(parent));
        mainPanel.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(460, 410, 100, 30);
        btnCancelar.setBackground(new Color(189, 195, 199));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> {
            this.setVisible(false);
            parent.setOscurecer(false);
        });
        mainPanel.add(btnCancelar);

        this.add(mainPanel);
    }

    private void guardar(Ventana parent) {
        if (comboCliente.getSelectedItem() == null || txtMonto.getText().isEmpty() || videojuegoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor completa todos los campos requeridos.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.setVisible(false);
        parent.setOscurecer(false);
        parent.mostrarAvisoExitoso(this);
    }
    
    private void abrirSelectorVideojuego() {
        DlgSeleccionVideojuegoOperacion dlg = new DlgSeleccionVideojuegoOperacion((Ventana) this.getParent(), videojuegoSeleccionado);
        dlg.setVisible(true);
        
        VideojuegoInfo seleccionado = dlg.getVideojuegoSeleccionado();
        if (seleccionado != null) {
            videojuegoSeleccionado = seleccionado;
            txtVideojuegoSeleccionado.setText(seleccionado.getTitulo() + " (" + seleccionado.getPlataforma() + ")");
        }
    }
}
