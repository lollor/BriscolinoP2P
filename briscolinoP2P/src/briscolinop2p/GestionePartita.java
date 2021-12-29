/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

import java.net.SocketException;
import javax.swing.JOptionPane;

/**
 *
 * @author Lorenzo
 */
public class GestionePartita extends Thread {

    //Giocatore giocatoreLocale, giocatoreEsterno;
    private Mazzo mazzo;
    private GestioneConnessione gestioneConnessione;
    boolean sonoMazziere;
    boolean partitaFinita;

    public GestionePartita(GestioneConnessione gestioneConnessione) {
        this.partitaFinita = false;
        this.sonoMazziere = false;
        this.gestioneConnessione = gestioneConnessione;
    }

    @Override
    public void run() {
        while (true) {
            while (!partitaFinita) {

            }
        }
    }

    public void InviaMazzo() throws SocketException {
        if (sonoMazziere) {
            //genero
            mazzo.Randomizzo();
            //invio mazzo
            gestioneConnessione.Invia("m;" + mazzo, gestioneConnessione.GetAddress());
            //controllo se mi è arrivata la ripsota dall'altra parte
            long time = System.currentTimeMillis();
            while (!gestioneConnessione.flagMazzoArrivato) {
                if ((System.currentTimeMillis() - time) > 10000) {
                    //cercare di capire cosa fare
                    JOptionPane.showConfirmDialog(null, "Richiesta mazzo scaduta.", "Errore", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                }
                //funzione che non fa niente
                // NON TOCCARE
                assert true;
                // NON TOCCARE
            }
            //inizia partita
        }
    }

}
