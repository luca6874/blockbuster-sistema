package frontend.src.view;

import frontend.src.controller.Ventana;

import javax.swing.*;
import java.awt.*;

public class DlgAvisoExitoso extends JDialog {
    public DlgAvisoExitoso(DlgEdicionEmpleado padre, Ventana host) {
        super(padre, true);
        this.setUndecorated(true);
        this.setSize(400, 200);
        this.setLocationRelativeTo(padre);

        JPanel content = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Ventana.CARD_WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.setColor(Ventana.ACCENT_RED);
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            }
        };
        content.setOpaque(false);

        JLabel lblTxt = new JLabel("<html><center>¡Operación Exitosa!<br>Los datos han sido actualizados.</center></html>", SwingConstants.CENTER);
        lblTxt.setBounds(20, 40, 360, 50);
        lblTxt.setFont(new Font("Arial", Font.BOLD, 16));
        lblTxt.setForeground(Ventana.MAROON_BG);
        content.add(lblTxt);

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(100, 130, 200, 40);
        btnAceptar.setBackground(Ventana.ACCENT_RED);
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.addActionListener(e -> {
            this.dispose();
            padre.dispose();
            host.setOscurecer(false);
            host.intentarRestaurarDashboard();
        });
        content.add(btnAceptar);

        this.add(content);
    }
}