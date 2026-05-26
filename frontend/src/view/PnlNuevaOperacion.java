package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.controller.OperacionController;
import frontend.src.model.ClienteInfo;
import frontend.src.model.OperacionInfo;
import frontend.src.model.UsuarioInfo;
import frontend.src.model.VideojuegoInfo;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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
    private JTextArea taTicket;
    private JButton btnSeleccionarCliente;
    private JButton btnSeleccionarVideojuego;
    private ClienteInfo clienteSeleccionado;
    private VideojuegoInfo videojuegoSeleccionado;
    private JComboBox<String> comboTipo;
    private JComboBox<String> comboDescuento;
    private JTextField txtFecha;
    private List<OperacionInfo> carrito = new ArrayList<>();
    private JButton btnConfirmar;
    private JButton btnCancelarTicket;
    

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
            "RENTA",
            "COMPRA"
        });
        this.comboTipo = comboTipo;
        this.comboTipo.setBounds(16, 223, 180, 24);
        panel.add(this.comboTipo);

        JLabel lblDescuento = createSectionLabel("Descuento", 290, 198, 90);
        panel.add(lblDescuento);

        this.comboDescuento = createComboBox(new String[]{"0%"});
        this.comboDescuento.setBounds(290, 223, 120, 24);
        this.comboDescuento.setEnabled(false);
        this.comboDescuento.setToolTipText("Selecciona un cliente para cargar descuentos disponibles.");
        panel.add(this.comboDescuento);

        JLabel lblFecha = createSectionLabel("Fecha de devolucion", 16, 266, 160);
        panel.add(lblFecha);

        this.txtFecha = createTextField();
        this.txtFecha.setBounds(16, 286, 180, 24);
        this.txtFecha.setToolTipText("Formato: dd/MM/yyyy");
        panel.add(this.txtFecha);

        JButton btnGuardar = createActionButton("Agregar al carrito", GREEN_BTN, new Color(57, 127, 58));
        btnGuardar.setBounds(290, 286, 106, 28);
        btnGuardar.addActionListener(e -> agregarAlCarrito());
        panel.add(btnGuardar);

        JButton btnEliminar = createActionButton("Eliminar articulo", RED_BTN, new Color(156, 69, 69));
        btnEliminar.setBounds(404, 286, 104, 28);
        panel.add(btnEliminar);

        return panel;
    }

    private JPanel createTicketPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(BORDER, 1, true));
        taTicket = new JTextArea();
        taTicket.setEditable(false);
        taTicket.setFont(new Font("Monospaced", Font.PLAIN, 10));
        taTicket.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(taTicket);
        scrollPane.setBounds(10, 10, 250, 270);
        scrollPane.setBorder(null);
        panel.add(scrollPane);

        actualizarTicket();

        btnConfirmar = createActionButton("Confirmar operacion", GREEN_BTN, new Color(57, 127, 58));
        btnConfirmar.setBounds(10, 290, 120, 28);
        btnConfirmar.setEnabled(false);
        btnConfirmar.addActionListener(e -> confirmarOperaciones());
        panel.add(btnConfirmar);

        btnCancelarTicket = createActionButton("Cancelar", RED_BTN, new Color(156, 69, 69));
        btnCancelarTicket.setBounds(140, 290, 120, 28);
        btnCancelarTicket.setEnabled(false);
        btnCancelarTicket.addActionListener(e -> cancelarTicket());
        panel.add(btnCancelarTicket);

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

        ClienteInfo seleccionado = dialogo.getClienteSeleccionado();
        if (seleccionado != null) {
            clienteSeleccionado = seleccionado;
            btnSeleccionarCliente.setText(seleccionado.getNombre());
            btnSeleccionarCliente.setForeground(Color.BLACK);
            cargarDescuentosPorCliente();
            actualizarTicket();
        }
    }

    private void abrirSelectorVideojuego() {
        parent.getHost().setOscurecer(true);
        DlgSeleccionVideojuegoOperacion dialogo = new DlgSeleccionVideojuegoOperacion(parent.getHost(), videojuegoSeleccionado);
        dialogo.setVisible(true);
        parent.getHost().setOscurecer(false);

        VideojuegoInfo seleccionado = dialogo.getVideojuegoSeleccionado();
        if (seleccionado != null) {
            videojuegoSeleccionado = seleccionado;
            btnSeleccionarVideojuego.setText(seleccionado.getTitulo());
            btnSeleccionarVideojuego.setForeground(Color.BLACK);
            actualizarTicket();
        }
    }

    private void agregarAlCarrito() {
        // Validar campos requeridos
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (videojuegoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un videojuego.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (videojuegoSeleccionado.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "No hay stock disponible para este videojuego.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipoSeleccionado = (String) comboTipo.getSelectedItem();
        if (tipoSeleccionado == null || tipoSeleccionado.equals("Seleccionar")) {
            JOptionPane.showMessageDialog(this, "Selecciona el tipo de operación.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener tipo (RENTA o COMPRA)
        String tipo = tipoSeleccionado.toUpperCase();

        // Calcular monto basado en el tipo y precio del videojuego
        double monto = tipo.equals("RENTA") ? videojuegoSeleccionado.getPrecioRenta() : videojuegoSeleccionado.getPrecioCompra();

        double descuento = calcularDescuentoSeleccionado(monto);

        if (descuento < 0 || descuento > monto) {
            JOptionPane.showMessageDialog(this, "El descuento no puede ser negativo ni mayor al monto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener fecha de devolución (si es renta)
        LocalDate fechaDevolucion = null;
        if (tipo.equals("RENTA")) {
            // Para RENTA, la fecha de devolución es obligatoria
            if (txtFecha.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La fecha de devolución es obligatoria para rentas.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                fechaDevolucion = LocalDate.parse(txtFecha.getText().trim(), formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validar que la fecha de devolución sea mayor a la fecha de renta (hoy)
            LocalDate hoy = LocalDate.now();
            if (!fechaDevolucion.isAfter(hoy)) {
                JOptionPane.showMessageDialog(this, "La fecha de devolución debe ser posterior a la fecha de renta.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if (!txtFecha.getText().trim().isEmpty()) {
            // Para COMPRA, el campo de fecha no debe estar lleno
            JOptionPane.showMessageDialog(this, "La fecha de devolución solo se aplica a rentas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extraer IDs (el ClienteDAO los devuelve como "CLI-001", necesitamos el número)
        int idCliente = extraerIdNumerico(clienteSeleccionado.getId());
        int idVideojuego = extraerIdNumerico(videojuegoSeleccionado.getId());
        UsuarioInfo usuarioActual = parent.getHost().getUsuarioActual();
        int idUsuario = usuarioActual != null ? usuarioActual.getIdUsuario() : 0;

        if (idCliente <= 0 || idVideojuego <= 0 || idUsuario <= 0) {
            JOptionPane.showMessageDialog(this, "No se pudo obtener el cliente, videojuego o usuario de la operacion.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LocalDate fechaOperacion = LocalDate.now();

        // Crear objeto operación y agregarlo al carrito
        OperacionInfo operacion = new OperacionInfo(
            idCliente,
            idVideojuego,
            idUsuario,
            tipo,
            monto,
            descuento,
            fechaOperacion,
            fechaDevolucion
        );

        carrito.add(operacion);

        // Actualizar UI
        actualizarTicket();
        limpiarFormularioSinCarrito();

        JOptionPane.showMessageDialog(this, "Videojuego agregado al carrito.", "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiarFormulario() {
        clienteSeleccionado = null;
        videojuegoSeleccionado = null;
        btnSeleccionarCliente.setText("Seleccionar cliente");
        btnSeleccionarCliente.setForeground(new Color(110, 110, 110));
        btnSeleccionarVideojuego.setText("Seleccionar videojuego");
        btnSeleccionarVideojuego.setForeground(new Color(110, 110, 110));
        comboTipo.setSelectedIndex(0);
        cargarDescuentosPorCliente();
        txtFecha.setText("");
    }

    private void limpiarFormularioSinCarrito() {
        videojuegoSeleccionado = null;
        btnSeleccionarVideojuego.setText("Seleccionar videojuego");
        btnSeleccionarVideojuego.setForeground(new Color(110, 110, 110));
        comboTipo.setSelectedIndex(0);
        if (comboDescuento != null) {
            comboDescuento.setSelectedIndex(0);
        }
        txtFecha.setText("");
    }

    private void cargarDescuentosPorCliente() {
        if (comboDescuento == null) {
            return;
        }

        comboDescuento.removeAllItems();
        int nivel = clienteSeleccionado != null ? clienteSeleccionado.getLvlFidelidad() : 0;
        for (String opcion : OperacionController.obtenerOpcionesDescuento(nivel)) {
            comboDescuento.addItem(opcion);
        }
        comboDescuento.setSelectedIndex(0);
        comboDescuento.setEnabled(clienteSeleccionado != null);
    }

    private double calcularDescuentoSeleccionado(double monto) {
        return monto * obtenerPorcentajeDescuentoSeleccionado() / 100.0;
    }

    private int obtenerPorcentajeDescuentoSeleccionado() {
        if (comboDescuento == null || comboDescuento.getSelectedItem() == null) {
            return 0;
        }

        String valor = comboDescuento.getSelectedItem().toString().replace("%", "").trim();
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void actualizarTicket() {
        StringBuilder ticketText = new StringBuilder();
        ticketText.append("========================\n");
        ticketText.append("    TICKET DIGITAL\n");
        ticketText.append("========================\n\n");

        if (carrito.isEmpty()) {
            ticketText.append("Carrito vacio\n");
        } else {
            // Obtener el cliente del primer item (todos comparten cliente)
            OperacionInfo primerItem = carrito.get(0);
            ticketText.append("Cliente: ").append(clienteSeleccionado != null ? clienteSeleccionado.getNombre() : "Sin cliente").append("\n\n");

            // Mostrar cada item en el carrito
            double totalGeneral = 0;
            int itemNum = 1;
            for (OperacionInfo item : carrito) {
                // Obtener info del videojuego por ID (necesitaremos una función para esto)
                ticketText.append(String.format("[%d] Tipo: %s\n", itemNum, item.getTipo()));
                ticketText.append(String.format("    Monto: $%.2f\n", item.getMonto()));
                ticketText.append(String.format("    Descto: $%.2f\n", item.getDescuento()));
                ticketText.append(String.format("    Neto: $%.2f\n", item.getMonto() - item.getDescuento()));
                ticketText.append("\n");
                totalGeneral += (item.getMonto() - item.getDescuento());
                itemNum++;
            }

            ticketText.append("------------------------\n");
            ticketText.append(String.format("Total: $%.2f\n", totalGeneral));
            ticketText.append("Puntos ganados: ")
                .append(OperacionController.calcularPuntosGanados(totalGeneral))
                .append("\n");
            ticketText.append("Items: ").append(carrito.size()).append("\n");
        }

        taTicket.setText(ticketText.toString());
        if (btnConfirmar != null) {
            btnConfirmar.setEnabled(!carrito.isEmpty());
        }
        if (btnCancelarTicket != null) {
            btnCancelarTicket.setEnabled(!carrito.isEmpty() || clienteSeleccionado != null || videojuegoSeleccionado != null);
        }
    }

    private void cancelarTicket() {
        carrito.clear();
        limpiarFormulario();
        actualizarTicket();
    }

    private void confirmarOperaciones() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "No hay cliente seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmar con el usuario
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Confirmar operación con " + carrito.size() + " item(s)?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION
        );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        // Guardar todas las operaciones del carrito
        int exitosas = 0;
        int fallidas = 0;
        List<OperacionInfo> operacionesFallidas = new ArrayList<>();
        int puntosGanados = 0;

        for (OperacionInfo operacion : carrito) {
            String resultado = OperacionController.guardarOperacion(
                operacion.getIdCliente(),
                operacion.getIdVideojuego(),
                operacion.getIdUsuario(),
                operacion.getTipo(),
                operacion.getMonto(),
                operacion.getDescuento(),
                operacion.getFechaOperacion(),
                operacion.getFechaDevolucion()
            );

            if (resultado.startsWith("Exito")) {
                exitosas++;
                puntosGanados += OperacionController.calcularPuntosGanados(operacion.getMonto() - operacion.getDescuento());
            } else {
                fallidas++;
                operacionesFallidas.add(operacion);
            }
        }

        // Mostrar resultado
        String mensaje = "Operacion completada:\n" +
                         "Exitosas: " + exitosas + "\n" +
                         "Fallidas: " + fallidas + "\n" +
                         "Puntos ganados: " + puntosGanados;

        if (fallidas == 0) {
            JOptionPane.showMessageDialog(this, mensaje, "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCarrito();
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
            carrito.clear();
            carrito.addAll(operacionesFallidas);
            actualizarTicket();
        }
    }

    private void limpiarCarrito() {
        carrito.clear();
        limpiarFormulario();
        actualizarTicket();
    }

    private int extraerIdNumerico(String id) {
        // Extrae el número de un ID como "CLI-001" → 1
        if (id == null || id.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(id.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
