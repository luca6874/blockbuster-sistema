package frontend.src.mainpackage;

import javax.swing.*;
import java.awt.*;

public class PnlPerfilPrincipal extends JPanel {
    public PnlPerfilPrincipal(ViewDashboard parent) {
        this.setLayout(null);
        this.setBackground(Ventana.CARD_WHITE);

        // Barra Superior
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(110, 60, 70));
        bar.setBounds(0, 0, 780, 40);
        JLabel tBar = new JLabel("  Perfil de Administrador", SwingConstants.LEFT);
        tBar.setForeground(Color.WHITE);
        bar.add(tBar);
        this.add(bar);

        // Contenido Central
        JLabel photo = parentIcon(parent, "/frontend/src/images/iconUserBig.png", 180, 180);
        photo.setBounds(300, 80, 180, 180);
        this.add(photo);

        JLabel uBox = new JLabel("Username", SwingConstants.CENTER);
        uBox.setOpaque(true);
        uBox.setBackground(Ventana.ACCENT_RED);
        uBox.setForeground(Color.WHITE);
        uBox.setFont(new Font("Arial Black", Font.BOLD, 18));
        uBox.setBounds(290, 280, 200, 35);
        this.add(uBox);

        JLabel lID = new JLabel("ID: 524264262277", SwingConstants.CENTER);
        lID.setBounds(290, 320, 200, 25);
        this.add(lID);

        JButton bInf = new JButton("Ver informacion");
        bInf.setBounds(290, 360, 200, 30);
        bInf.setBackground(Ventana.ACCENT_RED);
        bInf.setForeground(Color.WHITE);
        bInf.setFocusPainted(false);
        // Acción para inyectar el nuevo panel de información
        bInf.addActionListener(e -> parent.setContenido(new PnlInfoEmpleado(parent)));
        this.add(bInf);

        JButton bCs = new JButton("Cerrar sesion");
        bCs.setBounds(290, 400, 200, 30);
        bCs.setContentAreaFilled(false);
        bCs.setBorder(BorderFactory.createLineBorder(Ventana.ACCENT_RED));
        bCs.setForeground(Ventana.ACCENT_RED);
        bCs.addActionListener(e -> parent.getHost().router("login"));
        this.add(bCs);
    }

    private JLabel parentIcon(ViewDashboard p, String r, int w, int h) {
        try {
            ImageIcon i = new ImageIcon(getClass().getResource(r));
            return new JLabel(new ImageIcon(i.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH)));
        } catch(Exception e) { return new JLabel("!"); }
    }
}