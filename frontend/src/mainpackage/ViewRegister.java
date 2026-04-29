package frontend.src.mainpackage;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;

public class ViewRegister extends JPanel {
    private final Ventana host;
    private JCheckBox chk;

    public ViewRegister(Ventana host) {
        this.host = host; this.setLayout(null); this.setBackground(Ventana.MAROON_BG);
        init();
    }

    private void init() {
        JPanel c = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g); Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Ventana.CARD_WHITE); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        c.setBounds(350, 25, 500, 520); c.setOpaque(false);

        JLabel tit = new JLabel("REGISTRO DEL ADMINISTRADOR - NUEVO PERFIL", SwingConstants.CENTER);
        tit.setBounds(0, 20, 500, 30); 
        tit.setFont(new Font("Georgia", Font.PLAIN, 18)); 
        tit.setForeground(Ventana.ACCENT_RED);
        c.add(tit);

        crearCol("Nombres", 40, 80, 200, c); crearCol("Apellidos", 260, 80, 200, c);
        crearCol("E-mail", 40, 140, 420, c); crearCol("Contrasena", 40, 200, 200, c);
        crearCol("Confirmar", 260, 200, 200, c); crearCol("ID Empleado", 40, 290, 420, c);

        chk = new JCheckBox("Al acceder, confirmo que soy personal autorizado");
        chk.setBounds(40, 360, 420, 25); chk.setOpaque(false); c.add(chk);

        JButton btnC = new JButton("Crear Perfil"); btnC.setBounds(40, 410, 420, 40);
        btnC.setBackground(Ventana.ACCENT_RED); btnC.setForeground(Color.WHITE);
        btnC.addActionListener(e -> {
            if(chk.isSelected()) host.router("dashboard");
            else host.mostrarAlertaAutorizacion();
        });
        c.add(btnC);

        JButton btnB = new JButton("Cancelar"); btnB.setBounds(40, 460, 420, 35);
        btnB.setContentAreaFilled(false); btnB.setBorder(BorderFactory.createLineBorder(Ventana.ACCENT_RED));
        btnB.addActionListener(e -> host.router("login"));
        c.add(btnB);

        this.add(c);
    }

    private void crearCol(String t, int x, int y, int w, JPanel p) {
        JLabel l = new JLabel(t); l.setBounds(x, y, w, 20); p.add(l);
        JTextField f = (t.contains("Contrasena") || t.contains("Confirmar")) ? new JPasswordField() : new JTextField();
        f.setBounds(x, y + 20, w, 25); f.setBorder(new MatteBorder(0,0,1,0, Color.BLACK));
        f.setBackground(Ventana.CARD_WHITE); p.add(f);
    }
}