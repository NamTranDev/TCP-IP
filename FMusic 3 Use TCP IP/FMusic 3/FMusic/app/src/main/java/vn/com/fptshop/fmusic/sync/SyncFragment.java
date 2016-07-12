package vn.com.fptshop.fmusic.sync;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.List;

import vn.com.fptshop.fmusic.AppSetting;
import vn.com.fptshop.fmusic.R;

/**
 * Nam Tran
 */
public class SyncFragment extends Fragment implements IOnProccessListener {
    TextView textView;
    ContactManager contactManager;
    Gson gson;
    MediaManager mediaManager;

    public SyncFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sync, container, false);
        textView = (TextView) view.findViewById(R.id.textView);
//        button = (Button) view.findViewById(R.id.btnSync);

        contactManager = new ContactManager(getActivity(), this);
        mediaManager = new MediaManager(getActivity());
        gson = new Gson();
        actionSync();
        return view;
    }

    public static Thread mThread;
    private void actionSync() {
        Log.d("abc","" + SyncActivity.backpress);
        mThread = new Thread(new Runnable() {
            public void run() {
                List<ContactManager.PhoneContact> contacts = null;
                ArrayList<File> filesimage = null;
                ArrayList<File> filesvideo = null;
                ArrayList<File> filesmp3 = null;
                try {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("Đang đồng bộ...");
                            }
                        });
                    }
                    AppSetting.outputStream.writeUTF("SEND@@");
                    String fileChoose ="";
                    String contactss="";
                    String image="";
                    String videos="";
                    String mp3="";
                    if (FragmentChooseResource.contact)
                    {
                        contactss = "SENDContact@-@";
                        contacts = contactManager.getContacts();
                    }if (FragmentChooseResource.image)
                    {
                        image = "SENDImage@-@";
                        filesimage = mediaManager.getAllFileImageFromGalleries();
                    }if (FragmentChooseResource.video)
                    {
                        videos = "SENDVideo@-@";
                        filesvideo = mediaManager.getAllFileVideoFromGalleries();
                    }if (FragmentChooseResource.mp3)
                    {
                        mp3 = "SENDMp3@-@";
                        filesmp3 = mediaManager.getAllFileMusicFromGalleries();
                    }
                    fileChoose = contactss + image + videos + mp3;
                    AppSetting.outputStream.writeUTF(fileChoose);
                    if (FragmentChooseResource.contact)
                    {
                        AppSetting.outputStream.writeInt(contacts.size());
                        for (ContactManager.PhoneContact contact : contacts) {
                            AppSetting.outputStream.writeUTF(gson.toJson(contact));
                        }
                    }if (FragmentChooseResource.image)
                    {
                        AppSetting.outputStream.writeInt(filesimage.size());
                        byte[] buffer = new byte[1024];
                        int len =0;
                        for (int i =0;i<filesimage.size();i++) {

                            AppSetting.outputStream.writeUTF(filesimage.get(i).getName() + "@@" + filesimage.get(i).length());
                            Log.d("FILELENGHT", filesimage.get(i).getName() + " / " + filesimage.get(i).length());
                            AppSetting.outputStream.flush();
                        }
                        int count;
                        for (int i=0;i<filesimage.size();i++)
                        {
                            count = i;
                            Log.d("File", "ok");
                            FileInputStream fileInputStream = new FileInputStream(filesimage.get(i));
                            while ((len = fileInputStream.read(buffer)) != -1) {
                                Log.d("FILE", "Read");
                                AppSetting.outputStream.write(buffer, 0, len);
                                Log.d("FILE", "Read File");
                                if(SyncActivity.backpress){
                                   // Use timer.interrupt() instead of .stop(). Thread.stop() is deprecated, as your Log says.
                                    mThread.interrupt();
                                    break;
                                }
                            }
                            AppSetting.outputStream.flush();
                            Log.d("FILE",".....");
                            proccess("Đồng bộ file " + filesimage.get(i).getName() + "\n" + count + " / " + filesimage.size() );
                            Log.d("FILE","......................");
                        }
                    }if (FragmentChooseResource.video)
                    {
                        AppSetting.outputStream.writeInt(filesvideo.size());
                        byte[] buffer = new byte[1024];
                        int len =0;
                        for (int i =0;i<filesvideo.size();i++) {

                            AppSetting.outputStream.writeUTF(filesvideo.get(i).getName() + "@@" + filesvideo.get(i).length());
                            Log.d("FILELENGHT", filesvideo.get(i).getName() + " / " + filesvideo.get(i).length());
                            AppSetting.outputStream.flush();
                        }
                        int count;
                        for (int i=0;i<filesvideo.size();i++)
                        {
                            count = i;
                            Log.d("File", "ok");
                            FileInputStream fileInputStream = new FileInputStream(filesvideo.get(i));
                            while ((len = fileInputStream.read(buffer)) != -1) {
                                Log.d("FILE", "Read");
                                AppSetting.outputStream.write(buffer, 0, len);
                                if(SyncActivity.backpress){
                                    mThread.interrupt();
                                    break;
                                }
                                Log.d("FILE", "Read File");
                            }
                            AppSetting.outputStream.flush();
                            Log.d("FILE",".....");
                            proccess("Đồng bộ file " + filesvideo.get(i).getName() + "\n" + count + " / " + filesvideo.size() );
                            Log.d("FILE","......................");
                        }
                    }if (FragmentChooseResource.mp3)
                    {
                        AppSetting.outputStream.writeInt(filesmp3.size());
                        byte[] buffer = new byte[1024];
                        int len =0;
                        for (int i =0;i<filesmp3.size();i++) {

                            AppSetting.outputStream.writeUTF(filesmp3.get(i).getName() + "@@" + filesmp3.get(i).length());
                            Log.d("FILELENGHT", filesmp3.get(i).getName() + " / " + filesmp3.get(i).length());
                            AppSetting.outputStream.flush();
                        }
                        int count;
                        for (int i=0;i<filesmp3.size();i++)
                        {
                            count = i;
                            Log.d("File", "ok");
                            FileInputStream fileInputStream = new FileInputStream(filesmp3.get(i));
                            while ((len = fileInputStream.read(buffer)) != -1) {
                                Log.d("FILE", "Read");
                                AppSetting.outputStream.write(buffer, 0, len);
                                Log.d("FILE", "Read File");
                                if(SyncActivity.backpress){
                                    mThread.interrupt();
                                    break;
                                }
                            }
                            AppSetting.outputStream.flush();
                            Log.d("FILE",".....");
                            proccess("Đồng bộ file " + filesmp3.get(i).getName() + "\n" + count + " / " + filesmp3.size() );
                            Log.d("FILE","......................");
                        }
                    }

                }catch (FileNotFoundException e) {
                    proccess("Một vài file của bạn có vấn đề . Vui lòng thử lại !!!");
                }
                catch (IOException e) {
                    proccess("Đã mất kết nối . Vui lòng kiểm tra kết nối và thử lai");
                }
            }
        });
        mThread.start();
//        button.setVisibility(View.GONE);
    }

    public static void stopSync(){
        if(mThread.getState() != Thread.State.NEW){
            mThread.interrupt();
        }
    }

    @Override
    public void proccess(final String s) {

        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText(s);
                }
            });
        }catch (NullPointerException e){
        }
    }

}
