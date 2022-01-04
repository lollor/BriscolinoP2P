/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

import briscolinop2p.Carta;
//import static briscolinop2p.GestionePartita.istanza;
import briscolinop2p.Giocatore;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerosa_simone
 */
public class Tavolo {

    static Tavolo istanza;
    private Mazzo mazzo;
    private Giocatore ioGiocatore, Sfidante;
    private Carta briscola;
    private ArrayList<Carta> carteMostrate;
    
    public Tavolo() {
        this.mazzo = null;
        this.ioGiocatore = null;
        this.Sfidante = null;
        this.briscola = null;
        carteMostrate = new ArrayList<Carta>(0);
    }

    public static synchronized Tavolo getTavolo() throws SocketException {
        if (istanza == null) {
            istanza = new Tavolo();
        }
        return istanza;
    }

    public synchronized Carta GetCarta(boolean inviareMessaggio) {
        Carta carta = mazzo.TogliCarta();
        GestioneConnessione g;
        if (inviareMessaggio) {
            try {
                g = GestioneConnessione.getInstance();
                g.Invia("p;", g.GetAddress());
            } catch (SocketException ex) {
                System.out.println("Errore nell'invio di p; nella funzione GetCarta della classe Tavolo");
            }
        }
        return carta;
    }

    public void RandomizzoMazzo() {
        mazzo.Randomizzo();
        briscola = mazzo.GetCarta(mazzo.GetSize()-1);
    }

    public Mazzo GetMazzo() {
        return mazzo;
    }

    public Giocatore GetGiocatore(boolean io) {
        if (io) {
            return ioGiocatore;
        } else {
            return Sfidante;
        }
    }

    public boolean AggiungiCartaSulTavolo(Carta carta){
        if (carteMostrate.size() == 0 || carteMostrate.size() == 1){
            carteMostrate.add(carta);
            return true;
        } return false;
    }
    
    public void PulisciCarteTavolo(){
        carteMostrate = new ArrayList<Carta>(0);
    }
    
    public String CalcoloChiHaVintoMano(){
        if (briscola.getSeme() == )
    }
}
