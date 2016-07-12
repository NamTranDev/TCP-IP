package vn.com.fptshop.fmusic.sync;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import vn.com.fptshop.fmusic.AppSetting;
import vn.com.fptshop.fmusic.R;


public class ClientFragment extends Fragment {

    int dstPort = 35997;
    static final int SocketServerConnectPORT = 39995;
    TextView lblIpAddress;
    TextView lblPort;
    ListView listView;
    DeviceAdapter adapter;
    String ipHost = "";
    DataOutputStream[] outputStreams = new DataOutputStream[255];
    DataInputStream[] inputStreams = new DataInputStream[255];

    //Connect Client
    Socket socket;
    ServerSocket serverSocket;
    //Client
    Socket socketConnect;
    ProgressDialog progress;

    public ClientFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
//        lblIpAddress = (TextView) view.findViewById(R.id.lblIp);
//        lblPort = (TextView) view.findViewById(R.id.lblPort);
        String ip = AppSetting.getIpAddress();

        ipHost = ip.substring(0, ip.lastIndexOf(".") + 1);
//        lblIpAddress.setText("IP: " + ipHost);
        adapter = new DeviceAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actionClickItem(position);
            }
        });
        progress = ProgressDialog.show(getActivity(), "",
                "Đang tìm thiết bị kết nối...", true);
        progress.setCancelable(true);
        MyClientTask myClientTask = new MyClientTask();
        myClientTask.execute();

        return view;
    }



    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String[] results = new String[255];
        Socket[] sockets = new Socket[255];
        int count = 0;

        MyClientTask() {

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            for (int i = 0; i < 255; i++) {
                final String ip = ipHost + i;
                System.out.println("TEST " + ip);
                final String finalTMsg = Build.DEVICE + "@@" + Build.SERIAL;
                final int ind = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            sockets[ind] = new Socket(ip, dstPort);
                            outputStreams[ind] = new DataOutputStream(
                                    sockets[ind].getOutputStream());
                            inputStreams[ind] = new DataInputStream(sockets[ind].getInputStream());

                            outputStreams[ind].writeUTF(finalTMsg);

                            results[ind] = inputStreams[ind].readUTF();

                            System.out.println("TRA " + results[ind]);
                            StringTokenizer tokenizer = new StringTokenizer(results[ind], "@@");
                            final String name = tokenizer.nextToken();
                            final String serial = tokenizer.nextToken();
                            final Socket finalSocket = sockets[ind];
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Device device = new Device();
                                    device.setName(name);
                                    device.setIp(finalSocket.getInetAddress() + "");
                                    device.setPosition(ind);
                                    device.setSerial(serial);
                                    adapter.addData(device);
                                    adapter.notifyDataSetChanged();
                                    progress.dismiss();
                                }
                            });
                            ReceiveMasageConnect receiveMasage = new ReceiveMasageConnect(ind, "StartReceiveMsgs_" + ind);
                        } catch (UnknownHostException e) {

                            e.printStackTrace();
                        } catch (IOException e) {

                            e.printStackTrace();

                        } finally {
                            System.out.println("COUNT " + count++);
                        }
                    }
                }).start();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

    public class ReceiveMasageConnect implements Runnable {
        int position;
        String messageFromClient;

        ReceiveMasageConnect(int position, String masageClient) {
            this.position = position;
            Thread thread = new Thread(this, masageClient);
            thread.start();
        }

        @Override
        public void run() {
            try {
                do {
                    try {
                        messageFromClient = inputStreams[position].readUTF();
                        System.out.println("READ CLIENT 1 " + messageFromClient);
                        StringTokenizer tokenizer = new StringTokenizer(messageFromClient, "@@");
                        String key = tokenizer.nextToken();
                        // connect client
                        if (key.equalsIgnoreCase("CONNECT")) {
                            String values = tokenizer.nextToken();
                            socketConnect = new Socket(values, SocketServerConnectPORT);
                            ConnectServer connectServer = new ConnectServer();
                            System.out.println("TEST OK");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } while (!messageFromClient.equalsIgnoreCase("CAO"));
            } catch (Exception e) {

            }
        }
    }

    public class ConnectServer implements Runnable {
        DataInputStream inputStream;
        DataOutputStream outputStream;

        ConnectServer() {
            Thread thread = new Thread(this, "RunServerConnect");
            thread.start();
        }

        @Override
        public void run() {
            try {
                if (serverSocket != null)
                    serverSocket.close();
                outputStream = new DataOutputStream(socketConnect.getOutputStream());
                inputStream = new DataInputStream(socketConnect.getInputStream());
                outputStream.writeUTF("CONNECTED");
                outputStream.flush();
                System.out.println("CONNECTED");
                String s = inputStream.readUTF();
                AppSetting.confirm = s;
                Fragment fragment = new ConfirmFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
                ReceiveMasageConnect2 masageConnect = new ReceiveMasageConnect2(outputStream, inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ReceiveMasageConnect2 implements Runnable {
        int position;
        String messageFromClient;
        DataInputStream inputStream;
        DataOutputStream outputStream;

        ReceiveMasageConnect2(DataOutputStream outputStream, DataInputStream inputStream) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
            Thread thread = new Thread(this, "ReceiveMasageConnect");
            thread.start();
        }

        @Override
        public void run() {
            try {
                do {
                    try {

                        messageFromClient = inputStream.readUTF();
                        System.out.println("READ CLIENT 1 2  " + messageFromClient);


                        StringTokenizer tokenizer = new StringTokenizer(messageFromClient, "@@");
                        String key = tokenizer.nextToken();
                        if (key.equalsIgnoreCase("CONFIRM")) {
                            outputStream.writeUTF("CONFIRM@@OK");
                            outputStream.flush();
                            AppSetting.inputStream = inputStream;
                            AppSetting.outputStream = outputStream;
                            Fragment fragment = new SyncFragment();
                            SyncActivity.replaceFragment(fragment);

                        }
                        if (key.equalsIgnoreCase("SUCCESS")) {
                            System.out.println("@@");
                            Fragment fragment = new SuccessFragment();
                            SyncActivity.replaceFragment(fragment);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } while (!messageFromClient.equalsIgnoreCase("CAO"));
            } catch (Exception e) {

            }
        }
    }

    private void actionClickItem(int position) {
        Device device = adapter.getItem(position);
        System.out.println(device.getName());
        int port = AppSetting.random();
        try {

            serverSocket = new ServerSocket(port);
            outputStreams[device.getPosition()].writeUTF("CONNECT@@" + AppSetting.getIpAddress() + "@@" + port);
            outputStreams[device.getPosition()].flush();
            System.out.println("TEST " + device.getName() + " " + device.getPosition());
            AcceptClients newClients = new AcceptClients("RunServer", serverSocket);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

}
