/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Lorenzo
 */
public class JFrame extends javax.swing.JFrame {

    Tavolo t;
    GestioneConnessione gestisci;
    /**
     * Creates new form JFrame
     */
    public JFrame() throws SocketException {
        initComponents();
        t = Tavolo.getTavolo();
        gestisci = GestioneConnessione.getConnessione();
        gestisci.start();
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
            // do normal redraw
            grafica(offgc);
        } catch (SocketException ex) {
            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        // transfer offscreen to window
        g.drawImage(offscreen, 0, 0, this);
    }

    public void grafica(Graphics g) throws SocketException, IOException {

        t = Tavolo.getTavolo();
        if (!t.mazzo.vuoto()) {
            //se il mazzo non e vuoto stampo la carta mazzo
            mazzo2.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/back.gif"))));
        } else {
            mazzo2.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
        }

        if (GestionePartita.getPartita().sonoMazziere) {
            stampaManoSotto(t.mazziere.mano);
            stampaManoSopra(t.Sfidante.mano);
        } else {
            stampaManoSopra(t.mazziere.mano);
            stampaManoSotto(t.Sfidante.mano);
        }

        stampaMazzo(t.mazzo.mazzo);

    }

    public void stampaMazzo(ArrayList<Carta> m) throws IOException {
        if (m.size() == 0) {
            mazzo1.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
            mazzo2.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
        }
         if (m.size() == 1) {
            mazzo1.setIcon(new ImageIcon(ImageIO.read(new File(m.get(0).img))));
            mazzo2.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
        }
          if (m.size() == 2) {
            mazzo1.setIcon(new ImageIcon(ImageIO.read(new File(m.get(0).img))));
            mazzo2.setIcon(new ImageIcon(ImageIO.read(new File(m.get(1).img))));
        }
    }

    public void stampaManoSotto(ArrayList<Carta> m) throws IOException {
        if (m.size() == 3) {
            m1.setIcon(new ImageIcon(ImageIO.read(new File(m.get(0).img))));
            m2.setIcon(new ImageIcon(ImageIO.read(new File(m.get(1).img))));
            m3.setIcon(new ImageIcon(ImageIO.read(new File(m.get(2).img))));
        } else if (m.size() == 2) {
            m1.setIcon(new ImageIcon(ImageIO.read(new File(m.get(0).img))));
            m2.setIcon(new ImageIcon(ImageIO.read(new File(m.get(1).img))));
            m3.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
        } else if (m.size() == 1) {
            m1.setIcon(new ImageIcon(ImageIO.read(new File(m.get(0).img))));
            m2.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
            m3.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
        } else {
            m1.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
            m2.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
            m3.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
        }
    }

    public void stampaManoSopra(ArrayList<Carta> m) throws IOException {
        if (m.size() == 3) {
            av1.setIcon(new ImageIcon(ImageIO.read(new File(m.get(0).img))));
            av2.setIcon(new ImageIcon(ImageIO.read(new File(m.get(1).img))));
            av3.setIcon(new ImageIcon(ImageIO.read(new File(m.get(2).img))));
        } else if (m.size() == 2) {
            av1.setIcon(new ImageIcon(ImageIO.read(new File(m.get(0).img))));
            av2.setIcon(new ImageIcon(ImageIO.read(new File(m.get(1).img))));
            av3.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
        } else if (m.size() == 1) {
            av1.setIcon(new ImageIcon(ImageIO.read(new File(m.get(0).img))));
            av2.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
            av3.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
        } else {
            av1.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
            av2.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
            av3.setIcon(new ImageIcon(ImageIO.read(new File("../../img_carte/vuota.gif"))));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        av1 = new javax.swing.JLabel();
        av2 = new javax.swing.JLabel();
        av3 = new javax.swing.JLabel();
        m1 = new javax.swing.JLabel();
        m2 = new javax.swing.JLabel();
        m3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        punteggioSotto = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        punteggioSopra = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        mazzo2 = new javax.swing.JLabel();
        mazzo = new javax.swing.JLabel();
        mazzo1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        punteggioSotto.setColumns(20);
        punteggioSotto.setRows(5);
        jScrollPane1.setViewportView(punteggioSotto);

        punteggioSopra.setColumns(20);
        punteggioSopra.setRows(5);
        jScrollPane2.setViewportView(punteggioSopra);

        jLabel2.setText("------------------------------");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(m3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(m1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(m2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(av3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(av1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(av2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(274, 274, 274))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(mazzo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                .addComponent(mazzo1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(mazzo2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(783, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(89, 89, 89)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(av3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(av1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(av2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mazzo1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mazzo2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mazzo, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(m3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(201, 201, 201)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(334, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel av1;
    private javax.swing.JLabel av2;
    private javax.swing.JLabel av3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel m1;
    private javax.swing.JLabel m2;
    private javax.swing.JLabel m3;
    private javax.swing.JLabel mazzo;
    private javax.swing.JLabel mazzo1;
    private javax.swing.JLabel mazzo2;
    private javax.swing.JTextArea punteggioSopra;
    private javax.swing.JTextArea punteggioSotto;
    // End of variables declaration//GEN-END:variables
}
