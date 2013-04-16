package lib.ardrone.navigationdata.structures;

import lib.ardrone.Global;

public class NavDataVisionDetect {

    private int nb_detected = 0;
    private int[] type;
    private int[] xc;
    private int[] yc;
    private int[] width;
    private int[] height;
    private int[] dist;
    private float[] orientation_angle;

    public NavDataVisionDetect() {
        type = new int[Global.NB_TAG_SEARCHED];
        xc = new int[Global.NB_TAG_SEARCHED];
        yc = new int[Global.NB_TAG_SEARCHED];
        width = new int[Global.NB_TAG_SEARCHED];
        height = new int[Global.NB_TAG_SEARCHED];
        dist = new int[Global.NB_TAG_SEARCHED];
        orientation_angle = new float[Global.NB_TAG_SEARCHED];
    }

    public int getNbDetected() {
        return nb_detected;
    }

    public void setNbDetected(int nb) {
        this.nb_detected = nb;
    }

    /**
     *
     * @param nb
     * @return int FIXME catch out of boundaries exceptions iv nv >
     * NB_TAG_SEARCHED
     */
    public int getType(int nb) {
        return type[nb];
    }

    public void setType(int[] type) {
        this.type = type;
    }

    public int getXc(int nb) {
        return xc[nb];
    }

    public void setXc(int[] xc) {
        this.xc = xc;
    }

    public int getYc(int nb) {
        return yc[nb];
    }

    public void setYc(int[] yc) {
        this.yc = yc;
    }

    public int getWidth(int nb) {
        return width[nb];
    }

    public void setWidth(int[] width) {
        this.width = width;
    }

    public int getHeight(int nb) {
        return height[nb];
    }

    public void setHeight(int[] height) {
        this.height = height;
    }

    public int getDist(int nb) {
        return dist[nb];
    }

    public void setDist(int[] dist) {
        this.dist = dist;
    }

    public float getOrientationAngle(int nb) {
        return orientation_angle[nb];
    }

    public void setOrientationAngle(float[] angle) {
        this.orientation_angle = angle;
    }

    private String tabToString(int[] tab) {
        String result = "";
        for (int i = 0; i < tab.length; i++) {
            result = result + tab[i] + " ";
        }
        return result;
    }

    @Override
    public String toString() {
        return "VISIONDETECT: " + "NB:" + this.nb_detected + " TYPE: " + tabToString(type) + " Xc: " + tabToString(xc) + " Yc: " + tabToString(yc) + " WIDTH: " + tabToString(width) + " HEIGHT: " + tabToString(height) + " DIST: " + tabToString(dist);
    }
}
