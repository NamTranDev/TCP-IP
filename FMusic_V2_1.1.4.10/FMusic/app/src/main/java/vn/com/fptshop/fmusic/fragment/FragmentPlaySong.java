package vn.com.fptshop.fmusic.fragment;
/*
* SangTH
* 05/12/2015
* */

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.fptshop.fmusic.AppSetting;
import vn.com.fptshop.fmusic.PlayMusicService;
import vn.com.fptshop.fmusic.R;
import vn.com.fptshop.fmusic.RoundImage;
import vn.com.fptshop.fmusic.models.Song;

//import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by Administrator on 3/12/2015.
 */
public class FragmentPlaySong extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    static Context context;
    static FragmentActivity fragmentActivity;
    static Song song = null;
    static ArrayList<Song> songList;
    public static int currentSongIndex = -1;
    public static String currentLocal = "";
    //    private SlidingUpPanelLayout mLayout;
    String TAG = "FragmentPlaySong";

    RoundImage roundedImage;

    public static ImageView btnPlay, btnForward, btnBackward, btnNext,
            btnPrevious, listSongBtn, musicLogo;
    public static ImageButton btnShuffle, btnRepeat;
    public static SeekBar songProgressBar;


    // Songs list
    //public static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private ListAdapter adapter;
    private ListView listSongLv;
    private LinearLayout playerScreenLinearLayout, songTitleLinearLayout,
            repeatLinearLayout, progressSeekBarLinearLayout,
            progressImageLinearLayout, btnOptionLinearLayout;
    private Button backBtn;
    public static TextView songTitle, songCurrentDurationLabel,
            songTotalDurationLabel, singerName;
    Animation aniImageRotation;

    public static PlayMusicService playMusicService;
    String mode = "";
    String transfer = "";

    static Boolean mBound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        fragmentActivity = getActivity();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey("song")) {
                song = (Song) bundle.getSerializable("song");
                songList = (ArrayList<Song>) bundle.getSerializable("songlist");
            }
            if (bundle.containsKey("Mode")) {
                mode = bundle.getString("Mode");
            }
            if (bundle.containsKey("Transfer")) {
                transfer = bundle.getString("Transfer");
            }
        }

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mMessageReceiver), new IntentFilter("sendUpdateMessage"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup viewGroup, Bundle bundle) {
        final View rootView = inflater.inflate(R.layout.fragment_mini_player, viewGroup, false);

        playerScreenLinearLayout = (LinearLayout) rootView.findViewById(R.id.playerScreen);
        btnOptionLinearLayout = (LinearLayout) rootView.findViewById(R.id.fragment_play_btn_option_linearlayout);
        btnPlay = (ImageView) rootView.findViewById(R.id.btn_play_imageview);
        btnNext = (ImageView) rootView.findViewById(R.id.btn_next_imageview);
        btnPrevious = (ImageView) rootView.findViewById(R.id.btn_previous_imageview);
        songTitle = (TextView) rootView.findViewById(R.id.song_title_txt);
        singerName = (TextView) rootView.findViewById(R.id.singerName);
        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), PlayMusicService.class);
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            PlayMusicService.LocalBinder localBinder = (PlayMusicService.LocalBinder) service;
            playMusicService = localBinder.getService();
            mBound = true;
            playSong(song);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public static void playSong(Song playSong) {
        System.out.println("SONG : " + song.getSongId());
        System.out.println("SONG : " + song.getSongName());
        System.out.println("SONG : " + song.getLocal());

        try {

            if (mBound) {
                if (playSong != null) {
                    song = playSong;
                    updateUI();
                    String urlSong = "";
                    if (song.getLocal().equalsIgnoreCase("")) {
                        currentSongIndex = playSong.getSongId();
                        urlSong = AppSetting.URL + "/api/Music/Streaming?songId=" + currentSongIndex;
                    } else {
                        urlSong = song.getLocal();
                        currentLocal = playSong.getLocal();
                    }
                    System.out.println("URL SONG : " + urlSong);
                    playMusicService.playSong(urlSong);
                }
            } else {
                //Toast.makeText(getActivity(),"",Toast.LENGTH_LONG).show();
                System.out.println("Cannot connect service...");
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }

    }

    public static void updateUI() {
        fragmentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    songTitle.setText(song.getSongName());
                    btnPlay.setImageResource(R.drawable.ic_pause_circle_filled_white_24dp);
                    System.out.println("SingerName: " + song.getSingerName());
                    if (song.getSingerName() != null) {
                        singerName.setText(song.getSingerName());
                    } else {
                        singerName.setText("Đang cập nhật...");
                    }
                } catch (Exception ex) {
                    System.out.println("updateUI: " + ex);
                }
            }
        });

    }

    public static void updateSong(Song newSong) {
        song = newSong;
    }

    public static void updateSongList(ArrayList<Song> newSongList) {
        if (songList != null)
            songList.addAll(newSongList);
    }

    public static void addListAndClean(final ArrayList<Song> newSongList) {
        if (songList != null) {

            songList = new ArrayList<Song>();
            songList.addAll(newSongList);
           /* fragmentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    songList = new ArrayList<Song>();
                    songList.addAll(newSongList);
                }
            });*/
        }

    }

    public static void newSong(Song newSong, ArrayList<Song> newSongList) {
        if (newSong != null) {
            updateSong(newSong);
            addListAndClean(newSongList);

            if (newSong.getSongId() == 0) {
                if (!newSong.getLocal().equalsIgnoreCase(currentLocal)) {
                    currentLocal = newSong.getLocal();
                    playSong(song);
                }
            } else {
                if (newSong.getSongId() != currentSongIndex) {
                    currentSongIndex = newSong.getSongId();
                    playSong(song);
                }
            }


        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mMessageReceiver), new IntentFilter("sendUpdateMessage"));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
       /* if (mBound) {
            context.unbindService(mConnection);
            mBound = false;
        }*/
        //LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBound) {
            context.unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_play_imageview:

                if (currentSongIndex != -1 || !currentLocal.equalsIgnoreCase("")) {
                    if (playMusicService.checkMediaPlayer()) {
                        playMusicService.Pause();
                        btnPlay.setImageResource(R.drawable.ic_play_circle_filled_white_24dp);
                    } else {
                        playMusicService.Start();
                        btnPlay.setImageResource(R.drawable.ic_pause_circle_filled_white_24dp);
                    }
                }

                break;
            case R.id.btn_next_imageview:
                // check if next song is there or not
                Log.d("Player Service", "Next");
                Song songNext = null;
                int nextId = -1;
                String nextLocal = "";
                System.out.println("songlist .size(0 " + songList.size());
                System.out.println("song.getSongId(): " + song.getSongId());
                System.out.println("song.getLocal(): " + song.getLocal());

                if (!song.getLocal().equalsIgnoreCase("")) {
                    for (int i = 0; i < songList.size(); i++) {
                        if (songList.get(i).getLocal().equalsIgnoreCase(currentLocal)) {
                            if (i < (songList.size() - 1)) {
                                nextLocal = songList.get(i + 1).getLocal();
                                songNext = songList.get(i + 1);
                                System.out.println("nextLocal " + nextLocal);
                                System.out.println("songNext offline " + songNext);
                            }
                        }
                    }

                    if (!nextLocal.equalsIgnoreCase(currentLocal)) {
                        currentLocal = nextLocal;
                    }
                } else {
                    for (int i = 0; i < songList.size(); i++) {
                        if (songList.get(i).getSongId() == currentSongIndex) {
                            if (i < (songList.size() - 1)) {


                                nextId = songList.get(i + 1).getSongId();
                                songNext = songList.get(i + 1);
                                System.out.println("nextId " + nextId);
                                System.out.println("songNext " + songNext);
                            }
                        }
                    }

                    if (nextId != currentSongIndex) {
                        currentSongIndex = nextId;
                    }
                }


                if (songNext != null) {
                    playSong(songNext);
                }

                break;
            case R.id.btn_previous_imageview:
                int previousId = -1;
                Song songPrevious = null;
                String previousLocal = "";
                Log.d("Player Service", "Previous");
                if (song.getSongId() == 0) {
                    for (int i = 0; i < songList.size(); i++) {
                        if (songList.get(i).getLocal().equalsIgnoreCase(currentLocal)) {
                            if (i > 0) {
                                previousLocal = songList.get(i - 1).getLocal();
                                songPrevious = songList.get(i - 1);
                            }
                        }
                    }
                    if (!previousLocal.equalsIgnoreCase(currentLocal)) {
                        currentLocal = previousLocal;
                    }
                } else {
                    for (int i = 0; i < songList.size(); i++) {
                        if (songList.get(i).getSongId() == currentSongIndex) {
                            if (i > 0) {
                                previousId = songList.get(i - 1).getSongId();
                                songPrevious = songList.get(i - 1);
                            }
                        }
                    }
                    if (previousId != currentSongIndex) {
                        currentSongIndex = previousId;
                    }

                }

                if (songPrevious != null) {
                    playSong(songPrevious);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void nextSong() {
        Log.d("nextSong", "Next");
        Song songNext = null;
        int nextId = -1;
        String nextLocal = "";


        System.out.println("currentLocal: " + currentLocal);
        System.out.println("song.getLocal(): " + song.getLocal());

        if (!song.getLocal().equalsIgnoreCase("")) {

            for (int i = 0; i < songList.size(); i++) {
                if (songList.get(i).getLocal().equalsIgnoreCase(currentLocal)) {
                    if (i < (songList.size() - 1)) {
                        nextLocal = songList.get(i + 1).getLocal();
                        songNext = songList.get(i + 1);
                    }else{
                        nextLocal = songList.get(0).getLocal();
                        songNext = songList.get(0);
                    }
                }
            }

            if (!nextLocal.equalsIgnoreCase(currentLocal)) {
                currentLocal = nextLocal;
            }
        } else {
            for (int i = 0; i < songList.size(); i++) {
                if (songList.get(i).getSongId() == currentSongIndex) {
                    if (i < (songList.size() - 1)) {
                        nextId = songList.get(i + 1).getSongId();
                        songNext = songList.get(i + 1);
                    }else
                    {
                        nextId = songList.get(0).getSongId();
                        songNext = songList.get(0);
                    }
                }
            }

            if (nextId != currentSongIndex) {
                currentSongIndex = nextId;
            }
        }

        System.out.println("songNext: " + songNext);
        if (songNext != null) {
            playSong(songNext);
        }

    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("MediaPlayerCompletion");
            if (message.equalsIgnoreCase("NextSong")) {
                nextSong();
                Log.d("DBG", "Play next song: " + message);
            }
        }
    };
}
