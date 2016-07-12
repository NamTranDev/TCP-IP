package vn.com.fptshop.fmusic.sync;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import vn.com.fptshop.fmusic.R;

/**
 * Created by Nam on 1/11/2016.
 */
public class FragmentChooseResource extends Fragment implements View.OnClickListener{
    Button btnsync;
    CheckBox ckbContact;
    CheckBox ckbImage;
    CheckBox ckbVideo;
    CheckBox ckbMp3;
    public static boolean contact = false;
    public static boolean image = false;
    public static boolean video = false;
    public static boolean mp3 = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_resource,container,false);
        btnsync = (Button) view.findViewById(R.id.btnsync);
        ckbContact = (CheckBox) view.findViewById(R.id.ckbContact);
        ckbImage = (CheckBox) view.findViewById(R.id.ckbImage);
        ckbVideo = (CheckBox) view.findViewById(R.id.ckbVideo);
        ckbMp3 = (CheckBox) view.findViewById(R.id.ckbMp3);

        btnsync.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if ( id == R.id.btnsync )
        {
            if (ckbContact.isChecked()){
                contact = true;
            }else
            {
                contact = false;
            }if (ckbImage.isChecked()){
            image = true;
        }else
        {
            image = false;
        }if (ckbVideo.isChecked()){
            video = true;
        }else
        {
            video = false;
        }if (ckbMp3.isChecked()){
            mp3 = true;
        }else
        {
            mp3 = false;
        }
            if (contact == false && image == false && video == false && mp3 == false)
            {
                Toast.makeText(getActivity()," Vui lòng tích chọn loại cần đồng bộ",Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d("CHOOSE","" + contact + "/" + image + "/" + video + "/" + mp3);
                Fragment fragment = new SyncFragment();
                SyncActivity.replaceFragment(fragment);
            }

        }
    }
}
