/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Lorenzo
 */
public class GestionePartita extends Thread {

    //Giocatore giocatoreLocale, giocatoreEsterno;
    private static GestionePartita istanza = null;
    private Mazzo mazzo;
    private GestioneConnessione gestioneConnessione;
    boolean sonoMazziere;
    boolean partitaFinita;

    public GestionePartita() throws SocketException {
        istanza = this;
        this.partitaFinita = false;
        this.sonoMazziere = false;
        this.gestioneConnessione = GestioneConnessione.getConnessione();
    }

    public static synchronized GestionePartita getPartita() throws SocketException {
        if (istanza == null) {
            istanza = new GestionePartita();
        }
        return istanza;
    }

    @Override
    public void run() {
        while (true) {
            while (!partitaFinita) {

            }
        }
    }

    public void IniziaPartita(boolean sonoMazziere) {
        this.sonoMazziere = sonoMazziere;
        if (sonoMazziere) {
            if (InviaMazzo()) {

            }
        }
        start();
    }

    public boolean InviaMazzo() {
        if (sonoMazziere) {
            //genero
            mazzo.Randomizzo();
            try {
                //invio mazzo
                gestioneConnessione.Invia("m;" + mazzo, gestioneConnessione.GetAddress());
            } catch (SocketException ex) {
                Logger.getLogger(GestionePartita.class.getName()).log(Level.SEVERE, null, ex);
            }
            //controllo se mi è arrivata la ripsota dall'altra parte
            long time = System.currentTimeMillis();
            int timer = 10000;
            boolean provatoAMandare = false;
            while (!gestioneConnessione.flagMazzoArrivato) {
                if ((System.currentTimeMillis() - time) > timer) {
                    if (!provatoAMandare) {
                        try {
                            //invio mazzo
                            gestioneConnessione.Invia("m;" + mazzo, gestioneConnessione.GetAddress());
                            timer = 15000;
                            provatoAMandare = true;
                        } catch (SocketException ex) {
                            Logger.getLogger(GestionePartita.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (provatoAMandare) {
                        Util.ShowDialog("Richiesta mazzo scaduta.", "Errore");
                        return false;
                    }
                }
                //funzione che non fa niente
                // NON TOCCARE
                assert true;
                // NON TOCCARE
            }
            //inizia partita
            return true;
        } else return false;
    }

}
