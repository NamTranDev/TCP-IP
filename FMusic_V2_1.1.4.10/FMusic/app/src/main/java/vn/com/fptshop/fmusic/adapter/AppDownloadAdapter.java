package vn.com.fptshop.fmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import vn.com.fptshop.fmusic.OnItemClickListener;
import vn.com.fptshop.fmusic.R;
import vn.com.fptshop.fmusic.Utils.Utils;
import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.Song;

/**
 * Created by MinhDH on 12/8/15.
 */
public class AppDownloadAdapter extends BaseAdapter {

    private Context context;
    private List<App> data;

    private OnItemClickListener mListener;

    public AppDownloadAdapter(Context context, List<App> apps) {
        this.context = context;
        this.data = apps;
    }

    public void setData(List<App> apps) {

        this.data.addAll(apps);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_app_download, parent, false);
            holder = new ViewHolder();
            holder.appName = (TextView) convertView.findViewById(R.id.title);
            holder.appSize = (TextView) convertView.findViewById(R.id.artist);
            holder.download = (TextView) convertView.findViewById(R.id.btnDownload);
            holder.install = (TextView) convertView.findViewById(R.id.isInstall);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final App app = data.get(position);
        app.setStatus(app.getDownload());
        final int pos = position;
        holder.appName.setText(app.getApplicationName());

        holder.appSize.setText("Size " + humanReadableByteCount(app.getFileSize(), true));
//        if (Utils.isAppInstalled(context, app.getPackageName())) {
//            holder.install.setText("Installed");
//        }else{
            holder.install.setVisibility(View.GONE);
//        }

        holder.download.setText("Delete");
        if (app.getStatus() == App.STATUS_COMPLETE) {
            holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded_delete));
            holder.download.setBackgroundResource(R.drawable.action_button_background_4);
        } else {
            holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
            holder.download.setBackgroundResource(R.drawable.action_button_background);
        }
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, pos, app);
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
        public TextView install;

    }


}
