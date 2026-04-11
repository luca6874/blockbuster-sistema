package frontend.src.mainpackage;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ViewLogin extends JPanel {
    private final Ventana host;
    private JCheckBox chkAutorizado; // Atributo de clase para que el diálogo pueda marcarlo

    // Colores oficiales de Briarbuster
    private final Color MAROON_BG = new Color(61, 0, 0);       
    private final Color DARK_GRAY_BG = new Color(25, 25, 25); 
    private final Color ACCENT_RED = new Color(152, 33, 54);  
    private final Color CARD_WHITE = new Color(245, 245, 245);

    public ViewLogin(Ventana host) {
        this.host = host;
        this.setLayout(new GridLayout(1, 2)); 

        initLadoIzquierdo();
        initLadoDerecho();
    }

    private void initLadoIzquierdo() {
        JPanel leftPanel = new JPanel(new GridBagLayout()); 
        leftPanel.setBackground(DARK_GRAY_BG);

        JLabel lblBienvenido = new JLabel("<html><center>BIENVENIDO A<br>BRIARBUSTER</center></html>");
        lblBienvenido.setForeground(ACCENT_RED);
        lblBienvenido.setFont(new Font("Arial Black", Font.BOLD, 32)); 
        
        leftPanel.add(lblBienvenido);
        this.add(leftPanel);
    }

    private void initLadoDerecho() {
        JPanel rightPanel = new JPanel(null); 
        rightPanel.setBackground(MAROON_BG);

        // --- ICONO DE SALIDA (Esquina inferior derecha) ---
        try {
            ImageIcon exitIcon = new ImageIcon(getClass().getResource("/frontend/src/images/iconExit1.png"));
            Image scaledExit = exitIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            JLabel lblExit = new JLabel(new ImageIcon(scaledExit));
            lblExit.setBounds(450, 540, 25, 25); 
            lblExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lblExit.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) { System.exit(0); }
            });
            rightPanel.add(lblExit);
        } catch (Exception e) {}

        // --- CARD BLANCA ---
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CARD_WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        card.setBounds(50, 50, 400, 500); 
        card.setOpaque(false);
        
        JLabel titulo = new JLabel("ACCESO AL SISTEMA", SwingConstants.CENTER);
        titulo.setBounds(0, 20, 400, 40);
        titulo.setFont(new Font("Serif", Font.BOLD, 22));
        card.add(titulo);

        // Campos de texto
        crearInputUnderline("Nombre", 80, card);
        crearInputUnderline("E-mail", 145, card);
        crearInputUnderline("Contrasena", 210, card);

        // Checkbox (Referenciado al atributo de clase)
        chkAutorizado = new JCheckBox("Al acceder, confirmo ser personal autorizado");
        chkAutorizado.setBounds(40, 275, 320, 25);
        chkAutorizado.setOpaque(false);
        chkAutorizado.setFont(new Font("Arial", Font.PLAIN, 11));
        card.add(chkAutorizado);

        // Pill políticas con evento para abrir diálogo
        JLabel pillPoliticas = new JLabel("Ver políticas de acceso", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(ACCENT_RED);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        pillPoliticas.setBounds(120, 305, 160, 20);
        pillPoliticas.setForeground(Color.WHITE);
        pillPoliticas.setFont(new Font("Arial", Font.BOLD, 10));
        pillPoliticas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pillPoliticas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) { mostrarDialogoPoliticas(); }
        });
        card.add(pillPoliticas);

        // Botones principales
        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setBounds(40, 350, 320, 40);
        btnLogin.setBackground(ACCENT_RED);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> host.router("dashboard"));
        card.add(btnLogin);

        JButton btnReg = new JButton("Registro");
        btnReg.setBounds(40, 400, 320, 40);
        btnReg.setContentAreaFilled(false);
        btnReg.setForeground(ACCENT_RED);
        btnReg.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 2));
        btnReg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(btnReg);

        rightPanel.add(card);
        this.add(rightPanel);
    }

    private void crearInputUnderline(String texto, int y, JPanel panel) {
        JLabel lbl = new JLabel(texto);
        lbl.setBounds(40, y, 200, 20);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lbl);

        JTextField field = texto.equals("Contrasena") ? new JPasswordField() : new JTextField();
        field.setBounds(40, y + 20, 320, 25);
        field.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
        field.setBackground(CARD_WHITE);
        panel.add(field);
    }

    private void mostrarDialogoPoliticas() {
        host.setOscurecer(true);

        JDialog dialogo = new JDialog(host, "Políticas", true);
        dialogo.setUndecorated(true);
        dialogo.setSize(600, 400);
        dialogo.setLocationRelativeTo(host);

        JPanel contenido = new JPanel(null);
        contenido.setBackground(CARD_WHITE);
        contenido.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 2));

        JLabel lblTitulo = new JLabel("Políticas de Acceso", SwingConstants.CENTER);
        lblTitulo.setBounds(20, 20, 560, 30);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(ACCENT_RED);
        contenido.add(lblTitulo);

        JTextPane txtArea = new JTextPane();
        txtArea.setText("Al aceptar estas políticas, usted confirma que es personal autorizado para operar Briarbuster.\n\n"
                      + "1. No compartirá su contraseña.\n"
                      + "2. Hará uso ético de la información de clientes.\n"
                      + "3. Reportará cualquier fallo en el sistema.");
        txtArea.setEditable(false);
        txtArea.setBackground(CARD_WHITE);
        
        JScrollPane scroll = new JScrollPane(txtArea);
        scroll.setBounds(30, 60, 540, 260);
        scroll.setBorder(null);
        contenido.add(scroll);

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(280, 340, 130, 35);
        btnAceptar.setBackground(ACCENT_RED);
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.addActionListener(e -> {
            chkAutorizado.setSelected(true); // MARCAR AUTOMÁTICAMENTE
            host.setOscurecer(false);
            dialogo.dispose();
        });

        JButton btnRechazar = new JButton("Rechazar");
        btnRechazar.setBounds(420, 340, 130, 35);
        btnRechazar.setContentAreaFilled(false);
        btnRechazar.setForeground(ACCENT_RED);
        btnRechazar.setBorder(BorderFactory.createLineBorder(ACCENT_RED, 1));
        btnRechazar.addActionListener(e -> {
            host.setOscurecer(false);
            dialogo.dispose();
        });

        contenido.add(btnAceptar);
        contenido.add(btnRechazar);
        dialogo.add(contenido);
        dialogo.setVisible(true);
    }
}