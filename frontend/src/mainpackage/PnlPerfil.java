package frontend.src.mainpackage;

import java.awt.*;
import javax.swing.*;

/**
 * Vista resumida del administrador dentro del Dashboard.
 */
public class PnlPerfil extends JPanel {
    private final ViewDashboard parent;

    public PnlPerfil(ViewDashboard parent) {
        this.parent = parent;
        this.setLayout(null);
        this.setBackground(Ventana.CARD_WHITE);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(110, 60, 70));
        topBar.setBounds(0, 0, 1000, 40);
        JLabel lblTitulo = new JLabel("  Perfil de Administrador", SwingConstants.LEFT);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        topBar.add(lblTitulo);
        this.add(topBar);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/frontend/src/images/iconUserBig.png"));
            JLabel userPhoto = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
            userPhoto.setBounds(300, 80, 180, 180);
            this.add(userPhoto);
        } catch (Exception e) {}

        JLabel lblUserBox = new JLabel("Username", SwingConstants.CENTER);
        lblUserBox.setOpaque(true);
        lblUserBox.setBackground(Ventana.ACCENT_RED); 
        lblUserBox.setForeground(Color.WHITE);
        lblUserBox.setFont(new Font("Arial Black", Font.BOLD, 18));
        lblUserBox.setBounds(290, 280, 200, 35);
        this.add(lblUserBox);

        JLabel lblID = new JLabel("ID: 524264262277", SwingConstants.CENTER);
        lblID.setBounds(290, 320, 200, 25);
        this.add(lblID);

        JButton btnInfo = new JButton("Ver informacion");
        btnInfo.setBounds(290, 360, 200, 30);
        btnInfo.setBackground(Ventana.ACCENT_RED);
        btnInfo.setForeground(Color.WHITE);
        btnInfo.setFocusPainted(false);
        btnInfo.addActionListener(e -> parent.setContenido(new PnlInfoEmpleado(parent)));
        this.add(btnInfo);

        JButton btnCerrar = new JButton("Cerrar sesion");
        btnCerrar.setBounds(290, 400, 200, 30);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setBorder(BorderFactory.createLineBorder(Ventana.ACCENT_RED));
        btnCerrar.setForeground(Ventana.ACCENT_RED);
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> parent.getHost().mostrarConfirmacionCerrarSesion());
        this.add(btnCerrar);
    }
}