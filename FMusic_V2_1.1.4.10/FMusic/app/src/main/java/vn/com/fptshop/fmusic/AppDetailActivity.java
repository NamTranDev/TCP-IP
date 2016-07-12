package vn.com.fptshop.fmusic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import vn.com.fptshop.fmusic.Utils.Utils;
import vn.com.fptshop.fmusic.adapter.AppAdapter;
import vn.com.fptshop.fmusic.adapter.SongAdapter;
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.download.CallBack;
import vn.com.fptshop.fmusic.download.DownloadManager;
import vn.com.fptshop.fmusic.download.core.DownloadException;
import vn.com.fptshop.fmusic.fragment.FragmentPlaySong;
import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.Song;

public class AppDetailActivity extends AppCompatActivity implements OnItemClickListenerApp {
    DatabaseHandler db;
    Context context;
    int offset = 0;
    int limit = 20;
    int id = 0;

    List<App> listApp;
    ListView list;
    AppAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        db = new DatabaseHandler(this);
        context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        // getting attached intent data
        String appTemp = i.getStringExtra("app");
        StringTokenizer tokenizer = new StringTokenizer(appTemp, "_");
        int appId = 0;
        String appName = "";
        int totalApps = 0;
        while (tokenizer.hasMoreTokens()) {
            appId = Integer.parseInt(tokenizer.nextToken());
            appName = tokenizer.nextToken();
            totalApps = Integer.parseInt(tokenizer.nextToken());
        }
        id = appId;
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setSubtitle("Có " + totalApps + " ứng dụng.");
        listApp = db.getAppsLimit(id, offset, limit);

        list = (ListView) findViewById(R.id.listView);
        adapter = new AppAdapter(this);
        adapter.setOnItemClickListener(this);
        list.setAdapter(adapter);
        adapter.setData(listApp);
        list.setOnScrollListener(onScrollListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
                        listApp.addAll(db.getAppsLimit(id, offset, limit));
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
        String servicestring = Context.DOWNLOAD_SERVICE;
        android.app.DownloadManager downloadanager;
        downloadanager = (android.app.DownloadManager) getSystemService(servicestring);
        Uri uri = Uri
                .parse("http://118.69.201.53:8887/api/Application/Download?ApplicationId=" + app.getApplicationId());
        Environment
                .getExternalStoragePublicDirectory("FPTShop/App")
                .mkdirs();
        long lastDownload =
                downloadanager.enqueue(new android.app.DownloadManager.Request(uri)
                        .setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI |
                                android.app.DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(app.getApplicationName())
//                        .setDescription("")
                        .setDestinationInExternalPublicDir("FPTShop/App",
                                app.getApplicationName()+".apk"));

        v.setEnabled(false);
        Toast.makeText(context, "Tải " + app.getApplicationName() + ".apk", Toast.LENGTH_LONG).show();
    }

    private final File dir = new File(Environment.getExternalStorageDirectory(), "FPTShop/App");

    private void download(final int position, final App app) {

        DownloadManager.getInstance().download(app.getFileSize(), app.getApplicationName() + ".apk", "http://118.69.201.53:8887/api/Application/Download?ApplicationId=" + app.getApplicationId(), dir, new CallBack() {

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
                if (isCurrentListViewItemVisible(position)) {
                    AppAdapter.ViewHolder holder = getViewHolder(position);
                    holder.download.setText("Tải...");
                }
            }

            @Override
            public void onProgress(long finished, long total, int progress) {
                String downloadPerSize = getDownloadPerSize(finished, total);
                System.out.println(progress);
//                appInfo.setProgress(progress);
//                appInfo.setDownloadPerSize(downloadPerSize);
                app.setStatus(App.STATUS_DOWNLOADING);
                if (isCurrentListViewItemVisible(position)) {
                    AppAdapter.ViewHolder holder = getViewHolder(position);
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
                db.updateApp(app, App.STATUS_COMPLETE, "");
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
//
                if (isCurrentListViewItemVisible(position)) {
                    AppAdapter.ViewHolder holder = getViewHolder(position);
                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text_downloaded));
                    holder.download.setBackgroundResource(R.drawable.action_button_background_2);
                    holder.download.setText(app.getButtonText());
                }
            }

            @Override
            public void onDownloadPause() {
                app.setStatus(App.STATUS_PAUSE);
                System.out.println("TEST PASE");
                db.updateApp(app, App.STATUS_PAUSE, "");

                File aa = new File(dir, app.getApplicationName() + ".apk");
                if (aa.exists()) {
                    aa.delete();
                    System.out.println("[DELETE] Success: " + app.getApplicationName());
                } else {
                    System.out.println("[DELETE] File not exists.");
                }

                if (isCurrentListViewItemVisible(position)) {
                    AppAdapter.ViewHolder holder = getViewHolder(position);
                    holder.download.setTextColor(context.getResources().getColor(R.color.button_download_text));
                    holder.download.setBackgroundResource(R.drawable.action_button_background);
                    holder.download.setText(app.getButtonText());
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

    private AppAdapter.ViewHolder getViewHolder(int position) {
        int childPosition = position - list.getFirstVisiblePosition();
        View view = list.getChildAt(childPosition);
        return (AppAdapter.ViewHolder) view.getTag();
    }

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    private String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "M/" + DF.format((float) total / (1024 * 1024)) + "M";
    }
}
