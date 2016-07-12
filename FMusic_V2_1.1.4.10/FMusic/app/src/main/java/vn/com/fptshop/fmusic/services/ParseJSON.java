package vn.com.fptshop.fmusic.services;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.AppCombo;
import vn.com.fptshop.fmusic.models.AppVersion;
import vn.com.fptshop.fmusic.models.DBVersion;
import vn.com.fptshop.fmusic.models.Genre;
import vn.com.fptshop.fmusic.models.National;
import vn.com.fptshop.fmusic.models.Singer;
import vn.com.fptshop.fmusic.models.Song;

/**
 * Created by MinhDH on 11/26/15.
 */
public class ParseJSON {

    public static List<National> parseJsonToNationals(String json) {
        List<National> nationalList = new ArrayList<>();
        if (json != null) {
            try {
                // Getting JSON Array node
                JSONArray list = new JSONArray(json);
                // looping through All National
                for (int i = 0; i < list.length(); i++) {
                    JSONObject c = list.getJSONObject(i);

                    String nationalIdTemp = c.getString("NationalId");
                    String nationalNameTemp = c.getString("NationalName");
                    String totalSongsTemp = c.getString("TotalSongs");

                    // adding national to national list
                    National national = new National();
                    national.setNationalId(Integer.parseInt(nationalIdTemp));
                    national.setNationalName(nationalNameTemp);
                    national.setTotalSongs(Integer.parseInt(totalSongsTemp));
                    nationalList.add(national);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return nationalList;
    }

    public static List<Singer> parseJsonToSingers(String json) {
        List<Singer> singerList = new ArrayList<>();
        if (json != null) {
            try {
                // Getting JSON Array node
                JSONArray list = new JSONArray(json);
                // looping through All National
                for (int i = 0; i < list.length(); i++) {
                    JSONObject c = list.getJSONObject(i);

                    String singerIdTemp = c.getString("SingerId");
                    String singerNameTemp = c.getString("SingerName");
                    String nationalIdTemp = c.getString("NationalId");
                    String totalSongsTemp = c.getString("TotalSongs");

                    // adding singer to singer list
                    Singer singer = new Singer();
                    singer.setSingerId(Integer.parseInt(singerIdTemp));
                    singer.setSingerName(singerNameTemp);
                    singer.setNationalId(Integer.parseInt(nationalIdTemp));
                    singer.setTotalSongs(Integer.parseInt(totalSongsTemp));
                    singerList.add(singer);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return singerList;
    }

    public static List<Genre> parseJsonToGenres(String json) {
        List<Genre> genreList = new ArrayList<>();
        if (json != null) {
            try {
                // Getting JSON Array node
                JSONArray list = new JSONArray(json);
                // looping through All Genre
                for (int i = 0; i < list.length(); i++) {
                    JSONObject c = list.getJSONObject(i);

                    String genreIdTemp = c.getString("GenreId");
                    String genreNameTemp = c.getString("GenreName");
                    String totalSongsTemp = c.getString("TotalSongs");
                    String sortIndexTemp = c.getString("SortIndex");
                    String iconURL = c.getString("IconURL");

                    // adding genre to genre list
                    Genre genre = new Genre();
                    genre.setGenreId(Integer.parseInt(genreIdTemp));
                    genre.setGenreName(genreNameTemp);
                    genre.setTotalSongs(Integer.parseInt(totalSongsTemp));
                    genre.setSortIndex(Integer.parseInt(sortIndexTemp));
                    genre.setThumbnail(iconURL);
                    genreList.add(genre);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return genreList;
    }

    public static List<Song> parseJsonToSongs(String json) {
        List<Song> songList = new ArrayList<>();
        if (json != null) {
            try {
                // Getting JSON Array node
                JSONArray list = new JSONArray(json);
                // looping through All Song
                for (int i = 0; i < list.length(); i++) {
                    JSONObject c = list.getJSONObject(i);

                    String songIdTemp = c.getString("SongId");
                    String songNameTemp = c.getString("SongName");
                    String singerIdTemp = c.getString("SingerId");
                    String singerNameTemp = c.getString("SingerName");
                    String nationalIdTemp = c.getString("NationalId");
                    String nationalNameTemp = c.getString("NationalName");
                    String genreIdTemp = c.getString("GenreId");
                    String genreNameTemp = c.getString("GenreName");
                    String fileSizeTemp = c.getString("FileSize");

                    // adding song to song list
                    Song song = new Song();
                    song.setSongId(Integer.parseInt(songIdTemp));
                    song.setSongName(songNameTemp);
                    if (singerIdTemp != "null") {
                        song.setSingerId(Integer.parseInt(singerIdTemp));
                        song.setSingerName(singerNameTemp);
                    }else{
                        song.setSingerId(0);
                        song.setSingerName("");
                    }
                    if(nationalIdTemp!="null"){
                        song.setNationalId(Integer.parseInt(nationalIdTemp));
                        song.setNationalName(nationalNameTemp);
                    }else{
                        song.setNationalId(0);
                        song.setNationalName("");
                    }
                    if (genreIdTemp != "null") {
                        song.setGenreId(Integer.parseInt(genreIdTemp));
                        song.setGenreName(genreNameTemp);
                    }else{
                        song.setGenreId(0);
                        song.setGenreName("");
                    }
                    song.setFileSize(Integer.parseInt(fileSizeTemp));
                    song.setDownload(0);
                    song.setLocal("");
                    songList.add(song);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return songList;
    }

    public static List<AppCombo> parseJsonToAppCombos(String json) {
        List<AppCombo> appComboList = new ArrayList<>();
        if (json != null) {
            try {
                // Getting JSON Array node
                JSONArray list = new JSONArray(json);
                // looping through All AppCombo
                for (int i = 0; i < list.length(); i++) {
                    JSONObject c = list.getJSONObject(i);

                    String appComboIdTemp = c.getString("AppComboId");
                    String appComboNameTemp = c.getString("AppComboName");
                    String platformIdTemp = c.getString("PlatformId");
                    String appsCountTemp = c.getString("AppsCount");
                    String sortIndexTemp = c.getString("SortIndex");

                    // adding appcombo to appcombo list
                    AppCombo appCombo = new AppCombo();
                    appCombo.setAppComboId(Integer.parseInt(appComboIdTemp));
                    appCombo.setAppComboName(appComboNameTemp);
                    appCombo.setPlatformId(Integer.parseInt(platformIdTemp));
                    appCombo.setAppsCount(Integer.parseInt(appsCountTemp));
                    appCombo.setSortIndex(Integer.parseInt(sortIndexTemp));
                    appComboList.add(appCombo);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return appComboList;
    }

    public static List<App> parseJsonToApplications(String json) {
        List<App> appList = new ArrayList<>();
        if (json != null) {
            try {
                // Getting JSON Array node
                JSONArray list = new JSONArray(json);
                // looping through All AppCombo
                for (int i = 0; i < list.length(); i++) {
                    JSONObject c = list.getJSONObject(i);

                    String applicationIdTemp = c.getString("ApplicationId");
                    String applicationNameTemp = c.getString("ApplicationName");
                    String appComboIdTemp = c.getString("AppComboId");
                    String appComboNameTemp = c.getString("AppComboName");
                    String fileSizeTemp = c.getString("FileSize");

                    // adding appcombo to appcombo list
                    App app = new App();
                    app.setApplicationId(Integer.parseInt(applicationIdTemp));
                    app.setApplicationName(applicationNameTemp);
                    app.setAppComboId(Integer.parseInt(appComboIdTemp));
                    app.setAppComboName(appComboNameTemp);
                    app.setFileSize(Integer.parseInt(fileSizeTemp));
                    app.setDownload(0);
                    app.setLocal("");
                    appList.add(app);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return appList;
    }

    public static DBVersion parseJsonToDBVersion(String json) {
        DBVersion dbVersion = new DBVersion();
        dbVersion.setDbVersionId(0);
        dbVersion.setLastUpdate("");
        if (json != null) {
            try {
                JSONObject object = new JSONObject(json);
                int dbVersionIdTemp = object.getInt("DBVersionId");
                String lastUpdatedTemp = object.getString("LastUpdated");
                dbVersion.setDbVersionId(dbVersionIdTemp);
                dbVersion.setLastUpdate(lastUpdatedTemp);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return dbVersion;
    }
    public static AppVersion parseJsonToAppVersion(String json) {
        AppVersion appVersion = new AppVersion();
        if (json != null) {
            try {
                JSONObject object = new JSONObject(json);
                int idTemp = object.getInt("ID");
                String versionTemp = object.getString("Version");
                String googlePlayURLTemp = object.getString("GooglePlayURL");
                appVersion.setId(idTemp);
                appVersion.setVersion(versionTemp);
                appVersion.setUrl(googlePlayURLTemp);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return appVersion;
    }
}
