package frontend.src.mainpackage;

import javax.swing.*;
import java.awt.*;

/**
 * Contenedor principal de administración que gestiona la sidebar y paneles dinámicos.
 */
public class ViewDashboard extends JPanel {
    private final Ventana host;
    private JPanel mainContent; 
    private JPanel sidebar; 

    public ViewDashboard(Ventana host) {
        this.host = host;
        this.setLayout(new BorderLayout());
        
        initSidebar();
        
        // Espacio destinado a la inyección de paneles (PnlPerfil, PnlInfoEmpleado, etc.)
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Ventana.CARD_WHITE);
        this.add(mainContent, BorderLayout.CENTER);

        setContenido(new PnlPerfil(this));
    }

    private void initSidebar() {
        sidebar = new JPanel(null);
        sidebar.setPreferredSize(new Dimension(220, 600));
        sidebar.setBackground(Ventana.MAROON_BG);

        JPanel pLogo = new JPanel(new BorderLayout()); 
        pLogo.setBackground(Color.WHITE); 
        pLogo.setBounds(0, 0, 220, 60);
        JLabel lLogo = new JLabel("BRIARBUSTER®", SwingConstants.CENTER); 
        lLogo.setForeground(Ventana.MAROON_BG);
        lLogo.setFont(new Font("Arial Black", Font.BOLD, 18)); 
        pLogo.add(lLogo); 
        sidebar.add(pLogo);

        String[] menu = {"Gestion de Clientes", "Videojuegos", "Rentas y Compras", "Nueva operacion"};
        for(int i = 0; i < menu.length; i++) {
            JLabel itm = new JLabel(menu[i]); 
            itm.setBounds(20, 80 + (i * 50), 180, 30);
            itm.setForeground(Color.WHITE); 
            itm.setFont(new Font("Arial", Font.PLAIN, 14)); 
            sidebar.add(itm);
        }

        // --- Navegación Inferior ---
        JPanel pPerfil = new JPanel(null); 
        pPerfil.setBackground(new Color(255, 255, 255, 40));
        pPerfil.setBounds(10, 430, 200, 40); 
        pPerfil.setBorder(BorderFactory.createLineBorder(new Color(110, 60, 70), 1, true));
        
        JLabel iUser = icon("/frontend/src/images/iconUser1.png", 20, 20); 
        iUser.setBounds(10, 10, 20, 20);
        JLabel lPerf = new JLabel("Perfil"); 
        lPerf.setBounds(40, 10, 100, 20); 
        lPerf.setForeground(Color.WHITE);
        pPerfil.add(iUser); 
        pPerfil.add(lPerf); 
        sidebar.add(pPerfil);

        JLabel iSet = icon("/frontend/src/images/iconSettings.png", 20, 20); 
        iSet.setBounds(20, 485, 20, 20);
        JLabel lHelp = new JLabel("Ayuda y preguntas"); 
        lHelp.setBounds(50, 485, 150, 20); 
        lHelp.setForeground(Color.WHITE);
        sidebar.add(iSet); 
        sidebar.add(lHelp);

        JLabel iEx = icon("/frontend/src/images/iconExit1.png", 20, 20); 
        iEx.setBounds(20, 525, 20, 20);
        JLabel lEx = new JLabel("Salir"); 
        lEx.setBounds(50, 525, 100, 20); 
        lEx.setForeground(Color.WHITE);
        lEx.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lEx.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { host.mostrarConfirmacionSalida(); }
        });
        sidebar.add(iEx); 
        sidebar.add(lEx);

        this.add(sidebar, BorderLayout.WEST);
    }

    /**
     * Intercambia el panel central del dashboard.
     */
    public void setContenido(JPanel nuevoPanel) {
        mainContent.removeAll();
        mainContent.add(nuevoPanel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    public Ventana getHost() { return host; }

    private JLabel icon(String r, int w, int h) {
        try {
            ImageIcon i = new ImageIcon(getClass().getResource(r));
            return new JLabel(new ImageIcon(i.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH)));
        } catch(Exception e) { 
            return new JLabel("!"); 
        }
    }
}