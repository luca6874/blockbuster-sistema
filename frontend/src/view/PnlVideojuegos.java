package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.controller.VideojuegoController;
import frontend.src.model.VideojuegoInfo;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 * Modulo de Videojuegos.
 */
public class PnlVideojuegos extends JPanel {
    private final ViewDashboard parent;
    private JTable tablaVideojuegos;
    private JTextField txtBuscar;
    private JComboBox<String> comboPlataforma;
    private JLabel lblTituloTabla;
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
    private List<VideojuegoInfo> datosVideojuegos = new ArrayList<>();
    private int filaSeleccionada = -1;

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

        JLabel lblTitulo = new JLabel("Modulo de Videojuegos - Gestion de Videojuegos", SwingConstants.LEFT);
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

        txtBuscar = new JTextField("Buscar por nombre, genero, anio...");
        txtBuscar.setBounds(10, 10, 350, 30);
        txtBuscar.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        txtBuscar.setBackground(Color.WHITE);
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(txtBuscar);

        JLabel lblPlataforma = new JLabel("Plataforma:");
        lblPlataforma.setBounds(370, 10, 80, 30);
        lblPlataforma.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblPlataforma);

        comboPlataforma = new JComboBox<>(new String[]{"Todos", "Xbox 360", "SWITCH", "PS4", "Xbox ONE", "SWITCH 2", "PS5"});
        comboPlataforma.setBounds(450, 10, 120, 30);
        comboPlataforma.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        comboPlataforma.setBackground(Color.WHITE);
        comboPlataforma.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(comboPlataforma);

        JCheckBox chkFiltro = new JCheckBox();
        chkFiltro.setBounds(580, 10, 25, 30);
        chkFiltro.setBackground(Ventana.CARD_WHITE);
        panel.add(chkFiltro);

        JComboBox<String> comboEstado = new JComboBox<>(new String[]{"Ambos", "Disponible", "Agotado"});
        comboEstado.setBounds(610, 10, 120, 30);
        comboEstado.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        comboEstado.setBackground(Color.WHITE);
        comboEstado.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(comboEstado);

        JButton btnAgregar = new JButton("+ Agregar titulo");
        btnAgregar.setBounds(790, 10, 140, 30);
        btnAgregar.setBackground(new Color(152, 33, 54));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorderPainted(false);
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> {
            parent.getHost().setOscurecer(true);
            new DlgAgregarVideojuego(parent.getHost(), this::refrescarTabla).setVisible(true);
        });
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

        lblTituloTabla = new JLabel("Listado de videojuegos (0)");
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

        JButton btnDescargar = createActionButton("Descargar info", new Color(230, 230, 230));
        btnDescargar.setBounds(16, 315, 308, 28);
        btnDescargar.setForeground(Color.BLACK);
        btnDescargar.setBorder(new LineBorder(new Color(120, 120, 120), 1, true));
        btnDescargar.setBackground(new Color(230, 230, 230));
        panel.add(btnDescargar);

        JButton btnEditar = createActionButton("Editar juego", new Color(46, 204, 113));
        btnEditar.setBounds(20, 360, 115, 28);
        btnEditar.addActionListener(e -> abrirEditarSeleccionado());
        panel.add(btnEditar);

        JButton btnEliminar = createActionButton("Eliminar juego", new Color(231, 76, 60));
        btnEliminar.setBounds(145, 360, 115, 28);
        btnEliminar.addActionListener(e -> confirmarEliminarSeleccionado());
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
        String[] columnas = {"Titulo", "Genero", "Clasificacion", "Renta", "Compra", "Puntos"};

        datosVideojuegos = VideojuegoController.traerVideojuegosDeBD();
        if (lblTituloTabla != null) {
            lblTituloTabla.setText("Listado de videojuegos (" + datosVideojuegos.size() + ")");
        }

        DefaultTableModel modelo = new DefaultTableModel(convertirVideojuegosAArray(), columnas) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaVideojuegos = new JTable(modelo);
        tablaVideojuegos.setRowHeight(55);
        tablaVideojuegos.setSelectionBackground(new Color(152, 33, 54, 40));
        tablaVideojuegos.setSelectionForeground(Color.BLACK);
        tablaVideojuegos.setShowVerticalLines(false);
        tablaVideojuegos.setGridColor(new Color(235, 235, 235));
        tablaVideojuegos.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaVideojuegos.setCursor(new Cursor(Cursor.HAND_CURSOR));

        tablaVideojuegos.getColumnModel().getColumn(0).setPreferredWidth(180);
        tablaVideojuegos.getColumnModel().getColumn(0).setCellRenderer(new TituloTableRenderer());
        tablaVideojuegos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaVideojuegos.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaVideojuegos.getColumnModel().getColumn(3).setPreferredWidth(70);
        tablaVideojuegos.getColumnModel().getColumn(4).setPreferredWidth(70);
        tablaVideojuegos.getColumnModel().getColumn(5).setPreferredWidth(50);

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
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        if (datosVideojuegos.isEmpty()) {
            limpiarDetalle();
        } else {
            mostrarDetalle(0);
            tablaVideojuegos.setRowSelectionInterval(0, 0);
        }
    }

    public void refrescarTabla() {
        datosVideojuegos = VideojuegoController.traerVideojuegosDeBD();
        DefaultTableModel modelo = (DefaultTableModel) tablaVideojuegos.getModel();
        modelo.setDataVector(convertirVideojuegosAArray(), new String[]{"Titulo", "Genero", "Clasificacion", "Renta", "Compra", "Puntos"});
        tablaVideojuegos.getColumnModel().getColumn(0).setPreferredWidth(180);
        tablaVideojuegos.getColumnModel().getColumn(0).setCellRenderer(new TituloTableRenderer());
        tablaVideojuegos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaVideojuegos.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaVideojuegos.getColumnModel().getColumn(3).setPreferredWidth(70);
        tablaVideojuegos.getColumnModel().getColumn(4).setPreferredWidth(70);
        tablaVideojuegos.getColumnModel().getColumn(5).setPreferredWidth(50);

        if (lblTituloTabla != null) {
            lblTituloTabla.setText("Listado de videojuegos (" + datosVideojuegos.size() + ")");
        }

        if (datosVideojuegos.isEmpty()) {
            filaSeleccionada = -1;
            limpiarDetalle();
        } else {
            int nuevaFila = Math.min(Math.max(filaSeleccionada, 0), datosVideojuegos.size() - 1);
            mostrarDetalle(nuevaFila);
            tablaVideojuegos.setRowSelectionInterval(nuevaFila, nuevaFila);
        }
    }

    private Object[][] convertirVideojuegosAArray() {
        Object[][] datos = new Object[datosVideojuegos.size()][6];
        for (int i = 0; i < datosVideojuegos.size(); i++) {
            VideojuegoInfo videojuego = datosVideojuegos.get(i);
            datos[i][0] = videojuego.getTitulo();
            datos[i][1] = videojuego.getGenero();
            datos[i][2] = videojuego.getClasificacion();
            datos[i][3] = formatoMoneda(videojuego.getPrecioRenta());
            datos[i][4] = formatoMoneda(videojuego.getPrecioCompra());
            datos[i][5] = String.valueOf(videojuego.getPuntos());
        }
        return datos;
    }

    private void mostrarDetalle(int row) {
        if (row < 0 || row >= datosVideojuegos.size()) return;
        filaSeleccionada = row;
        VideojuegoInfo videojuego = datosVideojuegos.get(row);

        lblDetalleTitulo.setText("<html><b>" + videojuego.getTitulo() + "</b></html>");
        lblDetallePlataforma.setText("<html>" + valor(videojuego.getPlataforma()) + "</html>");
        lblDetalleGenero.setText("<html>Genero: " + valor(videojuego.getGenero()) + "</html>");
        lblDetalleClasificacion.setText("<html>Clasificacion: " + valor(videojuego.getClasificacion()) + "</html>");
        lblDetalleAnno.setText("<html>Anio: " + (videojuego.getAnioLanzamiento() > 0 ? videojuego.getAnioLanzamiento() : "-") + "</html>");
        lblDetalleModo.setText("<html>Modo: " + obtenerModo(videojuego) + "</html>");
        lblDetalleRenta.setText("Renta: " + formatoMoneda(videojuego.getPrecioRenta()));
        lblDetalleCompra.setText("Venta: " + formatoMoneda(videojuego.getPrecioCompra()));
        lblDetallePuntos.setText("Puntos: " + videojuego.getPuntos());
        lblDetalleStock.setText("Stock: " + videojuego.getStock());
        cargarImagenDetalle(videojuego.getImagenUrl());
    }

    private void abrirEditarSeleccionado() {
        if (filaSeleccionada < 0 || filaSeleccionada >= datosVideojuegos.size()) {
            return;
        }

        VideojuegoInfo videojuego = datosVideojuegos.get(filaSeleccionada);
        parent.getHost().setOscurecer(true);
        new DlgEdicionVideojuego(parent.getHost(), videojuego, this::refrescarTabla).setVisible(true);
    }

    private void confirmarEliminarSeleccionado() {
        if (filaSeleccionada < 0 || filaSeleccionada >= datosVideojuegos.size()) {
            return;
        }

        VideojuegoInfo videojuego = datosVideojuegos.get(filaSeleccionada);
        parent.getHost().setOscurecer(true);
        new DlgConfirmarEliminacionVideojuego(parent.getHost(), convertirVideojuegoADetalle(videojuego), () -> {
            boolean eliminado = VideojuegoController.eliminarVideojuego(videojuego.getId());
            if (eliminado) {
                refrescarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el videojuego.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).setVisible(true);
    }

    private String[] convertirVideojuegoADetalle(VideojuegoInfo videojuego) {
        return new String[]{
            videojuego.getTitulo(),
            videojuego.getGenero(),
            videojuego.getClasificacion(),
            formatoMoneda(videojuego.getPrecioRenta()),
            formatoMoneda(videojuego.getPrecioCompra()),
            String.valueOf(videojuego.getPuntos()),
            videojuego.getImagenUrl() != null ? videojuego.getImagenUrl() : "",
            videojuego.getPlataforma(),
            videojuego.getAnioLanzamiento() > 0 ? String.valueOf(videojuego.getAnioLanzamiento()) : "",
            obtenerModo(videojuego)
        };
    }

    private void cargarImagenDetalle(String nombreImagen) {
        lblDetalleImagen.setIcon(null);
        lblDetalleImagen.setText("");

        if (nombreImagen == null || nombreImagen.trim().isEmpty()) {
            lblDetalleImagen.setText("Sin imagen");
            lblDetalleImagen.setHorizontalAlignment(SwingConstants.CENTER);
            return;
        }

        try {
            URL url = getClass().getResource("/frontend/src/images/" + nombreImagen);
            if (url != null) {
                Image img = new ImageIcon(url).getImage().getScaledInstance(140, 180, Image.SCALE_SMOOTH);
                lblDetalleImagen.setIcon(new ImageIcon(img));
            } else {
                lblDetalleImagen.setText("Sin imagen");
                lblDetalleImagen.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } catch (Exception ex) {
            lblDetalleImagen.setText("Sin imagen");
            lblDetalleImagen.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    private void limpiarDetalle() {
        lblDetalleTitulo.setText("");
        lblDetallePlataforma.setText("");
        lblDetalleGenero.setText("");
        lblDetalleClasificacion.setText("");
        lblDetalleAnno.setText("");
        lblDetalleModo.setText("");
        lblDetalleRenta.setText("");
        lblDetalleCompra.setText("");
        lblDetallePuntos.setText("");
        lblDetalleStock.setText("");
        lblDetalleImagen.setIcon(null);
        lblDetalleImagen.setText("");
    }

    private String formatoMoneda(double valor) {
        return String.format("$%.2f", valor);
    }

    private String valor(String valor) {
        return valor == null || valor.trim().isEmpty() ? "-" : valor;
    }

    private String obtenerModo(VideojuegoInfo videojuego) {
        boolean tieneRenta = videojuego.getPrecioRenta() > 0;
        boolean tieneCompra = videojuego.getPrecioCompra() > 0;
        if (tieneRenta && tieneCompra) return "Venta/Renta";
        if (tieneRenta) return "Solo Renta";
        if (tieneCompra) return "Solo Venta";
        return "-";
    }

    private class TituloTableRenderer extends JPanel implements TableCellRenderer {
        private JLabel lblImagen;
        private JLabel lblTitulo;
        private JLabel lblAnio;

        public TituloTableRenderer() {
            setLayout(null);
            setOpaque(true);

            lblImagen = new JLabel();
            lblImagen.setBounds(5, 8, 40, 40);
            add(lblImagen);

            lblTitulo = new JLabel();
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 12));
            lblTitulo.setBounds(50, 8, 120, 18);
            lblTitulo.setForeground(Color.BLACK);
            add(lblTitulo);

            lblAnio = new JLabel();
            lblAnio.setFont(new Font("Arial", Font.PLAIN, 10));
            lblAnio.setBounds(50, 28, 120, 15);
            lblAnio.setForeground(new Color(100, 100, 100));
            add(lblAnio);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            lblImagen.setIcon(null);
            lblTitulo.setText("");
            lblAnio.setText("");

            if (row >= 0 && row < datosVideojuegos.size()) {
                VideojuegoInfo videojuego = datosVideojuegos.get(row);
                lblTitulo.setText(videojuego.getTitulo());
                lblAnio.setText(videojuego.getAnioLanzamiento() > 0 ? "(" + videojuego.getAnioLanzamiento() + ")" : "");

                try {
                    String imagen = videojuego.getImagenUrl();
                    URL url = imagen != null ? getClass().getResource("/frontend/src/images/" + imagen) : null;
                    if (url != null) {
                        Image img = new ImageIcon(url).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                        lblImagen.setIcon(new ImageIcon(img));
                    }
                } catch (Exception ignored) {
                    lblImagen.setIcon(null);
                }
            }

            setBackground(isSelected ? new Color(152, 33, 54, 40) : Color.WHITE);
            return this;
        }
    }
}
