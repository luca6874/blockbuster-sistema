package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.model.ClienteInfo;

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
 * Selector visual de clientes para el modulo de nueva operacion.
 */
public class DlgSeleccionClienteOperacion extends JDialog {
    private static final Color BORDER = new Color(214, 214, 214);
    private static final Color MUTED = new Color(110, 110, 110);
    private static final Color MAROON = new Color(110, 60, 70);

    private final List<ClienteInfo> clientes = new ArrayList<>();
    private final List<ClienteInfo> clientesFiltrados = new ArrayList<>();

    private JTextField txtBuscar;
    private JComboBox<String> comboEstatus;
    private JCheckBox chkFrecuentes;
    private JTable tablaClientes;
    private JLabel lblTituloTabla;
    private JLabel lblResumenNombre;
    private JLabel lblResumenEmail;
    private JLabel lblResumenId;
    private JLabel lblResumenTelefono;
    private JLabel lblResumenAvatar;
    private ClienteInfo clienteSeleccionado;

    public DlgSeleccionClienteOperacion(Ventana parent, ClienteInfo clienteActual) {
        super(parent, true);
        setUndecorated(true);
        setSize(680, 300);
        setLocationRelativeTo(parent);

        inicializarClientes();
        if (clienteActual != null) {
            clienteSeleccionado = buscarClientePorId(clienteActual.getId());
        }

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new LineBorder(BORDER, 1, true));

        txtBuscar = new JTextField();
        txtBuscar.setBounds(20, 14, 260, 30);
        txtBuscar.setBorder(new LineBorder(BORDER, 1, true));
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 11));
        txtBuscar.setText("Buscar por ID, nombre o email...");
        txtBuscar.setForeground(new Color(150, 150, 150));
        txtBuscar.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if ("Buscar por ID, nombre o email...".equals(txtBuscar.getText())) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtBuscar.getText().trim().isEmpty()) {
                    txtBuscar.setText("Buscar por ID, nombre o email...");
                    txtBuscar.setForeground(new Color(150, 150, 150));
                }
            }
        });
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aplicarFiltros();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aplicarFiltros();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aplicarFiltros();
            }
        });
        mainPanel.add(txtBuscar);

        JLabel lblEstatus = new JLabel("Estatus:");
        lblEstatus.setBounds(305, 18, 48, 20);
        lblEstatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblEstatus.setForeground(Color.BLACK);
        mainPanel.add(lblEstatus);

        comboEstatus = new JComboBox<>(new String[]{"Todos", "Activo", "Inactivo", "Suspendido"});
        comboEstatus.setBounds(352, 14, 110, 30);
        comboEstatus.setFont(new Font("Arial", Font.PLAIN, 11));
        comboEstatus.setBackground(Color.WHITE);
        comboEstatus.setBorder(new LineBorder(BORDER, 1));
        comboEstatus.addActionListener(e -> aplicarFiltros());
        mainPanel.add(comboEstatus);

        chkFrecuentes = new JCheckBox("Solo frecuentes");
        chkFrecuentes.setBounds(490, 14, 130, 30);
        chkFrecuentes.setFont(new Font("Arial", Font.PLAIN, 11));
        chkFrecuentes.setBackground(Color.WHITE);
        chkFrecuentes.setFocusPainted(false);
        chkFrecuentes.addActionListener(e -> aplicarFiltros());
        mainPanel.add(chkFrecuentes);

        lblTituloTabla = new JLabel();
        lblTituloTabla.setBounds(20, 52, 180, 18);
        lblTituloTabla.setFont(new Font("Arial", Font.BOLD, 12));
        lblTituloTabla.setForeground(Color.BLACK);
        mainPanel.add(lblTituloTabla);

        initTablaClientes();
        JScrollPane scroll = new JScrollPane(tablaClientes);
        scroll.setBounds(20, 72, 440, 170);
        scroll.setBorder(new LineBorder(BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scroll);

        JPanel panelResumen = createResumenPanel();
        panelResumen.setBounds(475, 72, 185, 105);
        mainPanel.add(panelResumen);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBounds(480, 220, 85, 24);
        btnConfirmar.setBackground(MAROON);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.addActionListener(e -> confirmar());
        mainPanel.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(580, 220, 90, 24);
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(MAROON);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(new LineBorder(new Color(189, 150, 157), 1, true));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());
        mainPanel.add(btnCancelar);

        setContentPane(mainPanel);

        aplicarFiltros();
        if (clienteSeleccionado != null) {
            seleccionarClienteEnTabla(clienteSeleccionado.getId());
        } else if (!clientesFiltrados.isEmpty()) {
            tablaClientes.setRowSelectionInterval(0, 0);
            actualizarResumen(clientesFiltrados.get(0));
        }
    }

    public ClienteInfo getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    private void inicializarClientes() {
        clientes.add(new ClienteInfo("13587", "Luis Spinetta", "pescado@rabioso.com", "Activo", "Frecuente", true, "55 271 4314"));
        clientes.add(new ClienteInfo("34938", "Luca Reinaga", "f131@gmail.com", "Activo", "-", false, "55 882 1477"));
        clientes.add(new ClienteInfo("03491", "Leonardo Mata", "ojala@gmail.com", "Activo", "-", false, "55 413 5678"));
        clientes.add(new ClienteInfo("01990", "DArnell Aguilar", "denmedebaja@ladygaga.com", "Inactivo", "Frecuente", true, "55 904 1122"));
        clientes.add(new ClienteInfo("79412", "Iran Ruiz Medellin", "iruiz@bb.com", "Inactivo", "-", false, "55 118 3477"));
        clientes.add(new ClienteInfo("67924", "Charly Garcia", "saynomore@baby.com", "Activo", "-", false, "55 994 1266"));
        clientes.add(new ClienteInfo("73469", "Saul Hernandez", "celula@caifanes.com", "Activo", "-", false, "55 677 0832"));
        clientes.add(new ClienteInfo("80457", "Leon Larregui", "violateca@zoe.com", "Activo", "Frecuente", true, "55 625 9901"));
        clientes.add(new ClienteInfo("80634", "Tyrone Gonzales", "can@serbero.com", "Inactivo", "-", false, "55 300 4410"));
    }

    private JPanel createResumenPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(BORDER, 1, true));

        JLabel lblTitulo = new JLabel("Resumen del cliente");
        lblTitulo.setBounds(8, 6, 140, 16);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 11));
        lblTitulo.setForeground(Color.BLACK);
        panel.add(lblTitulo);

        lblResumenAvatar = new JLabel("", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 239, 239));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(215, 201, 201));
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblResumenAvatar.setBounds(18, 26, 42, 42);
        lblResumenAvatar.setOpaque(false);
        lblResumenAvatar.setFont(new Font("Arial", Font.BOLD, 12));
        lblResumenAvatar.setForeground(MAROON);
        panel.add(lblResumenAvatar);

        lblResumenNombre = new JLabel("Selecciona un cliente");
        lblResumenNombre.setBounds(70, 28, 105, 16);
        lblResumenNombre.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblResumenNombre);

        lblResumenEmail = new JLabel("--");
        lblResumenEmail.setBounds(70, 46, 105, 14);
        lblResumenEmail.setFont(new Font("Arial", Font.PLAIN, 9));
        lblResumenEmail.setForeground(MUTED);
        panel.add(lblResumenEmail);

        lblResumenId = new JLabel("ID: --");
        lblResumenId.setBounds(18, 74, 70, 14);
        lblResumenId.setFont(new Font("Arial", Font.PLAIN, 10));
        panel.add(lblResumenId);

        lblResumenTelefono = new JLabel("--", SwingConstants.RIGHT);
        lblResumenTelefono.setBounds(90, 74, 80, 14);
        lblResumenTelefono.setFont(new Font("Arial", Font.PLAIN, 10));
        panel.add(lblResumenTelefono);

        return panel;
    }

    private void initTablaClientes() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nombre completo", "Email", "Estatus", "Nivel"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaClientes = new JTable(model);
        tablaClientes.setRowHeight(20);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.setSelectionBackground(new Color(152, 33, 54, 40));
        tablaClientes.setSelectionForeground(Color.BLACK);
        tablaClientes.setShowVerticalLines(true);
        tablaClientes.setGridColor(new Color(230, 230, 230));
        tablaClientes.setFont(new Font("Arial", Font.PLAIN, 11));

        JTableHeader header = tablaClientes.getTableHeader();
        header.setBackground(new Color(244, 238, 240));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 11));
        header.setBorder(new LineBorder(BORDER, 1));
        header.setReorderingAllowed(false);

        tablaClientes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaClientes.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaClientes.getColumnModel().getColumn(2).setPreferredWidth(135);
        tablaClientes.getColumnModel().getColumn(3).setPreferredWidth(65);
        tablaClientes.getColumnModel().getColumn(4).setPreferredWidth(55);

        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tablaClientes.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < clientesFiltrados.size()) {
                    actualizarResumen(clientesFiltrados.get(selectedRow));
                }
            }
        });
    }

    private void aplicarFiltros() {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase(Locale.ROOT);
        if ("buscar por id, nombre o email...".equals(textoBusqueda)) {
            textoBusqueda = "";
        }

        String estatus = (String) comboEstatus.getSelectedItem();
        boolean soloFrecuentes = chkFrecuentes.isSelected();

        clientesFiltrados.clear();
        for (ClienteInfo cliente : clientes) {
            boolean coincideBusqueda = textoBusqueda.isEmpty()
                || cliente.getId().toLowerCase(Locale.ROOT).contains(textoBusqueda)
                || cliente.getNombre().toLowerCase(Locale.ROOT).contains(textoBusqueda)
                || cliente.getEmail().toLowerCase(Locale.ROOT).contains(textoBusqueda);

            boolean coincideEstatus = "Todos".equals(estatus) || cliente.getEstatus().equalsIgnoreCase(estatus);
            boolean coincideFrecuencia = !soloFrecuentes || cliente.isFrecuente();

            if (coincideBusqueda && coincideEstatus && coincideFrecuencia) {
                clientesFiltrados.add(cliente);
            }
        }

        cargarTabla();
    }

    private void cargarTabla() {
        DefaultTableModel model = (DefaultTableModel) tablaClientes.getModel();
        model.setRowCount(0);

        for (ClienteInfo cliente : clientesFiltrados) {
            model.addRow(new Object[]{
                cliente.getId(),
                cliente.getNombre(),
                cliente.getEmail(),
                cliente.getEstatus(),
                cliente.getNivel()
            });
        }

        lblTituloTabla.setText("Lista de clientes (" + clientesFiltrados.size() + ")");

        if (clientesFiltrados.isEmpty()) {
            limpiarResumen();
            return;
        }

        int rowToSelect = 0;
        if (clienteSeleccionado != null) {
            for (int i = 0; i < clientesFiltrados.size(); i++) {
                if (clientesFiltrados.get(i).getId().equals(clienteSeleccionado.getId())) {
                    rowToSelect = i;
                    break;
                }
            }
        }

        tablaClientes.setRowSelectionInterval(rowToSelect, rowToSelect);
        actualizarResumen(clientesFiltrados.get(rowToSelect));
    }

    private void actualizarResumen(ClienteInfo cliente) {
        if (cliente == null) {
            limpiarResumen();
            return;
        }

        lblResumenAvatar.setText(obtenerIniciales(cliente.getNombre()));
        lblResumenNombre.setText(cliente.getNombre());
        lblResumenEmail.setText(cliente.getEmail());
        lblResumenId.setText("ID: " + cliente.getId());
        lblResumenTelefono.setText(cliente.getTelefono());
    }

    private void limpiarResumen() {
        lblResumenAvatar.setText("--");
        lblResumenNombre.setText("Sin resultados");
        lblResumenEmail.setText("--");
        lblResumenId.setText("ID: --");
        lblResumenTelefono.setText("--");
    }

    private void confirmar() {
        int selectedRow = tablaClientes.getSelectedRow();
        if (selectedRow < 0 || selectedRow >= clientesFiltrados.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente antes de confirmar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        clienteSeleccionado = clientesFiltrados.get(selectedRow);
        dispose();
    }

    private ClienteInfo buscarClientePorId(String id) {
        for (ClienteInfo cliente : clientes) {
            if (cliente.getId().equals(id)) {
                return cliente;
            }
        }
        return null;
    }

    private void seleccionarClienteEnTabla(String id) {
        for (int i = 0; i < clientesFiltrados.size(); i++) {
            if (clientesFiltrados.get(i).getId().equals(id)) {
                tablaClientes.setRowSelectionInterval(i, i);
                actualizarResumen(clientesFiltrados.get(i));
                return;
            }
        }
    }

    private String obtenerIniciales(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "--";
        }

        String[] partes = nombre.trim().split("\\s+");
        StringBuilder iniciales = new StringBuilder();
        for (String parte : partes) {
            if (!parte.isEmpty()) {
                iniciales.append(Character.toUpperCase(parte.charAt(0)));
            }
            if (iniciales.length() == 2) {
                break;
            }
        }
        return iniciales.toString();
    }
}
