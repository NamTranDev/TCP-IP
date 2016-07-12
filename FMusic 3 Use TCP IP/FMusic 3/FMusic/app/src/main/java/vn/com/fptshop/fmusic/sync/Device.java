package vn.com.fptshop.fmusic.sync;

/**
 * Created by MinhDH on 12/29/15.
 */
public class Device {
    private String name;
    private String ip;
    private int position;
    private String serial;

    public Device() {
    }

    public Device(String name, String ip, int position, String serial) {
        this.name = name;
        this.ip = ip;
        this.position = position;
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
