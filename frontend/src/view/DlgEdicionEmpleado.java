package frontend.src.view;

import frontend.src.controller.Ventana;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DlgEdicionEmpleado extends JDialog {
    private final Ventana host;

    public DlgEdicionEmpleado(Ventana host) {
        super(host, true);
        this.host = host;
        this.setUndecorated(true);
        this.setSize(700, 550);
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

        JLabel lblTitulo = new JLabel("Formulario de Actualización de Datos");
        lblTitulo.setBounds(40, 30, 400, 25);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Ventana.MAROON_BG);
        content.add(lblTitulo);

        int yIn = 80;
        crearCampo("Nombres del empleado", 40, yIn, 620, content);
        crearCampo("Apellidos", 40, yIn + 70, 620, content);
        crearCampo("RFC", 40, yIn + 140, 300, content);
        crearCampo("Curp", 360, yIn + 140, 300, content);
        crearCampo("Correo Electrónico", 40, yIn + 210, 620, content);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(40, 470, 300, 40);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setBorder(new LineBorder(Ventana.ACCENT_RED));
        btnCancelar.setForeground(Ventana.ACCENT_RED);
        btnCancelar.addActionListener(e -> { host.setOscurecer(false); this.dispose(); host.intentarRestaurarDashboard(); });
        content.add(btnCancelar);

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(360, 470, 300, 40);
        btnAceptar.setBackground(Ventana.ACCENT_RED);
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.addActionListener(e -> host.mostrarAvisoExitoso(this));
        content.add(btnAceptar);

        this.add(content);
    }

    private void crearCampo(String titulo, int x, int y, int w, JPanel p) {
        JLabel l = new JLabel(titulo); l.setBounds(x, y, w, 20); p.add(l);
        JTextField tf = new JTextField(); tf.setBounds(x, y + 25, w, 30);
        tf.setBorder(new LineBorder(new Color(220, 220, 220))); p.add(tf);
    }
}