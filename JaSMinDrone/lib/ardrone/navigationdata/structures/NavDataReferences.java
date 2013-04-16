package lib.ardrone.navigationdata.structures;

public class NavDataReferences {

    private int ref_theta, ref_phi, ref_theta_I, ref_phi_I, ref_pitch, ref_roll, ref_yaw, ref_psi;

    public void setRef_phi_I(int ref_phi_I) {
        this.ref_phi_I = ref_phi_I;
    }

    public int getRef_phi_I() {
        return ref_phi_I;
    }

    public void setRef_theta(int ref_theta) {
        this.ref_theta = ref_theta;
    }

    public int getRef_theta() {
        return ref_theta;
    }

    public void setRef_theta_I(int ref_theta_I) {
        this.ref_theta_I = ref_theta_I;
    }

    public int getRef_theta_I() {
        return ref_theta_I;
    }

    public void setRef_phi(int ref_phi) {
        this.ref_phi = ref_phi;
    }

    public int getRef_phi() {
        return ref_phi;
    }

    public void setRef_pitch(int ref_pitch) {
        this.ref_pitch = ref_pitch;
    }

    public int getRef_pitch() {
        return ref_pitch;
    }

    public void setRef_psi(int ref_psi) {
        this.ref_psi = ref_psi;
    }

    public int getRef_psi() {
        return ref_psi;
    }

    public void setRef_yaw(int ref_yaw) {
        this.ref_yaw = ref_yaw;
    }

    public int getRef_yaw() {
        return ref_yaw;
    }

    public void setRef_roll(int ref_roll) {
        this.ref_roll = ref_roll;
    }

    public int getRef_roll() {
        return ref_roll;
    }
}
