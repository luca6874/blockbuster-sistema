package frontend.src.mainpackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Contenedor principal de administración.
 * CUMPLIMIENTO REGLA DE ORO: Archivo completo. Se quitó icono de Gestión de Clientes.
 */
public class ViewDashboard extends JPanel {
    private final Ventana host;
    private JPanel mainContent; 
    private JPanel sidebar; 
    private JPanel itemSeleccionado; 
    private JPanel itemPanelActual; 

    private JPanel pPerfil;
    private JPanel pClientes;

    public ViewDashboard(Ventana host) {
        this.host = host;
        this.setLayout(new BorderLayout());
        
        initSidebar();
        
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Ventana.CARD_WHITE);
        this.add(mainContent, BorderLayout.CENTER);

        // Al inicio, el panel actual es Perfil
        itemPanelActual = pPerfil;
        resaltarItem(pPerfil);
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
        lLogo.setFont(new Font("Arial Black", Font.BOLD, 20));
        pLogo.add(lLogo);
        sidebar.add(pLogo);

        // --- NAVEGACIÓN SUPERIOR ---
        // CUMPLIMIENTO REGLA DE ORO: Se pasa null para eliminar el icono
        pClientes = crearItemSidebar("Gestión de Clientes", null, 80);
        pClientes.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                itemPanelActual = pClientes;
                resaltarItem(pClientes);
                setContenido(new PnlGestionClientes(ViewDashboard.this));
            }
        });
        sidebar.add(pClientes);

        JPanel pVideojuegos = crearItemSidebar("Videojuegos", null, 130);
        pVideojuegos.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                itemPanelActual = pVideojuegos;
                resaltarItem(pVideojuegos);
                setContenido(new PnlVideojuegos(ViewDashboard.this));
            }
        });
        sidebar.add(pVideojuegos);

        // --- BOTONES INFERIORES ---
        pPerfil = crearItemSidebar("Perfil", "/frontend/src/images/iconUser1.png", 470);
        pPerfil.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                itemPanelActual = pPerfil;
                resaltarItem(pPerfil);
                setContenido(new PnlPerfil(ViewDashboard.this));
            }
        });
        sidebar.add(pPerfil);

        JPanel pAyuda = crearItemSidebar("Ayuda y preguntas", "/frontend/src/images/iconSettings.png", 515);
        pAyuda.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                resaltarItem(pAyuda);
                host.mostrarDialogoAyuda();
            }
        });
        sidebar.add(pAyuda);

        JPanel pSalir = crearItemSidebar("Salir", "/frontend/src/images/iconExit1.png", 560);
        pSalir.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                resaltarItem(pSalir);
                host.mostrarConfirmacionSalida();
            }
        });
        sidebar.add(pSalir);

        this.add(sidebar, BorderLayout.WEST);
    }

    public void restaurarResaltado() {
        if (itemPanelActual != null) {
            resaltarItem(itemPanelActual);
        }
    }

    private JPanel crearItemSidebar(String texto, String rutaIcono, int y) {
        JPanel p = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getBackground().getAlpha() > 0) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(getBackground());
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                }
            }
        };
        p.setOpaque(false);
        p.setBackground(new Color(255, 255, 255, 0)); 
        p.setBounds(10, y, 200, 40);
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));

        int textoX = 45; // Posición normal del texto

        if (rutaIcono != null) {
            try {
                JLabel ico = icon(rutaIcono, 20, 20);
                ico.setBounds(15, 10, 20, 20);
                p.add(ico);
            } catch (Exception e) {}
        } else {
            textoX = 15; // Si no hay icono, el texto se mueve a la izquierda
        }

        JLabel txt = new JLabel(texto);
        txt.setBounds(textoX, 10, 170, 20);
        txt.setForeground(Color.WHITE);
        txt.setFont(new Font("Arial", Font.PLAIN, 13));
        p.add(txt);

        return p;
    }

    private void resaltarItem(JPanel item) {
        if (itemSeleccionado != null) {
            itemSeleccionado.setBackground(new Color(255, 255, 255, 0));
        }
        itemSeleccionado = item;
        item.setBackground(new Color(255, 255, 255, 40)); 
        sidebar.repaint();
    }

    public void setContenido(JPanel nuevoPanel) {
        mainContent.removeAll();
        mainContent.add(nuevoPanel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private JLabel icon(String path, int w, int h) {
        try {
            ImageIcon i = new ImageIcon(getClass().getResource(path));
            return new JLabel(new ImageIcon(i.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH)));
        } catch (Exception e) { return new JLabel(); }
    }

    public Ventana getHost() { return host; }
}