package frontend.src.view;

import frontend.src.service.ImageManager;
import frontend.src.dao.OperacionDAO;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Panel de Total Juegos - Conectado a base de datos en tiempo real.
 * Muestra:
 * - Total de videojuegos rentados
 * - Total de videojuegos vendidos
 * - Total de videojuegos pendientes de devolución
 */
public class PnlTotalJuego extends JPanel {

    private JLabel lblRentados;
    private JLabel lblVendidos;
    private JLabel lblPendientes;

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
        cargarDatos();
    }

    private void initComponentes() {

        // Tarjeta izquierda - Juegos Rentados
        JPanel cardRentados = createCardVertical(
                "Juegos", "rentados", "",
                "/frontend/src/images/iconVideoGame1.png",
                20, 20
        );
        lblRentados = (JLabel) cardRentados.getComponent(3); // El número es el 4º componente
        this.add(cardRentados);

        // Tarjeta derecha - Juegos Vendidos
        JPanel cardVendidos = createCardVertical(
                "Juegos", "vendidos", "",
                "/frontend/src/images/money.png",
                125, 20
        );
        lblVendidos = (JLabel) cardVendidos.getComponent(3); // El número es el 4º componente
        this.add(cardVendidos);

        // Texto inferior - Juegos Pendientes
        JLabel lblTexto = new JLabel("Total juegos pendientes :");
        lblTexto.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTexto.setBounds(20, 150, 170, 20);
        this.add(lblTexto);

        lblPendientes = new JLabel("0");
        lblPendientes.setFont(new Font("Arial", Font.BOLD, 16));
        lblPendientes.setBounds(190, 150, 40, 20);
        this.add(lblPendientes);
    }

    private void cargarDatos() {
        try {
            // Obtener datos de la BD
            int juegosRentados = OperacionDAO.contarTotalJuegosRentados();
            int juegosVendidos = OperacionDAO.contarTotalJuegosVendidos();
            int juegosPendientes = OperacionDAO.contarTotalJuegosPendientes();

            // Actualizar labels
            lblRentados.setText(String.valueOf(juegosRentados));
            lblVendidos.setText(String.valueOf(juegosVendidos));
            lblPendientes.setText(String.valueOf(juegosPendientes));
        } catch (Exception e) {
            System.err.println("Error al cargar datos de operaciones: " + e.getMessage());
            e.printStackTrace();
            // Valores por defecto en caso de error
            lblRentados.setText("0");
            lblVendidos.setText("0");
            lblPendientes.setText("0");
        }
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
            ImageIcon imageIcon = ImageManager.cargarImagenPreview(extraerNombreImagen(path), 30, 30);
            icon.setIcon(imageIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        icon.setBounds(0, 8, 100, 30);
        card.add(icon);

        // Texto línea 1
        JLabel t1 = new JLabel(l1, SwingConstants.CENTER);
        t1.setFont(new Font("Arial", Font.PLAIN, 11));
        t1.setBounds(0, 35, 100, 15);
        card.add(t1);

        // Texto línea 2
        JLabel t2 = new JLabel(l2, SwingConstants.CENTER);
        t2.setFont(new Font("Arial", Font.PLAIN, 11));
        t2.setBounds(0, 50, 100, 15);
        card.add(t2);

        // Número (inicialmente vacío, se llenará dinámicamente)
        JLabel num = new JLabel(cantidad, SwingConstants.CENTER);
        num.setFont(new Font("Arial", Font.BOLD, 20));
        num.setBounds(0, 70, 100, 30);
        card.add(num);

        return card;
    }

    private String extraerNombreImagen(String path) {
        if (path == null) return null;
        int slash = path.lastIndexOf('/');
        return slash >= 0 ? path.substring(slash + 1) : path;
    }
}
