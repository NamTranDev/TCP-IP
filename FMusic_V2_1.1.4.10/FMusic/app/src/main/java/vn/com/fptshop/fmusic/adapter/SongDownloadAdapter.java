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
import vn.com.fptshop.fmusic.models.Song;

/**
 * Created by MinhDH on 12/7/15.
 */
public class SongDownloadAdapter extends BaseAdapter {

    private Context context;
    private List<Song> data;

    private OnItemClickListener mListener;

    public SongDownloadAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.data = songs;
    }

    public void setData(List<Song> songList) {

        this.data.addAll(songList);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            holder = new ViewHolder();
            holder.songName = (TextView) convertView.findViewById(R.id.title);
            holder.singerName = (TextView) convertView.findViewById(R.id.artist);
            holder.download = (TextView) convertView.findViewById(R.id.btnDownload);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Song song = data.get(position);
        song.setStatus(song.getDownload());
        final int pos = position;
        holder.songName.setText(song.getSongName());

        if (song.getSingerName() != null) {
            holder.singerName.setText(song.getSingerName());
        } else {
            holder.singerName.setText("Đang cập nhật...");
        }

        holder.download.setText("Delete");
        if (song.getStatus() == Song.STATUS_COMPLETE) {
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
                    mListener.onItemClick(v, pos, song);
                }
            }
        });

        return convertView;
    }

    public final static class ViewHolder {
        public TextView songName;
        public TextView singerName;
        public TextView download;
    }


}
