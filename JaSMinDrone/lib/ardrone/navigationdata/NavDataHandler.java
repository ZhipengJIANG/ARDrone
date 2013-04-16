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
package lib.ardrone.navigationdata;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Calendar;
import lib.ardrone.listeners.*;
import lib.ardrone.navigationdata.structures.*;
import lib.ardrone.network.SocketManagerNavData;
import lib.ardrone.ARDroneEntity;
import lib.ardrone.Global;

public class NavDataHandler extends Thread {

    private boolean kill = false;
    private ARDroneEntity drone = null;
    private SocketManagerNavData socketManagerNavData = null;
    private NavDataStateMask navdataStateMask = null;
    private NavDataDemo navdataDemo = null;
    private NavDataVisionDetect navdataVisionDetect = null;
    private NavDataPhysMeasures navdataPhysMeasures = null;
    private NavDataGyrosOffsets navdataGyrosOffsets = null;
    private NavDataEulerAngles navdataEulerAngles = null;
    private NavDataReferences navdataReferences = null;
    private NavDataPwm navdataPwm = null;
    private NavDataVisionRaw navdataVisionRaw = null;
    private NavDataRawMeasures navdataRawMeasures = null;
    private NavDataAltitude navdataAltitude = null;
    private NavDataPressureRaw navdataPressureRaw = null;
    private long time1, time2;
    public double timediff, nps = 0;

    public NavDataHandler(ARDroneEntity drone) {
        this.drone = drone;
        initialize();
    }

    private void initialize() {
        navdataDemo = new NavDataDemo();
        navdataStateMask = new NavDataStateMask();
        navdataVisionDetect = new NavDataVisionDetect();
        navdataPhysMeasures = new NavDataPhysMeasures();
        navdataGyrosOffsets = new NavDataGyrosOffsets();
        navdataEulerAngles = new NavDataEulerAngles();
        navdataReferences = new NavDataReferences();
        navdataPwm = new NavDataPwm();
        navdataVisionRaw = new NavDataVisionRaw();
        socketManagerNavData = SocketManagerNavData.getInstance(drone);
        navdataRawMeasures = new NavDataRawMeasures();
        navdataAltitude = new NavDataAltitude();
        navdataPressureRaw = new NavDataPressureRaw();
    }

    public void run() {
        if (!drone.hasPassiveMulticast() && !drone.getLocalHost()) {
            tickleNavdataPort();
        }
        //sendControlAck();

        time1 = Calendar.getInstance().getTimeInMillis();

        NavDataParser parser = new NavDataParser();

        parser.setStateMaskListener(new StateMaskListener() {

            @Override
            public void stateMaskChanged(int stateMask) {
                navdataStateMask.setStateMask(stateMask);
            }
        });

        parser.setAttitudeListener(new AttitudeListener() {

            @Override
            public void attitudeUpdated(float pitch, float roll, float yaw, int altitude) {
                navdataDemo.setPitch(pitch);
                navdataDemo.setRoll(roll);
                navdataDemo.setYaw(yaw);
                if (drone.altitudeCorrected) {
                    altitude = (int) (altitude * Math.abs((Math.cos(Math.toRadians((int) pitch)))));
                    altitude = (int) (altitude * Math.abs((Math.cos(Math.toRadians((int) roll)))));
                    //System.out.println("Altitude corrigée");
                }
                navdataDemo.setAltitude(altitude);
            }
        });

        parser.setControlStateListener(new ControlStateListener() {

            @Override
            public void controlStateChanged(int state) {
                navdataDemo.setControlState(state);
            }
        });

        /*
         * parser.setControlStateListener(new ControlStateListener() { @Override
         * public void controlStateChanged(DroneState state) { } });
         */

        parser.setBatteryListener(new BatteryListener() {

            @Override
            public void batteryLevelChanged(int percentage) {
                navdataDemo.setBatteryPercentage(percentage);
            }
        });

        parser.setVelocityListener(new VelocityListener() {

            @Override
            public void velocityChanged(float vx, float vy, float vz) {
                navdataDemo.setVelocityX(vx);
                navdataDemo.setVelocityY(vy);
                navdataDemo.setVelocityZ(vz);
            }
        });

        parser.setTagListener(new TagListener() {

            @Override
            public void tagDetectionChanged(int nb_detected, int[] type, int[] xc, int[] yc, int[] width, int[] height, int[] dist, float[] orientation_angle) {
                navdataVisionDetect.setNbDetected(nb_detected);
                navdataVisionDetect.setType(type);
                navdataVisionDetect.setXc(xc);
                navdataVisionDetect.setYc(yc);
                navdataVisionDetect.setWidth(width);
                navdataVisionDetect.setHeight(height);
                navdataVisionDetect.setDist(dist);
            }
        });

        parser.setPhysMeasuresListener(new PhysMeasuresListener() {

            @Override
            public void physMeasuresChanged(float accs_temp, int gyro_temp, float[] phys_accs, float[] phys_gyros) {
                navdataPhysMeasures.setAccs_temp(accs_temp);
                navdataPhysMeasures.setGyro_temp(gyro_temp);
                navdataPhysMeasures.setPhys_accs(phys_accs);
                navdataPhysMeasures.setPhys_gyros(phys_gyros);
            }
        });

        parser.setGyrosOffsetsListener(new GyrosOffsetsListener() {

            @Override
            public void gyrosOffestsChanged(float[] gyrosOffsets) {
                navdataGyrosOffsets.setOffset(gyrosOffsets);
            }
        });

        parser.setEulerAnglesListener(new EulerAnglesListener() {

            @Override
            public void eulerAnglesChanged(float theta, float phi) {
                navdataEulerAngles.setPhi_a(phi);
                navdataEulerAngles.setTheta_a(theta);
            }
        });

        parser.setReferencesListener(new ReferencesListener() {

            @Override
            public void referencesChanged(int ref_theta, int ref_phi, int ref_theta_I, int ref_phi_I, int ref_pitch, int ref_roll, int ref_yaw, int ref_psi) {
                navdataReferences.setRef_theta(ref_theta);
                navdataReferences.setRef_phi(ref_phi);
                navdataReferences.setRef_theta_I(ref_theta_I);
                navdataReferences.setRef_phi_I(ref_phi_I);
                navdataReferences.setRef_pitch(ref_pitch);
                navdataReferences.setRef_roll(ref_roll);
                navdataReferences.setRef_yaw(ref_yaw);
                navdataReferences.setRef_psi(ref_psi);
            }
        });

        parser.setPwmListener(new PwmListener() {

            @Override
            public void pwmChanged(int motor1, int motor2, int motor3, int motor4,
                    int gaz_feed_forward, int gaz_altitude, float altitude_integral,
                    float vz_ref, int u_pitch, int u_roll, int u_yaw, float yaw_u_I,
                    int u_pitch_planif, int u_roll_planif, int u_yaw_planif,
                    float yaw_u_I_planif) {
                navdataPwm.setMotor1(motor1);
                navdataPwm.setMotor2(motor2);
                navdataPwm.setMotor3(motor3);
                navdataPwm.setMotor4(motor4);
                navdataPwm.setGaz_feed_forward(gaz_feed_forward);
                navdataPwm.setGaz_altitude(gaz_altitude);
                navdataPwm.setAltitude_integral(altitude_integral);
                navdataPwm.setVz_ref(vz_ref);
                navdataPwm.setU_pitch(u_pitch);
                navdataPwm.setU_roll(u_roll);
                navdataPwm.setU_yaw(u_yaw);
                navdataPwm.setYaw_u_I(yaw_u_I);
                navdataPwm.setU_pitch_planif(u_pitch_planif);
                navdataPwm.setU_roll_planif(u_roll_planif);
                navdataPwm.setU_yaw_planif(u_yaw_planif);
                navdataPwm.setYaw_u_I_planif(yaw_u_I_planif);
            }
        });

        parser.setVisionRawListener(new VisionRawListener() {

            @Override
            public void visionRawChanged(float vision_tx_raw, float vision_ty_raw, float vision_tz_raw) {
                navdataVisionRaw.setVision_tx_raw(vision_tx_raw);
                navdataVisionRaw.setVision_ty_raw(vision_ty_raw);
                navdataVisionRaw.setVision_tz_raw(vision_tz_raw);
            }
        });

        parser.setRawMeasuresListener(new RawMeasuresListener() {

            @Override
            public void rawMeasuresChanged(int[] raw_accs, int[] raw_gyros, int[] raw_gyros_110, int vbat_raw, int us_debut_echo, int us_fin_echo, int us_association_echo, int us_distance_echo) {
                navdataRawMeasures.setRaw_accs(raw_accs);
                navdataRawMeasures.setRaw_gyros(raw_gyros);
                navdataRawMeasures.setRaw_gyros_110(raw_gyros_110);
                navdataRawMeasures.setVbat_raw(vbat_raw);
                navdataRawMeasures.setUs_debut_echo(us_debut_echo);
                navdataRawMeasures.setUs_fin_echo(us_fin_echo);
                navdataRawMeasures.setUs_association_echo(us_association_echo);
                navdataRawMeasures.setUs_distance_echo(us_distance_echo);
            }
        });
        
        parser.setAltitudeListener(new AltitudeListener() {

            @Override
            public void altitudeChanged(int vision, float vz, int ref, int raw) {
                navdataAltitude.setAltitudeVision(vision);
                navdataAltitude.setAltitudeRef(ref);
                navdataAltitude.setAltitudeRaw(raw);
                navdataAltitude.setAltitudeVz(vz);
            }
        });
        
        parser.setPressureRawListener(new PressureRawListener() {

            @Override
            public void pressureRawChanged(int up, int ut, int temp, int press) {
                navdataPressureRaw.setUp(up);
                navdataPressureRaw.setUt(ut);
                navdataPressureRaw.setTemperatureRaw(temp);
                navdataPressureRaw.setPressureRaw(press);
            }
        });

        DatagramPacket packet = new DatagramPacket(new byte[10240], 10240, drone.getInetAddress(), Global.NAVDATA_PORT);

        drone.navdataHandlerDone = true;

        while (!kill) {

            try {
                if (drone.navDataError && !drone.getLocalHost()) {
                } else {
                    socketManagerNavData.navdataSocketReceivePacket(packet);
                    if (!drone.getInetAddress().equals(packet.getAddress())) {
                    } else {
                        ByteBuffer buffer = ByteBuffer.wrap(packet.getData(), 0, packet.getLength());

                        parser.parseNavData(buffer);

                        time2 = Calendar.getInstance().getTimeInMillis();
                        timediff = (double) (time2 - time1) / 1000;
                        if (time2 != time1) {
                            nps = 1 / timediff;
                            time1 = time2;
                        }
                    }
                }
            } catch (IOException e) {
                drone.navDataError = true;
            } catch (NavDataException e) {
                drone.navDataError = true;
            }
        }
    }

    private void tickleNavdataPort() {
        /**
         * Avoid multicast on navdata port for firmware 1.7.x
         *
         * @author Yannick Presse
         *
         */
        //byte[] buf=new byte[1];    // ancien paquet init pour firm inf Ã  1.7.x
        //buf[0]='\n';               // idem
        byte multi;

        if (drone.hasMulticast()) {
            multi = 0x02;  //multicast
            System.out.println("Navadata Multicast");
        } else {
            multi = 0x01;  //unicast
            System.out.println("Navadata Unicast");
        }
        byte[] buf = {multi, 0x00, 0x00, 0x00};
        DatagramPacket packet = new DatagramPacket(buf, 4, drone.getInetAddress(), Global.NAVDATA_PORT);
        socketManagerNavData.navdataSocketSendPacket(packet);
    }

    /**
     * Allows to reinitialiaze the navdata communication after a connection loss
     *
     * @author Yannick Presse
     */
    public void reinitNavDataConnect() {
        tickleNavdataPort();
    }

    public NavDataDemo getNavDataDemo() {
        if (navdataDemo != null) {
            return navdataDemo;
        } else {
            return null;
        }
    }

    public NavDataStateMask getNavDataStateMask() {
        if (navdataStateMask != null) {
            return navdataStateMask;
        } else {
            return null;
        }
    }

    public NavDataVisionDetect getNavDataVisionDetect() {
        if (navdataVisionDetect != null) {
            return navdataVisionDetect;
        } else {
            return null;
        }
    }

    public NavDataPhysMeasures getNavDataPhysMeasures() {
        if (navdataPhysMeasures != null) {
            return navdataPhysMeasures;
        } else {
            return null;
        }
    }

    public NavDataGyrosOffsets getNavDataGyrosOffsets() {
        if (navdataGyrosOffsets != null) {
            return navdataGyrosOffsets;
        } else {
            return null;
        }
    }

    public NavDataEulerAngles getNavDataEulerAngles() {
        if (navdataEulerAngles != null) {
            return navdataEulerAngles;
        } else {
            return null;
        }
    }

    public NavDataReferences getNavDataReferences() {
        if (navdataReferences != null) {
            return navdataReferences;
        } else {
            return null;
        }
    }

    public NavDataPwm getNavDataPwm() {
        if (navdataPwm != null) {
            return navdataPwm;
        } else {
            return null;
        }
    }

    public NavDataVisionRaw getNavDataVisionRaw() {
        if (navdataVisionRaw != null) {
            return navdataVisionRaw;
        } else {
            return null;
        }
    }

    public NavDataRawMeasures getNavDataRawMeasures() {
        if (navdataRawMeasures != null) {
            return navdataRawMeasures;
        } else {
            return null;
        }
    }
    
    public NavDataAltitude getNavDataAltitude(){
        if(navdataAltitude != null){
            return navdataAltitude;
        }else{
            return null;
        }
    }
    
    public NavDataPressureRaw getNavDataPressureRaw(){
        if(navdataPressureRaw != null){
            return navdataPressureRaw;
        }else{
            return null;
        }
    }

    public void kill() {
        kill = true;
    }
}
