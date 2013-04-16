/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.ardrone.navigationdata.structures;

/**
 *
 * @author Lucas
 */
public class NavDataPressureRaw {
    private int up, ut, temperature_raw, pressure_raw;
    
    public void setUp(int up){
        this.up = up;
    }
    
    public int getUp(){
        return this.up;
    }
    
    public void setUt(int ut){
        this.ut = ut;
    }
    
    public int getUt(){
        return this.ut;
    }
    
    public void setTemperatureRaw(int temperatureRaw){
        this.temperature_raw = temperatureRaw;
    }
    
    public int getTemperatureRaw(){
        return this.temperature_raw;
    }
    
    public void setPressureRaw(int pressureRaw){
        this.pressure_raw = pressureRaw;
    }
    
    public int getPressureRaw(){
        return this.pressure_raw;
    }
    
    @Override
    public String toString(){
        return "temerature = " + this.temperature_raw + "; pressure = " + this.pressure_raw;
    }
}
