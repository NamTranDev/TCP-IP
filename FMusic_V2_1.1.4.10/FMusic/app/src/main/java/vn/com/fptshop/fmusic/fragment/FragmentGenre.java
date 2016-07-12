package vn.com.fptshop.fmusic.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import vn.com.fptshop.fmusic.AppSetting;
import vn.com.fptshop.fmusic.DataDownloader;
import vn.com.fptshop.fmusic.GenreDetailActivity;
import vn.com.fptshop.fmusic.IOnClickGenreDownloader;
import vn.com.fptshop.fmusic.IOnClickPlay;
import vn.com.fptshop.fmusic.MainActivity;
import vn.com.fptshop.fmusic.OnItemClickListener;
import vn.com.fptshop.fmusic.R;
import vn.com.fptshop.fmusic.adapter.GenreAdapter;
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.download.CallBack;
import vn.com.fptshop.fmusic.download.DownloadManager;
import vn.com.fptshop.fmusic.download.core.DownloadException;
import vn.com.fptshop.fmusic.models.Genre;
import vn.com.fptshop.fmusic.models.Song;

/**
 * Created by MinhDH on 11/25/15.
 */
public class FragmentGenre extends Fragment implements IOnClickGenreDownloader, SearchView.OnQueryTextListener,IOnClickPlay {

    DatabaseHandler db;
    Context context;
    int offset = 0;
    int limit = 50;
    List<Genre> listGenre;

    ListView list;
    GridView gridView;
    GenreAdapter adapter;
    public Spinner spinner_nav;
    public Spinner spinner_nav_2;

    public HashMap<Integer, List<Song>> listDownloadTemp = new HashMap<>();
    public HashMap<Integer, Song> songDownloadTemp = new HashMap<>();
    OnItemClickListener onItemClickListener;

    boolean checkDownload = false;
    public Stack<Song> songStack = new Stack<>();
    android.app.DownloadManager downloadanager;
    public HashMap<Integer, Fragment> fragmentTemp = new HashMap<>();
    private static Song currentDownload = null;

    public FragmentGenre() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getActivity());
        context = getActivity();
        spinner_nav = MainActivity.spinner_nav;
        spinner_nav_2 = MainActivity.spinner_nav_2;
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_genre_girdview, container, false);
        listGenre = db.getGenresLimit(offset, limit);
        spinner_nav.setVisibility(View.GONE);
        spinner_nav_2.setVisibility(View.GONE);
//        getActivity().registerReceiver(onComplete,
//                new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//        String servicestring = Context.DOWNLOAD_SERVICE;
//
//        downloadanager = (android.app.DownloadManager) getActivity().getSystemService(servicestring);
//       list = (ListView) rootView.findViewById(R.id.listView);

        gridView = (GridView) rootView.findViewById(R.id.gridview);

        adapter = new GenreAdapter(context, listGenre);

        gridView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemClickListener1(this);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "TEST", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.notifyDataSetChanged();
//        list.setOnScrollListener(onScrollListener());

        // Inflate the layout for this fragment
        // listening to single list item on click



       /* list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getActivity(), GenreDetailActivity.class);
                // sending data to new activity
                Genre genre = listGenre.get(position);
                i.putExtra("genre", genre.getGenreId() + "_" + genre.getGenreName() + "_" + genre.getTotalSongs());
                startActivity(i);



            }
        });*/
        // showFooter();
        return rootView;
    }

    Fragment fragment;

    public void showFooter() {
        FragmentPlaySong test = (FragmentPlaySong) getActivity().getSupportFragmentManager().findFragmentById(R.id.container_footer);

        if (test != null && test.isVisible()) {
            //VISIBLE! =)
            System.out.println("VISIBLE: ");

        } else {
            //NOT VISIBLE =(
            System.out.println("NOT VISIBLE: ");

            fragment = new FragmentPlaySong();
            Bundle bundle = new Bundle();
            List<Song> songList = db.getSongsRandomLimit(limit);
            bundle.putSerializable("song", songList.get(0));
            bundle.putSerializable("songlist", (ArrayList<Song>) songList);
            bundle.putString("Mode", AppSetting.MODE_ONLINE);

            fragment.setArguments(bundle);

            if (fragment != null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_footer, fragment);
                fragmentTransaction.commit();
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        getActivity().unregisterReceiver(onComplete);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
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
                        offset = listGenre.size();
                        listGenre.addAll(db.getGenresLimit(offset, limit));
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


    public void onItemClick(View v, int position, Genre genre) {

        if (listDownloadTemp.containsKey(position)) {

            Song song = songDownloadTemp.get(position);
            if (song != null)
                DownloadManager.getInstance().pause("http://118.69.201.53:8887/api/Music/Download?songId=" + song.getSongId());

            listDownloadTemp.remove(position);
            songDownloadTemp.remove(position);
//            System.out.println("Co roi " + song.getSongName());
        } else {
            System.out.println("chua co");
            List<Song> songs = db.getSongsLimitOfGenreDownload(genre.getGenreId());
            listDownloadTemp.put(position, songs);
            List<Song> songsTemp = listDownloadTemp.get(position);
            if (songsTemp.size() > 0) {
                System.out.println("Length: " + songsTemp.size());
                download(position, genre);
            }
        }
    }

    private final File dir = new File(Environment.getExternalStorageDirectory(), "Download");

    private void download(final int position, final Genre genre) {

        if (listDownloadTemp.containsKey(position)) {
            List<Song> songsTemp = listDownloadTemp.get(position);
            if (songsTemp.size() > 0) {
                Song song = songsTemp.get(0);
                songsTemp.remove(0);
                listDownloadTemp.put(position, songsTemp);
                System.out.println("Length: " + songsTemp.size());
                downloader(position, song, genre);
            } else {
                listDownloadTemp.remove(position);
            }
        }
    }

    private void downloader(final int position, final Song song, final Genre genre) {

        System.out.println("Size: " + listDownloadTemp.get(position).size());

        DownloadManager.getInstance().download(song.getFileSize(), song.getSongName() + ".mp3", "http://118.69.201.53:8887/api/Music/Download?songId=" + song.getSongId(), dir, new CallBack() {


            @Override
            public void onDownloadStart() {

                System.out.println("test download");
                song.setStatus(Song.STATUS_CONNECTING);
                db.updateSong(song, Song.STATUS_CONNECTING, "");
                songDownloadTemp.put(position, song);
//                listDownloadTemp.put(position,song);
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
                    GenreAdapter.ViewHolder holder = getViewHolder(position);
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
                    GenreAdapter.ViewHolder holder = getViewHolder(position);
                    holder.download.setText("Tải..." + progress + "%");
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
                    int count = songsTemp.size();
                    if (songsTemp.size() == 0 && listDownloadTemp.containsKey(position)) {
                        listDownloadTemp.remove(position);
                        System.out.println("Xoa ra khoi hashmap");
                    }
                    if (songsTemp.size() > 0 && isDownload == false) {
                        Song song = songsTemp.get(0);
                        songsTemp.remove(0);
                        listDownloadTemp.put(position, songsTemp);
                        isDownload = true;
                        if (isCurrentListViewItemVisible(position)) {
                            GenreAdapter.ViewHolder holder = getViewHolder(position);
                            holder.proccess.setText((genre.getTotalSongs() - count) + "/" + genre.getTotalSongs());
                        }
                        downloader(position, song, genre);
                    }
                } else {
                    if (isCurrentListViewItemVisible(position) && isDownload == false) {
                        GenreAdapter.ViewHolder holder = getViewHolder(position);
                        holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded_success));
                        holder.download.setBackgroundResource(R.drawable.action_button_background_3);
                        holder.download.setText(song.getButtonText());
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
                    GenreAdapter.ViewHolder holder = getViewHolder(position);
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
                if (listDownloadTemp.containsKey(position)) {
                    List<Song> songsTemp = listDownloadTemp.get(position);
                    int count = songsTemp.size();
                    if (songsTemp.size() == 0 && listDownloadTemp.containsKey(position)) {
                        listDownloadTemp.remove(position);
                        System.out.println("Xoa ra khoi hashmap");
                    }
                    if (songsTemp.size() > 0 && isDownload == false) {
                        Song song = songsTemp.get(0);
                        songsTemp.remove(0);
                        listDownloadTemp.put(position, songsTemp);
                        isDownload = true;
                        if (isCurrentListViewItemVisible(position)) {
                            GenreAdapter.ViewHolder holder = getViewHolder(position);
                            holder.proccess.setText((genre.getTotalSongs() - count) + "/" + genre.getTotalSongs());
                        }
                        downloader(position, song, genre);
                    }
                } else {
                    if (isCurrentListViewItemVisible(position) && isDownload == false) {
                        GenreAdapter.ViewHolder holder = getViewHolder(position);
                        holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded_success));
                        holder.download.setBackgroundResource(R.drawable.action_button_background_3);
                        holder.download.setText(song.getButtonText());
                    }
                }
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

    private GenreAdapter.ViewHolder getViewHolder(int position) {
        int childPosition = position - list.getFirstVisiblePosition();
        View view = list.getChildAt(childPosition);
        return (GenreAdapter.ViewHolder) view.getTag();
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


        List<Genre> genreList = db.searchGenre(newText);
        listGenre.clear();
        listGenre.addAll(genreList);
        adapter.notifyDataSetChanged();
        System.out.println("TEST  1 " + newText + " SIZE: " + listGenre.size());


        return false;
    }

    @Override
    public void onClick(int key, View v, int position, Genre genre) {
//        onItemClick(v,position,genre);
//        boolean check = false;
//        for (Genre genre1 : DataDownloader.genreList) {
//            if (genre1.getGenreId() == genre.getGenreId()) {
//                check = true;
//                break;
//            }
//        }
//        if (!check) {
//            DataDownloader.genreList.add(genre);
//            Toast.makeText(context, "Đã thêm " + genre.getGenreName() + " vào downloader!", Toast.LENGTH_SHORT).show();
//            for (int i = 0; i < DataDownloader.singerList.size(); i++) {
//                if (DataDownloader.genreList.get(i).getGenreId()== genre.getGenreId()) {
//                    FragmentDownloader.downloadGenre(i, genre);
//                }
//            }
//        }else{
//            Toast.makeText(context, "Đã có " +genre.getGenreName() +" trong downloader!", Toast.LENGTH_SHORT).show();
//        }

//DOWNLOADMANAGER
//        List<Song> songs = db.getSongsLimitOfGenreDownload(genre.getGenreId());
//        songStack.addAll(songs);
//        if (checkDownload == false)
//            startDownload();
//        Toast.makeText(context, "Tải " + genre.getGenreName(), Toast.LENGTH_LONG).show();

        boolean check = false;
        for (Genre genre1 : DataDownloader.genreList) {
            if (genre1.getGenreId() == genre.getGenreId()) {
                check = true;
                break;
            }
        }
        if (!check) {
            DataDownloader.genreList.add(genre);
            Toast.makeText(context, "Đã thêm " + genre.getGenreName() + " vào downloader!", Toast.LENGTH_SHORT).show();
            FragmentDownloader.updateListGenre();
            for (int i = 0; i < DataDownloader.genreList.size(); i++) {
                if (DataDownloader.genreList.get(i).getGenreId() == genre.getGenreId()) {
                    FragmentDownloader.downloadGenre(i, genre);
                }
            }
        } else {
            Toast.makeText(context, "Đã có " + genre.getGenreName() + " trong downloader!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickShowDetail(int key, View v, int position, Genre genre) {
        // Launching new Activity on selecting single List Item
     /*   Intent i = new Intent(getActivity(), GenreDetailActivity.class);
        // sending data to new activity
        i.putExtra("genre", genre.getGenreId() + "_" + genre.getGenreName() + "_" + genre.getTotalSongs());
        startActivity(i);*/


        //Genre genre = listGenre.get(position);
        if (fragmentTemp.containsKey(genre.getGenreId())) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment fragment = fragmentTemp.get(genre.getGenreId());
            if (fragment.isAdded()) { // if the fragment is already in container
                ft.show(fragment);
            } else { // fragment needs to be added to frame container
                ft.add(R.id.container_body, fragment, "fragmentTemp" + genre.getGenreId());
            }
            if (this.isAdded()) {
                ft.hide(this);
            }
            ft.addToBackStack("FragmentGenreDetail");
            ft.commit();
            System.out.println("SHOW FRAGMENT GENRE DETAIL");
        } else {
            Fragment fragment = new FragmentGenreDetail();
            Bundle bundle = new Bundle();
            bundle.putString("genre", genre.getGenreId() + "_" + genre.getGenreName() + "_" + genre.getTotalSongs());
            fragment.setArguments(bundle);
            fragmentTemp.put(genre.getGenreId(), fragment);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.container_body, fragment, "FragmentGenreDetail");
            ft.addToBackStack("FragmentGenreDetail");
            if (fragment.isAdded()) { // if the fragment is already in container
                ft.show(fragment);
            } else { // fragment needs to be added to frame container
                ft.add(R.id.container_body, fragment, "fragmentTemp" + genre.getGenreId());
            }
//            if (this.isAdded()) {
//                ft.hide(this);
//            }
            ft.commit();
            System.out.println("NEW FRAGMENT GENRE DETAIL");
        }
    }


    private void startDownload() {
        if (!songStack.empty()) {
            checkDownload = true;
            Song song = songStack.pop();
            currentDownload = song;
            Uri uri = Uri
                    .parse("http://118.69.201.53:8887/api/Music/Download?songId=" + song.getSongId());
            Environment
                    .getExternalStoragePublicDirectory("FPTShop/Music")
                    .mkdirs();
            long lastDownload =
                    downloadanager.enqueue(new android.app.DownloadManager.Request(uri)
                            .setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI |
                                    android.app.DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle(song.getSongName())
                            .setDestinationInExternalPublicDir("FPTShop/Music",
                                    song.getSongName() + ".mp3"));

        } else {
            checkDownload = false;
        }
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
//            findViewById(R.id.start).setEnabled(true);
//            Toast.makeText(ctxt, "KAKA...hi!", Toast.LENGTH_LONG).show();
            System.out.println("GENRE DOWNLOAD SUCCESS ");
            startDownload();
        }
    };

    @Override
    public void onClick(View v, Genre genre) {
        System.out.println("NGHE " + genre.getGenreName());
        List<Song> songs = db.getSongsLimitOfGenre(genre.getGenreId(),50,0);
        final FragmentPlaySong fragmentPlaySong = (FragmentPlaySong) getActivity().getSupportFragmentManager().findFragmentById(R.id.container_footer);
        fragment = new FragmentPlaySong();
        if (fragmentPlaySong != null && fragmentPlaySong.isVisible()) {
            //VISIBLE
            System.out.println("VISIBLE: ");
            fragmentPlaySong.newSong(songs.get(0), (ArrayList<Song>) songs);

        } else {
            //NOT VISIBLE =(
            System.out.println("NOT VISIBLE: ");

            Bundle bundle = new Bundle();
            bundle.putSerializable("song", songs.get(0));

            bundle.putSerializable("songlist", (ArrayList<Song>) songs);
            fragment.setArguments(bundle);

            if (fragment != null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_footer, fragment);
                fragmentTransaction.commit();
            }
        }
    }
}