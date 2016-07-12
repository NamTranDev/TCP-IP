package vn.com.fptshop.fmusic.models;

import java.io.Serializable;

/**
 * Created by MinhDH on 11/25/15.
 */
public class App implements Serializable {

    public static final int STATUS_NOT_DOWNLOAD = 0;
    public static final int STATUS_CONNECTING = 1;
    public static final int STATUS_CONNECT_ERROR = 2;
    public static final int STATUS_DOWNLOADING = 3;
    public static final int STATUS_PAUSE = 4;
    public static final int STATUS_DOWNLOAD_ERROR = 5;
    public static final int STATUS_COMPLETE = 6;
    public static final int STATUS_INSTALLED = 7;

    private int applicationId;
    private String applicationName;
    private int appComboId;
    private String appComboName;
    private int fileSize;
    private int download;
    private int status;
    private String local;
    private String packageName;

    public App(int applicationId, String applicationName, int appComboId, String appComboName, int fileSize, int download, String local) {
        this.applicationId = applicationId;
        this.applicationName = applicationName;
        this.appComboId = appComboId;
        this.appComboName = appComboName;
        this.fileSize = fileSize;
        this.download = download;
        this.local = local;
    }

    public App() {
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
