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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;
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

    boolean finito = false;
    GestioneConnessione gestisci;
    volatile static Carta CartaSelezionata = null;
    
    //immagini
    private ImageIcon vuota, back;

    /**
     * Creates new form JFrame
     */
    public JFrame() throws SocketException, FileNotFoundException, IOException {
        initComponents();
        gestisci = GestioneConnessione.getInstance();
        gestisci.start();
        mostraCarteSulTavolo();
        
        vuota = new ImageIcon(ImageIO.read(new FileInputStream("../img_carte/vuota.gif")));
        back = new ImageIcon(ImageIO.read(new FileInputStream("../img_carte/back.gif")));
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

    public void grafica(Graphics g) throws SocketException, IOException, InterruptedException {
        Tavolo t = gestisci.GetPartita().tavolo;
        if (!t.GetMazzo().vuoto()) {
            //se il mazzo non e vuoto stampo la carta mazzo
            mazzo2.setIcon(back);
        } else {
            mazzo2.setIcon(vuota);
        }

        stampaManoSotto(t.GetGiocatore(true).mano);
        stampaManoSopra(t.GetGiocatore(false).mano);
        stampaMazzo(t.GetCarteMostrate());
    }

    public void stampaMazzo(ArrayList<Carta> m) throws IOException {
        if (m.size() == 0) {
            mazzo1.setIcon(vuota);
            mazzo2.setIcon(vuota);
        }
        if (m.size() == 1) {
            mazzo1.setIcon(new ImageIcon(ImageIO.read(new FileInputStream(m.get(0).img))));
            mazzo2.setIcon(vuota);
        }
        if (m.size() == 2) {
            mazzo1.setIcon(new ImageIcon(ImageIO.read(new FileInputStream(m.get(0).img))));
            mazzo2.setIcon(new ImageIcon(ImageIO.read(new FileInputStream(m.get(1).img))));
        }
    }

    public void stampaManoSotto(ArrayList<Carta> m) throws IOException, InterruptedException {
        if (m.size() == 3) { 
            System.out.println(m.get(0).img);
            m1.setIcon(new ImageIcon(ImageIO.read(new FileInputStream(m.get(0).img))));
            m2.setIcon(new ImageIcon(ImageIO.read(new FileInputStream(m.get(1).img))));
            sleep(500);
            m3.setIcon(new ImageIcon(ImageIO.read(new FileInputStream(m.get(2).img))));
        } else if (m.size() == 2) {
            m1.setIcon(new ImageIcon(ImageIO.read(new FileInputStream(m.get(0).img))));
            sleep(500);
            m2.setIcon(new ImageIcon(ImageIO.read(new FileInputStream(m.get(1).img))));
            m3.setIcon(vuota);
        } else if (m.size() == 1) {
            sleep(500);
            m1.setIcon(new ImageIcon(ImageIO.read(new FileInputStream(m.get(0).img))));
            m2.setIcon(vuota);
            m3.setIcon(vuota);
        } else {
            m1.setIcon(vuota);
            m2.setIcon(vuota);
            m3.setIcon(vuota);
        }
    }

    public void stampaManoSopra(ArrayList<Carta> m) throws IOException, InterruptedException {
        if (m.size() == 3) {
            av1.setIcon(back);
            av2.setIcon(back);
            sleep(500);
            av3.setIcon(back);
        } else if (m.size() == 2) {
            av1.setIcon(back);
            sleep(500);
            av2.setIcon(back);
            av3.setIcon(vuota);
        } else if (m.size() == 1) {
            sleep(500);
            av1.setIcon(back);
            av2.setIcon(vuota);
            av3.setIcon(vuota);
        } else {
            av1.setIcon(vuota);
            av2.setIcon(vuota);
            av3.setIcon(vuota);
        }

    }

    public void partitaFinita() {
        Tavolo t = gestisci.GetPartita().tavolo;
        finito = true;
        //il giocatore sfidante sta sempre sopra mentre tu sempre sotto
        punteggioSopra.setText(t.GetGiocatore(false).punteggio);
        punteggioSotto.setText(t.GetGiocatore(true).punteggio);

        if (parseInt(t.GetGiocatore(true).punteggio) > parseInt(t.GetGiocatore(false).punteggio)) {
            InfoIo.setText("hai vinto");
            InfoSfidante.setText("hai perso");
        } else if (parseInt(t.GetGiocatore(true).punteggio) == parseInt(t.GetGiocatore(false).punteggio)) {
            InfoIo.setText("pareggio");
            InfoSfidante.setText("pareggio");
        } else {
            InfoIo.setText("hai perso");
            InfoSfidante.setText("hai vinto");
        }
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
        InfoIo = new javax.swing.JLabel();
        InfoSfidante = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 102, 51));
        setBounds(new java.awt.Rectangle(0, 0, 5, 5));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        av1.setBackground(new java.awt.Color(255, 255, 255));

        av2.setBackground(new java.awt.Color(255, 255, 255));

        av3.setBackground(new java.awt.Color(255, 255, 255));

        m1.setBackground(new java.awt.Color(255, 255, 255));

        m2.setBackground(new java.awt.Color(255, 255, 255));

        m3.setBackground(new java.awt.Color(255, 255, 255));

        punteggioSotto.setBackground(new java.awt.Color(204, 204, 204));
        punteggioSotto.setColumns(20);
        punteggioSotto.setRows(5);
        jScrollPane1.setViewportView(punteggioSotto);

        punteggioSopra.setBackground(new java.awt.Color(204, 204, 204));
        punteggioSopra.setColumns(20);
        punteggioSopra.setRows(5);
        jScrollPane2.setViewportView(punteggioSopra);

        jLabel2.setText("------------------------------");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(mazzo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(mazzo1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(mazzo2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(m3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(m1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(m2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(av3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(av1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(av2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(74, 74, 74)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(InfoSfidante, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(57, 57, 57))
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(57, 57, 57)))
                            .addComponent(InfoIo, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(InfoSfidante, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(121, 121, 121)
                                .addComponent(mazzo, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(116, 116, 116)))
                        .addComponent(InfoIo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(av3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(av1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(av2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mazzo1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mazzo2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(m3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20))
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
                } catch (IOException ex) {
                    Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel InfoIo;
    private javax.swing.JLabel InfoSfidante;
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
