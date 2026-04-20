package frontend.src.mainpackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * Módulo de Videojuegos.
 */
public class PnlVideojuegos extends JPanel {
    private final ViewDashboard parent;
    private JTable tablaVideojuegos;
    private JTextField txtBuscar;
    private JComboBox<String> comboPlataforma;
    private JLabel lblDetalleTitulo;
    private JLabel lblDetallePlataforma;
    private JLabel lblDetalleGenero;
    private JLabel lblDetalleClasificacion;
    private JLabel lblDetalleAnno;
    private JLabel lblDetalleModo;
    private JLabel lblDetalleRenta;
    private JLabel lblDetalleCompra;
    private JLabel lblDetallePuntos;
    private JLabel lblDetalleStock;
    private JLabel lblDetalleImagen;
    private String[][] datosVideojuegos;

    public PnlVideojuegos(ViewDashboard parent) {
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

        JLabel lblTitulo = new JLabel("Módulo de Videojuegos - Gestión de Videojuegos", SwingConstants.LEFT);
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

        txtBuscar = new JTextField("Buscar por nombre, genero, año...");
        txtBuscar.setBounds(10, 10, 350, 30);
        txtBuscar.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        txtBuscar.setBackground(Color.WHITE);
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(txtBuscar);

        comboPlataforma = new JComboBox<>(new String[]{"Todos", "PS5", "Xbox", "PC", "Switch"});
        comboPlataforma.setBounds(370, 10, 120, 30);
        comboPlataforma.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        comboPlataforma.setBackground(Color.WHITE);
        comboPlataforma.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(comboPlataforma);

        JButton btnAgregar = new JButton("+ Agregar título");
        btnAgregar.setBounds(790, 10, 140, 30);
        btnAgregar.setBackground(new Color(152, 33, 54));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorderPainted(false);
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnAgregar);

        return panel;
    }

    private JPanel createPanelCentral() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);

        JPanel panelDetalle = createPanelDetalle();
        panelDetalle.setBounds(620, 0, 340, 420);
        panel.add(panelDetalle);

        JPanel panelTabla = createPanelTabla();
        panelTabla.setBounds(0, 0, 600, 500);
        panel.add(panelTabla);

        return panel;
    }

    private JPanel createPanelTabla() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);

        JLabel lblTituloTabla = new JLabel("Listado de videojuegos (24)");
        lblTituloTabla.setBounds(0, 0, 330, 25);
        lblTituloTabla.setFont(new Font("Arial", Font.BOLD, 14));
        lblTituloTabla.setForeground(Color.BLACK);
        panel.add(lblTituloTabla);

        initTablaVideojuegos();

        JScrollPane scroll = new JScrollPane(tablaVideojuegos);
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

        JLabel lblTitulo = new JLabel("Detalle del Videojuego");
        lblTitulo.setBounds(10, 10, 320, 25);
        lblTitulo.setFont(new Font("Inter", Font.BOLD, 14));
        lblTitulo.setForeground(Color.BLACK);
        header.add(lblTitulo);

        lblDetalleImagen = new JLabel();
        lblDetalleImagen.setBounds(16, 55, 141, 201);
        lblDetalleImagen.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        panel.add(lblDetalleImagen);

        lblDetalleTitulo = createDetailLabel(170, 60, 180, 25, Font.BOLD, 14);
        panel.add(lblDetalleTitulo);

        lblDetallePlataforma = createDetailLabel(170, 90, 180, 20, Font.PLAIN, 12);
        panel.add(lblDetallePlataforma);

        lblDetalleGenero = createDetailLabel(170, 115, 180, 18, Font.PLAIN, 11);
        panel.add(lblDetalleGenero);

        lblDetalleClasificacion = createDetailLabel(170, 135, 180, 18, Font.PLAIN, 11);
        panel.add(lblDetalleClasificacion);

        lblDetalleAnno = createDetailLabel(170, 155, 180, 18, Font.PLAIN, 11);
        panel.add(lblDetalleAnno);

        lblDetalleModo = createDetailLabel(170, 175, 180, 18, Font.PLAIN, 11);
        panel.add(lblDetalleModo);

        lblDetalleRenta = createDetailLabel(16, 265, 150, 18, Font.PLAIN, 11);
        panel.add(lblDetalleRenta);

        lblDetalleCompra = createDetailLabel(170, 265, 150, 18, Font.PLAIN, 11);
        panel.add(lblDetalleCompra);

        lblDetallePuntos = createDetailLabel(16, 285, 150, 18, Font.PLAIN, 11);
        panel.add(lblDetallePuntos);

        lblDetalleStock = createDetailLabel(170, 285, 150, 18, Font.PLAIN, 11);
        panel.add(lblDetalleStock);

        JButton btnDescargar = createActionButton("Descargar info", new Color(110, 60, 70));
        btnDescargar.setBounds(16, 315, 308, 28);
        panel.add(btnDescargar);

        JButton btnEditar = createActionButton("Editar juego", new Color(46, 204, 113));
        btnEditar.setBounds(20, 360, 115, 28);
        panel.add(btnEditar);

        JButton btnEliminar = createActionButton("Eliminar juego", new Color(231, 76, 60));
        btnEliminar.setBounds(145, 360, 115, 28);
        panel.add(btnEliminar);

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

    private void initTablaVideojuegos() {
        String[] columnas = {"Título", "Género", "Clasificación", "Renta", "Compra", "Puntos"};
        datosVideojuegos = new String[][]{
            {"The Evil Within", "Acción/Aventura", "M (Mature)", "$75.00", "$200.00", "10", "caratulaGame5.png", "Xbox 360", "2014", "Venta/Renta"},
            {"Silent Hill", "Survival Horror", "M (Mature)", "$75.00", "$200.00", "10", "caratulagame2.png", "PS5", "2001", "Venta/Renta"},
            {"Assassin's Creed IV", "Acción/Disparo", "M (Mature)", "$65.00", "$180.00", "8", "caratulaGame3.png", "PS4", "2013", "Venta/Renta"},
            {"The Last of Us", "Aventura/Survival", "M (Mature)", "$80.00", "$220.00", "12", "caratulaGame4.png", "PS3", "2013", "Venta/Renta"},
            {"Halo 3 ODST", "Acción/Disparo", "M (Mature)", "$75.00", "$200.00", "10", "caratulagame2.png", "Xbox 360", "2009", "Venta/Renta"},
            {"God of War", "Acción/Aventura", "M (Mature)", "$85.00", "$230.00", "15", "caratulaGame1.png", "PS5", "2018", "Venta/Renta"}
        };

        DefaultTableModel modelo = new DefaultTableModel(datosVideojuegos, columnas) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaVideojuegos = new JTable(modelo);
        tablaVideojuegos.setRowHeight(35);
        tablaVideojuegos.setSelectionBackground(new Color(152, 33, 54, 40));
        tablaVideojuegos.setSelectionForeground(Color.BLACK);
        tablaVideojuegos.setShowVerticalLines(false);
        tablaVideojuegos.setGridColor(new Color(235, 235, 235));
        tablaVideojuegos.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaVideojuegos.setCursor(new Cursor(Cursor.HAND_CURSOR));

        tablaVideojuegos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablaVideojuegos.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    mostrarDetalle(row);
                }
            }
        });

        JTableHeader header = tablaVideojuegos.getTableHeader();
        header.setBackground(Ventana.MAROON_BG);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 35));
        header.setReorderingAllowed(false);

        mostrarDetalle(0);
    }

    private void mostrarDetalle(int row) {
        if (row < 0 || row >= datosVideojuegos.length) return;
        String[] fila = datosVideojuegos[row];
        lblDetalleTitulo.setText("<html><b>" + fila[0] + "</b></html>");
        lblDetallePlataforma.setText("<html>" + fila[7] + "</html>");
        lblDetalleGenero.setText("<html>Género: " + fila[1] + "</html>");
        lblDetalleClasificacion.setText("<html>Clasificación: " + fila[2] + "</html>");
        lblDetalleAnno.setText("<html>Año: " + fila[8] + "</html>");
        lblDetalleModo.setText("<html>Modo: " + fila[9] + "</html>");
        lblDetalleRenta.setText("Renta: " + fila[3]);
        lblDetalleCompra.setText("Venta: " + fila[4]);
        lblDetallePuntos.setText("Puntos: " + fila[5]);
        lblDetalleStock.setText("Stock: 10/25");

        try {
            URL url = getClass().getResource("/frontend/src/images/" + fila[6]);
            if (url != null) {
                Image img = new ImageIcon(url).getImage().getScaledInstance(140, 180, Image.SCALE_SMOOTH);
                lblDetalleImagen.setIcon(new ImageIcon(img));
            } else {
                lblDetalleImagen.setIcon(null);
            }
        } catch (Exception ex) {
            lblDetalleImagen.setIcon(null);
        }
    }
}
