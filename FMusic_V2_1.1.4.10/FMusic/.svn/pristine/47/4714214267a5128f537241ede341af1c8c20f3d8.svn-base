package vn.com.fptshop.fmusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.com.fptshop.fmusic.IOnClickGenreDownloader;
import vn.com.fptshop.fmusic.IOnClickPlay;
import vn.com.fptshop.fmusic.OnItemClickListener;
import vn.com.fptshop.fmusic.R;
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.models.Genre;
import vn.com.fptshop.fmusic.models.Singer;
import vn.com.fptshop.fmusic.models.Song;

/**
 * Created by MinhDH on 11/27/15.
 */
public class GenreAdapter extends BaseAdapter {
    private Context context;
    private List<Genre> data;
    private IOnClickGenreDownloader mListener;
    private IOnClickPlay iOnClickPlay;
    DatabaseHandler db;
    private Activity activity;
    public ImageManager imageManager;
    public GenreAdapter(Context context, List<Genre> d) {
        this.context = context;
        data = d;
        db = new DatabaseHandler(context);
        imageManager =
                new ImageManager(context,1800000);
    }

    public void setData(List<Genre> genres) {

        this.data.addAll(genres);
    }

    public void setOnItemClickListener(IOnClickGenreDownloader listener) {
        this.mListener = listener;
    }
    public void setOnItemClickListener1(IOnClickPlay listener) {
        this.iOnClickPlay = listener;
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_genre, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.genreName = (TextView) convertView.findViewById(R.id.title);
            holder.totalSongs = (TextView) convertView.findViewById(R.id.artist);
            holder.download = (TextView) convertView.findViewById(R.id.btnDownload);
            holder.proccess = (TextView) convertView.findViewById(R.id.proccess);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.proccess.setVisibility(View.GONE);

        final Genre genre = data.get(position);
        List<Song> songs = db.getSongsLimitOfGenreDownload(genre.getGenreId());

        final int pos = position;
        if(genre.getGenreName().length()>19){
            holder.genreName.setText(genre.getGenreName().substring(0,19)+"...");
        }else{
            holder.genreName.setText(genre.getGenreName());
        }

        holder.totalSongs.setText("Đã tải " + (genre.getTotalSongs() - songs.size()) + "/" + genre.getTotalSongs() + " bài hát.");
//        holder.proccess.setText((genre.getTotalSongs() - songs.size()) + "/" + genre.getTotalSongs());
        holder.download.setText(genre.getButtonText());
        if (genre.getStatus() == Genre.STATUS_COMPLETE) {
            holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded_delete));
            holder.download.setBackgroundResource(R.drawable.action_button_background_4);
        } else {
            holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
            holder.download.setBackgroundResource(R.drawable.action_button_background);
        }
        if (holder.imageView != null) {
//            new ImageDownloaderTask(holder.imageView).execute(genre.getThumbnail());
//            holder.imageView.setImageResource(R.drawable.logo_begin);
            imageManager.displayImage(genre.getThumbnail(),  holder.imageView,R.drawable.logo_begin);
        }
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(1, v, pos, genre);
                }
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickShowDetail(1, v, pos, genre);
                }
            }
        });
        holder.genreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickShowDetail(1, v, pos, genre);
                }
            }
        });
        holder.proccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iOnClickPlay != null) {
                    iOnClickPlay.onClick(v, genre);
                }
            }
        });

        return convertView;

    }

    public final static class ViewHolder {
        public ImageView imageView;
        public TextView genreName;
        public TextView totalSongs;
        public TextView download;
        public TextView proccess;
    }
}
