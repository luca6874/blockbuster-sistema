package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.service.ImageManager;

import javax.swing.*;
import java.awt.*;

public class ViewSplash extends JPanel {
    public ViewSplash(Ventana host) {
        this.setBackground(Ventana.MAROON_BG);
        this.setLayout(new GridBagLayout());
        try {
            JLabel lbl = new JLabel(ImageManager.cargarImagenPreview("ticket_briarbuster.png", 600, 600));
            lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lbl.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) { host.router("login"); }
            });
            this.add(lbl);
        } catch (Exception e) { this.add(new JLabel("BRIARBUSTER - CLIC PARA ENTRAR")); }
    }
}
