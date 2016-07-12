package vn.com.fptshop.fmusic;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 16/12/2015.
 */
public class PlayMusicService extends Service implements MediaPlayer.OnCompletionListener {

    public static MediaPlayer mp;

    public PlayMusicService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp = new MediaPlayer();
        mp.setOnCompletionListener(this);
        mp.reset();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("super.onDestroy();");
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(fd, writer, args);
    }

    public class LocalBinder extends Binder {
        public PlayMusicService getService() {
            return PlayMusicService.this;
        }
    }

    private IBinder iBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }



    public void playSong(String urlSong) {
        // Play song
        try {
            mp.reset();
//            mp.release();
            mp.setDataSource(urlSong);
            mp.prepare();
            mp.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean checkMediaPlayer() {
        if (mp != null) {
            if (mp.isPlaying()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void Pause() {
        if (mp.isPlaying()) {
            if (mp != null) {
                mp.pause();
                Log.d("Player Service", "Pause");
            }
        }
    }

    public void Start() {
        if (!mp.isPlaying()) {
            if (mp != null) {
                mp.start();
                Log.d("Player Service", "Pause");
            }
        }
    }

    /**
     * On Song Playing completed play next song
     *
     */
    public void onCompletion(MediaPlayer arg0) {
        System.out.println("onCompletion");
        sendUpdateMessage();
    }

    private void sendUpdateMessage() {
        Log.d("KBR", "sendUpdateMessage");
        Intent intent = new Intent("sendUpdateMessage");
        intent.putExtra("MediaPlayerCompletion", "NextSong");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
