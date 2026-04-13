package frontend.src.mainpackage;

import javax.swing.*;
import java.awt.*;

public class ViewSplash extends JPanel {
    public ViewSplash(Ventana host) {
        this.setBackground(Ventana.MAROON_BG);
        this.setLayout(new GridBagLayout());
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/frontend/src/images/ticket_briarbuster.png"));
            JLabel lbl = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(600, -1, Image.SCALE_SMOOTH)));
            lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lbl.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) { host.router("login"); }
            });
            this.add(lbl);
        } catch (Exception e) { this.add(new JLabel("BRIARBUSTER - CLIC PARA ENTRAR")); }
    }
}