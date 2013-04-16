/**
 * @author DreamTeam
 */
package lib.ardrone.flightscenarii;

import java.util.ResourceBundle.Control;

import lib.ardrone.ARDroneEntity;
import lib.ardrone.navigationdata.*;

public class TestAltitude extends FlightScenario {

    /**
     * to track which drone executes the scenario, useful to send commands via
     * commands like drone.getController().backward() retrieving NavData with
     * drone.getNavDataHandler().getNavDataVisionDetect().getNbDetected()
     */
    private long startTime, timer;
    private int speed;
    private int bound;

    public TestAltitude(ARDroneEntity drone) {
        this.drone = drone;
        initialize();
    }

    public void initialize() {
        super.setName(drone.getName() + " AltitudeCorrection"); // to setup the
        // Thread's
        // name --> JProfiler

        speed = 20;
        timer = 0;
        bound = 30;
    }

    public void run() {

        this.drone.getController().takeOff();
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (!kill) {
            long startTime = System.currentTimeMillis();

            int altitude = this.drone.getNavDataHandler().getNavDataDemo().getAltitude();
            while (timer < 1000) {
                timer += System.currentTimeMillis() - startTime;
            }
            int altitudeChanged = this.drone.getNavDataHandler().getNavDataDemo().getAltitude();
            timer = 0;

            if ((altitude - bound >= altitudeChanged)
                    || (altitudeChanged >= altitude + bound)) {
                this.drone.getNavDataHandler().getNavDataDemo().setAltitude(
                        altitude);
                System.out.println("Changement d'altitude supérieur à " + bound);
            }
        }
        this.drone.getController().landing();
    }
}