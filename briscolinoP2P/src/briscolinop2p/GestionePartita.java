/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

import java.net.SocketException;

/**
 *
 * @author Lorenzo
 */
public class GestionePartita extends Thread{
    //Giocatore giocatoreLocale, giocatoreEsterno;
    private Mazzo mazzo;
    private GestioneConnessione gestioneConnessione;
    boolean sonoMazziere;
    boolean partitaFinita;
    public GestionePartita(GestioneConnessione gestioneConnessione) {
        this.gestioneConnessione = gestioneConnessione;
    }

    @Override
    public void run() {
        while (true){
            while (!partitaFinita){
                
            }
        }
    }
    
    public void InviaMazzo() throws SocketException{
        if (sonoMazziere){
            //genero
            mazzo.Randomizzo();
            //invio mazzo
            gestioneConnessione.Invia("m;"+mazzo, gestioneConnessione.GetAddress());
        }
    }
    
}
