package frontend.src.mainpackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * Módulo de Gestión de Clientes.
 */
public class PnlGestionClientes extends JPanel {
    private final ViewDashboard parent;
    private JTable tablaClientes;
    private JTable tablaHistorial;
    private JTabbedPane tabbedPane;
    private JTextField txtBuscarCliente;
    private JTextField txtBuscarHistorial;
    private JComboBox<String> comboEstatusCliente;
    private JComboBox<String> comboEstatusHistorial;
    private JCheckBox chkFrecuentes;
    private JButton btnCrearCliente;
    private JLabel lblTotalClientes;
    private JLabel lblTituloEstadisticas;
    private JLabel lblJuegosPendientes;
    private PnlTotalJuego pnlTotalJuego;
    private PnlResumenCliente pnlResumenCliente;

    public PnlGestionClientes(ViewDashboard parent) {
        this.parent = parent;
        this.setLayout(new BorderLayout());
        this.setBackground(Ventana.CARD_WHITE);
        
        initComponentes();
    }
    
    private void initComponentes() {
        // Panel principal con scroll
        JPanel panelPrincipal = new JPanel(null);
        panelPrincipal.setBackground(Ventana.CARD_WHITE);
        panelPrincipal.setPreferredSize(new Dimension(950, 750)); 
        
        // --- Barra Superior de Módulo ---
        JPanel topBar = createTopBar();
        topBar.setBounds(0, 0, 950, 40);
        panelPrincipal.add(topBar);
        
        // --- Panel de Búsqueda y Filtros ---
        JPanel panelFiltros = createPanelFiltros();
        panelFiltros.setBounds(0, 40, 950, 50);
        panelPrincipal.add(panelFiltros);
        
        // --- Panel Central (Tabla de Clientes y Estadísticas) ---
        JPanel panelCentral = createPanelCentral();
        panelCentral.setBounds(0, 90, 950, 340);
        panelPrincipal.add(panelCentral);

        // --- Panel Inferior (Historial y Juegos Visuales) ---
        JPanel panelInferior = createPanelInferior();
        panelInferior.setBounds(0, 440, 950, 380);
        panelPrincipal.add(panelInferior);

        // Scroll principal
        JScrollPane scrollPrincipal = new JScrollPane(panelPrincipal);
        scrollPrincipal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPrincipal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPrincipal.getViewport().setBackground(Ventana.CARD_WHITE);
        scrollPrincipal.setBorder(BorderFactory.createEmptyBorder());
        
        this.add(scrollPrincipal, BorderLayout.CENTER);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(110, 60, 70)); // TOP_BAR
        topBar.setBounds(0, 0, 1150, 40);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblTitulo = new JLabel("Módulo de Clientes - Gestión de clientes", SwingConstants.LEFT);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        topBar.add(lblTitulo, BorderLayout.WEST);
        
        return topBar;
    }
    
    private JPanel createPanelFiltros() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);
        panel.setBounds(0, 0, 1150, 50);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        
        // Campo de búsqueda
        txtBuscarCliente = new JTextField("Buscar por ID, nombre o email...");
        txtBuscarCliente.setBounds(0, 15, 300, 30);
        txtBuscarCliente.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        txtBuscarCliente.setBackground(Color.WHITE);
        txtBuscarCliente.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(txtBuscarCliente);
        
        // Combo de estatus
        comboEstatusCliente = new JComboBox<>(new String[]{"Todos", "Activo", "Inactivo", "Suspendido"});
        comboEstatusCliente.setBounds(320, 15, 120, 30);
        comboEstatusCliente.setBackground(Color.WHITE);
        comboEstatusCliente.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        comboEstatusCliente.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(comboEstatusCliente);
        
        // Checkbox frecuentes
        chkFrecuentes = new JCheckBox("Solo frecuentes");
        chkFrecuentes.setBounds(460, 15, 120, 30);
        chkFrecuentes.setBackground(Ventana.CARD_WHITE);
        chkFrecuentes.setFont(new Font("Arial", Font.PLAIN, 12));
        chkFrecuentes.setFocusPainted(false);
        panel.add(chkFrecuentes);
        
        // Botón crear cliente
        btnCrearCliente = new JButton("+ Crear cliente");
        btnCrearCliente.setBounds(600, 15, 150, 30);
        btnCrearCliente.setBackground(Ventana.ACCENT_RED);
        btnCrearCliente.setForeground(Color.WHITE);
        btnCrearCliente.setFocusPainted(false);
        btnCrearCliente.setBorderPainted(false);
        btnCrearCliente.setFont(new Font("Arial", Font.BOLD, 12));
        btnCrearCliente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCrearCliente.addActionListener(e -> parent.getHost().mostrarFormCliente());
        panel.add(btnCrearCliente);
        
        return panel;
    }
    
    private JPanel createPanelCentral() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);
        panel.setBounds(0, 90, 1150, 280);
        
        // Panel izquierda - Tabla de clientes
        JPanel panelTabla = createPanelTablaClientes();
        panel.add(panelTabla);
        
        // Panel derecha - Estadísticas
        JPanel panelStats = createPanelEstadisticas();
        panel.add(panelStats);
        
        return panel;
    }
    
    private JPanel createPanelTablaClientes() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);
        panel.setBounds(20, 0, 630, 280);
        
        // Título con contador
        lblTotalClientes = new JLabel("Lista de clientes (24)");
        lblTotalClientes.setBounds(0, 0, 200, 25);
        lblTotalClientes.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalClientes.setForeground(Color.BLACK);
        panel.add(lblTotalClientes);
        
        // Tabla de clientes
        initTablaClientes();
        
        JScrollPane scroll = new JScrollPane(tablaClientes);
        scroll.setBounds(0, 25, 630, 250);
        scroll.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll);
        
        return panel;
    }
    
    private JPanel createPanelEstadisticas() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);
        panel.setBounds(660, 0, 270, 340);
        
        // Título
        lblTituloEstadisticas = new JLabel("Total juegos (lapso 6 meses)");
        lblTituloEstadisticas.setBounds(0, 0, 250, 25);
        lblTituloEstadisticas.setFont(new Font("Arial", Font.BOLD, 14));
        lblTituloEstadisticas.setForeground(Color.BLACK);
        panel.add(lblTituloEstadisticas);
        
        // Área vacía para el panel externo
        JPanel areaVacia = new JPanel(null);
        areaVacia.setBackground(Color.WHITE);
        areaVacia.setBounds(0, 40, 250, 300);
        
        // Panel inicial de estadísticas
        pnlTotalJuego = new PnlTotalJuego();
        pnlTotalJuego.setBounds(0, 0, 250, 200);
        areaVacia.add(pnlTotalJuego);

        // Panel de resumen del cliente oculto al inicio
        pnlResumenCliente = new PnlResumenCliente();
        pnlResumenCliente.setBounds(0, 0, 250, 300);
        pnlResumenCliente.setVisible(false);
        areaVacia.add(pnlResumenCliente);
        
        panel.add(areaVacia);
        
        return panel;
    }
    
    private void mostrarPanelTotalJuego() {
        if (pnlTotalJuego != null && pnlResumenCliente != null) {
            pnlTotalJuego.setVisible(true);
            pnlResumenCliente.setVisible(false);
            lblTituloEstadisticas.setText("Total juegos (lapso 6 meses)");
        }
    }
    
    private void mostrarPanelResumenCliente() {
        if (pnlTotalJuego != null && pnlResumenCliente != null) {
            pnlTotalJuego.setVisible(false);
            pnlResumenCliente.setVisible(true);
            lblTituloEstadisticas.setText("Resumen del cliente");
        }
    }
    
    private JPanel createPanelInferior() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);
        panel.setBounds(0, 370, 950, 380);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        
        // Panel izquierda - Historial
        JPanel panelHistorial = createPanelHistorial();
        panel.add(panelHistorial);
        
        // Panel derecha - Juegos visuales
        JPanel panelJuegosVisuales = createPanelJuegosVisuales();
        panel.add(panelJuegosVisuales);
        
        return panel;
    }
    
    private JPanel createPanelHistorial() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);
        panel.setBounds(0, 0, 630, 380);
        
        // Pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(0, 0, 630, 380);
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 12));
        tabbedPane.setBackground(Color.WHITE);
        
        // Panel de historial de rentas
        JPanel panelRentas = createPanelContenidoHistorial();
        tabbedPane.addTab("Historial de Rentas", panelRentas);
        
        // Panel de historial de compras
        JPanel panelCompras = new JPanel();
        panelCompras.setBackground(Color.WHITE);
        panelCompras.add(new JLabel("Contenido de Historial de Compras"));
        tabbedPane.addTab("Historial de Compras", panelCompras);
        
        // Panel de descuentos
        JPanel panelDescuentos = new JPanel();
        panelDescuentos.setBackground(Color.WHITE);
        panelDescuentos.add(new JLabel("Contenido de Descuentos Aplicados"));
        tabbedPane.addTab("Descuentos Aplicados", panelDescuentos);
        
        panel.add(tabbedPane);
        
        return panel;
    }
    
    private JPanel createPanelContenidoHistorial() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Campo de búsqueda
        txtBuscarHistorial = new JTextField("Buscar por ID, nombre o fecha");
        txtBuscarHistorial.setBounds(0, 10, 250, 30);
        txtBuscarHistorial.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        txtBuscarHistorial.setBackground(Color.WHITE);
        txtBuscarHistorial.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(txtBuscarHistorial);
        
        // Combo de estatus
        comboEstatusHistorial = new JComboBox<>(new String[]{"Todos", "Devuelto", "Pendiente", "Vencido"});
        comboEstatusHistorial.setBounds(270, 10, 120, 30);
        comboEstatusHistorial.setBackground(Color.WHITE);
        comboEstatusHistorial.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        comboEstatusHistorial.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(comboEstatusHistorial);
        
        // Tabla de historial
        initTablaHistorial();
        
        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.setBounds(0, 50, 600, 280);
        scroll.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll);
        
        return panel;
    }
    
    private JPanel createPanelJuegosVisuales() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);
        panel.setBounds(640, 0, 300, 380);
        
        // Título
        JLabel lblTitulo = new JLabel("Juegos aspecto visual");
        lblTitulo.setBounds(0, 0, 250, 25);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(Color.BLACK);
        panel.add(lblTitulo);
        
        // Lista de juegos visuales
        JPanel panelListaJuegos = createListaJuegosVisuales();
        JScrollPane scroll = new JScrollPane(panelListaJuegos);
        scroll.setBounds(0, 30, 300, 250);
        scroll.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scroll);
        
        return panel;
    }
    
    private JPanel createListaJuegosVisuales() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBackground(Color.WHITE);
        
        // Datos de ejemplo de juegos
        String[][] juegos = {
            {"BIOSHOCK INFINITE (XBOX 360)", "caratulaGame1.png", "2024-01-15", "Devuelto", "USR-001"},
            {"Halo 3 ODST(XBOX 360)", "caratulagame2.png", "2024-01-20", "Pendiente", "USR-002"},
            {"Assassin's Creed IV: Black Flag(PS4)", "caratulaGame3.png", "2024-01-18", "Devuelto", "USR-003"},
            {"The Last of Us Remastered(PS4)", "caratulaGame4.png", "2024-01-22", "Pendiente", "USR-004"}
        };
        
        for (String[] juego : juegos) {
            JPanel itemJuego = createItemJuegoVisual(juego[0], juego[1], juego[2], juego[3], juego[4]);
            panel.add(itemJuego);
        }
        
        return panel;
    }
    
    private JPanel createItemJuegoVisual(String titulo, String imagen, String fecha, String estatus, String idUsuario) {
        JPanel item = new JPanel(new BorderLayout(5, 0));
        item.setBackground(Color.WHITE);
        item.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        item.setPreferredSize(new Dimension(280, 70));
        
        // Carátula del juego
        JLabel lblCaratula = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/" + imagen));
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(50, 60, Image.SCALE_SMOOTH);
                lblCaratula.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            lblCaratula.setText("[Img]");
            lblCaratula.setHorizontalAlignment(SwingConstants.CENTER);
            lblCaratula.setBorder(new LineBorder(Color.GRAY, 1));
        }
        lblCaratula.setPreferredSize(new Dimension(50, 60));
        
        // Información del juego
        JPanel panelInfo = new JPanel(new GridLayout(4, 1, 0, 2));
        panelInfo.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 10));
        lblTitulo.setForeground(Color.BLACK);
        
        JLabel lblFecha = new JLabel("Fecha límite: " + fecha);
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 9));
        lblFecha.setForeground(Color.GRAY);
        
        JLabel lblEstatus = new JLabel(estatus);
        lblEstatus.setFont(new Font("Arial", Font.BOLD, 9));
        if ("Devuelto".equals(estatus)) {
            lblEstatus.setForeground(new Color(46, 204, 113));
        } else if ("Pendiente".equals(estatus)) {
            lblEstatus.setForeground(Ventana.ACCENT_RED);
        }
        
        JLabel lblIdUsuario = new JLabel("ID: " + idUsuario);
        lblIdUsuario.setFont(new Font("Arial", Font.PLAIN, 9));
        lblIdUsuario.setForeground(Color.GRAY);
        
        panelInfo.add(lblTitulo);
        panelInfo.add(lblFecha);
        panelInfo.add(lblEstatus);
        panelInfo.add(lblIdUsuario);
        
        item.add(lblCaratula, BorderLayout.WEST);
        item.add(panelInfo, BorderLayout.CENTER);
        
        return item;
    }
    
    private void initTablaClientes() {
        String[] columnas = {"ID", "Nombre completo", "Email", "Estatus", "Nivel", "Acciones"};
        
        Object[][] datos = {
            {"CLI-001", "Caitlyn Kiramman", "caitlyn.k@email.com", "Activo", "Oro", "..."},
            {"CLI-002", "Vi Enforcer", "vi.enforcer@email.com", "Activo", "Plata", "..."},
            {"CLI-003", "Talía Braum", "talia.b@email.com", "Inactivo", "Bronce", "..."},
            {"CLI-004", "Viktor Herald", "viktor.h@email.com", "Activo", "Oro", "..."},
            {"CLI-005", "Jayce Tallis", "jayce.t@email.com", "Suspendido", "Plata", "..."}
        };
        
        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tablaClientes = new JTable(modelo);
        tablaClientes.setRowHeight(35);
        tablaClientes.setSelectionBackground(new Color(152, 33, 54, 40));
        tablaClientes.setSelectionForeground(Color.BLACK);
        tablaClientes.setShowVerticalLines(false);
        tablaClientes.setGridColor(new Color(235, 235, 235));
        tablaClientes.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablaClientes.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    mostrarPanelResumenCliente();
                }
            }
        });
        
        // Renderizador para la columna de acciones
        tablaClientes.getColumnModel().getColumn(5).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                
                // Botón ver
                JButton btnVer = new JButton("👁");
                btnVer.setBackground(Color.WHITE);
                btnVer.setBorder(BorderFactory.createEmptyBorder());
                btnVer.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnVer.setToolTipText("Ver detalles");
                
                // Botón editar
                JButton btnEditar = new JButton("✏");
                btnEditar.setBackground(Color.WHITE);
                btnEditar.setBorder(BorderFactory.createEmptyBorder());
                btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnEditar.setToolTipText("Editar cliente");
                
                // Botón eliminar
                JButton btnEliminar = new JButton("🗑");
                btnEliminar.setBackground(Color.WHITE);
                btnEliminar.setBorder(BorderFactory.createEmptyBorder());
                btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnEliminar.setToolTipText("Eliminar cliente");
                
                panel.add(btnVer);
                panel.add(btnEditar);
                panel.add(btnEliminar);
                
                return panel;
            }
        });
        
        // Estilo del encabezado
        JTableHeader header = tablaClientes.getTableHeader();
        header.setBackground(Ventana.MAROON_BG);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 35));
        header.setReorderingAllowed(false);
    }
    
    private void initTablaHistorial() {
        String[] columnas = {"Juego", "Fecha renta", "Fecha dev. (est)", "Estatus", "ID usuario"};
        
        Object[][] datos = {
            {"BIOSHOCK INFINITE", "2024-01-10", "2024-01-17", "Devuelto", "CLI-001"},
            {"Halo 3 ODST", "2024-01-15", "2024-01-22", "Pendiente", "CLI-002"},
            {"Assassin's Creed IV", "2024-01-08", "2024-01-15", "Devuelto", "CLI-003"},
            {"The Last of Us", "2024-01-20", "2024-01-27", "Pendiente", "CLI-004"}
        };
        
        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tablaHistorial = new JTable(modelo);
        tablaHistorial.setRowHeight(30);
        tablaHistorial.setSelectionBackground(new Color(152, 33, 54, 40));
        tablaHistorial.setSelectionForeground(Color.BLACK);
        tablaHistorial.setShowVerticalLines(false);
        tablaHistorial.setGridColor(new Color(235, 235, 235));
        tablaHistorial.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Renderizador para la columna de estatus
        tablaHistorial.getColumnModel().getColumn(3).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 10));
                
                if ("Devuelto".equals(value)) {
                    label.setBackground(new Color(46, 204, 113));
                    label.setForeground(Color.WHITE);
                } else if ("Pendiente".equals(value)) {
                    label.setBackground(Ventana.ACCENT_RED);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.ORANGE);
                    label.setForeground(Color.WHITE);
                }
                
                label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
                
                return label;
            }
        });
        
        // Estilo del encabezado
        JTableHeader header = tablaHistorial.getTableHeader();
        header.setBackground(Ventana.MAROON_BG);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 11));
        header.setPreferredSize(new Dimension(0, 30));
        header.setReorderingAllowed(false);
    }
}