package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.controller.VideojuegoController;
import frontend.src.model.VideojuegoInfo;
import frontend.src.service.FichaTecnicaPDFGenerator;
import frontend.src.service.ImageManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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
    private JComboBox<String> comboEstado;
    private JLabel lblTituloTabla;
    private JLabel lblDetalleTitulo;
    private JLabel lblDetallePlataforma;
    private JLabel lblDetalleGenero;
    private JLabel lblDetalleClasificacion;
    private JLabel lblDetalleAnno;
    private JLabel lblDetalleModo;
    private JLabel lblDetalleRenta;
    private JLabel lblDetalleCompra;
    private JLabel lblDetalleStock;
    private JLabel lblDetalleImagen;
    private List<VideojuegoInfo> datosVideojuegos = new ArrayList<>();
    private List<VideojuegoInfo> datosVideosjuegosFiltrados = new ArrayList<>();
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
        txtBuscar.setForeground(new Color(150, 150, 150)); // Gris para placeholder
        
        // Listener para placeholder
        txtBuscar.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtBuscar.getText().equals("Buscar por nombre, genero, anio...")) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtBuscar.getText().isEmpty()) {
                    txtBuscar.setText("Buscar por nombre, genero, anio...");
                    txtBuscar.setForeground(new Color(150, 150, 150));
                }
            }
        });
        
        panel.add(txtBuscar);

        JLabel lblPlataforma = new JLabel("Plataforma:");
        lblPlataforma.setBounds(370, 10, 80, 30);
        lblPlataforma.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblPlataforma);

        comboPlataforma = new JComboBox<>(new String[]{"Todos", "Xbox", "PlayStation", "Switch", "Xbox 360", "Xbox ONE", "SWITCH", "SWITCH 2", "PS4", "PS5"});
        comboPlataforma.setBounds(450, 10, 120, 30);
        comboPlataforma.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        comboPlataforma.setBackground(Color.WHITE);
        comboPlataforma.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(comboPlataforma);

        JCheckBox chkFiltro = new JCheckBox();
        chkFiltro.setBounds(580, 10, 25, 30);
        chkFiltro.setBackground(Ventana.CARD_WHITE);
        panel.add(chkFiltro);

        comboEstado = new JComboBox<>(new String[]{"Ambos", "Disponible", "Agotado"});
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

        // Agregar listeners para filtrado dinámico
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                filtrarVideojuegos();
            }
        });

        comboPlataforma.addActionListener(e -> filtrarVideojuegos());
        comboEstado.addActionListener(e -> filtrarVideojuegos());

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

        lblDetalleStock = createDetailLabel(16, 285, 150, 18, Font.PLAIN, 11);
        panel.add(lblDetalleStock);

        JButton btnDescargar = createActionButton("Descargar ficha técnica", new Color(230, 230, 230));
        btnDescargar.setBounds(16, 315, 308, 28);
        btnDescargar.setForeground(Color.BLACK);
        btnDescargar.setBorder(new LineBorder(new Color(120, 120, 120), 1, true));
        btnDescargar.setBackground(new Color(230, 230, 230));
        btnDescargar.addActionListener(e -> descargarFichaTecnicaSeleccionada());
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
        String[] columnas = {"Titulo", "Genero", "Clasificacion", "Renta", "Compra"};

        datosVideojuegos = VideojuegoController.traerVideojuegosDeBD();
        // Inicializar lista filtrada con todos los datos al inicio
        datosVideosjuegosFiltrados = new ArrayList<>(datosVideojuegos);
        
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

        if (datosVideosjuegosFiltrados.isEmpty()) {
            limpiarDetalle();
        } else {
            mostrarDetalle(0);
            // Validar que la tabla tiene filas antes de seleccionar
            if (tablaVideojuegos.getRowCount() > 0) {
                tablaVideojuegos.setRowSelectionInterval(0, 0);
            }
        }
    }

    public void refrescarTabla() {
        datosVideojuegos = VideojuegoController.traerVideojuegosDeBD();
        filaSeleccionada = -1;
        
        // Aplicar filtros después de cargar datos
        filtrarVideojuegos();
    }

    private Object[][] convertirVideojuegosAArray() {
        Object[][] datos = new Object[datosVideosjuegosFiltrados.size()][5];
        for (int i = 0; i < datosVideosjuegosFiltrados.size(); i++) {
            VideojuegoInfo videojuego = datosVideosjuegosFiltrados.get(i);
            datos[i][0] = videojuego.getTitulo();
            datos[i][1] = videojuego.getGenero();
            datos[i][2] = videojuego.getClasificacion();
            datos[i][3] = formatoMoneda(videojuego.getPrecioRenta());
            datos[i][4] = formatoMoneda(videojuego.getPrecioCompra());
        }
        return datos;
    }

    private void mostrarDetalle(int row) {
        if (row < 0 || row >= datosVideosjuegosFiltrados.size()) return;
        filaSeleccionada = row;
        VideojuegoInfo videojuego = datosVideosjuegosFiltrados.get(row);

        lblDetalleTitulo.setText("<html><b>" + videojuego.getTitulo() + "</b></html>");
        lblDetallePlataforma.setText("<html>" + valor(videojuego.getPlataforma()) + "</html>");
        lblDetalleGenero.setText("<html>Genero: " + valor(videojuego.getGenero()) + "</html>");
        lblDetalleClasificacion.setText("<html>Clasificacion: " + valor(videojuego.getClasificacion()) + "</html>");
        lblDetalleAnno.setText("<html>Anio: " + (videojuego.getAnioLanzamiento() > 0 ? videojuego.getAnioLanzamiento() : "-") + "</html>");
        lblDetalleModo.setText("<html>Modo: " + obtenerModo(videojuego) + "</html>");
        lblDetalleRenta.setText("Renta: " + formatoMoneda(videojuego.getPrecioRenta()));
        lblDetalleCompra.setText("Venta: " + formatoMoneda(videojuego.getPrecioCompra()));
        lblDetalleStock.setText("Stock: " + videojuego.getStock());
        cargarImagenDetalle(videojuego.getImagenUrl());
    }

    private void abrirEditarSeleccionado() {
        if (filaSeleccionada < 0 || filaSeleccionada >= datosVideosjuegosFiltrados.size()) {
            return;
        }

        VideojuegoInfo videojuego = datosVideosjuegosFiltrados.get(filaSeleccionada);
        parent.getHost().setOscurecer(true);
        new DlgEdicionVideojuego(parent.getHost(), videojuego, this::refrescarTabla).setVisible(true);
    }

    private void descargarFichaTecnicaSeleccionada() {
        VideojuegoInfo videojuego = obtenerVideojuegoSeleccionado();
        if (videojuego == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un videojuego para descargar su ficha técnica.", "Sin selección", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar ficha técnica");
        chooser.setSelectedFile(new File("ficha_tecnica_" + nombreArchivoSeguro(videojuego.getId()) + ".pdf"));

        int resultado = chooser.showSaveDialog(this);
        if (resultado != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            new FichaTecnicaPDFGenerator().generar(videojuego, chooser.getSelectedFile());
            JOptionPane.showMessageDialog(this, "Ficha técnica generada correctamente.", "Descarga completa", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo generar la ficha técnica. Verifica la ubicación e intenta nuevamente.", "Error al generar PDF", JOptionPane.ERROR_MESSAGE);
        }
    }

    private VideojuegoInfo obtenerVideojuegoSeleccionado() {
        if (filaSeleccionada < 0 || filaSeleccionada >= datosVideosjuegosFiltrados.size()) {
            return null;
        }
        return datosVideosjuegosFiltrados.get(filaSeleccionada);
    }

    private String nombreArchivoSeguro(String valor) {
        String nombre = valor == null || valor.trim().isEmpty() ? "sin_id" : valor.trim();
        return nombre.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private void confirmarEliminarSeleccionado() {
        if (filaSeleccionada < 0 || filaSeleccionada >= datosVideosjuegosFiltrados.size()) {
            return;
        }

        VideojuegoInfo videojuego = datosVideosjuegosFiltrados.get(filaSeleccionada);
        parent.getHost().setOscurecer(true);
        new DlgConfirmarEliminacionVideojuego(parent.getHost(), convertirVideojuegoADetalle(videojuego), () -> {
            boolean eliminado = VideojuegoController.eliminarVideojuego(videojuego.getId());
            if (eliminado) {
                refrescarTabla();
                JOptionPane.showMessageDialog(this, "Videojuego eliminado exitosamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
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

        ImageIcon icon = ImageManager.cargarImagenPreview(nombreImagen, 140, 180);
        if (icon != null) {
            lblDetalleImagen.setIcon(icon);
        } else {
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

    /**
     * Filtra los videojuegos según los criterios activos:
     * - Búsqueda textual (nombre, género, año)
     * - Plataforma
     * - Estado (disponible/agotado)
     * 
     * Actualiza la tabla dinámicamente con los resultados filtrados.
     */
    private void filtrarVideojuegos() {
        // Obtener criterios de filtrado
        String textoBusqueda = obtenerTextoBusqueda();
        String plataformaSeleccionada = obtenerValorCombo(comboPlataforma);
        String estadoSeleccionado = obtenerValorCombo(comboEstado);
        
        // Crear lista de videojuegos filtrados
        datosVideosjuegosFiltrados = new ArrayList<>();
        
        for (VideojuegoInfo videojuego : datosVideojuegos) {
            // Filtro 1: Búsqueda textual (nombre, género, año)
            boolean coincideBusqueda = true;
            if (!textoBusqueda.isEmpty()) {
                String titulo = videojuego.getTitulo() != null ? videojuego.getTitulo().trim().toLowerCase() : "";
                String genero = videojuego.getGenero() != null ? videojuego.getGenero().trim().toLowerCase() : "";
                String anio = String.valueOf(videojuego.getAnioLanzamiento());
                
                coincideBusqueda = titulo.contains(textoBusqueda) || 
                                  genero.contains(textoBusqueda) || 
                                  anio.contains(textoBusqueda);
            }
            
            // Filtro 2: Plataforma
            boolean coincidePlataforma = coincidePlataforma(videojuego.getPlataforma(), plataformaSeleccionada);
            
            // Filtro 3: Estado (disponible/agotado)
            boolean coincideEstado = true;
            if (!"Ambos".equalsIgnoreCase(estadoSeleccionado)) {
                boolean estaDisponible = videojuego.getStock() > 0;
                if ("Disponible".equalsIgnoreCase(estadoSeleccionado)) {
                    coincideEstado = estaDisponible;
                } else if ("Agotado".equalsIgnoreCase(estadoSeleccionado)) {
                    coincideEstado = !estaDisponible;
                }
            }
            
            // Aplicar todos los filtros
            if (coincideBusqueda && coincidePlataforma && coincideEstado) {
                datosVideosjuegosFiltrados.add(videojuego);
            }
        }
        
        // Actualizar tabla con datos filtrados
        DefaultTableModel modelo = (DefaultTableModel) tablaVideojuegos.getModel();
        modelo.setDataVector(convertirVideojuegosAArray(), new String[]{"Titulo", "Genero", "Clasificacion", "Renta", "Compra"});
        
        // Restaurar renderizadores y anchos de columna
        tablaVideojuegos.getColumnModel().getColumn(0).setPreferredWidth(180);
        tablaVideojuegos.getColumnModel().getColumn(0).setCellRenderer(new TituloTableRenderer());
        tablaVideojuegos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaVideojuegos.getColumnModel().getColumn(2).setPreferredWidth(90);
        tablaVideojuegos.getColumnModel().getColumn(3).setPreferredWidth(70);
        tablaVideojuegos.getColumnModel().getColumn(4).setPreferredWidth(70);
        
        // Actualizar título con cantidad de resultados
        if (lblTituloTabla != null) {
            lblTituloTabla.setText("Listado de videojuegos (" + datosVideosjuegosFiltrados.size() + ")");
        }
        
        // Actualizar detalle del videojuego seleccionado
        if (datosVideosjuegosFiltrados.isEmpty()) {
            filaSeleccionada = -1;
            limpiarDetalle();
        } else {
            int nuevaFila = Math.min(Math.max(filaSeleccionada, 0), datosVideosjuegosFiltrados.size() - 1);
            mostrarDetalle(nuevaFila);
            // Validar que la tabla tiene filas antes de seleccionar
            if (tablaVideojuegos.getRowCount() > 0 && nuevaFila < tablaVideojuegos.getRowCount()) {
                tablaVideojuegos.setRowSelectionInterval(nuevaFila, nuevaFila);
            }
        }
    }

    private String obtenerTextoBusqueda() {
        String texto = txtBuscar.getText();
        if (texto == null || "Buscar por nombre, genero, anio...".equals(texto)) {
            return "";
        }
        return texto.trim().toLowerCase();
    }

    private String obtenerValorCombo(JComboBox<String> combo) {
        Object seleccionado = combo.getSelectedItem();
        return seleccionado == null ? "" : seleccionado.toString().trim();
    }

    private boolean coincidePlataforma(String plataformaVideojuego, String plataformaSeleccionada) {
        String filtro = plataformaSeleccionada == null ? "" : plataformaSeleccionada.trim();
        if (filtro.isEmpty() || "Todos".equalsIgnoreCase(filtro)) {
            return true;
        }

        String plataforma = plataformaVideojuego == null ? "" : plataformaVideojuego.trim();
        if (plataforma.isEmpty()) {
            return false;
        }

        String plataformaNormalizada = plataforma.toLowerCase();
        String filtroNormalizado = filtro.toLowerCase();

        if (plataformaNormalizada.equals(filtroNormalizado)) {
            return true;
        }
        if ("xbox".equals(filtroNormalizado)) {
            return plataformaNormalizada.contains("xbox");
        }
        if ("playstation".equals(filtroNormalizado)) {
            return plataformaNormalizada.contains("playstation") || plataformaNormalizada.matches("ps\\s*\\d+");
        }
        if ("switch".equals(filtroNormalizado)) {
            return plataformaNormalizada.contains("switch");
        }

        return false;
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

            if (row >= 0 && row < datosVideosjuegosFiltrados.size()) {
                VideojuegoInfo videojuego = datosVideosjuegosFiltrados.get(row);
                lblTitulo.setText(videojuego.getTitulo());
                lblAnio.setText(videojuego.getAnioLanzamiento() > 0 ? "(" + videojuego.getAnioLanzamiento() + ")" : "");

                String imagen = videojuego.getImagenUrl();
                ImageIcon icon = ImageManager.cargarImagenPreview(imagen, 40, 40);
                if (icon != null) {
                    lblImagen.setIcon(icon);
                }
            }

            setBackground(isSelected ? new Color(152, 33, 54, 40) : Color.WHITE);
            return this;
        }
    }
}
