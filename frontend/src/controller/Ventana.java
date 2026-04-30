package frontend.src.controller;

import frontend.src.view.*;
import java.awt.*;
import javax.swing.*;

/**
 * JFrame principal que actúa como host para las vistas y gestiona diálogos modales.
 */
public class Ventana extends JFrame {
    
    public static final Color MAROON_BG = new Color(61, 0, 0);       
    public static final Color ACCENT_RED = new Color(152, 33, 54);  
    public static final Color CARD_WHITE = new Color(245, 245, 245);
    
    private JPanel glass;

    public Ventana() {
        this.setSize(1200, 600); 
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        this.setLocationRelativeTo(null);
        this.setResizable(false); 
        this.setTitle("Briarbuster - Sistema Administrativo");
        this.setLayout(new BorderLayout()); 

        glass = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        glass.setOpaque(false);
        glass.setVisible(false);
        glass.addMouseListener(new java.awt.event.MouseAdapter() {}); 
        this.setGlassPane(glass);

        router("splash");

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) { mostrarConfirmacionSalida(); }
        });

        this.setVisible(true);
    }

    public final void router(String target) {
        this.getContentPane().removeAll();
        switch (target) {
            case "splash":   this.add(new ViewSplash(this)); break;
            case "login":    this.add(new ViewLogin(this)); break;
            case "registro": this.add(new ViewRegister(this)); break;
            case "dashboard":this.add(new ViewDashboard(this)); break;
        }
        this.revalidate();
        this.repaint();
    }

    public void setOscurecer(boolean activo) {
        glass.setVisible(activo);
    }

    public void intentarRestaurarDashboard() {
        if (this.getContentPane().getComponentCount() > 0) {
            Component c = this.getContentPane().getComponent(0);
            if (c instanceof ViewDashboard) {
                ((ViewDashboard) c).restaurarResaltado();
            }
        }
    }

    // --- MÉTODOS DE CLIENTES ---

    public void mostrarFormCliente() {
        setOscurecer(true);
        new DlgFormCliente(this).setVisible(true);
    }

    public void mostrarAvisoExitosoCliente(JDialog padre) {
        JDialog d = new JDialog(padre, true);
        d.setUndecorated(true); d.setSize(400, 200); d.setLocationRelativeTo(padre);
        JPanel c = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_WHITE); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.setColor(ACCENT_RED); g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            }
        };
        c.setOpaque(false);
        JLabel lbl = new JLabel("<html><center>¡Operación Exitosa!<br>Los datos han sido actualizados.</center></html>", SwingConstants.CENTER);
        lbl.setBounds(20, 40, 360, 50); lbl.setFont(new Font("Arial", Font.BOLD, 16)); lbl.setForeground(MAROON_BG);
        c.add(lbl);
        JButton btn = new JButton("Aceptar");
        btn.setBounds(100, 130, 200, 40); btn.setBackground(ACCENT_RED); btn.setForeground(Color.WHITE);
        btn.addActionListener(e -> { d.dispose(); padre.dispose(); setOscurecer(false); intentarRestaurarDashboard(); });
        c.add(btn);
        d.add(c); d.setVisible(true);
    }

    // --- MÉTODOS DE EMPLEADOS ---

    public void mostrarEdicionEmpleado() {
        setOscurecer(true);
        new DlgEdicionEmpleado(this).setVisible(true);
    }

    public void mostrarAvisoExitoso(DlgEdicionEmpleado padre) {
        JDialog d = new JDialog(padre, true);
        d.setUndecorated(true); d.setSize(400, 200); d.setLocationRelativeTo(padre);
        JPanel c = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_WHITE); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.setColor(ACCENT_RED); g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            }
        };
        c.setOpaque(false);
        JLabel txt = new JLabel("<html><center>¡Operación Exitosa!<br>Los datos han sido actualizados.</center></html>", SwingConstants.CENTER);
        txt.setBounds(20, 40, 360, 50); txt.setFont(new Font("Arial", Font.BOLD, 16)); txt.setForeground(MAROON_BG);
        c.add(txt);
        JButton b = new JButton("Aceptar");
        b.setBounds(100, 130, 200, 40); b.setBackground(ACCENT_RED); b.setForeground(Color.WHITE);
        b.addActionListener(e -> { d.dispose(); padre.dispose(); setOscurecer(false); intentarRestaurarDashboard(); });
        c.add(b);
        d.add(c); d.setVisible(true);
    }

    // --- MÉTODOS DE VIDEOJUEGOS ---

    public void mostrarAvisoExitoso(DlgEdicionVideojuego padre) {
        JDialog d = new JDialog(padre, true);
        d.setUndecorated(true); d.setSize(400, 200); d.setLocationRelativeTo(padre);
        JPanel c = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_WHITE); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.setColor(ACCENT_RED); g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            }
        };
        c.setOpaque(false);
        JLabel txt = new JLabel("<html><center>¡Operación Exitosa!<br>Los datos han sido actualizados.</center></html>", SwingConstants.CENTER);
        txt.setBounds(20, 40, 360, 50); txt.setFont(new Font("Arial", Font.BOLD, 16)); txt.setForeground(MAROON_BG);
        c.add(txt);
        JButton b = new JButton("Aceptar");
        b.setBounds(100, 130, 200, 40); b.setBackground(ACCENT_RED); b.setForeground(Color.WHITE);
        b.addActionListener(e -> { d.dispose(); padre.dispose(); setOscurecer(false); intentarRestaurarDashboard(); });
        c.add(b);
        d.add(c); d.setVisible(true);
    }

    // --- MÉTODOS DE RENTAS Y COMPRAS ---

    public void mostrarAvisoExitoso(DlgNuevaOperacion padre) {
        JDialog d = new JDialog(padre, true);
        d.setUndecorated(true); d.setSize(400, 200); d.setLocationRelativeTo(padre);
        JPanel c = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_WHITE); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.setColor(ACCENT_RED); g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            }
        };
        c.setOpaque(false);
        JLabel txt = new JLabel("<html><center>¡Operación Exitosa!<br>La operación ha sido registrada.</center></html>", SwingConstants.CENTER);
        txt.setBounds(20, 40, 360, 50); txt.setFont(new Font("Arial", Font.BOLD, 16)); txt.setForeground(MAROON_BG);
        c.add(txt);
        JButton b = new JButton("Aceptar");
        b.setBounds(100, 130, 200, 40); b.setBackground(ACCENT_RED); b.setForeground(Color.WHITE);
        b.addActionListener(e -> { d.dispose(); padre.dispose(); setOscurecer(false); intentarRestaurarDashboard(); });
        c.add(b);
        d.add(c); d.setVisible(true);
    }

    public void mostrarAvisoExitoso(DlgEdicionOperacion padre) {
        JDialog d = new JDialog(padre, true);
        d.setUndecorated(true); d.setSize(400, 200); d.setLocationRelativeTo(padre);
        JPanel c = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_WHITE); g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.setColor(ACCENT_RED); g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            }
        };
        c.setOpaque(false);
        JLabel txt = new JLabel("<html><center>¡Operación Exitosa!<br>Los datos han sido actualizados.</center></html>", SwingConstants.CENTER);
        txt.setBounds(20, 40, 360, 50); txt.setFont(new Font("Arial", Font.BOLD, 16)); txt.setForeground(MAROON_BG);
        c.add(txt);
        JButton b = new JButton("Aceptar");
        b.setBounds(100, 130, 200, 40); b.setBackground(ACCENT_RED); b.setForeground(Color.WHITE);
        b.addActionListener(e -> { d.dispose(); padre.dispose(); setOscurecer(false); intentarRestaurarDashboard(); });
        c.add(b);
        d.add(c); d.setVisible(true);
    }

  public void mostrarAlertaAutorizacion() {
    setOscurecer(true);
    JDialog d = new JDialog(this, "Atención", true);
    d.setUndecorated(true); 
    d.setSize(480, 300); 
    d.setLocationRelativeTo(this);
    
    JPanel c = new JPanel(null); 
    c.setBackground(CARD_WHITE); 
    c.setBorder(BorderFactory.createLineBorder(new Color(0x9C1D3A), 2));

    JLabel t = new JLabel("<html><center><div style='font-family: Segoe UI, sans-serif; color:#9C1D3A;'>"
            + "<div style='font-size:34px; font-weight: bold; margin-bottom: 10px;'>¡Atención!</div>"
            + "<div style='font-size:14px; font-weight: normal; margin-top: 5px;'>"
            + "Debe de confirmar ser<br>personal autorizado.</div>"
            + "</div></center></html>", SwingConstants.CENTER);
    t.setBounds(20, 30, 440, 150); 
    c.add(t);

    JButton b1 = new JButton("Confirmar"); 
    b1.setBounds(65, 210, 165, 45); 
    b1.setBackground(new Color(0x9C1D3A)); 
    b1.setForeground(Color.WHITE); 
    b1.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
    b1.setFocusPainted(false);   
    b1.setBorderPainted(false);  
    b1.addActionListener(e -> { setOscurecer(false); d.dispose(); intentarRestaurarDashboard(); });
    c.add(b1);

    JButton b2 = new JButton("Cancelar"); 
    b2.setBounds(250, 210, 165, 45); 
    b2.setContentAreaFilled(false);
    b2.setForeground(new Color(0x9C1D3A)); 
    b2.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
    b2.setFocusPainted(false);   
    b2.setBorder(BorderFactory.createLineBorder(new Color(0x9C1D3A), 1));
    b2.addActionListener(e -> { setOscurecer(false); d.dispose(); });
    c.add(b2);

    d.add(c); 
    d.setVisible(true);
    }

    public void mostrarDialogoPoliticas(JCheckBox chk) {
    setOscurecer(true);
    Color ROJO_OSCURO = new Color(160, 33, 55); 
    
    JDialog d = new JDialog(this, "Políticas", true);
    d.setUndecorated(true); 
    d.setSize(600, 480);
    d.setLocationRelativeTo(this);
    
    JPanel c = new JPanel(null); 
    c.setBackground(Color.WHITE); 
    c.setBorder(BorderFactory.createLineBorder(ROJO_OSCURO, 1));

    JLabel t = new JLabel("Política de Acceso y Uso para Personal Autorizado");
    t.setBounds(20, 20, 560, 30); 
    t.setFont(new Font("Segoe UI", Font.PLAIN, 22)); 
    t.setForeground(ROJO_OSCURO);
    c.add(t);

    JTextPane tp = new JTextPane();
    tp.setContentType("text/html");
    tp.setEditable(false);
    tp.setFocusable(false);
    
    tp.setText(

        "<html><body style='font-family:Sans-Serif; font-size:10pt; color:#333333;'>" +

        "Al marcar la casilla de confirmación y acceder al sistema administrativo, usted reconoce y acepta que el uso de esta plataforma está estrictamente limitado a personal autorizado de la organización. Este acceso tiene como finalidad exclusiva la gestión, supervisión y operación interna del sistema.<br>" +

        "El usuario declara que:<ul>" +

        "<li>Cuenta con la debida autorización para acceder a funciones administrativas.</li>" +

        "<li>Utilizará el sistema únicamente para fines laborales y legítimos.</li>" +

        "<li>No compartirá sus credenciales de acceso con terceros bajo ninguna circunstancia.</li>" +

        "<li>Es responsable de todas las acciones realizadas desde su cuenta.</li>" +

        "</ul>Asimismo, el usuario se compromete a:<ul>" +

        "<li>Proteger la confidencialidad de la información a la que tenga acceso, incluyendo datos de clientes, contenido del sistema y configuraciones internas.</li>" +

        "<li>No modificar, eliminar o manipular información sin la debida autorización o fuera de sus funciones asignadas.</li>" +

        "<li>Evitar el uso indebido del sistema, incluyendo intentos de vulnerar la seguridad, acceder a áreas restringidas o alterar el funcionamiento de la plataforma.</li>" +

        "</ul>El incumplimiento de estas disposiciones puede derivar en consecuencias disciplinarias, incluyendo la suspensión o terminación del acceso, así como posibles acciones legales.<br>" +

        "Al continuar, usted confirma que comprende y acepta estas condiciones, y que hará uso responsable y ético del sistema administrativo." +

        "</body></html>"

    );
    JScrollPane s = new JScrollPane(tp); 
    s.setBounds(30, 60, 540, 330); 
    s.setBorder(null);
    c.add(s);

    
    BotonRedondeado btnA = new BotonRedondeado("Aceptar", ROJO_OSCURO, Color.WHITE);
    btnA.setBounds(320, 410, 120, 40); 
    btnA.addActionListener(e -> { 
        if(chk != null) chk.setSelected(true); 
        setOscurecer(false); d.dispose(); intentarRestaurarDashboard(); 
    });
    c.add(btnA);

    BotonRedondeado btnR = new BotonRedondeado("Rechazar", Color.WHITE, ROJO_OSCURO);
    btnR.setConBorde(true);
    btnR.setBounds(450, 410, 120, 40); 
    btnR.addActionListener(e -> { 
        setOscurecer(false); d.dispose(); intentarRestaurarDashboard(); 
    });
    c.add(btnR);

    d.add(c); 
    d.setVisible(true);
    }



    public void mostrarConfirmacionCerrarSesion() {
        setOscurecer(true);
        JDialog d = new JDialog(this, "Cerrar Sesión", true);
        d.setUndecorated(true); d.setSize(450, 320); d.setLocationRelativeTo(this);
        JPanel c = new JPanel(null); c.setBackground(CARD_WHITE); c.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 2));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/frontend/src/images/iconExitBig.png"));
            Image img = icon.getImage().getScaledInstance(150, -1, Image.SCALE_SMOOTH); 
            JLabel lblIco = new JLabel(new ImageIcon(img));
            lblIco.setBounds(150, 20, 150, 150); c.add(lblIco);
        } catch (Exception e) {}

        JLabel txt = new JLabel("<html><center>¿Está seguro que desea<br>cerrar sesión?</center></html>", SwingConstants.CENTER);
        txt.setBounds(20, 175, 410, 50); txt.setFont(new Font("Arial", Font.BOLD, 14)); txt.setForeground(ACCENT_RED);
        c.add(txt);

        JButton b1 = new JButton("Confirmar"); b1.setBounds(40, 245, 170, 40); b1.setBackground(ACCENT_RED);
        b1.setForeground(Color.WHITE); b1.addActionListener(e -> { setOscurecer(false); d.dispose(); router("login"); });
        c.add(b1);

        JButton b2 = new JButton("Cancelar"); b2.setBounds(240, 245, 170, 40); b2.setContentAreaFilled(false);
        b2.setForeground(ACCENT_RED); b2.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 1));
        b2.addActionListener(e -> { setOscurecer(false); d.dispose(); intentarRestaurarDashboard(); });
        c.add(b2);

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
        b1.setForeground(Color.WHITE); b1.addActionListener(e -> System.exit(0));
        c.add(b1);

        JButton b2 = new JButton("Cancelar"); b2.setBounds(240, 245, 170, 40); b2.setContentAreaFilled(false);
        b2.setForeground(ACCENT_RED); b2.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 1));
        b2.addActionListener(e -> { setOscurecer(false); d.dispose(); intentarRestaurarDashboard(); });
        c.add(b2);

        d.add(c); d.setVisible(true);
    }

   public void mostrarDialogoAyuda() {
        setOscurecer(true);
        JDialog d = new JDialog(this, "Ayuda", true);
        d.setUndecorated(true); d.setSize(600, 400); d.setLocationRelativeTo(this);
        JPanel c = new JPanel(null); c.setBackground(CARD_WHITE); c.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 2));

        JLabel tit = new JLabel("Ayuda y preguntas");
        tit.setBounds(40, 30, 300, 30); tit.setForeground(ACCENT_RED); tit.setFont(new Font("Arial", Font.BOLD, 22));
        c.add(tit);

        String con = "<html><div style='font-family:Arial; font-size:10px; color:#333333;'>"
                + "Teléfono: +52 (667) 845 9921<br>Soporte técnico:<br>soporte@briarbuster.dev<br>"
                + "Atención general:<br>contacto@briarbuster.mx<br><br>"
                + "Horario de atención:<br>Lunes a Viernes de 9:00 a 18:00 hrs<br>"
                + "Sábados de 10:00 a 14:00 hrs</div></html>";
        JLabel lC = new JLabel(con); lC.setBounds(40, 100, 250, 200); lC.setVerticalAlignment(SwingConstants.TOP); c.add(lC);
        
        JSeparator s = new JSeparator(JSeparator.VERTICAL); s.setBounds(300, 80, 1, 200); c.add(s);
        
        String ubi = "<html><div style='font-family:Arial; font-size:10px; color:#333333;'>"
                + "Ubicación<br>Centro de Desarrollo Tecnológico Altavista Digital<br>"
                + "Av. Circuito Innovación #4582, Piso 7<br>Col. Bosques del Horizonte<br>"
                + "C.P. 90847<br>Ciudad Neotrópolis, Estado de Nova Sierra<br>México</div></html>";
        JLabel lU = new JLabel(ubi); lU.setBounds(320, 100, 250, 200); lU.setVerticalAlignment(SwingConstants.TOP); c.add(lU);
        
        JButton bS = new JButton("Regresar"); bS.setBounds(225, 330, 150, 35); bS.setBackground(ACCENT_RED);
        bS.setForeground(Color.WHITE); bS.addActionListener(e -> { setOscurecer(false); d.dispose(); intentarRestaurarDashboard(); });
        c.add(bS);
        
        d.add(c); d.setVisible(true);
    }
}
