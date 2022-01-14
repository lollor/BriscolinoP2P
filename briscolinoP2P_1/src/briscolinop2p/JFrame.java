/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Lorenzo
 */
public class JFrame extends javax.swing.JFrame {

    public int click = 0;
    final int WIDTH = 100, HEIGHT = 147;
    final int xAv2 = 491, xAv1 = 370, xAv3 = 610, yAv = 40;
    final int xMazzo1 = 431, xMazzo2 = 558, yMazzo = 228;
    final int xM2 = 491, xM3 = 617, xM1 = 363, yM = 414;
    final int xMazzo = 125, xBriscola = 240;
    final int xMessaggio = 35, yMessaggio = 523;
    boolean finito = false;
    GestioneConnessione gestisci;
    volatile static Carta CartaSelezionata = null;

    //immagini
    private BufferedImage vuota, back;
    private static JFrame instance;

    /**
     * Creates new form JFrame
     */
    public JFrame() throws SocketException, FileNotFoundException, IOException {
        initComponents();
        gestisci = GestioneConnessione.getInstance();
        mostraCarteSulTavolo();
        vuota = ImageIO.read(new FileInputStream("../img_carte/vuota.gif"));
        back = ImageIO.read(new FileInputStream("../img_carte/back.gif"));
        instance = this;
    }

    public static JFrame getInstance() {
        return instance;
    }

    @Override
    public void paint(Graphics g) {
        Graphics offgc;
        Image offscreen = null;
        Dimension d = getSize();
        // create the offscreen buffer and associated Graphics
        offscreen = createImage(d.width, d.height);
        offgc = offscreen.getGraphics();
        // clear the exposed area
        offgc.setColor(getBackground());
        offgc.fillRect(0, 0, d.width, d.height);
        offgc.setColor(getForeground());

        try {
            try {
                // do normal redraw
                grafica(offgc);
            } catch (InterruptedException ex) {
                Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SocketException ex) {
            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        // transfer offscreen to window
        g.drawImage(offscreen, 0, 0, this);
    }

    //metodo che dice se il puntatore del mouse è sopra una determinata area
    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        if (mx > x && mx < x + width) {
            if (my > y && my < y + height) {
                return true;
            }
        }
        return false;
    }

    public void grafica(Graphics g) throws SocketException, IOException, InterruptedException {
        Tavolo t = gestisci.GetPartita().tavolo;
        if (!t.GetMazzo().vuoto()) {
            //se il mazzo non e vuoto stampo la carta mazzo
            g.drawImage(back, xMazzo2, yMazzo, WIDTH, HEIGHT, null);
        } else {
            g.drawImage(vuota, xMazzo2, yMazzo, WIDTH, HEIGHT, null);
        }
        stampaMazzo(t.GetCarteMostrate(), g);
        stampaManoSotto(t.GetGiocatore(true).mano, g);
        stampaManoSopra(t.GetGiocatore(false).mano, g);

        g.drawImage(ImageIO.read(new FileInputStream("src/briscolinop2p/logoBriscola.png")), 60, 50, 200, 136, null);
        g.setFont(new Font("Rockwell", Font.BOLD, 48));
        g.setColor(foregroundColor);
        g.drawString(text, xMessaggio, yMessaggio);
    }

    public void stampaMazzo(ArrayList<Carta> m, Graphics g) throws IOException, InterruptedException {

        if (m.size() == 0) {
            g.drawImage(vuota, xMazzo1, yMazzo, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xMazzo2, yMazzo, WIDTH, HEIGHT, null);
        }
        if (m.size() == 1) {
            g.drawImage(ImageIO.read(new FileInputStream(m.get(0).img)), xMazzo1, yMazzo, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xMazzo2, yMazzo, WIDTH, HEIGHT, null);
        }
        if (m.size() == 2) {
            g.drawImage(ImageIO.read(new FileInputStream(m.get(0).img)), xMazzo1, yMazzo, WIDTH, HEIGHT, null);
            g.drawImage(ImageIO.read(new FileInputStream(m.get(1).img)), xMazzo2, yMazzo, WIDTH, HEIGHT, null);
        }

        if (Tavolo.getTavolo().GetMazzo().vuoto()) {
            g.drawImage(vuota, xMazzo, yMazzo, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xBriscola, yMazzo, WIDTH, HEIGHT, null);
        } else {
            g.drawImage(back, xMazzo, yMazzo, WIDTH, HEIGHT, null);
            if (gestisci.GetPartita().tavolo.getBriscola() != null) {
                g.drawImage(ImageIO.read(new FileInputStream(gestisci.GetPartita().tavolo.getBriscola().img)), xBriscola, yMazzo, WIDTH, HEIGHT, null);
            } else {
                g.drawImage(vuota, xBriscola, yMazzo, WIDTH, HEIGHT, null);
            }
        }

    }

    public void stampaManoSotto(ArrayList<Carta> m, Graphics g) throws IOException, InterruptedException {
        sleep(500);
        if (m.size() == 3) {
            g.drawImage(ImageIO.read(new FileInputStream(m.get(0).img)), xM1, yM, WIDTH, HEIGHT, null);
            g.drawImage(ImageIO.read(new FileInputStream(m.get(1).img)), xM2, yM, WIDTH, HEIGHT, null);
            g.drawImage(ImageIO.read(new FileInputStream(m.get(2).img)), xM3, yM, WIDTH, HEIGHT, null);
        } else if (m.size() == 2) {
            g.drawImage(ImageIO.read(new FileInputStream(m.get(0).img)), xM1, yM, WIDTH, HEIGHT, null);
            g.drawImage(ImageIO.read(new FileInputStream(m.get(1).img)), xM2, yM, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xM3, yM, WIDTH, HEIGHT, null);
        } else if (m.size() == 1) {
            g.drawImage(ImageIO.read(new FileInputStream(m.get(0).img)), xM1, yM, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xM2, yM, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xM3, yM, WIDTH, HEIGHT, null);
        } else {
            g.drawImage(vuota, xM1, yM, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xM2, yM, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xM3, yM, WIDTH, HEIGHT, null);
        }
    }

    public void stampaManoSopra(ArrayList<Carta> m, Graphics g) throws IOException, InterruptedException {
        if (m.size() == 3) {
            g.drawImage(back, xAv2, yAv, WIDTH, HEIGHT, null);
            g.drawImage(back, xAv1, yAv, WIDTH, HEIGHT, null);
            //sleep(500);
            g.drawImage(back, xAv3, yAv, WIDTH, HEIGHT, null);
        } else if (m.size() == 2) {
            g.drawImage(back, xAv1, yAv, WIDTH, HEIGHT, null);
            //sleep(500);
            g.drawImage(back, xAv2, yAv, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xAv3, yAv, WIDTH, HEIGHT, null);
        } else if (m.size() == 1) {
            //sleep(500);
            g.drawImage(back, xAv1, yAv, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xAv2, yAv, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xAv3, yAv, WIDTH, HEIGHT, null);
        } else {
            g.drawImage(vuota, xAv1, yAv, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xAv2, yAv, WIDTH, HEIGHT, null);
            g.drawImage(vuota, xAv3, yAv, WIDTH, HEIGHT, null);
        }

    }

    private String text = "";
    private Color foregroundColor = Color.BLACK;

    public void SetMessaggio(String text, Color foregroundColor) {
        this.text = text;
        this.foregroundColor = foregroundColor;
    }

    public void partitaFinita() {
        Tavolo t = gestisci.GetPartita().tavolo;
        finito = true;
        
        
        //il giocatore sfidante sta sempre sopra mentre tu sempre sotto
       

        if (t.GetGiocatore(true).punteggio > 60) {
            Util.ShowDialog("Hai Vinto! "+t.GetGiocatore(true).punteggio+"-"+(120-t.GetGiocatore(true).punteggio), "Partita Finita");
        } else if (t.GetGiocatore(true).punteggio == 60) {
             Util.ShowDialog("Hai Pareggiato! 60-60 ", "Partita Finita");
        } else {
           Util.ShowDialog("Hai Perso! "+t.GetGiocatore(true).punteggio+"-"+(120-t.GetGiocatore(true).punteggio), "Partita Finita");
        }
        SetMessaggio("", foregroundColor);
        repaint();
    }

    //PER MOSTRARE LE CARTE SUL TAVOLO TI CONSIGLIO DI FARE UN THREAD CHE IN CONTINUAZIONE PRENDE L'ARRAY 
    //DI CARTE DAL TAVOLO E LO MOSTRA
    //TI FACCIO LA FUNZIONE POI TU SAI QUELLO CHE C'E DA METTERE DENTRO
    public void mostraCarteSulTavolo() {
        new Thread(() -> {
            while (finito == false) {
                //mostra tutte le carte del tavolo
                repaint();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            partitaFinita();
        }).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mazzo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Briscola");
        setBackground(new java.awt.Color(0, 153, 0));
        setBounds(new java.awt.Rectangle(0, 0, 5, 5));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(mazzo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(908, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(189, 189, 189)
                .addComponent(mazzo, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(245, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // TODO add your handling code here:
        if (click == 0) {
            int mx = evt.getX();
            int my = evt.getY();
            ArrayList<Carta> mano = gestisci.GetPartita().tavolo.GetGiocatore(true).mano;
            int lengthmano = mano.size();

            if (mouseOver(mx, my, xM1, yM, WIDTH, HEIGHT)) {
                if (lengthmano >= 1) {
                    CartaSelezionata = mano.get(0);
                }
            } else if (mouseOver(mx, my, xM2, yM, WIDTH, HEIGHT)) {
                if (lengthmano >= 2) {
                    CartaSelezionata = mano.get(1);
                }
            } else if (mouseOver(mx, my, xM3, yM, WIDTH, HEIGHT)) {
                if (lengthmano == 3) {
                    CartaSelezionata = mano.get(2);
                }
            }
        }

        click++;
    }//GEN-LAST:event_formMousePressed

    /**
     * @param args the command line arguments
     */
    public void start(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new JFrame().setVisible(true);
                } catch (SocketException ex) {
                    Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel mazzo;
    // End of variables declaration//GEN-END:variables

}
