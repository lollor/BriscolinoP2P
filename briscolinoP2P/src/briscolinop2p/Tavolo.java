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

/**
 *
 * @author gerosa_simone
 */
public class Tavolo {

    static Tavolo istanza;
    private Mazzo mazzo;
    private Giocatore ioGiocatore;
    private Giocatore Sfidante;
    private Carta briscola;

    public Tavolo() {
        this.mazzo = null;
        this.ioGiocatore = null;
        this.Sfidante = null;
        this.briscola = null;
    }

    public static synchronized Tavolo getTavolo() throws SocketException {
        if (istanza == null) {
            istanza = new Tavolo();
        }
        return istanza;
    }
    
    public synchronized Carta GetCarta(){
        return mazzo.TogliCarta();
    }
    
    public void RandomizzoMazzo(){
        mazzo.Randomizzo();
    }
    
    public Mazzo GetMazzo(){
        return mazzo;
    }
    
    public Giocatore GetGiocatore(boolean io){
        if (io) return ioGiocatore;
        else return Sfidante;
    }

}
