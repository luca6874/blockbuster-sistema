package frontend.src.view;

import frontend.src.controller.ClienteController;
import frontend.src.dao.OperacionDAO;
import frontend.src.service.ClienteCredentialPDFGenerator;
import frontend.src.service.ClienteInfoPDFGenerator;
import frontend.src.service.ImageManager;
import java.io.File;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Panel de Resumen del Cliente.
 */
public class PnlResumenCliente extends JPanel {
    private ViewDashboard parent;
    private PnlGestionClientes panelGestion;
    
    // Referencias a labels para actualización dinámica
    private JLabel lblFecha;
    private JLabel lblNombre;
    private JLabel lblEmail;
    private JLabel lblId;
    private JLabel lblTelefono;
    private JLabel lblJuegosRentados;
    private JLabel lblJuegosComprados;
    private JLabel lblDescuentosUsados;
    private JLabel lblDescFrecuencia;
    private JLabel badgeEstatus;
    private JLabel badgeFrecuente;
    private JLabel lblAvatarIcon;
    
    // Datos del cliente actual
    private frontend.src.model.ClienteInfo clienteActual;

    public PnlResumenCliente(ViewDashboard parent) {
        this(parent, null);
    }

    public PnlResumenCliente(ViewDashboard parent, PnlGestionClientes panelGestion) {
        this.parent = parent;
        this.panelGestion = panelGestion;
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(250, 300));
        this.setSize(250, 300);
        this.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        initComponentes();
    }

    private void initComponentes() {
        addFecha();
        addAvatarYCabecera();
        addInformacionBasica();
        addEstadisticas();
        addAcciones();
    }

    private void addFecha() {
        lblFecha = new JLabel("05/02/2004", SwingConstants.RIGHT);
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 10));
        lblFecha.setForeground(new Color(100, 100, 100));
        lblFecha.setBounds(120, 10, 120, 16);
        this.add(lblFecha);
    }

    private void addAvatarYCabecera() {
        JPanel avatarCont = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 245, 245));
                g2.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        avatarCont.setOpaque(false);
        avatarCont.setBounds(15, 30, 50, 50);

        lblAvatarIcon = new JLabel();
        lblAvatarIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblAvatarIcon.setBounds(0, 0, 50, 50);
        actualizarAvatar(null);
        avatarCont.add(lblAvatarIcon);
        this.add(avatarCont);

        lblNombre = new JLabel();
        lblNombre.setFont(new Font("Arial", Font.BOLD, 13));
        lblNombre.setForeground(Color.BLACK);
        lblNombre.setBounds(80, 30, 150, 18);
        this.add(lblNombre);

        lblEmail = new JLabel();
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 10));
        lblEmail.setForeground(new Color(120, 120, 120));
        lblEmail.setBounds(80, 50, 150, 16);
        this.add(lblEmail);

        badgeEstatus = createBadge("Activo", new Color(46, 204, 113), Color.WHITE, 80, 70, 65, 18);
        this.add(badgeEstatus);
        badgeFrecuente = createBadge("Cliente frecuente", new Color(245, 193, 65), new Color(80, 60, 20), 150, 70, 85, 18);
        this.add(badgeFrecuente);
    }

    private void addInformacionBasica() {
        lblId = new JLabel("ID: 13587");
        lblId.setFont(new Font("Arial", Font.PLAIN, 11));
        lblId.setForeground(Color.BLACK);
        lblId.setBounds(15, 100, 110, 16);
        this.add(lblId);

        lblTelefono = new JLabel("55 271 4314", SwingConstants.RIGHT);
        lblTelefono.setFont(new Font("Arial", Font.PLAIN, 11));
        lblTelefono.setForeground(Color.BLACK);
        lblTelefono.setBounds(130, 100, 105, 16);
        this.add(lblTelefono);
    }

    private void addEstadisticas() {
        int y = 125;
        // Stat 1: Juegos rentados
        JPanel card1 = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(220, 220, 220));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        card1.setOpaque(false);
        card1.setBounds(10, y, 55, 70);
        JLabel icon1 = createIconLabel("/frontend/src/images/iconVideoGame1.png", 0, 4);
        card1.add(icon1);
        JLabel title1 = createTitleLabel("Juegos rentados", 2, 22);
        card1.add(title1);
        lblJuegosRentados = createValueLabel("3", 0, 46);
        card1.add(lblJuegosRentados);
        this.add(card1);
        
        // Stat 2: Juegos comprados
        JPanel card2 = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(220, 220, 220));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        card2.setOpaque(false);
        card2.setBounds(67, y, 55, 70);
        JLabel icon2 = createIconLabel("/frontend/src/images/money.png", 0, 4);
        card2.add(icon2);
        JLabel title2 = createTitleLabel("Juegos comprados", 2, 22);
        card2.add(title2);
        lblJuegosComprados = createValueLabel("2", 0, 46);
        card2.add(lblJuegosComprados);
        this.add(card2);
        
        // Stat 3: Descuentos usados
        JPanel card3 = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(220, 220, 220));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        card3.setOpaque(false);
        card3.setBounds(124, y, 55, 70);
        JLabel icon3 = createIconLabel("/frontend/src/images/discount.png", 0, 4);
        card3.add(icon3);
        JLabel title3 = createTitleLabel("Descuentos usados", 2, 22);
        card3.add(title3);
        lblDescuentosUsados = createValueLabel("0", 0, 46);
        card3.add(lblDescuentosUsados);
        this.add(card3);
        
        //Puntos acumulados
        JPanel card4 = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(220, 220, 220));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        card4.setOpaque(false);
        card4.setBounds(181, y, 55, 70);
        JLabel icon4 = createIconLabel("/frontend/src/images/level.png", 0, 4);
        card4.add(icon4);
        JLabel title4 = createTitleLabel("Puntos acumulados", 2, 22);
        card4.add(title4);
        lblDescFrecuencia = createValueLabel("0", 0, 46);
        card4.add(lblDescFrecuencia);
        this.add(card4);
    }
    
    private JLabel createIconLabel(String iconPath, int x, int y) {
        JLabel icon = new JLabel();
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setBounds(x, y, 55, 18);
        try {
            ImageIcon imageIcon = ImageManager.cargarImagenPreview(normalizarNombreImagen(iconPath), 16, 16);
            icon.setIcon(imageIcon);
        } catch (Exception ignored) {
        }
        return icon;
    }

    private String normalizarNombreImagen(String iconPath) {
        if (iconPath == null) {
            return null;
        }

        int slash = iconPath.lastIndexOf('/');
        return slash >= 0 ? iconPath.substring(slash + 1) : iconPath;
    }

    private void actualizarAvatar(String foto) {
        if (lblAvatarIcon == null) {
            return;
        }

        ImageIcon avatar = ImageManager.cargarImagenPreview(foto, 44, 44);
        if (avatar == null) {
            avatar = ImageManager.cargarImagenPreview("iconUserBig.png", 44, 44);
        }

        lblAvatarIcon.setIcon(avatar);
    }
    
    private JLabel createTitleLabel(String title, int x, int y) {
        JLabel lblTitle = new JLabel("<html><center>" + title + "</center></html>", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 8));
        lblTitle.setForeground(new Color(100, 100, 100));
        lblTitle.setBounds(x, y, 51, 24);
        return lblTitle;
    }
    
    private JLabel createValueLabel(String value, int x, int y) {
        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 14));
        lblValue.setForeground(Color.BLACK);
        lblValue.setBounds(x, y, 55, 22);
        return lblValue;
    }

    private void addAcciones() {
        JButton btnDescargar = createActionButton("Descargar info", new Color(110, 60, 70));
        btnDescargar.setBounds(15, 205, 110, 26);

        btnDescargar.addActionListener(e ->{
            if(clienteActual == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "No hay cliente seleccionado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                String idTexto = clienteActual.getId();
                int idNumerico = Integer.parseInt(idTexto.replace("CLI-", ""));

                int totalCompras = OperacionDAO.contarJuegosComprados(idNumerico);
                int totalRentas = OperacionDAO.contarJuegosRentados(idNumerico);
                int totalOperaciones = totalCompras + totalRentas;

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar resumen del cliente");
                fileChooser.setSelectedFile(
                    new File("cliente_" + idTexto + ".pdf")
                );


                int resultado = fileChooser.showSaveDialog(this);

                if (resultado != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                File archivo = fileChooser.getSelectedFile();

                ClienteInfoPDFGenerator generator = new ClienteInfoPDFGenerator();

                 generator.generar(
                    clienteActual,
                    totalCompras,
                    totalRentas,
                    totalOperaciones,
                    archivo
                );

                  JOptionPane.showMessageDialog(
                        this,
                        "PDF generado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                    );
            }catch (Exception ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                    this,
                    "Error al generar el PDF:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        this.add(btnDescargar);

        JButton btnTarjeta = createActionButton("Generar tarjeta", new Color(152, 33, 54));
        btnTarjeta.setBounds(130, 205, 110, 26);
        btnTarjeta.addActionListener(e -> {
            try {
                if (clienteActual == null) {
                    JOptionPane.showMessageDialog(
                        this,
                        "No hay cliente seleccionado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar tarjeta de cliente");
                fileChooser.setSelectedFile(
                    new File("tarjeta_" + clienteActual.getId() + ".pdf")
                );

                int opcion = fileChooser.showSaveDialog(this);

                if (opcion != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                ClienteCredentialPDFGenerator generator =
                    new ClienteCredentialPDFGenerator();

                generator.generar(clienteActual, fileChooser.getSelectedFile());

                JOptionPane.showMessageDialog(
                    this,
                    "Tarjeta generada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {
                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                    this,
                    "Error al generar la tarjeta",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });


        this.add(btnTarjeta);

        JButton btnEditar = createActionButton("Editar cliente", new Color(46, 204, 113));
        btnEditar.setBounds(15, 235, 110, 26);
        btnEditar.addActionListener(e -> {
            // Obtener datos del cliente desde el panel
            String clienteId = obtenerClienteId();
            String nombres = obtenerNombres();
            String primerApellido = clienteActual != null ? (clienteActual.getPrimerApellido() != null ? clienteActual.getPrimerApellido() : "") : "";
            String segundoApellido = clienteActual != null ? (clienteActual.getSegundoApellido() != null ? clienteActual.getSegundoApellido() : "") : "";
            String email = obtenerEmail();
            String telefono = obtenerTelefono();
            String fechaNacimiento = obtenerFechaNacimiento();
            
            DlgEdicionCliente dlgEditar = new DlgEdicionCliente(parent.getHost(), clienteId, nombres, primerApellido, segundoApellido, email, telefono, fechaNacimiento, panelGestion);
            parent.getHost().setOscurecer(true);
            dlgEditar.setVisible(true);
        });
        this.add(btnEditar);

        JButton btnEliminar = createActionButton("Eliminar cliente", new Color(231, 76, 60));
        btnEliminar.setBounds(130, 235, 110, 26);
        btnEliminar.addActionListener(e -> {
            // Validar que haya cliente seleccionado
            if (clienteActual == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "No hay cliente seleccionado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            // Obtener datos del cliente
            String clienteId = obtenerClienteId();
            String nombreCliente = obtenerNombres();
            if (panelGestion != null) {
                panelGestion.confirmarEliminarCliente(clienteId, nombreCliente);
                return;
            }
            
            // Mostrar confirmación
            int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas eliminar este cliente (" + nombreCliente + ")?\n\nEsta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (respuesta == JOptionPane.YES_OPTION) {
                // Llamar al controlador para eliminar
                String resultado = ClienteController.eliminarCliente(clienteId);
                if ("OK".equals(resultado)) {

                    JOptionPane.showMessageDialog(
                        this,
               "Cliente eliminado exitosamente",
                 "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    parent.setContenido(new PnlGestionClientes(parent));

                } else if ("OPERACIONES".equals(resultado)) {

                        JOptionPane.showMessageDialog(
                            this,
                   "Este cliente no puede eliminarse porque tiene operaciones registradas.",
                     "Operación no permitida",
                            JOptionPane.WARNING_MESSAGE
                        );
                } else {
                    JOptionPane.showMessageDialog(
                        this,
              "Error al eliminar el cliente. Por favor, intenta de nuevo.",
                "Error",
                        JOptionPane.ERROR_MESSAGE
                    );

                        }                                     
            }
        });
        this.add(btnEliminar);
    }


    private JLabel createBadge(String text, Color background, Color foreground, int x, int y, int width, int height) {
        JLabel badge = new JLabel(text, SwingConstants.CENTER);
        badge.setFont(new Font("Arial", Font.BOLD, 9));
        badge.setOpaque(true);
        badge.setBackground(background);
        badge.setForeground(foreground);
        badge.setBounds(x, y, width, height);
        badge.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1, true));
        return badge;
    }

    private JButton createActionButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 10));
        button.setForeground(Color.WHITE);
        button.setBackground(bg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Métodos para obtener datos del cliente
    private String obtenerClienteId() {
        return clienteActual != null ? clienteActual.getId() : "N/A";
    }

    private String obtenerNombres() {
        return clienteActual != null ? clienteActual.getNombres() : "Sin datos";
    }


    private String obtenerEmail() {
        return clienteActual != null ? clienteActual.getEmail() : "N/A";
    }

    private String obtenerTelefono() {
        return clienteActual != null && clienteActual.getTelefono() != null ? clienteActual.getTelefono() : "N/A";
    }

    private String obtenerFechaNacimiento() {
        return clienteActual != null && clienteActual.getFechaNacimiento() != null ? clienteActual.getFechaNacimiento() : "N/A";
    }
    
    /**
     * Actualiza el panel con los datos de un cliente específico.
     * 
     * @param cliente ClienteInfo con los datos a mostrar
     */
    public void actualizarConDatos(frontend.src.model.ClienteInfo cliente) {
        if (cliente == null) {
            System.err.println("Cliente nulo en PnlResumenCliente");
            return;
        }
        
        this.clienteActual = cliente;
        actualizarAvatar(cliente.getFoto());
        
        // Actualizar nombre completo
        if (lblNombre != null) {
            lblNombre.setText(cliente.getNombre());
        }
        
        // Actualizar email
        if (lblEmail != null) {
            lblEmail.setText(cliente.getEmail() != null ? cliente.getEmail() : "N/A");
        }
        
        // Actualizar teléfono
        if (lblTelefono != null) {
            lblTelefono.setText(cliente.getTelefono() != null ? cliente.getTelefono() : "N/A");
        }
        
        // Actualizar ID
        if (lblId != null) {
            lblId.setText("ID: " + (cliente.getId() != null ? cliente.getId() : "N/A"));
        }
        
        // Actualizar fecha de nacimiento
        if (lblFecha != null && cliente.getFechaNacimiento() != null) {
            lblFecha.setText(cliente.getFechaNacimiento());
        }
        
        // Actualizar estadísticas de juegos y puntos
        actualizarEstadisticas();
        
        // Actualizar badges de estatus y frecuencia
        actualizarBadges();
        
        // Revalidar el panel para refrescar la UI
        this.revalidate();
        this.repaint();
    }
    
    /**
     * Actualiza las estadísticas de juegos rentados, comprados, descuentos usados y puntos acumulados.
     * Obtiene datos dinámicos del cliente seleccionado desde la base de datos.
     */
    private void actualizarEstadisticas() {
        if (clienteActual == null) {
            // Si no hay cliente, mostrar valores por defecto
            if (lblJuegosRentados != null) lblJuegosRentados.setText("0");
            if (lblJuegosComprados != null) lblJuegosComprados.setText("0");
            if (lblDescuentosUsados != null) lblDescuentosUsados.setText("0");
            if (lblDescFrecuencia != null) lblDescFrecuencia.setText("0");
            return;
        }
        
        try {
            // Obtener ID numérico real del cliente (ej: 3 de "CLI-003")
            int clienteIdNumerico = clienteActual.getIdNumerico();
            if (clienteIdNumerico <= 0) {
                resetearEstadisticas();
                return;
            }
            
            // Actualizar juegos rentados desde operaciones
            if (lblJuegosRentados != null) {
                int rentados = OperacionDAO.contarJuegosRentados(clienteIdNumerico);
                lblJuegosRentados.setText(String.valueOf(rentados));
            }
            
            // Actualizar juegos comprados desde operaciones
            if (lblJuegosComprados != null) {
                int comprados = OperacionDAO.contarJuegosComprados(clienteIdNumerico);
                lblJuegosComprados.setText(String.valueOf(comprados));
            }
            
            // Actualizar puntos acumulados desde el cliente
            if (lblDescFrecuencia != null) {
                int puntos = clienteActual.getPuntos();
                lblDescFrecuencia.setText(String.valueOf(puntos));
            }
            
            // Nota: lblDescuentosUsados puede ser actualizado si existe un método en DAO para contar descuentos
            // Por ahora se mantiene como está (puede ser mejorado en futuras versiones)
            if (lblDescuentosUsados != null) {
                // Mantener valor actual o implementar lógica de descuentos si es necesario
                if (lblDescuentosUsados.getText().equals("")) {
                    lblDescuentosUsados.setText("0");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error al actualizar estadísticas: " + e.getMessage());
            e.printStackTrace();
            resetearEstadisticas();
        }
    }
    
    /**
     * Reinicia las estadísticas a valores por defecto.
     */
    private void resetearEstadisticas() {
        if (lblJuegosRentados != null) lblJuegosRentados.setText("0");
        if (lblJuegosComprados != null) lblJuegosComprados.setText("0");
        if (lblDescuentosUsados != null) lblDescuentosUsados.setText("0");
        if (lblDescFrecuencia != null) lblDescFrecuencia.setText("0");
    }

    /**
     * Actualiza los badges de estatus y frecuencia del cliente.
     */
    private void actualizarBadges() {
        if (clienteActual == null) return;
        
        // Actualizar badge de estatus
        if (badgeEstatus != null) {
            String estatus = clienteActual.getEstatus();
            if ("Activo".equals(estatus)) {
                badgeEstatus.setBackground(new Color(46, 204, 113));
                badgeEstatus.setForeground(Color.WHITE);
                badgeEstatus.setText("Activo");
            } else if ("Inactivo".equals(estatus)) {
                badgeEstatus.setBackground(new Color(189, 195, 199));
                badgeEstatus.setForeground(Color.BLACK);
                badgeEstatus.setText("Inactivo");
            } else if ("Suspendido".equals(estatus)) {
                badgeEstatus.setBackground(new Color(231, 76, 60));
                badgeEstatus.setForeground(Color.WHITE);
                badgeEstatus.setText("Suspendido");
            }
        }
        
        // Actualizar badge de frecuencia / nivel de fidelidad
        if (badgeFrecuente != null) {
            if (clienteActual.isFrecuente()) {
                // Mostrar el nombre del nivel de fidelidad dinámicamente
                String nombreNivel = frontend.src.dao.NivelFidelidad.obtenerNombreNivel(clienteActual.getLvlFidelidad());
                badgeFrecuente.setVisible(true);
                badgeFrecuente.setText("Nivel: " + nombreNivel);
                
                // Cambiar color según el nivel
                int[] colorNivel = frontend.src.dao.NivelFidelidad.obtenerColorNivel(clienteActual.getLvlFidelidad());
                Color bgColor = new Color(colorNivel[0], colorNivel[1], colorNivel[2]);
                badgeFrecuente.setBackground(bgColor);
                badgeFrecuente.setForeground(Color.WHITE);
            } else {
                badgeFrecuente.setVisible(false);
            }
        }
    }
}
