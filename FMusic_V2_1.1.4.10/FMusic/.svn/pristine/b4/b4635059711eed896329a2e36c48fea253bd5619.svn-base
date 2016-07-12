package vn.com.fptshop.fmusic.download.core;

/**
 * Created by MinhDH on 12/4/15.
 */
public interface DownloadTask extends Runnable {

    interface OnDownloadListener {
        void onProgress(long finished, long length);

        void onComplete();

        void onPause();

        void onCancel();

        void onFailure(DownloadException de);
    }

    void cancel();

    void pause();

    boolean isDownloading();

    boolean isComplete();

    boolean isPaused();

    boolean isCanceled();

    boolean isFailure();

    @Override
    void run();
}

