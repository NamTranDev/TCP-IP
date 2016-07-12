package vn.com.fptshop.fmusic.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import vn.com.fptshop.fmusic.AppSetting;
import vn.com.fptshop.fmusic.MainActivity;
import vn.com.fptshop.fmusic.OnItemClickListener;
import vn.com.fptshop.fmusic.R;
import vn.com.fptshop.fmusic.adapter.SongAdapter;
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.download.CallBack;
import vn.com.fptshop.fmusic.download.DownloadManager;
import vn.com.fptshop.fmusic.download.core.DownloadException;
import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.AppCombo;
import vn.com.fptshop.fmusic.models.Song;

/**
 * Created by Administrator on 18/12/2015.
 */
public class FragmentSingerDetail extends Fragment implements OnItemClickListener<Song>, SearchView.OnQueryTextListener {

    public Spinner spinner_nav;
    public Spinner spinner_nav_2;
    Context context;

    String singerTemp="";
    StringTokenizer tokenizer;
    int singerId = 0;
    String singerName = "";
    int totalSongs = 0;
    int id = 0;
    int offset = 0;
    int limit = 20;
    List<Song> listSong;
    ListView list;
    SongAdapter adapter;
    DatabaseHandler db;
    Fragment fragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        setHasOptionsMenu(true);
        spinner_nav = MainActivity.spinner_nav;
        spinner_nav_2 = MainActivity.spinner_nav_2;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey("singer")) {
                singerTemp = bundle.getString("singer");
            }
        }
        tokenizer = new StringTokenizer(singerTemp, "_");
        while (tokenizer.hasMoreTokens()) {
            singerId = Integer.parseInt(tokenizer.nextToken());
            singerName = tokenizer.nextToken();
            totalSongs = Integer.parseInt(tokenizer.nextToken());
        }
        System.out.println("onCreate " + singerId);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(singerName);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Có " + totalSongs + " bài hát.");

        id = singerId;
        db = new DatabaseHandler(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_singer_detail,container,false);

        spinner_nav.setVisibility(View.GONE);
        spinner_nav_2.setVisibility(View.GONE);

        list = (ListView) rootView.findViewById(R.id.listView);

        listSong = db.getSongsLimitOfSinger(singerId, limit, offset);
        adapter = new SongAdapter(context, listSong);
        adapter.setOnItemClickListener(this);
        list.setAdapter(adapter);
        System.out.println("onCreateView");
        list.setOnScrollListener(onScrollListener());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final FragmentPlaySong fragmentPlaySong = (FragmentPlaySong) getActivity().getSupportFragmentManager().findFragmentById(R.id.container_footer);
                fragment = new FragmentPlaySong();
                if (fragmentPlaySong != null && fragmentPlaySong.isVisible()) {
                    //VISIBLE
                    System.out.println("VISIBLE: ");
                    fragmentPlaySong.newSong(listSong.get(position), (ArrayList<Song>) listSong);

                } else {
                    //NOT VISIBLE =(
                    System.out.println("NOT VISIBLE: ");

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("song", listSong.get(position));
                    bundle.putSerializable("songlist", (ArrayList<Song>) listSong);
                    fragment.setArguments(bundle);

                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_footer, fragment);
                        fragmentTransaction.commit();
                    }
                }
            }
        });


        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        final MenuItem item = menu.findItem(R.id.action_search);
        if (item != null) {
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener(this);

            MenuItemCompat.setOnActionExpandListener(item,
                    new MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            // Do something when collapsed
                            System.out.println("Test");
                            return true; // Return true to collapse action view
                        }

                        @Override
                        public boolean onMenuItemActionExpand(MenuItem item) {
                            // Do something when expanded
                            return true; // Return true to expand action view
                        }
                    });
        } else {
            inflater.inflate(R.menu.main, menu);
            onCreateOptionsMenu(menu, inflater);
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 10;
                int count = list.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (list.getLastVisiblePosition() >= count - threshold) {
                        Log.i("TEST", "loading more data");
                        offset = listSong.size();
                        listSong.addAll(db.getSongsLimitOfSinger(id, limit, offset));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }

        };
    }

    @Override
    public void onItemClick(View v, int position, Song song) {
        System.out.println("TEST Click");
        if (!dir.exists())
            dir.mkdirs();
        if (song.getStatus() == Song.STATUS_DOWNLOADING || song.getStatus() == Song.STATUS_CONNECTING) {
            if (isCurrentListViewItemVisible(position)) {
                DownloadManager.getInstance().pause(AppSetting.URL+"/api/Music/Download?songId=" + song.getSongId());
            }
            return;
        }else{
            if (song.getStatus() != 6)
            download(position,song);
        }

//      String servicestring = Context.DOWNLOAD_SERVICE;
//        android.app.DownloadManager downloadanager;
//        downloadanager = (android.app.DownloadManager) getActivity().getSystemService(servicestring);
//        Uri uri = Uri
//                .parse("http://118.69.201.53:8887/api/Music/Download?songId=" + song.getSongId());
//        Environment
//                .getExternalStoragePublicDirectory("FPTShop/Music")
//                .mkdirs();
//        long lastDownload =
//                downloadanager.enqueue(new android.app.DownloadManager.Request(uri)
//                        .setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI |
//                                android.app.DownloadManager.Request.NETWORK_MOBILE)
//                        .setAllowedOverRoaming(false)
//                        .setTitle(song.getSongName())
////                        .setDescription("")
//                        .setDestinationInExternalPublicDir("FPTShop/Music",
//                                song.getSongName() + ".mp3"));
//
//        v.setEnabled(false);
//        Toast.makeText(context, "Tải " + song.getSongName() + ".mp3", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClickAppCombo(View v, int pos, AppCombo appCombo) {


    }

    @Override
    public void onItemClickApp(View v, int pos, App app) {

    }

    private final File dir = new File(Environment.getExternalStorageDirectory(), "FPTShop/Music");

    private void download(final int position, final Song song) {

        DownloadManager.getInstance().download(song.getFileSize(), song.getSongName() + ".mp3", AppSetting.URL+ "/api/Music/Download?songId=" + song.getSongId(), dir, new CallBack() {

            @Override
            public void onDownloadStart() {
                System.out.println("test download");
                song.setStatus(Song.STATUS_CONNECTING);
                db.updateSong(song, Song.STATUS_CONNECTING, "");
//                if (isCurrentListViewItemVisible(position)) {
//                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.btnDownload.setText(appInfo.getButtonText());
//                }
            }

            @Override
            public void onConnected(long total, boolean isRangeSupport) {
                song.setStatus(Song.STATUS_DOWNLOADING);
                db.updateSong(song, Song.STATUS_DOWNLOADING, "");
                if (isCurrentListViewItemVisible(position)) {
                    SongAdapter.ViewHolder holder = getViewHolder(position);
                    holder.download.setText("Tải...");
                }
            }

            @Override
            public void onProgress(long finished, long total, int progress) {
//                String downloadPerSize = getDownloadPerSize(finished, total);
//                System.out.println(progress);
//                appInfo.setProgress(progress);
//                appInfo.setDownloadPerSize(downloadPerSize);
                song.setStatus(Song.STATUS_DOWNLOADING);

                if (isCurrentListViewItemVisible(position)) {
                    SongAdapter.ViewHolder holder = getViewHolder(position);
                    holder.download.setText("Tải..." + progress + "%");
//                    holder.progressBar.setProgress(progress);
//                    holder.tvStatus.setText(appInfo.getStatusText());
//                    holder.btnDownload.setText(appInfo.getButtonText());
                }
            }

            @Override
            public void onComplete() {
                song.setStatus(Song.STATUS_COMPLETE);
                song.setDownload(Song.STATUS_COMPLETE);
                db.updateSong(song, Song.STATUS_COMPLETE, dir + "/" + song.getSongName() + ".mp3");
                if (isCurrentListViewItemVisible(position)) {
                    SongAdapter.ViewHolder holder = getViewHolder(position);
                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded_success));
                    holder.download.setBackgroundResource(R.drawable.action_button_background_3);
                    holder.download.setText(song.getButtonText());
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
                    SongAdapter.ViewHolder holder = getViewHolder(position);
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

    private boolean isCurrentListViewItemVisible(int position) {
        int first = list.getFirstVisiblePosition();
        int last = list.getLastVisiblePosition();
        return first <= position && position <= last;
    }

    private SongAdapter.ViewHolder getViewHolder(int position) {
        int childPosition = position - list.getFirstVisiblePosition();
        View view = list.getChildAt(childPosition);
        return (SongAdapter.ViewHolder) view.getTag();
    }

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    private String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "M/" + DF.format((float) total / (1024 * 1024)) + "M";
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length()>0) {
            List<Song> songList = db.searchSongsOf(newText,id,1);
            listSong.clear();
            listSong.addAll(songList);
            adapter.notifyDataSetChanged();
            System.out.println("TEST  1" + newText + " SIZE: " + songList.size());
        }else{
            listSong.clear();
            offset = 0;
            listSong.addAll(db.getSongsLimitOfSinger(id, limit, offset));
            System.out.println("TEST  2" + newText + " SIZE: " + listSong.size());
            adapter.notifyDataSetChanged();
        }
        return false;
    }
}
