package vn.com.fptshop.fmusic.models;

import java.io.Serializable;

/**
 * Created by MinhDH on 11/25/15.
 */
public class Song implements Serializable {
    public static final int STATUS_NOT_DOWNLOAD = 0;
    public static final int STATUS_CONNECTING = 1;
    public static final int STATUS_CONNECT_ERROR = 2;
    public static final int STATUS_DOWNLOADING = 3;
    public static final int STATUS_PAUSE = 4;
    public static final int STATUS_DOWNLOAD_ERROR = 5;
    public static final int STATUS_COMPLETE = 6;
    public static final int STATUS_INSTALLED = 7;
    private int songId;
    private String songName;
    private int singerId;
    private String singerName;
    private int nationalId;
    private String nationalName;
    private int genreId;
    private String genreName;
    private int fileSize;
    private int download;
    private String local;
    protected long downloadPerCent;
    private int status;

    public Song(int songId, String songName, int singerId, String singerName, int nationalId, String nationalName, int genreId, String genreName, int fileSize, int download, String local) {
        this.songId = songId;
        this.songName = songName;
        this.singerId = singerId;
        this.singerName = singerName;
        this.nationalId = nationalId;
        this.nationalName = nationalName;
        this.genreId = genreId;
        this.genreName = genreName;
        this.fileSize = fileSize;
        this.download = download;
        this.local = local;
    }

    public Song() {
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getSingerId() {
        return singerId;
    }

    public void setSingerId(int singerId) {
        this.singerId = singerId;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public int getNationalId() {
        return nationalId;
    }

    public void setNationalId(int nationalId) {
        this.nationalId = nationalId;
    }

    public String getNationalName() {
        return nationalName;
    }

    public void setNationalName(String nationalName) {
        this.nationalName = nationalName;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
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
                return "Pause";
            case STATUS_PAUSE:
                return "Tải";
            case STATUS_DOWNLOAD_ERROR:
                return "Try Again";
            case STATUS_COMPLETE:
                return "Đã tải";
            case STATUS_INSTALLED:
                return "UnInstall";
            default:
                return "Tải";
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
