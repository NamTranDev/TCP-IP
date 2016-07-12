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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import vn.com.fptshop.fmusic.AppDetailActivity;
import vn.com.fptshop.fmusic.AppSetting;
import vn.com.fptshop.fmusic.DataDownloader;
import vn.com.fptshop.fmusic.GenreDetailActivity;
import vn.com.fptshop.fmusic.MainActivity;
import vn.com.fptshop.fmusic.OnItemClickListener;
import vn.com.fptshop.fmusic.OnItemClickListenerApp;
import vn.com.fptshop.fmusic.R;
import vn.com.fptshop.fmusic.Utils.Utils;
import vn.com.fptshop.fmusic.adapter.AppAdapter;
import vn.com.fptshop.fmusic.adapter.AppComboAdapter;
import vn.com.fptshop.fmusic.adapter.GenreAdapter;
import vn.com.fptshop.fmusic.adapter.SingerAdapter;
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.download.CallBack;
import vn.com.fptshop.fmusic.download.DownloadManager;
import vn.com.fptshop.fmusic.download.core.DownloadException;
import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.AppCombo;
import vn.com.fptshop.fmusic.models.Genre;
import vn.com.fptshop.fmusic.models.Song;

/**
 * Created by MinhDH on 11/25/15.
 */
public class FragmentApp extends Fragment implements OnItemClickListener<AppCombo>, OnItemClickListenerApp, SearchView.OnQueryTextListener {
    DatabaseHandler db;
    Context context;
    int offset = 0;
    int limit = 50;
    List<AppCombo> listApp;

    ListView list;
    AppComboAdapter adapter;
    AppAdapter appAdapter;
    public Spinner spinner_nav;
    public Spinner spinner_nav_2;

    public HashMap<Integer, List<App>> listDownloadTemp = new HashMap<>();
    public HashMap<Integer, App> songDownloadTemp = new HashMap<>();
    public HashMap<Integer, List<App>> listInstallTemp = new HashMap<>();
    OnItemClickListener onItemClickListener;
    android.app.DownloadManager downloadanager;
    public Stack<App> appStack = new Stack<>();
    boolean checkDownload = false;

    FragmentManager fragmentManager;

    boolean isAppCombo = true;

    public FragmentApp() {
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
        fragmentManager = getActivity().getSupportFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app, container, false);
        if (spinner_nav != null) {
            spinner_nav.setVisibility(View.GONE);
            spinner_nav_2.setVisibility(View.GONE);
        }
//        getActivity().registerReceiver(onComplete,
//                new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//        String servicestring = Context.DOWNLOAD_SERVICE;
//
//        downloadanager = (android.app.DownloadManager) getActivity().getSystemService(servicestring);
        listApp = db.getAllAppCombos(offset, limit);
        System.out.println("TEst " + listApp.size());
        list = (ListView) rootView.findViewById(R.id.listView);

        adapter = new AppComboAdapter(context, listApp);
        adapter.setOnItemClickListener(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item

                if (isAppCombo) {
                    /*Intent i = new Intent(getActivity(), AppDetailActivity.class);
                    // sending data to new activity
                    AppCombo appCombo = listApp.get(position);
                    i.putExtra("app", appCombo.getAppComboId() + "_" + appCombo.getAppComboName() + "_" + appCombo.getAppsCount());
                    startActivity(i);*/

                    AppCombo appCombo = listApp.get(position);

                    Fragment fragment = new FragmentAppDetail();
                    Bundle bundle = new Bundle();
                    bundle.putString("app", appCombo.getAppComboId() + "_" + appCombo.getAppComboName() + "_" + appCombo.getAppsCount());
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container_body, fragment, "FragmentAppDetail");
                    ft.addToBackStack("FragmentAppDetail");
                    ft.commit();
                }
            }
        });

        list.setOnScrollListener(onScrollListener());
        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        System.out.println("listApp Resume" + listApp);

        if (adapter != null) {
            offset =0;
            List<AppCombo> appComboList =  db.getAllAppCombos(offset, limit);
            listApp.clear();
            listApp.addAll(appComboList);
            adapter.notifyDataSetChanged();
            System.out.println("Update APP " + limit + " " + offset + " " + appComboList.size() + " " + listApp.size());
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
                        Log.i("TEST", "loading more data");
                        offset = listApp.size();
                        listApp.addAll(db.getAllAppCombos(offset, limit));
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
    public void onItemClick(View v, int position, AppCombo appCombo) {

//        if (!dir.exists())
//            dir.mkdirs();
//        if (isAppCombo) {
//            if (listDownloadTemp.containsKey(position)) {
//
//                App app = songDownloadTemp.get(position);
//                if (app != null)
//                    DownloadManager.getInstance().pause("http://118.69.201.53:8887/api/Application/Download?ApplicationId=" + app.getApplicationId());
//                listDownloadTemp.remove(position);
//                songDownloadTemp.remove(position);
//            } else {
//                System.out.println("chua co");
//                List<App> apps = db.getAppsDownload(appCombo.getAppComboId());
//                listDownloadTemp.put(position, apps);
//                List<App> songsTemp = listDownloadTemp.get(position);
//                if (songsTemp.size() > 0) {
//                    System.out.println("Length: " + songsTemp.size());
//                    download(position, appCombo);
//                }
//            }
//        } else {
//            System.out.println("APP");
//
//        }
        //DOWNLOADMANAGER
//        List<App> apps = db.getAppsDownload(appCombo.getAppComboId());
//        appStack.addAll(apps);
//        if (checkDownload == false)
//            startDownload();
//        Toast.makeText(context, "Tải " + appCombo.getAppComboName(), Toast.LENGTH_LONG).show();

            boolean check = false;
            for (AppCombo appCombo1 : DataDownloader.appComboList) {
                if (appCombo1.getAppComboId() == appCombo.getAppComboId()) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                DataDownloader.appComboList.add(appCombo);
                Toast.makeText(context, "Đã thêm " + appCombo.getAppComboName() + " vào downloader!", Toast.LENGTH_SHORT).show();
                FragmentDownloader.updateListAppCombo();
                for (int i = 0; i < DataDownloader.appComboList.size(); i++) {
                    if (DataDownloader.appComboList.get(i).getAppComboId() == appCombo.getAppComboId()) {
                        FragmentDownloader.downloadAppCombo(i, appCombo);
                    }
                }
            } else {
                Toast.makeText(context, "Đã có " + appCombo.getAppComboName() + " trong downloader!", Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onItemClickAppCombo(View v, int pos, AppCombo appCombo) {

    }

//    private void startDownload() {
//        if (!appStack.empty()) {
//            checkDownload = true;
//            App app = appStack.pop();
//
//            Uri uri = Uri
//                    .parse("http://118.69.201.53:8887/api/Application/Download?ApplicationId=" + app.getApplicationId());
//            Environment
//                    .getExternalStoragePublicDirectory("FPTShop/App")
//                    .mkdirs();
//            long lastDownload =
//                    downloadanager.enqueue(new android.app.DownloadManager.Request(uri)
//                            .setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI |
//                                    android.app.DownloadManager.Request.NETWORK_MOBILE)
//                            .setAllowedOverRoaming(false)
//                            .setTitle(app.getApplicationName())
//                            .setDestinationInExternalPublicDir("FPTShop/App",
//                                    app.getApplicationName() + ".apk"));
//
//        } else {
//            checkDownload = false;
//        }
//    }

//    private final File dir = new File(Environment.getExternalStorageDirectory(), "FPTShop/App");
//
//    private void download(final int position, final AppCombo appCombo) {
//
//
//        if (listDownloadTemp.containsKey(position)) {
//            List<App> songsTemp = listDownloadTemp.get(position);
//            if (songsTemp.size() > 0) {
//                App app = songsTemp.get(0);
//                songsTemp.remove(0);
//                listDownloadTemp.put(position, songsTemp);
//                System.out.println("Length: " + songsTemp.size());
//                downloader(position, app, appCombo);
//            } else {
//                listDownloadTemp.remove(position);
//            }
//        }
//    }

//    private void downloader(final int position, final App app, final AppCombo appCombo) {
//
//        System.out.println("Size: " + listDownloadTemp.get(position).size());
//
//        DownloadManager.getInstance().download(app.getFileSize(), app.getApplicationName() + ".apk", "http://118.69.201.53:8887/api/Application/Download?ApplicationId=" + app.getApplicationId(), dir, new CallBack() {
//
//
//                    @Override
//                    public void onDownloadStart() {
//
//                        System.out.println("test download");
//                        app.setStatus(App.STATUS_CONNECTING);
//                        db.updateApp(app, App.STATUS_CONNECTING, "");
//                        songDownloadTemp.put(position, app);
////                listDownloadTemp.put(position,song);
////                if (isCurrentListViewItemVisible(position)) {
////                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
////                    holder.tvStatus.setText(appInfo.getStatusText());
////                    holder.btnDownload.setText(appInfo.getButtonText());
////                }
//                    }
//
//                    @Override
//                    public void onConnected(long total, boolean isRangeSupport) {
//                        app.setStatus(Song.STATUS_DOWNLOADING);
//                        db.updateApp(app, Song.STATUS_DOWNLOADING, "");
//                        if (isCurrentListViewItemVisible(position)) {
//                            AppComboAdapter.ViewHolder holder = getViewHolder(position);
//                            holder.download.setText("Tải...");
//                        }
//                    }
//
//                    @Override
//                    public void onProgress(long finished, long total, int progress) {
//                        String downloadPerSize = getDownloadPerSize(finished, total);
////                System.out.println(progress);
////                appInfo.setProgress(progress);
////                appInfo.setDownloadPerSize(downloadPerSize);
//                        app.setStatus(Song.STATUS_DOWNLOADING);
//
//                        if (isCurrentListViewItemVisible(position)) {
//                            AppComboAdapter.ViewHolder holder = getViewHolder(position);
//                            holder.download.setText("Tải..." + progress + "%");
////                    holder.progressBar.setProgress(progress);
////                    holder.tvStatus.setText(appInfo.getStatusText());
////                    holder.btnDownload.setText(appInfo.getButtonText());
//                        }
//                    }
//
//                    public boolean isDownload = false;
//
//                    @Override
//                    public void onComplete() {
//                        app.setStatus(Song.STATUS_COMPLETE);
//                        app.setDownload(Song.STATUS_COMPLETE);
//                        db.updateApp(app, Song.STATUS_COMPLETE, dir + "/" + app.getApplicationName() + ".apk");
//
//
//                        if (listDownloadTemp.containsKey(position)) {
//                            List<App> songsTemp = listDownloadTemp.get(position);
//                            int count = songsTemp.size();
//                            if (songsTemp.size() == 0 && listDownloadTemp.containsKey(position)) {
//                                listDownloadTemp.remove(position);
//                                System.out.println("Xoa ra khoi hashmap");
//                            }
//                            if (songsTemp.size() > 0 && isDownload == false) {
//                                App app1 = songsTemp.get(0);
//                                songsTemp.remove(0);
//                                listDownloadTemp.put(position, songsTemp);
//                                isDownload = true;
//                                if (isCurrentListViewItemVisible(position)) {
//                                    AppComboAdapter.ViewHolder holder = getViewHolder(position);
//                                    holder.proccess.setText((appCombo.getAppsCount() - count) + "/" + appCombo.getAppsCount());
//                                }
//                                List<App> list = listInstallTemp.get(position);
//                                if (list != null) {
//                                    list.add(app1);
//                                } else {
//                                    list = new ArrayList<App>();
//                                    list.add(app);
//                                    list.add(app1);
//                                }
//                                listInstallTemp.put(position, list);
//                                downloader(position, app1, appCombo);
//                            }
//                        } else {
//                            System.out.println("DOWNLOAD XONG");
//                            List<App> list = listInstallTemp.get(position);
//                            if (list != null && isDownload == false) {
//                                for (int i = 0; i < list.size(); i++) {
//                                    App app1 = list.get(i);
//                                    File apk = new File(dir, app1.getApplicationName() + ".apk");
//                                    System.out.println("pack " + apk.getAbsolutePath());
//                                    if (apk.isFile() && apk.exists()) {
//                                        String packageName = Utils.getApkFilePackage(context, apk);
//                                        System.out.println("pack " + packageName);
//                                        Utils.installApp(context, apk);
//                                        if (Utils.isAppInstalled(context, packageName)) {
//                                            app.setStatus(App.STATUS_INSTALLED);
//                                        }
//                                    }
//                                }
//                                listInstallTemp.remove(position);
//                            }
//                            if (isCurrentListViewItemVisible(position) && isDownload == false) {
//                                AppComboAdapter.ViewHolder holder = getViewHolder(position);
//                                holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded_success));
//                                holder.download.setBackgroundResource(R.drawable.action_button_background_3);
//                                holder.download.setText(app.getButtonText());
//                                holder.proccess.setText((appCombo.getAppsCount()) + "/" + appCombo.getAppsCount());
//                            }
//                        }
//
//                    }
//
//                    boolean isPause = false;
//
//                    @Override
//                    public void onDownloadPause() {
//                        app.setStatus(Song.STATUS_PAUSE);
//                        if (isPause == false) {
//                            System.out.println("TEST PASE");
//                            List<App> list = listInstallTemp.get(position);
//                            if (list != null && isDownload == false) {
//                                for (int i = 0; i < list.size(); i++) {
//                                    App app1 = list.get(i);
//                                    File apk = new File(dir, app1.getApplicationName() + ".apk");
//                                    System.out.println("pack " + apk.getAbsolutePath());
//                                    if (apk.length() == app1.getFileSize())
//                                        if (apk.isFile() && apk.exists()) {
//                                            String packageName = Utils.getApkFilePackage(context, apk);
//                                            System.out.println("pack " + packageName);
//                                            Utils.installApp(context, apk);
//                                            if (Utils.isAppInstalled(context, packageName)) {
//                                                app.setStatus(App.STATUS_INSTALLED);
//                                            }
//                                        }
//
//                                }
//                                listInstallTemp.remove(position);
//                            }
//                            isPause = !isPause;
//                        }
//
//                        db.updateApp(app, Song.STATUS_PAUSE, "");
//
//                        File aa = new File(dir, app.getApplicationName() + ".apk");
//                        if (aa.exists()) {
//                            aa.delete();
//                            System.out.println("[DELETE] Success: " + app.getApplicationName());
//                        } else {
//                            System.out.println("[DELETE] File not exists.");
//                        }
//                        if (
//                                isCurrentListViewItemVisible(position)
//                                ) {
//                            AppComboAdapter.ViewHolder holder = getViewHolder(position);
//                            holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
//                            holder.download.setBackgroundResource(R.drawable.action_button_background);
//                            holder.download.setText(app.getButtonText());
//                        }
//                    }
//
//                    @Override
//                    public void onDownloadCancel() {
//                        app.setStatus(Song.STATUS_NOT_DOWNLOAD);
//                        app.setDownload(Song.STATUS_NOT_DOWNLOAD);
//                        System.out.println("TEST CAN");
////                if (isCurrentListViewItemVisible(position)) {
////                    SongAdapter.ViewHolder holder = getViewHolder(position);
////                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
////                    holder.download.setBackgroundResource(R.drawable.action_button_background_2);
////                    holder.download.setText(song.getButtonText());
////                }
//                    }
//
//                    @Override
//                    public void onFailure(DownloadException e) {
//                        app.setStatus(Song.STATUS_DOWNLOAD_ERROR);
//
////                if (isCurrentListViewItemVisible(position)) {
////                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
////                    holder.tvStatus.setText(appInfo.getStatusText());
////                    holder.tvDownloadPerSize.setText("");
////                    holder.btnDownload.setText(appInfo.getButtonText());
////                }
//                        e.printStackTrace();
//                    }
//                }
//
//        );
//    }

//    private boolean isCurrentListViewItemVisible(int position) {
//        int first = list.getFirstVisiblePosition();
//        int last = list.getLastVisiblePosition();
//        return first <= position && position <= last;
//    }
//
//    private AppComboAdapter.ViewHolder getViewHolder(int position) {
//        int childPosition = position - list.getFirstVisiblePosition();
//        View view = list.getChildAt(childPosition);
//        return (AppComboAdapter.ViewHolder) view.getTag();
//    }
//
//
//    private String getDownloadPerSize(long finished, long total) {
//        return DF.format((float) finished / (1024 * 1024)) + "M/" + DF.format((float) total / (1024 * 1024)) + "M";
//    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        List<App> appList = db.searchApp(newText, 0);


        if (newText.length() > 0) {
            isAppCombo = false;
            appAdapter = new AppAdapter(context);
            appAdapter.setData(appList);
            list.setAdapter(appAdapter);
            appAdapter.setOnItemClickListener(this);
            appAdapter.notifyDataSetChanged();
        } else {
            isAppCombo = true;
            listApp = db.getAllAppCombos(offset, limit);
            adapter = new AppComboAdapter(context, listApp);
            adapter.setOnItemClickListener(this);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        System.out.println("KEY " + newText + " SIZE " + appList.size() + " isAppCombo " + isAppCombo);
        return false;
    }

    @Override
    public void onItemClickApp(View v, int position, App app) {
        System.out.println("test " + app.getApplicationId() + "-" + app.getApplicationName());
//        if (!dir.exists())
//            dir.mkdirs();
//        if (app.getStatus() == App.STATUS_DOWNLOADING || app.getStatus() == App.STATUS_CONNECTING) {
//            if (isCurrentListViewItemVisible(position)) {
//                DownloadManager.getInstance().pause("http://118.69.201.53:8887/api/Application/Download?ApplicationId=" + app.getApplicationId());
//            }
//            return;
//        } else if (app.getStatus() == App.STATUS_COMPLETE) {
//            File apk = new File(dir, app.getApplicationName() + ".apk");
//            System.out.println("pack " + apk.getAbsolutePath());
//            if (apk.isFile() && apk.exists()) {
//                String packageName = Utils.getApkFilePackage(context, apk);
////                    app.setPackageName(packageName);
//                System.out.println("pack " + packageName);
//                Utils.installApp(context, apk);
//                if (Utils.isAppInstalled(context, packageName)) {
//                    app.setStatus(App.STATUS_INSTALLED);
//                }
//            }
//        } else {
//            download(position, app);
//        }
//        String servicestring = Context.DOWNLOAD_SERVICE;
//        android.app.DownloadManager downloadanager;
//        downloadanager = (android.app.DownloadManager) getActivity().getSystemService(servicestring);
//        Uri uri = Uri
//                .parse("http://118.69.201.53:8887/api/Application/Download?ApplicationId=" + app.getApplicationId());
//        Environment
//                .getExternalStoragePublicDirectory("FPTShop/App")
//                .mkdirs();
//        long lastDownload =
//                downloadanager.enqueue(new android.app.DownloadManager.Request(uri)
//                        .setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI |
//                                android.app.DownloadManager.Request.NETWORK_MOBILE)
//                        .setAllowedOverRoaming(false)
//                        .setTitle(app.getApplicationName())
////                        .setDescription("")
//                        .setDestinationInExternalPublicDir("FPTShop/App",
//                                app.getApplicationName() + ".apk"));
//
//        v.setEnabled(false);
//        Toast.makeText(context, "Tải " + app.getApplicationName() + ".apk", Toast.LENGTH_LONG).show();

        boolean check = false;
        for (App app1 : DataDownloader.appList) {
            if (app1.getApplicationId()== app.getApplicationId()) {
                check = true;
                break;
            }
        }
        if (!check) {
            DataDownloader.appList.add(app);
            Toast.makeText(context, "Đã thêm " + app.getApplicationName() + " vào downloader!", Toast.LENGTH_SHORT).show();
            FragmentDownloader.updateListGenre();
            for (int i = 0; i < DataDownloader.appList.size(); i++) {
                if (DataDownloader.appList.get(i).getApplicationId() == app.getApplicationId()) {
                    FragmentDownloader.downloadApp(i, app);
                }
            }
        } else {
            Toast.makeText(context, "Đã có " + app.getApplicationName() + " trong downloader!", Toast.LENGTH_SHORT).show();
        }
    }


//    private void download(final int position, final App app) {
//
//        DownloadManager.getInstance().download(app.getFileSize(), app.getApplicationName() + ".apk", "http://118.69.201.53:8887/api/Application/Download?ApplicationId=" + app.getApplicationId(), dir, new CallBack() {
//
//            @Override
//            public void onDownloadStart() {
//                System.out.println("test download");
//                app.setStatus(App.STATUS_CONNECTING);
//                db.updateApp(app, App.STATUS_CONNECTING, "");
////                if (isCurrentListViewItemVisible(position)) {
////                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
////                    holder.tvStatus.setText(appInfo.getStatusText());
////                    holder.btnDownload.setText(appInfo.getButtonText());
////                }
//            }
//
//            @Override
//            public void onConnected(long total, boolean isRangeSupport) {
//                app.setStatus(App.STATUS_DOWNLOADING);
//                db.updateApp(app, App.STATUS_DOWNLOADING, "");
//                if (isCurrentListViewItemVisibleApp(position)) {
//                    AppAdapter.ViewHolder holder = getViewHolderApp(position);
//                    holder.download.setText("Tải...");
//                }
//            }
//
//            @Override
//            public void onProgress(long finished, long total, int progress) {
//                String downloadPerSize = getDownloadPerSize(finished, total);
//                System.out.println(progress);
////                appInfo.setProgress(progress);
////                appInfo.setDownloadPerSize(downloadPerSize);
//                app.setStatus(App.STATUS_DOWNLOADING);
//                if (isCurrentListViewItemVisibleApp(position) && isAppCombo == false) {
//                    AppAdapter.ViewHolder holder = getViewHolderApp(position);
//                    holder.download.setText("Tải..." + progress + "%");
////                    holder.progressBar.setProgress(progress);
////                    holder.tvStatus.setText(appInfo.getStatusText());
////                    holder.btnDownload.setText(appInfo.getButtonText());
//                }
//            }
//
//            @Override
//            public void onComplete() {
//                app.setStatus(App.STATUS_COMPLETE);
//                app.setDownload(App.STATUS_COMPLETE);
//                db.updateApp(app, App.STATUS_COMPLETE, "");
//                File apk = new File(dir, app.getApplicationName() + ".apk");
//                System.out.println("pack " + apk.getAbsolutePath());
//                if (apk.isFile() && apk.exists()) {
//                    String packageName = Utils.getApkFilePackage(context, apk);
////                    app.setPackageName(packageName);
//                    System.out.println("pack " + packageName);
//                    Utils.installApp(context, apk);
//                    if (Utils.isAppInstalled(context, packageName)) {
//                        app.setStatus(App.STATUS_INSTALLED);
//                    }
//                }
////
//                if (isCurrentListViewItemVisibleApp(position)) {
//                    AppAdapter.ViewHolder holder = getViewHolderApp(position);
//                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded));
//                    holder.download.setBackgroundResource(R.drawable.action_button_background_2);
//                    holder.download.setText(app.getButtonText());
//                }
//            }
//
//            @Override
//            public void onDownloadPause() {
//                app.setStatus(App.STATUS_PAUSE);
//                System.out.println("TEST PASE");
//                db.updateApp(app, App.STATUS_PAUSE, "");
//
//                File aa = new File(dir, app.getApplicationName() + ".apk");
//                if (aa.exists()) {
//                    aa.delete();
//                    System.out.println("[DELETE] Success: " + app.getApplicationName());
//                } else {
//                    System.out.println("[DELETE] File not exists.");
//                }
//
//                if (isCurrentListViewItemVisibleApp(position)) {
//                    AppAdapter.ViewHolder holder = getViewHolderApp(position);
//                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
//                    holder.download.setBackgroundResource(R.drawable.action_button_background);
//                    holder.download.setText(app.getButtonText());
//                }
//            }
//
//            @Override
//            public void onDownloadCancel() {
//                app.setStatus(App.STATUS_NOT_DOWNLOAD);
////                appInfo.setDownloadPerSize("");
////                if (isCurrentListViewItemVisible(position)) {
////                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
////                    holder.tvStatus.setText(appInfo.getStatusText());
////                    holder.tvDownloadPerSize.setText("");
////                    holder.btnDownload.setText(appInfo.getButtonText());
////                }
//            }
//
//            @Override
//            public void onFailure(DownloadException e) {
//                app.setStatus(App.STATUS_DOWNLOAD_ERROR);
//
////                if (isCurrentListViewItemVisible(position)) {
////                    ListViewAdapter.ViewHolder holder = getViewHolder(position);
////                    holder.tvStatus.setText(appInfo.getStatusText());
////                    holder.tvDownloadPerSize.setText("");
////                    holder.btnDownload.setText(appInfo.getButtonText());
////                }
//                e.printStackTrace();
//            }
//        });
//    }

//    private boolean isCurrentListViewItemVisibleApp(int position) {
//        int first = list.getFirstVisiblePosition();
//        int last = list.getLastVisiblePosition();
//        return first <= position && position <= last;
//    }
//
//    private AppAdapter.ViewHolder getViewHolderApp(int position) {
//        int childPosition = position - list.getFirstVisiblePosition();
//        View view = list.getChildAt(childPosition);
//        return (AppAdapter.ViewHolder) view.getTag();
//    }
//
//    private static final DecimalFormat DF = new DecimalFormat("0.00");
//    BroadcastReceiver onComplete = new BroadcastReceiver() {
//        public void onReceive(Context ctxt, Intent intent) {
////            findViewById(R.id.start).setEnabled(true);
////            Toast.makeText(ctxt, "KAKA...hi!", Toast.LENGTH_LONG).show();
//            System.out.println("TEST AppComBo");
//            startDownload();
//        }
//    };

}
