package frontend.src.mainpackage;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.MatteBorder;

public class ViewRegister extends JPanel {
    private final Ventana host;
    private JCheckBox chk;
    private final Color ROJO_VINO = new Color(160, 33, 55);

    public ViewRegister(Ventana host) {
        this.host = host; 
        this.setLayout(null); 
        this.setBackground(Ventana.MAROON_BG);
        init();
    }

    private void init() {
        
        JPanel c = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g); 
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Ventana.CARD_WHITE); 
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0); // En la imagen 1 no parece muy redondeado, pero puedes ponerle 20, 20 si gustas
            }
        };
        
        c.setBounds(350, 10, 500, 680); 
        c.setOpaque(false);

        
        JLabel tit = new JLabel("NUEVO PERFIL - ADMINISTRADOR", SwingConstants.CENTER);
        tit.setBounds(0, 25, 500, 40); 
        tit.setFont(new Font("Serif", Font.BOLD, 22)); 
        tit.setForeground(new Color(100, 20, 30)); 
        c.add(tit);

        
        JLabel sub1 = new JLabel("Datos personales");
        sub1.setBounds(40, 80, 200, 30);
        sub1.setFont(new Font("Segoe UI", Font.PLAIN, 19));
        c.add(sub1);

        crearCol("Nombres", 40, 120, 200, c); 
        crearCol("Apellidos", 260, 120, 200, c);
        crearCol("E-mail", 40, 185, 420, c); 
        crearCol("Contrasena", 40, 250, 200, c);
        crearCol("Confirmar contrasena", 260, 250, 200, c);

       
        JLabel sub2 = new JLabel("Informacion de usuario");
        sub2.setBounds(40, 320, 300, 30);
        sub2.setFont(new Font("Segoe UI", Font.PLAIN, 19));
        c.add(sub2);

        crearCol("ID Empleado", 40, 355, 420, c);

        
        chk = new JCheckBox("Al acceder, confirmo que soy personal autorizado");
        chk.setBounds(40, 425, 420, 25); 
        chk.setFont(new Font("SansSerif", Font.PLAIN, 11));
        chk.setOpaque(false); 
        c.add(chk);

       
        
        
        JButton btnP = new BotonRedondeado("Ver políticas de acceso", ROJO_VINO, Color.WHITE);
        btnP.setBounds(150, 465, 200, 25);
        btnP.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnP.addActionListener(e -> host.mostrarDialogoPoliticas(chk));
        c.add(btnP);

        
        JButton btnC = new BotonRedondeado("Crear Perfil de  Administrador", ROJO_VINO, Color.WHITE);
        btnC.setBounds(100, 505, 300, 40);
        btnC.addActionListener(e -> {
            if(chk.isSelected()) host.router("dashboard");
            else host.mostrarAlertaAutorizacion();
        });
        c.add(btnC);

        
        JButton btnB = new BotonRedondeado("Cancelar", Color.WHITE, ROJO_VINO);
        ((BotonRedondeado)btnB).setConBorde(true);
        btnB.setBounds(100, 575, 300, 35);
        btnB.addActionListener(e -> host.router("login"));
        c.add(btnB);

        this.add(c);
    }

    private void crearCol(String t, int x, int y, int w, JPanel p) {
        JLabel l = new JLabel(t); 
        l.setBounds(x, y, w, 20); 
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        p.add(l);
        
        JTextField f = (t.contains("Contrasena")) ? new JPasswordField() : new JTextField();
        f.setBounds(x, y + 20, w, 25); 
        f.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        f.setBackground(Ventana.CARD_WHITE); 
        p.add(f);
    }

    
    class BotonRedondeado extends JButton {
        private Color bg, fg;
        private boolean conBorde = false;

        public BotonRedondeado(String texto, Color bg, Color fg) {
            super(texto);
            this.bg = bg; this.fg = fg;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(fg);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public void setConBorde(boolean b) { this.conBorde = b; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
            if (conBorde) {
                g2.setColor(fg);
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 12, 12));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}