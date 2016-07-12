package vn.com.fptshop.fmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import vn.com.fptshop.fmusic.OnItemClickListener;
import vn.com.fptshop.fmusic.R;
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.AppCombo;
import vn.com.fptshop.fmusic.models.Singer;

/**
 * Created by MinhDH on 12/3/15.
 */
public class AppComboAdapter extends BaseAdapter {

    private Context context;
    private List<AppCombo> data;

    private OnItemClickListener mListener;

    DatabaseHandler db;

    public AppComboAdapter(Context context, List<AppCombo> d) {
        this.context = context;
        data = d;
        db = new DatabaseHandler(context);
    }

    public void setData(List<AppCombo> appCombos) {

        this.data.addAll(appCombos);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_appcombo, parent, false);
            holder = new ViewHolder();
            holder.appName = (TextView) convertView.findViewById(R.id.title);
            holder.appTotal = (TextView) convertView.findViewById(R.id.artist);
            holder.download = (TextView) convertView.findViewById(R.id.btnDownload);
            holder.proccess = (TextView) convertView.findViewById(R.id.proccess);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.proccess.setVisibility(View.GONE);
        final AppCombo appCombo = data.get(position);
//        List<App> appList = db.getAppsDownload(appCombo.getAppComboId());
        final int pos = position;
        holder.appName.setText(appCombo.getAppComboName());
        holder.appTotal.setText("Có " + appCombo.getAppsCount() + " ứng dụng.");
//        holder.proccess.setText((appCombo.getAppsCount() - appList.size()) + "/" + appCombo.getAppsCount());

        holder.download.setText(appCombo.getButtonText());
        if (appCombo.getStatus() == AppCombo.STATUS_COMPLETE) {
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
                    mListener.onItemClick(v, pos, appCombo);
                }
            }
        });
        return convertView;
    }

    public final static class ViewHolder {

        public TextView appName;

        public TextView appTotal;

        public TextView download;

        public TextView proccess;

    }

}
