package frontend.src.mainpackage;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Panel de Total Juegos - Versión Corregida con Tamaño Ajustado.
 */
public class PnlTotalJuego extends JPanel {

    public PnlTotalJuego() {
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(250, 200));
        this.setSize(250, 200);

        this.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        initComponentes();
    }

    private void initComponentes() {

        // Tarjeta izquierda
        JPanel cardRentados = createCardVertical(
                "Juegos", "rentados", "20",
                "/frontend/src/images/iconVideoGame1.png",
                20, 20
        );
        this.add(cardRentados);

        // Tarjeta derecha
        JPanel cardVendidos = createCardVertical(
                "Juegos", "vendidos", "18",
                "/frontend/src/images/money.png",
                125, 20
        );
        this.add(cardVendidos);

        // Texto inferior
        JLabel lblPendientes = new JLabel("Total juegos pendientes :");
        lblPendientes.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPendientes.setBounds(20, 150, 170, 20);
        this.add(lblPendientes);

        JLabel lblNum = new JLabel("18");
        lblNum.setFont(new Font("Arial", Font.BOLD, 16));
        lblNum.setBounds(190, 150, 40, 20);
        this.add(lblNum);
    }

    private JPanel createCardVertical(String l1, String l2, String cantidad, String path, int x, int y) {

        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(210, 210, 210));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };

        card.setOpaque(false);
        card.setBounds(x, y, 100, 110);

        // Icono
        JLabel icon = new JLabel();
icon.setHorizontalAlignment(SwingConstants.CENTER);

try {
    URL url = getClass().getResource(path);

    if (url == null) {
        System.out.println("No se encontró: " + path);
    } else {
        Image img = new ImageIcon(url).getImage()
                .getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        icon.setIcon(new ImageIcon(img));
    }

} catch (Exception e) {
    e.printStackTrace();
}


icon.setBounds(0, 8, 100, 30);
card.add(icon);

        // Texto
        JLabel t1 = new JLabel(l1, SwingConstants.CENTER);
        t1.setFont(new Font("Arial", Font.PLAIN, 11));
        t1.setBounds(0, 35, 100, 15);
        card.add(t1);

        JLabel t2 = new JLabel(l2, SwingConstants.CENTER);
        t2.setFont(new Font("Arial", Font.PLAIN, 11));
        t2.setBounds(0, 50, 100, 15);
        card.add(t2);

        // Número
        JLabel num = new JLabel(cantidad, SwingConstants.CENTER);
        num.setFont(new Font("Arial", Font.BOLD, 20));
        num.setBounds(0, 70, 100, 30);
        card.add(num);

        return card;
    }
}