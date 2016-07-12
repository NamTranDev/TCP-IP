package vn.com.fptshop.fmusic.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.com.fptshop.fmusic.download.core.DownloadRequest;
import vn.com.fptshop.fmusic.download.core.DownloadStatus;
import vn.com.fptshop.fmusic.download.core.DownloadStatusDelivery;
import vn.com.fptshop.fmusic.download.core.DownloadStatusDeliveryImpl;
import vn.com.fptshop.fmusic.download.database.DataBaseManager;
import vn.com.fptshop.fmusic.download.entity.DownloadInfo;
import vn.com.fptshop.fmusic.download.entity.ThreadInfo;
import vn.com.fptshop.fmusic.download.util.FileUtils;
import vn.com.fptshop.fmusic.download.util.L;

/**
 * Created by MinhDH on 12/4/15.
 */
public class DownloadManager {

    /**
     * singleton of DownloadManager
     */
    private static DownloadManager sDownloadManager;

    private DataBaseManager mDBManager;

    /**
     * key: Tag
     * value:DownloadRequest
     */
    private Map<String, DownloadRequest> mDownloadRequestMap;

    private DownloadConfiguration mConfig;

    private ExecutorService mExecutorService;

    private DownloadStatusDelivery mDelivery;

    public static DownloadManager getInstance() {
        if (sDownloadManager == null) {
            synchronized (DownloadManager.class) {
                sDownloadManager = new DownloadManager();
            }
        }
        return sDownloadManager;
    }

    /**
     * private construction
     */
    private DownloadManager() {
        mDownloadRequestMap = new LinkedHashMap<String, DownloadRequest>();
    }

    public void init(Context context) {
        init(context, null);
    }

    public void init(Context context, DownloadConfiguration config) {
        if (config == null) {
            config = new DownloadConfiguration();
        }
        if (config.maxThreadNum <= 0) {
            //default value
            config.maxThreadNum = 10;
        }
        if (config.downloadDir == null) {
            //default value
            config.downloadDir = FileUtils.getDefaultDownloadDir(context);
        }
        mConfig = config;
        mDBManager = DataBaseManager.getInstance(context);
        mExecutorService = Executors.newFixedThreadPool(config.maxThreadNum);
        mDelivery = new DownloadStatusDeliveryImpl(new Handler(Looper.getMainLooper()));
    }

    /**
     * core method: download a file using a http/https url.
     *
     * @param fileName the file's name.
     * @param url      http or https download url
     * @param callBack {@link CallBack} of download
     */
    public void download(long length,String fileName, String url, File dir, CallBack callBack) {
        if (mConfig == null) {
            throw new RuntimeException("Please config first!");
        }
        if (TextUtils.isEmpty(fileName) || TextUtils.isEmpty(url)) {
            throw new RuntimeException("fileName or url can not be null or empty!");
        }
        final String tag = createTag(url);
        final DownloadInfo downloadInfo;
        final DownloadRequest request;
        if (mDownloadRequestMap.containsKey(tag)) {
            L.i("DownloadManager", "use cached request");
            request = mDownloadRequestMap.get(tag);
        } else {
            L.i("DownloadManager", "use new request");
            if (dir == null) {
                dir = mConfig.downloadDir;
            }
            downloadInfo = new DownloadInfo(length,fileName, url, dir);

            request = new DownloadRequest(downloadInfo, mDBManager, mExecutorService, new DownloadStatus(), mDelivery);
            mDownloadRequestMap.put(tag, request);
        }
        if (!request.isStarted()) {
            request.start(callBack);
        } else {
            L.i("DownloadManager", fileName + " : has started!");
        }
    }

    /**
     * <p>Core method: pause the downloading task.
     * <p>
     * <p>Pause the downloading task and record the progress data in database.
     * Once you invoke{@link #download(long,String, String, File, CallBack)} method again,
     * the task will be resumed automatically from where you had paused.
     *
     * @param url the url of the download task you want to pause
     */
    public void pause(String url) {
        String tag = createTag(url);
        DownloadRequest request = mDownloadRequestMap.get(tag);
        if (request != null) {
            request.pause();
        } else {
            L.i("DownloadManager", "pause " + url + " request == null");
        }
    }

    /**
     * <p>Core method: pause all downloading task
     * <p>detail see{@link #pause(String)}
     */
    public void pauseAll() {
        for (DownloadRequest request : mDownloadRequestMap.values()) {
            if (request != null && request.isStarted()) {
                request.pause();
            }
        }
    }

    /**
     * <p>Core method: cancel the download task.
     * <p>
     * <p>The difference between {@link #pause(String url)} and {@link #cancel(String url)}
     * is that {@link #cancel(String url)} release the reference of the thread task, and
     * {@link #cancel(String url)} will delete the unfinished file created in the download
     * path you have configured in {@link DownloadConfiguration#setDownloadDir(File)} and
     * delete the download progress data in database.
     * <p>
     * <p>Note: if your downloading task is connecting the server you can only invoke {@link #cancel(String url)}
     * to cancel {@link com.aspsine.multithreaddownload.core.ConnectTask} task.
     *
     * @param url the url of the download task you want to cancel
     */
    public void cancel(String url) {
        String tag = createTag(url);
        DownloadRequest request = mDownloadRequestMap.get(tag);
        if (request != null) {
            request.cancel();
        } else {
            L.i("DownloadManager", "cancel " + url + " request == null");
        }
        mDownloadRequestMap.remove(tag);
    }

    /**
     * <p>Core method: cancel all downloading task
     * <p>detail see{@link #cancel(String)}
     */
    public void cancelAll() {
        for (DownloadRequest request : mDownloadRequestMap.values()) {
            if (request != null && request.isStarted()) {
                request.cancel();
            }
        }
    }

    public DownloadInfo getDownloadProgress(String url) {
        List<ThreadInfo> threadInfos = mDBManager.getThreadInfos(url);
        DownloadInfo downloadInfo = null;
        if (!threadInfos.isEmpty()) {
            int finished = 0;
            int progress = 0;
            int total = 0;
            for (ThreadInfo info : threadInfos) {
                finished += info.getFinished();
                total += (info.getEnd() - info.getStart());
            }
            progress = (int) ((long) finished * 100 / total);
            downloadInfo = new DownloadInfo();
            downloadInfo.setFinished(finished);
            downloadInfo.setLength(total);
            downloadInfo.setProgress(progress);
        }
        return downloadInfo;
    }

    private DownloadRequest getDownloadRequest(String url) {
        return mDownloadRequestMap.get(createTag(url));
    }

    private static String createTag(String url) {
        return String.valueOf(url.hashCode());
    }

}

