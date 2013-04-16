/**
 * @author Jeremie Hoelter
 */
package gui_client;

import controlP5.ControlP5;
import napplet.NAppletManager;
import processing.core.PApplet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Yannick Presse
 */
public class ARDroneDemo extends PApplet {

    private static final long serialVersionUID = 1L;
    ControlP5 controlP5;
    NAppletManager nappletManager;

    
    /**
     * Setup the characteristic of the window
     */
    public void setup() {
        size(150,200);
        background(0);
        controlP5 = new ControlP5(this);

        nappletManager = new NAppletManager(this);

        controlP5.addButton("NewDrone", 0, ((height / 2) - 50), (width / 2), 50, 19);
        
//         try {
//            ServerSocket ss = new ServerSocket(1800);
//            Socket s = ss.accept();
//            System.out.println("Client Accepted");
//            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//            System.out.println(br.readLine());
//            PrintWriter wr = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
//            wr.println("Welcome to Socket Programming");
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        
//        System.out.println("test");
//        controlP5.addTextfield("asdf", 0, 0, 50, 30);

    }

    /**
     * Draw the window
     */
    public void draw() {
    }

    /**
     * Create two Napplet and launch the applet UserNetworkConfig when clicking
     * on NewDrone
     */
    public void NewDrone() {
        
        gui_client.ARDroneUserInterface guiApplet = (gui_client.ARDroneUserInterface)
                (nappletManager.createWindowedNApplet("gui_client.ARDroneUserInterface", 500, 300));
        
        gui_client.UserNetworkConfig configApplet = (gui_client.UserNetworkConfig)
                (nappletManager.createWindowedNApplet("gui_client.UserNetworkConfig", 445, 440));
        configApplet.config(guiApplet);
        
    }

    /**
     * This is main
     *
     * @param args
     */
    static public void main(String args[]) {
        PApplet.main(new String[]{"--bgcolor=#000000", "gui_client.ARDroneDemo"});
    }
}
