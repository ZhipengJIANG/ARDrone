package lib.ardrone.flightscenarii;

import lib.ardrone.ARDroneEntity;
import lib.ardrone.ARDroneRegistry;

public class SuiviDynamique extends FlightScenario {

    private ARDroneRegistry dronesRegistry = null;
    private ARDroneEntity leader = null; //to speed up thread, stores the leader pointer...

    public SuiviDynamique(ARDroneEntity drone) {
        this.drone = drone;
        initialize();
    }

    public void initialize() {
        dronesRegistry = ARDroneRegistry.getSingleton();

    }

    public void run() {
        dronesRegistry.addDrone(drone);
        if (drone.getId() != 1) {
            leader = dronesRegistry.getLeader();
            //A Priori : theta = roulis
            float leaderYawInit = leader.getNavDataHandler().getNavDataDemo().getYaw();
            float myYawInit = this.drone.getNavDataHandler().getNavDataDemo().getYaw();
            while (!kill) {
                this.rollAdapt();
                this.pitchAdapt();
                this.yawAdapt(leaderYawInit, myYawInit);
                this.drone.getController().move();
                //System.out.println("boucle");
                try {
                    Thread.sleep(100); //kills CPU otherwise! sure?
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void kill() {
        this.kill = true;
        dronesRegistry.removeDrone(drone);
    }

    public void yawAdapt(float leaderYawInit, float myYawInit) {
        float leaderYaw = leader.getNavDataHandler().getNavDataDemo().getYaw() - leaderYawInit;
        float myYaw = this.drone.getNavDataHandler().getNavDataDemo().getYaw() - myYawInit;
        //System.out.println("leader :"+leaderYaw);
        //System.out.println("suiveur :"+myYaw);
        if (Math.abs(leaderYaw - myYaw) > 10) {
            if (leaderYaw * myYaw > 0) {
                this.drone.setSpeedYaw((leaderYaw - myYaw) / Math.abs(leaderYaw - myYaw) * Math.min(Math.abs(leaderYaw - myYaw) / 30, 1));
            } else {
                if (myYaw < 0) {
                    if (leaderYaw - myYaw < 360 - leaderYaw + myYaw) {
                        this.drone.setSpeedYaw(Math.min(1, (leaderYaw - myYaw) / 30));
                    } else {
                        this.drone.setSpeedYaw(-Math.min(1, Math.abs(360 - leaderYaw + myYaw) / 30));
                    }
                } else {
                    if (-leaderYaw + myYaw < 360 + leaderYaw - myYaw) {
                        this.drone.setSpeedYaw(-Math.min(1, (-leaderYaw + myYaw) / 30));
                    } else {
                        this.drone.setSpeedYaw(Math.min(1, Math.abs(360 + leaderYaw - myYaw) / 30));
                    }
                }
            }

        } else {
            this.drone.setSpeedYaw(0);
        }
        //System.out.println("consigne yaw :"+this.drone.speedYaw);
    }

    public void pitchAdapt() {
        float leaderPitch = leader.getNavDataHandler().getNavDataDemo().getPitch();
        this.drone.setSpeedBF(leaderPitch / 45);

        //System.out.println("consigne "+this.drone.speedBF);
    }

    public void rollAdapt() {
        float leaderRoll = leader.getNavDataHandler().getNavDataDemo().getRoll();
        this.drone.setSpeedLR(leaderRoll / 45);

        //System.out.println("consigne "+this.drone.speedLR);
    }

    public void altAdapt() {
        float leaderAlt = leader.getNavDataHandler().getNavDataDemo().getAltitude();
        float myAlt = this.drone.getNavDataHandler().getNavDataDemo().getAltitude();
        if (Math.abs(leaderAlt - myAlt) > 6) {
            this.drone.setSpeedUD(Math.min((leaderAlt - myAlt) / 150, 1));
            //System.out.println((leaderAlt-myAlt)/150);
            this.drone.getController().move();
        } else {
            this.drone.setSpeedUD(0);
            this.drone.getController().move();
        }
    }
}
