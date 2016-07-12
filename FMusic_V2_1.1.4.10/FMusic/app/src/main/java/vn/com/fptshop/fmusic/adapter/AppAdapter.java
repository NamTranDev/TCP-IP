package vn.com.fptshop.fmusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.com.fptshop.fmusic.OnItemClickListener;
import vn.com.fptshop.fmusic.OnItemClickListenerApp;
import vn.com.fptshop.fmusic.R;
import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.Genre;

/**
 * Created by MinhDH on 11/27/15.
 */
public class AppAdapter extends BaseAdapter {
    private List<App> mAppInfos;
    private Context context;
    private OnItemClickListenerApp mListener;

    public AppAdapter(Context context) {
        this.mAppInfos = new ArrayList<App>();
        this.context = context;
    }

    public void setData(List<App> appInfos) {
        this.mAppInfos.clear();
        this.mAppInfos.addAll(appInfos);
    }

    public void setOnItemClickListener(OnItemClickListenerApp listener) {
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return mAppInfos.size();
    }

    @Override
    public App getItem(int position) {
        return mAppInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            holder = new ViewHolder();
            holder.appName = (TextView) convertView.findViewById(R.id.title);
            holder.appSize = (TextView) convertView.findViewById(R.id.artist);
            holder.download = (TextView) convertView.findViewById(R.id.btnDownload);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final App appInfo = mAppInfos.get(position);
        appInfo.setStatus(appInfo.getDownload());
        holder.appName.setText(appInfo.getApplicationName());
        holder.appSize.setText("Size " + humanReadableByteCount(appInfo.getFileSize(), true));
        holder.download.setText(appInfo.getButtonText());
        if (appInfo.getStatus() == App.STATUS_COMPLETE) {
            holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded_success));
            holder.download.setBackgroundResource(R.drawable.action_button_background_3);
        } else {
            holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
            holder.download.setBackgroundResource(R.drawable.action_button_background);
        }
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClickApp(v, position, appInfo);
                }
            }
        });
        return convertView;
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sb", bytes / Math.pow(unit, exp), pre);
    }

    public final static class ViewHolder {
        public TextView appName;
        public TextView appSize;
        public TextView download;
    }
}
