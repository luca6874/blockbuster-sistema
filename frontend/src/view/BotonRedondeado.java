package frontend.src.view;

import frontend.src.controller.Ventana;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

/**
 * Botón redondeado con borde opcional.
 * Utiliza Graphics2D para dibujar el botón con bordes redondeados.
 * Utilizado en viewRegister y politicas.
 */
public class BotonRedondeado extends JButton {
    private Color bg, fg;
    private boolean conBorde = false;
    private int arco = 12; 

    public BotonRedondeado(String texto, Color bg, Color fg) {
        super(texto);
        this.bg = bg;
        this.fg = fg;
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
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arco, arco));
        
        
        if (conBorde) {
            g2.setColor(fg);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth()-1.5, getHeight()-1.5, arco, arco));
        }
        
        g2.dispose();
        super.paintComponent(g);
    }
}