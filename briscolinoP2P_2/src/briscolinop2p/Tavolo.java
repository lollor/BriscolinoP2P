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

    public Tavolo() throws SocketException {
        this.mazzo = new Mazzo();
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

    public Carta getBriscola() {
        return briscola;
    }
    public Carta getBriscolaVisiva(){
        if (mazzo.vuoto() || mazzo == null)
            return null;
        return briscola;
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
        briscola = mazzo.GetCarta(mazzo.GetSize() - 1);
    }

    public Mazzo GetMazzo() {
        return mazzo;
    }

    public Giocatore GetGiocatore(boolean io) {
        if (io) {
            if (ioGiocatore == null) ioGiocatore = new Giocatore();
            return ioGiocatore;
        } else {
            if (Sfidante == null) Sfidante = new Giocatore();
            return Sfidante;
        }
    }

    public void SetMazzo(Mazzo mazzo){
        this.mazzo = mazzo;
        briscola = mazzo.GetCarta(mazzo.GetSize() - 1);
        System.out.println("Settato mazzo");
    }
    
    public boolean AggiungiCartaSulTavolo(Carta carta) {
        if (carteMostrate.size() == 0 || carteMostrate.size() == 1) {
            carteMostrate.add(carta);
            return true;
        }
        return false;
    }

    public void PulisciCarteTavolo() {
        carteMostrate = new ArrayList<Carta>(0);
    }
    public ArrayList<Carta> GetCarteMostrate(){
        return carteMostrate;
    }

    public String CalcoloChiHaVintoMano() throws InterruptedException {
        //-------------------------------------
        //LA CARTA 0 E' LA SUA E LA 1 E' LA MIA
        //-------------------------------------
        Carta carta0 = carteMostrate.get(0), carta1 = carteMostrate.get(1);
        PulisciCarteTavolo();
        //se la carta che ha buttato lui è la briscola e la mia no
        if (briscola.getSeme() == carta0.getSeme() && briscola.getSeme() != carta1.getSeme()) {
            return "w;";
        } 
        //se la carta che lui ha buttato non è la briscola e la mia si
        else if (briscola.getSeme() != carta0.getSeme() && briscola.getSeme() == carta1.getSeme()) {
            return "l;";
        } 
        //se entrambi abbiamo la briscola
        else if (briscola.getSeme() == carta0.getSeme() && briscola.getSeme() == carta1.getSeme()) {
            if (carta0.getPunti() > carta1.getPunti()) {
                return "w;";
            } else if (carta0.getPunti() < carta1.getPunti()) {
                return "l;";
            } else {
                if (carta0.getNumero() > carta1.getNumero()) {
                    return "w;";
                } else {
                    return "l;";
                }
            }
        } 
        //se entrambi abbiamo delle non briscole
        else if (carta0.getSeme() == carta1.getSeme() && briscola.getSeme() != carta0.getSeme()) {
            if (carta0.getPunti() > carta1.getPunti()) {
                return "w;";
            } else if (carta0.getPunti() < carta1.getPunti()) {
                return "l;";
            } else {
                if (carta0.getNumero() > carta1.getNumero()) {
                    return "w;";
                } else {
                    return "l;";
                }
            }
        } else if (carta0.getSeme() != carta1.getSeme() && briscola.getSeme() != carta0.getSeme() && briscola.getSeme() != carta1.getSeme()){
            return "w;";
        }
        
        return null;
    }
}
