package vn.com.fptshop.fmusic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import vn.com.fptshop.fmusic.Synchronous_Contacts.ContactManager;
import vn.com.fptshop.fmusic.database.DatabaseHandler;
import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.AppCombo;
import vn.com.fptshop.fmusic.models.AppVersion;
import vn.com.fptshop.fmusic.models.DBVersion;
import vn.com.fptshop.fmusic.models.Genre;
import vn.com.fptshop.fmusic.models.National;
import vn.com.fptshop.fmusic.models.Singer;
import vn.com.fptshop.fmusic.models.Song;
import vn.com.fptshop.fmusic.services.ParseJSON;
import vn.com.fptshop.fmusic.services.ServiceHandler;

public class FirstActivity extends Activity implements IProccessBar {
    DatabaseHandler db;
    ServiceHandler sh;
    AppVersion appVersion;
    DBVersion dbVersionTemp;
    Context context;
    TextView progressTitle, versionTitle;
    IProccessBar iProccessBar;
    public static List<ContactManager.PhoneContact> phoneContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_first);

        progressTitle = (TextView) findViewById(R.id.progressTitle);
        versionTitle = (TextView) findViewById(R.id.versionTitle);
        String versionName = BuildConfig.VERSION_NAME;
        versionTitle.setText("Version " + versionName + " Beta");
        iProccessBar = this;
        db = new DatabaseHandler(this, iProccessBar);
        sh = new ServiceHandler();
        if (isNetWorkAvailable(context)) {
            new GetVersion().execute();
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder.setTitle("Thông Báo");

            alertDialogBuilder.setMessage("Kiểm tra lại kết nối internet!");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    FirstActivity.this.finish();
                }

            });
            AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.show();
        }
    }

    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = connec
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifi.isConnected() || mobileNetwork.isConnected();
    }

    private void runFirst(int key) {
        System.out.println("Run First " + key);
        db.dropDatabase();
        new GetAPI().execute(key);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(String name, int value, int size) {
//        final double process = ((double) value / (double) size) * 100;
        final String nameTemp = name;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                progressTitle.setText("Đang cập nhật " + nameTemp + "..." + round(process, 1) + "%");
                progressTitle.setText("Đang cập nhật " + nameTemp + "...");
            }
        });

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetAPI extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Integer... arg0) {
            // Creating service handler class instance
            int i = arg0[0];
            if (i == 0) {
                System.out.println("GHI LOG");
                runLog();
            }
            getNational();
            getGenre();
            getAppCombo();
            getSongs();

            getDBVersion();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            System.out.println(">>" + result);
            startApp();

            // Dismiss the progress dialog

            /**
             * Updating parsed JSON data into ListView
             * */

        }

        protected void getDBVersion() {
            String jsonDBVersion = sh.makeServiceCall(AppSetting.URL + "/api/System/GetLatestDBVersion", ServiceHandler.GET);
            DBVersion dbVersion = ParseJSON.parseJsonToDBVersion(jsonDBVersion);
            db.addDBVersion(dbVersion);
        }

        protected void getNational() {
            String jsonNational = sh.makeServiceCall(AppSetting.URL + "/api/Music/getNationals", ServiceHandler.GET);
            List<National> nationalList = ParseJSON.parseJsonToNationals(jsonNational);
            db.addNationals(nationalList);
            for (National national : nationalList) {
                getSingerOfNational(national.getNationalId());
            }
        }

        protected void getSingerOfNational(int id) {
            String jsonSinger = sh.makeServiceCall(AppSetting.URL + "/api/Music/GetSingersByNational?nationalId=" + id, ServiceHandler.GET);
            List<Singer> singerList = ParseJSON.parseJsonToSingers(jsonSinger);
            db.addSingers(singerList);
        }

        protected void getGenre() {
            String jsonGenre = sh.makeServiceCall(AppSetting.URL + "/api/Music/getGenres", ServiceHandler.GET);
            List<Genre> genreList = ParseJSON.parseJsonToGenres(jsonGenre);
            db.addGenres(genreList);
        }

        protected void getAppCombo() {
            String jsonAppCombo = sh.makeServiceCall(AppSetting.URL + "/api/Application/GetAppCombo?appComboTypeId=1&platformId=1", ServiceHandler.GET);
            List<AppCombo> appComboList = ParseJSON.parseJsonToAppCombos(jsonAppCombo);
            db.addAppCombos(appComboList);
            for (AppCombo appCombo : appComboList) {
                getComboApplications(appCombo.getAppComboId());
                System.out.println(appCombo.getAppComboId());
            }
        }

        protected void getComboApplications(int id) {
            String jsonApp = sh.makeServiceCall(AppSetting.URL + "/api/Application/GetComboApplications?appComboId=" + id, ServiceHandler.GET);
            List<App> appList = ParseJSON.parseJsonToApplications(jsonApp);
            db.addApps(appList);
        }

        protected void getSongs() {
            String jsonSong = sh.makeServiceCall(AppSetting.URL + "/api/Music/GetAllSongs", ServiceHandler.GET);
            List<Song> songList = ParseJSON.parseJsonToSongs(jsonSong);
            System.out.println("TEST " + songList.size());
            db.addSongs(songList);
        }

        protected void runLog() {
            String imei = getDeviceId(context);
            if (imei == null) {
                imei = "";
            }
            WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            if (ip == null) {
                ip = "";
            }

            String mPhoneNumber = getphoneNumber(context);
            if (mPhoneNumber == null) {
                mPhoneNumber = "";
            }

            System.out.println("Phone: " + mPhoneNumber);

            String jsonSong = sh.makeServiceCall(AppSetting.URL + "/api/ActionLog/Add?userCode=" + imei + "&shopCode=" + ip + "&action=install%20app%20v2&tags=android,", ServiceHandler.GET);
            System.out.println("LOG " + jsonSong);
        }

        public String getDeviceId(Context context) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        }

        public String getphoneNumber(Context context) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getLine1Number();
        }
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetVersion extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            getAppVersion();
            getDBVersion();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            String versionName = BuildConfig.VERSION_NAME;
            System.out.println("App Version: " + appVersion.getVersion() + " || " + versionName);
            if (appVersion != null) {
                if (!appVersion.getVersion().equalsIgnoreCase(versionName)) {
                    System.out.println("RUN UPDATE APP " + appVersion.getUrl());
                    openAlert(appVersion);
                } else {
                    DBVersion dbVersion = db.getAllDBVersion();
                    if (dbVersion.getDbVersionId() > 0) {
                        if (dbVersionTemp.getDbVersionId() > dbVersion.getDbVersionId()) {
                            System.out.println("UPDATE DATABASE " + dbVersionTemp.getDbVersionId());
                            db.dropDatabase();
                            runFirst(1);
                        } else {
                            System.out.println("Run App");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    startApp();
                                }
                            }, 1000);
                        }
                    } else {

                        runFirst(0);

                    }
                }

            }

        }

        protected void getAppVersion() {
            String jsonAppVersion = sh.makeServiceCall(AppSetting.URL + "/api/System/GetLatestAppVersion?deviceType=1", ServiceHandler.GET);
            appVersion = ParseJSON.parseJsonToAppVersion(jsonAppVersion);
        }

        protected void getDBVersion() {
            String jsonDBVersion = sh.makeServiceCall(AppSetting.URL + "/api/System/GetLatestDBVersion", ServiceHandler.GET);
            dbVersionTemp = ParseJSON.parseJsonToDBVersion(jsonDBVersion);
        }

        private void openAlert(final AppVersion appVersion) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder.setTitle("Cập nhật ứng dụng");

            alertDialogBuilder.setMessage("Version update " + appVersion.getVersion());
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appVersion.getUrl()));
                    startActivity(browserIntent);
                    FirstActivity.this.finish();
                }

            });

            alertDialogBuilder.setNegativeButton("Để sau", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    DBVersion dbVersion = db.getAllDBVersion();
                    if (dbVersion.getDbVersionId() > 0) {
                        if (dbVersionTemp.getDbVersionId() > dbVersion.getDbVersionId()) {
                            System.out.println("UPDATE DATABASE " + dbVersionTemp.getDbVersionId());
                            db.dropDatabase();
                            runFirst(1);
                        } else {
                            System.out.println("Run App");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    startApp();
                                }
                            }, 2000);
                        }
                    } else {

                        runFirst(0);

                    }
                }

            });

            AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.show();

        }


    }

    public void startApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.out.println("SONG: " + db.getSongCount());
    }
}
