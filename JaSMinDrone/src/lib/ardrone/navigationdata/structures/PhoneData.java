package lib.ardrone.navigationdata.structures;

public class PhoneData {

    private String latitude, longitude, altitude;

    public PhoneData() {
        latitude = "0";
        longitude = "0";
        altitude = "0";
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getAltitude() {
        return altitude;
    }
}
