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
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.models.Singer;
import vn.com.fptshop.fmusic.models.Song;

/**
 * Created by MinhDH on 12/17/15.
 */
public class SingerDownloaderAdapter  extends BaseAdapter {
    private Context context;
    private List<Singer> data;
    DatabaseHandler db;

    private OnItemClickListener mListener;

    public SingerDownloaderAdapter(Context context, List<Singer> d) {
        this.context = context;
        data = d;
        db = new DatabaseHandler(context);
    }

    public void setData(List<Singer> singers) {

        this.data.addAll(singers);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_singer, parent, false);
            holder = new ViewHolder();
            holder.singerName = (TextView) convertView.findViewById(R.id.title);
            holder.totalSongs = (TextView) convertView.findViewById(R.id.artist);
            holder.download = (TextView) convertView.findViewById(R.id.btnDownload);
            holder.proccess = (TextView) convertView.findViewById(R.id.proccess);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Singer singer = data.get(position);
        List<Song> songs = db.getSongsLimitOfSingerDownload(singer.getSingerId());
        final int pos = position;
        holder.singerName.setText(singer.getSingerName());
        holder.totalSongs.setText("Có " + singer.getTotalSongs() + " bài hát.");
        holder.proccess.setText((singer.getTotalSongs() - songs.size()) + "/" + singer.getTotalSongs());

        if (songs.size() == 0) {
            singer.setStatus(Singer.STATUS_COMPLETE);
        }
        holder.download.setText(singer.getButtonText());

        if (singer.getStatus() == Singer.STATUS_COMPLETE) {
            holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded_success));
            holder.download.setBackgroundResource(R.drawable.action_button_background_3);
        } else {
            holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
            holder.download.setBackgroundResource(R.drawable.action_button_background);
        }
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("TEST CLICK");
                if (mListener != null) {
                    mListener.onItemClick(v, pos, singer);
                }
            }
        });
        return convertView;
    }

    public final static class ViewHolder {
        public TextView singerName;
        public TextView totalSongs;
        public TextView download;
        public TextView proccess;
    }
}
