package frontend.src.mainpackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Selector visual de videojuegos para el módulo de nueva operación.
 */
public class DlgSeleccionVideojuegoOperacion extends JDialog {
    public static class VideojuegoInfo {
        private final String id;
        private final String titulo;
        private final String plataforma;
        private final String genero;
        private final String clasificacion;
        private final double precioRenta;
        private final double precioCompra;
        private final int puntos;
        private final int stock;
        private final String imagenUrl;

        public VideojuegoInfo(String id, String titulo, String plataforma, String genero, String clasificacion, 
                             double precioRenta, double precioCompra, int puntos, int stock, String imagenUrl) {
            this.id = id;
            this.titulo = titulo;
            this.plataforma = plataforma;
            this.genero = genero;
            this.clasificacion = clasificacion;
            this.precioRenta = precioRenta;
            this.precioCompra = precioCompra;
            this.puntos = puntos;
            this.stock = stock;
            this.imagenUrl = imagenUrl;
        }

        public String getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getPlataforma() { return plataforma; }
        public String getGenero() { return genero; }
        public String getClasificacion() { return clasificacion; }
        public double getPrecioRenta() { return precioRenta; }
        public double getPrecioCompra() { return precioCompra; }
        public int getPuntos() { return puntos; }
        public int getStock() { return stock; }
        public String getImagenUrl() { return imagenUrl; }
    }

    private static final Color BORDER = new Color(214, 214, 214);
    private static final Color MUTED = new Color(110, 110, 110);
    private static final Color MAROON = new Color(110, 60, 70);

    private final List<VideojuegoInfo> videojuegos = new ArrayList<>();
    private final List<VideojuegoInfo> videojuegosFiltrados = new ArrayList<>();

    private JTextField txtBuscar;
    private JComboBox<String> comboPlataforma;
    private JTable tablaVideojuegos;
    private JLabel lblTituloTabla;
    private JLabel lblResumenTitulo;
    private JLabel lblResumenGenero;
    private JLabel lblResumenClasificacion;
    private JLabel lblResumenPrecio;
    private JLabel lblResumenPuntos;
    private JLabel lblResumenStock;
    private VideojuegoInfo videojuegoSeleccionado;

    public DlgSeleccionVideojuegoOperacion(Ventana parent, VideojuegoInfo videojuegoActual) {
        super(parent, true);
        setUndecorated(true);
        setSize(900, 380);
        setLocationRelativeTo(parent);

        inicializarVideojuegos();
        if (videojuegoActual != null) {
            videojuegoSeleccionado = buscarVideojuegoPorId(videojuegoActual.getId());
        }

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new LineBorder(BORDER, 1, true));

        txtBuscar = new JTextField();
        txtBuscar.setBounds(20, 14, 350, 30);
        txtBuscar.setBorder(new LineBorder(BORDER, 1, true));
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 11));
        txtBuscar.setText("Buscar por título, género, año...");
        txtBuscar.setForeground(new Color(150, 150, 150));
        txtBuscar.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if ("Buscar por título, género, año...".equals(txtBuscar.getText())) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtBuscar.getText().trim().isEmpty()) {
                    txtBuscar.setText("Buscar por título, género, año...");
                    txtBuscar.setForeground(new Color(150, 150, 150));
                }
            }
        });
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override
            public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override
            public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });
        mainPanel.add(txtBuscar);

        JLabel lblPlataforma = new JLabel("Plataforma:");
        lblPlataforma.setBounds(390, 18, 70, 20);
        lblPlataforma.setFont(new Font("Arial", Font.PLAIN, 11));
        lblPlataforma.setForeground(Color.BLACK);
        mainPanel.add(lblPlataforma);

        comboPlataforma = new JComboBox<>(new String[]{"Todos", "PS5", "Xbox", "PC", "Switch", "Nintendo 64"});
        comboPlataforma.setBounds(460, 14, 110, 30);
        comboPlataforma.setFont(new Font("Arial", Font.PLAIN, 11));
        comboPlataforma.setBackground(Color.WHITE);
        comboPlataforma.setBorder(new LineBorder(BORDER, 1));
        comboPlataforma.addActionListener(e -> aplicarFiltros());
        mainPanel.add(comboPlataforma);

        lblTituloTabla = new JLabel();
        lblTituloTabla.setBounds(20, 52, 200, 18);
        lblTituloTabla.setFont(new Font("Arial", Font.BOLD, 12));
        lblTituloTabla.setForeground(Color.BLACK);
        mainPanel.add(lblTituloTabla);

        initTablaVideojuegos();
        JScrollPane scroll = new JScrollPane(tablaVideojuegos);
        scroll.setBounds(20, 72, 600, 230);
        scroll.setBorder(new LineBorder(BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scroll);

        JPanel panelResumen = createResumenPanel();
        panelResumen.setBounds(635, 72, 255, 170);
        mainPanel.add(panelResumen);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBounds(645, 325, 85, 24);
        btnConfirmar.setBackground(MAROON);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.addActionListener(e -> confirmar());
        mainPanel.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(795, 325, 90, 24);
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(MAROON);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(new LineBorder(new Color(189, 150, 157), 1, true));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());
        mainPanel.add(btnCancelar);

        setContentPane(mainPanel);

        aplicarFiltros();
        if (videojuegoSeleccionado != null) {
            seleccionarVideojuegoEnTabla(videojuegoSeleccionado.getId());
        } else if (!videojuegosFiltrados.isEmpty()) {
            tablaVideojuegos.setRowSelectionInterval(0, 0);
            actualizarResumen(videojuegosFiltrados.get(0));
        }
    }

    public VideojuegoInfo getVideojuegoSeleccionado() {
        return videojuegoSeleccionado;
    }

    private void inicializarVideojuegos() {
        videojuegos.add(new VideojuegoInfo("V001", "The Evil Within", "PS5", "Acción/Horror", "M Maduro", 75.00, 200.00, 10, 5, "the-evil-within.jpg"));
        videojuegos.add(new VideojuegoInfo("V002", "Silent Hill", "PS5", "Horror", "M Maduro", 75.00, 200.00, 10, 3, "silent-hill.jpg"));
        videojuegos.add(new VideojuegoInfo("V003", "Persona 5", "PS5", "RPG", "T Teen", 65.00, 180.00, 8, 7, "persona-5.jpg"));
        videojuegos.add(new VideojuegoInfo("V004", "God of War", "PS5", "Acción/Aventura", "M Maduro", 70.00, 220.00, 12, 4, "god-of-war.jpg"));
        videojuegos.add(new VideojuegoInfo("V005", "The Last of Us Part I", "PS5", "Acción/Aventura", "M Maduro", 75.00, 210.00, 11, 6, "the-last-of-us.jpg"));
        videojuegos.add(new VideojuegoInfo("V006", "Halo Infinite", "Xbox", "FPS", "M Maduro", 70.00, 200.00, 10, 5, "halo-infinite.jpg"));
        videojuegos.add(new VideojuegoInfo("V007", "Forza Motorsport", "Xbox", "Carreras", "E 3+", 65.00, 190.00, 9, 8, "forza-motorsport.jpg"));
        videojuegos.add(new VideojuegoInfo("V008", "Elden Ring", "PC", "RPG/Acción", "M Maduro", 60.00, 185.00, 10, 4, "elden-ring.jpg"));
        videojuegos.add(new VideojuegoInfo("V009", "Super Smash Bros Ultimate", "Switch", "Lucha", "T Teen", 55.00, 170.00, 7, 10, "smash-bros.jpg"));
        videojuegos.add(new VideojuegoInfo("V010", "Mario Kart 8 Deluxe", "Switch", "Carreras", "E 3+", 50.00, 165.00, 6, 12, "mario-kart.jpg"));
    }

    private VideojuegoInfo buscarVideojuegoPorId(String id) {
        for (VideojuegoInfo videojuego : videojuegos) {
            if (videojuego.getId().equals(id)) {
                return videojuego;
            }
        }
        return null;
    }

    private JPanel createResumenPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(BORDER, 1, true));

        JLabel lblTitulo = new JLabel("Resumen del videojuego");
        lblTitulo.setBounds(8, 6, 160, 16);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 11));
        lblTitulo.setForeground(Color.BLACK);
        panel.add(lblTitulo);

        lblResumenTitulo = new JLabel("Selecciona un videojuego");
        lblResumenTitulo.setBounds(10, 26, 235, 16);
        lblResumenTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblResumenTitulo);

        lblResumenGenero = new JLabel("Género: --");
        lblResumenGenero.setBounds(10, 44, 235, 14);
        lblResumenGenero.setFont(new Font("Arial", Font.PLAIN, 9));
        lblResumenGenero.setForeground(MUTED);
        panel.add(lblResumenGenero);

        lblResumenClasificacion = new JLabel("Clasificación: --");
        lblResumenClasificacion.setBounds(10, 60, 235, 14);
        lblResumenClasificacion.setFont(new Font("Arial", Font.PLAIN, 9));
        lblResumenClasificacion.setForeground(MUTED);
        panel.add(lblResumenClasificacion);

        lblResumenStock = new JLabel("Stock: --");
        lblResumenStock.setBounds(10, 76, 235, 14);
        lblResumenStock.setFont(new Font("Arial", Font.PLAIN, 10));
        panel.add(lblResumenStock);

        lblResumenPrecio = new JLabel("Renta: $-- | Compra: $--");
        lblResumenPrecio.setBounds(10, 95, 235, 14);
        lblResumenPrecio.setFont(new Font("Arial", Font.PLAIN, 10));
        lblResumenPrecio.setForeground(MUTED);
        panel.add(lblResumenPrecio);

        lblResumenPuntos = new JLabel("Puntos: --");
        lblResumenPuntos.setBounds(10, 111, 235, 14);
        lblResumenPuntos.setFont(new Font("Arial", Font.BOLD, 10));
        lblResumenPuntos.setForeground(MAROON);
        panel.add(lblResumenPuntos);

        return panel;
    }

    private void initTablaVideojuegos() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Título", "Plataforma", "Género", "Clasificación", "Renta ($)", "Compra ($)", "Puntos"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaVideojuegos = new JTable(model);
        tablaVideojuegos.setRowHeight(20);
        tablaVideojuegos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVideojuegos.setSelectionBackground(new Color(152, 33, 54, 40));
        tablaVideojuegos.setSelectionForeground(Color.BLACK);
        tablaVideojuegos.setShowVerticalLines(true);
        tablaVideojuegos.setGridColor(new Color(230, 230, 230));
        tablaVideojuegos.setFont(new Font("Arial", Font.PLAIN, 10));

        JTableHeader header = tablaVideojuegos.getTableHeader();
        header.setBackground(new Color(244, 238, 240));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 10));
        header.setBorder(new LineBorder(BORDER, 1));
        header.setReorderingAllowed(false);

        tablaVideojuegos.getColumnModel().getColumn(0).setPreferredWidth(130);
        tablaVideojuegos.getColumnModel().getColumn(1).setPreferredWidth(65);
        tablaVideojuegos.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaVideojuegos.getColumnModel().getColumn(3).setPreferredWidth(75);
        tablaVideojuegos.getColumnModel().getColumn(4).setPreferredWidth(60);
        tablaVideojuegos.getColumnModel().getColumn(5).setPreferredWidth(65);
        tablaVideojuegos.getColumnModel().getColumn(6).setPreferredWidth(55);

        tablaVideojuegos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tablaVideojuegos.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < videojuegosFiltrados.size()) {
                    actualizarResumen(videojuegosFiltrados.get(selectedRow));
                }
            }
        });
    }

    private void aplicarFiltros() {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase(Locale.ROOT);
        if ("buscar por título, género, año...".equals(textoBusqueda)) {
            textoBusqueda = "";
        }

        String plataforma = (String) comboPlataforma.getSelectedItem();

        videojuegosFiltrados.clear();
        for (VideojuegoInfo videojuego : videojuegos) {
            boolean coincideBusqueda = textoBusqueda.isEmpty()
                || videojuego.getTitulo().toLowerCase(Locale.ROOT).contains(textoBusqueda)
                || videojuego.getGenero().toLowerCase(Locale.ROOT).contains(textoBusqueda);

            boolean coincidePlataforma = "Todos".equals(plataforma) || videojuego.getPlataforma().equalsIgnoreCase(plataforma);

            if (coincideBusqueda && coincidePlataforma) {
                videojuegosFiltrados.add(videojuego);
            }
        }

        cargarTabla();
    }

    private void cargarTabla() {
        DefaultTableModel model = (DefaultTableModel) tablaVideojuegos.getModel();
        model.setRowCount(0);

        for (VideojuegoInfo videojuego : videojuegosFiltrados) {
            model.addRow(new Object[]{
                videojuego.getTitulo(),
                videojuego.getPlataforma(),
                videojuego.getGenero(),
                videojuego.getClasificacion(),
                String.format("$%.2f", videojuego.getPrecioRenta()),
                String.format("$%.2f", videojuego.getPrecioCompra()),
                videojuego.getPuntos()
            });
        }

        lblTituloTabla.setText("Lista de videojuegos (" + videojuegosFiltrados.size() + ")");

        if (videojuegosFiltrados.isEmpty()) {
            limpiarResumen();
            return;
        }

        int rowToSelect = 0;
        if (videojuegoSeleccionado != null) {
            for (int i = 0; i < videojuegosFiltrados.size(); i++) {
                if (videojuegosFiltrados.get(i).getId().equals(videojuegoSeleccionado.getId())) {
                    rowToSelect = i;
                    break;
                }
            }
        }

        tablaVideojuegos.setRowSelectionInterval(rowToSelect, rowToSelect);
        actualizarResumen(videojuegosFiltrados.get(rowToSelect));
    }

    private void actualizarResumen(VideojuegoInfo videojuego) {
        if (videojuego == null) {
            limpiarResumen();
            return;
        }

        lblResumenTitulo.setText(videojuego.getTitulo());
        lblResumenGenero.setText("Género: " + videojuego.getGenero());
        lblResumenClasificacion.setText("Clasificación: " + videojuego.getClasificacion());
        lblResumenStock.setText("Stock: " + videojuego.getStock() + " disponibles");
        lblResumenPrecio.setText(String.format("Renta: $%.2f | Compra: $%.2f", 
            videojuego.getPrecioRenta(), videojuego.getPrecioCompra()));
        lblResumenPuntos.setText("Puntos: " + videojuego.getPuntos());
        videojuegoSeleccionado = videojuego;
    }

    private void limpiarResumen() {
        lblResumenTitulo.setText("Sin resultados");
        lblResumenGenero.setText("Género: --");
        lblResumenClasificacion.setText("Clasificación: --");
        lblResumenStock.setText("Stock: --");
        lblResumenPrecio.setText("Renta: $-- | Compra: $--");
        lblResumenPuntos.setText("Puntos: --");
    }

    private void seleccionarVideojuegoEnTabla(String id) {
        for (int i = 0; i < videojuegosFiltrados.size(); i++) {
            if (videojuegosFiltrados.get(i).getId().equals(id)) {
                tablaVideojuegos.setRowSelectionInterval(i, i);
                return;
            }
        }
    }

    private void confirmar() {
        if (videojuegoSeleccionado != null) {
            dispose();
        }
    }
}
