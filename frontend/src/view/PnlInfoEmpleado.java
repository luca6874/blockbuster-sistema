package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.model.UsuarioInfo;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Panel detallado de informacion del empleado.
 */
public class PnlInfoEmpleado extends JPanel {
    private final ViewDashboard parent;
    private JTextField tfNombres;
    private JTextField tfApellidos;
    private JTextField tfEmail;
    private JTextField tfPassword;
    private JTextField tfFechaNacimiento;

    public PnlInfoEmpleado(ViewDashboard parent) {
        this.parent = parent;
        this.setLayout(null);
        this.setBackground(Ventana.CARD_WHITE);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(110, 60, 70));
        topBar.setBounds(0, 0, 2000, 40);
        JLabel tB = new JLabel("  Perfil de Administrador - Informacion de Empleado", SwingConstants.LEFT);
        tB.setForeground(Color.WHITE);
        tB.setFont(new Font("Arial", Font.BOLD, 14));
        topBar.add(tB);
        this.add(topBar);

        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.setColor(new Color(220, 220, 220));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            }
        };
        card.setBounds(30, 60, 720, 480);
        card.setOpaque(false);

        initLadoIdentidad(card);

        JSeparator s = new JSeparator(JSeparator.VERTICAL);
        s.setBounds(240, 40, 2, 400);
        card.add(s);

        int startX = 280;
        tfNombres = crearCampo("Nombres", startX, 40, 200, card);
        tfApellidos = crearCampo("Apellidos", startX + 220, 40, 180, card);
        tfEmail = crearCampo("E-mail", startX, 110, 400, card);
        tfPassword = crearCampo("Contrasena", startX, 180, 300, card);
        tfFechaNacimiento = crearCampo("Fecha de nacimiento", startX, 250, 150, card);

        llenarCamposConDatosUsuario();

        JButton btnCambiar = new JButton("cambiar informacion");
        btnCambiar.setBounds(400, 400, 160, 35);
        btnCambiar.setContentAreaFilled(false);
        btnCambiar.setBorder(new LineBorder(Ventana.ACCENT_RED, 1, true));
        btnCambiar.setForeground(Ventana.ACCENT_RED);
        btnCambiar.addActionListener(e -> parent.getHost().mostrarEdicionEmpleado());
        card.add(btnCambiar);

        JButton btnImprimir = new JButton("Imprimir info");
        btnImprimir.setBounds(570, 400, 130, 35);
        btnImprimir.setBackground(Ventana.ACCENT_RED);
        btnImprimir.setForeground(Color.WHITE);
        btnImprimir.setFocusPainted(false);
        card.add(btnImprimir);

        this.add(card);
    }

    private void llenarCamposConDatosUsuario() {
        UsuarioInfo usuario = parent.getHost().getUsuarioActual();

        if (usuario != null) {
            tfNombres.setText(usuario.getNombre() != null ? usuario.getNombre() : "");
            tfApellidos.setText(obtenerApellidos(usuario));
            tfEmail.setText(usuario.getCorreo() != null ? usuario.getCorreo() : "");
            tfPassword.setText(usuario.getPassword() != null ? usuario.getPassword() : "");
            tfFechaNacimiento.setText(usuario.getFechaNacimiento() != null ? usuario.getFechaNacimiento().toString() : "");
        }
    }

    private void initLadoIdentidad(JPanel card) {
        try {
            java.net.URL url = getClass().getResource("/frontend/src/images/iconUserBig.png");

            if (url == null) {
                System.out.println("No se encontro la imagen: /frontend/src/images/iconUserBig.png");
            } else {
                ImageIcon icon = new ImageIcon(url);
                JLabel photo = new JLabel(new ImageIcon(
                        icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)
                ));
                photo.setBounds(30, 40, 180, 180);
                card.add(photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        UsuarioInfo usuario = parent.getHost().getUsuarioActual();
        String usernameDisplay = (usuario != null) ? usuario.getNombreCompletoVisible() : "Sin usuario";
        String idDisplay = (usuario != null) ? "ID: " + usuario.getIdUsuario() : "ID: N/A";

        JLabel uBox = new JLabel(usernameDisplay, SwingConstants.CENTER);
        uBox.setOpaque(true);
        uBox.setBackground(Ventana.ACCENT_RED);
        uBox.setForeground(Color.WHITE);
        uBox.setBounds(20, 240, 200, 30);
        uBox.setFont(new Font("Arial Black", Font.BOLD, 16));
        card.add(uBox);

        JLabel lblID = new JLabel(idDisplay, SwingConstants.CENTER);
        lblID.setBounds(20, 275, 200, 20);
        card.add(lblID);

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBounds(50, 310, 140, 30);
        btnRegresar.setBackground(Ventana.ACCENT_RED);
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.addActionListener(e -> parent.setContenido(new PnlPerfil(parent)));
        card.add(btnRegresar);

        JButton btnCerrar = new JButton("Cerrar sesion");
        btnCerrar.setBounds(50, 350, 140, 30);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setBorder(new LineBorder(Ventana.ACCENT_RED, 1, true));
        btnCerrar.setForeground(Ventana.ACCENT_RED);
        btnCerrar.addActionListener(e -> parent.getHost().mostrarConfirmacionCerrarSesion());
        card.add(btnCerrar);
    }

    private String obtenerApellidos(UsuarioInfo usuario) {
        StringBuilder apellidos = new StringBuilder();
        agregarTexto(apellidos, usuario.getPrimerApellido());
        agregarTexto(apellidos, usuario.getSegundoApellido());
        return apellidos.toString();
    }

    private void agregarTexto(StringBuilder destino, String texto) {
        if (texto != null && !texto.trim().isEmpty()) {
            if (destino.length() > 0) {
                destino.append(" ");
            }
            destino.append(texto.trim());
        }
    }

    private JTextField crearCampo(String t, int x, int y, int w, JPanel p) {
        JLabel l = new JLabel(t);
        l.setBounds(x, y, w, 20);
        l.setFont(new Font("Arial", Font.BOLD, 13));
        p.add(l);

        JTextField tf = new JTextField();
        tf.setBounds(x, y + 22, w, 28);
        tf.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        p.add(tf);
        return tf;
    }
}
