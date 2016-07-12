package vn.com.fptshop.fmusic.sync;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.com.fptshop.fmusic.AppSetting;
import vn.com.fptshop.fmusic.R;


public class ConfirmFragment extends Fragment {
    String confirm;
    TextView textView;
    static FragmentActivity activity;

    public ConfirmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);

        textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(AppSetting.confirm);
        return view;
    }




}
