package vn.com.fptshop.fmusic.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
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

import vn.com.fptshop.fmusic.MainActivity;
import vn.com.fptshop.fmusic.OnItemClickListener;
import vn.com.fptshop.fmusic.R;
import vn.com.fptshop.fmusic.Utils.Utils;
import vn.com.fptshop.fmusic.adapter.AppDownloadAdapter;
import vn.com.fptshop.fmusic.adapter.SongDownloadAdapter;
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.AppCombo;
import vn.com.fptshop.fmusic.models.Song;

/**
 * Created by MinhDH on 11/25/15.
 */
public class FragmentDownloadApp extends Fragment implements OnItemClickListener<App>, SearchView.OnQueryTextListener {

    DatabaseHandler db;
    Context context;
    int offset = 0;
    int limit = 20;
    List<App> listApp;

    ListView list;
    AppDownloadAdapter adapter;

    public Spinner spinner_nav;
    public Spinner spinner_nav_2;
    public static int a = 0;

    public FragmentDownloadApp() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getActivity());
        context = getActivity();
        spinner_nav = MainActivity.spinner_nav;
        spinner_nav_2 = MainActivity.spinner_nav_2;
        listApp = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_download_app, container, false);
        spinner_nav.setVisibility(View.GONE);
        spinner_nav_2.setVisibility(View.GONE);

        listApp = db.getAppsDownloadLimit(limit, offset);
        a++;
//        File root = Environment
//                .getExternalStoragePublicDirectory("FPTShop/App");
//        if (!root.exists())
//            root.mkdirs();
//        System.out.println(root.getAbsoluteFile());
//        File[] listFiles = root.listFiles();
//
//        for (File file : listFiles) {
//            System.out.println(file.getName());
//            App app = new App();
//            app.setApplicationName(file.getName().replace(".apk", "").trim());
//            app.setLocal(file.getAbsolutePath());
//            app.setFileSize((int) file.length());
//            String packageName = Utils.getApkFilePackage(context, file);
//            app.setPackageName(packageName);
//            if (app != null)
//                listApp.add(app);
//        }

        list = (ListView) rootView.findViewById(R.id.listView);
        adapter = new AppDownloadAdapter(context, listApp);
        adapter.setOnItemClickListener(this);
        list.setAdapter(adapter);

        list.setOnScrollListener(onScrollListener());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                App app = listApp.get(position);
                System.out.println(app.getApplicationName());
                File apk = new File(dir, app.getApplicationName() + ".apk");
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
            }
        });
        return rootView;
    }

    private final File dir = new File(Environment.getExternalStorageDirectory(), "FPTShop/App");

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

                        offset = listApp.size();
                        listApp.addAll(db.getAppsDownloadLimit(limit, offset));
                        Log.i("TEST", "loading more data " + listApp.size());
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
    public void onItemClick(View v, int position, App app) {
//        deleteCell(v, position, song);
        removeListItem(v, position, app);
    }

    @Override
    public void onItemClickAppCombo(View v, int pos, AppCombo appCombo) {

    }

    @Override
    public void onItemClickApp(View v, int pos, App app) {

    }


    protected void removeListItem(View rowView, final int positon, final App app) {
        final Animation animation = AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.slide_in_left);
        rowView.startAnimation(animation);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            public void run() {
                String path = app.getLocal();
                File file = new File(path);
                try {
                    if (file.exists()) {
                        file.delete();
                        db.updateApp(app, App.STATUS_NOT_DOWNLOAD, "");
                        System.out.println("[DELETE] Success: " + app.getApplicationName());
                    } else {
                        db.updateApp(app, Song.STATUS_NOT_DOWNLOAD, "");
                        System.out.println("[DELETE] File not exists.");
                    }
                    if (positon < listApp.size())
                        listApp.remove(positon);
                } catch (Exception e) {
                    System.out.println("[DELETE] ERRO");
                }
                adapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("RESUM " + a);
        if (adapter != null) {
            List<App> appList = db.getAppsDownloadLimit(limit, 0);
            listApp.clear();
            listApp.addAll(appList);
            adapter.notifyDataSetChanged();
            System.out.println("Update " + limit + " " + offset + " " + appList.size() + " " + listApp.size());
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {
            List<App> appList = db.searchApp(newText, 1);
            listApp.clear();
            listApp.addAll(appList);
            adapter.notifyDataSetChanged();
            System.out.println("TEST  1" + newText + " SIZE: " + listApp.size());
        } else {
            listApp.clear();
            offset = 0;
            listApp.addAll(db.getAppsDownloadLimit(limit, offset));
            System.out.println("TEST  2" + newText + " SIZE: " + listApp.size());
            adapter.notifyDataSetChanged();
        }


//        File root = Environment
//                .getExternalStoragePublicDirectory("FPTShop/App");
//        if (!root.exists())
//            root.mkdirs();
//        System.out.println(root.getAbsoluteFile());
//        if (newText.length() > 0) {
//            File[] listFiles = root.listFiles();
//            List<App> appList = new ArrayList<>();
//            for (File file : listFiles) {
//                System.out.println(file.getName());
//                if (file.getName().toLowerCase().contains(newText.toLowerCase())) {
//                    App app = new App();
//                    app.setApplicationName(file.getName().replace(".apk", "").trim());
//                    app.setLocal(file.getAbsolutePath());
//                    app.setFileSize((int) file.length());
//                    String packageName = Utils.getApkFilePackage(context, file);
//                    app.setPackageName(packageName);
//                    if (app != null)
//                        appList.add(app);
//                }
//            }
//            listApp.clear();
//            listApp.addAll(appList);
//            adapter.notifyDataSetChanged();
//        } else {
//
//            listApp.clear();
//            File[] listFiles = root.listFiles();
//            List<App> appList = new ArrayList<>();
//            for (File file : listFiles) {
//                System.out.println(file.getName());
//                App app = new App();
//                app.setApplicationName(file.getName().replace(".apk", "").trim());
//                app.setLocal(file.getAbsolutePath());
//                app.setFileSize((int) file.length());
//                String packageName = Utils.getApkFilePackage(context, file);
//                app.setPackageName(packageName);
//                if (app != null)
//                    appList.add(app);
//            }
//
//            listApp.addAll(appList);
//            System.out.println("TEST  2" + newText + " SIZE: " + listApp.size());
//            adapter.notifyDataSetChanged();
//        }
        return false;
    }
}
