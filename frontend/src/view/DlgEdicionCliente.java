package frontend.src.view;

import frontend.src.controller.Ventana;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Diálogo para editar información del cliente.
 */
public class DlgEdicionCliente extends JDialog {
    private final Ventana host;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtEmail;
    private JTextField txtTelefono;
    private JTextField txtFechaNacimiento;
    private JLabel lblId;
    private JLabel lblFoto;
    private String clienteId;

    public DlgEdicionCliente(Ventana host, String clienteId, String nombres, String apellidos, 
                             String email, String telefono, String fechaNacimiento) {
        super(host, true);
        this.host = host;
        this.clienteId = clienteId;
        this.setUndecorated(true);
        this.setSize(700, 550);
        this.setLocationRelativeTo(host);

        JPanel content = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Ventana.CARD_WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        content.setOpaque(false);

        // Título
        JLabel lblTit = new JLabel("Editar información del cliente");
        lblTit.setBounds(40, 20, 400, 25);
        lblTit.setFont(new Font("Arial", Font.BOLD, 18));
        lblTit.setForeground(Ventana.MAROON_BG);
        content.add(lblTit);

        // Panel de foto de perfil
        JPanel fotoPanelBg = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibujar círculo
                g2d.setColor(new Color(200, 200, 200));
                g2d.fillOval(0, 0, 120, 120);
            }
        };
        fotoPanelBg.setBounds(40, 60, 120, 120);
        content.add(fotoPanelBg);

        // Foto de perfil (simulada con un color)
        lblFoto = new JLabel();
        lblFoto.setBounds(45, 65, 110, 110);
        lblFoto.setOpaque(true);
        lblFoto.setBackground(new Color(220, 150, 150));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setVerticalAlignment(SwingConstants.CENTER);
        lblFoto.setFont(new Font("Arial", Font.BOLD, 48));
        lblFoto.setForeground(new Color(150, 80, 80));
        lblFoto.setText("👤");
        content.add(lblFoto);

        // Botón cambiar foto
        JButton btnCambiar = new JButton("Cambiar");
        btnCambiar.setBounds(40, 190, 120, 30);
        btnCambiar.setBackground(Ventana.ACCENT_RED);
        btnCambiar.setForeground(Color.WHITE);
        btnCambiar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCambiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de cambiar foto próximamente"));
        content.add(btnCambiar);

        // Campos de entrada
        int xDerecha = 180;
        int yInicio = 60;
        int altoCampo = 30;
        int espaciado = 60;

        // Nombres
        crearCampo("Nombres", xDerecha, yInicio, 280, content, nombres, result -> txtNombres = result);
        
        // Apellidos
        crearCampo("Apellidos", xDerecha + 320, yInicio, 180, content, apellidos, result -> txtApellidos = result);

        // Email
        crearCampo("E-mail", xDerecha, yInicio + espaciado, 520, content, email, result -> txtEmail = result);

        // Teléfono
        crearCampo("Teléfono", xDerecha, yInicio + espaciado * 2, 280, content, telefono, result -> txtTelefono = result);

        // Fecha de nacimiento
        crearCampo("Fecha de nacimiento", xDerecha + 320, yInicio + espaciado * 2, 180, content, fechaNacimiento, result -> txtFechaNacimiento = result);

        // ID
        JLabel lblIdLabel = new JLabel("ID:");
        lblIdLabel.setBounds(xDerecha, yInicio + espaciado * 3, 50, 25);
        lblIdLabel.setFont(new Font("Arial", Font.BOLD, 12));
        content.add(lblIdLabel);

        lblId = new JLabel(clienteId);
        lblId.setBounds(xDerecha + 50, yInicio + espaciado * 3, 150, 25);
        lblId.setFont(new Font("Arial", Font.PLAIN, 14));
        lblId.setForeground(new Color(80, 80, 80));
        content.add(lblId);

        // Botones
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(40, 470, 300, 40);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setBorder(new LineBorder(Ventana.ACCENT_RED));
        btnCancelar.setForeground(Ventana.ACCENT_RED);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> {
            host.setOscurecer(false);
            this.dispose();
            host.intentarRestaurarDashboard();
        });
        content.add(btnCancelar);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBounds(360, 470, 300, 40);
        btnConfirmar.setBackground(Ventana.ACCENT_RED);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 12));
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.addActionListener(e -> {
            // Aquí puedes agregar la lógica para guardar los cambios
            host.mostrarAvisoExitosoCliente(this);
        });
        content.add(btnConfirmar);

        this.add(content);
    }

    private void crearCampo(String label, int x, int y, int w, JPanel p, String valor, java.util.function.Consumer<JTextField> setter) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, w, 15);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(new Color(80, 80, 80));
        p.add(lbl);

        JTextField tf = new JTextField(valor != null ? valor : "");
        tf.setBounds(x, y + 20, w, 30);
        tf.setBorder(new LineBorder(new Color(220, 220, 220)));
        tf.setFont(new Font("Arial", Font.PLAIN, 12));
        p.add(tf);
        setter.accept(tf);
    }

    public String getNombres() { return txtNombres.getText(); }
    public String getApellidos() { return txtApellidos.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public String getTelefono() { return txtTelefono.getText(); }
    public String getFechaNacimiento() { return txtFechaNacimiento.getText(); }
}
