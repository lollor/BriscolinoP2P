/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

/**
 *
 * @author gerosa_simone
 */
public class Carta {

    private int numero;
    private char seme;
    private String img;
    private int punti;

    
    public Carta(int num, char seme) {
        this.numero = num;
        this.seme = seme;
        String path = num + seme + ".gif";
        this.img = "../../img_carte/" + path;
        if(numero==1)
            punti=11;
        else if(numero==3)
            punti=10;
        else if(numero==8)
            punti=2;
        else if(numero==9)
            punti=3;
        else if(numero==10)
            punti=4;
        else 
            punti=0;
                    
    }
    
    public Carta creaCarta(String s){
        String[] dati=s.split(",");
        Carta c=new Carta(Integer.parseInt(dati[0]),dati[1].charAt(0));
        return c;
    }
    
    
}
