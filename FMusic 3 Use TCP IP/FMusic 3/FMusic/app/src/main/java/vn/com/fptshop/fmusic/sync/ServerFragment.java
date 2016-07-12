package vn.com.fptshop.fmusic.sync;

import android.app.ProgressDialog;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

import vn.com.fptshop.fmusic.AppSetting;
import vn.com.fptshop.fmusic.R;

public class ServerFragment extends Fragment {
    //server
    ServerSocket serverSocket;
    static final int SocketServerPORT = 35997;
    Socket socket;
    int count = 0;
    //Client
    Socket socketConnect;
    static final int SocketServerConnectPORT = 39995;

    TextView lblIpAddress;
    TextView lblPort;
    ListView listView;
    DeviceAdapter adapter;

    DataInputStream[] inputStreams = new DataInputStream[50];
    DataOutputStream[] outputStreams = new DataOutputStream[50];
    Fragment fragment;
    ProgressDialog progress;

    public ServerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server, container, false);
//        listView = (ListView) view.findViewById(R.id.listView);
        lblIpAddress = (TextView) view.findViewById(R.id.lblIp);
        lblPort = (TextView) view.findViewById(R.id.lblPort);
        String msgReply = Build.DEVICE + " [" + Build.SERIAL + "]";
        lblIpAddress.setText("Đã chia sẻ thiết bị: ");
        lblPort.setText(msgReply);
//        adapter = new DeviceAdapter(getActivity());
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                actionClickItem(position);
//            }
//        });
//        progress = ProgressDialog.show(getActivity(),"",
//                "Đang tìm thiết bị kết nối...", true);
        //start server
        startServerTCP();
        return view;

    }

    private void startServerTCP() {
        try {
            serverSocket = new ServerSocket(SocketServerPORT);

            System.out.println("TEST " + serverSocket.isClosed() + " " + AppSetting.getIpAddress());
            // accept new client
            AcceptNewClients newClients = new AcceptNewClients("RunServer");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class AcceptNewClients extends Thread implements Runnable {
        AcceptNewClients(String imeNiti) {
            Thread thread = new Thread(this, imeNiti);
            thread.start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (Exception e) {
                    break;
                }
                try {
                    inputStreams[count] = new DataInputStream(
                            socket.getInputStream());
                    outputStreams[count] = new DataOutputStream(
                            socket.getOutputStream());
                    // read key from client
                    String messageFromClient = inputStreams[count].readUTF();
                    System.out.println("READ CLIENT " + messageFromClient);

                    StringTokenizer tokenizer = new StringTokenizer(messageFromClient, "@@");
                    // name device client
                    final String name = tokenizer.nextToken();
                    // serial device client
                    final String serial = tokenizer.nextToken();

                    final String finalMessageFromClient = messageFromClient;
                    final Socket finalSocket = socket;
                    // update UI
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Device device = new Device();
                            device.setName(name);
                            device.setIp(finalSocket.getInetAddress() + "");
                            device.setPosition(count);
                            device.setSerial(serial);
//                            adapter.addData(device);
//                            adapter.notifyDataSetChanged();
//                            progress.dismiss();
                        }
                    });
                    // send key to client
                    String msgReply = Build.DEVICE + "@@" + Build.SERIAL;
                    outputStreams[count].writeUTF(msgReply);
                    outputStreams[count].flush();
                    // Receive key all client
                    ReceiveMasage receiveMasage = new ReceiveMasage(count, "StartReceiveMsgs_" + count);
                    count++;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    boolean check = false;

    public class ReceiveMasage implements Runnable {
        int position;
        String messageFromClient;

        ReceiveMasage(int position, String masageClient) {
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
                        // connect
                        if (key.equalsIgnoreCase("CONNECT") && check == false) {
                            String values = tokenizer.nextToken();
                            String port = tokenizer.nextToken();
                            socketConnect = new Socket(values, Integer.parseInt(port));
                            ConnectServer connectServer = new ConnectServer();
                            check = true;
                            break;
                        } else {
                            System.out.println("SKLFDLSAJL");
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
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

                outputStream = new DataOutputStream(socketConnect.getOutputStream());
                inputStream = new DataInputStream(socketConnect.getInputStream());
                outputStream.writeUTF("CONNECTED");
                outputStream.flush();
                System.out.println("CONNECTED");
                String s = inputStream.readUTF();
                AppSetting.confirm = s;
                fragment = new ConfirmFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
                ReceiveMasageConnect masageConnect = new ReceiveMasageConnect(outputStream, inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ReceiveMasageConnect implements Runnable {
        int position;
        String messageFromClient;
        DataInputStream inputStream;
        DataOutputStream outputStream;

        ReceiveMasageConnect(DataOutputStream outputStream, DataInputStream inputStream) {
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
                            Fragment fragment = new FragmentChooseResource();
                            SyncActivity.replaceFragment(fragment);
                        }
                        if (key.equalsIgnoreCase("SUCCESS")) {
                            System.out.println("@@");
                            Fragment fragment = new SuccessFragment();
                            SyncActivity.replaceFragment(fragment);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }

                } while (!messageFromClient.equalsIgnoreCase("CAO"));
            } catch (Exception e) {

            }
        }
    }

    private void actionClickItem(int position) {
        Device device = adapter.getItem(position);
        System.out.println(device.getName());
        try {
            serverSocket = new ServerSocket(SocketServerConnectPORT);
            outputStreams[device.getPosition()].writeUTF("CONNECT@@" + AppSetting.getIpAddress());
            outputStreams[device.getPosition()].flush();
            AcceptClients clients = new AcceptClients("RunServer", serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
        System.out.println("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
