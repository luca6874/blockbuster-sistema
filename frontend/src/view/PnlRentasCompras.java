package frontend.src.view;

import frontend.src.controller.OperacionController;
import frontend.src.controller.Ventana;
import frontend.src.model.OperacionTicketInfo;
import frontend.src.service.ImageManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PnlRentasCompras extends JPanel {
    private static final int IDX_CLIENTE = 0;
    private static final int IDX_VIDEOJUEGO = 1;
    private static final int IDX_TIPO = 2;
    private static final int IDX_MONTO = 3;
    private static final int IDX_DESCUENTO = 4;
    private static final int IDX_FECHA_OPERACION = 5;
    private static final int IDX_PUNTOS = 6;
    private static final int IDX_ID = 7;
    private static final int IDX_FECHA_VENCIMIENTO = 8;
    private static final int IDX_ESTADO = 9;
    private static final int IDX_CORREO = 10;
    private static final int IDX_PLATAFORMA = 11;
    private static final int IDX_IMAGEN = 12;

    private static final Color TOP_BAR = new Color(110, 60, 70);
    private static final Color DETAIL_BORDER = new Color(222, 214, 214);
    private static final Color DETAIL_MUTED = new Color(116, 116, 116);
    private static final Color DETAIL_SOFT = new Color(252, 249, 249);
    private static final Color TYPE_BG = new Color(243, 232, 235);
    private static final Color TYPE_FG = new Color(124, 50, 65);

    private final ViewDashboard parent;
    private JTable tablaOperaciones;
    private JTextField txtBuscarCliente;
    private JTextField txtBuscarVideojuego;
    private JComboBox<String> comboPlataforma;
    private JComboBox<String> comboTipo;
    private JLabel lblTituloTabla;
    private JLabel lblDetalleID;
    private JLabel lblDetalleEstado;
    private JLabel lblDetalleAvatar;
    private JLabel lblDetalleCliente;
    private JLabel lblDetalleCorreo;
    private JLabel lblDetalleCaratula;
    private JLabel lblDetalleVideojuego;
    private JLabel lblDetalleSubtitulo;
    private JLabel lblDetallePlataforma;
    private JLabel lblDetalleTipo;
    private JLabel lblDetalleFechaRenta;
    private JLabel lblDetalleFechaVencimiento;
    private JLabel lblDetalleMonto;
    private JLabel lblDetalleDescuento;
    private JLabel lblDetalleTotal;
    private JLabel lblDetallePuntos;
    private JButton btnDescargar;
    private List<String[]> datosOperaciones = new ArrayList<>();
    private List<String[]> datosOperacionesFiltradas = new ArrayList<>();
    private int filaSeleccionada = -1;

    public PnlRentasCompras(ViewDashboard parent) {
        this.parent = parent;
        this.setLayout(null);
        this.setBackground(Ventana.CARD_WHITE);
        this.setPreferredSize(new Dimension(980, 530));

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
        panelCentral.setBounds(0, 100, 980, 430);
        this.add(panelCentral);
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(null);
        topBar.setBackground(TOP_BAR);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Rentas y Compras - Modulo de Gestion", SwingConstants.LEFT);
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

        txtBuscarCliente = new JTextField("Buscar por cliente...");
        txtBuscarCliente.setBounds(10, 10, 160, 30);
        txtBuscarCliente.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        txtBuscarCliente.setBackground(Color.WHITE);
        txtBuscarCliente.setFont(new Font("Arial", Font.PLAIN, 12));
        txtBuscarCliente.setForeground(new Color(150, 150, 150));
        configurarPlaceholder(txtBuscarCliente, "Buscar por cliente...");
        panel.add(txtBuscarCliente);

        txtBuscarVideojuego = new JTextField("Buscar por videojuego...");
        txtBuscarVideojuego.setBounds(180, 10, 160, 30);
        txtBuscarVideojuego.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        txtBuscarVideojuego.setBackground(Color.WHITE);
        txtBuscarVideojuego.setFont(new Font("Arial", Font.PLAIN, 12));
        txtBuscarVideojuego.setForeground(new Color(150, 150, 150));
        configurarPlaceholder(txtBuscarVideojuego, "Buscar por videojuego...");
        panel.add(txtBuscarVideojuego);

        JLabel lblPlataforma = new JLabel("Plataforma:");
        lblPlataforma.setBounds(350, 10, 70, 30);
        lblPlataforma.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblPlataforma);

        String[] plataformas = {"Todos", "Xbox", "PlayStation", "Switch", "Xbox 360", "Xbox ONE", "SWITCH", "SWITCH 2", "PS4", "PS5"};
        comboPlataforma = new JComboBox<>(plataformas);
        comboPlataforma.setBounds(425, 10, 110, 30);
        comboPlataforma.setBackground(Color.WHITE);
        panel.add(comboPlataforma);

        JCheckBox chkOperacion = new JCheckBox();
        chkOperacion.setBounds(545, 15, 25, 20);
        chkOperacion.setOpaque(false);
        panel.add(chkOperacion);

        String[] opcionesOperacion = {"Ambos", "Renta", "Compra"};
        comboTipo = new JComboBox<>(opcionesOperacion);
        comboTipo.setBounds(575, 10, 90, 30);
        comboTipo.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        comboTipo.setBackground(Color.WHITE);
        comboTipo.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(comboTipo);

        JButton btnNuevaOperacion = new JButton("+ Nueva operacion");
        btnNuevaOperacion.setBounds(790, 10, 140, 30);
        btnNuevaOperacion.setBackground(Ventana.ACCENT_RED);
        btnNuevaOperacion.setForeground(Color.WHITE);
        btnNuevaOperacion.setFocusPainted(false);
        btnNuevaOperacion.setBorderPainted(false);
        btnNuevaOperacion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevaOperacion.addActionListener(e -> parent.mostrarNuevaOperacion());
        panel.add(btnNuevaOperacion);

        registrarFiltradoTexto(txtBuscarCliente);
        registrarFiltradoTexto(txtBuscarVideojuego);
        comboPlataforma.addActionListener(e -> filtrarOperaciones());
        comboTipo.addActionListener(e -> filtrarOperaciones());

        return panel;
    }

    private JPanel createPanelCentral() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);

        JPanel panelDetalle = createPanelDetalle();
        panelDetalle.setBounds(620, 0, 340, 430);
        panel.add(panelDetalle);

        JPanel panelTabla = createPanelTabla();
        panelTabla.setBounds(0, 0, 600, 430);
        panel.add(panelTabla);

        return panel;
    }

    private JPanel createPanelTabla() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Ventana.CARD_WHITE);

        lblTituloTabla = new JLabel();
        lblTituloTabla.setBounds(0, 0, 330, 25);
        lblTituloTabla.setFont(new Font("Arial", Font.BOLD, 14));
        lblTituloTabla.setForeground(Color.BLACK);
        panel.add(lblTituloTabla);

        initTablaOperaciones();
        actualizarTituloTabla();

        JScrollPane scroll = new JScrollPane(tablaOperaciones);
        scroll.setBounds(0, 30, 600, 390);
        scroll.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll);

        return panel;
    }

    private JPanel createPanelDetalle() {
        JPanel panel = createRoundedPanel(Ventana.CARD_WHITE, DETAIL_BORDER, 26);

        JLabel lblTitulo = new JLabel("Detalle de Operacion");
        lblTitulo.setBounds(16, 12, 180, 22);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(Color.BLACK);
        panel.add(lblTitulo);

        JSeparator headerLine = new JSeparator();
        headerLine.setBounds(12, 42, 316, 1);
        headerLine.setForeground(new Color(229, 229, 229));
        panel.add(headerLine);

        lblDetalleID = createTextLabel("", 16, 50, 160, 24, Font.BOLD, 18, Color.BLACK);
        panel.add(lblDetalleID);

        lblDetalleEstado = createBadgeLabel();
        lblDetalleEstado.setBounds(228, 49, 92, 24);
        panel.add(lblDetalleEstado);

        JPanel panelCliente = createRoundedPanel(Color.WHITE, new Color(232, 232, 232), 18);
        panelCliente.setBounds(16, 82, 308, 64);
        panel.add(panelCliente);

        lblDetalleAvatar = createAvatarLabel();
        lblDetalleAvatar.setBounds(14, 12, 40, 40);
        panelCliente.add(lblDetalleAvatar);

        lblDetalleCliente = createTextLabel("", 68, 12, 220, 18, Font.BOLD, 13, Color.BLACK);
        panelCliente.add(lblDetalleCliente);

        lblDetalleCorreo = createTextLabel("", 68, 32, 220, 14, Font.PLAIN, 10, DETAIL_MUTED);
        panelCliente.add(lblDetalleCorreo);

        JPanel panelJuego = createRoundedPanel(Color.WHITE, new Color(232, 232, 232), 18);
        panelJuego.setBounds(16, 156, 308, 118);
        panel.add(panelJuego);

        lblDetalleCaratula = new JLabel("", SwingConstants.CENTER);
        lblDetalleCaratula.setBounds(12, 12, 62, 86);
        lblDetalleCaratula.setOpaque(true);
        lblDetalleCaratula.setBackground(new Color(248, 248, 248));
        lblDetalleCaratula.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        lblDetalleCaratula.setFont(new Font("Arial", Font.BOLD, 10));
        lblDetalleCaratula.setForeground(new Color(125, 125, 125));
        panelJuego.add(lblDetalleCaratula);

        lblDetalleVideojuego = createTextLabel("", 88, 12, 202, 18, Font.BOLD, 13, Color.BLACK);
        panelJuego.add(lblDetalleVideojuego);

        lblDetalleSubtitulo = createTextLabel("", 88, 32, 202, 16, Font.PLAIN, 12, Color.BLACK);
        panelJuego.add(lblDetalleSubtitulo);

        lblDetallePlataforma = createTextLabel("", 88, 52, 202, 14, Font.PLAIN, 10, DETAIL_MUTED);
        panelJuego.add(lblDetallePlataforma);

        lblDetalleTipo = createBadgeLabel();
        lblDetalleTipo.setBounds(88, 70, 74, 22);
        panelJuego.add(lblDetalleTipo);

        lblDetalleFechaRenta = createTextLabel("", 88, 92, 202, 13, Font.PLAIN, 10, DETAIL_MUTED);
        panelJuego.add(lblDetalleFechaRenta);

        lblDetalleFechaVencimiento = createTextLabel("", 88, 104, 202, 13, Font.PLAIN, 10, DETAIL_MUTED);
        panelJuego.add(lblDetalleFechaVencimiento);

        JPanel panelResumen = createRoundedPanel(DETAIL_SOFT, new Color(235, 229, 229), 18);
        panelResumen.setBounds(16, 286, 308, 76);
        panel.add(panelResumen);

        lblDetalleMonto = addSummaryRow(panelResumen, 10, "Monto pagado");
        lblDetalleDescuento = addSummaryRow(panelResumen, 28, "Descuento aplicado");
        lblDetalleTotal = addSummaryRow(panelResumen, 46, "Total pagado");
        lblDetalleTotal.setFont(new Font("Arial", Font.BOLD, 12));
        lblDetallePuntos = addSummaryRow(panelResumen, 64, "Puntos conseguidos");

        btnDescargar = new JButton("Descargar ticket o comprobante (PDF)");
        btnDescargar.setBounds(16, 374, 308, 30);
        btnDescargar.setBackground(Color.WHITE);
        btnDescargar.setForeground(Color.BLACK);
        btnDescargar.setFont(new Font("Arial", Font.PLAIN, 10));
        btnDescargar.setFocusPainted(false);
        btnDescargar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDescargar.setBorder(new LineBorder(new Color(170, 170, 170), 1, true));
        btnDescargar.setHorizontalAlignment(SwingConstants.LEFT);
        btnDescargar.setIcon(loadScaledIcon("/frontend/src/images/download.png", 16, 16));
        btnDescargar.setIconTextGap(8);
        btnDescargar.setMargin(new Insets(0, 10, 0, 0));
        btnDescargar.addActionListener(e -> descargarTicketPDF());
        panel.add(btnDescargar);

        limpiarDetalle();
        return panel;
    }

    private JLabel addSummaryRow(JPanel panel, int y, String texto) {
        JLabel lblNombre = new JLabel(texto);
        lblNombre.setBounds(14, y, 160, 14);
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 11));
        lblNombre.setForeground(new Color(80, 80, 80));
        panel.add(lblNombre);

        JLabel lblValor = new JLabel("", SwingConstants.RIGHT);
        lblValor.setBounds(175, y, 118, 14);
        lblValor.setFont(new Font("Arial", Font.BOLD, 11));
        lblValor.setForeground(Color.BLACK);
        panel.add(lblValor);

        if (y < 64) {
            JSeparator separator = new JSeparator();
            separator.setBounds(14, y + 16, 279, 1);
            separator.setForeground(new Color(230, 225, 225));
            panel.add(separator);
        }

        return lblValor;
    }

    private JLabel createTextLabel(String texto, int x, int y, int w, int h, int estilo, int tamano, Color color) {
        JLabel label = new JLabel(texto, SwingConstants.LEFT);
        label.setBounds(x, y, w, h);
        label.setFont(new Font("Arial", estilo, tamano));
        label.setForeground(color);
        return label;
    }

    private JLabel createBadgeLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = (Color) getClientProperty("badgeBackground");
                Color fg = (Color) getClientProperty("badgeForeground");
                if (bg == null) bg = TYPE_BG;
                if (fg == null) fg = TYPE_FG;

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();

                setForeground(fg);
                super.paintComponent(g);
            }
        };
        label.setOpaque(false);
        label.setFont(new Font("Arial", Font.BOLD, 10));
        return label;
    }

    private JLabel createAvatarLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(239, 226, 230));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(211, 188, 195));
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setOpaque(false);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(105, 58, 69));
        return label;
    }

    private JPanel createRoundedPanel(Color background, Color border, int radius) {
        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(background);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                if (border != null) {
                    g2.setColor(border);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    private ImageIcon loadScaledIcon(String path, int width, int height) {
        return ImageManager.cargarImagenPreview(extraerNombreImagen(path), width, height);
    }

    private String extraerNombreImagen(String path) {
        if (path == null) {
            return null;
        }

        int slash = path.lastIndexOf('/');
        return slash >= 0 ? path.substring(slash + 1) : path;
    }

    private void initTablaOperaciones() {
        String[] columnas = {"Cliente", "Videojuego", "Tipo", "Monto", "Descuento", "Fecha"};
        datosOperaciones = new ArrayList<>(OperacionController.obtenerTodas());
        datosOperacionesFiltradas = new ArrayList<>(datosOperaciones);

        DefaultTableModel modelo = new DefaultTableModel(crearFilasTabla(), columnas) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tablaOperaciones = new JTable(modelo);
        tablaOperaciones.setRowHeight(35);
        tablaOperaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaOperaciones.setSelectionBackground(new Color(152, 33, 54, 40));
        tablaOperaciones.setSelectionForeground(Color.BLACK);
        tablaOperaciones.setShowVerticalLines(false);
        tablaOperaciones.setGridColor(new Color(235, 235, 235));
        tablaOperaciones.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaOperaciones.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JTableHeader header = tablaOperaciones.getTableHeader();
        header.setBackground(new Color(244, 238, 240));
        header.setForeground(new Color(88, 48, 57));
        header.setFont(new Font("Arial", Font.BOLD, 11));
        header.setBorder(new LineBorder(new Color(225, 220, 220), 1));
        header.setReorderingAllowed(false);

        tablaOperaciones.getColumnModel().getColumn(0).setPreferredWidth(150);
        tablaOperaciones.getColumnModel().getColumn(1).setPreferredWidth(185);
        tablaOperaciones.getColumnModel().getColumn(2).setPreferredWidth(70);
        tablaOperaciones.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaOperaciones.getColumnModel().getColumn(4).setPreferredWidth(70);
        tablaOperaciones.getColumnModel().getColumn(5).setPreferredWidth(85);

        tablaOperaciones.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablaOperaciones.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    filaSeleccionada = row;
                    mostrarDetalle(row);
                }
            }
        });

        if (!datosOperacionesFiltradas.isEmpty() && tablaOperaciones.getRowCount() > 0) {
            filaSeleccionada = 0;
            tablaOperaciones.setRowSelectionInterval(0, 0);
            mostrarDetalle(0);
        } else {
            filaSeleccionada = -1;
            limpiarDetalle();
        }
    }

    private String[][] crearFilasTabla() {
        String[][] filas = new String[datosOperacionesFiltradas.size()][6];
        for (int i = 0; i < datosOperacionesFiltradas.size(); i++) {
            String[] datos = datosOperacionesFiltradas.get(i);
            filas[i][0] = datos[IDX_CLIENTE];
            filas[i][1] = datos[IDX_VIDEOJUEGO];
            filas[i][2] = datos[IDX_TIPO];
            filas[i][3] = datos[IDX_MONTO];
            filas[i][4] = datos[IDX_DESCUENTO];
            filas[i][5] = datos[IDX_FECHA_OPERACION];
        }
        return filas;
    }

    private void mostrarDetalle(int fila) {
        if (!isDetalleInicializado()) {
            return;
        }

        if (fila < 0 || fila >= datosOperacionesFiltradas.size()) {
            limpiarDetalle();
            return;
        }

        filaSeleccionada = fila;
        String[] datos = datosOperacionesFiltradas.get(fila);
        String cliente = datos[IDX_CLIENTE];
        String videojuego = datos[IDX_VIDEOJUEGO];
        String[] juegoInfo = resolverFichaJuego(datos);
        String descuento = datos[IDX_DESCUENTO];
        double montoBase = parseCurrency(datos[IDX_MONTO]);
        double descuentoAplicado = parseCurrency(descuento);
        double totalPagado = Math.max(0.0, montoBase - descuentoAplicado);

        lblDetalleID.setText("ID: " + datos[IDX_ID]);
        lblDetalleCliente.setText(cliente);
        lblDetalleCorreo.setText(datos[IDX_CORREO] == null || datos[IDX_CORREO].trim().isEmpty()
            ? generarCorreo(cliente)
            : datos[IDX_CORREO]);
        lblDetalleAvatar.setText(obtenerIniciales(cliente));

        lblDetalleVideojuego.setText(juegoInfo[0]);
        lblDetalleSubtitulo.setText(juegoInfo[1]);
        lblDetallePlataforma.setText("Plataforma: " + juegoInfo[2]);
        lblDetalleTipo.setText(datos[IDX_TIPO]);
        configurarBadgeTipo(datos[IDX_TIPO]);

        lblDetalleFechaRenta.setText("Fecha de operacion: " + datos[IDX_FECHA_OPERACION]);
        if ("N/A".equalsIgnoreCase(datos[IDX_FECHA_VENCIMIENTO])) {
            lblDetalleFechaVencimiento.setText("Entrega: inmediata");
        } else {
            lblDetalleFechaVencimiento.setText("Fecha limite: " + datos[IDX_FECHA_VENCIMIENTO]);
        }

        lblDetalleEstado.setText(datos[IDX_ESTADO]);
        configurarBadgeEstado(datos[IDX_ESTADO]);

        lblDetalleMonto.setText(formatCurrency(montoBase));
        if (descuentoAplicado > 0.0) {
            lblDetalleDescuento.setText("-" + formatCurrency(descuentoAplicado));
        } else {
            lblDetalleDescuento.setText("N/A");
        }
        lblDetalleTotal.setText(formatCurrency(totalPagado));
        lblDetallePuntos.setText(datos[IDX_PUNTOS]);

        ImageIcon caratula = juegoInfo[3].trim().isEmpty()
            ? null
            : loadScaledIcon("/frontend/src/images/" + juegoInfo[3], 62, 86);
        if (caratula != null) {
            lblDetalleCaratula.setText("");
            lblDetalleCaratula.setIcon(caratula);
        } else {
            lblDetalleCaratula.setIcon(null);
            lblDetalleCaratula.setText("<html><center>Sin<br>caratula</center></html>");
        }

        setDetalleDisponible(true);
    }

    private void limpiarDetalle() {
        if (!isDetalleInicializado()) {
            return;
        }

        lblDetalleID.setText("ID: ----");
        lblDetalleCliente.setText("Selecciona una operacion");
        lblDetalleCorreo.setText("El detalle aparecera aqui.");
        lblDetalleAvatar.setText("--");
        lblDetalleVideojuego.setText("Sin videojuego");
        lblDetalleSubtitulo.setText("Selecciona una fila para ver mas informacion.");
        lblDetallePlataforma.setText("Plataforma: --");
        lblDetalleTipo.setText("Tipo");
        lblDetalleFechaRenta.setText("Fecha de operacion: --");
        lblDetalleFechaVencimiento.setText("Fecha limite: --");
        lblDetalleEstado.setText("Sin estado");
        lblDetalleMonto.setText("$0.00");
        lblDetalleDescuento.setText("N/A");
        lblDetalleTotal.setText("$0.00");
        lblDetallePuntos.setText("--");
        lblDetalleCaratula.setIcon(null);
        lblDetalleCaratula.setText("<html><center>Sin<br>caratula</center></html>");
        configurarBadgeTipo("Tipo");
        configurarBadgeEstado("Sin estado");
        setDetalleDisponible(false);
    }

    private void setDetalleDisponible(boolean disponible) {
        btnDescargar.setEnabled(disponible);
    }

    private boolean isDetalleInicializado() {
        return lblDetalleID != null
            && lblDetalleCliente != null
            && lblDetalleCorreo != null
            && lblDetalleAvatar != null
            && lblDetalleVideojuego != null
            && lblDetalleSubtitulo != null
            && lblDetallePlataforma != null
            && lblDetalleTipo != null
            && lblDetalleFechaRenta != null
            && lblDetalleFechaVencimiento != null
            && lblDetalleEstado != null
            && lblDetalleMonto != null
            && lblDetalleDescuento != null
            && lblDetalleTotal != null
            && lblDetallePuntos != null
            && lblDetalleCaratula != null
            && btnDescargar != null;
    }

    private void filtrarOperaciones() {
        String textoCliente = obtenerTextoBusqueda(txtBuscarCliente, "Buscar por cliente...");
        String textoVideojuego = obtenerTextoBusqueda(txtBuscarVideojuego, "Buscar por videojuego...");
        String plataformaSeleccionada = obtenerValorCombo(comboPlataforma);
        String tipoSeleccionado = obtenerValorCombo(comboTipo);
        String idSeleccionado = filaSeleccionada >= 0 && filaSeleccionada < datosOperacionesFiltradas.size()
            ? valorIndice(datosOperacionesFiltradas.get(filaSeleccionada), IDX_ID)
            : "";

        datosOperacionesFiltradas = new ArrayList<>();

        for (String[] operacion : datosOperaciones) {
            boolean coincideCliente = textoCliente.isEmpty()
                || normalizar(valorIndice(operacion, IDX_CLIENTE)).contains(textoCliente);
            boolean coincideVideojuego = textoVideojuego.isEmpty()
                || normalizar(valorIndice(operacion, IDX_VIDEOJUEGO)).contains(textoVideojuego);
            boolean coincidePlataforma = coincidePlataforma(valorIndice(operacion, IDX_PLATAFORMA), plataformaSeleccionada);
            boolean coincideTipo = coincideTipo(valorIndice(operacion, IDX_TIPO), tipoSeleccionado);

            if (coincideCliente && coincideVideojuego && coincidePlataforma && coincideTipo) {
                datosOperacionesFiltradas.add(operacion);
            }
        }

        if (tablaOperaciones == null) {
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tablaOperaciones.getModel();
        modelo.setDataVector(crearFilasTabla(), new String[]{"Cliente", "Videojuego", "Tipo", "Monto", "Descuento", "Fecha"});
        configurarColumnasTabla();
        actualizarTituloTabla();

        if (datosOperacionesFiltradas.isEmpty()) {
            filaSeleccionada = -1;
            limpiarDetalle();
            return;
        }

        int nuevaFila = buscarFilaPorId(idSeleccionado);
        if (nuevaFila < 0) {
            nuevaFila = Math.min(Math.max(filaSeleccionada, 0), datosOperacionesFiltradas.size() - 1);
        }

        mostrarDetalle(nuevaFila);
        if (tablaOperaciones.getRowCount() > 0 && nuevaFila < tablaOperaciones.getRowCount()) {
            tablaOperaciones.setRowSelectionInterval(nuevaFila, nuevaFila);
        }
    }

    private void configurarColumnasTabla() {
        tablaOperaciones.getColumnModel().getColumn(0).setPreferredWidth(150);
        tablaOperaciones.getColumnModel().getColumn(1).setPreferredWidth(185);
        tablaOperaciones.getColumnModel().getColumn(2).setPreferredWidth(70);
        tablaOperaciones.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaOperaciones.getColumnModel().getColumn(4).setPreferredWidth(70);
        tablaOperaciones.getColumnModel().getColumn(5).setPreferredWidth(85);
    }

    private int buscarFilaPorId(String idOperacion) {
        if (idOperacion == null || idOperacion.trim().isEmpty()) {
            return -1;
        }

        for (int i = 0; i < datosOperacionesFiltradas.size(); i++) {
            if (idOperacion.equals(valorIndice(datosOperacionesFiltradas.get(i), IDX_ID))) {
                return i;
            }
        }
        return -1;
    }

    private void configurarPlaceholder(JTextField campo, String placeholder) {
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (placeholder.equals(campo.getText())) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campo.getText().trim().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(new Color(150, 150, 150));
                }
            }
        });
    }

    private void registrarFiltradoTexto(JTextField campo) {
        campo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrarOperaciones();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrarOperaciones();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrarOperaciones();
            }
        });
    }

    private String obtenerTextoBusqueda(JTextField campo, String placeholder) {
        String texto = campo == null ? "" : campo.getText();
        if (texto == null || placeholder.equals(texto)) {
            return "";
        }
        return normalizar(texto);
    }

    private String obtenerValorCombo(JComboBox<String> combo) {
        Object seleccionado = combo == null ? null : combo.getSelectedItem();
        return seleccionado == null ? "" : seleccionado.toString().trim();
    }

    private boolean coincideTipo(String tipoOperacion, String tipoSeleccionado) {
        String filtro = tipoSeleccionado == null ? "" : tipoSeleccionado.trim();
        if (filtro.isEmpty() || "Ambos".equalsIgnoreCase(filtro)) {
            return true;
        }

        String tipo = tipoOperacion == null ? "" : tipoOperacion.trim();
        if ("Compra".equalsIgnoreCase(filtro) || "Venta".equalsIgnoreCase(filtro)) {
            return "COMPRA".equalsIgnoreCase(tipo) || "VENTA".equalsIgnoreCase(tipo);
        }
        return tipo.equalsIgnoreCase(filtro);
    }

    private boolean coincidePlataforma(String plataformaOperacion, String plataformaSeleccionada) {
        String filtro = plataformaSeleccionada == null ? "" : plataformaSeleccionada.trim();
        if (filtro.isEmpty() || "Todos".equalsIgnoreCase(filtro)) {
            return true;
        }

        String plataforma = plataformaOperacion == null ? "" : plataformaOperacion.trim();
        if (plataforma.isEmpty()) {
            return false;
        }

        String plataformaNormalizada = normalizar(plataforma);
        String filtroNormalizado = normalizar(filtro);

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

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim().toLowerCase(Locale.ROOT);
    }

    private String valorIndice(String[] datos, int indice) {
        if (datos == null || indice < 0 || indice >= datos.length || datos[indice] == null) {
            return "";
        }
        return datos[indice].trim();
    }

    private void eliminarOperacionSeleccionada() {
        if (filaSeleccionada < 0 || filaSeleccionada >= datosOperacionesFiltradas.size()) {
            return;
        }

        String idOperacion = valorIndice(datosOperacionesFiltradas.get(filaSeleccionada), IDX_ID);
        datosOperaciones.removeIf(datos -> valorIndice(datos, IDX_ID).equals(idOperacion));
        filtrarOperaciones();

    }

    private void descargarTicketPDF() {
        int fila = tablaOperaciones != null ? tablaOperaciones.getSelectedRow() : filaSeleccionada;
        if (fila >= 0) {
            filaSeleccionada = fila;
        }

        if (filaSeleccionada < 0 || filaSeleccionada >= datosOperacionesFiltradas.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona una operacion para generar el PDF.", "Sin operacion seleccionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] operacion = datosOperacionesFiltradas.get(filaSeleccionada);
        OperacionTicketInfo ticket = crearTicketSeleccionado(operacion);

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar ticket PDF");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF (*.pdf)", "pdf"));
        chooser.setSelectedFile(new File("ticket_operacion_" + idOperacionParaArchivo(ticket.getIdOperacion()) + ".pdf"));

        int opcion = chooser.showSaveDialog(this);
        if (opcion != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File destino = asegurarExtensionPdf(chooser.getSelectedFile());
        try {
            OperacionController.generarTicketPDF(ticket, destino);
            JOptionPane.showMessageDialog(this, "Ticket PDF generado correctamente:\n" + destino.getAbsolutePath(), "PDF generado", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo generar el PDF:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private OperacionTicketInfo crearTicketSeleccionado(String[] operacion) {
        return new OperacionTicketInfo(
                valorIndice(operacion, IDX_ID),
                valorIndice(operacion, IDX_TIPO),
                valorIndice(operacion, IDX_CLIENTE),
                valorIndice(operacion, IDX_VIDEOJUEGO),
                valorIndice(operacion, IDX_PLATAFORMA),
                valorIndice(operacion, IDX_FECHA_OPERACION),
                valorIndice(operacion, IDX_FECHA_VENCIMIENTO),
                valorIndice(operacion, IDX_MONTO),
                valorIndice(operacion, IDX_DESCUENTO),
                valorIndice(operacion, IDX_ESTADO)
        );
    }

    private File asegurarExtensionPdf(File archivo) {
        if (archivo == null || archivo.getName().toLowerCase(Locale.ROOT).endsWith(".pdf")) {
            return archivo;
        }
        return new File(archivo.getParentFile(), archivo.getName() + ".pdf");
    }

    private String idOperacionParaArchivo(String idOperacion) {
        if (idOperacion == null || idOperacion.trim().isEmpty()) {
            return "sin_id";
        }
        String digitos = idOperacion.replaceAll("[^0-9]", "");
        if (digitos.isEmpty()) {
            return idOperacion.replaceAll("[^A-Za-z0-9_-]", "_");
        }
        try {
            return String.valueOf(Integer.parseInt(digitos));
        } catch (NumberFormatException e) {
            return digitos;
        }
    }

    private void actualizarTituloTabla() {
        if (lblTituloTabla != null) {
            int total = datosOperacionesFiltradas == null ? 0 : datosOperacionesFiltradas.size();
            lblTituloTabla.setText("Listado de Rentas y Compras (" + total + ")");
        }
    }

    private void configurarBadgeTipo(String tipo) {
        lblDetalleTipo.putClientProperty("badgeBackground", TYPE_BG);
        lblDetalleTipo.putClientProperty("badgeForeground", TYPE_FG);
        lblDetalleTipo.repaint();
    }

    private void configurarBadgeEstado(String estado) {
        Color background = new Color(241, 241, 241);
        Color foreground = new Color(88, 88, 88);

        if ("Activa".equalsIgnoreCase(estado)) {
            background = new Color(232, 245, 237);
            foreground = new Color(45, 122, 82);
        } else if ("Completada".equalsIgnoreCase(estado)) {
            background = new Color(229, 239, 255);
            foreground = new Color(53, 91, 171);
        } else if ("Pendiente".equalsIgnoreCase(estado)) {
            background = new Color(253, 243, 227);
            foreground = new Color(184, 112, 39);
        } else if ("Cancelada".equalsIgnoreCase(estado)) {
            background = new Color(252, 234, 231);
            foreground = new Color(183, 64, 53);
        }

        lblDetalleEstado.putClientProperty("badgeBackground", background);
        lblDetalleEstado.putClientProperty("badgeForeground", foreground);
        lblDetalleEstado.repaint();
    }

    private String[] resolverFichaJuego(String[] datos) {
        return new String[]{
            datos[IDX_VIDEOJUEGO],
            "Operacion " + datos[IDX_TIPO].toLowerCase(Locale.ROOT),
            datos[IDX_PLATAFORMA],
            datos[IDX_IMAGEN]
        };
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
        return iniciales.length() == 0 ? "--" : iniciales.toString();
    }

    private String generarCorreo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "cliente@briarbuster.com";
        }

        String correoBase = Normalizer.normalize(nombre.trim().toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .replaceAll("[^a-z0-9\\s]", "")
            .trim()
            .replaceAll("\\s+", ".");

        if (correoBase.isEmpty()) {
            correoBase = "cliente";
        }

        return correoBase + "@briarbuster.com";
    }

    private double parseCurrency(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return 0.0;
        }
        String limpio = texto.replace("$", "").replace(",", "").trim();
        try {
            return Double.parseDouble(limpio);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String formatCurrency(double cantidad) {
        return String.format(Locale.US, "$%.2f", cantidad);
    }
}
