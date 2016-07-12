package vn.com.fptshop.fmusic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import vn.com.fptshop.fmusic.AppSetting;
import vn.com.fptshop.fmusic.DataDownloader;
import vn.com.fptshop.fmusic.IOnClickAppDownloader;
import vn.com.fptshop.fmusic.IOnClickGenreDownloader;
import vn.com.fptshop.fmusic.MainActivity;
import vn.com.fptshop.fmusic.OnItemClickListener;
import vn.com.fptshop.fmusic.R;
import vn.com.fptshop.fmusic.Utils.Utils;
import vn.com.fptshop.fmusic.adapter.AppAdapter;
import vn.com.fptshop.fmusic.adapter.AppComboAdapter;
import vn.com.fptshop.fmusic.adapter.AppComboDownloadAdapter;
import vn.com.fptshop.fmusic.adapter.AppDownloadAdapter;
import vn.com.fptshop.fmusic.adapter.AppDownloaderAdapter;
import vn.com.fptshop.fmusic.adapter.GenreAdapter;
import vn.com.fptshop.fmusic.adapter.GenreDownloaderAdapter;
import vn.com.fptshop.fmusic.adapter.SingerAdapter;
import vn.com.fptshop.fmusic.adapter.SingerDownloaderAdapter;
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.download.CallBack;
import vn.com.fptshop.fmusic.download.DownloadManager;
import vn.com.fptshop.fmusic.download.core.DownloadException;
import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.AppCombo;
import vn.com.fptshop.fmusic.models.Genre;
import vn.com.fptshop.fmusic.models.Singer;
import vn.com.fptshop.fmusic.models.Song;

/**
 * Created by MinhDH on 12/14/15.
 */
public class FragmentDownloader extends Fragment implements OnItemClickListener<Singer>, IOnClickGenreDownloader, IOnClickAppDownloader {
    static DatabaseHandler db;
    static Context context;

    //SingerDownloader
    static ListView listViewSinger;
    static SingerDownloaderAdapter singerAdapter;
    public static HashMap<Integer, List<Song>> listDownloadTemp;
    public static HashMap<Integer, Song> songDownloadTemp;

    //GenreDownloader
    static ListView listViewGenre;
    static GenreDownloaderAdapter genreAdapter;
    public static HashMap<Integer, List<Song>> listDownloadTempGenre;
    public static HashMap<Integer, Song> songDownloadTempGenre;

    //AppComboDownloader
    static ListView listViewAppCombo;
    static AppComboDownloadAdapter appComboAdapter;
    public static HashMap<Integer, List<App>> listDownloadTempAppCombo;
    public static HashMap<Integer, App> appDownloadTempAppCombo;
    public static HashMap<Integer, List<App>> listInstallTemp = new HashMap<>();
    public static android.app.DownloadManager downloadanager;

    //AppDownloader
    static ListView listViewApp;
    static AppDownloaderAdapter appAdapter;
    public static HashMap<Integer, App> appDownloadTempApp;

    public FragmentDownloader() {

        listDownloadTemp = new HashMap<>();
        songDownloadTemp = new HashMap<>();
        listDownloadTempGenre = new HashMap<>();
        songDownloadTempGenre = new HashMap<>();
        listDownloadTempAppCombo = new HashMap<>();
        appDownloadTempAppCombo = new HashMap<>();
        appDownloadTempApp = new HashMap<>();

        db = new DatabaseHandler(getActivity());
        context = getActivity();
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getActivity());
        context = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_downloader, container, false);
        //SingerDownloader

//        getActivity().registerReceiver(onComplete,
//                new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//        String servicestring = Context.DOWNLOAD_SERVICE;
//
//        downloadanager = (android.app.DownloadManager) getActivity().getSystemService(servicestring);

        listViewSinger = (ListView) rootView.findViewById(R.id.listViewSinger);
        singerAdapter = new SingerDownloaderAdapter(getActivity(), DataDownloader.singerList);
        singerAdapter.setOnItemClickListener(this);
        listViewSinger.setAdapter(singerAdapter);

        listViewGenre = (ListView) rootView.findViewById(R.id.listViewGenre);
        genreAdapter = new GenreDownloaderAdapter(getActivity(), DataDownloader.genreList);
        genreAdapter.setOnItemClickListener(this);
        listViewGenre.setAdapter(genreAdapter);

        //GenreDownloader
        listViewAppCombo = (ListView) rootView.findViewById(R.id.listViewAppCombo);
        appComboAdapter = new AppComboDownloadAdapter(getActivity(), DataDownloader.appComboList);
        appComboAdapter.setOnItemClickListener(this);
        listViewAppCombo.setAdapter(appComboAdapter);

        //AppDownloader
        listViewApp = (ListView) rootView.findViewById(R.id.listViewApp);
        appAdapter = new AppDownloaderAdapter(getActivity(), DataDownloader.appList);
        appAdapter.setOnItemClickListener(this);
        listViewApp.setAdapter(appAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("resum");
    }

    public static void updateList() {
        singerAdapter.notifyDataSetChanged();
    }

    public static void updateListGenre() {
        genreAdapter.notifyDataSetChanged();
    }

    //SingerDownloader
    public static void downloadSinger(int position, Singer singer) {
        if (!dir.exists())
            dir.mkdirs();
        if (listDownloadTemp.containsKey(position)) {
            Song song = songDownloadTemp.get(position);
            if (song != null)
                DownloadManager.getInstance().pause("http://118.69.201.53:8887/api/Music/Download?songId=" + song.getSongId());

            listDownloadTemp.remove(position);
            songDownloadTemp.remove(position);
        } else {
            System.out.println("chua co sd");
            List<Song> songs = db.getSongsLimitOfSingerDownload(singer.getSingerId());
            listDownloadTemp.put(position, songs);
            List<Song> songsTemp = listDownloadTemp.get(position);
            if (songsTemp.size() > 0) {
                System.out.println("Length: " + songsTemp.size());
                download(position, singer);
            }
        }
    }

    static final File dir = new File(Environment.getExternalStorageDirectory(), "FPTShop/Music");
    static final File dir1 = new File(Environment.getExternalStorageDirectory(), "FPTShop/App");

    public static void download(final int position, final Singer singer) {

        if (listDownloadTemp.containsKey(position)) {
            List<Song> songsTemp = listDownloadTemp.get(position);
            if (songsTemp.size() > 0) {
                Song song = songsTemp.get(0);
                songsTemp.remove(0);
                listDownloadTemp.put(position, songsTemp);
                System.out.println("Length: " + songsTemp.size());
                downloader(position, song, singer);
            } else {
                listDownloadTemp.remove(position);
            }
        }
    }

    static void downloader(final int position, final Song song, final Singer singer) {

        System.out.println("Size: " + listDownloadTemp.get(position).size());

        DownloadManager.getInstance().download(song.getFileSize(), song.getSongName() + ".mp3", AppSetting.URL + "/api/Music/Download?songId=" + song.getSongId(), dir, new CallBack() {


            @Override
            public void onDownloadStart() {
                System.out.println("test download");
                song.setStatus(Song.STATUS_CONNECTING);
                db.updateSong(song, Song.STATUS_CONNECTING, "");
                songDownloadTemp.put(position, song);

//                listDownloadTemp.put(position,song);
                if (isCurrentListViewItemVisible(position)) {
                    SingerDownloaderAdapter.ViewHolder holder = getViewHolder(position);
                    if (listDownloadTemp.get(position) != null) {
                        holder.proccess.setText((singer.getTotalSongs() - (listDownloadTemp.get(position).size() + 1)) + "/" + singer.getTotalSongs());
                    } else {
                        holder.proccess.setText((singer.getTotalSongs() - 1) + "/" + singer.getTotalSongs());
                    }
//  holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.btnDownload.setText(appInfo.getButtonText());
                }
            }

            @Override
            public void onConnected(long total, boolean isRangeSupport) {
                song.setStatus(Song.STATUS_DOWNLOADING);
                db.updateSong(song, Song.STATUS_DOWNLOADING, "");
                if (isCurrentListViewItemVisible(position)) {
                    SingerDownloaderAdapter.ViewHolder holder = getViewHolder(position);
                    holder.download.setText("Tải...");
                }
            }

            @Override
            public void onProgress(long finished, long total, int progress) {
                String downloadPerSize = getDownloadPerSize(finished, total);
//                System.out.println(progress);
//                appInfo.setProgress(progress);
//                appInfo.setDownloadPerSize(downloadPerSize);
                song.setStatus(Song.STATUS_DOWNLOADING);

                if (isCurrentListViewItemVisible(position)) {
                    SingerDownloaderAdapter.ViewHolder holder = getViewHolder(position);
                    holder.download.setText("Huỷ");
                    if (singer != null && song!=null && listDownloadTemp.get(position) != null && holder.proccess!=null){
                        holder.proccess.setText("" + (singer.getTotalSongs() - (listDownloadTemp.get(position).size() + 1)) + "/" + singer.getTotalSongs() + ": " + song.getSongName() + " (" + progress + "%)");
                    }
//                    holder.progressBar.setProgress(progress);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.btnDownload.setText(appInfo.getButtonText());
                }
            }

            public boolean isDownload = false;

            @Override
            public void onComplete() {
                song.setStatus(Song.STATUS_COMPLETE);
                song.setDownload(Song.STATUS_COMPLETE);
                db.updateSong(song, Song.STATUS_COMPLETE, dir + "/" + song.getSongName() + ".mp3");

                if (listDownloadTemp.containsKey(position)) {
                    List<Song> songsTemp = listDownloadTemp.get(position);
                    if (songsTemp.size() == 0 && listDownloadTemp.containsKey(position)) {
                        listDownloadTemp.remove(position);
                        System.out.println("Xoa ra khoi hashmap");
                    }
                    if (songsTemp.size() > 0 && isDownload == false) {
                        Song song = songsTemp.get(0);
                        songsTemp.remove(0);
                        listDownloadTemp.put(position, songsTemp);
                        isDownload = true;
                        downloader(position, song, singer);
                    }
                } else {
                    if (isCurrentListViewItemVisible(position) && isDownload == false) {

                        SingerDownloaderAdapter.ViewHolder holder = getViewHolder(position);
                        holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded_success));
                        holder.download.setBackgroundResource(R.drawable.action_button_background_3);
                        holder.download.setText(song.getButtonText());
                        if (listDownloadTemp.get(position) != null) {
                            holder.proccess.setText("Đã tải " +(singer.getTotalSongs() - (listDownloadTemp.get(position).size() + 1)) + "/" + singer.getTotalSongs());
                        } else {
                            holder.proccess.setText("Đã tải " +db.getCountSongSingerDownload(singer.getSingerId()) + "/" + singer.getTotalSongs());
                        }
                    }
                }

            }

            @Override
            public void onDownloadPause() {
                song.setStatus(Song.STATUS_PAUSE);
                System.out.println("TEST PASE");
                db.updateSong(song, Song.STATUS_PAUSE, "");
                File aa = new File(dir, song.getSongName() + ".mp3");
                if (aa.exists()) {
                    aa.delete();
                    System.out.println("[DELETE] Success: " + song.getSongName());
                } else {
                    System.out.println("[DELETE] File not exists.");
                }
                if (isCurrentListViewItemVisible(position)) {
                    SingerDownloaderAdapter.ViewHolder holder = getViewHolder(position);
                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
                    holder.download.setBackgroundResource(R.drawable.action_button_background);
                    holder.download.setText(song.getButtonText());
                }
            }

            @Override
            public void onDownloadCancel() {
                song.setStatus(Song.STATUS_NOT_DOWNLOAD);
                song.setDownload(Song.STATUS_NOT_DOWNLOAD);
                System.out.println("TEST CAN");
//                if (isCurrentListViewItemVisible(position)) {
//                    SongAdapter.ViewHolder holder = getViewHolder(position);
//                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
//                    holder.download.setBackgroundResource(R.drawable.action_button_background_2);
//                    holder.download.setText(song.getButtonText());
//                }
            }

            @Override
            public void onFailure(DownloadException e) {
                song.setStatus(Song.STATUS_DOWNLOAD_ERROR);

//                if (isCurrentListViewItemVisible(position)) {
//                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.tvDownloadPerSize.setText("");
//                    holder.btnDownload.setText(appInfo.getButtonText());
//                }
                e.printStackTrace();
            }
        });
    }

    static boolean isCurrentListViewItemVisible(int position) {
        int first = listViewSinger.getFirstVisiblePosition();
        int last = listViewSinger.getLastVisiblePosition();
        return first <= position && position <= last;
    }

    static SingerDownloaderAdapter.ViewHolder getViewHolder(int position) {
        int childPosition = position - listViewSinger.getFirstVisiblePosition();
        View view = listViewSinger.getChildAt(childPosition);
        return (SingerDownloaderAdapter.ViewHolder) view.getTag();
    }

    static final DecimalFormat DF = new DecimalFormat("0.00");

    static String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "M/" + DF.format((float) total / (1024 * 1024)) + "M";
    }
//GenreDownloader

    @Override
    public void onItemClick(View v, int position, Singer singer) {
        downloadSinger(position, singer);

        System.out.println("DUNG LAI");

        if (isCurrentListViewItemVisible(position)) {
            SingerDownloaderAdapter.ViewHolder holder = getViewHolder(position);
            holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
            holder.download.setBackgroundResource(R.drawable.action_button_background);
            holder.download.setText("Tải lại");
        }
//        songStack.clear();
//        downloadanager.remove(currentDownload);
    }


    @Override
    public void onClickShowDetail(int key, View v, int position, Genre genre) {

    }

    public static Stack<Song> songStack = new Stack<>();

    public static long currentDownload = 0;

    public static void downloadStack() {
        if (!DataDownloader.singerIdStack.empty()) {
            int id = DataDownloader.singerIdStack.pop();
            List<Song> songList = db.getSongsLimitOfSingerDownload(id);
            songStack.addAll(songList);
            Singer singer = db.getSinger(id);
            DataDownloader.singerList.add(singer);
            singerAdapter.notifyDataSetChanged();
            startDownload();
        } else {

        }
    }

    public static void startDownload() {
        if (!songStack.empty()) {
            Song song = songStack.pop();
            Uri uri = Uri
                    .parse("http://118.69.201.53:8887/api/Music/Download?songId=" + song.getSongId());
            Environment
                    .getExternalStoragePublicDirectory("FPTShop/Music")
                    .mkdirs();
            currentDownload =
                    downloadanager.enqueue(new android.app.DownloadManager.Request(uri)
                            .setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI |
                                    android.app.DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle(song.getSongName())
                            .setDestinationInExternalPublicDir("FPTShop/Music",
                                    song.getSongName() + ".mp3"));

        } else {

        }
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
//            findViewById(R.id.start).setEnabled(true);
//            Toast.makeText(ctxt, "KAKA...hi!", Toast.LENGTH_LONG).show();
            System.out.println("DOWNLOAD SUCCESS ");

            startDownload();
        }
    };

    public static void downloadGenre(int position, Genre genre) {
        if (!dir.exists())
            dir.mkdirs();
        if (listDownloadTempGenre.containsKey(position)) {
            Song song = songDownloadTempGenre.get(position);
            if (song != null) {
                try {
                    DownloadManager.getInstance().pause("http://118.69.201.53:8887/api/Music/Download?songId=" + song.getSongId());
                } catch (Exception e) {
                    System.out.println("LOI PAUSE");
                }
            }

            listDownloadTempGenre.remove(position);
            songDownloadTempGenre.remove(position);
        } else {
            System.out.println("chua co sd");
            List<Song> songs = db.getSongsLimitOfGenreDownload(genre.getGenreId());
            listDownloadTempGenre.put(position, songs);
            List<Song> songsTemp = listDownloadTempGenre.get(position);
            if (songsTemp.size() > 0) {
                System.out.println("Length: " + songsTemp.size());
                download(position, genre);
            }
        }
    }

    public static void download(final int position, final Genre genre) {

        if (listDownloadTempGenre.containsKey(position)) {
            List<Song> songsTemp = listDownloadTempGenre.get(position);
            if (songsTemp.size() > 0) {
                Song song = songsTemp.get(0);
                songsTemp.remove(0);
                listDownloadTempGenre.put(position, songsTemp);
                System.out.println("Length: " + songsTemp.size());
                downloader(position, song, genre);
            } else {
                listDownloadTempGenre.remove(position);
            }
        }
    }

    static void downloader(final int position, final Song song, final Genre genre) {

        System.out.println("Size: " + listDownloadTempGenre.get(position).size());

        DownloadManager.getInstance().download(song.getFileSize(), song.getSongName() + ".mp3", "http://118.69.201.53:8887/api/Music/Download?songId=" + song.getSongId(), dir, new CallBack() {


            @Override
            public void onDownloadStart() {
                System.out.println("test download");
                song.setStatus(Song.STATUS_CONNECTING);
                db.updateSong(song, Song.STATUS_CONNECTING, "");
                songDownloadTempGenre.put(position, song);

//                listDownloadTemp.put(position,song);
                if (isCurrentListViewItemVisibleGenre(position)) {
                    GenreDownloaderAdapter.ViewHolder holder = getViewHolderGenre(position);
                    if (listDownloadTempGenre.get(position) != null) {
                        holder.proccess.setText((genre.getTotalSongs() - (listDownloadTempGenre.get(position).size() + 1)) + "/" + genre.getTotalSongs());
                    } else {
                        holder.proccess.setText((genre.getTotalSongs() - 1) + "/" + genre.getTotalSongs());
                    }
//  holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.btnDownload.setText(appInfo.getButtonText());
                }
            }

            @Override
            public void onConnected(long total, boolean isRangeSupport) {
                song.setStatus(Song.STATUS_DOWNLOADING);
                db.updateSong(song, Song.STATUS_DOWNLOADING, "");
                if (isCurrentListViewItemVisibleGenre(position)) {
                    GenreDownloaderAdapter.ViewHolder holder = getViewHolderGenre(position);
                    holder.download.setText("Tải...");
                }
            }

            @Override
            public void onProgress(long finished, long total, int progress) {
                String downloadPerSize = getDownloadPerSize(finished, total);

                song.setStatus(Song.STATUS_DOWNLOADING);

                if (isCurrentListViewItemVisibleGenre(position)) {
                    GenreDownloaderAdapter.ViewHolder holder = getViewHolderGenre(position);
                    holder.download.setText("Huỷ ");


                    if (genre != null && song!=null && listDownloadTempGenre.get(position) != null && holder.proccess!=null){
                        holder.proccess.setText("" + (genre.getTotalSongs() - (listDownloadTempGenre.get(position).size() + 1)) + "/" + genre.getTotalSongs() + ": " + song.getSongName() + " (" + progress + "%)");
                    }
//                    else{
//
//                    }
//                    holder.progressBar.setProgress(progress);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.btnDownload.setText(appInfo.getButtonText());
                }
            }

            public boolean isDownloadGenre = false;

            @Override
            public void onComplete() {
                song.setStatus(Song.STATUS_COMPLETE);
                song.setDownload(Song.STATUS_COMPLETE);
                db.updateSong(song, Song.STATUS_COMPLETE, dir + "/" + song.getSongName() + ".mp3");

                if (listDownloadTempGenre.containsKey(position)) {
                    List<Song> songsTemp = listDownloadTempGenre.get(position);
                    if (songsTemp.size() == 0 && listDownloadTempGenre.containsKey(position)) {
                        listDownloadTempGenre.remove(position);
                        System.out.println("Xoa ra khoi hashmap");
                    }

                    if (songsTemp.size() > 0 && isDownloadGenre == false) {
                        Song song = songsTemp.get(0);
                        songsTemp.remove(0);
                        listDownloadTempGenre.put(position, songsTemp);
                        isDownloadGenre = true;
                        downloader(position, song, genre);
                    }
                } else {

                    if (isCurrentListViewItemVisibleGenre(position) &&isDownloadGenre == false) {
                        System.out.println("INT RA");
                        GenreDownloaderAdapter.ViewHolder holder = getViewHolderGenre(position);
                        holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded_success));
                        holder.download.setBackgroundResource(R.drawable.action_button_background_3);
                        holder.download.setText(song.getButtonText());
                        if (listDownloadTempGenre.get(position) != null) {
                            holder.proccess.setText((genre.getTotalSongs() - (listDownloadTempGenre.get(position).size() + 1)) + "/" + genre.getTotalSongs());
                        } else {
                            holder.proccess.setText("Đã tải " + db.getCountSongGenreDownload(genre.getGenreId()) + "/" + genre.getTotalSongs());
                        }
                    }
                }

            }

            @Override
            public void onDownloadPause() {
                song.setStatus(Song.STATUS_PAUSE);
                System.out.println("TEST PASE");
                db.updateSong(song, Song.STATUS_PAUSE, "");
                File aa = new File(dir, song.getSongName() + ".mp3");
                if (aa.exists()) {
                    aa.delete();
                    System.out.println("[DELETE] Success: " + song.getSongName());
                } else {
                    System.out.println("[DELETE] File not exists.");
                }
                if (isCurrentListViewItemVisibleGenre(position)) {
                    GenreDownloaderAdapter.ViewHolder holder = getViewHolderGenre(position);
                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
                    holder.download.setBackgroundResource(R.drawable.action_button_background);
                    holder.download.setText(song.getButtonText());
                }
            }

            @Override
            public void onDownloadCancel() {
                song.setStatus(Song.STATUS_NOT_DOWNLOAD);
                song.setDownload(Song.STATUS_NOT_DOWNLOAD);
                System.out.println("TEST CAN");
//                if (isCurrentListViewItemVisible(position)) {
//                    SongAdapter.ViewHolder holder = getViewHolder(position);
//                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
//                    holder.download.setBackgroundResource(R.drawable.action_button_background_2);
//                    holder.download.setText(song.getButtonText());
//                }
            }

            @Override
            public void onFailure(DownloadException e) {
                song.setStatus(Song.STATUS_DOWNLOAD_ERROR);

//                if (isCurrentListViewItemVisible(position)) {
//                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.tvDownloadPerSize.setText("");
//                    holder.btnDownload.setText(appInfo.getButtonText());
//                }
                e.printStackTrace();
            }
        });
    }

    static boolean isCurrentListViewItemVisibleGenre(int position) {
        int first = listViewGenre.getFirstVisiblePosition();
        int last = listViewGenre.getLastVisiblePosition();
        return first <= position && position <= last;
    }

    static GenreDownloaderAdapter.ViewHolder getViewHolderGenre(int position) {
        int childPosition = position - listViewGenre.getFirstVisiblePosition();
        View view = listViewGenre.getChildAt(childPosition);
        return (GenreDownloaderAdapter.ViewHolder) view.getTag();
    }

    @Override
    public void onClick(int key, View v, int position, Genre genre) {
        downloadGenre(position, genre);
        System.out.println("DUNG LAI GENRE");

    }

    public static void updateListAppCombo() {
        appComboAdapter.notifyDataSetChanged();
    }

    public static void downloadAppCombo(int position, AppCombo appCombo) {
        if (!dir1.exists())
            dir1.mkdirs();
        if (listDownloadTempAppCombo.containsKey(position)) {
            App app = appDownloadTempAppCombo.get(position);
            if (app != null)
                DownloadManager.getInstance().pause("http://118.69.201.53:8887/api/Application/Download?ApplicationId=" + app.getApplicationId());
            listDownloadTempAppCombo.remove(position);
            appDownloadTempAppCombo.remove(position);
        } else {
            System.out.println("chua co sd");
            List<App> apps = db.getAppsDownload(appCombo.getAppComboId());
            listDownloadTempAppCombo.put(position, apps);
            List<App> appList = listDownloadTempAppCombo.get(position);
            if (appList.size() > 0) {
                System.out.println("Length: " + appList.size());
                download(position, appCombo);
            }
        }
    }

    @Override
    public void onItemClickAppCombo(View v, int pos, AppCombo appCombo) {
        downloadAppCombo(pos, appCombo);
        System.out.println("DUNG LAI DOWNLOAD");
    }


    public static void download(final int position, final AppCombo appCombo) {

        if (listDownloadTempAppCombo.containsKey(position)) {
            List<App> apps = listDownloadTempAppCombo.get(position);
            if (apps.size() > 0) {
                App app = apps.get(0);
                apps.remove(0);
                listDownloadTempAppCombo.put(position, apps);
                System.out.println("Length: " + apps.size());
                downloader(position, app, appCombo);
            } else {
                listDownloadTempAppCombo.remove(position);
            }
        }
    }

    public static void downloader(final int position, final App app, final AppCombo appCombo) {

        System.out.println("Size: " + listDownloadTempAppCombo.get(position).size());

        DownloadManager.getInstance().download(app.getFileSize(), app.getApplicationName() + ".apk", "http://118.69.201.53:8887/api/Application/Download?ApplicationId=" + app.getApplicationId(), dir1, new CallBack() {


                    @Override
                    public void onDownloadStart() {

                        System.out.println("test download");
                        app.setStatus(App.STATUS_CONNECTING);
                        db.updateApp(app, App.STATUS_CONNECTING, "");
                        appDownloadTempAppCombo.put(position, app);
//                listDownloadTemp.put(position,song);
//                if (isCurrentListViewItemVisible(position)) {
//                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.btnDownload.setText(appInfo.getButtonText());
//                }
                    }

                    @Override
                    public void onConnected(long total, boolean isRangeSupport) {
                        app.setStatus(Song.STATUS_DOWNLOADING);
                        db.updateApp(app, Song.STATUS_DOWNLOADING, "");
                        if (isCurrentListViewItemVisibleAppCombo(position)) {
                            AppComboDownloadAdapter.ViewHolder holder = getViewHolderAppCombo(position);
                            holder.download.setText("Tải...");
                        }
                    }

                    @Override
                    public void onProgress(long finished, long total, int progress) {
//                        String downloadPerSize = getDownloadPerSize(finished, total);
//                System.out.println(progress);
//                appInfo.setProgress(progress);
//                appInfo.setDownloadPerSize(downloadPerSize);
                        app.setStatus(Song.STATUS_DOWNLOADING);

                        if (isCurrentListViewItemVisibleAppCombo(position)) {
                            AppComboDownloadAdapter.ViewHolder holder = getViewHolderAppCombo(position);
                            holder.download.setText("Huỷ");
                            if (appCombo != null && app!=null && listDownloadTempAppCombo.get(position) != null && holder.proccess!=null){
                                holder.proccess.setText("" + (appCombo.getAppsCount() - (listDownloadTempAppCombo.get(position).size() + 1)) + "/" + appCombo.getAppsCount() + ": " + app.getApplicationName() + " (" + progress + "%)");
                            }
//                    holder.progressBar.setProgress(progress);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.btnDownload.setText(appInfo.getButtonText());
                        }
                    }

                    public boolean isDownload = false;

                    @Override
                    public void onComplete() {
                        app.setStatus(Song.STATUS_COMPLETE);
                        app.setDownload(Song.STATUS_COMPLETE);
                        db.updateApp(app, Song.STATUS_COMPLETE, dir1 + "/" + app.getApplicationName() + ".apk");


                        if (listDownloadTempAppCombo.containsKey(position)) {
                            List<App> songsTemp = listDownloadTempAppCombo.get(position);
                            int count = songsTemp.size();
                            if (songsTemp.size() == 0 && listDownloadTempAppCombo.containsKey(position)) {
                                listDownloadTempAppCombo.remove(position);
                                System.out.println("Xoa ra khoi hashmap");
                            }
                            if (songsTemp.size() > 0 && isDownload == false) {
                                App app1 = songsTemp.get(0);
                                songsTemp.remove(0);
                                listDownloadTempAppCombo.put(position, songsTemp);
                                isDownload = true;
                                if (isCurrentListViewItemVisibleAppCombo(position)) {
                                    AppComboDownloadAdapter.ViewHolder holder = getViewHolderAppCombo(position);
                                    holder.proccess.setText((appCombo.getAppsCount() - count) + "/" + appCombo.getAppsCount());
                                }
                                List<App> list = listInstallTemp.get(position);
                                if (list != null) {
                                    list.add(app1);
                                } else {
                                    list = new ArrayList<App>();
                                    list.add(app);
                                    list.add(app1);
                                }
                                listInstallTemp.put(position, list);
                                downloader(position, app1, appCombo);
                            }
                        } else {
                            System.out.println("DOWNLOAD XONG");
                            List<App> list = listInstallTemp.get(position);
                            if (list != null && isDownload == false) {
                                for (int i = 0; i < list.size(); i++) {
                                    App app1 = list.get(i);
                                    File apk = new File(dir1, app1.getApplicationName() + ".apk");
                                    System.out.println("pack " + apk.getAbsolutePath());
                                    if (apk.isFile() && apk.exists()) {
                                        String packageName = Utils.getApkFilePackage(context, apk);
                                        System.out.println("pack " + packageName);
                                        Utils.installApp(context, apk);
                                        if (Utils.isAppInstalled(context, packageName)) {
                                            app.setStatus(App.STATUS_INSTALLED);
                                        }
                                    }
                                }
                                listInstallTemp.remove(position);
                            }
                            if (isCurrentListViewItemVisibleAppCombo(position) && isDownload == false) {
                                AppComboDownloadAdapter.ViewHolder holder = getViewHolderAppCombo(position);
                                holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded_success));
                                holder.download.setBackgroundResource(R.drawable.action_button_background_3);
                                holder.download.setText(app.getButtonText());
                                holder.proccess.setText("Đã tải " + db.getCountAppDownload(appCombo.getAppComboId()) + "/" + appCombo.getAppsCount());
                            }
                        }

                    }

                    boolean isPause = false;

                    @Override
                    public void onDownloadPause() {
                        app.setStatus(Song.STATUS_PAUSE);
                        if (isPause == false) {
                            System.out.println("TEST PASE");
                            List<App> list = listInstallTemp.get(position);
                            if (list != null && isDownload == false) {
                                for (int i = 0; i < list.size(); i++) {
                                    App app1 = list.get(i);
                                    File apk = new File(dir1, app1.getApplicationName() + ".apk");
                                    System.out.println("pack " + apk.getAbsolutePath());
                                    if (apk.length() == app1.getFileSize())
                                        if (apk.isFile() && apk.exists()) {
                                            String packageName = Utils.getApkFilePackage(context, apk);
                                            System.out.println("pack " + packageName);
                                            Utils.installApp(context, apk);
                                            if (Utils.isAppInstalled(context, packageName)) {
                                                app.setStatus(App.STATUS_INSTALLED);
                                            }
                                        }

                                }
                                listInstallTemp.remove(position);
                            }
                            isPause = !isPause;
                        }

                        db.updateApp(app, Song.STATUS_PAUSE, "");

                        File aa = new File(dir1, app.getApplicationName() + ".apk");
                        if (aa.exists()) {
                            aa.delete();
                            System.out.println("[DELETE] Success: " + app.getApplicationName());
                        } else {
                            System.out.println("[DELETE] File not exists.");
                        }
                        if (
                                isCurrentListViewItemVisibleAppCombo(position)
                                ) {
                            AppComboDownloadAdapter.ViewHolder holder = getViewHolderAppCombo(position);
                            holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
                            holder.download.setBackgroundResource(R.drawable.action_button_background);
                            holder.download.setText(app.getButtonText());
                        }
                    }

                    @Override
                    public void onDownloadCancel() {
                        app.setStatus(Song.STATUS_NOT_DOWNLOAD);
                        app.setDownload(Song.STATUS_NOT_DOWNLOAD);
                        System.out.println("TEST CAN");
//                if (isCurrentListViewItemVisible(position)) {
//                    SongAdapter.ViewHolder holder = getViewHolder(position);
//                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
//                    holder.download.setBackgroundResource(R.drawable.action_button_background_2);
//                    holder.download.setText(song.getButtonText());
//                }
                    }

                    @Override
                    public void onFailure(DownloadException e) {
                        app.setStatus(Song.STATUS_DOWNLOAD_ERROR);

//                if (isCurrentListViewItemVisible(position)) {
//                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.tvDownloadPerSize.setText("");
//                    holder.btnDownload.setText(appInfo.getButtonText());
//                }
                        e.printStackTrace();
                    }
                }

        );
    }

    static boolean isCurrentListViewItemVisibleAppCombo(int position) {
        int first = listViewAppCombo.getFirstVisiblePosition();
        int last = listViewAppCombo.getLastVisiblePosition();
        return first <= position && position <= last;
    }

    static AppComboDownloadAdapter.ViewHolder getViewHolderAppCombo(int position) {
        int childPosition = position - listViewAppCombo.getFirstVisiblePosition();
        View view = listViewAppCombo.getChildAt(childPosition);
        return (AppComboDownloadAdapter.ViewHolder) view.getTag();
    }

    public static void updateListApp() {
        appAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickApp(View v, int pos, App app) {

    }

    public static void downloadApp(int position, App app) {
        if (!dir1.exists())
            dir1.mkdirs();
        if (appDownloadTempApp.containsKey(app.getApplicationId())) {
            if (app != null)
                DownloadManager.getInstance().pause("http://118.69.201.53:8887/api/Application/Download?ApplicationId=" + app.getApplicationId());
            appDownloadTempApp.remove(app.getApplicationId());
        } else {
            System.out.println("chua co sd");
            appDownloadTempApp.put(app.getApplicationId(), app);
            download(position, app);
        }

    }

    public static void download(final int position, final App app) {

        DownloadManager.getInstance().download(app.getFileSize(), app.getApplicationName() + ".apk", "http://118.69.201.53:8887/api/Application/Download?ApplicationId=" + app.getApplicationId(), dir1, new CallBack() {

            @Override
            public void onDownloadStart() {
                System.out.println("test download");
                app.setStatus(App.STATUS_CONNECTING);
                db.updateApp(app, App.STATUS_CONNECTING, "");
//                if (isCurrentListViewItemVisible(position)) {
//                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.btnDownload.setText(appInfo.getButtonText());
//                }
            }

            @Override
            public void onConnected(long total, boolean isRangeSupport) {
                app.setStatus(App.STATUS_DOWNLOADING);
                db.updateApp(app, App.STATUS_DOWNLOADING, "");
                if (isCurrentListViewItemVisibleApp(position)) {
                    AppDownloaderAdapter.ViewHolder holder = getViewHolderApp(position);
                    holder.download.setText("Tải...");
                }
            }

            @Override
            public void onProgress(long finished, long total, int progress) {
//                String downloadPerSize = getDownloadPerSize(finished, total);
//                System.out.println(progress);
//                appInfo.setProgress(progress);
//                appInfo.setDownloadPerSize(downloadPerSize);
                app.setStatus(App.STATUS_DOWNLOADING);
                if (isCurrentListViewItemVisibleApp(position)) {
                    AppDownloaderAdapter.ViewHolder holder = getViewHolderApp(position);
                    holder.download.setText("Tải..." + progress + "%");
//                    holder.progressBar.setProgress(progress);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.btnDownload.setText(appInfo.getButtonText());
                }
            }

            @Override
            public void onComplete() {
                app.setStatus(App.STATUS_COMPLETE);
                app.setDownload(App.STATUS_COMPLETE);
                File apk = new File(dir1, app.getApplicationName() + ".apk");
                db.updateApp(app, App.STATUS_COMPLETE, apk.getAbsolutePath());
                System.out.println("pack " + apk.getAbsolutePath());
                if (apk.isFile() && apk.exists()) {
                    String packageName = Utils.getApkFilePackage(context, apk);
//                    app.setPackageName(packageName);
                    System.out.println("pack " + packageName);
                    Utils.installApp(context, apk);
                    if (Utils.isAppInstalled(context, packageName)) {
                        app.setStatus(App.STATUS_INSTALLED);
                    }
                }
//
                if (isCurrentListViewItemVisibleApp(position)) {
                    AppDownloaderAdapter.ViewHolder holder = getViewHolderApp(position);
                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded));
                    holder.download.setBackgroundResource(R.drawable.action_button_background_2);
                    holder.download.setText("Đã tải");
                    appDownloadTempApp.remove(app.getApplicationId());

                }
            }

            @Override
            public void onDownloadPause() {
                app.setStatus(App.STATUS_PAUSE);
                System.out.println("TEST PASE");
                db.updateApp(app, App.STATUS_PAUSE, "");

                File aa = new File(dir1, app.getApplicationName() + ".apk");
                if (aa.exists()) {
                    aa.delete();
                    System.out.println("[DELETE] Success: " + app.getApplicationName());
                } else {
                    System.out.println("[DELETE] File not exists.");
                }

                if (isCurrentListViewItemVisibleApp(position)) {
                    AppDownloaderAdapter.ViewHolder holder = getViewHolderApp(position);
                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
                    holder.download.setBackgroundResource(R.drawable.action_button_background);
                    holder.download.setText(app.getButtonText());
                    appDownloadTempApp.remove(app.getApplicationId());
                }
            }

            @Override
            public void onDownloadCancel() {
                app.setStatus(App.STATUS_NOT_DOWNLOAD);
//                appInfo.setDownloadPerSize("");
//                if (isCurrentListViewItemVisible(position)) {
//                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.tvDownloadPerSize.setText("");
//                    holder.btnDownload.setText(appInfo.getButtonText());
//                }
            }

            @Override
            public void onFailure(DownloadException e) {
                app.setStatus(App.STATUS_DOWNLOAD_ERROR);
                appDownloadTempApp.remove(app.getApplicationId());
//                if (isCurrentListViewItemVisible(position)) {
//                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.tvDownloadPerSize.setText("");
//                    holder.btnDownload.setText(appInfo.getButtonText());
//                }
                e.printStackTrace();
            }
        });
    }

    public static boolean isCurrentListViewItemVisibleApp(int position) {
        int first = listViewApp.getFirstVisiblePosition();
        int last = listViewApp.getLastVisiblePosition();
        return first <= position && position <= last;
    }

    public static AppDownloaderAdapter.ViewHolder getViewHolderApp(int position) {
        int childPosition = position - listViewApp.getFirstVisiblePosition();
        View view = listViewApp.getChildAt(childPosition);
        return (AppDownloaderAdapter.ViewHolder) view.getTag();
    }

    @Override
    public void onClick(View v, int position, App app) {
        downloadApp(position, app);
        System.out.println("DUNG APP");
    }
}
