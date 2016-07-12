package vn.com.fptshop.filetransfer;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;


public class ServerFragment extends Fragment {
    TextView lblIpAddress;

    public ServerFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server, container, false);
        lblIpAddress = (TextView) view.findViewById(R.id.lblIPAddress);
        lblIpAddress.setText(getIpAddress());
        startServer();
        return view;
    }

    private void startServer() {
        try {


            System.out.println("Đang đợi kết nối...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ServerSocket serverSocket = null;
                    try {
                        serverSocket = new ServerSocket(1234);
                        Socket socket = serverSocket.accept();
                        int index = 1;
                        ServerProc proc = new ServerProc(socket, index);
                        System.out.println("Đã kết nối với máy " + index++);
                        proc.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress();
                    }

                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }

}
