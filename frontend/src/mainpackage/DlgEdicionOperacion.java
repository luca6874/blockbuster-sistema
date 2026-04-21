package frontend.src.mainpackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Dialogo para editar una operacion de renta o compra existente.
 */
public class DlgEdicionOperacion extends JDialog {
    private static final int IDX_CLIENTE = 0;
    private static final int IDX_VIDEOJUEGO = 1;
    private static final int IDX_TIPO = 2;
    private static final int IDX_MONTO = 3;
    private static final int IDX_DESCUENTO = 4;
    private static final int IDX_PUNTOS = 5;
    private static final int IDX_ID = 6;
    private static final int IDX_FECHA_RENTA = 7;
    private static final int IDX_FECHA_VENCIMIENTO = 8;
    private static final int IDX_ESTADO = 9;

    private JComboBox<String> comboCliente;
    private JComboBox<String> comboVideojuego;
    private JComboBox<String> comboTipo;
    private JComboBox<String> comboEstado;
    private JTextField txtMonto;
    private JTextField txtDescuento;
    private JTextField txtPuntos;
    private JTextField txtFechaRenta;
    private JTextField txtFechaVencimiento;
    private final String[] datosOperacion;

    public DlgEdicionOperacion(Ventana parent, String[] datosOperacion) {
        super(parent, "Edicion de Operacion", true);
        this.datosOperacion = datosOperacion;
        this.setUndecorated(true);
        this.setSize(600, 550);
        this.setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Ventana.CARD_WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPanel header = new JPanel(null);
        header.setBackground(new Color(110, 60, 70));
        header.setBounds(0, 0, 600, 40);
        mainPanel.add(header);

        JLabel lblTitulo = new JLabel("Editar Operacion - ID: " + datosOperacion[IDX_ID]);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBounds(15, 10, 500, 20);
        header.add(lblTitulo);

        int colX = 20;
        int col1Y = 50;
        int col2Y = 110;
        int col3Y = 170;
        int col4Y = 230;
        int col5Y = 290;
        int col6Y = 350;

        JLabel lblCliente = new JLabel("Cliente");
        lblCliente.setBounds(colX, col1Y, 100, 20);
        lblCliente.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblCliente);

        comboCliente = new JComboBox<>(new String[]{"Luis sornoza", "Gustavo Cerati", "Juan Garcia", "Maria Lopez"});
        comboCliente.setBounds(colX, col1Y + 20, 550, 30);
        comboCliente.setFont(new Font("Arial", Font.PLAIN, 12));
        comboCliente.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        comboCliente.setSelectedItem(datosOperacion[IDX_CLIENTE]);
        mainPanel.add(comboCliente);

        JLabel lblVideojuego = new JLabel("Videojuego");
        lblVideojuego.setBounds(colX, col2Y, 100, 20);
        lblVideojuego.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblVideojuego);

        comboVideojuego = new JComboBox<>(new String[]{"Persona 5", "LEAGUE OF LEGENDS: El Rey Arruinado", "God of War", "The Last of Us"});
        comboVideojuego.setBounds(colX, col2Y + 20, 550, 30);
        comboVideojuego.setFont(new Font("Arial", Font.PLAIN, 12));
        comboVideojuego.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        comboVideojuego.setSelectedItem(datosOperacion[IDX_VIDEOJUEGO]);
        mainPanel.add(comboVideojuego);

        JLabel lblTipo = new JLabel("Tipo de Operacion");
        lblTipo.setBounds(colX, col3Y, 120, 20);
        lblTipo.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblTipo);

        comboTipo = new JComboBox<>(new String[]{"Renta", "Compra"});
        comboTipo.setBounds(colX, col3Y + 20, 250, 30);
        comboTipo.setFont(new Font("Arial", Font.PLAIN, 12));
        comboTipo.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        comboTipo.setSelectedItem(datosOperacion[IDX_TIPO]);
        mainPanel.add(comboTipo);

        JLabel lblEstado = new JLabel("Estado");
        lblEstado.setBounds(colX + 280, col3Y, 100, 20);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblEstado);

        comboEstado = new JComboBox<>(new String[]{"Activa", "Completada", "Cancelada", "Pendiente"});
        comboEstado.setBounds(colX + 280, col3Y + 20, 250, 30);
        comboEstado.setFont(new Font("Arial", Font.PLAIN, 12));
        comboEstado.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        comboEstado.setSelectedItem(datosOperacion[IDX_ESTADO]);
        mainPanel.add(comboEstado);

        JLabel lblMonto = new JLabel("Monto ($)");
        lblMonto.setBounds(colX, col4Y, 250, 20);
        lblMonto.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblMonto);

        txtMonto = new JTextField();
        txtMonto.setBounds(colX, col4Y + 20, 250, 30);
        txtMonto.setFont(new Font("Arial", Font.PLAIN, 12));
        txtMonto.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        txtMonto.setText(datosOperacion[IDX_MONTO]);
        mainPanel.add(txtMonto);

        JLabel lblDescuento = new JLabel("Descuento (%)");
        lblDescuento.setBounds(colX + 280, col4Y, 250, 20);
        lblDescuento.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblDescuento);

        txtDescuento = new JTextField();
        txtDescuento.setBounds(colX + 280, col4Y + 20, 250, 30);
        txtDescuento.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDescuento.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        txtDescuento.setText(datosOperacion[IDX_DESCUENTO]);
        mainPanel.add(txtDescuento);

        JLabel lblPuntos = new JLabel("Puntos");
        lblPuntos.setBounds(colX, col5Y, 100, 20);
        lblPuntos.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblPuntos);

        txtPuntos = new JTextField();
        txtPuntos.setBounds(colX, col5Y + 20, 550, 30);
        txtPuntos.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPuntos.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        txtPuntos.setText(datosOperacion[IDX_PUNTOS]);
        mainPanel.add(txtPuntos);

        JLabel lblFechaRenta = new JLabel("Fecha Renta (DD/MM/YYYY)");
        lblFechaRenta.setBounds(colX, col6Y, 250, 20);
        lblFechaRenta.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblFechaRenta);

        txtFechaRenta = new JTextField();
        txtFechaRenta.setBounds(colX, col6Y + 20, 250, 30);
        txtFechaRenta.setFont(new Font("Arial", Font.PLAIN, 12));
        txtFechaRenta.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        txtFechaRenta.setText(datosOperacion[IDX_FECHA_RENTA]);
        mainPanel.add(txtFechaRenta);

        JLabel lblFechaVencimiento = new JLabel("Fecha Vencimiento (DD/MM/YYYY)");
        lblFechaVencimiento.setBounds(colX + 280, col6Y, 250, 20);
        lblFechaVencimiento.setFont(new Font("Arial", Font.BOLD, 11));
        mainPanel.add(lblFechaVencimiento);

        txtFechaVencimiento = new JTextField();
        txtFechaVencimiento.setBounds(colX + 280, col6Y + 20, 250, 30);
        txtFechaVencimiento.setFont(new Font("Arial", Font.PLAIN, 12));
        txtFechaVencimiento.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        txtFechaVencimiento.setText(datosOperacion[IDX_FECHA_VENCIMIENTO]);
        mainPanel.add(txtFechaVencimiento);

        JButton btnGuardar = new JButton("Guardar cambios");
        btnGuardar.setBounds(340, 470, 100, 30);
        btnGuardar.setBackground(new Color(152, 33, 54));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardar(parent));
        mainPanel.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(460, 470, 100, 30);
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
        if (comboCliente.getSelectedItem() == null || txtMonto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor completa todos los campos requeridos.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        datosOperacion[IDX_CLIENTE] = (String) comboCliente.getSelectedItem();
        datosOperacion[IDX_VIDEOJUEGO] = (String) comboVideojuego.getSelectedItem();
        datosOperacion[IDX_TIPO] = (String) comboTipo.getSelectedItem();
        datosOperacion[IDX_MONTO] = txtMonto.getText();
        datosOperacion[IDX_DESCUENTO] = txtDescuento.getText();
        datosOperacion[IDX_PUNTOS] = txtPuntos.getText();
        datosOperacion[IDX_FECHA_RENTA] = txtFechaRenta.getText();
        datosOperacion[IDX_FECHA_VENCIMIENTO] = txtFechaVencimiento.getText();
        datosOperacion[IDX_ESTADO] = (String) comboEstado.getSelectedItem();

        this.setVisible(false);
        parent.setOscurecer(false);
        parent.mostrarAvisoExitoso(this);
    }
}
