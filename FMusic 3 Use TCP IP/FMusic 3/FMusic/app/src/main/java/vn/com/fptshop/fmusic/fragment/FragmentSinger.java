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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import vn.com.fptshop.fmusic.CustomSpinnerAdapter;
import vn.com.fptshop.fmusic.DataDownloader;
import vn.com.fptshop.fmusic.MainActivity;
import vn.com.fptshop.fmusic.OnItemClickListener;
import vn.com.fptshop.fmusic.R;
import vn.com.fptshop.fmusic.adapter.SingerAdapter;
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.download.CallBack;
import vn.com.fptshop.fmusic.download.DownloadManager;
import vn.com.fptshop.fmusic.download.core.DownloadException;
import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.AppCombo;
import vn.com.fptshop.fmusic.models.Singer;
import vn.com.fptshop.fmusic.models.Song;

/**
 * Created by MinhDH on 11/25/15.
 */
public class FragmentSinger extends Fragment implements OnItemClickListener<Singer>, SearchView.OnQueryTextListener {
    DatabaseHandler db;
    public Context context;
    public int offset = 0;
    public int limit = 30;
    public String key = "";
    public List<Singer> listSinger;

    public ListView listView;
    public SingerAdapter adapter;
    public int id = 0;
    public Spinner spinner_nav;
    public Spinner spinner_nav_2;

    public List<Song> songList;
    public HashMap<Integer, List<Song>> listDownloadTemp = new HashMap<>();
    public HashMap<Integer, Song> songDownloadTemp = new HashMap<>();

    OnItemClickListener onItemClickListener;
    android.app.DownloadManager downloadanager;
    public Stack<Song> songStack = new Stack<>();
    public HashMap<Integer, Fragment> fragmentTemp = new HashMap<>();

    public FragmentSinger() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getActivity());
        songList = new ArrayList<>();
        context = getActivity();
        onItemClickListener = this;
        spinner_nav = MainActivity.spinner_nav;
        spinner_nav_2 = MainActivity.spinner_nav_2;
        addItemsToSpinner();
        addItemsToSpinner2();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_singer, container, false);
        spinner_nav.setVisibility(View.VISIBLE);
        spinner_nav_2.setVisibility(View.VISIBLE);
//        getActivity().registerReceiver(onComplete,
//                new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//        String servicestring = Context.DOWNLOAD_SERVICE;

//        downloadanager = (android.app.DownloadManager) getActivity().getSystemService(servicestring);
//        listSinger = db.getSingersLimit(id,offset, limit);

        listView = (ListView) rootView.findViewById(R.id.listView);

//        adapter = new SingerAdapter(context, listSinger);
//
//        list.setAdapter(adapter)

        listView.setOnScrollListener(onScrollListener());
        // listening to single list item on click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item

                // Launching new Activity on selecting single List Item
               /* Intent i = new Intent(getActivity(), SingerDetailActivity.class);
                // sending data to new activity
                Singer singer = listSinger.get(position);
                i.putExtra("singer", singer.getSingerId() + "_" + singer.getSingerName() + "_" + singer.getTotalSongs());
                startActivity(i);*/

                Singer singer = listSinger.get(position);
//                Fragment fragment = new FragmentSingerDetail();
//                Bundle bundle = new Bundle();
//                bundle.putString("singer", singer.getSingerId() + "_" + singer.getSingerName() + "_" + singer.getTotalSongs());
//                fragment.setArguments(bundle);
//
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.container_body, fragment, "FragmentSingerDetail");
//                ft.addToBackStack("FragmentSingerDetail");
//                ft.commit();

                if (fragmentTemp.containsKey(singer.getSingerId())) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment fragment = fragmentTemp.get(singer.getSingerId());
                    if (fragment.isAdded()) {
                        ft.show(fragment);
                    } else {
                        ft.add(R.id.container_body, fragment, "fragmentTemp" + singer.getSingerId());
                    }

                    ft.addToBackStack("FragmentSingerDetail");
                    ft.commit();
                    System.out.println("SHOW FRAGMENT  FragmentSingerDetail");
                } else {
                    Fragment fragment = new FragmentSingerDetail();
                    Bundle bundle = new Bundle();
                    bundle.putString("singer", singer.getSingerId() + "_" + singer.getSingerName() + "_" + singer.getTotalSongs());
                    fragment.setArguments(bundle);
                    fragmentTemp.put(singer.getSingerId(), fragment);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack("FragmentSingerDetail");
                    if (fragment.isAdded()) {
                        ft.show(fragment);
                    } else {
                        ft.add(R.id.container_body, fragment, "fragmentTemp" + singer.getSingerId());
                    }
//            if (this.isAdded()) {
//                ft.hide(this);
//            }
                    ft.commit();
                    System.out.println("NEW FRAGMENT FragmentSingerDetail");
                }

            }
        });
        return rootView;
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

        System.out.println("FragmentSinger Resume");
        listSinger = db.getSingersLimit(id, offset, limit, key);

        adapter = new SingerAdapter(context, listSinger);
        adapter.setOnItemClickListener(onItemClickListener);
        listView.setAdapter(adapter);
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
                int count = listView.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() >= count - threshold) {
                        Log.i("TEST", "loading more data");
                        offset = listSinger.size();
                        listSinger.addAll(db.getSingersLimit(id, offset, limit, key));
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

    // add items into spinner dynamically
    public void addItemsToSpinner() {

        ArrayList<String> list = new ArrayList<String>();
        list.add("Tất cả");
        list.add("Việt Nam");
        list.add("Âu Mỹ");
        list.add("Hàn Quốc");

        // Custom ArrayAdapter with spinner item layout to set popup background

        CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(
                getActivity(), list);


        spinner_nav.setAdapter(spinAdapter);


        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> a, View v,
                                       int position, long i) {
                id = position;
                offset = 0;
//                limit = 20;

                System.out.println("TEST " + id);

                listSinger = db.getSingersLimit(id, offset, limit, key);

                adapter = new SingerAdapter(context, listSinger);
                adapter.setOnItemClickListener(onItemClickListener);
                listView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    // add items into spinner dynamically
    public void addItemsToSpinner2() {

        ArrayList<String> list = new ArrayList<String>();
        list.add("Nổi bật");
        list.add("#");
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        list.add("G");
        list.add("H");
        list.add("I");
        list.add("J");
        list.add("K");
        list.add("L");
        list.add("M");
        list.add("N");
        list.add("O");
        list.add("P");
        list.add("Q");
        list.add("R");
        list.add("S");
        list.add("T");
        list.add("U");
        list.add("W");
        list.add("X");
        list.add("Y");
        list.add("Z");


        // Custom ArrayAdapter with spinner item layout to set popup background

        CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(
                getActivity(), list);


        spinner_nav_2.setAdapter(spinAdapter);

        spinner_nav_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> a, View v,
                                       int position, long i) {
                // On selecting a spinner item
                key = a.getItemAtPosition(position).toString();

                offset = 0;
//                limit = 20;
                if ("Nổi bật".equalsIgnoreCase(key)) {
                    key = "";
                }
                System.out.println("TEST " + id);
                listSinger = db.getSingersLimit(id, offset, limit, key);

                adapter = new SingerAdapter(context, listSinger);
                adapter.setOnItemClickListener(onItemClickListener);
                listView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    boolean checkDownload = false;

    @Override
    public void onItemClick(View v, int position, Singer singer) {
//        if (DataDownloader.singerIdStack.search(singer.getSingerId()) == -1) {
//            DataDownloader.singerIdStack.add(singer.getSingerId());
//            Toast.makeText(context, "Đã thêm " + singer.getSingerName() +" vào downloader!", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Đã có " + singer.getSingerName() +" trong downloader!", Toast.LENGTH_SHORT).show();
//        }
//        FragmentDownloader.downloadStack();

        boolean check = false;
        for (Singer singer1 : DataDownloader.singerList) {
            if (singer1.getSingerId() == singer.getSingerId()) {
                check = true;
                break;
            }
        }
        if (!check) {
            DataDownloader.singerList.add(singer);
            Toast.makeText(context, "Đã thêm " + singer.getSingerName() + " vào downloader!", Toast.LENGTH_SHORT).show();
            FragmentDownloader.updateList();
            for (int i = 0; i < DataDownloader.singerList.size(); i++) {
                if (DataDownloader.singerList.get(i).getSingerId() == singer.getSingerId()) {
                    FragmentDownloader.downloadSinger(i, singer);
                }
            }
        } else {
            Toast.makeText(context, "Đã có " + singer.getSingerName() + " trong downloader!", Toast.LENGTH_SHORT).show();
        }

//        if (listDownloadTemp.containsKey(position)) {
//            Song song = songDownloadTemp.get(position);
//            if (song != null)
//                DownloadManager.getInstance().pause("http://118.69.201.53:8887/api/Music/Download?songId=" + song.getSongId());
//
//            listDownloadTemp.remove(position);
//            songDownloadTemp.remove(position);
//        } else {
//            System.out.println("chua co");
//            List<Song> songs = db.getSongsLimitOfSingerDownload(singer.getSingerId());
//            listDownloadTemp.put(position, songs);
//            List<Song> songsTemp = listDownloadTemp.get(position);
//            if (songsTemp.size() > 0) {
//                System.out.println("Length: " + songsTemp.size());
//                download(position, singer);
//            }
//        }

        //DOWNLOADMANAGER
//        List<Song> songs = db.getSongsLimitOfSingerDownload(singer.getSingerId());
//        songStack.addAll(songs);
//        if (checkDownload == false)
//            startDownload();
//        Toast.makeText(context, "Tải " + singer.getSingerName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClickAppCombo(View v, int pos, AppCombo appCombo) {

    }

    @Override
    public void onItemClickApp(View v, int pos, App app) {

    }

    private void startDownload() {
        if (!songStack.empty()) {
            checkDownload = true;
            Song song = songStack.pop();

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

    private final File dir = new File(Environment.getExternalStorageDirectory(), "Download");

    private void download(final int position, final Singer singer) {

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

    private void downloader(final int position, final Song song, final Singer singer) {

        System.out.println("Size: " + listDownloadTemp.get(position).size());

        DownloadManager.getInstance().download(song.getFileSize(), song.getSongName() + ".mp3", "http://118.69.201.53:8887/api/Music/Download?songId=" + song.getSongId(), dir, new CallBack() {


            @Override
            public void onDownloadStart() {
                System.out.println("test download");
                song.setStatus(Song.STATUS_CONNECTING);
                db.updateSong(song, Song.STATUS_CONNECTING, "");
                songDownloadTemp.put(position, song);

//                listDownloadTemp.put(position,song);
                if (isCurrentListViewItemVisible(position)) {
                    SingerAdapter.ViewHolder holder = getViewHolder(position);
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
                    SingerAdapter.ViewHolder holder = getViewHolder(position);
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
                    SingerAdapter.ViewHolder holder = getViewHolder(position);
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
                        SingerAdapter.ViewHolder holder = getViewHolder(position);
                        holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded_success));
                        holder.download.setBackgroundResource(R.drawable.action_button_background_3);
                        holder.download.setText(song.getButtonText());
                        if (listDownloadTemp.get(position) != null) {
                            holder.proccess.setText((singer.getTotalSongs() - (listDownloadTemp.get(position).size() + 1)) + "/" + singer.getTotalSongs());
                        } else {
                            holder.proccess.setText(singer.getTotalSongs() + "/" + singer.getTotalSongs());
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
                    SingerAdapter.ViewHolder holder = getViewHolder(position);
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
        int first = listView.getFirstVisiblePosition();
        int last = listView.getLastVisiblePosition();
        return first <= position && position <= last;
    }

    private SingerAdapter.ViewHolder getViewHolder(int position) {
        int childPosition = position - listView.getFirstVisiblePosition();
        View view = listView.getChildAt(childPosition);
        return (SingerAdapter.ViewHolder) view.getTag();
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

        if (newText.length() > 0) {
            List<Singer> singerList = db.searchSinger(newText);
            listSinger.clear();
            listSinger.addAll(singerList);
            adapter.notifyDataSetChanged();
            System.out.println("TEST  1" + newText + " SIZE: " + songList.size());
        } else {
            System.out.println("TEST  2" + newText + " SIZE: " + newText.length());
            listSinger.clear();
            offset = 0;
            listSinger.addAll(db.getSingersLimit(id, offset, limit, key));
            adapter.notifyDataSetChanged();
        }
        return false;
    }


    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
//            findViewById(R.id.start).setEnabled(true);
//            Toast.makeText(ctxt, "KAKA...hi!", Toast.LENGTH_LONG).show();
            System.out.println("TEST NE");
            startDownload();
        }
    };
}