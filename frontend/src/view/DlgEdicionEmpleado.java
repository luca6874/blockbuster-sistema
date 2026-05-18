package frontend.src.view;

import frontend.src.controller.Ventana;
import frontend.src.controller.UsuarioController;
import frontend.src.model.UsuarioInfo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DlgEdicionEmpleado extends JDialog {
    private final Ventana host;
    private JTextField tfNombre;
    private JTextField tfPrimerApellido;
    private JTextField tfSegundoApellido;
    private JTextField tfUsername;
    private JTextField tfCorreo;
    private JTextField tfFechaNacimiento;
    private JPasswordField tfPassword;
    private JPasswordField tfConfirmarPassword;

    public DlgEdicionEmpleado(Ventana host) {
        super(host, true);
        this.host = host;
        this.setUndecorated(true);
        this.setSize(700, 680);
        this.setLocationRelativeTo(host);

        JPanel content = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Ventana.CARD_WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        content.setOpaque(false);

        JLabel lblTitulo = new JLabel("Editar datos personales");
        lblTitulo.setBounds(40, 30, 400, 25);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Ventana.MAROON_BG);
        content.add(lblTitulo);

        int yIn = 80;
        
        tfNombre = crearCampo("Nombre", 40, yIn, 620, content);
        tfPrimerApellido = crearCampo("Primer Apellido", 40, yIn + 70, 300, content);
        tfSegundoApellido = crearCampo("Segundo Apellido", 360, yIn + 70, 300, content);
        
        tfFechaNacimiento = crearCampo("Fecha de Nacimiento (yyyy-MM-dd)", 40, yIn + 140, 620, content);
        
        tfUsername = crearCampoSoloLectura("Usuario (Solo lectura)", 40, yIn + 210, 300, content);
        tfCorreo = crearCampoSoloLectura("Correo (Solo lectura)", 360, yIn + 210, 300, content);
        
        tfPassword = crearCampoPassword("Contraseña (dejar vacío para no cambiar)", 40, yIn + 280, 620, content);
        tfConfirmarPassword = crearCampoPassword("Confirmar Contraseña", 40, yIn + 350, 620, content);

        cargarDatosActuales();

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(40, 600, 300, 40);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setBorder(new LineBorder(Ventana.ACCENT_RED));
        btnCancelar.setForeground(Ventana.ACCENT_RED);
        btnCancelar.addActionListener(e -> { host.setOscurecer(false); this.dispose(); host.intentarRestaurarDashboard(); });
        content.add(btnCancelar);

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(360, 600, 300, 40);
        btnAceptar.setBackground(Ventana.ACCENT_RED);
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.addActionListener(e -> guardarCambios());
        content.add(btnAceptar);

        this.add(content);
    }

    private void cargarDatosActuales() {
        UsuarioInfo usuario = host.getUsuarioActual();
        if (usuario != null) {
            tfNombre.setText(usuario.getNombre() != null ? usuario.getNombre() : "");
            tfPrimerApellido.setText(usuario.getPrimerApellido() != null ? usuario.getPrimerApellido() : "");
            tfSegundoApellido.setText(usuario.getSegundoApellido() != null ? usuario.getSegundoApellido() : "");
            tfUsername.setText(usuario.getUsername() != null ? usuario.getUsername() : "");
            tfCorreo.setText(usuario.getCorreo() != null ? usuario.getCorreo() : "");
            if (usuario.getFechaNacimiento() != null) {
                tfFechaNacimiento.setText(usuario.getFechaNacimiento().toString());
            }
        }
    }

    private void guardarCambios() {
        UsuarioInfo usuario = host.getUsuarioActual();
        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Error: No hay usuario actual", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar campos obligatorios
        String nombre = tfNombre.getText().trim();
        String primerApellido = tfPrimerApellido.getText().trim();
        String segundoApellido = tfSegundoApellido.getText().trim();
        String fechaNacimientoStr = tfFechaNacimiento.getText().trim();
        String password = new String(tfPassword.getPassword()).trim();
        String confirmarPassword = new String(tfConfirmarPassword.getPassword()).trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (primerApellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El primer apellido no puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (fechaNacimientoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fecha de nacimiento no puede estar vacía", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar fecha de nacimiento
        LocalDate fechaNacimiento;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            fechaNacimiento = LocalDate.parse(fechaNacimientoStr, formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use yyyy-MM-dd", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar contraseña si se intenta cambiar
        if (fechaNacimiento.getYear() < 1900) {
            JOptionPane.showMessageDialog(this, "La fecha no puede ser anterior a 1900", "ValidaciÃ³n", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "La fecha no puede ser futura", "ValidaciÃ³n", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.isEmpty() || !confirmarPassword.isEmpty()) {
            if (!password.equals(confirmarPassword)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Si las contraseñas están vacías, pasar null para no actualizar
        String passwordActualizar = password.isEmpty() ? null : password;

        // Realizar actualización
        boolean exito = UsuarioController.actualizarUsuario(usuario.getIdUsuario(), nombre, primerApellido,
                                                            segundoApellido, fechaNacimiento, passwordActualizar);

        if (exito) {
            // Actualizar usuario en memoria
            usuario.setNombre(nombre);
            usuario.setPrimerApellido(primerApellido);
            usuario.setSegundoApellido(segundoApellido);
            usuario.setFechaNacimiento(fechaNacimiento);
            if (passwordActualizar != null) {
                usuario.setPassword(passwordActualizar);
            }

            host.mostrarAvisoExitoso(this);
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar los datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField crearCampo(String titulo, int x, int y, int w, JPanel p) {
        JLabel l = new JLabel(titulo);
        l.setBounds(x, y, w, 20);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        p.add(l);

        JTextField tf = new JTextField();
        tf.setBounds(x, y + 25, w, 30);
        tf.setBorder(new LineBorder(new Color(220, 220, 220)));
        p.add(tf);
        return tf;
    }

    private JTextField crearCampoSoloLectura(String titulo, int x, int y, int w, JPanel p) {
        JLabel l = new JLabel(titulo);
        l.setBounds(x, y, w, 20);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        p.add(l);

        JTextField tf = new JTextField();
        tf.setBounds(x, y + 25, w, 30);
        tf.setBorder(new LineBorder(new Color(220, 220, 220)));
        tf.setEditable(false);
        tf.setBackground(new Color(240, 240, 240));
        p.add(tf);
        return tf;
    }

    private JPasswordField crearCampoPassword(String titulo, int x, int y, int w, JPanel p) {
        JLabel l = new JLabel(titulo);
        l.setBounds(x, y, w, 20);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        p.add(l);

        JPasswordField pf = new JPasswordField();
        pf.setBounds(x, y + 25, w, 30);
        pf.setBorder(new LineBorder(new Color(220, 220, 220)));
        p.add(pf);
        return pf;
    }
}
