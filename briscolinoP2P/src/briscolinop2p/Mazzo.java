/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

import java.net.SocketException;
import java.util.ArrayList;

/**
 *
 * @author Lorenzo
 */
public class Mazzo {
    public ArrayList<Carta> mazzo;
    private GestionePartita gestionePartita;
    
    public Mazzo() throws SocketException {
        this.gestionePartita = GestionePartita.getPartita();
    }
    
    public void TogliCarta(){
        if (!gestionePartita.partitaFinita)
            mazzo.remove(0);
    }

    public void Randomizzo() {
        java.util.Collections.shuffle(mazzo);
    }
    
    public boolean vuoto(){
        if(mazzo.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String finale="";
        //controllare che il toString della carta sia in uqesto modo "1,f"
        for (Carta carta : mazzo) {
            finale+=carta+";";
        }
        return finale;
    }
    
}
