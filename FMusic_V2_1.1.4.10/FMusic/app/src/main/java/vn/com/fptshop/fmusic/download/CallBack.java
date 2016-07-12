package vn.com.fptshop.fmusic.download;

import java.net.HttpURLConnection;

import vn.com.fptshop.fmusic.download.core.DownloadException;

/**
 * Created by MinhDH on 12/4/15.
 */
public interface CallBack {

    /**
     * <p> {@link #onDownloadStart()}
     * <p> this will be the the first method called by
     * {@link com.aspsine.multithreaddownload.core.ConnectTask}.
     */
    void onDownloadStart();

    /**
     * <p> {@link #onConnected(long, boolean)}
     * <p> if {@link com.aspsine.multithreaddownload.core.ConnectTask} is successfully
     * connected with the http/https server this method will be invoke. If not method
     * {@link #onFailure(DownloadException)} will be invoke.
     *
     * @param total          The length of the file. See {@link HttpURLConnection#getContentLength()}
     * @param isRangeSupport indicate whether download can be resumed from pause.
     *                       See {@link ConnectTask#run()}. If the value of http header field
     *                       {@code Accept-Ranges} is {@code bytes} the value of  isRangeSupport is
     *                       {@code true} else {@code false}
     */
    void onConnected(long total, boolean isRangeSupport);

    /**
     * <p> {@link #onProgress(long, long, int)}
     * <p> progress callback.
     *
     * @param finished the downloaded length of the file
     * @param total    the total length of the file same value with method {@link }
     * @param progress the percent of progress (finished/total)*100
     */
    void onProgress(long finished, long total, int progress);

    /**
     * <p>{@link #onComplete()}
     * <p> download complete
     */
    void onComplete();

    /**
     * <p>{@link #onDownloadPause()}
     * <p> if you invoke {@link DownloadManager#pause(String)} or {@link DownloadManager#pauseAll()}
     * this method will be invoke if the downloading task is successfully paused.
     */
    void onDownloadPause();

    /**
     * <p>{@link #onDownloadCancel()}
     * <p> if you invoke {@link DownloadManager#cancel(String)} or {@link DownloadManager#cancelAll()}
     * this method will be invoke if the downloading task is successfully canceled.
     */
    void onDownloadCancel();

    /**
     * <p>{@link #onDownloadCancel()}
     * <p> download fail or exception callback
     *
     * @param e
     */
    void onFailure(DownloadException e);
}
