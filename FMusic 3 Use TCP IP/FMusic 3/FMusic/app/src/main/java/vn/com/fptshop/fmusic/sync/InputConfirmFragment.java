package vn.com.fptshop.fmusic.sync;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;

import vn.com.fptshop.fmusic.AppSetting;
import vn.com.fptshop.fmusic.R;

public class InputConfirmFragment extends Fragment {
    EditText editText;
Button button;
    public InputConfirmFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_confirm, container, false);
        editText = (EditText) view.findViewById(R.id.editText);
        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionConfirm(editText.getText().toString());
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    actionConfirm(editText.getText().toString());
                }
                return false;
            }
        });
        return view;
    }

    private void actionConfirm(String key) {
        if(key.equalsIgnoreCase(AppSetting.confirm)){
            System.out.println("OK");
            DataOutputStream outputStream = AppSetting.outputStream;
            try {
                outputStream.writeUTF("CONFIRM@@OK");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("SAI");
        }
    }

}
