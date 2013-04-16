/*
 * Copyright 2010 Cliff L. Biffle.  All Rights Reserved. modified by <Jeremie Hoelter>
 * Use of this source code is governed by a BSD-style license that can be found
 * in the LICENSE file.
 */
package lib.ardrone.navigationdata;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


//import old.DroneState;

import lib.ardrone.listeners.*;
import lib.ardrone.Global;

public class NavDataParser {

    private AttitudeListener attitudeListener;
    private StateMaskListener stateMaskListener;
    private ControlStateListener ControlStateListener;
    private VelocityListener velocityListener;
    private BatteryListener batteryListener;
    private TagListener tagListener;
    private PhysMeasuresListener physMeasuresListener;
    private GyrosOffsetsListener gyrosOffsetsListener;
    private EulerAnglesListener eulerAnglesListener;
    private ReferencesListener referencesListener;
    private PwmListener pwmListener;
    private AltitudeListener altitudeListener;
    private VisionRawListener visionRawListener;
    private RawMeasuresListener rawMeasuresListener;
    private PressureRawListener pressureRawListener;
    long lastSequenceNumber = 1;

    // set listeners
    public void setBatteryListener(BatteryListener batteryListener) {
        this.batteryListener = batteryListener;
    }

    public void setStateMaskListener(StateMaskListener stateMaskListener) {
        this.stateMaskListener = stateMaskListener;
    }

    public void setAttitudeListener(AttitudeListener attitudeListener) {
        this.attitudeListener = attitudeListener;
    }

    public void setControlStateListener(ControlStateListener stateListener) {
        this.ControlStateListener = stateListener;
    }

    public void setVelocityListener(VelocityListener velocityListener) {
        this.velocityListener = velocityListener;
    }

    public void setTagListener(TagListener tagListener) {
        this.tagListener = tagListener;
    }

    public void setPhysMeasuresListener(
            PhysMeasuresListener physMeasuresListener) {
        this.physMeasuresListener = physMeasuresListener;
    }

    public void setGyrosOffsetsListener(
            GyrosOffsetsListener gyrosOffsetsListener) {
        this.gyrosOffsetsListener = gyrosOffsetsListener;
    }

    public void setEulerAnglesListener(EulerAnglesListener eulerAnglesListener) {
        this.eulerAnglesListener = eulerAnglesListener;
    }

    public void setReferencesListener(ReferencesListener referencesListener) {
        this.referencesListener = referencesListener;
    }

    public void setPwmListener(PwmListener pwmListener) {
        this.pwmListener = pwmListener;
    }

    public void setAltitudeListener(AltitudeListener altitudeListener){
        this.altitudeListener = altitudeListener;
    }
    
    public void setVisionRawListener(VisionRawListener visionRawListener) {
        this.visionRawListener = visionRawListener;
    }

    public void setRawMeasuresListener(RawMeasuresListener rawMeasuresListener) {
        this.rawMeasuresListener = rawMeasuresListener;
    }
    
    public void setPressureRawListener(PressureRawListener pressureRawListener) {
        this.pressureRawListener = pressureRawListener;
    }

    public void parseNavData(ByteBuffer buffer) throws NavDataException  {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int magic = buffer.getInt();
        requireEquals("Magic must be correct", 0x55667788, magic);
        
        int state = buffer.getInt();
        if (stateMaskListener != null) {
            stateMaskListener.stateMaskChanged(state);
        }

        long sequence = buffer.getInt() & 0xFFFFFFFFL;
        @SuppressWarnings("unused")
        int vision = buffer.getInt();
        //System.out.println("sequence number is " + sequence);
        if (sequence <= lastSequenceNumber && sequence != 1) {
            return;
        }
        lastSequenceNumber = sequence;


        while (buffer.position() < buffer.limit()) {
            int tag = buffer.getShort() & 0xFFFF;
            int payloadSize = (buffer.getShort() & 0xFFFF) - 4;

            ByteBuffer optionData = buffer.slice().order(
                    ByteOrder.LITTLE_ENDIAN);
            optionData.limit(payloadSize);
            buffer.position(buffer.position() + payloadSize);
            dispatch(tag, optionData);
        }
    }

    
     /*NAVDATA_DEMO_TAG(0), NAVDATA_TIME_TAG(1), NAVDATA_RAW_MEASURES_TAG(2),
      * NAVDATA_PHYS_MEASURES_TAG(3), NAVDATA_GYROS_OFFSETS_TAG(4), NAVDATA_EULER_ANGLES_TAG(5),
      * NAVDATA_REFERENCES_TAG(6), NAVDATA_TRIMS_TAG(7), NAVDATA_RC_REFERENCES_TAG(8),
      * NAVDATA_PWM_TAG(9), NAVDATA_ALTITUDE_TAG(10), NAVDATA_VISION_RAW_TAG(11),
      * NAVDATA_VISION_OF_TAG(12), NAVDATA_VISION_TAG(13), NAVDATA_VISION_PERF_TAG(14),
      * NAVDATA_TRACKERS_SEND_TAG(15), NAVDATA_VISION_DETECT_TAG(16), NAVDATA_WATCHDOG_TAG(17),
      * NAVDATA_ADC_DATA_FRAME_TAG(18), NAVDATA_VIDEO_STREAM_TAG(19), NAVDATA_CKS_TAG(0xFFFF);*/
    private void dispatch(int tag, ByteBuffer optionData) {
        switch (tag) {
            case 0:
                processNavDataDemo(optionData);
                break;
            case 2: // navdata_raw_measures
                processNavDataRawMeasures(optionData);
                break;
            case 3: // navdata_phys_measures_t
                processNavDataPhysMeasures(optionData);
                break;
            case 4: // navdata_gyros_offsets_t
                processNavDataGyrosOffsets(optionData);
                break;
            case 5: // navdata_euler_angles_t
                processNavDataEulerAngles(optionData);
                break;
            case 6: // navdata_references_t
                processNavDataReferences(optionData);
                break;
            case 7: // navdata_trims_t
                /**
                 * TODO: control not sent in future firmware versions. not sent
                 * in 1.4.7 even in full data mode
                 */
                // System.out.println(optionData.getFloat()+
                // " "+optionData.getFloat()+ " "+optionData.getFloat());
                break;
            case 8: // navdata_rc_references_t
                /**
                 * TODO: check it's correct only zero sent even while flying...
                 */
                // System.out.println(optionData.getInt()+ " "+optionData.getInt()+
                // " "+optionData.getInt()+ " "+optionData.getInt()+
                // " "+optionData.getInt());
                break;
            case 9: // navdata_pwm_t
                processNavDataPwm(optionData);
                break;
            case 10: // navdata_altitude_t
                break;
            case 11: // navdata_vision_raw_t
                processNavDataVisionRaw(optionData);
                break;
            case 16:
                processNavDataVisionDetect(optionData);
                break;
            case 21:
                processNavDataPressureRaw(optionData);
                break;
            default:
                // System.out.println("NAVDATA #TAG: " + tag + " size: " +
                // optionData.limit() + "bytes " + Utils.getDateTime());
                System.out.println("NAVDATA #TAG: " + tag + "     size: " + optionData.limit() + " bytes");
                break;
        }
    }

    private void processNavDataDemo(ByteBuffer optionData) {
        @SuppressWarnings("unused")
        int controlState = optionData.getInt();
        int batteryPercentage = optionData.getInt();
        float theta = optionData.getFloat() / 1000;
        float phi = optionData.getFloat() / 1000;
        float psi = optionData.getFloat() / 1000;
        int altitude = optionData.getInt();
        float vx = optionData.getFloat();
        float vy = optionData.getFloat();
        float vz = optionData.getFloat();

        if (batteryListener != null) {
            batteryListener.batteryLevelChanged(batteryPercentage);
        }

        if (ControlStateListener != null) {
            ControlStateListener.controlStateChanged(controlState);
        }

        if (attitudeListener != null) {
            attitudeListener.attitudeUpdated(theta, phi, psi, altitude);
        }

        if (velocityListener != null) {
            velocityListener.velocityChanged(vx, vy, vz);
        }
    }

    private void processNavDataVisionDetect(ByteBuffer optionData) {
        int nb_detected = optionData.getInt();

        int[] type = new int[Global.NB_TAG_SEARCHED];
        int[] xc = new int[Global.NB_TAG_SEARCHED];
        int[] yc = new int[Global.NB_TAG_SEARCHED];
        int[] width = new int[Global.NB_TAG_SEARCHED];
        int[] height = new int[Global.NB_TAG_SEARCHED];
        int[] dist = new int[Global.NB_TAG_SEARCHED];
        float[] orientation_angle = new float[Global.NB_TAG_SEARCHED];

        for (int i = 0; i < Global.NB_TAG_SEARCHED; i++) {
            type[i] = optionData.getInt();
        }

        for (int i = 0; i < Global.NB_TAG_SEARCHED; i++) {
            xc[i] = optionData.getInt();
        }

        for (int i = 0; i < Global.NB_TAG_SEARCHED; i++) {
            yc[i] = optionData.getInt();
        }

        for (int i = 0; i < Global.NB_TAG_SEARCHED; i++) {
            width[i] = optionData.getInt();
        }

        for (int i = 0; i < Global.NB_TAG_SEARCHED; i++) {
            height[i] = optionData.getInt();
        }

        for (int i = 0; i < Global.NB_TAG_SEARCHED; i++) {
            dist[i] = optionData.getInt();
        }
        /*
         * NOT IMPLEMENTED IN CURRENT ARDRONE FIRMWARE VERSION (1.4.7) 
         * for (int i=0 ; i<Global.NB_TAG_SEARCHED ; i++){
         *    orientation_angle[i]=optionData.getFloat();
         * }
         */

        if (tagListener != null) {
            tagListener.tagDetectionChanged(nb_detected, type, xc, yc, width,
                    height, dist, orientation_angle);
        }

    }

    private void processNavDataRawMeasures(ByteBuffer optionData) {
        int[] raw_accs = new int[Global.NB_ACCS];
        int[] raw_gyros = new int[Global.NB_GYROS];
        int[] raw_gyros_110 = new int[2];
        for (int i = 0; i < Global.NB_ACCS; i++) {
            raw_accs[i] = optionData.getShort() & 0xFFFF;
        }
        for (int i = 0; i < Global.NB_GYROS; i++) {
            raw_gyros[i] = optionData.getShort() & 0xFFFF;
        }
        for (int i = 0; i < 2; i++) {
            raw_gyros_110[i] = optionData.getShort() & 0xFFFF;
        }
        int vbat_raw = optionData.getInt();
        int us_debut_echo = optionData.getShort() & 0xFFFF;
        int us_fin_echo = optionData.getShort() & 0xFFFF;
        int us_association_echo = optionData.getShort() & 0xFFFF;
        int us_distance_echo = optionData.getShort() & 0xFFFF;

        if (rawMeasuresListener != null) {
            rawMeasuresListener.rawMeasuresChanged(raw_accs, raw_gyros, raw_gyros_110, vbat_raw, us_debut_echo, us_fin_echo, us_association_echo, us_distance_echo);
        }
    }

    private void processNavDataPhysMeasures(ByteBuffer optionData) {

        float accs_temp = optionData.getFloat();
        int gyro_temp = (optionData.getShort() & 0xFFFF);
        float[] phys_accs = new float[Global.NB_ACCS];
        float[] phys_gyros = new float[Global.NB_GYROS];
        for (int i = 0; i < Global.NB_ACCS; i++) {
            phys_accs[i] = optionData.getFloat();
        }
        for (int i = 0; i < Global.NB_GYROS; i++) {
            phys_gyros[i] = optionData.getFloat();
        }

        if (physMeasuresListener != null) {
            physMeasuresListener.physMeasuresChanged(accs_temp, gyro_temp,
                    phys_accs, phys_gyros);
        }

    }

    private void processNavDataGyrosOffsets(ByteBuffer optionData) {
        float offset_g[] = new float[Global.NB_GYROS];
        for (int i = 0; i < Global.NB_GYROS; i++) {
            offset_g[i] = optionData.getFloat();
        }

        if (gyrosOffsetsListener != null) {
            gyrosOffsetsListener.gyrosOffestsChanged(offset_g);
        }
    }

    private void processNavDataEulerAngles(ByteBuffer optionData) {
        float theta_a = 0, phi_a = 0;
        theta_a = optionData.getFloat();
        phi_a = optionData.getFloat();

        if (eulerAnglesListener != null) {
            eulerAnglesListener.eulerAnglesChanged(theta_a, phi_a);
        }
    }

    private void processNavDataReferences(ByteBuffer optionData) {
        int ref_theta, ref_phi, ref_theta_I, ref_phi_I, ref_pitch, ref_roll, ref_yaw, ref_psi;
        ref_theta = optionData.getInt();
        ref_phi = optionData.getInt();
        ref_theta_I = optionData.getInt();
        ref_phi_I = optionData.getInt();
        ref_pitch = optionData.getInt();
        ref_roll = optionData.getInt();
        ref_yaw = optionData.getInt();
        ref_psi = optionData.getInt();

        if (referencesListener != null) {
            referencesListener.referencesChanged(ref_theta, ref_phi,
                    ref_theta_I, ref_phi_I, ref_pitch, ref_roll, ref_yaw,
                    ref_psi);
        }
    }

    private void processNavDataPwm(ByteBuffer optionData) {
        int motor1, motor2, motor3, motor4;
        int gaz_feed_forward, gaz_altitude;
        float altitude_integral, vz_ref;
        int u_pitch, u_roll, u_yaw;
        float yaw_u_I;
        int u_pitch_planif, u_roll_planif, u_yaw_planif;
        float yaw_u_I_planif;

        motor1 = (optionData.get() & 0xFF);
        motor2 = (optionData.get() & 0xFF);
        motor3 = (optionData.get() & 0xFF);
        motor4 = (optionData.get() & 0xFF);
        optionData.position(optionData.position() + 4); // to jump over sat_motor 1 to 4 (type uint8)
        gaz_feed_forward = optionData.getInt();
        gaz_altitude = optionData.getInt();
        altitude_integral = optionData.getFloat();
        vz_ref = optionData.getFloat();
        u_pitch = optionData.getInt();
        u_roll = optionData.getInt();
        u_yaw = optionData.getInt();
        yaw_u_I = optionData.getFloat();
        u_pitch_planif = optionData.getInt();
        u_roll_planif = optionData.getInt();
        u_yaw_planif = optionData.getInt();
        yaw_u_I_planif = optionData.getFloat();


        if (this.pwmListener != null) {
            pwmListener.pwmChanged(motor1, motor2, motor3, motor4,
                    gaz_feed_forward, gaz_altitude, altitude_integral, vz_ref,
                    u_pitch, u_roll, u_yaw, yaw_u_I, u_pitch_planif,
                    u_roll_planif, u_yaw_planif, yaw_u_I_planif);
        }
    }

    private void processNavDataAltitude(ByteBuffer optionData){
        float altitude_vz;
        int altitude_vision, altitude_ref, altitude_raw;
        altitude_vision = optionData.getInt();
        altitude_vz = optionData.getFloat();
        altitude_ref = optionData.getInt();
        altitude_raw = optionData.getInt();
        
        if (this.altitudeListener != null) {
            altitudeListener.altitudeChanged(altitude_vision, altitude_vz, altitude_ref, altitude_raw);
        }
    }
    
    private void processNavDataVisionRaw(ByteBuffer optionData) {
        float vision_tx_raw, vision_ty_raw, vision_tz_raw;
        vision_tx_raw = optionData.getFloat();
        vision_ty_raw = optionData.getFloat();
        vision_tz_raw = optionData.getFloat();

        if (this.visionRawListener != null) {
            visionRawListener.visionRawChanged(vision_tx_raw, vision_ty_raw, vision_tz_raw);
        }
    }
    
    private void  processNavDataPressureRaw(ByteBuffer optionData){
        int up, ut,temperature_meas, pression_meas;
        up = optionData.getInt();
        ut = optionData.getShort() & 0xFFFF; 
        temperature_meas = optionData.getInt();
        pression_meas = optionData.getInt();
        
        if (this.pressureRawListener != null) {
            pressureRawListener.pressureRawChanged(up, ut, temperature_meas, pression_meas);
        }
    }

    private void requireEquals(String message, int expected, int actual)
            throws NavDataException {
        if (expected != actual) {
            throw new NavDataException(message + " : expected " + expected
                    + ", was " + actual);
        }
    }
}