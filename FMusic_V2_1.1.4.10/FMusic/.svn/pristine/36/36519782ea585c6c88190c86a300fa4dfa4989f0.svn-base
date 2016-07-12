package vn.com.fptshop.fmusic.models;

/**
 * Created by MinhDH on 11/25/15.
 */
public class AppCombo {
    public static final int STATUS_NOT_DOWNLOAD = 0;
    public static final int STATUS_CONNECTING = 1;
    public static final int STATUS_CONNECT_ERROR = 2;
    public static final int STATUS_DOWNLOADING = 3;
    public static final int STATUS_PAUSE = 4;
    public static final int STATUS_DOWNLOAD_ERROR = 5;
    public static final int STATUS_COMPLETE = 6;
    public static final int STATUS_INSTALLED = 7;
    private int appComboId;
    private String appComboName;
    private int platformId;
    private int appsCount;
    private int sortIndex;
    private int status;

    public AppCombo(int appComboId, String appComboName, int platformId, int appsCount, int sortIndex) {
        this.appComboId = appComboId;
        this.appComboName = appComboName;
        this.platformId = platformId;
        this.appsCount = appsCount;
        this.sortIndex = sortIndex;
    }

    public AppCombo() {
    }

    public int getAppComboId() {
        return appComboId;
    }

    public void setAppComboId(int appComboId) {
        this.appComboId = appComboId;
    }

    public String getAppComboName() {
        return appComboName;
    }

    public void setAppComboName(String appComboName) {
        this.appComboName = appComboName;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public int getAppsCount() {
        return appsCount;
    }

    public void setAppsCount(int appsCount) {
        this.appsCount = appsCount;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }
    public String getStatusText() {
        switch (status) {
            case STATUS_NOT_DOWNLOAD:
                return "Not Download";
            case STATUS_CONNECTING:
                return "Connecting";
            case STATUS_CONNECT_ERROR:
                return "Connect Error";
            case STATUS_DOWNLOADING:
                return "Downloading";
            case STATUS_PAUSE:
                return "Pause";
            case STATUS_DOWNLOAD_ERROR:
                return "Download Error";
            case STATUS_COMPLETE:
                return "Complete";
            case STATUS_INSTALLED:
                return "Installed";
            default:
                return "Not Download";
        }
    }

    public String getButtonText() {
        switch (status) {
            case STATUS_NOT_DOWNLOAD:
                return "Tải";
            case STATUS_CONNECTING:
                return "Cancel";
            case STATUS_CONNECT_ERROR:
                return "Try Again";
            case STATUS_DOWNLOADING:
                return "Tải";
            case STATUS_PAUSE:
                return "Tải";
            case STATUS_DOWNLOAD_ERROR:
                return "Try Again";
            case STATUS_COMPLETE:
                return "Install";
            case STATUS_INSTALLED:
                return "UnInstall";
            default:
                return "Download";
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
