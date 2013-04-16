package lib.ardrone.listeners;

public interface PwmListener {

    public void pwmChanged(int motor1, int motor2, int motor3, int motor4,
            int gaz_feed_forward, int gaz_altitude, float altitude_integral,
            float vz_ref, int u_pitch, int u_roll, int u_yaw, float yaw_u_I,
            int u_pitch_planif, int u_roll_planif, int u_yaw_planif,
            float yaw_u_I_planif);
}
