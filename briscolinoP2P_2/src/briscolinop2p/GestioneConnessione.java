/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package briscolinop2p;

//import static briscolinop2p.GestionePartita.istanza;
import java.awt.Color;
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

    private final int THISPORT = 12345;
    private final int OTHERPORT = 12346;

    private static GestioneConnessione istanza = null;
    private DatagramSocket socketRicezione;
    private DatagramSocket socketInvio;
    private InetAddress ipAddress;
    boolean connesso;
    private volatile int faseConnessione;
    private GestionePartita gestionePartita;
    private String nomeMittente, mioNome;
    //0 - nessuno ha iniziato la connessione
    //1 - la connessione è stata iniziata da una delle due parti ed è arrivato/stato inviato il pacchetto "a;"
    //2 - la connessione è stata accettata 

    //tutte le flag
    volatile boolean flagMazzoArrivato = false;
    volatile boolean flagAltroHaPescato = false;
    volatile Carta flagCartaButtataDallAltro = null;
    volatile Boolean flagAltroHaDatoPunteggio = null;
    volatile boolean flagAltroHaMandatoMazzo = false;

    public static synchronized GestioneConnessione getInstance() throws SocketException {
        if (istanza == null) {
            istanza = new GestioneConnessione();
        }
        return istanza;
    }

    public GestioneConnessione() throws SocketException {
        istanza = this;
        this.socketRicezione = new DatagramSocket(THISPORT);
        this.socketInvio = new DatagramSocket();
        this.connesso = false;
        this.faseConnessione = 0;
        this.gestionePartita = GestionePartita.getInstance();
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
                } catch (IOException ex) {
                    Logger.getLogger(GestioneConnessione.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GestioneConnessione.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();
        }
    }

    private void gestisciPaccketto(String data, InetAddress address) throws SocketException, IOException, InterruptedException {
        System.out.println("IN [" + data + "] [" + address + "]");
        char tipoRichiesta = data.charAt(0);
        String resto = data.substring(data.indexOf(";") + 1);
        switch (tipoRichiesta) {
            case 'a':
                //apertura connessione
                if (faseConnessione == 0 && !connesso) {
                    if (JOptionPane.showConfirmDialog(null, resto.split(";")[0] + " (" + address + ") ha chiesto di connettersi. Accettare?", "Richiesta", JOptionPane.YES_NO_OPTION) == 0) {
                        mioNome = JOptionPane.showInputDialog("Inserire nome");
                        if (mioNome.equals("")) {
                            mioNome = "default";
                        }
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
                    if (resto.equals("")) {
                        //arrivato pacchetto "y;" dopo che io ho inviato "y;mioNome;"
                        faseConnessione = 2;
                        //System.out.println("Ho cambiato fase connessione a 2");
                        ipAddress = address;
                        connesso = true;
                        gestionePartita.SetNomiGiocatori(mioNome, nomeMittente);
                        JFrame f = new JFrame();
                        Connessione.GetInstance().setVisible(false);
                        f.start(null);
                        //gestionePartita.IniziaPartita(true);
                    } else if (!resto.equals("")) {
                        //arrivato pacchetto "y;suoNome;"
                        ipAddress = address;
                        nomeMittente = resto.split(";")[0].trim();
                        faseConnessione = 2;
                        gestionePartita.SetNomiGiocatori(mioNome, nomeMittente);
                    } //impostare nome giocatore
                    else {
                        //è arrivato un pacchetto y; da un client diverso
                        //rimane uguale e questo server aspetta comunque il pacchetto y; dal client precedente
                        //gestionePartita.SetNomiGiocatori(mioNome, nomeMittente);
                        //gestionePartita.IniziaPartita(false);
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
                //salva mazzo
                if (resto.split(";")[0].equals("y")) {
                    flagMazzoArrivato = true;
                    //gestionePartita.IniziaPartita(true);
                } else {
                    gestionePartita.tavolo.SetMazzo(new Mazzo(resto.split(";")));
                    Invia("m;y;", address);
                    flagAltroHaMandatoMazzo = true;
                    gestionePartita.IniziaPartita(false);
                }
                break;
            case 'b':
                Carta carta = Carta.creaCarta(resto.split(";")[0].trim());
                gestionePartita.cartaButtata = carta;
                if (gestionePartita.tavolo.AggiungiCartaSulTavolo(carta)) {
                    flagCartaButtataDallAltro = carta;
                    sleep(200);
                    flagCartaButtataDallAltro = null;
                } else {
                    System.out.println("Errore nell'aggiungere la carta sul tavolo nel case 'b' della funzione GestionePacchetto della classe GestioneConnessione");
                }
                break;
            case 'p':
                if (!gestionePartita.tavolo.GetMazzo().vuoto())
                    gestionePartita.tavolo.GetCarta(false);
                flagAltroHaPescato = true;
                break;
            case 'l':
                JFrame.getInstance().SetMessaggio("Hai perso!", Color.red);
                sleep(200);
                gestionePartita.tavolo.PulisciCarteTavolo();
                flagAltroHaDatoPunteggio = false;
                gestionePartita.turnoMio = false;

                break;
            case 'w':
                JFrame.getInstance().SetMessaggio("Hai vinto!", Color.green);
                sleep(200);
                gestionePartita.tavolo.PulisciCarteTavolo();
                flagAltroHaDatoPunteggio = true;
                gestionePartita.turnoMio = true;

                break;
            default:
                throw new AssertionError();
        }
    }

    public boolean IniziaConnessione(InetAddress address, String nome) throws SocketException {
        Invia("a;" + nome + ";", address);
        faseConnessione = 1;
        //timer di attesa della risposta, 20 secondi
        long time = System.currentTimeMillis();
        while (faseConnessione == 1) {
            if (faseConnessione == 2) {
                break;
            }
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
        if (faseConnessione != 2) {
            return false;
        }
        Invia("y;", address);
        ipAddress = address;
        connesso = true;
        Util.ShowDialog("Sono connesso a " + nomeMittente, "Connessione effettuata.");
        new Thread(() -> {
            try {
                gestionePartita.IniziaPartita(true);
            } catch (InterruptedException ex) {
                Logger.getLogger(GestioneConnessione.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SocketException ex) {
                Logger.getLogger(GestioneConnessione.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
        return true;
    }

    public void Invia(String data, InetAddress address) throws SocketException {
        new Thread(() -> {
            byte[] bufRisposta = data.getBytes();
            try {
                socketInvio.send(new DatagramPacket(bufRisposta, bufRisposta.length, address, OTHERPORT));
                System.out.println("OUT [" + data + "] [" + address + "]");
            } catch (IOException ex) {
                System.out.println("Errore nella send");
            }
        }).start();
    }

    public void ChiudiConnessione(){
        this.connesso = false;
        this.faseConnessione = 0;
        ipAddress = null;
    }
    
    public InetAddress GetAddress() {
        return ipAddress;
    }

    public GestionePartita GetPartita() {
        return gestionePartita;
    }
}
