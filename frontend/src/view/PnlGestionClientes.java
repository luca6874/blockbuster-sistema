package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.controller.ClienteController;
import frontend.src.controller.OperacionController;
import frontend.src.model.ClienteInfo;
import frontend.src.service.ImageManager;

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
    private static final String[] COLUMNAS_HISTORIAL = {"Cliente", "Juego", "Fecha renta", "Fecha dev. (est)", "Estatus", "Accion", "ID cliente"};
    private static final String[] COLUMNAS_COMPRAS = {"Juego", "Cliente", "Fecha de compra", "Inventario actual", "Precio"};
    private static final String[] COLUMNAS_DESCUENTOS = {"Juego", "Fecha de uso", "Cliente", "Código de descuento", "% descontado"};
    private static final String PLACEHOLDER_BUSQUEDA_CLIENTE = "Buscar por nombre o email...";
    private static final String PLACEHOLDER_BUSQUEDA_HISTORIAL = "Buscar por ID, nombre o fecha...";
    private static final String PLACEHOLDER_BUSQUEDA_COMPRAS = "Buscar por juego, cliente o fecha...";
    private static final String PLACEHOLDER_BUSQUEDA_DESCUENTOS = "Buscar por juego, cliente o código...";
    private static final int IDX_HIST_ID = 0;
    private static final int IDX_HIST_CLIENTE = 1;
    private static final int IDX_HIST_JUEGO = 2;
    private static final int IDX_HIST_FECHA_RENTA = 3;
    private static final int IDX_HIST_FECHA_DEVOLUCION = 4;
    private static final int IDX_HIST_ESTATUS = 5;
    private static final int IDX_HIST_CLIENTE_ID = 6;
    private static final int IDX_HIST_PLATAFORMA = 7;
    private static final int IDX_HIST_IMAGEN = 8;
    private static final int IDX_DESC_JUEGO = 0;
    private static final int IDX_DESC_FECHA = 1;
    private static final int IDX_DESC_CLIENTE = 2;
    private static final int IDX_DESC_CODIGO = 3;
    private static final int IDX_DESC_PORCENTAJE = 4;

    private final ViewDashboard parent;
    private JTable tablaClientes;
    private JTable tablaHistorial;
    private JTable tablaCompras;
    private JTable tablaDescuentos;
    private JTabbedPane tabbedPane;
    private JTextField txtBuscarCliente;
    private JTextField txtBuscarHistorial;
    private JTextField txtBuscarCompras;
    private JTextField txtBuscarDescuentos;
    private JComboBox<String> comboEstatusCliente;
    private JComboBox<String> comboEstatusHistorial;
    private JComboBox<String> comboIDClientes;  // Nuevo: combo de IDs de clientes
    private JCheckBox chkFrecuentes;
    private JButton btnCrearCliente;
    private JLabel lblTotalClientes;
    private JLabel lblTituloEstadisticas;
    private PnlTotalJuego pnlTotalJuego;
    private PnlResumenCliente pnlResumenCliente;
    private List<ClienteInfo> clientesActuales;  // Almacena clientes para filtrado
    private List<String[]> rentasHistorial = new ArrayList<>();
    private List<String[]> rentasHistorialFiltradas = new ArrayList<>();
    private List<String[]> comprasHistorial = new ArrayList<>();
    private List<String[]> comprasHistorialFiltradas = new ArrayList<>();
    private boolean actualizandoComboClientes;
    private List<String[]> descuentosHistorial = new ArrayList<>();
    private List<String[]> descuentosHistorialFiltrados = new ArrayList<>();

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
            JOptionPane.showMessageDialog(this, "No se pudo cargar la información del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
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
            String exito = ClienteController.eliminarCliente(clienteId);
            
            if ("OK".equals(exito)) {
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
                  "Este cliente no puede eliminarse porque tiene operaciones registradas.",
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
        JPanel panelCompras = createPanelContenidoCompras();
        tabbedPane.addTab("Historial de Compras", panelCompras);
        JPanel panelDescuentos = createPanelContenidoDescuentos();
        tabbedPane.addTab("Descuentos Aplicados", panelDescuentos);
        panel.add(tabbedPane);
        return panel;
    }
    
    private JPanel createPanelContenidoHistorial() {
    JPanel panel = new JPanel(null);
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
    
    txtBuscarHistorial = new JTextField(PLACEHOLDER_BUSQUEDA_HISTORIAL);
    txtBuscarHistorial.setBounds(0, 10, 250, 30);
    txtBuscarHistorial.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
    txtBuscarHistorial.setFont(new Font("Arial", Font.PLAIN, 12));
    txtBuscarHistorial.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (PLACEHOLDER_BUSQUEDA_HISTORIAL.equals(txtBuscarHistorial.getText())) {
                txtBuscarHistorial.setText("");
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (txtBuscarHistorial.getText().trim().isEmpty()) {
                txtBuscarHistorial.setText(PLACEHOLDER_BUSQUEDA_HISTORIAL);
            }
        }
    });
    txtBuscarHistorial.getDocument().addDocumentListener(new DocumentListener() {
        @Override public void insertUpdate(DocumentEvent e) { aplicarFiltrosHistorial(); }
        @Override public void removeUpdate(DocumentEvent e) { aplicarFiltrosHistorial(); }
        @Override public void changedUpdate(DocumentEvent e) { aplicarFiltrosHistorial(); }
    });
    panel.add(txtBuscarHistorial);
    
    JLabel lblEstatusHist = new JLabel("Estatus:");
    lblEstatusHist.setBounds(270, 10, 60, 30);
    lblEstatusHist.setFont(new Font("Arial", Font.PLAIN, 12));
    panel.add(lblEstatusHist);
    
    comboEstatusHistorial = new JComboBox<>(new String[]{"Todos", "Devuelto", "Pendiente", "Retrasado", "Vencido"});
    comboEstatusHistorial.setBounds(330, 10, 110, 30);
    comboEstatusHistorial.setBackground(Color.WHITE);
    comboEstatusHistorial.addActionListener(e -> aplicarFiltrosHistorial());
    panel.add(comboEstatusHistorial);
    
    initTablaHistorial();
    
    JScrollPane scroll = new JScrollPane(tablaHistorial);
    scroll.setBounds(0, 50, 610, 280);
    scroll.setBorder(new LineBorder(new Color(200, 200, 200), 1));
    scroll.getViewport().setBackground(Color.WHITE);
    panel.add(scroll);
    
    return panel;
}

    private JPanel createPanelContenidoCompras() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        txtBuscarCompras = new JTextField(PLACEHOLDER_BUSQUEDA_COMPRAS);
        txtBuscarCompras.setBounds(0, 10, 300, 30);
        txtBuscarCompras.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        txtBuscarCompras.setFont(new Font("Arial", Font.PLAIN, 12));
        txtBuscarCompras.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (PLACEHOLDER_BUSQUEDA_COMPRAS.equals(txtBuscarCompras.getText())) {
                    txtBuscarCompras.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtBuscarCompras.getText().trim().isEmpty()) {
                    txtBuscarCompras.setText(PLACEHOLDER_BUSQUEDA_COMPRAS);
                }
            }
        });
        txtBuscarCompras.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltrosCompras(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltrosCompras(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltrosCompras(); }
        });
        panel.add(txtBuscarCompras);

        initTablaCompras();

        JScrollPane scroll = new JScrollPane(tablaCompras);
        scroll.setBounds(0, 50, 610, 280);
        scroll.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll);

        return panel;
    }
    
    private JPanel createPanelContenidoDescuentos() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        txtBuscarDescuentos = new JTextField(PLACEHOLDER_BUSQUEDA_DESCUENTOS);
        txtBuscarDescuentos.setBounds(0, 10, 300, 30);
        txtBuscarDescuentos.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        txtBuscarDescuentos.setFont(new Font("Arial", Font.PLAIN, 12));
        txtBuscarDescuentos.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (PLACEHOLDER_BUSQUEDA_DESCUENTOS.equals(txtBuscarDescuentos.getText())) {
                    txtBuscarDescuentos.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtBuscarDescuentos.getText().trim().isEmpty()) {
                    txtBuscarDescuentos.setText(PLACEHOLDER_BUSQUEDA_DESCUENTOS);
                }
            }
        });
        txtBuscarDescuentos.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltrosDescuentos(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltrosDescuentos(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltrosDescuentos(); }
        });
        panel.add(txtBuscarDescuentos);

        initTablaDescuentos();

        JScrollPane scroll = new JScrollPane(tablaDescuentos);
        scroll.setBounds(0, 50, 610, 280);
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

        List<String[]> rentas = rentasHistorial != null ? rentasHistorial : OperacionController.obtenerHistorialRentas();
        if (rentas == null || rentas.isEmpty()) {
            JLabel lblVacio = new JLabel("Sin rentas registradas", SwingConstants.CENTER);
            lblVacio.setFont(new Font("Arial", Font.PLAIN, 12));
            lblVacio.setForeground(Color.GRAY);
            panel.add(lblVacio);
            return panel;
        }

        int limite = Math.min(rentas.size(), 8);
        for (int i = 0; i < limite; i++) {
            String[] renta = rentas.get(i);
            String plataforma = valorHistorial(renta, IDX_HIST_PLATAFORMA);
            String titulo = valorHistorial(renta, IDX_HIST_JUEGO) + (plataforma.trim().isEmpty() ? "" : " (" + plataforma + ")");
            JPanel itemJuego = createItemJuegoVisual(
                    titulo,
                    valorHistorial(renta, IDX_HIST_IMAGEN),
                    valorHistorial(renta, IDX_HIST_FECHA_DEVOLUCION),
                    valorHistorial(renta, IDX_HIST_ESTATUS),
                    valorHistorial(renta, IDX_HIST_CLIENTE_ID)
            );
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
        ImageIcon icon = ImageManager.cargarImagenPreview(imagen, 50, 60);
        if (icon != null) {
            lblCaratula.setIcon(icon);
        } else {
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
        cargarHistorialRentasDesdeBD();
        
        DefaultTableModel modelo = new DefaultTableModel(convertirRentasAArray(rentasHistorial), COLUMNAS_HISTORIAL) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tablaHistorial = new JTable(modelo);
        tablaHistorial.setRowHeight(30);
        tablaHistorial.setSelectionBackground(new Color(152, 33, 54, 40));
        tablaHistorial.setSelectionForeground(Color.BLACK);
        tablaHistorial.setShowVerticalLines(false);
        tablaHistorial.setGridColor(new Color(235, 235, 235));
        tablaHistorial.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaHistorial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablaHistorial.rowAtPoint(e.getPoint());
                int col = tablaHistorial.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 5) {
                    marcarRentaSeleccionadaComoDevuelta(row);
                }
            }
        });
        
        // Renderizador para la columna de estatus
        tablaHistorial.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value != null ? value.toString() : "");
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

        tablaHistorial.getColumnModel().getColumn(5).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JButton boton = new JButton(value != null ? value.toString() : "");
                boton.setFont(new Font("Arial", Font.BOLD, 10));
                boton.setFocusPainted(false);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                boton.setBorder(new LineBorder(new Color(170, 170, 170), 1, true));
                boolean habilitado = value != null && !value.toString().trim().isEmpty();
                boton.setEnabled(habilitado);
                boton.setBackground(habilitado ? Color.WHITE : new Color(245, 245, 245));
                boton.setForeground(habilitado ? Ventana.ACCENT_RED : Color.GRAY);
                return boton;
            }
        });

        configurarColumnasHistorial();
        
        // Estilo del encabezado
        JTableHeader header = tablaHistorial.getTableHeader();
        header.setBackground(Ventana.MAROON_BG);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 11));
        header.setPreferredSize(new Dimension(0, 30));
        header.setReorderingAllowed(false);
    }

    private void initTablaCompras() {
        cargarHistorialComprasDesdeBD();

        DefaultTableModel modelo = new DefaultTableModel(convertirComprasAArray(comprasHistorial), COLUMNAS_COMPRAS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaCompras = new JTable(modelo);
        tablaCompras.setRowHeight(30);
        tablaCompras.setSelectionBackground(new Color(152, 33, 54, 40));
        tablaCompras.setSelectionForeground(Color.BLACK);
        tablaCompras.setShowVerticalLines(false);
        tablaCompras.setGridColor(new Color(235, 235, 235));
        tablaCompras.setFont(new Font("Arial", Font.PLAIN, 11));
        configurarColumnasCompras();

        JTableHeader header = tablaCompras.getTableHeader();
        header.setBackground(Ventana.MAROON_BG);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 11));
        header.setPreferredSize(new Dimension(0, 30));
        header.setReorderingAllowed(false);
    }

    private void initTablaDescuentos() {
        cargarHistorialDescuentosDesdeBD();

        DefaultTableModel modelo = new DefaultTableModel(convertirDescuentosAArray(descuentosHistorial), COLUMNAS_DESCUENTOS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaDescuentos = new JTable(modelo);
        tablaDescuentos.setRowHeight(30);
        tablaDescuentos.setSelectionBackground(new Color(152, 33, 54, 40));
        tablaDescuentos.setSelectionForeground(Color.BLACK);
        tablaDescuentos.setShowVerticalLines(false);
        tablaDescuentos.setGridColor(new Color(235, 235, 235));
        tablaDescuentos.setFont(new Font("Arial", Font.PLAIN, 11));
        configurarColumnasDescuentos();

        JTableHeader header = tablaDescuentos.getTableHeader();
        header.setBackground(Ventana.MAROON_BG);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 11));
        header.setPreferredSize(new Dimension(0, 30));
        header.setReorderingAllowed(false);
    }

    private void cargarHistorialRentasDesdeBD() {
        rentasHistorial = OperacionController.obtenerHistorialRentas();
        if (rentasHistorial == null) {
            rentasHistorial = new ArrayList<>();
        }
        rentasHistorialFiltradas = new ArrayList<>(rentasHistorial);
    }

    private void cargarHistorialComprasDesdeBD() {
        comprasHistorial = OperacionController.obtenerHistorialCompras();
        if (comprasHistorial == null) {
            comprasHistorial = new ArrayList<>();
        }
        comprasHistorialFiltradas = new ArrayList<>(comprasHistorial);
    }

    private void cargarHistorialDescuentosDesdeBD() {
        descuentosHistorial = OperacionController.obtenerHistorialDescuentos();
        if (descuentosHistorial == null) {
            descuentosHistorial = new ArrayList<>();
        }
        descuentosHistorialFiltrados = new ArrayList<>(descuentosHistorial);
    }

    private void configurarColumnasHistorial() {
        tablaHistorial.getColumnModel().getColumn(0).setPreferredWidth(115);
        tablaHistorial.getColumnModel().getColumn(1).setPreferredWidth(135);
        tablaHistorial.getColumnModel().getColumn(2).setPreferredWidth(78);
        tablaHistorial.getColumnModel().getColumn(3).setPreferredWidth(88);
        tablaHistorial.getColumnModel().getColumn(4).setPreferredWidth(75);
        tablaHistorial.getColumnModel().getColumn(5).setPreferredWidth(82);
        tablaHistorial.getColumnModel().getColumn(6).setPreferredWidth(72);
    }

    private void configurarColumnasCompras() {
        tablaCompras.getColumnModel().getColumn(0).setPreferredWidth(150);
        tablaCompras.getColumnModel().getColumn(1).setPreferredWidth(140);
        tablaCompras.getColumnModel().getColumn(2).setPreferredWidth(105);
        tablaCompras.getColumnModel().getColumn(3).setPreferredWidth(105);
        tablaCompras.getColumnModel().getColumn(4).setPreferredWidth(80);
    }

    private void configurarColumnasDescuentos() {
        tablaDescuentos.getColumnModel().getColumn(0).setPreferredWidth(140);
        tablaDescuentos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaDescuentos.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaDescuentos.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaDescuentos.getColumnModel().getColumn(4).setPreferredWidth(100);
    }

    private Object[][] convertirRentasAArray(List<String[]> rentas) {
        if (rentas == null || rentas.isEmpty()) {
            return new Object[0][COLUMNAS_HISTORIAL.length];
        }

        Object[][] datos = new Object[rentas.size()][COLUMNAS_HISTORIAL.length];
        for (int i = 0; i < rentas.size(); i++) {
            String[] renta = rentas.get(i);
            String estatus = valorHistorial(renta, IDX_HIST_ESTATUS);
            datos[i][0] = valorHistorial(renta, IDX_HIST_CLIENTE);
            datos[i][1] = valorHistorial(renta, IDX_HIST_JUEGO);
            datos[i][2] = valorHistorial(renta, IDX_HIST_FECHA_RENTA);
            datos[i][3] = valorHistorial(renta, IDX_HIST_FECHA_DEVOLUCION);
            datos[i][4] = estatus;
            datos[i][5] = "Devuelto".equalsIgnoreCase(estatus) ? "" : "Marcar dev.";
            datos[i][6] = valorHistorial(renta, IDX_HIST_CLIENTE_ID);
        }
        return datos;
    }

    private Object[][] convertirComprasAArray(List<String[]> compras) {
        if (compras == null || compras.isEmpty()) {
            return new Object[0][COLUMNAS_COMPRAS.length];
        }

        Object[][] datos = new Object[compras.size()][COLUMNAS_COMPRAS.length];
        for (int i = 0; i < compras.size(); i++) {
            String[] compra = compras.get(i);
            datos[i][0] = valorHistorial(compra, 0);
            datos[i][1] = valorHistorial(compra, 1);
            datos[i][2] = valorHistorial(compra, 2);
            datos[i][3] = valorHistorial(compra, 3);
            datos[i][4] = valorHistorial(compra, 4);
        }
        return datos;
    }

    private Object[][] convertirDescuentosAArray(List<String[]> descuentos) {
        if (descuentos == null || descuentos.isEmpty()) {
            return new Object[0][COLUMNAS_DESCUENTOS.length];
        }

        Object[][] datos = new Object[descuentos.size()][COLUMNAS_DESCUENTOS.length];
        for (int i = 0; i < descuentos.size(); i++) {
            String[] descuento = descuentos.get(i);
            datos[i][0] = valorHistorial(descuento, IDX_DESC_JUEGO);
            datos[i][1] = valorHistorial(descuento, IDX_DESC_FECHA);
            datos[i][2] = valorHistorial(descuento, IDX_DESC_CLIENTE);
            datos[i][3] = valorHistorial(descuento, IDX_DESC_CODIGO);
            datos[i][4] = valorHistorial(descuento, IDX_DESC_PORCENTAJE);
        }
        return datos;
    }

    private void aplicarFiltrosHistorial() {
        if (tablaHistorial == null || rentasHistorial == null) return;

        String busqueda = obtenerTextoBusquedaHistorial();
        String estatusSeleccionado = comboEstatusHistorial != null ? String.valueOf(comboEstatusHistorial.getSelectedItem()) : "Todos";
        if ("Vencido".equalsIgnoreCase(estatusSeleccionado)) {
            estatusSeleccionado = "Retrasado";
        }

        rentasHistorialFiltradas = new ArrayList<>();
        for (String[] renta : rentasHistorial) {
            String estatus = valorHistorial(renta, IDX_HIST_ESTATUS);
            boolean coincideBusqueda = busqueda.isEmpty()
                    || contiene(valorHistorial(renta, IDX_HIST_CLIENTE_ID), busqueda)
                    || contiene(valorHistorial(renta, IDX_HIST_CLIENTE), busqueda)
                    || contiene(valorHistorial(renta, IDX_HIST_JUEGO), busqueda)
                    || contiene(valorHistorial(renta, IDX_HIST_FECHA_RENTA), busqueda)
                    || contiene(valorHistorial(renta, IDX_HIST_FECHA_DEVOLUCION), busqueda);
            boolean coincideEstatus = "Todos".equalsIgnoreCase(estatusSeleccionado)
                    || estatusSeleccionado.equalsIgnoreCase(estatus);

            if (coincideBusqueda && coincideEstatus) {
                rentasHistorialFiltradas.add(renta);
            }
        }

        actualizarTablaHistorial();
    }

    private void aplicarFiltrosCompras() {
        if (tablaCompras == null || comprasHistorial == null) return;

        String busqueda = obtenerTextoBusquedaCompras();
        comprasHistorialFiltradas = new ArrayList<>();
        for (String[] compra : comprasHistorial) {
            boolean coincideBusqueda = busqueda.isEmpty()
                    || contiene(valorHistorial(compra, 0), busqueda)
                    || contiene(valorHistorial(compra, 1), busqueda)
                    || contiene(valorHistorial(compra, 2), busqueda)
                    || contiene(valorHistorial(compra, 3), busqueda)
                    || contiene(valorHistorial(compra, 4), busqueda);

            if (coincideBusqueda) {
                comprasHistorialFiltradas.add(compra);
            }
        }

        actualizarTablaCompras();
    }

    private void aplicarFiltrosDescuentos() {
        if (tablaDescuentos == null || descuentosHistorial == null) return;

        String busqueda = obtenerTextoBusquedaDescuentos();
        descuentosHistorialFiltrados = new ArrayList<>();
        for (String[] descuento : descuentosHistorial) {
            boolean coincideBusqueda = busqueda.isEmpty()
                    || contiene(valorHistorial(descuento, IDX_DESC_JUEGO), busqueda)
                    || contiene(valorHistorial(descuento, IDX_DESC_CLIENTE), busqueda)
                    || contiene(valorHistorial(descuento, IDX_DESC_CODIGO), busqueda)
                    || contiene(valorHistorial(descuento, IDX_DESC_FECHA), busqueda);

            if (coincideBusqueda) {
                descuentosHistorialFiltrados.add(descuento);
            }
        }

        actualizarTablaDescuentos();
    }

    private void actualizarTablaHistorial() {
        DefaultTableModel modelo = (DefaultTableModel) tablaHistorial.getModel();
        modelo.setDataVector(convertirRentasAArray(rentasHistorialFiltradas), COLUMNAS_HISTORIAL);
        if (tablaHistorial.getColumnModel().getColumnCount() > 6) {
            tablaHistorial.getColumnModel().getColumn(4).setCellRenderer(crearRenderizadorEstatusHistorial());
            tablaHistorial.getColumnModel().getColumn(5).setCellRenderer(crearRenderizadorAccionHistorial());
            configurarColumnasHistorial();
        }
    }

    private void actualizarTablaCompras() {
        DefaultTableModel modelo = (DefaultTableModel) tablaCompras.getModel();
        modelo.setDataVector(convertirComprasAArray(comprasHistorialFiltradas), COLUMNAS_COMPRAS);
        if (tablaCompras.getColumnModel().getColumnCount() > 4) {
            configurarColumnasCompras();
        }
    }

    private void actualizarTablaDescuentos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaDescuentos.getModel();
        modelo.setDataVector(convertirDescuentosAArray(descuentosHistorialFiltrados), COLUMNAS_DESCUENTOS);
        if (tablaDescuentos.getColumnModel().getColumnCount() > 4) {
            configurarColumnasDescuentos();
        }
    }

    private void refrescarHistorialRentas() {
        cargarHistorialRentasDesdeBD();
        aplicarFiltrosHistorial();
    }

    private void marcarRentaSeleccionadaComoDevuelta(int fila) {
        if (fila < 0 || fila >= rentasHistorialFiltradas.size()) return;

        String[] renta = rentasHistorialFiltradas.get(fila);
        if ("Devuelto".equalsIgnoreCase(valorHistorial(renta, IDX_HIST_ESTATUS))) {
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "Marcar esta renta como devuelta y aumentar el stock del videojuego?",
                "Confirmar devolución",
                JOptionPane.YES_NO_OPTION
        );
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean actualizado = OperacionController.marcarRentaComoDevuelta(valorHistorial(renta, IDX_HIST_ID));
        if (actualizado) {
            refrescarHistorialRentas();
            JOptionPane.showMessageDialog(this, "Renta marcada como devuelta.", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo marcar la renta como devuelta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerTextoBusquedaHistorial() {
        if (txtBuscarHistorial == null) return "";
        String texto = txtBuscarHistorial.getText();
        if (texto == null || PLACEHOLDER_BUSQUEDA_HISTORIAL.equals(texto)) {
            return "";
        }
        return texto.trim().toLowerCase(Locale.ROOT);
    }

    private String obtenerTextoBusquedaCompras() {
        if (txtBuscarCompras == null) return "";
        String texto = txtBuscarCompras.getText();
        if (texto == null || PLACEHOLDER_BUSQUEDA_COMPRAS.equals(texto)) {
            return "";
        }
        return texto.trim().toLowerCase(Locale.ROOT);
    }

    private String obtenerTextoBusquedaDescuentos() {
        if (txtBuscarDescuentos == null) return "";
        String texto = txtBuscarDescuentos.getText();
        if (texto == null || PLACEHOLDER_BUSQUEDA_DESCUENTOS.equals(texto)) {
            return "";
        }
        return texto.trim().toLowerCase(Locale.ROOT);
    }

    private String valorHistorial(String[] datos, int indice) {
        if (datos == null || indice < 0 || indice >= datos.length || datos[indice] == null) {
            return "";
        }
        return datos[indice];
    }

    private TableCellRenderer crearRenderizadorEstatusHistorial() {
        return new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value != null ? value.toString() : "");
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
        };
    }

    private TableCellRenderer crearRenderizadorAccionHistorial() {
        return new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JButton boton = new JButton(value != null ? value.toString() : "");
                boton.setFont(new Font("Arial", Font.BOLD, 10));
                boton.setFocusPainted(false);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                boton.setBorder(new LineBorder(new Color(170, 170, 170), 1, true));
                boolean habilitado = value != null && !value.toString().trim().isEmpty();
                boton.setEnabled(habilitado);
                boton.setBackground(habilitado ? Color.WHITE : new Color(245, 245, 245));
                boton.setForeground(habilitado ? Ventana.ACCENT_RED : Color.GRAY);
                return boton;
            }
        };
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
        };
    }
}
