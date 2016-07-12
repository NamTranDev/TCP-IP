package vn.com.fptshop.fmusic.download.core;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import vn.com.fptshop.fmusic.download.database.DataBaseManager;
import vn.com.fptshop.fmusic.download.entity.DownloadInfo;
import vn.com.fptshop.fmusic.download.entity.ThreadInfo;

/**
 * Created by MinhDH on 12/4/15.
 */
public class MultiDownloadTask extends AbsDownloadTask {

    private DataBaseManager mDBManager;

    public MultiDownloadTask(DownloadInfo downloadInfo, ThreadInfo threadInfo, DataBaseManager dbManager, OnDownloadListener listener) {

        super(downloadInfo, threadInfo, listener);
        this.mDBManager = dbManager;
    }

    @Override
    protected void insertIntoDB(ThreadInfo info) {
        if (!mDBManager.exists(info.getUrl(), info.getId())) {
            mDBManager.insert(info);
        }
    }

    @Override
    protected int getResponseCode() {
        return HttpURLConnection.HTTP_PARTIAL;
    }

    @Override
    protected void updateDBProgress(ThreadInfo info) {
        mDBManager.update(info.getUrl(), info.getId(), info.getFinished());
    }

    @Override
    protected Map<String, String> getHttpHeaders(ThreadInfo info) {
        Map<String, String> headers = new HashMap<String, String>();
        long start = info.getStart() + info.getFinished();
        long end = info.getEnd();
        headers.put("Range", "bytes=" + start + "-" + end);
        return headers;
    }

    @Override
    protected RandomAccessFile getFile(ThreadInfo threadInfo, DownloadInfo downloadInfo) throws IOException {
        File file = new File(downloadInfo.getDir(), downloadInfo.getName());
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        long start = threadInfo.getStart() + threadInfo.getFinished();
        raf.seek(start);
        return raf;
    }

    @Override
    protected String getTag() {
        return this.getClass().getSimpleName();
    }
}