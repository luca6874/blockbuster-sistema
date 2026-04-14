package frontend.src.mainpackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.net.URL;

/**
 * Panel de Total Juegos - Versión Corregida con Tamaño Ajustado.
 */
public class PnlTotalJuego extends JPanel {

    public PnlTotalJuego() {
        // Aumentamos el tamaño para que quepa todo sin cortarse
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(350, 300)); // Tamaño sugerido para el contenedor
        this.setSize(350, 300); 

        // Borde exterior redondeado
        this.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        initComponentes();
    }

    private void initComponentes() {
        // 1. Título Superior - Ajustamos posición y ancho
        JLabel lblTituloPrincipal = new JLabel("Total juegos (lapso 6 meses)");
        lblTituloPrincipal.setFont(new Font("Arial", Font.PLAIN, 18));
        lblTituloPrincipal.setBounds(20, 15, 300, 25);
        this.add(lblTituloPrincipal);

        // Línea divisoria
        JSeparator separador = new JSeparator();
        separador.setBounds(20, 45, 310, 2);
        separador.setForeground(new Color(230, 230, 230));
        this.add(separador);

        // 2. Tarjetas - Ajustamos X para que estén centradas en los 350px
        // Tarjeta Izquierda (x=25)
        JPanel cardRentados = createCardVertical("Juegos", "rentados", "20", "/frontend/src/images/iconVideoGame1.png", 25, 65);
        this.add(cardRentados);

        // Tarjeta Derecha (x=190)
        JPanel cardComprados = createCardVertical("Juegos", "comprados", "18", "/frontend/src/images/icon$.png", 190, 65);
        this.add(cardComprados);

        // 3. Sección inferior - Subimos un poco el Y para que no quede al ras
        JLabel lblPendientes = new JLabel("Juegos pendientes de entrega: ");
        lblPendientes.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPendientes.setBounds(25, 250, 250, 30);
        this.add(lblPendientes);

        JLabel lblNumPendientes = new JLabel("18");
        lblNumPendientes.setFont(new Font("Arial", Font.BOLD, 22));
        lblNumPendientes.setBounds(255, 248, 50, 30);
        this.add(lblNumPendientes);
    }

    private JPanel createCardVertical(String linea1, String linea2, String cantidad, String iconPath, int x, int y) {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2d.setColor(new Color(210, 210, 210)); 
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setBounds(x, y, 135, 170); // Ancho de tarjeta ajustado a 135

        // Icono
        JLabel lblIcono = new JLabel();
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                lblIcono.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {}
        lblIcono.setBounds(0, 15, 135, 40);
        card.add(lblIcono);

        // Texto descriptivo
        JLabel txt1 = new JLabel(linea1, SwingConstants.CENTER);
        txt1.setFont(new Font("Arial", Font.PLAIN, 14));
        txt1.setBounds(0, 55, 135, 20);
        card.add(txt1);

        JLabel txt2 = new JLabel(linea2, SwingConstants.CENTER);
        txt2.setFont(new Font("Arial", Font.PLAIN, 14));
        txt2.setBounds(0, 75, 135, 20);
        card.add(txt2);

        // Número
        JLabel lblCant = new JLabel(cantidad, SwingConstants.CENTER);
        lblCant.setFont(new Font("Arial", Font.BOLD, 32));
        lblCant.setBounds(0, 110, 135, 40);
        card.add(lblCant);

        return card;
    }
}