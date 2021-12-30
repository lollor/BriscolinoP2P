/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

import briscolinop2p.Carta;
import static briscolinop2p.GestionePartita.istanza;
import briscolinop2p.Giocatore;
import java.net.SocketException;
import java.util.ArrayList;

/**
 *
 * @author gerosa_simone
 */
public class Tavolo {
    static Tavolo istanza;
    Mazzo mazzo;
    Giocatore mazziere;
    Giocatore Sfidante;

    public Tavolo() {
        this.mazzo = null;
        this.mazziere = null;
        this.Sfidante = null;
    }
    
    
      public static synchronized Tavolo getTavolo() throws SocketException {
        if (istanza == null) {
            istanza = new Tavolo();
        }
        return istanza;
    }

}
