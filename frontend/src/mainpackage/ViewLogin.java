package frontend.src.mainpackage;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ViewLogin extends JPanel {
    private final Ventana host;
    private JCheckBox chkAutorizado;

    public ViewLogin(Ventana host) {
        this.host = host;
        this.setLayout(new GridLayout(1, 2));
        initLadoIzquierdo();
        initLadoDerecho();
    }

    private void initLadoIzquierdo() {
        JPanel lp = new JPanel(new GridBagLayout()); lp.setBackground(new Color(25, 25, 25));
        JLabel lbl = new JLabel("<html><center>BIENVENIDO A<br>BRIARBUSTER</center></html>");
        lbl.setForeground(Ventana.ACCENT_RED); lbl.setFont(new Font("Arial Black", Font.BOLD, 32));
        lp.add(lbl); this.add(lp);
    }

    private void initLadoDerecho() {
        JPanel rp = new JPanel(null); rp.setBackground(Ventana.MAROON_BG);

        try {
            ImageIcon ex = new ImageIcon(getClass().getResource("/frontend/src/images/iconExit1.png"));
            JLabel lEx = new JLabel(new ImageIcon(ex.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
            lEx.setBounds(450, 540, 25, 25); lEx.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lEx.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) { host.mostrarConfirmacionSalida(); }
            });
            rp.add(lEx);
        } catch (Exception e) {}

        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g); Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Ventana.CARD_WHITE); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        card.setBounds(50, 50, 400, 500); card.setOpaque(false);
        
        JLabel t = new JLabel("ACCESO AL SISTEMA", SwingConstants.CENTER);
        t.setBounds(0, 20, 400, 40); t.setFont(new Font("Serif", Font.BOLD, 22)); card.add(t);

        crearIn("Nombre", 80, card); crearIn("E-mail", 145, card); crearIn("Contraseña", 210, card);

        chkAutorizado = new JCheckBox("Al acceder, confirmo ser personal autorizado");
        chkAutorizado.setBounds(40, 275, 320, 25); chkAutorizado.setOpaque(false);
        chkAutorizado.setFont(new Font("Arial", Font.PLAIN, 11)); card.add(chkAutorizado);

        JLabel pill = new JLabel("Ver políticas de acceso", SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g; g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Ventana.ACCENT_RED); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        pill.setBounds(120, 305, 160, 20); pill.setForeground(Color.WHITE); pill.setFont(new Font("Arial", Font.BOLD, 10));
        pill.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pill.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { host.mostrarDialogoPoliticas(chkAutorizado); }
        });
        card.add(pill);

        JButton btnL = new JButton("Iniciar sesión"); btnL.setBounds(40, 350, 320, 40);
        btnL.setBackground(Ventana.ACCENT_RED); btnL.setForeground(Color.WHITE); btnL.setFocusPainted(false);
        btnL.addActionListener(e -> {
            if(chkAutorizado.isSelected()) host.router("dashboard");
            else host.mostrarAlertaAutorizacion();
        });
        card.add(btnL);

        JButton btnR = new JButton("Registro"); btnR.setBounds(40, 400, 320, 40);
        btnR.setContentAreaFilled(false); btnR.setForeground(Ventana.ACCENT_RED);
        btnR.setBorder(BorderFactory.createLineBorder(Ventana.ACCENT_RED, 2));
        btnR.addActionListener(e -> host.router("registro"));
        card.add(btnR);

        rp.add(card); this.add(rp);
    }

    private void crearIn(String t, int y, JPanel p) {
        JLabel l = new JLabel(t); l.setBounds(40, y, 200, 20); l.setFont(new Font("Arial", Font.BOLD, 12)); p.add(l);
        JTextField f = t.equals("Contraseña") ? new JPasswordField() : new JTextField();
        f.setBounds(40, y + 20, 320, 25); f.setBorder(new MatteBorder(0,0,1,0, Color.BLACK));
        f.setBackground(Ventana.CARD_WHITE); p.add(f);
    }
}