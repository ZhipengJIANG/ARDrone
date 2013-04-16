/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.ardrone.listeners;

/**
 *
 * @author Lucas
 */
public interface PressureRawListener {
    void pressureRawChanged(int up, int ut, int temperature_meas, int pression_meas);
}
