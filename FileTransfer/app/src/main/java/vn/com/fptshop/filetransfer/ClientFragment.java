package vn.com.fptshop.filetransfer;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;


public class ClientFragment extends Fragment {
    EditText editTextIpAddress;
    EditText editTextPort;
    Button btnStartClient;

    public ClientFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client, container, false);
        editTextIpAddress = (EditText) view.findViewById(R.id.editTextIPAddress);
        editTextPort = (EditText) view.findViewById(R.id.editTextPort);
        btnStartClient = (Button) view.findViewById(R.id.btnStartClient);
        btnStartClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionStart();
            }
        });
        return view;
    }

    private void actionStart() {

        try {
            final String ipAddress = editTextIpAddress.getText().toString();
            final Integer port = Integer.parseInt(editTextPort.getText().toString());
            System.out.println("TEST CLIENT " + ipAddress + " " + port);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket socket = null;
                    try {
                        socket = new Socket(ipAddress, port);
                        DataOutputStream netOut = new DataOutputStream(socket.getOutputStream());
                        DataInputStream netIn = new DataInputStream(socket.getInputStream());
                        System.out.println("[Server respone]: " + netIn.readUTF());
                        String command;
                        String line;
                        while (true) {
                            command = "Download: ";
                            netOut.writeUTF(command);
                            line = netIn.readUTF();
                            System.out.println("[Server respone]: " + line);
                            //Thoát
                            if (line.equals("Bye bye!")) {
                                break;
                            } else {
                                if (line.indexOf("DOWNLOAD") == 0) {
                                    StringTokenizer st = new StringTokenizer(line, "@@");
                                    st.nextToken();
                                    int size = Integer.parseInt(st.nextToken());
                                    File file = Environment.getExternalStorageDirectory();
                                    for (int i = 0; i < size; i++) {
                                        String sendText = netIn.readUTF();
                                        StringTokenizer tokenizer = new StringTokenizer(sendText, "@@");
                                        tokenizer.nextToken();
                                        String pathFile = tokenizer.nextToken();
                                        long sizeFile = Long.parseLong(tokenizer.nextToken());
                                        int port = Integer.parseInt(tokenizer.nextToken());
                                        Socket socketTransfer = new Socket(ipAddress, port);
                                        System.out.println("Download " + pathFile + " " + sizeFile);
                                        File file1 = new File(file.getAbsolutePath() + pathFile);
                                        if (!file1.getParentFile().exists())
                                            file1.getParentFile().mkdirs();
                                        System.out.println("Download 1 " + file1.getAbsolutePath());
                                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file1));
                                        DataInputStream netInTransfer = new DataInputStream(socketTransfer.getInputStream());
                                        download(netInTransfer, bos);
                                        socketTransfer.close();
                                        System.out.println("Download thành công!");
                                    }
                                } else {
                                }
                            }
                            break;
                        }
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void download(DataInputStream netIn, BufferedOutputStream bos) {
        byte[] buff = new byte[1024];
        int read;
        try {
            while ((read = netIn.read(buff)) != -1) {
                bos.write(buff, 0, read);
            }
            bos.close();
            netIn.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
