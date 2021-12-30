/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

//import static briscolinop2p.GestionePartita.istanza;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import jdk.jshell.spi.ExecutionControl;

/**
 *
 * @author Lorenzo
 */
public class GestioneConnessione extends Thread {

    private static GestioneConnessione istanza = null;
    private DatagramSocket socketRicezione;
    private DatagramSocket socketInvio;
    private InetAddress ipAddress;
    boolean connesso;
    private int faseConnessione;
    private GestionePartita gestionePartita;
    //0 - nessuno ha iniziato la connessione
    //1 - la connessione è stata iniziata da una delle due parti ed è arrivato/stato inviato il pacchetto "a;"
    //2 - la connessione è stata accettata 

    //tutte le flag
    boolean flagMazzoArrivato = false;

    public static synchronized GestioneConnessione getConnessione() throws SocketException {
        if (istanza == null) {
            istanza = new GestioneConnessione();
        }
        return istanza;
    }

    public GestioneConnessione() throws SocketException {
        istanza = this;
        this.socketRicezione = new DatagramSocket(12345);
        this.socketInvio = new DatagramSocket();
        this.connesso = false;
        this.faseConnessione = 0;
        this.gestionePartita = GestionePartita.getPartita();
    }

    @Override
    public void run() {
        while (true) {
            byte[] buf = new byte[1500];
            DatagramPacket p = new DatagramPacket(buf, buf.length);
            try {
                socketRicezione.receive(p);
            } catch (IOException ex) {
                System.out.println("Eccezione ricevimento pacchetto nella run della classe GestioneConnessione.\nErrore: " + ex.getLocalizedMessage());
            }
            new Thread(() -> {
                try {
                    gestisciPaccketto(new String(p.getData()).trim(), p.getAddress());
                } catch (SocketException ex) {
                    Logger.getLogger(GestioneConnessione.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();
        }
    }

    private void gestisciPaccketto(String data, InetAddress address) throws SocketException {
        System.out.println("IN [" + data + "] [" + address + "]");
        char tipoRichiesta = data.charAt(0);
        String resto = data.substring(data.indexOf(";") + 1);
        switch (tipoRichiesta) {
            case 'a':
                //apertura connessione
                if (faseConnessione == 0 && !connesso) {
                    if (JOptionPane.showConfirmDialog(null, resto.split(";")[0] + " (" + address + ") ha chiesto di connettersi. Accettare?", "Richiesta", JOptionPane.YES_NO_OPTION) == 0) {
                        String mioNome = JOptionPane.showInputDialog("Inserire nome");
                        nomeMittente = resto.split(";")[0].trim();
                        ipAddress = address;
                        Invia("y;" + mioNome + ";", ipAddress);
                        faseConnessione = 1;
                    } else {
                        Invia("n;", address);
                    }
                } else {
                    Invia("n;", address);
                }
                break;
            case 'y':
                if (faseConnessione == 1 && !connesso) {
                    if (address.equals(ipAddress)) {
                        if (resto.equals("")) {
                            //arrivato pacchetto dopo che io ho inviato "y;mioNome;"
                            faseConnessione = 2;
                            connesso = true;
                            gestionePartita.IniziaPartita(false);
                        } else if (!resto.equals("")) {
                            //arrivato pacchetto "y;suoNome;"
                            nomeMittente = resto.split(";")[0].trim();
                            faseConnessione = 2;
                        }
                        //impostare nome giocatore
                    } else {
                        //è arrivato un pacchetto y; da un client diverso
                        //rimane uguale e questo server aspetta comunque il pacchetto y; dal client precedente
                    }
                }
                break;
            case 'n':
                if (faseConnessione == 1 && !connesso) {
                    if (address.equals(ipAddress)) {
                        faseConnessione = 0;
                        connesso = false;
                        //si riparte da capo con la connessione
                    }
                }
                break;
            case 'm':
                //gestire anche flagMazzoArrivato
                break;
            case 'b':
                break;
            case 'p':
                break;
            case 'f':
                break;

            default:
                throw new AssertionError();
        }
    }
    private String nomeMittente;

    public boolean IniziaConnessione(InetAddress address, String nome) throws SocketException {
        //throw new UnsupportedOperationException("Not supported yet.");
        Invia("a;" + nome + ";", address);
        faseConnessione = 1;
        //timer di attesa della risposta, 20 secondi
        long time = System.currentTimeMillis();
        while (faseConnessione == 1) {
            if ((System.currentTimeMillis() - time) > 20000 && !connesso && faseConnessione == 1) {
                connesso = false;
                faseConnessione = 0;
                nomeMittente = "";
                Util.ShowDialog("Richiesta scaduta.", "Errore");
                return false;
            }
            //funzione che non fa niente
            // NON TOCCARE
            assert true;
            // NON TOCCARE
        }
        //se arriva il messaggio ma ho gia creato una connessione la rifiuto e ritorno false
        if (faseConnessione != 2 || connesso) {
            return false;
        }
        Invia("y;", address);
        connesso = true;
        gestionePartita.IniziaPartita(true);
        
        Util.ShowDialog("Sono connesso a " + nomeMittente, "Connessione effettuata.");
        return true;
    }

    public void Invia(String data, InetAddress address) throws SocketException {
        System.out.println("OUT [" + data + "] [" + address + "]");
        new Thread(() -> {
            byte[] bufRisposta = data.getBytes();
            try {
                socketInvio.send(new DatagramPacket(bufRisposta, bufRisposta.length, address, 12345));
            } catch (IOException ex) {
                Logger.getLogger(GestioneConnessione.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    public InetAddress GetAddress() {
        return ipAddress;
    }
}
