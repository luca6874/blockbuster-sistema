package frontend.src.mainpackage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Módulo de Rentas y Compras.
 * Gestión de operaciones de alquiler y venta de videojuegos.
 */
public class PnlRentasCompras extends JPanel {
    private final ViewDashboard parent;
    private JTable tablaOperaciones;
    private JTextField txtBuscarCliente;
    private JTextField txtBuscarVideojuego;
    private JComboBox<String> comboTipo;
    private JLabel lblDetalleID;
    private JLabel lblDetalleCliente;
    private JLabel lblDetalleVideojuego;
    private JLabel lblDetalleTipo;
    private JLabel lblDetalleFechaRenta;
    private JLabel lblDetalleFechaVencimiento;
    private JLabel lblDetalleMonto;
    private JLabel lblDetalleDescuento;
    private JLabel lblDetallePuntos;
    private JLabel lblDetalleEstado;
    private List<String[]> datosOperaciones;
    private int filaSeleccionada = 0;

    public PnlRentasCompras(ViewDashboard parent) {
        this.parent = parent;
        this.setLayout(null);
        this.setBackground(Ventana.CARD_WHITE);
        this.setPreferredSize(new Dimension(980, 600));

        initComponentes();
    }

    private void initComponentes() {
        JPanel topBar = createTopBar();
        topBar.setBounds(0, 0, 980, 40);
        this.add(topBar);

        JPanel panelFiltros = createPanelFiltros();
        panelFiltros.setBounds(0, 45, 980, 50);
        this.add(panelFiltros);

        JPanel panelCentral = createPanelCentral();
        panelCentral.setBounds(0, 100, 980, 500);
        this.add(panelCentral);
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(null);
        topBar.setBackground(new Color(110, 60, 70));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Rentas y Compras - Módulo de Gestión", SwingConstants.LEFT);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(0, 0, 700, 20);
        topBar.add(lblTitulo);

        return topBar;
    }

    private JPanel createPanelFiltros() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        txtBuscarCliente = new JTextField("Buscar por cliente...");
        txtBuscarCliente.setBounds(10, 10, 200, 30);
        txtBuscarCliente.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        txtBuscarCliente.setBackground(Color.WHITE);
        txtBuscarCliente.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(txtBuscarCliente);

        txtBuscarVideojuego = new JTextField("Buscar por videojuego...");
        txtBuscarVideojuego.setBounds(220, 10, 200, 30);
        txtBuscarVideojuego.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        txtBuscarVideojuego.setBackground(Color.WHITE);
        txtBuscarVideojuego.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(txtBuscarVideojuego);

        comboTipo = new JComboBox<>(new String[]{"Todos", "Renta", "Compra"});
        comboTipo.setBounds(430, 10, 100, 30);
        comboTipo.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        comboTipo.setBackground(Color.WHITE);
        comboTipo.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(comboTipo);

        JButton btnNuevaOperacion = new JButton("+ Nueva operación");
        btnNuevaOperacion.setBounds(790, 10, 140, 30);
        btnNuevaOperacion.setBackground(new Color(152, 33, 54));
        btnNuevaOperacion.setForeground(Color.WHITE);
        btnNuevaOperacion.setFocusPainted(false);
        btnNuevaOperacion.setBorderPainted(false);
        btnNuevaOperacion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevaOperacion.addActionListener(e -> {
            parent.getHost().setOscurecer(true);
            new DlgNuevaOperacion(parent.getHost(), false).setVisible(true);
        });
        panel.add(btnNuevaOperacion);

        return panel;
    }

    private JPanel createPanelCentral() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);

        JPanel panelDetalle = createPanelDetalle();
        panelDetalle.setBounds(620, 0, 340, 500);
        panel.add(panelDetalle);

        JPanel panelTabla = createPanelTabla();
        panelTabla.setBounds(0, 0, 600, 500);
        panel.add(panelTabla);

        return panel;
    }

    private JPanel createPanelTabla() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);

        JLabel lblTituloTabla = new JLabel("Listado de Rentas y Compras (117)");
        lblTituloTabla.setBounds(0, 0, 330, 25);
        lblTituloTabla.setFont(new Font("Arial", Font.BOLD, 14));
        lblTituloTabla.setForeground(Color.BLACK);
        panel.add(lblTituloTabla);

        initTablaOperaciones();

        JScrollPane scroll = new JScrollPane(tablaOperaciones);
        scroll.setBounds(0, 30, 600, 460);
        scroll.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll);

        return panel;
    }

    private JPanel createPanelDetalle() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);
        panel.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        JPanel header = new JPanel(null);
        header.setBackground(new Color(245, 245, 245));
        header.setBounds(0, 0, 340, 45);
        header.setBorder(new LineBorder(new Color(220, 220, 220), 0));
        panel.add(header);

        JLabel lblTitulo = new JLabel("Detalle de Operación");
        lblTitulo.setBounds(10, 10, 320, 25);
        lblTitulo.setFont(new Font("Inter", Font.BOLD, 14));
        lblTitulo.setForeground(Color.BLACK);
        header.add(lblTitulo);

        int yPos = 55;
        int lineHeight = 20;

        lblDetalleID = createDetailLabel(16, yPos, 300, lineHeight, Font.BOLD, 12);
        panel.add(lblDetalleID);
        yPos += lineHeight + 5;

        lblDetalleCliente = createDetailLabel(16, yPos, 300, lineHeight, Font.PLAIN, 11);
        panel.add(lblDetalleCliente);
        yPos += lineHeight + 5;

        lblDetalleVideojuego = createDetailLabel(16, yPos, 300, lineHeight, Font.PLAIN, 11);
        panel.add(lblDetalleVideojuego);
        yPos += lineHeight + 5;

        lblDetalleTipo = createDetailLabel(16, yPos, 300, lineHeight, Font.PLAIN, 11);
        panel.add(lblDetalleTipo);
        yPos += lineHeight + 5;

        lblDetalleFechaRenta = createDetailLabel(16, yPos, 300, lineHeight, Font.PLAIN, 11);
        panel.add(lblDetalleFechaRenta);
        yPos += lineHeight + 5;

        lblDetalleFechaVencimiento = createDetailLabel(16, yPos, 300, lineHeight, Font.PLAIN, 11);
        panel.add(lblDetalleFechaVencimiento);
        yPos += lineHeight + 5;

        lblDetalleMonto = createDetailLabel(16, yPos, 150, lineHeight, Font.PLAIN, 11);
        panel.add(lblDetalleMonto);

        lblDetalleDescuento = createDetailLabel(170, yPos, 150, lineHeight, Font.PLAIN, 11);
        panel.add(lblDetalleDescuento);
        yPos += lineHeight + 5;

        lblDetallePuntos = createDetailLabel(16, yPos, 150, lineHeight, Font.PLAIN, 11);
        panel.add(lblDetallePuntos);

        lblDetalleEstado = createDetailLabel(170, yPos, 150, lineHeight, Font.PLAIN, 11);
        panel.add(lblDetalleEstado);
        yPos += lineHeight + 10;

        JButton btnEditar = createActionButton("Editar operación", new Color(46, 204, 113));
        btnEditar.setBounds(20, 260, 115, 28);
        btnEditar.addActionListener(e -> {
            parent.getHost().setOscurecer(true);
            if (filaSeleccionada >= 0 && filaSeleccionada < datosOperaciones.size()) {
                new DlgEdicionOperacion(parent.getHost(), datosOperaciones.get(filaSeleccionada)).setVisible(true);
            }
        });
        panel.add(btnEditar);

        JButton btnEliminar = createActionButton("Eliminar operación", new Color(231, 76, 60));
        btnEliminar.setBounds(145, 260, 115, 28);
        btnEliminar.addActionListener(e -> {
            eliminarOperacionSeleccionada();
        });
        panel.add(btnEliminar);

        JButton btnDescargar = createActionButton("Descargar comprobante", new Color(230, 230, 230));
        btnDescargar.setBounds(16, 300, 308, 28);
        btnDescargar.setForeground(Color.BLACK);
        btnDescargar.setBorder(new LineBorder(new Color(120, 120, 120), 1, true));
        btnDescargar.setBackground(new Color(230, 230, 230));
        panel.add(btnDescargar);

        return panel;
    }

    private JLabel createDetailLabel(int x, int y, int w, int h, int style, int size) {
        JLabel label = new JLabel("", SwingConstants.LEFT);
        label.setBounds(x, y, w, h);
        label.setFont(new Font("Inter", style, size));
        label.setForeground(Color.BLACK);
        return label;
    }

    private JButton createActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void initTablaOperaciones() {
        String[] columnas = {"Cliente", "Videojuego", "Tipo", "Monto", "Descuento", "Puntos"};
        datosOperaciones = new ArrayList<>(Arrays.asList(new String[][]{
            {"Luis sornoza", "Persona 5", "Renta", "$250.00", "N/A", "10", "01990", "01/01/2025", "15/01/2025", "Activa"},
            {"Luis sornoza", "Persona 5", "Renta", "$250.00", "N/A", "10", "01990", "01/01/2025", "15/01/2025", "Activa"},
            {"Luis sornoza", "Persona 5", "Renta", "$250.00", "N/A", "10", "01990", "01/01/2025", "15/01/2025", "Activa"},
            {"Gustavo Cerati", "LEAGUE OF LEGENDS: El Rey Arruinado", "Compra", "$225.00", "10%", "15", "01990", "20/04/2025", "N/A", "Completada"},
            {"Luis sornoza", "Persona 5", "Renta", "$250.00", "N/A", "10", "01990", "01/01/2025", "15/01/2025", "Activa"},
            {"Luis sornoza", "Persona 5", "Renta", "$250.00", "N/A", "10", "01990", "01/01/2025", "15/01/2025", "Activa"},
            {"Luis sornoza", "Persona 5", "Renta", "$250.00", "N/A", "10", "01990", "01/01/2025", "15/01/2025", "Activa"},
            {"Luis sornoza", "Persona 5", "Renta", "$250.00", "N/A", "10", "01990", "01/01/2025", "15/01/2025", "Activa"},
            {"Luis sornoza", "Persona 5", "Renta", "$250.00", "N/A", "10", "01990", "01/01/2025", "15/01/2025", "Activa"},
            {"Luis sornoza", "Persona 5", "Renta", "$250.00", "N/A", "10", "01990", "01/01/2025", "15/01/2025", "Activa"},
            {"Luis sornoza", "Persona 5", "Renta", "$250.00", "N/A", "10", "01990", "01/01/2025", "15/01/2025", "Activa"},
            {"Luis sornoza", "Persona 5", "Renta", "$250.00", "N/A", "10", "01990", "01/01/2025", "15/01/2025", "Activa"}
        }));

        DefaultTableModel modelo = new DefaultTableModel(datosOperaciones.toArray(new String[0][]), columnas) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaOperaciones = new JTable(modelo);
        tablaOperaciones.setRowHeight(35);
        tablaOperaciones.setSelectionBackground(new Color(152, 33, 54, 40));
        tablaOperaciones.setSelectionForeground(Color.BLACK);
        tablaOperaciones.setShowVerticalLines(false);
        tablaOperaciones.setGridColor(new Color(235, 235, 235));
        tablaOperaciones.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaOperaciones.setCursor(new Cursor(Cursor.HAND_CURSOR));

        tablaOperaciones.getColumnModel().getColumn(0).setPreferredWidth(150);
        tablaOperaciones.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaOperaciones.getColumnModel().getColumn(2).setPreferredWidth(70);
        tablaOperaciones.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaOperaciones.getColumnModel().getColumn(4).setPreferredWidth(70);
        tablaOperaciones.getColumnModel().getColumn(5).setPreferredWidth(50);

        tablaOperaciones.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablaOperaciones.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    filaSeleccionada = row;
                    mostrarDetalle(row);
                }
            }
        });

        mostrarDetalle(0);
    }

    private void mostrarDetalle(int fila) {
        if (fila >= 0 && fila < datosOperaciones.size()) {
            String[] datos = datosOperaciones.get(fila);
            lblDetalleID.setText("ID: " + datos[6]);
            lblDetalleCliente.setText("Cliente: " + datos[0]);
            lblDetalleVideojuego.setText("Videojuego: " + datos[1]);
            lblDetalleTipo.setText("Tipo: " + datos[2]);
            lblDetalleFechaRenta.setText("Fecha renta: " + datos[7]);
            lblDetalleFechaVencimiento.setText("Fecha vencimiento: " + datos[8]);
            lblDetalleMonto.setText("Monto: " + datos[3]);
            lblDetalleDescuento.setText("Descuento: " + datos[5]);
            lblDetallePuntos.setText("Puntos: " + datos[4]);
            lblDetalleEstado.setText("Estado: " + datos[9]);
        }
    }

    private void eliminarOperacionSeleccionada() {
        if (filaSeleccionada >= 0 && filaSeleccionada < datosOperaciones.size()) {
            datosOperaciones.remove(filaSeleccionada);
            DefaultTableModel modelo = (DefaultTableModel) tablaOperaciones.getModel();
            modelo.removeRow(filaSeleccionada);
            
            if (filaSeleccionada > 0) filaSeleccionada--;
            if (datosOperaciones.size() > 0) mostrarDetalle(filaSeleccionada);
        }
    }
}
