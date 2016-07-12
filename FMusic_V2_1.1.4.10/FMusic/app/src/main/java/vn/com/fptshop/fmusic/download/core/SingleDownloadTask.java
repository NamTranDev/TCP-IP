package vn.com.fptshop.fmusic.download.core;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.Map;

import vn.com.fptshop.fmusic.download.entity.DownloadInfo;
import vn.com.fptshop.fmusic.download.entity.ThreadInfo;

/**
 * Created by MinhDH on 12/4/15.
 */
public class SingleDownloadTask extends AbsDownloadTask {

    public SingleDownloadTask(DownloadInfo mDownloadInfo, ThreadInfo mThreadInfo, OnDownloadListener mOnDownloadListener) {
        super(mDownloadInfo, mThreadInfo, mOnDownloadListener);
    }

    @Override
    protected void insertIntoDB(ThreadInfo info) {
        // don't support
    }

    @Override
    protected int getResponseCode() {
        return HttpURLConnection.HTTP_OK;
    }

    @Override
    protected void updateDBProgress(ThreadInfo info) {
        // needn't Override this
    }

    @Override
    protected Map<String, String> getHttpHeaders(ThreadInfo info) {
        // simply return null
        return null;
    }

    @Override
    protected RandomAccessFile getFile(ThreadInfo threadInfo, DownloadInfo downloadInfo) throws IOException {
        File file = new File(downloadInfo.getDir(), downloadInfo.getName());
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        raf.seek(0);
        return raf;
    }

    @Override
    protected String getTag() {
        return this.getClass().getSimpleName();
    }
}


