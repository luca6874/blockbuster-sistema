package frontend.src.mainpackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Panel detallado de información del empleado con formulario editable.
 */
public class PnlInfoEmpleado extends JPanel {
    private final ViewDashboard parent;

    public PnlInfoEmpleado(ViewDashboard parent) {
        this.parent = parent;
        this.setLayout(null);
        this.setBackground(Ventana.CARD_WHITE);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(110, 60, 70));
        topBar.setBounds(0, 0, 780, 40);
        JLabel tB = new JLabel("  Perfil de Administrador - Información de Empleado", SwingConstants.LEFT);
        tB.setForeground(Color.WHITE);
        tB.setFont(new Font("Arial", Font.BOLD, 14));
        topBar.add(tB);
        this.add(topBar);

        // Contenedor principal del formulario con bordes redondeados
        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.setColor(new Color(220, 220, 220)); g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            }
        };
        card.setBounds(30, 60, 720, 480);
        card.setOpaque(false);

        initLadoIdentidad(card);
        
        JSeparator s = new JSeparator(JSeparator.VERTICAL);
        s.setBounds(240, 40, 2, 400);
        card.add(s);

        int startX = 280;
        crearCampo("Nombres", startX, 40, 200, card);
        crearCampo("Apellidos", startX + 220, 40, 180, card);
        crearCampo("E-mail", startX, 110, 400, card);
        crearCampo("Contrasena", startX, 180, 300, card);
        crearCampo("Fecha de nacimiento", startX, 250, 150, card);

        JButton btnCambiar = new JButton("cambiar informacion");
        btnCambiar.setBounds(400, 400, 160, 35);
        btnCambiar.setContentAreaFilled(false);
        btnCambiar.setBorder(new LineBorder(Ventana.ACCENT_RED, 1, true));
        btnCambiar.setForeground(Ventana.ACCENT_RED);
        card.add(btnCambiar);

        JButton btnImprimir = new JButton("Imprimir info");
        btnImprimir.setBounds(570, 400, 130, 35);
        btnImprimir.setBackground(Ventana.ACCENT_RED);
        btnImprimir.setForeground(Color.WHITE);
        btnImprimir.setFocusPainted(false);
        card.add(btnImprimir);

        this.add(card);
    }

    private void initLadoIdentidad(JPanel card) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/frontend/src/images/iconUserBig.png"));
            JLabel photo = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
            photo.setBounds(30, 40, 180, 180);
            card.add(photo);
        } catch (Exception e) {}

        JLabel uBox = new JLabel("Username", SwingConstants.CENTER);
        uBox.setOpaque(true); 
        uBox.setBackground(Ventana.ACCENT_RED);
        uBox.setForeground(Color.WHITE); uBox.setBounds(20, 240, 200, 30);
        uBox.setFont(new Font("Arial Black", Font.BOLD, 16));
        card.add(uBox);

        JLabel lblID = new JLabel("ID: 524264262277", SwingConstants.CENTER);
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

    private void crearCampo(String t, int x, int y, int w, JPanel p) {
        JLabel l = new JLabel(t); l.setBounds(x, y, w, 20);
        l.setFont(new Font("Arial", Font.BOLD, 13)); p.add(l);
        JTextField tf = new JTextField();
        tf.setBounds(x, y + 22, w, 28);
        tf.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        p.add(tf);
    }
}