package frontend.src.mainpackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.net.URL;

/**
 * Panel de Resumen del Cliente.
 */
public class PnlResumenCliente extends JPanel {
    private ViewDashboard parent;

    public PnlResumenCliente(ViewDashboard parent) {
        this.parent = parent;
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(250, 300));
        this.setSize(250, 300);
        this.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        initComponentes();
    }

    private void initComponentes() {
        addFecha();
        addAvatarYCabecera();
        addInformacionBasica();
        addEstadisticas();
        addAcciones();
    }

    private void addFecha() {
        JLabel lblFecha = new JLabel("05/02/2004", SwingConstants.RIGHT);
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 10));
        lblFecha.setForeground(new Color(100, 100, 100));
        lblFecha.setBounds(120, 10, 120, 16);
        this.add(lblFecha);
    }

    private void addAvatarYCabecera() {
        JPanel avatarCont = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 245, 245));
                g2.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        avatarCont.setOpaque(false);
        avatarCont.setBounds(15, 30, 50, 50);

        JLabel icon = new JLabel();
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setBounds(0, 0, 50, 50);
        try {
            URL url = getClass().getResource("/frontend/src/images/iconUserBig.png");
            if (url != null) {
                Image img = new ImageIcon(url).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH);
                icon.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {
        }
        avatarCont.add(icon);
        this.add(avatarCont);

        JLabel lblNombre = new JLabel("Luis Spinetta");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 13));
        lblNombre.setForeground(Color.BLACK);
        lblNombre.setBounds(80, 30, 150, 18);
        this.add(lblNombre);

        JLabel lblEmail = new JLabel("pescado@rabioso.com");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 10));
        lblEmail.setForeground(new Color(120, 120, 120));
        lblEmail.setBounds(80, 50, 150, 16);
        this.add(lblEmail);

        this.add(createBadge("Activo", new Color(46, 204, 113), Color.WHITE, 80, 70, 65, 18));
        this.add(createBadge("Cliente frecuente", new Color(245, 193, 65), new Color(80, 60, 20), 150, 70, 85, 18));
    }

    private void addInformacionBasica() {
        JLabel lblId = new JLabel("ID: 13587");
        lblId.setFont(new Font("Arial", Font.PLAIN, 11));
        lblId.setForeground(Color.BLACK);
        lblId.setBounds(15, 100, 110, 16);
        this.add(lblId);

        JLabel lblTelefono = new JLabel("55 271 4314", SwingConstants.RIGHT);
        lblTelefono.setFont(new Font("Arial", Font.PLAIN, 11));
        lblTelefono.setForeground(Color.BLACK);
        lblTelefono.setBounds(130, 100, 105, 16);
        this.add(lblTelefono);
    }

    private void addEstadisticas() {
        int y = 125;
        this.add(createStatCard("/frontend/src/images/iconVideoGame1.png", "Juegos rentados", "3", 10, y));
        this.add(createStatCard("/frontend/src/images/money.png", "Juegos comprados", "2", 67, y));
        this.add(createStatCard("/frontend/src/images/discount.png", "Descuentos usados", "0", 124, y));
        this.add(createStatCard("/frontend/src/images/level.png", "Desc. frecuencia", "SI", 181, y));
    }

    private void addAcciones() {
        JButton btnDescargar = createActionButton("Descargar info", new Color(110, 60, 70));
        btnDescargar.setBounds(15, 205, 110, 26);
        this.add(btnDescargar);

        JButton btnTarjeta = createActionButton("Generar tarjeta", new Color(152, 33, 54));
        btnTarjeta.setBounds(130, 205, 110, 26);
        this.add(btnTarjeta);

        JButton btnEditar = createActionButton("Editar cliente", new Color(46, 204, 113));
        btnEditar.setBounds(15, 235, 110, 26);
        btnEditar.addActionListener(e -> {
            // Obtener datos del cliente desde el panel
            String clienteId = obtenerClienteId();
            String nombres = obtenerNombres();
            String apellidos = obtenerApellidos();
            String email = obtenerEmail();
            String telefono = obtenerTelefono();
            String fechaNacimiento = obtenerFechaNacimiento();
            
            DlgEdicionCliente dlgEditar = new DlgEdicionCliente(parent.getHost(), clienteId, nombres, apellidos, email, telefono, fechaNacimiento);
            parent.getHost().setOscurecer(true);
            dlgEditar.setVisible(true);
        });
        this.add(btnEditar);

        JButton btnEliminar = createActionButton("Eliminar cliente", new Color(231, 76, 60));
        btnEliminar.setBounds(130, 235, 110, 26);
        this.add(btnEliminar);
    }

    private JPanel createStatCard(String iconPath, String title, String value, int x, int y) {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(220, 220, 220));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        card.setOpaque(false);
        card.setBounds(x, y, 55, 70);

        JLabel icon = new JLabel();
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setBounds(0, 4, 55, 18);
        try {
            URL url = getClass().getResource(iconPath);
            if (url != null) {
                Image img = new ImageIcon(url).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                icon.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {
        }
        card.add(icon);

        JLabel lblTitle = new JLabel("<html><center>" + title + "</center></html>", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 8));
        lblTitle.setForeground(new Color(100, 100, 100));
        lblTitle.setBounds(2, 22, 51, 24);
        card.add(lblTitle);

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 14));
        lblValue.setForeground(Color.BLACK);
        lblValue.setBounds(0, 46, 55, 22);
        card.add(lblValue);

        return card;
    }

    private JLabel createBadge(String text, Color background, Color foreground, int x, int y, int width, int height) {
        JLabel badge = new JLabel(text, SwingConstants.CENTER);
        badge.setFont(new Font("Arial", Font.BOLD, 9));
        badge.setOpaque(true);
        badge.setBackground(background);
        badge.setForeground(foreground);
        badge.setBounds(x, y, width, height);
        badge.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1, true));
        return badge;
    }

    private JButton createActionButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setForeground(Color.WHITE);
        button.setBackground(bg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Métodos para obtener datos del cliente (datos simulados)
    private String obtenerClienteId() {
        return "13587";
    }

    private String obtenerNombres() {
        return "Luis";
    }

    private String obtenerApellidos() {
        return "Spinetta";
    }

    private String obtenerEmail() {
        return "pescado@rabioso.com";
    }

    private String obtenerTelefono() {
        return "55 271 4314";
    }

    private String obtenerFechaNacimiento() {
        return "05/02/2004";
    }
}
