package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.model.UsuarioInfo;
import frontend.src.service.ImageManager;

import java.awt.*;
import javax.swing.*;

/**
 * Vista resumida del administrador dentro del Dashboard.
 */
public class PnlPerfil extends JPanel {

    public PnlPerfil(ViewDashboard parent) {
        this.setLayout(null);
        this.setBackground(Ventana.CARD_WHITE);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(110, 60, 70));
        topBar.setBounds(0, 0, 2000, 40);
        JLabel lblTitulo = new JLabel("  Perfil de Administrador", SwingConstants.LEFT);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        topBar.add(lblTitulo);
        this.add(topBar);

        try {
            JLabel userPhoto = new JLabel(ImageManager.cargarImagenPreview("iconUserBig.png", 180, 180));
            userPhoto.setBounds(300, 80, 180, 180);
            this.add(userPhoto);
        } catch (Exception e) {}

        // Obtener usuario actual autenticado
        UsuarioInfo usuario = parent.getHost().getUsuarioActual();
        
        // Usar datos reales o mostrar valores por defecto si no hay usuario
        String usernameDisplay = (usuario != null) ? usuario.getNombreCompletoVisible() : "Sin usuario";
        String idDisplay = (usuario != null) ? "ID: " + usuario.getIdUsuario() : "ID: N/A";

        JLabel lblUserBox = new JLabel(usernameDisplay, SwingConstants.CENTER);
        lblUserBox.setOpaque(true);
        lblUserBox.setBackground(Ventana.ACCENT_RED); 
        lblUserBox.setForeground(Color.WHITE);
        lblUserBox.setFont(new Font("Arial Black", Font.BOLD, 18));
        lblUserBox.setBounds(290, 280, 200, 35);
        this.add(lblUserBox);

        JLabel lblID = new JLabel(idDisplay, SwingConstants.CENTER);
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
