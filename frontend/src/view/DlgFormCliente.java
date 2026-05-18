package frontend.src.view;

import frontend.src.controller.ClienteController;
import frontend.src.controller.Ventana;
import frontend.src.model.ClienteInfo;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class DlgFormCliente extends JDialog {
    private final Ventana host;
    private PnlGestionClientes panelGestion;
    private JTextField txtNombres;
    private JTextField txtPrimerApellido;
    private JTextField txtSegundoApellido;
    private JTextField txtEmail;
    private JTextField txtTelefono;
    private JTextField txtFechaNacimiento;

    public DlgFormCliente(Ventana host, PnlGestionClientes panelGestion) {
        super(host, true);
        this.host = host;
        this.panelGestion = panelGestion;
        this.setUndecorated(true);
        this.setSize(700, 500);
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

        JLabel lblTit = new JLabel("Registro de Nuevo Cliente");
        lblTit.setBounds(40, 30, 400, 25);
        lblTit.setFont(new Font("Arial", Font.BOLD, 18));
        lblTit.setForeground(Ventana.MAROON_BG);
        content.add(lblTit);

        int y = 80;
        txtNombres = crearCampo("Nombres del Cliente", 40, y, 190, content);
        txtPrimerApellido = crearCampo("Primer apellido", 250, y, 180, content);
        txtSegundoApellido = crearCampo("Segundo apellido", 450, y, 190, content);
        txtEmail = crearCampo("Correo Electronico", 40, y + 70, 620, content);
        txtTelefono = crearCampo("Telefono de Contacto", 40, y + 140, 280, content);
        txtFechaNacimiento = crearCampo("Fecha de nacimiento (yyyy-mm-dd)", 360, y + 140, 280, content);

        JButton btnCan = new JButton("Cancelar");
        btnCan.setBounds(40, 420, 160, 40);
        btnCan.setContentAreaFilled(false);
        btnCan.setBorder(new LineBorder(Ventana.ACCENT_RED));
        btnCan.setForeground(Ventana.ACCENT_RED);
        btnCan.addActionListener(e -> {
            host.setOscurecer(false);
            this.dispose();
            host.intentarRestaurarDashboard();
        });
        content.add(btnCan);

        JButton btnReg = new JButton("Confirmar");
        btnReg.setBounds(210, 420, 160, 40);
        btnReg.setBackground(Ventana.ACCENT_RED);
        btnReg.setForeground(Color.WHITE);
        btnReg.addActionListener(e -> guardarNuevoCliente());
        content.add(btnReg);

        JSeparator linea = new JSeparator(SwingConstants.VERTICAL);
        linea.setBounds(390, 420, 2, 40);
        linea.setForeground(new Color(210, 210, 210));
        content.add(linea);

        JLabel lblId = new JLabel("ID generada automaticamente");
        lblId.setBounds(410, 420, 240, 40);
        lblId.setFont(new Font("Arial", Font.PLAIN, 16));
        lblId.setForeground(new Color(80, 80, 80));
        content.add(lblId);

        this.add(content);
    }

    private void guardarNuevoCliente() {
        String nombres = txtNombres.getText().trim();
        String primerApellido = txtPrimerApellido.getText().trim();
        String segundoApellido = txtSegundoApellido.getText().trim();
        String email = txtEmail.getText().trim();
        String telefono = normalizarTelefono(txtTelefono.getText());
        String fechaNacimiento = txtFechaNacimiento.getText().trim();

        if (nombres.isEmpty() || primerApellido.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor, completa los campos requeridos (Nombres, Primer apellido y Email)",
                "Campos incompletos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!telefono.isEmpty() && !telefono.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(
                this,
                "El telefono debe tener 10 digitos",
                "Telefono invalido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!fechaNacimiento.isEmpty()) {
            LocalDate fecha = parseFechaNacimiento(fechaNacimiento);
            if (fecha == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "La fecha de nacimiento debe tener formato AAAA-MM-DD",
                    "Fecha invalida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (fecha.getYear() < 1900) {
                JOptionPane.showMessageDialog(
                    this,
                    "La fecha no puede ser anterior a 1900",
                    "Fecha invalida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (fecha.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(
                    this,
                    "La fecha no puede ser futura",
                    "Fecha invalida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        ClienteInfo nuevoCliente = new ClienteInfo();
        nuevoCliente.setNombre(nombres);
        nuevoCliente.setPrimerApellido(primerApellido);
        nuevoCliente.setSegundoApellido(segundoApellido);
        nuevoCliente.setEmail(email);
        nuevoCliente.setTelefono(telefono);
        nuevoCliente.setFechaNacimiento(fechaNacimiento);
        nuevoCliente.setNivel("Bronce");
        nuevoCliente.setEstatus("Activo");

        boolean exito = ClienteController.agregarCliente(nuevoCliente);

        if (exito) {
            JOptionPane.showMessageDialog(
                this,
                "Cliente agregado exitosamente",
                "Exito",
                JOptionPane.INFORMATION_MESSAGE
            );

            if (panelGestion != null) {
                panelGestion.refrescarTabla();
            }

            host.setOscurecer(false);
            this.dispose();
            host.intentarRestaurarDashboard();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Error al agregar el cliente. Por favor, intenta de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private JTextField crearCampo(String t, int x, int y, int w, JPanel p) {
        JLabel l = new JLabel(t);
        l.setBounds(x, y, w, 20);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        p.add(l);
        JTextField tf = new JTextField();
        tf.setBounds(x, y + 25, w, 30);
        tf.setBorder(new LineBorder(new Color(220, 220, 220)));
        p.add(tf);
        return tf;
    }

    private String normalizarTelefono(String telefono) {
        return telefono == null ? "" : telefono.replaceAll("\\D", "");
    }

    private LocalDate parseFechaNacimiento(String fechaNacimiento) {
        try {
            return LocalDate.parse(fechaNacimiento);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
