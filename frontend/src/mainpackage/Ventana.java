package frontend.src.mainpackage;

import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame {

    public Ventana() {
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("Briarbuster - Sistema Administrativo");
        this.setLayout(new BorderLayout()); 

        router("splash");

        this.setVisible(true);
    }

    public void router(String target) {
        this.getContentPane().removeAll();
        
        if (target.equals("splash")) {
            this.add(new ViewSplash(this));
        } else if (target.equals("login")) {
            this.add(new ViewLogin(this));
        }

        this.revalidate();
        this.repaint();
    }

    // Agrega esto dentro de tu clase Ventana
public void setOscurecer(boolean activar) {
    if (activar) {
        // Creamos un panel negro semi-transparente
        JPanel panelOscuro = new JPanel();
        panelOscuro.setBackground(new Color(0, 0, 0, 150)); 
        this.setGlassPane(panelOscuro);
        this.getGlassPane().setVisible(true);
    } else {
        this.getGlassPane().setVisible(false);
    }
    }
}