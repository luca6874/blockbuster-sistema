package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.controller.ClienteController;
import frontend.src.model.ClienteInfo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * Módulo de Gestión de Clientes.
 */
public class PnlGestionClientes extends JPanel {
    private static final String[] COLUMNAS_CLIENTES = {"ID", "Nombre completo", "Email", "Estatus", "Nivel", "Acciones"};
    private static final String PLACEHOLDER_BUSQUEDA_CLIENTE = "Buscar por nombre o email...";

    private final ViewDashboard parent;
    private JTable tablaClientes;
    private JTable tablaHistorial;
    private JTabbedPane tabbedPane;
    private JTextField txtBuscarCliente;
    private JTextField txtBuscarHistorial;
    private JComboBox<String> comboEstatusCliente;
    private JComboBox<String> comboEstatusHistorial;
    private JComboBox<String> comboIDClientes;  // Nuevo: combo de IDs de clientes
    private JCheckBox chkFrecuentes;
    private JButton btnCrearCliente;
    private JLabel lblTotalClientes;
    private JLabel lblTituloEstadisticas;
    private JLabel lblJuegosPendientes;
    private PnlTotalJuego pnlTotalJuego;
    private PnlResumenCliente pnlResumenCliente;
    private List<ClienteInfo> clientesActuales;  // Almacena clientes para filtrado
    private boolean actualizandoComboClientes;

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
    panel.setBounds(0, 0, 950, 50); 
    
    // Etiqueta y combo de IDs de clientes (NUEVO)
    JLabel lblSeleccionarID = new JLabel("Seleccionar por ID:");
    lblSeleccionarID.setBounds(10, 15, 110, 30);
    lblSeleccionarID.setFont(new Font("Arial", Font.PLAIN, 11));
    panel.add(lblSeleccionarID);
    
    comboIDClientes = new JComboBox<>();
    comboIDClientes.setBounds(120, 15, 120, 30);
    cargarIDsClientes();  // Cargar IDs desde la BD
    comboIDClientes.addActionListener(e -> manejarSeleccionID());
    panel.add(comboIDClientes);
    
    txtBuscarCliente = new JTextField(PLACEHOLDER_BUSQUEDA_CLIENTE);
    txtBuscarCliente.setBounds(250, 15, 200, 30);
    txtBuscarCliente.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
    txtBuscarCliente.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (PLACEHOLDER_BUSQUEDA_CLIENTE.equals(txtBuscarCliente.getText())) {
                txtBuscarCliente.setText("");
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (txtBuscarCliente.getText().trim().isEmpty()) {
                txtBuscarCliente.setText(PLACEHOLDER_BUSQUEDA_CLIENTE);
            }
        }
    });
    txtBuscarCliente.getDocument().addDocumentListener(new DocumentListener() {
        @Override public void insertUpdate(DocumentEvent e) { aplicarFiltrosClientes(); }
        @Override public void removeUpdate(DocumentEvent e) { aplicarFiltrosClientes(); }
        @Override public void changedUpdate(DocumentEvent e) { aplicarFiltrosClientes(); }
    });
    panel.add(txtBuscarCliente);
    
    JLabel lblEstatus = new JLabel("Estatus:");
    lblEstatus.setBounds(470, 15, 60, 30);
    lblEstatus.setFont(new Font("Arial", Font.PLAIN, 12));
    panel.add(lblEstatus);
    
    comboEstatusCliente = new JComboBox<>(new String[]{"Todos", "Activo", "Inactivo", "Suspendido"});
    comboEstatusCliente.setBounds(530, 15, 110, 30);
    comboEstatusCliente.addActionListener(e -> aplicarFiltrosClientes());
    panel.add(comboEstatusCliente);
    
    chkFrecuentes = new JCheckBox("Solo frecuentes");
    chkFrecuentes.setBounds(650, 15, 130, 30);
    chkFrecuentes.setBackground(Ventana.CARD_WHITE);
    chkFrecuentes.addActionListener(e -> aplicarFiltrosClientes());
    panel.add(chkFrecuentes);
    
    btnCrearCliente = new JButton("+ Crear cliente");
    btnCrearCliente.setBounds(780, 15, 150, 30);
    btnCrearCliente.setBackground(Ventana.ACCENT_RED);
    btnCrearCliente.setForeground(Color.WHITE);
    btnCrearCliente.setFont(new Font("Arial", Font.BOLD, 12));
    btnCrearCliente.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnCrearCliente.addActionListener(e -> {
        parent.getHost().setOscurecer(true);
        new DlgFormCliente(parent.getHost(), this).setVisible(true);
    });
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
        pnlResumenCliente = new PnlResumenCliente(parent, this);
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
    
    private void mostrarPanelResumenCliente(String clienteId) {
        if (pnlTotalJuego == null || pnlResumenCliente == null) {
            return;
        }
        
        // Validar que haya un cliente seleccionado
        if (clienteId == null || clienteId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un cliente primero.", "Sin cliente seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener el cliente desde la BD
        ClienteInfo cliente = ClienteController.obtenerClientePorId(clienteId);
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la información del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Actualizar el panel de resumen con los datos del cliente
        pnlResumenCliente.actualizarConDatos(cliente);
        
        // Mostrar el panel de resumen y ocultar el de total juegos
        pnlTotalJuego.setVisible(false);
        pnlResumenCliente.setVisible(true);
        lblTituloEstadisticas.setText("Resumen del cliente");
    }

    private void abrirEditarCliente(String clienteId) {
        ClienteInfo cliente = ClienteController.obtenerClientePorId(clienteId);
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la informacion del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DlgEdicionCliente dlgEditar = new DlgEdicionCliente(parent.getHost(), cliente.getId(), cliente.getNombres(), cliente.getPrimerApellido(), cliente.getSegundoApellido(), cliente.getEmail(), cliente.getTelefono(), cliente.getFechaNacimiento(), this);
        parent.getHost().setOscurecer(true);
        dlgEditar.setVisible(true);
    }
    
    /**
     * Carga dinámicamente los IDs de clientes desde la BD al comboBox.
     * Se llama al inicializar y al refrescar la tabla.
     */
    private void cargarIDsClientes() {
        if (comboIDClientes == null) return;
        
        // Obtener lista de clientes desde BD
        if (clientesActuales == null) {
            cargarClientesDesdeBD();
        }
        
        // Limpiar combo y agregar opción inicial
        actualizandoComboClientes = true;
        comboIDClientes.removeAllItems();
        comboIDClientes.addItem("-- Seleccionar cliente --");
        
        // Agregar IDs de clientes
        if (clientesActuales != null && !clientesActuales.isEmpty()) {
            for (ClienteInfo cliente : clientesActuales) {
                if (cliente != null && cliente.getId() != null) {
                    comboIDClientes.addItem(cliente.getId());
                }
            }
        }
        actualizandoComboClientes = false;
        
        System.out.println("✓ IDs de clientes cargados: " + (clientesActuales != null ? clientesActuales.size() : 0));
    }
    
    /**
     * Maneja la selección de un cliente en el comboBox de IDs.
     * Filtra la tabla y actualiza el panel de resumen.
     */
    private void manejarSeleccionID() {
        if (actualizandoComboClientes) return;
        if (comboIDClientes == null || tablaClientes == null) return;
        
        Object seleccionado = comboIDClientes.getSelectedItem();
        
        // Validar que se haya seleccionado un ID válido
        if (seleccionado == null || seleccionado.toString().startsWith("--")) {
            // Mostrar todos los clientes si no hay selección
            aplicarFiltrosClientes();
            mostrarPanelTotalJuego();
            return;
        }
        
        String clienteIdSeleccionado = seleccionado.toString();
        
        // Buscar el cliente en la lista cargada
        ClienteInfo clienteSeleccionado = null;
        if (clientesActuales != null) {
            for (ClienteInfo cliente : clientesActuales) {
                if (cliente != null && clienteIdSeleccionado.equals(cliente.getId())) {
                    clienteSeleccionado = cliente;
                    break;
                }
            }
        }
        
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Filtrar la tabla para mostrar solo este cliente
        Object[][] datosCliente = new Object[1][6];
        datosCliente[0][0] = clienteSeleccionado.getId();
        datosCliente[0][1] = clienteSeleccionado.getNombre();
        datosCliente[0][2] = clienteSeleccionado.getEmail();
        datosCliente[0][3] = clienteSeleccionado.getEstatus();
        datosCliente[0][4] = clienteSeleccionado.getNivel();
        datosCliente[0][5] = "...";
        
        // Actualizar modelo de la tabla
        DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
        modelo.setDataVector(datosCliente, new String[]{"ID", "Nombre completo", "Email", "Estatus", "Nivel", "Acciones"});
        
        // Reconfigurar renderizador de acciones
        tablaClientes.getColumnModel().getColumn(5).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                
                JButton btnVer = new JButton("👁");
                btnVer.setBackground(Color.WHITE);
                btnVer.setBorder(BorderFactory.createEmptyBorder());
                btnVer.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnVer.setToolTipText("Ver detalles");
                
                JButton btnEditar = new JButton("✏");
                btnEditar.setBackground(Color.WHITE);
                btnEditar.setBorder(BorderFactory.createEmptyBorder());
                btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnEditar.setToolTipText("Editar cliente");
                
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
        
        // Actualizar etiqueta con información filtrada
        if (lblTotalClientes != null) {
            lblTotalClientes.setText("Cliente seleccionado (1/" + (clientesActuales != null ? clientesActuales.size() : 0) + ")");
        }
        
        // Mostrar resumen del cliente seleccionado
        mostrarPanelResumenCliente(clienteIdSeleccionado);
        
        System.out.println("✓ Cliente filtrado: " + clienteIdSeleccionado);
    }
    
    /**
     * Refresca la tabla de clientes con datos actuales desde la BD.
     * Se llama después de CRUD (agregar, editar, eliminar).
     */
    public void refrescarTabla() {
        if (tablaClientes == null) return;
        
        // Obtener datos nuevos desde BD
        cargarClientesDesdeBD();
        Object[][] datos = convertirClientesAArray(clientesActuales);
        String[] columnas = COLUMNAS_CLIENTES;
        
        // Actualizar el modelo de datos existente
        DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
        modelo.setDataVector(datos, columnas);
        
        // Actualizar etiqueta con cantidad real
        if (lblTotalClientes != null) {
            lblTotalClientes.setText("Lista de clientes (" + clientesActuales.size() + ")");
        }
        
        // Refrescar el comboBox de IDs
        cargarIDsClientes();
        aplicarFiltrosClientes();
        
        // Reconfigurar renderizador de acciones (se pierde al actualizar modelo)
        tablaClientes.getColumnModel().getColumn(5).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                
                JButton btnVer = new JButton("👁");
                btnVer.setBackground(Color.WHITE);
                btnVer.setBorder(BorderFactory.createEmptyBorder());
                btnVer.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnVer.setToolTipText("Ver detalles");
                
                JButton btnEditar = new JButton("✏");
                btnEditar.setBackground(Color.WHITE);
                btnEditar.setBorder(BorderFactory.createEmptyBorder());
                btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnEditar.setToolTipText("Editar cliente");
                
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
        
        System.out.println("✓ Tabla de clientes refrescada");
    }
    
    /**
     * Muestra diálogo de confirmación y elimina un cliente.
     * @param clienteId el ID del cliente a eliminar
     * @param nombreCliente el nombre del cliente (para mostrar en el diálogo)
     */
    public void confirmarEliminarCliente(String clienteId, String nombreCliente) {
        if (clienteId == null || clienteId.trim().isEmpty() || clienteId.startsWith("N/A")) {
            JOptionPane.showMessageDialog(this, "No hay un cliente valido seleccionado.", "Sin cliente seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que deseas eliminar a " + nombreCliente + "?\n\nEsta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (respuesta == JOptionPane.YES_OPTION) {
            // Llamar al controlador para eliminar
            boolean exito = ClienteController.eliminarCliente(clienteId);
            
            if (exito) {
                JOptionPane.showMessageDialog(
                    this,
                    "Cliente eliminado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
                // Refrescar tabla
                refrescarTabla();
                mostrarPanelTotalJuego();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar el cliente. Por favor, intenta de nuevo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
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
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(0, 0, 630, 380);
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 12));
        tabbedPane.setBackground(Color.WHITE);
        JPanel panelRentas = createPanelContenidoHistorial();
        tabbedPane.addTab("Historial de Rentas", panelRentas);
        JPanel panelCompras = new JPanel();
        panelCompras.setBackground(Color.WHITE);
        panelCompras.add(new JLabel("Contenido de Historial de Compras"));
        tabbedPane.addTab("Historial de Compras", panelCompras);
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
    panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
    
    txtBuscarHistorial = new JTextField("Buscar por ID, nombre o fecha...");
    txtBuscarHistorial.setBounds(0, 10, 250, 30);
    txtBuscarHistorial.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
    txtBuscarHistorial.setFont(new Font("Arial", Font.PLAIN, 12));
    panel.add(txtBuscarHistorial);
    
    JLabel lblEstatusHist = new JLabel("Estatus:");
    lblEstatusHist.setBounds(270, 10, 60, 30);
    lblEstatusHist.setFont(new Font("Arial", Font.PLAIN, 12));
    panel.add(lblEstatusHist);
    
    comboEstatusHistorial = new JComboBox<>(new String[]{"Todos", "Devuelto", "Pendiente", "Vencido"});
    comboEstatusHistorial.setBounds(330, 10, 110, 30);
    comboEstatusHistorial.setBackground(Color.WHITE);
    panel.add(comboEstatusHistorial);
    
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
        
        // Obtener datos reales desde la BD a través del Controller
        cargarClientesDesdeBD();
        Object[][] datos = convertirClientesAArray(clientesActuales);
        
        // Actualizar etiqueta con cantidad real
        if (lblTotalClientes != null) {
            lblTotalClientes.setText("Lista de clientes (" + clientesActuales.size() + ")");
        }
        
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
                int col = tablaClientes.columnAtPoint(e.getPoint());
                if (row >= 0) {
                    // Obtener ID del cliente de la fila (siempre en la columna 0)
                    String clienteId = (String) tablaClientes.getValueAt(row, 0);
                    String nombreCompleto = (String) tablaClientes.getValueAt(row, 1);
                    
                    // Si el clic es en la columna de acciones (columna 5)
                    if (col == 5) {
                        // Detectar posición x del clic dentro de la celda para saber qué botón
                        Rectangle cellRect = tablaClientes.getCellRect(row, col, true);
                        int posX = e.getX() - cellRect.x;
                        
                        // Botones: ver (x < 25), editar (25-50), eliminar (> 50)
                        if (posX < 25) {
                            // Botón ver - mostrar resumen con datos del cliente
                            mostrarPanelResumenCliente(clienteId);
                        } else if (posX > 20 && posX < 50) {
                            // Botón editar
                            abrirEditarCliente(clienteId);
                        } else if (posX > 50) {
                            // Botón eliminar
                            confirmarEliminarCliente(clienteId, nombreCompleto);
                        }
                    } else {
                        // Clic en cualquier otra columna - mostrar resumen
                        mostrarPanelResumenCliente(clienteId);
                    }
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

    /**
     * Convierte una lista de ClienteInfo a un array de objetos para JTable.
     * 
     * @param clientes lista de ClienteInfo desde la BD
     * @return array 2D con formato: {ID, Nombre, Email, Estatus, Nivel, "..."}
     */
    private Object[][] convertirClientesAArray(List<ClienteInfo> clientes) {
        if (clientes == null || clientes.isEmpty()) {
            return new Object[0][6];  // Array vacío
        }

        Object[][] datos = new Object[clientes.size()][6];
        
        for (int i = 0; i < clientes.size(); i++) {
            ClienteInfo cliente = clientes.get(i);
            datos[i][0] = cliente.getId();              // ID
            datos[i][1] = cliente.getNombre();           // Nombre completo
            datos[i][2] = cliente.getEmail();            // Email
            datos[i][3] = cliente.getEstatus();          // Estatus
            datos[i][4] = cliente.getNivel();            // Nivel fidelidad
            datos[i][5] = "...";                         // Botones de acciones
        }
        
        return datos;
    }

    private void cargarClientesDesdeBD() {
        clientesActuales = ClienteController.traerClientesDeBD();
        if (clientesActuales == null) {
            clientesActuales = new ArrayList<>();
        }
    }

    private void aplicarFiltrosClientes() {
        if (tablaClientes == null || clientesActuales == null) return;

        String textoBusqueda = obtenerTextoBusquedaCliente();
        String estatus = comboEstatusCliente != null ? String.valueOf(comboEstatusCliente.getSelectedItem()) : "Todos";
        boolean soloFrecuentes = chkFrecuentes != null && chkFrecuentes.isSelected();
        String idSeleccionado = obtenerIdSeleccionado();

        List<ClienteInfo> filtrados = new ArrayList<>();
        for (ClienteInfo cliente : clientesActuales) {
            if (cliente == null) continue;

            boolean coincideId = idSeleccionado.isEmpty() || idSeleccionado.equals(cliente.getId());
            boolean coincideBusqueda = textoBusqueda.isEmpty()
                    || contiene(cliente.getNombres(), textoBusqueda)
                    || contiene(cliente.getPrimerApellido(), textoBusqueda)
                    || contiene(cliente.getSegundoApellido(), textoBusqueda)
                    || contiene(cliente.getEmail(), textoBusqueda);
            boolean coincideEstatus = "Todos".equals(estatus) || estatus.equals(cliente.getEstatus());
            boolean coincideFrecuencia = !soloFrecuentes || cliente.isFrecuente();

            if (coincideId && coincideBusqueda && coincideEstatus && coincideFrecuencia) {
                filtrados.add(cliente);
            }
        }

        actualizarTablaClientes(filtrados);
    }

    private void actualizarTablaClientes(List<ClienteInfo> clientes) {
        DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
        modelo.setDataVector(convertirClientesAArray(clientes), COLUMNAS_CLIENTES);

        if (tablaClientes.getColumnModel().getColumnCount() > 5) {
            tablaClientes.getColumnModel().getColumn(5).setCellRenderer(crearRenderizadorAcciones());
        }

        if (lblTotalClientes != null) {
            int total = clientesActuales != null ? clientesActuales.size() : 0;
            int visibles = clientes != null ? clientes.size() : 0;
            lblTotalClientes.setText(visibles == total
                    ? "Lista de clientes (" + total + ")"
                    : "Lista de clientes (" + visibles + "/" + total + ")");
        }
    }

    private String obtenerTextoBusquedaCliente() {
        if (txtBuscarCliente == null) return "";
        String texto = txtBuscarCliente.getText();
        if (texto == null || PLACEHOLDER_BUSQUEDA_CLIENTE.equals(texto)) {
            return "";
        }
        return texto.trim().toLowerCase(Locale.ROOT);
    }

    private String obtenerIdSeleccionado() {
        if (comboIDClientes == null || comboIDClientes.getSelectedItem() == null) {
            return "";
        }
        String seleccionado = comboIDClientes.getSelectedItem().toString();
        return seleccionado.startsWith("--") ? "" : seleccionado;
    }

    private boolean contiene(String valor, String busqueda) {
        return valor != null && valor.toLowerCase(Locale.ROOT).contains(busqueda);
    }

    private TableCellRenderer crearRenderizadorAcciones() {
        return new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);

                JButton btnVer = new JButton("...");
                btnVer.setBackground(Color.WHITE);
                btnVer.setBorder(BorderFactory.createEmptyBorder());
                btnVer.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnVer.setToolTipText("Ver detalles");

                JButton btnEditar = new JButton("E");
                btnEditar.setBackground(Color.WHITE);
                btnEditar.setBorder(BorderFactory.createEmptyBorder());
                btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnEditar.setToolTipText("Editar cliente");

                JButton btnEliminar = new JButton("X");
                btnEliminar.setBackground(Color.WHITE);
                btnEliminar.setBorder(BorderFactory.createEmptyBorder());
                btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnEliminar.setToolTipText("Eliminar cliente");

                panel.add(btnVer);
                panel.add(btnEditar);
                panel.add(btnEliminar);

                return panel;
            }
        };
    }
}
