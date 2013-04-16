package lib.ardrone.navigationdata.structures;

import lib.ardrone.Global;

public class NavDataGyrosOffsets {

    private float offset_g[];

    public NavDataGyrosOffsets() {
        setOffset(new float[Global.NB_GYROS]);
    }

    public void setOffset(float offset_g[]) {
        this.offset_g = offset_g;
    }

    public float[] getOffset() {
        return offset_g;
    }

    public float getOffset(int i) {
        return offset_g[i];
    }

    private String tabToString(float[] tab) {
        String result = "";
        for (int i = 0; i < tab.length; i++) {
            result = result + tab[i] + " ";
        }
        return result;
    }

    @ Override
    public String toString() {
        return "GYRO_OFFSETS: " + tabToString(this.offset_g);
    }
}
