package vn.com.fptshop.fmusic.models;

/**
 * Created by MinhDH on 11/26/15.
 */
public class AppVersion {
    private int id;
    private String version;
    private  String url;

    public AppVersion(int id, String version, String url) {
        this.id = id;
        this.version = version;
        this.url = url;
    }

    public AppVersion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
