package frontend.src.view;

import frontend.src.controller.ClienteController;
import frontend.src.controller.Ventana;
import frontend.src.model.ClienteInfo;
import frontend.src.service.ImageManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Dialogo para editar informacion del cliente.
 */
public class DlgEdicionCliente extends JDialog {
    private final Ventana host;
    private PnlGestionClientes panelGestion;
    private JTextField txtNombres;
    private JTextField txtPrimerApellido;
    private JTextField txtSegundoApellido;
    private JTextField txtEmail;
    private JTextField txtTelefono;
    private JTextField txtFechaNacimiento;
    private JLabel lblId;
    private JLabel lblFoto;
    private String clienteId;
    private String fotoActual;  

    public DlgEdicionCliente(Ventana host, String clienteId, String nombres, String primerApellido,
                             String email, String telefono, String fechaNacimiento, PnlGestionClientes panelGestion) {
        this(host, clienteId, nombres, primerApellido, "", email, telefono, fechaNacimiento, panelGestion);
    }

    public DlgEdicionCliente(Ventana host, String clienteId, String nombres, String primerApellido,
                             String segundoApellido, String email, String telefono, String fechaNacimiento,
                             PnlGestionClientes panelGestion) {
        super(host, true);
        this.host = host;
        this.panelGestion = panelGestion;
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

        JLabel lblTit = new JLabel("Editar informacion del cliente");
        lblTit.setBounds(40, 20, 400, 25);
        lblTit.setFont(new Font("Arial", Font.BOLD, 18));
        lblTit.setForeground(Ventana.MAROON_BG);
        content.add(lblTit);

        JPanel fotoPanelBg = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(200, 200, 200));
                g2d.fillOval(0, 0, 120, 120);
            }
        };
        fotoPanelBg.setBounds(40, 60, 120, 120);
        content.add(fotoPanelBg);

        lblFoto = new JLabel();
        lblFoto.setBounds(45, 65, 110, 110);
        lblFoto.setOpaque(true);
        lblFoto.setBackground(new Color(220, 150, 150));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setVerticalAlignment(SwingConstants.CENTER);
        lblFoto.setFont(new Font("Arial", Font.BOLD, 48));
        lblFoto.setForeground(new Color(150, 80, 80));
        lblFoto.setText("U");
        content.add(lblFoto);

        JButton btnCambiar = new JButton("Cambiar foto");
        btnCambiar.setBounds(40, 190, 120, 30);
        btnCambiar.setBackground(Ventana.ACCENT_RED);
        btnCambiar.setForeground(Color.WHITE);
        btnCambiar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCambiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiar.addActionListener(e -> seleccionarFotoPerfil());
        content.add(btnCambiar);

        JButton btnQuitar = new JButton("Quitar foto");
        btnQuitar.setBounds(40, 225, 120, 30);
        btnQuitar.setContentAreaFilled(false);
        btnQuitar.setBorder(new LineBorder(Ventana.ACCENT_RED));
        btnQuitar.setForeground(Ventana.ACCENT_RED);
        btnQuitar.setFont(new Font("Arial", Font.BOLD, 12));
        btnQuitar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQuitar.addActionListener(e -> quitarFotoPerfil());
        content.add(btnQuitar);

        int xDerecha = 180;
        int yInicio = 60;
        int espaciado = 60;

        crearCampo("Nombres", xDerecha, yInicio, 160, content, nombres, result -> txtNombres = result);
        crearCampo("Primer apellido", xDerecha + 180, yInicio, 150, content, primerApellido, result -> txtPrimerApellido = result);
        crearCampo("Segundo apellido", xDerecha + 350, yInicio, 150, content, segundoApellido, result -> txtSegundoApellido = result);
        crearCampo("E-mail", xDerecha, yInicio + espaciado, 520, content, email, result -> txtEmail = result);
        crearCampo("Telefono", xDerecha, yInicio + espaciado * 2, 280, content, telefono, result -> txtTelefono = result);
        crearCampo("Fecha nacimiento (dd-mm-yyyy)", xDerecha + 320, yInicio + espaciado * 2, 180, content, fechaNacimiento, result -> txtFechaNacimiento = result);

        JLabel lblIdLabel = new JLabel("ID:");
        lblIdLabel.setBounds(xDerecha, yInicio + espaciado * 3, 50, 25);
        lblIdLabel.setFont(new Font("Arial", Font.BOLD, 12));
        content.add(lblIdLabel);

        lblId = new JLabel(clienteId);
        lblId.setBounds(xDerecha + 50, yInicio + espaciado * 3, 150, 25);
        lblId.setFont(new Font("Arial", Font.PLAIN, 14));
        lblId.setForeground(new Color(80, 80, 80));
        content.add(lblId);

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
        btnConfirmar.addActionListener(e -> guardarCambios());
        content.add(btnConfirmar);

        this.add(content);
        cargarFotoActualDesdeBD();

    }

    private void cargarFotoActualDesdeBD() {
        ClienteInfo cliente = ClienteController.obtenerClientePorId(clienteId);
        if (cliente == null) {
            return;
        }

        fotoActual = cliente.getFoto();
        mostrarFoto(fotoActual);
    }

    private void seleccionarFotoPerfil() {

    JFileChooser fileChooser = new JFileChooser();

    FileNameExtensionFilter filtro = new FileNameExtensionFilter(
            "Imágenes", "png", "jpg", "jpeg");

    fileChooser.setFileFilter(filtro);

    int resultado = fileChooser.showOpenDialog(this);

    if (resultado == JFileChooser.APPROVE_OPTION) {

        File archivoSeleccionado = fileChooser.getSelectedFile();

        try {

            // Guardar imagen usando ImageManager
            String nombreImagen = ImageManager.guardarImagen(
                    archivoSeleccionado);

            if (nombreImagen == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo guardar la imagen seleccionada.",
                        "Imagen invalida",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Guardar nombre de archivo
            fotoActual = nombreImagen;

            // Mostrar preview
            mostrarFoto(nombreImagen);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar imagen:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            ex.printStackTrace();
        }
    }
}

    private void mostrarFoto(String nombreImagen) {
        ImageIcon icono = ImageManager.cargarImagenPreview(nombreImagen, 110, 110);
        if (icono != null) {
            lblFoto.setIcon(icono);
            lblFoto.setText("");
            return;
        }

        lblFoto.setIcon(null);
        lblFoto.setText("U");
    }

    private void quitarFotoPerfil() {
        if (fotoActual == null || fotoActual.trim().isEmpty()) {
            mostrarFoto(null);
            return;
        }

        ImageManager.eliminarImagen(fotoActual);
        fotoActual = null;
        mostrarFoto(null);
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

    private void guardarCambios() {
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

        // NUEVA VALIDACIÓN: Nombres y apellidos solo letras
        if (!validarNombreFormato(nombres) || !validarNombreFormato(primerApellido)) {
            JOptionPane.showMessageDialog(
                this,
                "Nombre y apellidos solo pueden contener letras",
                "Formato inválido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Validar segundo apellido si está presente
        if (!segundoApellido.isEmpty() && !validarNombreFormato(segundoApellido)) {
            JOptionPane.showMessageDialog(
                this,
                "Nombre y apellidos solo pueden contener letras",
                "Formato inválido",
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

        // NUEVA VALIDACIÓN: Fecha no puede ser futura
        if (!fechaNacimiento.isEmpty()) {
            LocalDate fecha = parseFechaNacimiento(fechaNacimiento);
            if (fecha == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "La fecha de nacimiento debe tener formato AAAA-MM-DD o dd/MM/yyyy",
                    "Fecha inválida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            
            if (fecha.getYear() < 1900) {
                JOptionPane.showMessageDialog(
                    this,
                    "La fecha no puede ser anterior a 1900",
                    "Fecha invÃ¡lida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (fecha.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(
                    this,
                    "La fecha no puede ser futura",
                    "Fecha inválida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        ClienteInfo clienteActualizado = new ClienteInfo();
        clienteActualizado.setId(clienteId);
        clienteActualizado.setNombre(nombres);
        clienteActualizado.setPrimerApellido(primerApellido);
        clienteActualizado.setSegundoApellido(segundoApellido);
        clienteActualizado.setEmail(email);
        clienteActualizado.setTelefono(telefono);
        clienteActualizado.setFechaNacimiento(fechaNacimiento);
        clienteActualizado.setFoto(fotoActual);
        // Nota: NO establecemos nivel aquí - se calcula automáticamente por puntos en OperacionDAO

        boolean exito = ClienteController.actualizarCliente(clienteActualizado);

        if (exito) {
            JOptionPane.showMessageDialog(
                this,
                "Cliente actualizado exitosamente",
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
                "Error al actualizar el cliente. Por favor, intenta de nuevo.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String normalizarTelefono(String telefono) {
        return telefono == null ? "" : telefono.replaceAll("\\D", "");
    }

    /**
     * Valida que un nombre/apellido solo contenga letras, espacios, acentos y ñ.
     * No permite números, símbolos ni caracteres especiales.
     * 
     * @param texto el texto a validar
     * @return true si es válido, false si contiene caracteres inválidos
     */
    private boolean validarNombreFormato(String texto) {
        if (texto == null || texto.isEmpty()) {
            return false;
        }
        // Patrón: solo letras (incluyendo acentos), ñ, y espacios
        return texto.matches("^[a-zA-ZáéíóúñüÁÉÍÓÚÑÜàèìòùÀÈÌÒÙäëïöÄËÏÖ ]+$");
    }

    /**
     * Valida que una fecha tenga formato válido (AAAA-MM-DD o dd-mm-yyyy).
     * 
     * @param fechaString la fecha a validar
     * @return true si es válida, false si no
     */
    private boolean fechaValida(String fechaString) {
        return parseFechaNacimiento(fechaString) != null;
    }

    /**
     * Verifica si una fecha es futura comparándola con la fecha actual.
     * 
     * @param fechaString la fecha en formato AAAA-MM-DD o dd-mm-yyyy
     * @return true si la fecha es futura, false si es pasada o actual
     */
    private boolean fechaEsFutura(String fechaString) {
        LocalDate fecha = parseFechaNacimiento(fechaString);
        return fecha != null && fecha.isAfter(LocalDate.now());
    }

    private LocalDate parseFechaNacimiento(String fechaString) {
        try {
            return LocalDate.parse(fechaString);
        } catch (DateTimeParseException ignored) {
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(fechaString, formatter);
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    public String getNombres() { return txtNombres.getText(); }
    public String getPrimerApellido() { return txtPrimerApellido.getText(); }
    public String getSegundoApellido() { return txtSegundoApellido.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public String getTelefono() { return txtTelefono.getText(); }
    public String getFechaNacimiento() { return txtFechaNacimiento.getText(); }
}
