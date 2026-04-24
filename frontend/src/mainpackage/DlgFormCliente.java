package frontend.src.mainpackage;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Diálogo para registrar o editar clientes.
 */
public class DlgFormCliente extends JDialog {
    private final Ventana host;

    public DlgFormCliente(Ventana host) {
        super(host, true);
        this.host = host;
        this.setUndecorated(true);
        this.setSize(700, 500);
        this.setLocationRelativeTo(host);

        JPanel content = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Ventana.CARD_WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        content.setOpaque(false);

        JLabel lblTit = new JLabel("Registro de Nuevo Cliente");
        lblTit.setBounds(40, 30, 400, 25);
        lblTit.setFont(new Font("Arial", Font.BOLD, 18));
        lblTit.setForeground(Ventana.MAROON_BG);
        content.add(lblTit);

        // Campos según Figma
        int y = 80;
        crearCampo("Nombres del Cliente", 40, y, 280, content);
        crearCampo("Apellidos", 360, y, 280, content);
        crearCampo("Correo Electrónico", 40, y + 70, 620, content);
        crearCampo("Teléfono de Contacto", 40, y + 140, 280, content);
        crearCampo("Nivel de Fidelidad (Plata/Oro/Platino)", 360, y + 140, 280, content);

        JButton btnCan = new JButton("Cancelar");
        btnCan.setBounds(40, 420, 300, 40);
        btnCan.setContentAreaFilled(false);
        btnCan.setBorder(new LineBorder(Ventana.ACCENT_RED));
        btnCan.setForeground(Ventana.ACCENT_RED);
        btnCan.addActionListener(e -> {
            host.setOscurecer(false);
            this.dispose();
            host.intentarRestaurarDashboard();
        });
        content.add(btnCan);

        JButton btnReg = new JButton("Registrar Cliente");
        btnReg.setBounds(360, 420, 300, 40);
        btnReg.setBackground(Ventana.ACCENT_RED);
        btnReg.setForeground(Color.WHITE);
        btnReg.addActionListener(e -> {
            // Mostrar aviso de éxito (Imagen image_a0e1e7)
            host.mostrarAvisoExitosoCliente(this);
        });
        content.add(btnReg);

        this.add(content);
    }

    private void crearCampo(String t, int x, int y, int w, JPanel p) {
        JLabel l = new JLabel(t); l.setBounds(x, y, w, 20); l.setFont(new Font("Arial", Font.BOLD, 12)); p.add(l);
        JTextField tf = new JTextField(); tf.setBounds(x, y + 25, w, 30);
        tf.setBorder(new LineBorder(new Color(220, 220, 220))); p.add(tf);
    }
}