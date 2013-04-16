/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.ardrone.navigationdata.structures;

public class NavDataAltitude {
    private int altitude_vision, altitude_ref, altitude_raw;
    private float altitude_vz;

    public void setAltitudeVision(int vision) {
        this.altitude_vision = vision;
    }
    
    public int getAltitudeVision() {
        return altitude_vision;
    }
    
    public void setAltitudeRef(int ref) {
        this.altitude_ref = ref;
    }
    
    public int getAltitudeRef() {
        return altitude_ref;
    }
    
    public void setAltitudeRaw(int raw) {
        this.altitude_raw = raw;
    }
    
    public int getAltitudeRaw() {
        return altitude_raw;
    }
    
    public void setAltitudeVz(float vz) {
        this.altitude_vz = vz;
    }
    
    public float getAltitudeVz() {
        return altitude_vz;
    }

    @Override
    public String toString() {
        return "Altitude: " + "vision: " + altitude_vision + ", ref: " + + altitude_ref + ", raw: " + altitude_raw + ", vz: " + altitude_vz ;
    }
}
