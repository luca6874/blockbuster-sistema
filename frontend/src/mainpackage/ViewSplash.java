package frontend.src.mainpackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ViewSplash extends JPanel {
    private Ventana host;
    private final Color MAROON_BG = new Color(61, 0, 0);

    public ViewSplash(Ventana host) {
        this.host = host;
        this.setBackground(MAROON_BG);
        this.setLayout(new GridBagLayout()); 

        try {
            // Ruta absoluta rápida que te funcionó
            ImageIcon ticketIcon = new ImageIcon(getClass().getResource("/frontend/src/images/ticket_briarbuster.png"));
            Image scaledImage = ticketIcon.getImage().getScaledInstance(800, -1, Image.SCALE_SMOOTH);
            JLabel ticketLabel = new JLabel(new ImageIcon(scaledImage));

            ticketLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            ticketLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    host.router("login");
                }
            });

            this.add(ticketLabel);
        } catch (Exception e) {
            this.add(new JLabel("ERROR AL CARGAR IMAGEN"));
        }
    }
}