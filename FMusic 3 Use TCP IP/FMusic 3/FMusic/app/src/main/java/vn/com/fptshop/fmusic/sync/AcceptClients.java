package vn.com.fptshop.fmusic.sync;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.SocketHandler;

import vn.com.fptshop.fmusic.AppSetting;

public class AcceptClients extends Thread implements Runnable {
    Socket socket;
    ServerSocket serverSocket;
    AcceptClients(String imeNiti, ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
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
                DataInputStream inputStream = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(
                        socket.getOutputStream());
                String messageFromClient = inputStream.readUTF();
                System.out.println("READ CLIENT " + messageFromClient);
                int rd = AppSetting.random();
                outputStream.writeUTF("" + rd);

                //setting
                AppSetting.confirm = "" + rd;
                AppSetting.inputStream = inputStream;
                AppSetting.outputStream = outputStream;
                AppSetting.socket = socket;
                Fragment fragment = new InputConfirmFragment();

                SyncActivity.replaceFragment(fragment);

                ReceiveMasageFromClient masageFromClient = new ReceiveMasageFromClient(inputStream, "StartReceiveMsgs_" + Build.SERIAL);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}