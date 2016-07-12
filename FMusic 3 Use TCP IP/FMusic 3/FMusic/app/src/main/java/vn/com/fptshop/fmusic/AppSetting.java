package vn.com.fptshop.fmusic;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Formatter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import vn.com.fptshop.fmusic.sync.ContactManager;
import vn.com.fptshop.fmusic.sync.MediaManager;
import vn.com.fptshop.fmusic.sync.ShowFile;

/**
 * Created by MinhDH on 11/26/15.
 */
public class AppSetting {
    public static final String URL = "http://118.69.201.53:8887";
    public static int currentAppComboId = -1;
    public static String MODE_ONLINE = "ONLINE";
    public static String MODE_OFFLINE = "OFFLINE";
    public static int currentStatus = 0;

    //sync contacts
    public static String confirm = "";

    public static DataInputStream inputStream;

    public static DataOutputStream outputStream;

    public static Socket socket;

    public static String proccess = "";

    public static ContactManager contactManager;

    public static ShowFile showFile;

    public static int constactSize = 0;

    public static int constactInsert = 0;

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
    // random
    public static int randomPort() {
        int max = 99999;
        int min = 10000;
        int diff = max - min;
        Random rn = new Random();
        int i = rn.nextInt(diff + 1);
        i += min;
        System.out.print("The Random Number is " + i);
        return i;
    }

    /**
     * return all file with extension filter from Directory
     * @param dir
     * @param extensions
     * @return
     */
    public static ArrayList<File> getAllFileExtensionFilterFromDir(File dir,String[] extensions)
    {
        ArrayList<File> files = new ArrayList<File>();
        ExtensionsNameFilter extensionsNameFilter = new ExtensionsNameFilter(extensions);
        File[] file = dir.listFiles();
        for (File fileChild : file)
        {
            if (fileChild.isDirectory())
            {
                files.addAll(getAllFileExtensionFilterFromDir(fileChild,extensions));
            }
            else if (extensionsNameFilter.accept(fileChild,fileChild.getName()))
            {
                files.add(fileChild);
            }
        }
        return files;
    }

    public static class ExtensionsNameFilter implements FilenameFilter {
        public static final String[] IMAGE_FILTER = new String[] {".png", ".jpg", ".bmp"};
        public static final String[] MP3_FILTER = new String[] {".mp3"};
        public static final String[] VIDEO_FILTER = new String[] {".mp4"};
        String[] mExtensions;
        public ExtensionsNameFilter(String[] extensions)
        {
            mExtensions = extensions;
        }
        @Override
        public boolean accept(File dir, String filename) {
            String lowercaseName = filename.toLowerCase();
            for(String ext : mExtensions) {
                if (lowercaseName.endsWith(ext)) {
                    return true;
                }
            }
            return false;
        }
    }

}