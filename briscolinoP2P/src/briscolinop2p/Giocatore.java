/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

import java.util.ArrayList;

/**
 *
 * @author gerosa_simone
 */
public class Giocatore {

    String nome;
    String punteggio;
    ArrayList<Carta> mano;//tre carte che ha in mano il giocatore

    public Giocatore(String nome) {
        this.nome = nome;
        punteggio = "";
        mano = new ArrayList<Carta>(0);
    }

    public boolean AggiungiCartaAllaMano(Carta carta) {
        if (mano.size() < 3) {
            mano.add(carta);
            return true;
        } else {
            return false;
        }
    }
}
