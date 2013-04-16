package lib.ardrone.network;

import java.io.IOException;
import java.net.*;
import lib.ardrone.ARDroneEntity;

import lib.ardrone.Global;

/**
 * Design Pattern Singleton to insure only one SocketManager in the project!
 *
 * @author hoelter
 *
 */
public class SocketManagerVideo {

    private DatagramSocket socketUnicast = null;
    private MulticastSocket socketMulticast = null;
    private static SocketManagerVideo instance;
    private ARDroneEntity drone = null;

    private SocketManagerVideo(ARDroneEntity drone) {
        this.drone = drone;
        if (!drone.hasMulticast()) {
            try {
                System.out.println("Video Socket is Unicast");
                if (socketMulticast != null) {
                    socketMulticast.close();
                }
                socketUnicast = new DatagramSocket(Global.VIDEO_PORT);
                socketUnicast.setSoTimeout(3000);
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                System.out.println("Video Socket is Multicast");
                if (socketUnicast != null) {
                    socketUnicast.close();
                }
                socketMulticast = new MulticastSocket(Global.VIDEO_PORT);
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

    public static SocketManagerVideo getInstance(ARDroneEntity drone) {
        if (null == instance) { // First call
            instance = new SocketManagerVideo(drone);
        }
        /*
         * if (drone.hasMulticast()) { instance = new SocketManagerVideo(drone);
        }
         */
        return instance;
    }

    /**
     * Video Method
     *
     * @throws IOException
     */
    public synchronized void videoSocketReceivePacket(DatagramPacket packet)
            throws IOException {
        if (!drone.hasMulticast()) {
            socketUnicast.receive(packet);
        } else {
            socketMulticast.receive(packet);
        }
    }

    /**
     * Video Method
     */
    public synchronized void videoSocketSendPacket(DatagramPacket packet) {
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
