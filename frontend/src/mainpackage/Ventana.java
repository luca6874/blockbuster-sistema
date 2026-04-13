package frontend.src.mainpackage;

import javax.swing.*;
import java.awt.*;

/**
 * JFrame principal que actúa como host para las vistas y gestiona diálogos modales.
 */
public class Ventana extends JFrame {
    
    public static final Color MAROON_BG = new Color(61, 0, 0);       
    public static final Color ACCENT_RED = new Color(152, 33, 54);  
    public static final Color CARD_WHITE = new Color(245, 245, 245);

    public Ventana() {
        this.setSize(1000, 600); 
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        this.setLocationRelativeTo(null);
        this.setResizable(false); 
        this.setTitle("Briarbuster - Sistema Administrativo");
        this.setLayout(new BorderLayout()); 

        router("splash");

        // Captura el evento de cierre de ventana para mostrar confirmación
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) { mostrarConfirmacionSalida(); }
        });

        this.setVisible(true);
    }

    /**
     * Sistema de navegación principal entre vistas completas.
     */
    public final void router(String target) {
        this.getContentPane().removeAll();
        if (target.equals("splash")) this.add(new ViewSplash(this));
        else if (target.equals("login")) this.add(new ViewLogin(this));
        else if (target.equals("registro")) this.add(new ViewRegister(this)); 
        else if (target.equals("dashboard")) this.add(new ViewDashboard(this));
        this.revalidate();
        this.repaint();
    }

    /**
     * Crea un efecto de profundidad oscureciendo el fondo para diálogos modales.
     */
    public void setOscurecer(boolean activar) {
        if (activar) {
            JPanel panelOscuro = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    g.setColor(getBackground());
                    g.fillRect(0, 0, getWidth(), getHeight());
                    super.paintComponent(g);
                }
            };
            panelOscuro.setOpaque(false); 
            panelOscuro.setBackground(new Color(0, 0, 0, 150)); 
            panelOscuro.addMouseListener(new java.awt.event.MouseAdapter() {}); 
            this.setGlassPane(panelOscuro);
            this.getGlassPane().setVisible(true);
        } else {
            if (this.getGlassPane() != null) this.getGlassPane().setVisible(false);
        }
    }

    /**
     * Diálogo de confirmación para retorno al Login.
     */
    public void mostrarConfirmacionCerrarSesion() {
        setOscurecer(true);
        JDialog d = new JDialog(this, "Cerrar Sesión", true);
        d.setUndecorated(true);
        d.setSize(450, 320);
        d.setLocationRelativeTo(this);

        JPanel c = new JPanel(null);
        c.setBackground(CARD_WHITE);
        c.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 2));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/frontend/src/images/iconExitBig.png"));
            Image img = icon.getImage().getScaledInstance(150, -1, Image.SCALE_SMOOTH); 
            JLabel lblIco = new JLabel(new ImageIcon(img));
            lblIco.setBounds(150, 20, 150, 150); 
            c.add(lblIco);
        } catch (Exception e) {}

        JLabel txt = new JLabel("<html><center>¿Está seguro que desea<br>cerrar sesión?</center></html>", SwingConstants.CENTER);
        txt.setBounds(20, 175, 410, 50);
        txt.setFont(new Font("Arial", Font.BOLD, 14));
        txt.setForeground(ACCENT_RED);
        c.add(txt);

        JButton b1 = new JButton("Confirmar");
        b1.setBounds(40, 245, 170, 40);
        b1.setBackground(ACCENT_RED);
        b1.setForeground(Color.WHITE);
        b1.setFocusPainted(false);
        b1.addActionListener(e -> {
            setOscurecer(false);
            d.dispose();
            router("login"); 
        });
        c.add(b1);

        JButton b2 = new JButton("Cancelar");
        b2.setBounds(240, 245, 170, 40);
        b2.setContentAreaFilled(false);
        b2.setForeground(ACCENT_RED);
        b2.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 1));
        b2.setFocusPainted(false);
        b2.addActionListener(e -> {
            setOscurecer(false);
            d.dispose();
        });
        c.add(b2);

        d.add(c);
        d.setVisible(true);
    }

    /**
     * Alerta visual cuando no se aceptan términos/autorización.
     */
    public void mostrarAlertaAutorizacion() {
        setOscurecer(true);
        JDialog d = new JDialog(this, "Atención", true);
        d.setUndecorated(true); d.setSize(450, 260); d.setLocationRelativeTo(this);
        JPanel c = new JPanel(null); c.setBackground(CARD_WHITE); c.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 2));

        JLabel t = new JLabel("<html><center>!Atención. Debe de confirmar ser<br>personal autorizado.!</center></html>", SwingConstants.CENTER);
        t.setBounds(20, 50, 410, 80); t.setFont(new Font("Arial", Font.BOLD, 16)); t.setForeground(ACCENT_RED);
        c.add(t);

        JButton b1 = new JButton("Confirmar"); b1.setBounds(40, 160, 170, 40); b1.setBackground(ACCENT_RED);
        b1.setForeground(Color.WHITE); b1.setFocusPainted(false); b1.addActionListener(e -> { setOscurecer(false); d.dispose(); });
        c.add(b1);

        JButton b2 = new JButton("Cancelar"); b2.setBounds(240, 160, 170, 40); b2.setContentAreaFilled(false);
        b2.setForeground(ACCENT_RED); b2.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 1));
        b2.setFocusPainted(false); b2.addActionListener(e -> { setOscurecer(false); d.dispose(); });
        c.add(b2);

        d.add(c); d.setVisible(true);
    }

    public void mostrarDialogoPoliticas(JCheckBox chk) {
        setOscurecer(true);
        JDialog d = new JDialog(this, "Políticas", true);
        d.setUndecorated(true); d.setSize(600, 400); d.setLocationRelativeTo(this);
        JPanel c = new JPanel(null); c.setBackground(CARD_WHITE); c.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 2));

        JLabel t = new JLabel("Políticas de Acceso y Uso", SwingConstants.CENTER);
        t.setBounds(20, 20, 560, 30); t.setFont(new Font("Arial", Font.BOLD, 18)); t.setForeground(ACCENT_RED);
        c.add(t);

        JTextPane tp = new JTextPane();
        tp.setText("Al aceptar, reconoce el uso limitado del sistema para personal autorizado.\n\n1. No compartirá credenciales.\n2. Uso ético de la información.\n3. Reportará fallos.");
        tp.setEditable(false); tp.setBackground(CARD_WHITE);
        JScrollPane s = new JScrollPane(tp); s.setBounds(30, 70, 540, 250); s.setBorder(null);
        c.add(s);

        JButton btnA = new JButton("Aceptar"); btnA.setBounds(280, 340, 130, 35); btnA.setBackground(ACCENT_RED);
        btnA.setForeground(Color.WHITE); btnA.addActionListener(e -> { if(chk != null) chk.setSelected(true); setOscurecer(false); d.dispose(); });
        c.add(btnA);

        JButton btnR = new JButton("Rechazar"); btnR.setBounds(420, 340, 130, 35); btnR.setContentAreaFilled(false);
        btnR.setForeground(ACCENT_RED); btnR.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 1));
        btnR.addActionListener(e -> { setOscurecer(false); d.dispose(); });
        c.add(btnR);

        d.add(c); d.setVisible(true);
    }

    public void mostrarConfirmacionSalida() {
        setOscurecer(true);
        JDialog d = new JDialog(this, "Salir", true);
        d.setUndecorated(true); d.setSize(450, 320); d.setLocationRelativeTo(this);
        JPanel c = new JPanel(null); c.setBackground(CARD_WHITE); c.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 2));

        try {
            ImageIcon i = new ImageIcon(getClass().getResource("/frontend/src/images/iconExitBig.png"));
            JLabel l = new JLabel(new ImageIcon(i.getImage().getScaledInstance(150, -1, Image.SCALE_SMOOTH)));
            l.setBounds(150, 20, 150, 150); c.add(l);
        } catch (Exception e) {}

        JLabel txt = new JLabel("<html><center>Se cerrará el acceso al panel<br>administrativo. ¿Continuar?</center></html>", SwingConstants.CENTER);
        txt.setBounds(20, 175, 410, 50); txt.setFont(new Font("Arial", Font.BOLD, 14)); txt.setForeground(ACCENT_RED);
        c.add(txt);

        JButton b1 = new JButton("Continuar"); b1.setBounds(40, 245, 170, 40); b1.setBackground(ACCENT_RED);
        b1.setForeground(Color.WHITE); b1.setFocusPainted(false); b1.addActionListener(e -> System.exit(0));
        c.add(b1);

        JButton b2 = new JButton("Cancelar"); b2.setBounds(240, 245, 170, 40); b2.setContentAreaFilled(false);
        b2.setForeground(ACCENT_RED); b2.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 1));
        b2.setFocusPainted(false); b2.addActionListener(e -> { setOscurecer(false); d.dispose(); });
        c.add(b2);

        d.add(c); d.setVisible(true);
    }
}