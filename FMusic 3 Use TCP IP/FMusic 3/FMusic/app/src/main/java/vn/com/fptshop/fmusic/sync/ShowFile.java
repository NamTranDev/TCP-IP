package vn.com.fptshop.fmusic.sync;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import vn.com.fptshop.fmusic.AppSetting;

/**
 * Created by Nam on 1/11/2016.
 */
public class ShowFile {
    Context mContext;

    public ShowFile(Context mContext) {
        this.mContext = mContext;
    }

    public void DisplaySync(String filename ,int currentfile,int sizefile)
    {
        Intent intent = new Intent("addContactSuccess");
        intent.putExtra("ContactManager", "Success");
        intent.putExtra("filename", true);
        AppSetting.proccess = filename + "\n" +"\n" +"Đồng bộ " + currentfile + "/" + sizefile + " File";
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
