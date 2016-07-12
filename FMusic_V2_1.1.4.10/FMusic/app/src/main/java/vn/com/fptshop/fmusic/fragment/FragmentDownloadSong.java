package vn.com.fptshop.fmusic.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vn.com.fptshop.fmusic.AppSetting;
import vn.com.fptshop.fmusic.MainActivity;
import vn.com.fptshop.fmusic.OnItemClickListener;
import vn.com.fptshop.fmusic.PlayMusicService;
import vn.com.fptshop.fmusic.R;
import vn.com.fptshop.fmusic.adapter.SongDownloadAdapter;
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.AppCombo;
import vn.com.fptshop.fmusic.models.Song;

/**
 * Created by MinhDH on 11/25/15.
 */
public class FragmentDownloadSong extends Fragment implements OnItemClickListener<Song>, SearchView.OnQueryTextListener {
    static final int ANIMATION_DURATION = 200;
    DatabaseHandler db;
    Context context;
    int offset = 0;
    int limit = 20;
    List<Song> listSong;

    ListView list;
    SongDownloadAdapter adapter;
    public Spinner spinner_nav;
    public Spinner spinner_nav_2;
    Fragment fragment;

    public FragmentDownloadSong() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getActivity());
        context = getActivity();
        spinner_nav = MainActivity.spinner_nav;
        spinner_nav_2 = MainActivity.spinner_nav_2;
        listSong = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_download_song, container, false);
        spinner_nav.setVisibility(View.GONE);
        spinner_nav_2.setVisibility(View.GONE);

        listSong = db.getSongsDownloadLimit(limit, offset);

//        File root = Environment
//                .getExternalStoragePublicDirectory("FPTShop/Music");
//        System.out.println(root.getAbsoluteFile());
//        File[] listFiles = root.listFiles();
//
//        for (File file : listFiles) {
//            System.out.println(file.getName());
//            Song song = new Song();
//            song.setSongName(file.getName().replace(".mp3", "").trim());
//            song.setLocal(file.getAbsolutePath());
//            song.setFileSize((int) file.length());
//            if (song != null)
//                listSong.add(song);
//        }
        list = (ListView) rootView.findViewById(R.id.listView);
        adapter = new SongDownloadAdapter(context, listSong);
        adapter.setOnItemClickListener(this);
        list.setAdapter(adapter);
        list.setOnScrollListener(onScrollListener());
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final FragmentPlaySong fragmentPlaySong = (FragmentPlaySong) getActivity().getSupportFragmentManager().findFragmentById(R.id.container_footer);
                fragment = new FragmentPlaySong();
                if (fragmentPlaySong != null && fragmentPlaySong.isVisible()) {
                    //VISIBLE
                    System.out.println("VISIBLE: " + listSong.get(position).getSongName());

                    fragmentPlaySong.newSong(listSong.get(position), (ArrayList<Song>) listSong);


                } else {
                    //NOT VISIBLE =(
                    System.out.println("NOT VISIBLE: ");

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("song", listSong.get(position));
                    bundle.putSerializable("songlist", (ArrayList<Song>) listSong);
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
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Resum");
        if (adapter != null) {
            List<Song> songList = db.getSongsDownloadLimit(limit, 0);
            listSong.clear();
            listSong.addAll(songList);
            adapter.notifyDataSetChanged();
            System.out.println("Update " + limit + " " + offset + " " + songList.size() + " " + listSong.size());
        }
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
                        offset = listSong.size();
                        List<Song> rs = db.getSongsDownloadLimit(limit, offset);

                        final FragmentPlaySong fragmentPlaySong = (FragmentPlaySong) getActivity().getSupportFragmentManager().findFragmentById(R.id.container_footer);
                        if (fragmentPlaySong != null && fragmentPlaySong.isVisible()) {
                            //VISIBLE
                            System.out.println("VISIBLE: ");
                            fragmentPlaySong.updateSongList((ArrayList<Song>) rs);
                        }
                        listSong.addAll(rs);
                        adapter.notifyDataSetChanged();
                        Log.i("TEST", "loading more data " + listSong.size() + " off " + offset + " li " + limit + " rs " + rs.size());

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
//        deleteCell(v, position, song);
        removeListItem(v, position, song);
    }

    @Override
    public void onItemClickAppCombo(View v, int pos, AppCombo appCombo) {

    }

    @Override
    public void onItemClickApp(View v, int pos, App app) {

    }


    protected void removeListItem(View rowView, final int positon, final Song song) {
        final Animation animation = AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.slide_in_left);
        rowView.startAnimation(animation);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            public void run() {
                String path = song.getLocal();
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                    db.updateSong(song, Song.STATUS_NOT_DOWNLOAD, "");
                    System.out.println("[DELETE] Success: " + song.getSongName());
                } else {
                    db.updateSong(song, Song.STATUS_NOT_DOWNLOAD, "");
                    System.out.println("[DELETE] File not exists.");
                }
                if (positon < listSong.size())
                    listSong.remove(positon);
                adapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.length() > 0) {
            List<Song> songList = db.searchSong(newText, 1);
            listSong.clear();
            listSong.addAll(songList);
            adapter.notifyDataSetChanged();
            System.out.println("TEST  1" + newText + " SIZE: " + songList.size());
        } else {

            listSong.clear();
            offset = 0;
//            List<Song> songList = db.getSongsDownloadLimit(limit, offset);
            listSong.addAll(db.getSongsDownloadLimit(limit, offset));
            System.out.println("TEST  2" + newText + " SIZE: " + listSong.size());
            adapter.notifyDataSetChanged();
        }

//        File root = Environment
//                .getExternalStoragePublicDirectory("FPTShop/Music");
//        System.out.println(root.getAbsoluteFile());
//        if (newText.length() > 0) {
//            File[] listFiles = root.listFiles();
//            List<Song> songList = new ArrayList<>();
//            for (File file : listFiles) {
//                System.out.println(file.getName());
//                if (file.getName().toLowerCase().contains(newText.toLowerCase())) {
//                    Song song = new Song();
//                    song.setSongName(file.getName().replace(".mp3", "").trim());
//                    song.setLocal(file.getAbsolutePath());
//                    song.setFileSize((int) file.length());
//                    if (song != null)
//                        songList.add(song);
//                }
//            }
//            listSong.clear();
//            listSong.addAll(songList);
//            adapter.notifyDataSetChanged();
//        } else {
//
//            listSong.clear();
//            File[] listFiles = root.listFiles();
//            List<Song> songList = new ArrayList<>();
//            for (File file : listFiles) {
//                System.out.println(file.getName());
//                Song song = new Song();
//                song.setSongName(file.getName().replace(".mp3", "").trim());
//                song.setLocal(file.getAbsolutePath());
//                song.setFileSize((int) file.length());
//                if (song != null)
//                    songList.add(song);
//            }
//
//            listSong.addAll(songList);
//            System.out.println("TEST  2" + newText + " SIZE: " + listSong.size());
//            adapter.notifyDataSetChanged();
//        }
        return false;
    }
}