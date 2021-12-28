/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lorenzo
 */
public class GestioneInvio {
    
    private static DatagramSocket socketInvio;

    public static void Invia(String data, InetAddress address) throws SocketException {
        if (socketInvio == null){
            socketInvio = new DatagramSocket();
        }
        new Thread(() -> {
            byte[] bufRisposta = data.getBytes();
            try {
                socketInvio.send(new DatagramPacket(bufRisposta, bufRisposta.length, address, 12345));
            } catch (IOException ex) {
                Logger.getLogger(GestioneInvio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
}
