package vn.com.fptshop.fmusic.download.util;

import android.util.Log;

import vn.com.fptshop.fmusic.download.Constants;

/**
 * Created by MinhDH on 12/4/15.
 */
public class L {
    private static final String TAG = "MultiThreadDownload";

    /**
     * i
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (Constants.CONFIG.DEBUG) {
            Log.i(tag, msg);
        }
    }

    /**
     * i
     * default tag fang
     * @param msg
     */
    public static void i(String msg) {
        i(TAG, msg);
    }

    /**
     * e
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (Constants.CONFIG.DEBUG) {
            Log.e(tag, msg);
        }
    }

    /**
     * e
     * default tag fang
     * @param msg
     */
    public static void e(String msg){
        e(TAG, msg);
    }

    /**
     * d
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (Constants.CONFIG.DEBUG) {
            Log.e(tag, msg);
        }
    }

    /**
     * d
     * @param msg
     */
    public static void d(String msg){
        e(TAG, msg);
    }
}

