package vn.com.fptshop.fmusic.models;

/**
 * Created by MinhDH on 11/25/15.
 */
public class Genre {
    public static final int STATUS_NOT_DOWNLOAD = 0;
    public static final int STATUS_CONNECTING = 1;
    public static final int STATUS_CONNECT_ERROR = 2;
    public static final int STATUS_DOWNLOADING = 3;
    public static final int STATUS_PAUSE = 4;
    public static final int STATUS_DOWNLOAD_ERROR = 5;
    public static final int STATUS_COMPLETE = 6;
    public static final int STATUS_INSTALLED = 7;
    private int genreId;
    private String genreName;
    private int totalSongs;
    private int sortIndex;
    private int status;
    private String thumbnail;

    public Genre(int genreId, String genreName, int totalSongs, int sortIndex, String thumbnail) {
        this.genreId = genreId;
        this.genreName = genreName;
        this.totalSongs = totalSongs;
        this.sortIndex = sortIndex;
        this.thumbnail = thumbnail;
    }

    public Genre() {

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

    public int getTotalSongs() {
        return totalSongs;
    }

    public void setTotalSongs(int totalSongs) {
        this.totalSongs = totalSongs;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
