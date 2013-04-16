/*
 * Copyright (c) <2011>, <Shigeo Yoshida> modified by <Jeremie Hoelter>
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 The names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package lib.ardrone.control;

import java.net.DatagramPacket;
import java.net.SocketException;
import lib.ardrone.network.SocketManagerController;
import lib.ardrone.ARDroneEntity;
import lib.ardrone.Global;
import lib.utils.*;

public class Control extends Thread {

    private ARDroneEntity drone = null;
    private SocketManagerController socketManagerController = null;
    private boolean kill = false;
    private boolean ATconstruct = false;
    private static int seq = 1;
    private static final String CR = "\r";
    private boolean continuance = false;
    private String atCommand = null;
    private String atArg1 = null;
    private String atArg2 = null;

    public Control(ARDroneEntity drone) throws SocketException {
        this.drone = drone;

        initialize();
    }

    public void initialize() {
        seq = 1;
        socketManagerController = SocketManagerController.getInstance(drone);


        try {
            sendATCommand("AT*PMODE=" + (seq++) + ",2" + CR + "AT*MISC=" + (seq++) + ",2,20,2000,3000" + CR + "AT*REF=" + (seq++) + ",290717696");
            sleep(20);
            sendATCommand("AT*CONFIG=" + (seq++) + ",\"general:navdata_demo\",\"FALSE\"" + CR + "AT*FTRIM=" + (seq++));
            sleep(20);
            sendATCommand("AT*CONFIG=" + (seq++) + ",\"detect:detect_type\",\"2\"");
            sleep(20);
            sendATCommand("AT*CONFIG=" + (seq++) + ",\"detect:enemy_without_shell\",\"1\"");
            sleep(20);
            sendATCommand("AT*PCMD=" + (seq++) + ",0,0,0,0,0" + CR + "AT*REF=" + (seq++) + ",290717696" + CR + "AT*COMWDG=" + (seq++));
            sleep(20);
            System.out.println("Drone " + drone.getName() + " initialization completed!!!!");

        } catch (InterruptedException ex) {
        }

    }

    /**
     * AR.Drone Send command to ARDrone
     *
     * @param atCommand
     * @return
     */
    private synchronized void sendATCommand(String atCommand) {
        byte[] buffer = (atCommand + CR).getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, drone.getInetAddress(), Global.CONTROL_PORT);

        // Uses SocketManager to synchronously send packets
        socketManagerController.sendATCommandPacket(packet);
    }

    public void setATCommand(String atCommand, boolean continuance) {
        this.atCommand = atCommand;
        this.continuance = continuance;
    }

    public void setATarg1(String atArg1) {
        this.atArg1 = atArg1;
    }

    public void setATarg2(String atArg2) {
        this.atArg2 = atArg2;
    }

    public void run() {
        drone.controllerDone = true;

        while (!kill) {
            try {

                /**
                 * Add for manage the drone moving during a long press key
                 *
                 * @author Yannick Presse
                 */
                if (ATconstruct) {
                    setATCommand("AT*PCMD=" + (seq++) + "," + this.atArg1 + "\r" + "AT*REF=" + (seq++) + "," + this.atArg2, false);
                    sendATCommand(this.atCommand);
                    sendATCommand("AT*COMWDG=" + (seq++));
                    sleep(20);
                    continue;
                }

                if (this.atCommand != null) {
                    sendATCommand(this.atCommand);
                    sendATCommand("AT*COMWDG=" + (seq++));
                    sleep(20);

                    if (!this.continuance) {
                        this.atCommand = null;
                        continue;
                    }
                    continue;
                } else {
                    if (drone.landing) {
                        sendATCommand("AT*PCMD=" + (seq++) + ",0,0,0,0,0" + CR + "AT*REF=" + (seq++) + ",290717696" + CR + "AT*COMWDG=" + (seq++));
                        sleep(20);

                    } else {
                        sendATCommand("AT*PCMD=" + (seq++) + ",0,0,0,0,0" + CR + "AT*REF=" + (seq++) + ",290718208" + CR + "AT*COMWDG=" + (seq++));
                        sleep(20);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void kill() {
        this.kill = true;
    }

    public void reinitControlConnect() {
        initialize();
    }

    public void enableDemoData() {
        setATCommand("AT*CONFIG=" + (seq++) + ",\"general:navdata_demo\",\"TRUE\"", false);
        System.out.println("NavdataDemo enabled!");
    }

    public void disableDemoData() {
        setATCommand("AT*CONFIG=" + (seq++) + ",\"general:navdata_demo\",\"FALSE\"", false);
        System.out.println("Navdata FULL!");
    }

    public void enableVideoData() {
        setATCommand("AT*CONFIG=" + (seq++) + ",\"general:video_enable\",\"TRUE\"", false);
    }

    public void disableVideoData() {
        setATCommand("AT*CONFIG=" + (seq++) + ",\"general:video_enable\",\"FALSE\"", false);
    }

    /**
     * send Ack AT*CTRL=sequence number,0
     */
    public void sendControlAck() {
        setATCommand("AT*CTRL=" + (seq++) + ",0", false);
    }

    /**
     * Switch to the next possible camera combination
     */
    public void setNextCamera() {//next possible camera
        setATCommand("AT*CONFIG=" + (seq++) + ",\"video:video_channel\",\"4\"", false);

    }

    /**
     * FLIGHT CONTROL COMMANDS
     */
    /**
     * landing
     */
    public void landing() {
        drone.landing = true;
        ATconstruct = false;
        setATCommand("AT*REF=" + (seq++) + ",290717696", false);

        /**
         * force drone landing when disconnected
         *
         * @author presse_yan
         *
         */
        if (drone.disconnect) {
            sendATCommand("AT*PCMD=" + (seq++) + ",0,0,0,0,0" + CR + "AT*REF=" + (seq++) + ",290717696");
        }
    }

    /**
     * enable or disable emergency mode
     *
     * @author presse_yan
     */
    public void emergency() {
        ATconstruct = false;
        setATCommand("AT*REF=" + (seq++) + ",290717952", false);
    }

    /**
     * trim: reset to zero the inertial unit
     *
     * @author presse_yan
     */
    public void trim() {
        ATconstruct = false;
        setATCommand("AT*FTRIM=" + (seq++), false);
    }

    /**
     * hovering in the air
     *
     * @author presse_yan
     */
    public void hover() {

        ATconstruct = false;
        atCommand = null;
    }

    /**
     * hovering in flight scenario TagFollow
     *
     * @author presse_yan
     */
    public void hoverTag() {
        if (drone.landing) {
            return;
        }
        setATCommand("AT*PCMD=" + (seq++) + ",0,0,0,0,0" + "\r" + "AT*REF=" + (seq++) + ",290718208", true);

    }

    /**
     * Re-initiate sequence number
     *
     * @author Yannick Presse
     */
    public void initSeq() {
        seq = 1;
    }

    /**
     * take off
     */
    public void takeOff() {
        ATconstruct = false;
        setATCommand("AT*PCMD=" + (seq++) + ",0,0,0,0,0" + "\r" + "AT*REF=" + (seq++) + ",290718208", false);
    }

    /**
     * New ATcommand construct method Manage the drone moving during long press
     * on the keys
     *
     * @author Yannick Presse
     */
    /**
     * forward pitch-
     */
    public void forward() {
        if (drone.landing) {
            return;
        }

        ATconstruct = true;
        setATarg1("1,0," + Utils.intOfFloat(-drone.speed) + ",0,0");
        setATarg2("290718208");
    }

    /**
     * forward with specified speed
     *
     * @param speed
     */
    public void forward(int speed) {
        drone.setSpeed(speed);
        forward();
    }

    /**
     * backward pitch+
     */
    public void backward() {
        if (drone.landing) {
            return;
        }

        ATconstruct = true;
        setATarg1("1,0," + Utils.intOfFloat(drone.speed) + ",0,0");
        setATarg2("290718208");
    }

    /**
     * backward with specified speed
     *
     * @param speed
     */
    public void backward(int speed) {
        drone.setSpeed(speed);
        backward();
    }

    /**
     * move to right roll+
     */
    public void goRight() {
        if (drone.landing) {
            return;
        }

        ATconstruct = true;
        setATarg1("1," + Utils.intOfFloat(drone.speed) + ",0,0,0");
        setATarg2("290718208");
    }

    /**
     * move right with specified speed
     *
     * @param speed
     */
    public void goRight(int speed) {
        drone.setSpeed(speed);
        goRight();
    }

    /**
     * move to left roll-
     */
    public void goLeft() {
        if (drone.landing) {
            return;
        }

        ATconstruct = true;
        setATarg1("1," + Utils.intOfFloat(-drone.speed) + ",0,0,0");
        setATarg2("290718208");
    }

    /**
     * move left with specified speed
     *
     * @param speed
     */
    public void goLeft(int speed) {
        drone.setSpeed(speed);
        goLeft();
    }

    /**
     * up gaz+
     */
    public void up() {
        if (drone.landing) {
            return;
        }

        ATconstruct = true;
        setATarg1("1,0,0," + Utils.intOfFloat((drone.speed * 4)) + ",0");  //add a factor to harmonize speed movements  @author Yannick Presse
        setATarg2("290718208");
    }

    /**
     * move up with specified speed
     *
     * @param speed
     */
    public void up(int speed) {
        drone.setSpeed(speed);
        up();
    }

    /**
     * down gaz-
     */
    public void down() {
        if (drone.landing) {
            return;
        }

        ATconstruct = true;
        setATarg1("1,0,0," + Utils.intOfFloat(-(drone.speed * 4)) + ",0"); //add a factor to harmonize speed movements @author Yannick Presse
        setATarg2("290718208");
    }

    /**
     * move down with specified speed
     *
     * @param speed
     */
    public void down(int speed) {
        drone.setSpeed(speed);
        down();
    }

    /**
     * spin right yaw+
     */
    public void spinRight() {
        if (drone.landing) {
            return;
        }

        ATconstruct = true;
        setATarg1("1,0,0,0," + Utils.intOfFloat((drone.speed * 5)));    //add a factor to harmonize speed movements @author Yannick Presse
        setATarg2("290718208");
    }

    /**
     * spin right with specified speed
     *
     * @param speed
     */
    public void spinRight(int speed) {
        drone.setSpeed(speed);
        spinRight();
    }

    /**
     * spin left yaw-
     */
    public void spinLeft() {
        if (drone.landing) {
            return;
        }

        ATconstruct = true;
        setATarg1("1,0,0,0," + Utils.intOfFloat(-(drone.speed * 5)));   //add a factor to harmonize speed movements @author Yannick Presse
        setATarg2("290718208");
    }

    /**
     * spin left with specified speed
     *
     * @param speed
     */
    public void spinLeft(int speed) {
        drone.setSpeed(speed);
        spinLeft();
    }

    /**
     * not implemented
     *
     * @param radius
     */
    public void turnLeft(int radius) {
        if (drone.landing) {
            return;
        }
    }

    /**
     * not implemented
     *
     * @param radius
     */
    public void turnRight(int radius) {
        if (drone.landing) {
            return;
        }
    }

    public void move() {
        if (drone.landing) {
            return;
        }
        setATCommand("AT*PCMD=" + (seq++) + ",1," + Utils.intOfFloat(drone.speedLR) + "," + Utils.intOfFloat(drone.speedBF) + "," + Utils.intOfFloat(drone.speedUD) + "," + Utils.intOfFloat(drone.speedYaw) + "" + "\r" + "AT*REF=" + (seq++) + ",290718208", true);
    }

    public void advancedMove(float vectX, float vectY, float vectZ, float yaw) {
        if (drone.landing) {
            return;
        }
        setATCommand("AT*PCMD=" + (seq++) + ",1," + Utils.intOfFloat(vectX) + "," + Utils.intOfFloat(vectY) + "," + Utils.intOfFloat(vectZ) + "," + Utils.intOfFloat(yaw) + "" + "\r" + "AT*REF=" + (seq++) + ",290718208", true);
    }

    /*
     * Choose the wanted color of the tag for the "follow the tag" scenario
     * @author: Yannick Presse
     */
    public void tagColor(int tagColorChosen) {
        switch (tagColorChosen) {
            case 1: {
                System.out.println("Tag Color is Yellow");
                sendATCommand("AT*CONFIG=" + (seq++) + ",\"detect:enemy_colors\",\"1\""); //yellow
                break;
            }
            case 2: {
                System.out.println("Tag Color is Green");
                sendATCommand("AT*CONFIG=" + (seq++) + ",\"detect:enemy_colors\",\"2\""); //Green
                break;
            }
            case 3: {

                System.out.println("Tag Color is Blue");
                sendATCommand("AT*CONFIG=" + (seq++) + ",\"detect:enemy_colors\",\"3\""); //blue
                break;
            }

        }
    }

    public void altitudeMax(int alt) {
        sendATCommand("AT*CONFIG=" + (seq++) + ",\"control:altitude_max\",\"" + alt + "\"");
        System.out.println("Max altitude is set to " + (alt / 1000) + "m");
    }
}
