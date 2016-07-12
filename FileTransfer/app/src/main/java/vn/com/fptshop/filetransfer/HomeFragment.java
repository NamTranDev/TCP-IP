package vn.com.fptshop.filetransfer;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeFragment extends Fragment {
    Button btnServer;
    Button btnClient;

    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnServer = (Button) view.findViewById(R.id.btnServer);
        btnClient = (Button) view.findViewById(R.id.btnClient);
        btnServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionServer();

            }
        });
        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionClient();
            }
        });
        return view;
    }

    private void actionServer() {
        Fragment fragment = new ServerFragment();
        displayView(fragment);
        System.out.println("Start Server");
    }

    private void actionClient() {
        Fragment fragment = new ClientFragment();
        displayView(fragment);
        System.out.println("Start Client");
    }

    private void displayView(Fragment fragment) {

        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }
    }


}
