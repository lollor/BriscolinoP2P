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

    Giocatore giocatoreLocale, giocatoreEsterno;
    private static GestionePartita istanza = null;
    //private Mazzo mazzo;
    private GestioneConnessione gestioneConnessione;
    boolean sonoMazziere;
    boolean partitaFinita;
    boolean turnoMio;
    public Tavolo tavolo;

    public GestionePartita() throws SocketException {
        istanza = this;
        this.partitaFinita = false;
        this.sonoMazziere = false;
        this.gestioneConnessione = GestioneConnessione.getInstance();
        this.tavolo = new Tavolo();
        giocatoreLocale = tavolo.GetGiocatore(true);
        giocatoreEsterno = tavolo.GetGiocatore(false);
    }

    public static synchronized GestionePartita getInstance() throws SocketException {
        if (istanza == null) {
            istanza = new GestionePartita();
        }
        return istanza;
    }

    boolean primaMano = true;

    @Override
    public void run() {
        while (true) {
            while (!partitaFinita) {
                if (turnoMio) {
                    if (primaMano) {
                        while (JFrame.CartaSelezionata == null) {
                            assert true;
                        }
                        Carta cartaSelezionata = JFrame.CartaSelezionata;
                        if (giocatoreLocale.TogliCartaDallaMano(cartaSelezionata)){
                            tavolo.AggiungiCartaSulTavolo(cartaSelezionata);
                            try {
                                gestioneConnessione.Invia("b;"+cartaSelezionata+";", gestioneConnessione.GetAddress());
                            } catch (SocketException ex) {
                                System.out.println("Errore nell'invia della run della classe GestionePartita");
                            }
                            //mossa avversario
                            while (!gestioneConnessione.flagAltroHaDatoPunteggio) assert true;
                        } else {
                            System.out.println("Errore nel togliere la carta dalla mano nella run della classe GestionePartita");
                        }
                    }
                }
            }
        }
    }

    public void IniziaPartita(boolean sonoMazziere) {
        this.sonoMazziere = sonoMazziere;
        if (sonoMazziere) {
            if (!InviaMazzo()) {
                return;
            }
            for (int i = 0; i < 3; i++) {
                giocatoreLocale.AggiungiCartaAllaMano(tavolo.GetCarta(true));
                while (!gestioneConnessione.flagAltroHaPescato) {
                    assert true;
                }
                gestioneConnessione.flagAltroHaPescato = false;
            }
            this.turnoMio = true;
        } else {
            while (!gestioneConnessione.flagAltroHaMandatoMazzo) assert true;
            for (int i = 0; i < 3; i++) {
                while (!gestioneConnessione.flagAltroHaPescato) {
                    assert true;
                }
                giocatoreLocale.AggiungiCartaAllaMano(tavolo.GetCarta(true));
                gestioneConnessione.flagAltroHaPescato = false;
            }
            this.turnoMio = false;
        }
        start();
    }

    public boolean InviaMazzo() {
        if (sonoMazziere) {
            //genero
            tavolo.RandomizzoMazzo();
            try {
                //invio mazzo
                gestioneConnessione.Invia("m;" + tavolo.GetMazzo(), gestioneConnessione.GetAddress());
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
                            gestioneConnessione.Invia("m;" + tavolo.GetMazzo(), gestioneConnessione.GetAddress());
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
        } else {
            return false;
        }
    }

    
}
