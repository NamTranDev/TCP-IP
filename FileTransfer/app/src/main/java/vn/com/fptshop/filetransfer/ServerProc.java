package vn.com.fptshop.filetransfer;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class ServerProc extends Thread {

    Socket socket;
    DataOutputStream netOut;
    DataInputStream netIn;
    byte[] buff = new byte[1024];
    int may;

    public ServerProc(Socket s, int may) throws IOException {
        this.socket = s;
        this.may = may;
        netOut = new DataOutputStream(socket.getOutputStream());
        netIn = new DataInputStream(socket.getInputStream());
        netOut.writeUTF("Welcome!");
        netOut.flush();
    }

    @Override
    public void run() {
        String line;
        while (true) {
            try {
                line = netIn.readUTF();
                //Thoat
                if (line.equalsIgnoreCase("quit")) {
                    netOut.writeUTF("Bye bye!");
                    netOut.flush();
                    break;
                } else {
                    //Download
                    if (line.indexOf("Download: ") == 0) {
                        File file = Environment.getExternalStorageDirectory();
                        List<File> fileList = getAllFileExtensionFilterFromDir(file, ExtensionsNameFilter.MP3_FILTER);
                        System.out.println("FILE SIZE  = " + fileList.size());
                        netOut.writeUTF("DOWNLOAD@@" + fileList.size());
                        netOut.flush();

                        for (File file1 : fileList) {
                            int port = random();
                            System.out.println(file1.getName() + " " + file1.length());
                            netOut.writeUTF("SEND@@" + file1.getAbsolutePath().replace(file.getAbsolutePath(), "") + "@@" + file1.length()+"@@"+port);
                            netOut.flush();
                            ServerSocket serverSocketTransfer = new ServerSocket(port);
                            Socket socketTransfer = serverSocketTransfer.accept();
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file1));
                            DataOutputStream dos = new DataOutputStream(socketTransfer.getOutputStream());
                            procWriteDataToClient(bis, dos);
                            serverSocketTransfer.close();
                        }

// String fileSource_Dest = line.substring(10, line.length());
//                        StringTokenizer st = new StringTokenizer(fileSource_Dest, "_");
//                        String fileSource = st.nextToken();
//                        String fileDest = st.nextToken();
//                        int portTran = 54321 + may;
//                        ServerSocket serverSocketTransfer = new ServerSocket(portTran);
//                        netOut.writeUTF("DOWNLOAD_" + fileSource + "_" + fileDest + "_" + portTran);
//                        netOut.flush();
//                        Socket socketTransfer = serverSocketTransfer.accept();
//                        System.out.println("Đã kết nối trao đổi dư liệu máy " + may);
//                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileSource));
//                        DataOutputStream dos = new DataOutputStream(socketTransfer.getOutputStream());
//                        procWriteDataToClient(bis, dos);
//                        serverSocketTransfer.close();
                    } else {
                        //Upload
                        if (line.indexOf("Upload: ") == 0) {
                            String fileSource_Dest = line.substring(8, line.length());
                            StringTokenizer st = new StringTokenizer(fileSource_Dest, "_");
                            String fileSource = st.nextToken();
                            String fileDest = st.nextToken();
                            int portTran = 12345 + may;
                            netOut.writeUTF("UPLOAD_" + fileSource + "_" + fileDest + "_" + portTran);
                            netOut.flush();
                            ServerSocket serverSocketTransfer = new ServerSocket(portTran);
                            Socket socketTransfer = serverSocketTransfer.accept();
                            System.out.println("Đã kết nối trao đổi dư liệu máy " + may);
                            BufferedOutputStream bis = new BufferedOutputStream(new FileOutputStream(fileDest));
                            DataInputStream dis = new DataInputStream(socketTransfer.getInputStream());
                            procReadDataToClient(bis, dis);
                            socketTransfer.close();
                        } else {//Khong hieu lenh
                            netOut.writeUTF("Unknown Your Command!");
                            netOut.flush();
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            socket.close();
            System.out.println("Đã đóng kết nối máy " + may);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    // random
    public static int random() {
        int max = 9999;
        int min = 1000;
        int diff = max - min;
        Random rn = new Random();
        int i = rn.nextInt(diff + 1);
        i += min;
        System.out.print("The Random Number is " + i);
        return i;
    }
    private void procWriteDataToClient(BufferedInputStream bis, DataOutputStream dos) throws IOException {
        int read;
        int count = 0;
        while ((read = bis.read(buff)) != -1) {
            dos.write(buff, 0, read);
        }
        dos.flush();
        bis.close();
        dos.close();
    }

    private void procReadDataToClient(BufferedOutputStream bos, DataInputStream dis) throws IOException {
        int read;
        while ((read = dis.read(buff)) != -1) {
            bos.write(buff, 0, read);
        }
        bos.close();
        dis.close();
    }

    public static ArrayList<File> getAllFileExtensionFilterFromDir(File dir, String[] extensions) {
        ArrayList<File> files = new ArrayList<File>();
        ExtensionsNameFilter extensionsNameFilter = new ExtensionsNameFilter(extensions);
        File[] file = dir.listFiles();
        for (File fileChild : file) {
            if (fileChild.isDirectory()) {
                files.addAll(getAllFileExtensionFilterFromDir(fileChild, extensions));
            } else if (extensionsNameFilter.accept(fileChild, fileChild.getName())) {
                if (fileChild.length() > 0)
                    files.add(fileChild);
            }
        }
        return files;
    }

    public static class ExtensionsNameFilter implements FilenameFilter {
        public static final String[] IMAGE_FILTER = new String[]{".png", ".jpg", ".bmp"};
        public static final String[] MP3_FILTER = new String[]{".mp3"};
        public static final String[] VIDEO_FILTER = new String[]{".mp4"};
        String[] mExtensions;

        public ExtensionsNameFilter(String[] extensions) {
            mExtensions = extensions;
        }

        @Override
        public boolean accept(File dir, String filename) {
            String lowercaseName = filename.toLowerCase();
            for (String ext : mExtensions) {
                if (lowercaseName.endsWith(ext)) {
                    return true;
                }
            }
            return false;
        }
    }
}
