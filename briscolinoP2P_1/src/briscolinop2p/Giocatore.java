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
    int punteggio;
    ArrayList<Carta> mano;//tre carte che ha in mano il giocatore

    public Giocatore() {
        this.nome = "";
        punteggio = 0;
        mano = new ArrayList<Carta>(0);
    }

    public boolean AggiungiCartaAllaMano(Carta carta) {
        if (mano.size() < 3) {
            if (mano.add(carta)) {
                return true;
            }
        }
        return false;
    }

    public void AggiungiPunti(int puntiDaAggiungere){
        punteggio += puntiDaAggiungere;
    }
    
    public boolean TogliCartaDallaMano(Carta carta) {
        if (mano.size() > 0) {
            if (mano.remove(carta)) {
                return true;
            }
        }
        return false;
    }
    
    public String GetPunteggio(){
        return String.valueOf(punteggio);
    }
}
