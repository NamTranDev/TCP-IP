package vn.com.fptshop.fmusic;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Spinner;


import java.io.File;
import java.util.List;

import vn.com.fptshop.fmusic.Synchronous_Contacts.ContactManager;
import vn.com.fptshop.fmusic.Synchronous_Contacts.SysnContacts;
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.download.DownloadConfiguration;
import vn.com.fptshop.fmusic.download.DownloadManager;
import vn.com.fptshop.fmusic.download.util.FileUtils;
import vn.com.fptshop.fmusic.fragment.FragmentApp;
import vn.com.fptshop.fmusic.fragment.FragmentDownloader;
import vn.com.fptshop.fmusic.fragment.FragmentDownloadApp;
import vn.com.fptshop.fmusic.fragment.FragmentDownloadSong;
import vn.com.fptshop.fmusic.fragment.FragmentGenre;
import vn.com.fptshop.fmusic.fragment.FragmentSinger;
import vn.com.fptshop.fmusic.fragment.FragmentSongHot;
import vn.com.fptshop.fmusic.fragment.FragmentSongNew;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static ActionBar actionBar;
    public static Spinner spinner_nav;
    public static Spinner spinner_nav_2;
    public static FragmentManager fragmentManager;
    public static FragmentSongNew fragmentSongNew;
    public static FragmentSongHot fragmentSongHot;
    public static FragmentSinger fragmentSinger;
    public static FragmentGenre fragmentGenre;
    public static FragmentApp fragmentApp;
    public static FragmentDownloadSong fragmentDownloadSong;
    public static FragmentDownloadApp fragmentDownloadApp;
    public static FragmentDownloader fragmentDownloader;
    String title;

    public static int currentShow = 0;
    public static DatabaseHandler db;

    ContactManager contactManager;
    public static List<ContactManager.PhoneContact> phoneContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        contactManager = new ContactManager(this);
        actionBar = getSupportActionBar();
        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
        spinner_nav_2 = (Spinner) findViewById(R.id.spinner_nav_2);

        db = new DatabaseHandler(this);
        fragmentManager = getSupportFragmentManager();
        fragmentSongNew = new FragmentSongNew();
        fragmentSongHot = new FragmentSongHot();
        fragmentSinger = new FragmentSinger();
        fragmentGenre = new FragmentGenre();
        fragmentApp = new FragmentApp();
        fragmentDownloadApp = new FragmentDownloadApp();
        fragmentDownloadSong = new FragmentDownloadSong();
        fragmentDownloader = new FragmentDownloader();
        fragmentDownloader.onResume();

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displayView(7);
//        displayView(6);
//        displayView(5);
        displayView(3);

        spinner_nav.setVisibility(View.GONE);
        spinner_nav_2.setVisibility(View.GONE);
        initDownloader();
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("BACK");
//            }
//        });

//        Fragment fragment;
//        fragment = new FragmentPlaySong();
//        Bundle bundle = new Bundle();
//
//        bundle.putSerializable("song", listSong.get(position));
//        bundle.putSerializable("songlist", (ArrayList<Song>) listSong);
//        bundle.putString("Mode", AppSetting.MODE_ONLINE);
//
//        System.out.println("song: " + listSong.get(position));
//        System.out.println("song: " + listSong.get(position).getSongName());
//
//        fragment.setArguments(bundle);
//        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.container_footer, fragment);
//            fragmentTransaction.commit();
//        }


        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int stackHeight = getSupportFragmentManager().getBackStackEntryCount();
                if (stackHeight > 0) { // if we have something on the stack (doesn't include the current shown fragment)
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager fm = getSupportFragmentManager();
                            if (fm.getBackStackEntryCount() > 0) {
                                fm.popBackStack();
                            }
                            getSupportFragmentManager().popBackStack();

                        }
                    });
                    navigationView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            System.out.println("onTouchNavigation");
                            return false;
                        }
                    });


                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                } else {

                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    toolbar.setNavigationIcon(R.drawable.ic_home);
                    toolbar.setTitle(title);
                    toolbar.setSubtitle("");
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    });
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }
        });
        if (!dirMusic.exists())
            dirMusic.mkdirs();
        if (!dirApp.exists())
            dirApp.mkdirs();
    }

    private final File dirMusic = new File(Environment.getExternalStorageDirectory(), "FPTShop/Music");
    private final File dirApp = new File(Environment.getExternalStorageDirectory(), "FPTShop/App");

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (PlayMusicService.mp !=null)
            if (!PlayMusicService.mp.isPlaying()) {
                stopService(new Intent(MainActivity.this, PlayMusicService.class));
            }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        if (id == android.R.id.home) {
//            System.out.println(">>>");
//        }


        return super.onOptionsItemSelected(item);
    }

    public MainActivity() {
    }

    private boolean check = true;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_song_new) {
            currentShow = 0;
            displayView(0);

            // Handle the camera action
        } else if (id == R.id.nav_song_hot) {
            currentShow = 1;
            displayView(1);

        } else if (id == R.id.nav_singer) {
            currentShow = 2;
            displayView(2);

        } else if (id == R.id.nav_genre) {
            currentShow = 3;
            displayView(3);

        } else if (id == R.id.nav_app_game) {
            currentShow = 4;
            displayView(4);
            AppSetting.currentAppComboId = 1;
        } else if (id == R.id.nav_download_song) {
            fragmentDownloadSong.onResume();
            currentShow = 5;
            displayView(5);
        } else if (id == R.id.nav_download_app) {
            currentShow = 6;
            displayView(6);
            fragmentDownloadApp.onResume();
        } else if (id == R.id.nav_downloader) {
            currentShow = 7;
            displayView(7);
        }else if (id == R.id.syncContacts) {
            if (check)
            {
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Quét danh bạ . Vui lòng đợi trong ít phút");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        phoneContacts = contactManager.getContacts();
                        check = false;
                        progressDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, SysnContacts.class);
                        startActivity(intent);
                    }
                });
                thread.start();
            }
            else
            {
                Intent intent = new Intent(MainActivity.this, SysnContacts.class);
                startActivity(intent);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayView(int position) {
        Fragment fragment = null;
        title = getString(R.string.app_name);
        switch (position) {
            case 0:
//                fragment = new FragmentSongNew();
                displayFragmentSongNew();
                spinner_nav.setVisibility(View.GONE);
                spinner_nav_2.setVisibility(View.GONE);
                title = getString(R.string.fragment_title_song_new);
                break;
            case 1:
//                fragment = new FragmentSongHot();
                displayFragmentSongHot();
                spinner_nav.setVisibility(View.GONE);
                spinner_nav_2.setVisibility(View.GONE);
                title = getString(R.string.fragment_title_song_hot);
                break;
            case 2:
//                fragment = new FragmentSinger();
                displayFragmentSinger();
                spinner_nav.setVisibility(View.VISIBLE);
                spinner_nav_2.setVisibility(View.VISIBLE);
                title = getString(R.string.fragment_title_singer);
                break;
            case 3:
//                fragment = new FragmentGenre();
                displayFragmentGenre();
                spinner_nav.setVisibility(View.GONE);
                spinner_nav_2.setVisibility(View.GONE);
                title = getString(R.string.fragment_title_genre);
                break;
            case 4:
//                fragment = new FragmentApp();
                displayFragmentApp();
                spinner_nav.setVisibility(View.GONE);
                spinner_nav_2.setVisibility(View.GONE);
                title = getString(R.string.fragment_title_app);
                break;
            case 5:
//                fragment = new FragmentDownloadSong();
                displayFragmentDownloadSong();
                spinner_nav.setVisibility(View.GONE);
                spinner_nav_2.setVisibility(View.GONE);
                title = getString(R.string.fragment_title_download_song);
                break;
            case 6:
//                fragment = new FragmentDownloadApp();
                displayFragmentDownloadApp();
                spinner_nav.setVisibility(View.GONE);
                spinner_nav_2.setVisibility(View.GONE);
                title = getString(R.string.fragment_title_download_app);
                break;
            case 7:
                displayFragmentDownloader();
                spinner_nav.setVisibility(View.GONE);
                spinner_nav_2.setVisibility(View.GONE);
                title = getString(R.string.fragment_title_downloader);
                break;
            default:
                break;
        }

//        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//
//            // set the toolbar title
        getSupportActionBar().setTitle(title);
//  }
    }

    protected void displayFragmentSongNew() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentSongNew.isAdded()) { // if the fragment is already in container
            ft.show(fragmentSongNew);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.container_body, fragmentSongNew, "fragmentSongNew");
        }
        // Hide fragment B
        if (fragmentSongHot.isAdded()) {
            ft.hide(fragmentSongHot);
        }
        // Hide fragment C
        if (fragmentSinger.isAdded()) {
            ft.hide(fragmentSinger);
        }
        if (fragmentGenre.isAdded()) {
            ft.hide(fragmentGenre);
        }
        if (fragmentApp.isAdded()) {
            ft.hide(fragmentApp);
        }
        if (fragmentDownloadSong.isAdded()) {
            ft.hide(fragmentDownloadSong);
        }
        if (fragmentDownloadApp.isAdded()) {
            ft.hide(fragmentDownloadApp);
        }
        if (fragmentDownloader.isAdded()) {
            ft.hide(fragmentDownloader);
        }
        // Commit changes
        ft.commit();
    }

    protected void displayFragmentSongHot() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentSongHot.isAdded()) { // if the fragment is already in container
            ft.show(fragmentSongHot);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.container_body, fragmentSongHot, "fragmentSongHot");
        }
        // Hide fragment B
        if (fragmentSongNew.isAdded()) {
            ft.hide(fragmentSongNew);
        }
        // Hide fragment C
        if (fragmentSinger.isAdded()) {
            ft.hide(fragmentSinger);
        }
        if (fragmentGenre.isAdded()) {
            ft.hide(fragmentGenre);
        }
        if (fragmentApp.isAdded()) {
            ft.hide(fragmentApp);
        }
        if (fragmentDownloadSong.isAdded()) {
            ft.hide(fragmentDownloadSong);
        }
        if (fragmentDownloadApp.isAdded()) {
            ft.hide(fragmentDownloadApp);
        }
        if (fragmentDownloader.isAdded()) {
            ft.hide(fragmentDownloader);
        }
        // Commit changes
        ft.commit();
    }

    protected void displayFragmentSinger() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentSinger.isAdded()) { // if the fragment is already in container
            ft.show(fragmentSinger);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.container_body, fragmentSinger, "fragmentSinger");
        }
        // Hide fragment B
        if (fragmentSongNew.isAdded()) {
            ft.hide(fragmentSongNew);
        }
        // Hide fragment C
        if (fragmentSongHot.isAdded()) {
            ft.hide(fragmentSongHot);
        }
        if (fragmentGenre.isAdded()) {
            ft.hide(fragmentGenre);
        }
        if (fragmentApp.isAdded()) {
            ft.hide(fragmentApp);
        }
        if (fragmentDownloadSong.isAdded()) {
            ft.hide(fragmentDownloadSong);
        }
        if (fragmentDownloadApp.isAdded()) {
            ft.hide(fragmentDownloadApp);
        }
        if (fragmentDownloader.isAdded()) {
            ft.hide(fragmentDownloader);
        }
        // Commit changes
        ft.commit();
    }

    public static void displayFragmentGenre() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (fragmentGenre.isAdded()) { // if the fragment is already in container
            ft.show(fragmentGenre);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.container_body, fragmentGenre, "fragmentGenre");
        }
        // Hide fragment B
        if (fragmentSongNew.isAdded()) {
            ft.hide(fragmentSongNew);
        }
        // Hide fragment C
        if (fragmentSongHot.isAdded()) {
            ft.hide(fragmentSongHot);
        }
        if (fragmentSinger.isAdded()) {
            ft.hide(fragmentSinger);
        }
        if (fragmentApp.isAdded()) {
            ft.hide(fragmentApp);
        }
        if (fragmentDownloadSong.isAdded()) {
            ft.hide(fragmentDownloadSong);
        }
        if (fragmentDownloadApp.isAdded()) {
            ft.hide(fragmentDownloadApp);
        }
        if (fragmentDownloader.isAdded()) {
            ft.hide(fragmentDownloader);
        }
        // Commit changes
        ft.commit();
    }

    public static void hideFragmentGenre() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
//        if (fragmentGenre.isAdded()) { // if the fragment is already in container
//            ft.show(fragmentGenre);
//        } else { // fragment needs to be added to frame container
//            ft.add(R.id.container_body, fragmentGenre, "fragmentGenre");
//        }
        if (fragmentGenre.isAdded()) {
            ft.hide(fragmentGenre);
        }
        // Hide fragment B
        if (fragmentSongNew.isAdded()) {
            ft.hide(fragmentSongNew);
        }
        // Hide fragment C
        if (fragmentSongHot.isAdded()) {
            ft.hide(fragmentSongHot);
        }
        if (fragmentSinger.isAdded()) {
            ft.hide(fragmentSinger);
        }
        if (fragmentApp.isAdded()) {
            ft.hide(fragmentApp);
        }
        if (fragmentDownloadSong.isAdded()) {
            ft.hide(fragmentDownloadSong);
        }
        if (fragmentDownloadApp.isAdded()) {
            ft.hide(fragmentDownloadApp);
        }
        if (fragmentDownloader.isAdded()) {
            ft.hide(fragmentDownloader);
        }
        // Commit changes
        ft.commit();
    }

    protected void displayFragmentApp() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentApp.isAdded()) { // if the fragment is already in container
            ft.show(fragmentApp);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.container_body, fragmentApp, "fragmentApp");
        }
        // Hide fragment B
        if (fragmentSongNew.isAdded()) {
            ft.hide(fragmentSongNew);
        }
        // Hide fragment C
        if (fragmentSongHot.isAdded()) {
            ft.hide(fragmentSongHot);
        }
        if (fragmentSinger.isAdded()) {
            ft.hide(fragmentSinger);
        }
        if (fragmentGenre.isAdded()) {
            ft.hide(fragmentGenre);
        }
        if (fragmentDownloadSong.isAdded()) {
            ft.hide(fragmentDownloadSong);
        }
        if (fragmentDownloadApp.isAdded()) {
            ft.hide(fragmentDownloadApp);
        }
        if (fragmentDownloader.isAdded()) {
            ft.hide(fragmentDownloader);
        }
        // Commit changes
        ft.commit();
    }

    protected void displayFragmentDownloadSong() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentDownloadSong.isAdded()) { // if the fragment is already in container
            ft.show(fragmentDownloadSong);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.container_body, fragmentDownloadSong, "fragmentDownloadSong");
        }
        // Hide fragment B
        if (fragmentSongNew.isAdded()) {
            ft.hide(fragmentSongNew);
        }
        // Hide fragment C
        if (fragmentSongHot.isAdded()) {
            ft.hide(fragmentSongHot);
        }
        if (fragmentSinger.isAdded()) {
            ft.hide(fragmentSinger);
        }
        if (fragmentApp.isAdded()) {
            ft.hide(fragmentApp);
        }
        if (fragmentGenre.isAdded()) {
            ft.hide(fragmentGenre);
        }
        if (fragmentDownloadApp.isAdded()) {
            ft.hide(fragmentDownloadApp);
        }
        if (fragmentDownloader.isAdded()) {
            ft.hide(fragmentDownloader);
        }
        // Commit changes
        ft.commit();
    }

    protected void displayFragmentDownloadApp() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentDownloadApp.isAdded()) { // if the fragment is already in container
            ft.show(fragmentDownloadApp);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.container_body, fragmentDownloadApp, "fragmentDownloadApp");
        }
        // Hide fragment B
        if (fragmentSongNew.isAdded()) {
            ft.hide(fragmentSongNew);
        }
        // Hide fragment C
        if (fragmentSongHot.isAdded()) {
            ft.hide(fragmentSongHot);
        }
        if (fragmentSinger.isAdded()) {
            ft.hide(fragmentSinger);
        }
        if (fragmentApp.isAdded()) {
            ft.hide(fragmentApp);
        }
        if (fragmentGenre.isAdded()) {
            ft.hide(fragmentGenre);
        }
        if (fragmentDownloadSong.isAdded()) {
            ft.hide(fragmentDownloadSong);
        }
        if (fragmentDownloader.isAdded()) {
            ft.hide(fragmentDownloader);
        }
        // Commit changes
        ft.commit();
    }

    protected void displayFragmentDownloader() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentDownloader.isAdded()) { // if the fragment is already in container
            ft.show(fragmentDownloader);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.container_body, fragmentDownloader, "fragmentDownloader");
        }
        // Hide fragment B
        if (fragmentSongNew.isAdded()) {
            ft.hide(fragmentSongNew);
        }
        // Hide fragment C
        if (fragmentSongHot.isAdded()) {
            ft.hide(fragmentSongHot);
        }
        if (fragmentSinger.isAdded()) {
            ft.hide(fragmentSinger);
        }
        if (fragmentApp.isAdded()) {
            ft.hide(fragmentApp);
        }
        if (fragmentGenre.isAdded()) {
            ft.hide(fragmentGenre);
        }
        if (fragmentDownloadSong.isAdded()) {
            ft.hide(fragmentDownloadSong);
        }
        if (fragmentDownloadApp.isAdded()) {
            ft.hide(fragmentDownloadApp);
        }
        // Commit changes
        ft.commit();
    }

    private void initDownloader() {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setDownloadDir(FileUtils.getDefaultDownloadDir(getApplicationContext()));
        configuration.setMaxThreadNum(10);
        DownloadManager.getInstance().init(getApplicationContext(), configuration);
    }


}
