/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.ardrone.listeners;

public interface AltitudeListener {
    void altitudeChanged(int altitude_vision, float altitude_vz, int altitude_ref,
                         int altitude_raw);
}
