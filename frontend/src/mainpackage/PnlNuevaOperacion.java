package frontend.src.mainpackage;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Modulo visual para registrar una nueva operacion.
 */
public class PnlNuevaOperacion extends JPanel {
    private static final Color TOP_BAR = new Color(110, 60, 70);
    private static final Color FORM_HEADER = new Color(104, 104, 104);
    private static final Color BORDER = new Color(214, 214, 214);
    private static final Color LIGHT_TEXT = new Color(110, 110, 110);
    private static final Color GREEN_BTN = new Color(167, 226, 160);
    private static final Color RED_BTN = new Color(241, 182, 182);

    private final ViewDashboard parent;
    private JButton btnSeleccionarCliente;
    private JButton btnSeleccionarVideojuego;
    private DlgSeleccionClienteOperacion.ClienteInfo clienteSeleccionado;
    private DlgSeleccionVideojuegoOperacion.VideojuegoInfo videojuegoSeleccionado;

    public PnlNuevaOperacion(ViewDashboard parent) {
        this.parent = parent;
        setLayout(null);
        setBackground(Ventana.CARD_WHITE);
        setPreferredSize(new Dimension(980, 600));

        initComponentes();
    }

    private void initComponentes() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(TOP_BAR);
        topBar.setBounds(0, 0, 980, 40);

        JLabel lblTitulo = new JLabel("  Perfil de Administrador", SwingConstants.LEFT);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        topBar.add(lblTitulo, BorderLayout.WEST);
        add(topBar);

        JPanel panelFormulario = createFormularioPanel();
        panelFormulario.setBounds(80, 70, 528, 344);
        add(panelFormulario);

        JLabel lblTicket = new JLabel("Ticket digital");
        lblTicket.setBounds(628, 70, 120, 18);
        lblTicket.setFont(new Font("Arial", Font.BOLD, 12));
        lblTicket.setForeground(Color.BLACK);
        add(lblTicket);

        JPanel panelTicket = createTicketPanel();
        panelTicket.setBounds(628, 92, 278, 344);
        add(panelTicket);
    }

    private JPanel createFormularioPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(BORDER, 1, true));

        JPanel header = new JPanel(null);
        header.setBackground(FORM_HEADER);
        header.setBounds(0, 0, 528, 28);
        panel.add(header);

        JLabel lblHeader = new JLabel("Nueva operacion");
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Arial", Font.PLAIN, 12));
        lblHeader.setBounds(10, 5, 150, 18);
        header.add(lblHeader);

        JLabel lblCliente = createSectionLabel("Cliente", 16, 42, 120);
        panel.add(lblCliente);

        btnSeleccionarCliente = createSelectorButton("Seleccionar cliente");
        btnSeleccionarCliente.setBounds(16, 62, 180, 28);
        btnSeleccionarCliente.addActionListener(e -> abrirSelectorCliente());
        panel.add(btnSeleccionarCliente);

        JSeparator sepUno = new JSeparator();
        sepUno.setBounds(12, 108, 504, 1);
        sepUno.setForeground(BORDER);
        panel.add(sepUno);

        JLabel lblVideojuego = createSectionLabel("Videojuego", 16, 120, 120);
        panel.add(lblVideojuego);

        btnSeleccionarVideojuego = createSelectorButton("Seleccionar videojuego");
        btnSeleccionarVideojuego.setBounds(16, 140, 180, 28);
        btnSeleccionarVideojuego.addActionListener(e -> abrirSelectorVideojuego());
        panel.add(btnSeleccionarVideojuego);

        JSeparator sepDos = new JSeparator();
        sepDos.setBounds(12, 186, 504, 1);
        sepDos.setForeground(BORDER);
        panel.add(sepDos);

        JLabel lblTipo = createSectionLabel("Tipo de operacion", 16, 198, 140);
        panel.add(lblTipo);

        JComboBox<String> comboTipo = createComboBox(new String[]{
            "Seleccionar",
            "Renta",
            "Compra"
        });
        comboTipo.setBounds(16, 220, 170, 30);
        panel.add(comboTipo);

        JLabel lblDescuento = createSectionLabel("Descuento", 290, 198, 90);
        panel.add(lblDescuento);

        JCheckBox chkDescuento = new JCheckBox("Aplicar descuento");
        chkDescuento.setBounds(290, 223, 140, 18);
        chkDescuento.setBackground(Color.WHITE);
        chkDescuento.setFocusPainted(false);
        chkDescuento.setFont(new Font("Arial", Font.PLAIN, 10));
        panel.add(chkDescuento);

        JTextField txtDescuento = createTextField();
        txtDescuento.setBounds(435, 220, 72, 30);
        panel.add(txtDescuento);

        JLabel lblFecha = createSectionLabel("Fecha de devolucion", 16, 266, 160);
        panel.add(lblFecha);

        JTextField txtFecha = createTextField();
        txtFecha.setBounds(16, 288, 170, 28);
        panel.add(txtFecha);

        JButton btnAgregar = createActionButton("Agregar al carrito", GREEN_BTN, new Color(57, 127, 58));
        btnAgregar.setBounds(290, 286, 106, 28);
        panel.add(btnAgregar);

        JButton btnEliminar = createActionButton("Eliminar articulo", RED_BTN, new Color(156, 69, 69));
        btnEliminar.setBounds(404, 286, 104, 28);
        panel.add(btnEliminar);

        return panel;
    }

    private JPanel createTicketPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(BORDER, 1, true));
        return panel;
    }

    private JLabel createSectionLabel(String text, int x, int y, int width) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, 16);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(Color.BLACK);
        return label;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Arial", Font.PLAIN, 10));
        combo.setBackground(Color.WHITE);
        combo.setBorder(new LineBorder(BORDER, 1));
        combo.setFocusable(false);
        combo.setForeground(LIGHT_TEXT);
        return combo;
    }

    private JButton createSelectorButton(String text) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Arial", Font.PLAIN, 10));
        button.setBackground(Color.WHITE);
        button.setForeground(LIGHT_TEXT);
        button.setBorder(new LineBorder(BORDER, 1));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(0, 8, 0, 8));
        return button;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 10));
        field.setBorder(new LineBorder(BORDER, 1));
        field.setBackground(Color.WHITE);
        return field;
    }

    private JButton createActionButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 9));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(bg.darker(), 1, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void abrirSelectorCliente() {
        parent.getHost().setOscurecer(true);
        DlgSeleccionClienteOperacion dialogo = new DlgSeleccionClienteOperacion(parent.getHost(), clienteSeleccionado);
        dialogo.setVisible(true);
        parent.getHost().setOscurecer(false);

        DlgSeleccionClienteOperacion.ClienteInfo seleccionado = dialogo.getClienteSeleccionado();
        if (seleccionado != null) {
            clienteSeleccionado = seleccionado;
            btnSeleccionarCliente.setText(seleccionado.getNombre());
            btnSeleccionarCliente.setForeground(Color.BLACK);
        }
    }

    private void abrirSelectorVideojuego() {
        parent.getHost().setOscurecer(true);
        DlgSeleccionVideojuegoOperacion dialogo = new DlgSeleccionVideojuegoOperacion(parent.getHost(), videojuegoSeleccionado);
        dialogo.setVisible(true);
        parent.getHost().setOscurecer(false);

        DlgSeleccionVideojuegoOperacion.VideojuegoInfo seleccionado = dialogo.getVideojuegoSeleccionado();
        if (seleccionado != null) {
            videojuegoSeleccionado = seleccionado;
            btnSeleccionarVideojuego.setText(seleccionado.getTitulo() + " (" + seleccionado.getPlataforma() + ")");
            btnSeleccionarVideojuego.setForeground(Color.BLACK);
        }
    }
}
