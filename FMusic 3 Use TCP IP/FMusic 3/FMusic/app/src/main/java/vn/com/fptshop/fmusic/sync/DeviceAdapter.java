package vn.com.fptshop.fmusic.sync;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.com.fptshop.fmusic.R;

/**
 * Created by MinhDH on 12/29/15.
 */
public class DeviceAdapter extends BaseAdapter {

    private Activity activity;
    private List<Device> data;
    private static LayoutInflater inflater=null;

    public DeviceAdapter(Activity a) {
        activity = a;
        data = new ArrayList<>();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void addData(Device device){
        data.add(device);
    }

    public int getCount() {
        return data.size();
    }

    public Device getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);
        TextView title = (TextView)vi.findViewById(R.id.title);
        title.setText(data.get(position).getName() + " ["+data.get(position).getSerial()+"]");
        return vi;
    }
}
