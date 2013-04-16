package lib.ardrone.navigationdata.structures;

import lib.ardrone.Global;

public class NavDataPhysMeasures {

    private float accs_temp = 0;
    private int gyro_temp = 0;
    private float[] phys_accs;
    private float[] phys_gyros;
    // other info useless for us so far...

    public NavDataPhysMeasures() {
        setPhys_accs(new float[Global.NB_ACCS]);
        setPhys_gyros(new float[Global.NB_GYROS]);
    }

    public void setAccs_temp(float accs_temp) {
        this.accs_temp = accs_temp;
    }

    public float getAccs_temp() {
        return accs_temp;
    }

    public void setGyro_temp(int gyro_temp) {
        this.gyro_temp = gyro_temp;
    }

    public int getGyro_temp() {
        return gyro_temp;
    }

    public void setPhys_accs(float[] phys_accs) {
        this.phys_accs = phys_accs;
    }

    public float[] getPhys_accs() {
        return phys_accs;
    }

    public float getPhys_accs(int i) {
        return phys_accs[i];
    }

    public void setPhys_gyros(float[] phys_gyros) {
        this.phys_gyros = phys_gyros;
    }

    public float[] getPhys_gyros() {
        return phys_gyros;
    }

    public float getPhys_gyros(int i) {
        return phys_gyros[i];
    }

    private String tabToString(float[] tab) {
        String result = "";
        for (int i = 0; i < tab.length; i++) {
            result = result + tab[i] + " ";
        }
        return result;
    }

    @Override
    public String toString() {
        return "PHYSMEASURES: " + "ACCS_temp:" + this.accs_temp + " GYRO_temp: " + gyro_temp + " PHYS_ACCS: " + tabToString(phys_accs) + " PHYS_GYROS: " + tabToString(phys_gyros);
    }
}
