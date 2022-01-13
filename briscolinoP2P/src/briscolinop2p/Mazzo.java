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

    private ArrayList<Carta> mazzo;
    private GestionePartita gestionePartita;

    public Mazzo() throws SocketException {
        mazzo = new ArrayList<Carta>(0);
        for (int i = 0; i < 10; i++) {
            mazzo.add(new Carta(i + 1, 'b'));
        }
        for (int i = 0; i < 10; i++) {
            mazzo.add(new Carta(i + 1, 'c'));
        }
        for (int i = 0; i < 10; i++) {
            mazzo.add(new Carta(i + 1, 's'));
        }
        for (int i = 0; i < 10; i++) {
            mazzo.add(new Carta(i + 1, 'd'));
        }
        this.gestionePartita = GestionePartita.getInstance();
    }

    public Mazzo(String[] vettoreCarte) throws SocketException {
        mazzo = new ArrayList<Carta>(0);
        if (vettoreCarte.length != 40) {
            System.out.println("Il mazzo non ha 40 carte.");
        }
        for (String string : vettoreCarte) {
            mazzo.add(Carta.creaCarta(string));
        }
        this.gestionePartita = GestionePartita.getInstance();
    }

    public ArrayList<Carta> GetMazzo() {
        return mazzo;
    }

    public Carta GetCarta(int index) {
        return mazzo.get(index);
    }

    public int GetSize() {
        return mazzo.size();
    }

    public Carta TogliCarta() {
        if (!gestionePartita.partitaFinita) {
            Carta carta = null;
            try {
                carta = mazzo.get(0);
                if (mazzo.remove(carta)) {
                    return carta;
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }

        }
        return null;
    }

    public void Randomizzo() {
        java.util.Collections.shuffle(mazzo);
    }

    public boolean vuoto() {
        if (mazzo.size() == 0) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        String finale = "";
        //controllare che il toString della carta sia in uqesto modo "1,f"
        for (Carta carta : mazzo) {
            finale += carta + ";";
        }
        return finale;
    }

}
