package non_interactive_client;

import java.io.IOException;
import lib.ardrone.ARDroneEntity;
import lib.ardrone.control.Control;

/**
 * 
 * @author Yannick Presse
 */
public class AutoPilot {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        ARDroneEntity myDrone = new ARDroneEntity();
        boolean stopScenario;
        boolean lowBattery;
        myDrone.setSpeed(40);
        myDrone.setName("drone");
        myDrone.setInetAdress("192.168.1.1");
        myDrone.setId(1);
        myDrone.connect();
        Control myController = myDrone.getController();


        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }


        stopScenario = myDrone.testEmergency();
         if (!stopScenario) {

           lowBattery = myDrone.testBatterie();

           if (!lowBattery) {

                /*
                 * Scenario begins here
                 *
                 * Use the method with time arguments in millisec
                 * An optionnal second argument is the movement speed in % (default value is 40%)
                 * 
                 * The combined move needs 4 arguments: time in millisec, speed in % [forward(-) or backward(+)],
                 *                                      speed in % [down(-) or up (+)], speed in % [spin left(-) or right(+)]
                 * In the following example the drone will go forward at 40%, go up at 30% and spin right at 50% during 2 seconds
                 *  myDrone.combinedMoves(2000, -40, 30, 50);
                 * 
                 */

                myDrone.trim();

                myDrone.takeoff();

                myDrone.hover(1500);
/*
                myDrone.goForward(800, 30);

                myDrone.goBackward(800, 30);

                myDrone.hover(1000);

                myDrone.goForward(1500, 10);

                myDrone.goBackward(2000, 10);

                myDrone.hover(1000);

                myDrone.goLeft(600);

                myDrone.goRight(800);

                myDrone.up(800);

                myDrone.down(600);

                myDrone.spinLeft(1000);

                myDrone.spinRight(1200);
 
                myDrone.combinedMoves(2000, -40, 30, 50);
               
                myDrone.hover(1500);
               */
                myDrone.landing();


                /*
                 * The scenario ends here
                 */

            } else {
                for (int i = 0; i < 5; i++) {
                    System.out.println("\n LOW BATTERY");
                }
            }
        }
        myDrone.Disconnect();

    }
}
