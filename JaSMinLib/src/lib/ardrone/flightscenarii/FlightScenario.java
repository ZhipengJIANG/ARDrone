/**
 * @author hoelter
 */
package lib.ardrone.flightscenarii;

import lib.ardrone.ARDroneEntity;

public abstract class FlightScenario extends Thread {

    protected ARDroneEntity drone = null;
    protected boolean kill = false;

    public void kill() {
        this.kill = true;
    }
}
