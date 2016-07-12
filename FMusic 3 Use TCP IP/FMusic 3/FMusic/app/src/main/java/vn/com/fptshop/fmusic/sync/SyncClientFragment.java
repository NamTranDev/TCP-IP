package vn.com.fptshop.fmusic.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.com.fptshop.fmusic.AppSetting;
import vn.com.fptshop.fmusic.R;

public class SyncClientFragment extends Fragment {
    TextView textView;
    TextView textFile;

    public SyncClientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mMessageReceiver), new IntentFilter("addContactSuccess"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync_client, container, false);
        textView = (TextView) view.findViewById(R.id.textView);
        textFile = (TextView) view.findViewById(R.id.textFile);
        return view;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("ContactManager");
            final boolean filename = intent.getBooleanExtra("filename", false);
            if (message.equalsIgnoreCase("Success")) {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (filename) {
                                textFile.setVisibility(View.VISIBLE);
                            }
                            textView.setText(AppSetting.proccess);
                        }
                    });
                }catch (NullPointerException e){
                    e.printStackTrace();
                }


            }
        }
    };

    @Override
    public void onDestroy() {
        if (mMessageReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        }
        super.onDestroy();

    }
}
