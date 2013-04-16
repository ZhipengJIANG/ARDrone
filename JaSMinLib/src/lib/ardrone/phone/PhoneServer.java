package lib.ardrone.phone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.net.ServerSocket;
import java.net.Socket;

import lib.ardrone.listeners.AttitudeListener;
import lib.ardrone.listeners.PhoneDataListener;
import lib.ardrone.navigationdata.structures.PhoneData;
import lib.ardrone.Global;

;

public class PhoneServer extends Thread {

    /**
     * TODO to restructure: global variables, listener...
     */
    private PhoneData data = null;
    private boolean kill = false;
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private PrintWriter output = null;
    private BufferedReader input = null;
    private PhoneDataListener phoneDataListener = null;

    /*
     * private static String latitude; private static String longitude; private
     * static String altitude;
     */
    //static String[] params = { "latitude", "longitude", "altitude" };
    public PhoneServer() {
        data = new PhoneData();

        phoneDataListener = new PhoneDataListener() {

            @Override
            public void phoneDataChanged(String latitude, String longitude, String altitude) {
                data.setLatitude(latitude);
                data.setLongitude(longitude);
                data.setAltitude(altitude);
            }
        };
    }

    public void init() {
        try {
            serverSocket = new ServerSocket(Global.PHONE_PORT);
            System.out.println("#PhoneServer : Server ready, port " + Global.PHONE_PORT);
            socket = serverSocket.accept();

            // Un BufferedReader permet de lire par ligne.
            input = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            // Un PrintWriter possède toutes les opérations print classiques.
            // En mode auto-flush, le tampon est vidé (flush) à l'appel de
            // println.
            output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void run() {
        init();
        String str;
        while (!kill) {
            try {
                str = input.readLine();
                if (str.charAt(0) == '+') {
                    recuperationParams(str);
                    /**
                     * TODO a ameliorer quand on aura changer la structure des
                     * retours du clients (recuperationParams...)
                     */
                    if (phoneDataListener != null) {
                        phoneDataListener.phoneDataChanged(data.getLatitude(), data.getLongitude(), data.getAltitude());
                    }
                    afficherParams();
                } else {
                    System.out.println(str); // trace locale
                    if (str.equals("#Client : Quit button hit")) {
                        kill = true;
                        System.out.println("#Server : Connection will close");
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // lecture du message

        }
        kill();
    }

    public void kill() {
        //if(!kill){
        this.kill = true;
        try {
            input.close();
            output.close();
            socket.close();
            System.out.println("#Server : End of service");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //} //otherwise this is not a manual thread kill, but killed by the client...


    }

    public void recuperationParams(String s) {
        String sbis = s.substring(1);
        int compteur = 0;
        String temp = "";
        int i = 0;
        /**
         * TODO a revoir a Baptiste pour l'ordre des donnees envoyees... peut
         * etre revoir le client pour qu'il indique clairement quelle donnee il
         * envoie...
         */
        if (data != null) {
            while ((compteur != 3) && (i < sbis.length())) {
                if (sbis.charAt(i) == ';') {
                    switch (compteur) {
                        case 0:
                            data.setLatitude(temp);
                            break;
                        case 1:
                            data.setLongitude(temp);
                            break;
                    }
                    //params[compteur] = temp;
                    temp = "";
                    compteur = compteur + 1;
                } else {
                    temp = temp + sbis.charAt(i);
                }
                i++;
            }

            data.setAltitude(temp);
            //params[2] = temp;
        }
        /*
         * while ((compteur != 3) && (i < sbis.length())) { if (sbis.charAt(i)
         * == ';') { params[compteur] = temp; temp = ""; compteur = compteur +
         * 1; } else { temp = temp + sbis.charAt(i); } i++; } params[2] = temp;
         */
    }

    public void afficherParams() {
        System.out.println("Latitude : " + this.getPhoneData().getLatitude() + " ; Longitude : "
                + this.getPhoneData().getLongitude() + " ; Altitude : " + this.getPhoneData().getAltitude());
    }

    public PhoneData getPhoneData() {
        if (data != null) {
            return data;
        } else {
            return null;
        }
    }
}
