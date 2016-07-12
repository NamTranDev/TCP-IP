package vn.com.fptshop.fmusic.download;

import java.io.File;

/**
 * Created by MinhDH on 12/4/15.
 */
public class DownloadConfiguration {
    /**
     * dir of the download file will be stored
     */
    public File downloadDir;
    /**
     * the max num of thread that  create
     */
    public int maxThreadNum;

    /**
     * init with default value
     */
    public DownloadConfiguration() {
    }

    public DownloadConfiguration(File downloadDir, int maxThreadNum) {
        this.downloadDir = downloadDir;
        this.maxThreadNum = maxThreadNum;
    }

    public File getDownloadDir() {
        return downloadDir;
    }

    public void setDownloadDir(File downloadDir) {
        this.downloadDir = downloadDir;
    }

    public int getMaxThreadNum() {
        return maxThreadNum;
    }

    public void setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
    }
}
