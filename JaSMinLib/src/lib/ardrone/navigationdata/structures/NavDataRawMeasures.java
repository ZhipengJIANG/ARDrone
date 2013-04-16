package lib.ardrone.navigationdata.structures;

import lib.ardrone.Global;

public class NavDataRawMeasures {

    private int[] raw_accs;
    private int[] raw_gyros;
    private int[] raw_gyros_110;
    private int vbat_raw;
    private int us_debut_echo;
    private int us_fin_echo;
    private int us_association_echo;
    private int us_distance_echo;
    // other items not accounted for here yet.

    public NavDataRawMeasures() {
        raw_accs = new int[Global.NB_ACCS];
        raw_gyros = new int[Global.NB_GYROS];
        raw_gyros_110 = new int[2];
    }

    public void setRaw_accs(int[] raw_accs) {
        this.raw_accs = raw_accs;
    }

    public int[] getRaw_accs() {
        return raw_accs;
    }

    public void setRaw_gyros(int[] raw_gyros) {
        this.raw_gyros = raw_gyros;
    }

    public int[] getRaw_gyros() {
        return raw_gyros;
    }

    public void setRaw_gyros_110(int[] raw_gyros_110) {
        this.raw_gyros_110 = raw_gyros_110;
    }

    public int[] getRaw_gyros_110() {
        return raw_gyros_110;
    }

    public void setVbat_raw(int vbat_raw) {
        this.vbat_raw = vbat_raw;
    }

    public int getVbat_raw() {
        return vbat_raw;
    }

    public void setUs_debut_echo(int us_debut_echo) {
        this.us_debut_echo = us_debut_echo;
    }

    public int getUs_debut_echo() {
        return us_debut_echo;
    }

    public void setUs_fin_echo(int us_fin_echo) {
        this.us_fin_echo = us_fin_echo;
    }

    public int getUs_fin_echo() {
        return us_fin_echo;
    }

    public void setUs_association_echo(int us_association_echo) {
        this.us_association_echo = us_association_echo;
    }

    public int getUs_association_echo() {
        return us_association_echo;
    }

    public void setUs_distance_echo(int us_distance_echo) {
        this.us_distance_echo = us_distance_echo;
    }

    public int getUs_distance_echo() {
        return us_distance_echo;
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
        return "RAWMEASURES " + "Raw_accs: " + tabToString(this.raw_accs) + " Raw_gyros: " + tabToString(this.raw_gyros) + " Raw_gyros_110: " + tabToString(this.raw_gyros_110) + " Vbat_raw: " + vbat_raw + " US_debut: " + us_debut_echo + " US_fin:" + us_fin_echo + " US_association: " + us_association_echo + " US_distance: " + us_distance_echo;
    }
}
