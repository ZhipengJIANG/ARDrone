package lib.ardrone.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import lib.ardrone.ARDroneEntity;
import java.net.MulticastSocket; //multicast
import java.net.InetAddress;   //multicast
import java.net.NetworkInterface;
import lib.ardrone.Global;
import java.util.*;

/**
 * Design Pattern Singleton to insure only one SocketManager in the project!
 *
 * @author hoelter
 *
 */
public class SocketManagerNavData {

    private DatagramSocket socketUnicast = null;
    private MulticastSocket socketMulticast = null;
    private static SocketManagerNavData instance;
    private ARDroneEntity drone = null;

    private SocketManagerNavData(ARDroneEntity drone) {
        this.drone = drone;
        if (!drone.hasMulticast()) {
            try {
                System.out.println("Navdata Socket is Unicast");
                if (socketMulticast != null) {
                    socketMulticast.close();
                }

                socketUnicast = new DatagramSocket(Global.NAVDATA_PORT);

                socketUnicast.setSoTimeout(3000);
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                System.out.println("Navdata Socket is Multicast");
                if (socketUnicast != null) {
                    socketUnicast.close();
                }
                socketMulticast = new MulticastSocket(Global.NAVDATA_PORT);
                InetAddress multiGroup = InetAddress.getByName(Global.MULTICAST_GROUP);
                socketMulticast.setInterface(drone.getMultiInterfAdress());
                socketMulticast.joinGroup(multiGroup);
                socketMulticast.setSoTimeout(3000);


            } catch (IOException ie) {
                // TODO Auto-generated catch block
                ie.printStackTrace();
            }

        }
    }

    public static SocketManagerNavData getInstance(ARDroneEntity drone) {
        if (null == instance) { // First call
            instance = new SocketManagerNavData(drone);
        }

        return instance;
    }

    /**
     * NavData Method
     *
     * @throws IOException
     */
    public synchronized void navdataSocketReceivePacket(DatagramPacket packet)
            throws IOException {
        if (!drone.hasMulticast()) {
            socketUnicast.receive(packet);
        } else {
            socketMulticast.receive(packet);
        }
    }

    /**
     * NavData Method
     */
    public synchronized void navdataSocketSendPacket(DatagramPacket packet) {
        if (!drone.hasMulticast()) {
            try {

                socketUnicast.send(packet);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                socketMulticast.send(packet);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
