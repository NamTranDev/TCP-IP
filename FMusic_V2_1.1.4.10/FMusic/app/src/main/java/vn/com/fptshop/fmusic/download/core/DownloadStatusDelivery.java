package vn.com.fptshop.fmusic.download.core;

/**
 * Created by MinhDH on 12/4/15.
 */
public interface DownloadStatusDelivery {
    void postStart(DownloadStatus status);

    void postConnected(long length, boolean isRangeSupport, DownloadStatus status);

    void postProgressUpdate(long finished, long total, DownloadStatus status);

    void postComplete(DownloadStatus status);

    void postPause(DownloadStatus status);

    void postCancel(DownloadStatus status);

    void postFailure(DownloadException e, DownloadStatus status);
}
