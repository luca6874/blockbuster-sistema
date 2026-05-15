package frontend.src.view;

import frontend.src.controller.RegistroController;
import frontend.src.controller.Ventana;
import frontend.src.model.UsuarioInfo;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.MatteBorder;

public class ViewRegister extends JPanel {
    private final Ventana host;
    private JCheckBox chk;
    private final Color ROJO_VINO = new Color(160, 33, 55);

    private JTextField tfNombre;
    private JTextField tfPrimerApellido;
    private JTextField tfSegundoApellido;
    private JTextField tfFechaNacimiento;
    private JTextField tfEmail;
    private JPasswordField tfPassword;
    private JPasswordField tfPasswordConfirm;
    private JTextField tfUsername;

    public ViewRegister(Ventana host) {
        this.host = host;
        this.setLayout(null);
        this.setBackground(Ventana.MAROON_BG);
        init();
    }

    private void init() {
        try {
            ImageIcon ex = new ImageIcon(getClass().getResource("/frontend/src/images/iconExit1.png"));
            JLabel lEx = new JLabel(new ImageIcon(ex.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH)));
            lEx.setBounds(1155, 525, 45, 45);
            lEx.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lEx.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    host.mostrarConfirmacionSalida();
                }
            });
            this.add(lEx);
            this.setComponentZOrder(lEx, 0);
        } catch (Exception e) {}

        JPanel c = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Ventana.CARD_WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
            }
        };

        c.setBounds(350, 10, 500, 580);
        c.setOpaque(false);

        JLabel tit = new JLabel("NUEVO PERFIL - ADMINISTRADOR", SwingConstants.CENTER);
        tit.setBounds(0, 15, 500, 40);
        tit.setFont(new Font("Serif", Font.BOLD, 22));
        tit.setForeground(new Color(100, 20, 30));
        c.add(tit);

        JLabel sub1 = new JLabel("Datos personales");
        sub1.setBounds(40, 65, 200, 30);
        sub1.setFont(new Font("Segoe UI", Font.PLAIN, 19));
        c.add(sub1);

        tfNombre = crearCol("Nombre", 40, 100, 420, c);
        tfPrimerApellido = crearCol("Primer apellido", 40, 155, 200, c);
        tfSegundoApellido = crearCol("Segundo apellido", 260, 155, 200, c);
        tfFechaNacimiento = crearCol("Fecha nacimiento (AAAA-MM-DD)", 40, 210, 200, c);
        tfEmail = crearCol("E-mail", 260, 210, 200, c);
        tfUsername = crearCol("Nombre de usuario", 40, 263, 420, c);

        /*JLabel sub2 = new JLabel("Informacion de acceso");
        sub2.setBounds(40, 270, 300, 30);
        sub2.setFont(new Font("Segoe UI", Font.PLAIN, 19));
        c.add(sub2);*/

        tfPassword = crearPasswordCol("Contrasena", 40, 305, 200, c);
        tfPasswordConfirm = crearPasswordCol("Confirmar contrasena", 260, 305, 200, c);

        chk = new JCheckBox("Al acceder, confirmo que soy personal autorizado");
        chk.setBounds(40, 365, 420, 25);
        chk.setFont(new Font("SansSerif", Font.PLAIN, 11));
        chk.setOpaque(false);
        c.add(chk);

        BotonRedondeado btnP = new BotonRedondeado("Ver politicas de acceso", ROJO_VINO, Color.WHITE);
        btnP.setBounds(150, 400, 200, 25);
        btnP.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnP.addActionListener(e -> host.mostrarDialogoPoliticas(chk));
        c.add(btnP);

        BotonRedondeado btnC = new BotonRedondeado("Crear Perfil de Administrador", ROJO_VINO, Color.WHITE);
        btnC.setBounds(100, 440, 300, 40);
        btnC.addActionListener(e -> procesarRegistro());
        c.add(btnC);

        BotonRedondeado btnB = new BotonRedondeado("Cancelar", Color.WHITE, ROJO_VINO);
        btnB.setConBorde(true);
        btnB.setBounds(100, 500, 300, 35);
        btnB.addActionListener(e -> host.router("login"));
        c.add(btnB);

        this.add(c);
    }

    private void procesarRegistro() {
        if (!chk.isSelected()) {
            host.mostrarAlertaAutorizacion();
            return;
        }

        String nombre = tfNombre.getText().trim();
        String primerApellido = tfPrimerApellido.getText().trim();
        String segundoApellido = tfSegundoApellido.getText().trim();
        String fechaNacimiento = tfFechaNacimiento.getText().trim();
        String correo = tfEmail.getText().trim();
        String username = tfUsername.getText().trim();
        String password = new String(tfPassword.getPassword()).trim();
        String passwordConfirm = new String(tfPasswordConfirm.getPassword()).trim();

        UsuarioInfo usuario = RegistroController.registrar(
                nombre,
                primerApellido,
                segundoApellido,
                fechaNacimiento,
                username,
                correo,
                password,
                passwordConfirm
        );

        if (usuario != null) {
            JOptionPane.showMessageDialog(
                ViewRegister.this,
                "Perfil de administrador creado exitosamente.\nUsuario: " + usuario.getNombreCompletoVisible(),
                "Registro Exitoso",
                JOptionPane.INFORMATION_MESSAGE
            );

            host.setUsuarioActual(usuario);
            host.router("dashboard");
        } else {
            JOptionPane.showMessageDialog(
                ViewRegister.this,
                obtenerMensajeError(nombre, primerApellido, segundoApellido, fechaNacimiento, correo, password, passwordConfirm),
                "Error en el Registro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String obtenerMensajeError(String nombre, String primerApellido, String segundoApellido,
                                       String fechaNacimiento, String correo,
                                       String password, String passwordConfirm) {
        if (nombre.isEmpty() || primerApellido.isEmpty() || segundoApellido.isEmpty() ||
            fechaNacimiento.isEmpty() || correo.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            return "Por favor, rellena todos los campos";
        }

        if (!password.equals(passwordConfirm)) {
            return "Las contrasenas no coinciden";
        }

        if (password.length() < RegistroController.PASSWORD_MIN_LENGTH) {
            return "La contrasena debe tener al menos " + RegistroController.PASSWORD_MIN_LENGTH + " caracteres";
        }

        if (!correo.contains("@")) {
            return "El correo debe ser valido";
        }

        if (!fechaValida(fechaNacimiento)) {
            return "La fecha de nacimiento debe tener formato AAAA-MM-DD";
        }

        return "El nombre de usuario o correo ya esta registrado.\nPor favor, usa otros diferentes.";
    }

    private boolean fechaValida(String fechaNacimiento) {
        try {
            LocalDate.parse(fechaNacimiento);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private JTextField crearCol(String t, int x, int y, int w, JPanel p) {
        JLabel l = new JLabel(t);
        l.setBounds(x, y, w, 20);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        p.add(l);

        JTextField f = new JTextField();
        configurarInput(f, x, y, w, p);

        return f;
    }

    private JPasswordField crearPasswordCol(String t, int x, int y, int w, JPanel p) {
        JLabel l = new JLabel(t);
        l.setBounds(x, y, w, 20);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        p.add(l);

        JPasswordField f = new JPasswordField();
        configurarInput(f, x, y, w, p);

        return f;
    }

    private void configurarInput(JTextField f, int x, int y, int w, JPanel p) {
        f.setBounds(x, y + 20, w, 25);
        f.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
        f.setBackground(Ventana.CARD_WHITE);
        p.add(f);
    }
}
