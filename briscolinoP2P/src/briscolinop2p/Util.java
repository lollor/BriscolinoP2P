/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

import javax.swing.JOptionPane;

/**
 *
 * @author Lorenzo
 */
public class Util {
    public static void ShowDialog(String messaggio, String titolo){
        new Thread(()->{JOptionPane.showConfirmDialog(null, messaggio, titolo, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);}).start();
    }
}
