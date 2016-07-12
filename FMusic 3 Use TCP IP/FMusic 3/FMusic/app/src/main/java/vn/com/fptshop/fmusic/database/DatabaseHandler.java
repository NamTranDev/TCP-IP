package vn.com.fptshop.fmusic.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import vn.com.fptshop.fmusic.IProccessBar;
import vn.com.fptshop.fmusic.models.App;
import vn.com.fptshop.fmusic.models.AppCombo;
import vn.com.fptshop.fmusic.models.DBVersion;
import vn.com.fptshop.fmusic.models.Genre;
import vn.com.fptshop.fmusic.models.National;
import vn.com.fptshop.fmusic.models.Singer;
import vn.com.fptshop.fmusic.models.Song;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "dbFptshop";

    // Tables name
    private static final String TABLE_NATIONAL = "national";
    private static final String TABLE_SINGER = "singer";
    private static final String TABLE_GENRE = "genre";
    private static final String TABLE_SONG = "song";
    private static final String TABLE_APPCOMBO = "appcombo";
    private static final String TABLE_APP = "app";
    private static final String TABLE_DBVERSION = "dbversion";

    // TABLE_NATIONAL Columns names
    private static final String KEY_NATIONALID = "nationalId";
    private static final String KEY_NATIONALNAME = "nationalName";
    private static final String KEY_TOTALSONGS = "totalSongs";

    // TABLE_SINGER Columns names
    private static final String KEY_SINGERID = "singerId";
    private static final String KEY_SINGERNAME = "singerName";
//    private static final String KEY_NATIONALID = "nationalId";
//    private static final String KEY_TOTALSONGS = "totalSongs";

    // TABLE_GENRE Columns names
    private static final String KEY_GENREID = "genreId";
    private static final String KEY_GENRENAME = "genreName";
    //    private static final String KEY_TOTALSONGS = "totalSongs";
    private static final String KEY_THUMBNAIL = "thumbnail";

    // TABLE_SONG Columns names
    private static final String KEY_SONGID = "songId";
    private static final String KEY_SONGNAME = "songName";
    //    private static final String KEY_SINGERID = "singerId";
//    private static final String KEY_SINGERNAME = "singerName";
//    private static final String KEY_NATIONALID = "nationalId";
//    private static final String KEY_NATIONALNAME = "nationalName";
//    private static final String KEY_GENREID = "genreId";
//    private static final String KEY_GENRENAME = "genreName";
    private static final String KEY_FILESIZE = "fileSize";
    private static final String KEY_DOWNLOAD = "download";
    private static final String KEY_LOCAL = "local";

    // TABLE_APPCOMBO Columns names
    private static final String KEY_APPCOMBOID = "appComboId";
    private static final String KEY_APPCOMBONAME = "appComboName";
    private static final String KEY_PLATFORMID = "platformId";
    private static final String KEY_APPSCOUNT = "appsCount";
    private static final String KEY_SORTINDEX = "sortIndex";

    // TABLE_APP Columns names
    private static final String KEY_APPLICATIONID = "applicationId";
    private static final String KEY_APPLICATIONNAME = "applicationName";
//    private static final String KEY_APPCOMBOID = "appComboId";
//    private static final String KEY_APPCOMBONAME = "appComboName";
//    private static final String KEY_FILESIZE = "fileSize";
//    private static final String KEY_DOWNLOAD = "download";
//    private static final String KEY_LOCAL = "local";

    // TABLE_DBVERSION Columns names
    private static final String KEY_DBVERSIONID = "dBVersionId";
    private static final String KEY_LASTUPDATE = "lastUpdated";

    IProccessBar iProccessBar;

    public DatabaseHandler(Context context, IProccessBar iProccessBar) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.iProccessBar = iProccessBar;
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //system.out.println("CREATE DATABSE");
        String CREATE_DBVERSION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DBVERSION + "("
                + KEY_DBVERSIONID + " INTEGER PRIMARY KEY," + KEY_LASTUPDATE + " TEXT" + ")";
        String CREATE_NATIONAL_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NATIONAL + "("
                + KEY_NATIONALID + " INTEGER PRIMARY KEY," + KEY_NATIONALNAME + " TEXT," + KEY_TOTALSONGS + " INTEGER" + ")";
        String CREATE_SINGER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SINGER + "("
                + KEY_SINGERID + " INTEGER PRIMARY KEY," + KEY_SINGERNAME + " TEXT," + KEY_NATIONALID + " INTEGER," + KEY_TOTALSONGS + " INTEGER" + ")";
        String CREATE_GENRE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GENRE + "("
                + KEY_GENREID + " INTEGER PRIMARY KEY," + KEY_GENRENAME + " TEXT," + KEY_TOTALSONGS + " INTEGER," + KEY_SORTINDEX + " INTEGER," + KEY_THUMBNAIL + " TEXT" + ")";
        String CREATE_SONG_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SONG + "("
                + KEY_SONGID + " INTEGER PRIMARY KEY," + KEY_SONGNAME + " TEXT," + KEY_SINGERID + " INTEGER," + KEY_SINGERNAME + " TEXT," + KEY_NATIONALID + " INTEGER," + KEY_NATIONALNAME + " TEXT," + KEY_GENREID + " INTEGER," + KEY_GENRENAME + " TEXT," + KEY_FILESIZE + " INTEGER," + KEY_DOWNLOAD + " INTEGER," + KEY_LOCAL + " TEXT" + ")";
        String CREATE_APPCOMBO_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_APPCOMBO + "("
                + KEY_APPCOMBOID + " INTEGER PRIMARY KEY," + KEY_APPCOMBONAME + " TEXT," + KEY_PLATFORMID + " INTEGER," + KEY_APPSCOUNT + " INTEGER," + KEY_SORTINDEX + " INTEGER" + ")";
        String CREATE_APP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_APP + "("
                + KEY_APPLICATIONID + " INTEGER PRIMARY KEY," + KEY_APPLICATIONNAME + " TEXT," + KEY_APPCOMBOID + " INTEGER," + KEY_APPCOMBONAME + " TEXT," + KEY_FILESIZE + " INTEGER," + KEY_DOWNLOAD + " INTEGER," + KEY_LOCAL + " TEXT" + ")";
        db.execSQL(CREATE_DBVERSION_TABLE);
        db.execSQL(CREATE_NATIONAL_TABLE);
        db.execSQL(CREATE_SINGER_TABLE);
        db.execSQL(CREATE_GENRE_TABLE);
        db.execSQL(CREATE_SONG_TABLE);
        db.execSQL(CREATE_APPCOMBO_TABLE);
        db.execSQL(CREATE_APP_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //system.out.println("UPDATE DATABSE");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DBVERSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NATIONAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SINGER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENRE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPCOMBO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP);
        // Create tables again
        onCreate(db);
    }

    public void dropDatabase() {
        //system.out.println("DROP DATABSE");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DBVERSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NATIONAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SINGER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENRE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPCOMBO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP);
        onCreate(db);
    }


    /**
     * ================================================
     * All CRUD(Create, Read, Update, Delete) National
     * ================================================
     */

    // Adding new national
    public void addNationals(List<National> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = 1;
        int size = list.size();
        String name = "quốc gia";
        if (iProccessBar != null)
            iProccessBar.update(name, count++, size);
        for (National national : list) {
            //system.out.println("[National] insert to db >> " + national.getNationalName());
            ContentValues values = new ContentValues();
            values.put(KEY_NATIONALID, national.getNationalId());
            values.put(KEY_NATIONALNAME, national.getNationalName());
            values.put(KEY_TOTALSONGS, national.getTotalSongs());
            // Inserting Row
            db.insert(TABLE_NATIONAL, null, values);
//            if (iProccessBar != null)
//                iProccessBar.update(name, count++, size);
        }
//        db.close();
    }

    // Getting single national
    National getNational(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NATIONAL, new String[]{KEY_NATIONALID,
                        KEY_NATIONALNAME, KEY_TOTALSONGS}, KEY_NATIONALID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        National national = new National(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)));
        return national;
    }

    // Getting All National
    public List<National> getAllNationals() {
        List<National> nationalList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NATIONAL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                National national = new National();
                national.setNationalId(Integer.parseInt(cursor.getString(0)));
                national.setNationalName(cursor.getString(1));
                national.setTotalSongs(Integer.parseInt(cursor.getString(2)));
                nationalList.add(national);
            } while (cursor.moveToNext());
        }
        return nationalList;
    }

    // Updating single national
    public int updateNational(National national) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NATIONALID, national.getNationalId());
        values.put(KEY_NATIONALNAME, national.getNationalName());
        values.put(KEY_TOTALSONGS, national.getTotalSongs());

        return db.update(TABLE_NATIONAL, values, KEY_NATIONALID + " = ?",
                new String[]{String.valueOf(national.getNationalId())});
    }

    // Deleting single national
    public void deleteNational(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NATIONAL, KEY_NATIONALID + " = ?",
                new String[]{String.valueOf(id)});
//        db.close();
    }

    // Getting National Count
    public int getNationalCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NATIONAL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
        // return count
        return cursor.getCount();
    }

    /**
     * ================================================
     * All CRUD(Create, Read, Update, Delete) Singer
     * ================================================
     */
// Adding new Singer
    public void addSingers(List<Singer> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = 1;
        int size = list.size();
        String name = "ca sĩ";
        if (iProccessBar != null)
            iProccessBar.update(name, count++, size);
        String sql = "INSERT INTO " + TABLE_SINGER + " (" + KEY_SINGERID + ", " + KEY_SINGERNAME + "," + KEY_NATIONALID + "," + KEY_TOTALSONGS + ") VALUES (?, ?,?,?)";
        db.beginTransaction();

        SQLiteStatement stmt = db.compileStatement(sql);
        for (Singer singer : list) {
            stmt.bindLong(1, singer.getSingerId());
            stmt.bindString(2, singer.getSingerName());
            stmt.bindLong(3, singer.getNationalId());
            stmt.bindLong(4, singer.getTotalSongs());
            stmt.execute();
            stmt.clearBindings();
//            if (iProccessBar != null)
//                iProccessBar.update(name, count++, size);
        }
        db.setTransactionSuccessful();
        db.endTransaction();

//        for (Singer singer : list) {
//            //system.out.println("[Singer] insert to db >> " + singer.getSingerName());
//            ContentValues values = new ContentValues();
//            values.put(KEY_SINGERID, singer.getSingerId());
//            values.put(KEY_SINGERNAME, singer.getSingerName());
//            values.put(KEY_NATIONALID, singer.getNationalId());
//            values.put(KEY_TOTALSONGS, singer.getTotalSongs());
//            // Inserting Row
//            db.insert(TABLE_SINGER, null, values);
//            if (iProccessBar != null)
//                iProccessBar.update(name, count++, size);
//        }
//        db.close();
    }

    // Getting single Singer
    public Singer getSinger(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SINGER, new String[]{KEY_SINGERID,
                        KEY_SINGERNAME, KEY_NATIONALID, KEY_TOTALSONGS}, KEY_SINGERID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Singer singer = new Singer(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));
        return singer;
    }

    // Getting All Singer
    public List<Singer> getSingersLimit(int id, int offset, int limit, String key) {
        List<Singer> singerList = new ArrayList<>();
        // Select All Query
        String selectQuery = "";
        if (id == 0) {
            if ("".equalsIgnoreCase(key)) {
                selectQuery = "SELECT  * FROM " + TABLE_SINGER + " LIMIT " + limit + " OFFSET " + offset;
            } else {
                selectQuery = "SELECT  * FROM " + TABLE_SINGER + " WHERE " + KEY_SINGERNAME + " LIKE '" + key + "%' LIMIT " + limit + " OFFSET " + offset;
            }
        } else {

            if ("".equalsIgnoreCase(key)) {
                selectQuery = "SELECT  * FROM " + TABLE_SINGER + " WHERE " + KEY_NATIONALID + " = " + id + " LIMIT " + limit + " OFFSET " + offset;
            } else {
                selectQuery = "SELECT  * FROM " + TABLE_SINGER + " WHERE " + KEY_NATIONALID + " = " + id + " AND " + KEY_SINGERNAME + " LIKE '" + key + "%' LIMIT " + limit + " OFFSET " + offset;
            }

        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Singer singer = new Singer();
                singer.setSingerId(Integer.parseInt(cursor.getString(0)));
                singer.setSingerName(cursor.getString(1));
                singer.setNationalId(Integer.parseInt(cursor.getString(2)));
                singer.setTotalSongs(Integer.parseInt(cursor.getString(3)));
                singerList.add(singer);
            } while (cursor.moveToNext());
        }
        return singerList;
    }

    // Getting All Singer
    public List<Singer> searchSinger(String key) {
        List<Singer> singerList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SINGER + " WHERE " + KEY_SINGERNAME + " LIKE '%" + key + "%'";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Singer singer = new Singer();
                singer.setSingerId(Integer.parseInt(cursor.getString(0)));
                singer.setSingerName(cursor.getString(1));
                singer.setNationalId(Integer.parseInt(cursor.getString(2)));
                singer.setTotalSongs(Integer.parseInt(cursor.getString(3)));
                singerList.add(singer);
            } while (cursor.moveToNext());
        }
        return singerList;
    }

    // Updating single singer
    public int updateSinger(Singer singer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SINGERID, singer.getNationalId());
        values.put(KEY_SINGERNAME, singer.getSingerName());
        values.put(KEY_NATIONALID, singer.getNationalId());
        values.put(KEY_TOTALSONGS, singer.getTotalSongs());

        return db.update(TABLE_SINGER, values, KEY_SINGERID + " = ?",
                new String[]{String.valueOf(singer.getSingerId())});
    }

    // Deleting single singer
    public void deleteSinger(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SINGER, KEY_SINGERID + " = ?",
                new String[]{String.valueOf(id)});
//        db.close();
    }

    // Getting National Count
    public int getSingerCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SINGER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
        // return count
        return cursor.getCount();
    }

    /**
     * ================================================
     * All CRUD(Create, Read, Update, Delete) Genre
     * ================================================
     */
    public int countTest = 0;

    // Adding new Genre
    public void addGenres(List<Genre> list) {
//        String[] listImageTest = new String[7];
//        listImageTest[0] = "http://image.mp3.zdn.vn/thumb/240_240/covers/e/0/e00203613c27b249a5c6229f4143c1cc_1449739225.jpg";
//        listImageTest[1] = "http://image.mp3.zdn.vn/thumb/240_240/covers/e/0/e00203613c27b249a5c6229f4143c1cc_1449739225.jpg";
//        listImageTest[2] = "http://image.mp3.zdn.vn/thumb/240_240/covers/e/0/e00203613c27b249a5c6229f4143c1cc_1449739225.jpg";
//        listImageTest[3] = "http://image.mp3.zdn.vn/thumb/240_240/covers/e/0/e00203613c27b249a5c6229f4143c1cc_1449739225.jpg";
//        listImageTest[4] = "http://image.mp3.zdn.vn/thumb/240_240/covers/e/0/e00203613c27b249a5c6229f4143c1cc_1449739225.jpg";
//        listImageTest[5] = "http://lh6.ggpht.com/_Nsxc889y6hY/TBp7jfx-cgI/AAAAAAAAHAg/Rr7jX44r2Gc/s144-c/IMGP9775a.jpg";
//        listImageTest[6] = "http://lh6.ggpht.com/_ZN5zQnkI67I/TCFFZaJHDnI/AAAAAAAABVk/YoUbDQHJRdo/s144-c/P9250508.JPG";

        SQLiteDatabase db = this.getWritableDatabase();
//        int count = 1;
//        int size = list.size();
//        String name = "thể loại";
        for (Genre genre : list) {
//            if (countTest >= listImageTest.length)
//                countTest = 0;
//            genre.setThumbnail(listImageTest[countTest++]);
            //system.out.println("[Genre] insert to db >> " + genre.getGenreName());
            ContentValues values = new ContentValues();
            values.put(KEY_GENREID, genre.getGenreId());
            values.put(KEY_GENRENAME, genre.getGenreName());
            values.put(KEY_TOTALSONGS, genre.getTotalSongs());
            values.put(KEY_SORTINDEX, genre.getSortIndex());
            values.put(KEY_THUMBNAIL, genre.getThumbnail());
            // Inserting Row
            db.insert(TABLE_GENRE, null, values);
//            if (iProccessBar != null)
//                iProccessBar.update(name, count++, size);
        }
//        db.close();
    }

    // Getting single Genre
    Genre getGenre(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GENRE, new String[]{KEY_GENREID,
                        KEY_GENRENAME, KEY_TOTALSONGS, KEY_SORTINDEX}, KEY_GENREID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Genre genre = new Genre(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), cursor.getString(4));
        return genre;
    }

    // Getting All Genre
    public List<Genre> getGenresLimit(int offset, int limit) {
        List<Genre> genreList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GENRE + " ORDER BY " + KEY_SORTINDEX + " ASC LIMIT " + limit + " OFFSET " + offset;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Genre genre = new Genre();
                genre.setGenreId(Integer.parseInt(cursor.getString(0)));
                genre.setGenreName(cursor.getString(1));
                genre.setTotalSongs(Integer.parseInt(cursor.getString(2)));
                genre.setSortIndex(Integer.parseInt(cursor.getString(3)));
                genre.setThumbnail(cursor.getString(4));
                genreList.add(genre);
            } while (cursor.moveToNext());
        }
        return genreList;
    }

    // Getting All Genre
    public List<Genre> searchGenre(String key) {
        List<Genre> genreList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GENRE + " WHERE " + KEY_GENRENAME + " LIKE '%" + key + "%'  ORDER BY " + KEY_SORTINDEX;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Genre genre = new Genre();
                genre.setGenreId(Integer.parseInt(cursor.getString(0)));
                genre.setGenreName(cursor.getString(1));
                genre.setTotalSongs(Integer.parseInt(cursor.getString(2)));
                genre.setSortIndex(Integer.parseInt(cursor.getString(3)));
                genre.setThumbnail(cursor.getString(4));
                genreList.add(genre);
            } while (cursor.moveToNext());
        }
        return genreList;
    }

    // Updating single genre
    public int updateGenre(Genre genre) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GENREID, genre.getGenreId());
        values.put(KEY_GENRENAME, genre.getGenreName());
        values.put(KEY_TOTALSONGS, genre.getTotalSongs());
        values.put(KEY_THUMBNAIL, genre.getThumbnail());

        return db.update(TABLE_GENRE, values, KEY_GENREID + " = ?",
                new String[]{String.valueOf(genre.getGenreId())});
    }

    // Deleting single genre
    public void deleteGenre(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GENRE, KEY_GENREID + " = ?",
                new String[]{String.valueOf(id)});
//        db.close();
    }

    // Getting Genre Count
    public int getGenreCount() {
        String countQuery = "SELECT  * FROM " + TABLE_GENRE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
        // return count
        return cursor.getCount();
    }

    /**
     * ================================================
     * All CRUD(Create, Read, Update, Delete) Song
     * ================================================
     */
// Adding new Genre
    public void addSongs(List<Song> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String sql = "INSERT INTO " + TABLE_SONG + " (" + KEY_SONGID + "," + KEY_SONGNAME + "," + KEY_SINGERID + "," + KEY_SINGERNAME + "," + KEY_NATIONALID + "," + KEY_NATIONALNAME + "," + KEY_GENREID + "," + KEY_GENRENAME + "," + KEY_FILESIZE + "," + KEY_DOWNLOAD + "," + KEY_LOCAL + ") VALUES (?, ?,?,?,?,?,?,?,?,?,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        int count = 1;
        int size = list.size();
        String name = "bài hát";
        if (iProccessBar != null)
            iProccessBar.update(name, count++, size);
        for (Song song : list) {
            stmt.bindLong(1, song.getSongId());
            stmt.bindString(2, song.getSongName());
            stmt.bindLong(3, song.getSingerId());
            stmt.bindString(4, song.getSingerName());
            stmt.bindLong(5, song.getNationalId());
            stmt.bindString(6, song.getNationalName());
            stmt.bindLong(7, song.getGenreId());
            stmt.bindString(8, song.getGenreName());
            stmt.bindLong(9, song.getFileSize());
            stmt.bindLong(10, song.getDownload());
            stmt.bindString(11, song.getLocal());
            stmt.execute();
            stmt.clearBindings();
//            if (iProccessBar != null)
//                iProccessBar.update(name, count++, size);
        }
        db.setTransactionSuccessful();
        db.endTransaction();


//        for (Song song : list) {
//            //system.out.println("[Song] insert to db >> " + song.getSongName());
//            ContentValues values = new ContentValues();
//            values.put(KEY_SONGID, song.getSongId());
//            values.put(KEY_SONGNAME, song.getSongName());
//            values.put(KEY_SINGERID, song.getSingerId());
//            values.put(KEY_SINGERNAME, song.getSingerName());
//            values.put(KEY_NATIONALID, song.getNationalId());
//            values.put(KEY_NATIONALNAME, song.getNationalName());
//            values.put(KEY_GENREID, song.getGenreId());
//            values.put(KEY_GENRENAME, song.getGenreName());
//            values.put(KEY_FILESIZE, song.getFileSize());
//            values.put(KEY_DOWNLOAD, song.getDownload());
//            values.put(KEY_LOCAL, song.getLocal());
//            // Inserting Row
//            db.insert(TABLE_SONG, null, values);
//            if (iProccessBar != null)
//                iProccessBar.update(name, count++, size);
//        }
//        db.close();
    }

    // Getting single Song
    Song getSong(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SONG, new String[]{KEY_SONGID,
                        KEY_SINGERNAME, KEY_SINGERID, KEY_SINGERNAME, KEY_NATIONALID, KEY_NATIONALNAME, KEY_GENREID, KEY_GENRENAME, KEY_FILESIZE, KEY_DOWNLOAD, KEY_LOCAL}, KEY_GENREID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Song song = new Song(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                cursor.getString(3), Integer.parseInt(
                cursor.getString(4)),
                cursor.getString(5), Integer.parseInt(
                cursor.getString(6)),
                cursor.getString(7), Integer.parseInt(
                cursor.getString(8)), Integer.parseInt(
                cursor.getString(9)),
                cursor.getString(10));
        return song;
    }

    // Getting All Song
    public List<Song> getAllSongs() {
        List<Song> songList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONG;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(
                        cursor.getString(4)),
                        cursor.getString(5), Integer.parseInt(
                        cursor.getString(6)),
                        cursor.getString(7), Integer.parseInt(
                        cursor.getString(8)), Integer.parseInt(
                        cursor.getString(9)),
                        cursor.getString(10));

                songList.add(song);
            } while (cursor.moveToNext());
        }
        return songList;
    }

    // Getting All Song
    public List<Song> searchSong(String key, int download) {
        List<Song> songList = new ArrayList<>();
        int limit = 20;
        // Select All Query
        String selectQuery = "";
        if (download == 0) {
            selectQuery = "SELECT  * FROM " + TABLE_SONG + " WHERE " + KEY_SONGNAME + " LIKE '%" + key + "%' LIMIT " + limit;
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_SONG + " WHERE " + KEY_DOWNLOAD + " =6 AND " + KEY_SONGNAME + " LIKE '%" + key + "%' LIMIT " + limit;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(
                        cursor.getString(4)),
                        cursor.getString(5), Integer.parseInt(
                        cursor.getString(6)),
                        cursor.getString(7), Integer.parseInt(
                        cursor.getString(8)), Integer.parseInt(
                        cursor.getString(9)),
                        cursor.getString(10));

                songList.add(song);
            } while (cursor.moveToNext());
        }
        return songList;
    }

    public List<Song> searchSongsOf(String key, int id, int isSinger) {
        List<Song> songList = new ArrayList<>();
        // Select All Query
        String selectQuery = "";
        if (isSinger == 0) {
            selectQuery = "SELECT  * FROM " + TABLE_SONG + " WHERE " + KEY_GENREID + " = " + id + " AND " + KEY_SONGNAME + " LIKE '%" + key + "%'";
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_SONG + " WHERE " + KEY_SINGERID + " = " + id + " AND " + KEY_SONGNAME + " LIKE '%" + key + "%'";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(
                        cursor.getString(4)),
                        cursor.getString(5), Integer.parseInt(
                        cursor.getString(6)),
                        cursor.getString(7), Integer.parseInt(
                        cursor.getString(8)), Integer.parseInt(
                        cursor.getString(9)),
                        cursor.getString(10));

                songList.add(song);
            } while (cursor.moveToNext());
        }
        return songList;
    }

    // Getting All Song
    public List<Song> getSongsLimit(int offset, int limit) {
        List<Song> songList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONG + " LIMIT " + limit + " OFFSET " + offset;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(
                        cursor.getString(4)),
                        cursor.getString(5), Integer.parseInt(
                        cursor.getString(6)),
                        cursor.getString(7), Integer.parseInt(
                        cursor.getString(8)), Integer.parseInt(
                        cursor.getString(9)),
                        cursor.getString(10));

                songList.add(song);
            } while (cursor.moveToNext());
        }
        return songList;
    }

    // Getting All Song
    public List<Song> getSongsLimitOfSinger(int id, int limit, int offset) {
        List<Song> songList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONG + " WHERE " + KEY_SINGERID + " = " + id + " LIMIT " + limit + " OFFSET " + offset;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(
                        cursor.getString(4)),
                        cursor.getString(5), Integer.parseInt(
                        cursor.getString(6)),
                        cursor.getString(7), Integer.parseInt(
                        cursor.getString(8)), Integer.parseInt(
                        cursor.getString(9)),
                        cursor.getString(10));

                songList.add(song);
            } while (cursor.moveToNext());
        }
        return songList;
    }

    public List<Song> getSongsLimitOfSingerDownload(int id) {
        List<Song> songList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONG + " WHERE " + KEY_SINGERID + " = " + id + " AND " + KEY_DOWNLOAD + " !=6";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(
                        cursor.getString(4)),
                        cursor.getString(5), Integer.parseInt(
                        cursor.getString(6)),
                        cursor.getString(7), Integer.parseInt(
                        cursor.getString(8)), Integer.parseInt(
                        cursor.getString(9)),
                        cursor.getString(10));

                songList.add(song);
            } while (cursor.moveToNext());
        }
        return songList;
    }
    public int getCountAppDownload(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  count(*) FROM " + TABLE_APP + " WHERE " + KEY_APPCOMBOID + " = " + id + " AND " + KEY_DOWNLOAD + " =6";
        Cursor mcursor = db.rawQuery(selectQuery, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        System.out.println("NUMBER IN DB: " + icount);
        return icount;
    }

    public int getCountSongSingerDownload(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  count(*) FROM " + TABLE_SONG + " WHERE " + KEY_SINGERID + " = " + id + " AND " + KEY_DOWNLOAD + " =6";
        Cursor mcursor = db.rawQuery(selectQuery, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        System.out.println("NUMBER IN DB: " + icount);
        return icount;
    }

    public int getCountSongGenreDownload(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  count(*) FROM " + TABLE_SONG + " WHERE " + KEY_GENREID + " = " + id + " AND " + KEY_DOWNLOAD + " =6";
        Cursor mcursor = db.rawQuery(selectQuery, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        System.out.println("NUMBER IN DB: " + icount);
        return icount;
    }

    public List<Song> getSongsLimitOfGenreDownload(int id) {
        List<Song> songList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONG + " WHERE " + KEY_GENREID + " = " + id + " AND " + KEY_DOWNLOAD + " !=6";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Song song = new Song(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                            cursor.getString(3), Integer.parseInt(
                            cursor.getString(4)),
                            cursor.getString(5), Integer.parseInt(
                            cursor.getString(6)),
                            cursor.getString(7), Integer.parseInt(
                            cursor.getString(8)), Integer.parseInt(
                            cursor.getString(9)),
                            cursor.getString(10));

                    songList.add(song);
                } while (cursor.moveToNext());
            }
        return songList;
    }

    public List<Song> getSongsLimitOfGenre(int id, int limit, int offset) {
        List<Song> songList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONG + " WHERE " + KEY_GENREID + " = " + id + " LIMIT " + limit + " OFFSET " + offset;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(
                        cursor.getString(4)),
                        cursor.getString(5), Integer.parseInt(
                        cursor.getString(6)),
                        cursor.getString(7), Integer.parseInt(
                        cursor.getString(8)), Integer.parseInt(
                        cursor.getString(9)),
                        cursor.getString(10));

                songList.add(song);
            } while (cursor.moveToNext());
        }
        return songList;
    }

    // Getting All Song
    public List<Song> getSongsRandomLimit(int limit) {
        List<Song> songList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONG + " Order BY RANDOM() LIMIT " + limit;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(
                        cursor.getString(4)),
                        cursor.getString(5), Integer.parseInt(
                        cursor.getString(6)),
                        cursor.getString(7), Integer.parseInt(
                        cursor.getString(8)), Integer.parseInt(
                        cursor.getString(9)),
                        cursor.getString(10));

                songList.add(song);
            } while (cursor.moveToNext());
        }
        return songList;
    }

    // Getting All Song
    public List<Song> getSongsDownloadLimit(int limit, int offset) {
        List<Song> songList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONG + " WHERE " + KEY_DOWNLOAD + " =6 ORDER BY " + KEY_SONGNAME + " LIMIT " + limit + " OFFSET " + offset;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3), Integer.parseInt(
                        cursor.getString(4)),
                        cursor.getString(5), Integer.parseInt(
                        cursor.getString(6)),
                        cursor.getString(7), Integer.parseInt(
                        cursor.getString(8)), Integer.parseInt(
                        cursor.getString(9)),
                        cursor.getString(10));

                songList.add(song);
            } while (cursor.moveToNext());
        }
        return songList;
    }

    // Updating single song
    public int updateSong(Song song, int status, String local) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SONGID, song.getSongId());
        values.put(KEY_SONGNAME, song.getSongName());
        values.put(KEY_SINGERID, song.getSingerId());
        values.put(KEY_SINGERNAME, song.getSingerName());
        values.put(KEY_NATIONALID, song.getNationalId());
        values.put(KEY_NATIONALNAME, song.getNationalName());
        values.put(KEY_GENREID, song.getGenreId());
        values.put(KEY_GENRENAME, song.getGenreName());
        values.put(KEY_FILESIZE, song.getFileSize());
        values.put(KEY_DOWNLOAD, status);
        values.put(KEY_LOCAL, local);

        return db.update(TABLE_SONG, values, KEY_SONGID + " = ?",
                new String[]{String.valueOf(song.getSongId())});
    }

    // Deleting single song
    public void deleteSong(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONG, KEY_SONGID + " = ?",
                new String[]{String.valueOf(id)});
//        db.close();
    }

    // Getting Song Count
    public int getSongCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SONG;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
        // return count
        return cursor.getCount();
    }

    /**
     * ================================================
     * All CRUD(Create, Read, Update, Delete) AppCombo
     * ================================================
     */

    // Adding new AppCombo
    public void addAppCombos(List<AppCombo> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = 1;
        int size = list.size();
        String name = "app combo";
        if (iProccessBar != null)
            iProccessBar.update(name, count++, size);
        for (AppCombo appCombo : list) {
            //system.out.println("[AppCombo] insert to db >> " + appCombo.getAppComboName());
            ContentValues values = new ContentValues();
            values.put(KEY_APPCOMBOID, appCombo.getAppComboId());
            values.put(KEY_APPCOMBONAME, appCombo.getAppComboName());
            values.put(KEY_PLATFORMID, appCombo.getPlatformId());
            values.put(KEY_APPSCOUNT, appCombo.getAppsCount());
            values.put(KEY_SORTINDEX, appCombo.getSortIndex());
            // Inserting Row
            db.insert(TABLE_APPCOMBO, null, values);
//            if (iProccessBar != null)
//                iProccessBar.update(name, count++, size);
        }
//        db.close();
    }

    // Getting single AppCombo
    public AppCombo getAppCombo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_APPCOMBO, new String[]{KEY_APPCOMBOID,
                        KEY_APPCOMBONAME, KEY_PLATFORMID, KEY_APPSCOUNT, KEY_SORTINDEX}, KEY_APPCOMBOID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        AppCombo appCombo = new AppCombo(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
        return appCombo;
    }

    // Getting All AppCombo
    public List<AppCombo> getAllAppCombos(int offset, int limit) {
        List<AppCombo> appComboList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_APPCOMBO + " ORDER BY " + KEY_SORTINDEX + " ASC LIMIT " + limit + " OFFSET " + offset;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AppCombo appCombo = new AppCombo(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
                appComboList.add(appCombo);
            } while (cursor.moveToNext());
        }
        return appComboList;
    }


    // Updating single AppCombo
    public int updateAppCombo(AppCombo appCombo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_APPCOMBOID, appCombo.getAppComboId());
        values.put(KEY_APPCOMBONAME, appCombo.getAppComboName());
        values.put(KEY_PLATFORMID, appCombo.getPlatformId());
        values.put(KEY_APPSCOUNT, appCombo.getAppsCount());

        return db.update(TABLE_APPCOMBO, values, KEY_APPCOMBOID + " = ?",
                new String[]{String.valueOf(appCombo.getAppComboId())});
    }

    // Deleting single AppCombo
    public void deleteAppCombo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_APPCOMBO, KEY_APPCOMBOID + " = ?",
                new String[]{String.valueOf(id)});
//        db.close();
    }

    // Getting AppCombo Count
    public int getAppComboCount() {
        String countQuery = "SELECT  * FROM " + TABLE_APPCOMBO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
        // return count
        return cursor.getCount();
    }

    /**
     * ================================================
     * All CRUD(Create, Read, Update, Delete) AppCombo
     * ================================================
     */

    // Adding new App
    public void addApps(List<App> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = 1;
        int size = list.size();
        String name = "app & game";
        if (iProccessBar != null)
            iProccessBar.update(name, count++, size);
        String sql = "INSERT INTO " + TABLE_APP + " (" + KEY_APPLICATIONID + ", " + KEY_APPLICATIONNAME + "," + KEY_APPCOMBOID + "," + KEY_APPCOMBONAME + "," + KEY_FILESIZE + "," + KEY_DOWNLOAD + "," + KEY_LOCAL + ") VALUES (?, ?,?,?,?,?,?)";
        db.beginTransaction();

        SQLiteStatement stmt = db.compileStatement(sql);
        for (App app : list) {
            stmt.bindLong(1, app.getApplicationId());
            stmt.bindString(2, app.getApplicationName());
            stmt.bindLong(3, app.getAppComboId());
            stmt.bindString(4, app.getAppComboName());
            stmt.bindLong(5, app.getFileSize());
            stmt.bindLong(6, app.getDownload());
            stmt.bindString(7, app.getLocal());
            stmt.execute();
            stmt.clearBindings();
//            if (iProccessBar != null)
//                iProccessBar.update(name, count++, size);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

//        for (App app : list) {
//            //system.out.println("[App] insert to db >> " + app.getApplicationName());
//            ContentValues values = new ContentValues();
//            values.put(KEY_APPLICATIONID, app.getApplicationId());
//            values.put(KEY_APPLICATIONNAME, app.getApplicationName());
//            values.put(KEY_APPCOMBOID, app.getAppComboId());
//            values.put(KEY_APPCOMBONAME, app.getAppComboName());
//            values.put(KEY_FILESIZE, app.getFileSize());
//            values.put(KEY_DOWNLOAD, app.getDownload());
//            values.put(KEY_LOCAL, app.getLocal());
//            // Inserting Row
//            db.insert(TABLE_APP, null, values);
//            if (iProccessBar != null)
//                iProccessBar.update(name, count++, size);
//        }
//        db.close();
    }

    // Getting single App
    App getApp(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_APP, new String[]{KEY_APPLICATIONID,
                        KEY_APPLICATIONNAME, KEY_APPCOMBOID, KEY_APPCOMBONAME, KEY_FILESIZE, KEY_DOWNLOAD, KEY_LOCAL}, KEY_APPLICATIONID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        App app = new App(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), cursor.getString(6));
        return app;
    }

    // Getting All App
    public List<App> getAppsLimit(int id, int offset, int limit) {
        List<App> appList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_APP + " WHERE " + KEY_APPCOMBOID + " = " + id + " LIMIT " + limit + " OFFSET " + offset;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                App app = new App(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), cursor.getString(6));
                appList.add(app);
            } while (cursor.moveToNext());
        }
        return appList;
    }

    // Getting All App
    public List<App> getAppsDownloadLimit(int limit, int offset) {
        List<App> appList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_APP + " WHERE " + KEY_DOWNLOAD + " =6  LIMIT " + limit + " OFFSET " + offset;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                App app = new App(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), cursor.getString(6));
                appList.add(app);
            } while (cursor.moveToNext());
        }
        return appList;
    }

    // Getting All App
    public List<App> searchApp(String key, int download) {
        List<App> appList = new ArrayList<>();
        // Select All Query
        String selectQuery = "";
        if (download == 0) {
            selectQuery = "SELECT  * FROM " + TABLE_APP + " WHERE " + KEY_APPLICATIONNAME + " LIKE '%" + key + "%' GROUP BY " + KEY_APPLICATIONNAME;
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_APP + " WHERE " + KEY_DOWNLOAD + " =6  AND " + KEY_APPLICATIONNAME + " LIKE '%" + key + "%'";
        }


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                App app = new App(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), cursor.getString(6));
                appList.add(app);
            } while (cursor.moveToNext());
        }
        return appList;
    }

    // Getting All App
    public List<App> getAppsDownload(int id) {
        List<App> appList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_APP + " WHERE " + KEY_APPCOMBOID + " = " + id + " AND " + KEY_DOWNLOAD + " !=6";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                App app = new App(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), cursor.getString(6));
                appList.add(app);
            } while (cursor.moveToNext());
        }
        return appList;
    }

    // Updating single App
    public int updateApp(App app, int status, String local) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_APPLICATIONID, app.getApplicationId());
        values.put(KEY_APPLICATIONNAME, app.getApplicationName());
        values.put(KEY_APPCOMBOID, app.getAppComboId());
        values.put(KEY_APPCOMBONAME, app.getAppComboName());
        values.put(KEY_FILESIZE, app.getFileSize());
        values.put(KEY_DOWNLOAD, status);
        values.put(KEY_LOCAL, local);

        return db.update(TABLE_APP, values, KEY_APPLICATIONID + " = ?",
                new String[]{String.valueOf(app.getApplicationId())});
    }

    // Deleting single App
    public void deleteApp(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_APP, KEY_APPLICATIONID + " = ?",
                new String[]{String.valueOf(id)});
//        db.close();
    }

    // Getting App Count
    public int getAppCount() {
        String countQuery = "SELECT  * FROM " + TABLE_APP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
        // return count
        return cursor.getCount();
    }

    /**
     * ================================================
     * All CRUD(Create, Read, Update, Delete) DBVersion
     * ================================================
     */
// Adding new DBVersion
    public void addDBVersion(DBVersion dbVersion) {
        SQLiteDatabase db = this.getWritableDatabase();
        //system.out.println("[DBVersion] insert to db >> " + dbVersion.getDbVersionId() + " || " + dbVersion.getLastUpdate());
        ContentValues values = new ContentValues();
        values.put(KEY_DBVERSIONID, dbVersion.getDbVersionId());
        values.put(KEY_LASTUPDATE, dbVersion.getLastUpdate());
        // Inserting Row
        db.insert(TABLE_DBVERSION, null, values);

//        db.close();
    }

    // Getting single DBVersion
    DBVersion getDBVersion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DBVERSION, new String[]{KEY_DBVERSIONID,
                        KEY_LASTUPDATE}, KEY_DBVERSIONID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        DBVersion dbVersion = new DBVersion(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        return dbVersion;
    }

    // Getting All DBVersion
    public DBVersion getAllDBVersion() {
        DBVersion dbVersion = new DBVersion();
        dbVersion.setDbVersionId(0);
        dbVersion.setLastUpdate("");

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DBVERSION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                dbVersion.setDbVersionId(Integer.parseInt(cursor.getString(0)));
                dbVersion.setLastUpdate(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return dbVersion;
    }

    // Updating single DBVersion
    public int updateDBVersion(DBVersion dbVersion) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DBVERSIONID, dbVersion.getDbVersionId());
        values.put(KEY_LASTUPDATE, dbVersion.getLastUpdate());

        return db.update(TABLE_DBVERSION, values, KEY_DBVERSIONID + " = ?",
                new String[]{String.valueOf(dbVersion.getDbVersionId())});
    }

    // Deleting single DBVersion
    public void deleteDBVersion(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DBVERSION, KEY_DBVERSIONID + " = ?",
                new String[]{String.valueOf(id)});
//        db.close();
    }

    // Getting DBVersion Count
    public int getDBVersionCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DBVERSION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
        // return count
        return cursor.getCount();
    }
}