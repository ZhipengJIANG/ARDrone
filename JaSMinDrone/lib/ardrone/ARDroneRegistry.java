/**
 * @author Jeremie Hoelter
 */
package lib.ardrone;

import java.util.ArrayList;
import java.util.Iterator;

public class ARDroneRegistry {

    private static ARDroneRegistry instance = null;
    public ArrayList<ARDroneEntity> drones;

    private ARDroneRegistry() {
        drones = new ArrayList<ARDroneEntity>();
    }

    public static ARDroneRegistry getSingleton() {
        if (instance == null) {
            instance = new ARDroneRegistry();
        }
        return instance;
    }

    public void addDrone(ARDroneEntity drone) {
        drones.add(drone);
        System.out.println("Drone " + drone.getId() + " has been added to Registry.");
    }

    public void removeDrone(ARDroneEntity drone) {
        drones.remove(drone);
        System.out.println("Drone " + drone.getId() + " has been removed from Registry.");
    }

    public ARDroneEntity getLeader() {
        ARDroneEntity leader = null;
        Iterator<ARDroneEntity> itr = drones.iterator();
        while (itr.hasNext() && leader == null) {
            ARDroneEntity current = itr.next();
            if (current.getId() == 1) {
                leader = current;
            }
        }

        return leader;
    }
}
