package vn.com.fptshop.fmusic.sync;

import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import vn.com.fptshop.fmusic.AppSetting;

/**
 * Nam Tran
 */
public class ReceiveMasageFromClient implements Runnable {
    DataInputStream inputStream;
    String messageFromClient;

    ReceiveMasageFromClient(DataInputStream inputStream, String masageClient) {
        this.inputStream = inputStream;
        Thread thread = new Thread(this, masageClient);
        thread.start();
    }

    @Override
    public void run() {
        do {
            try {
                messageFromClient = inputStream.readUTF();
                System.out.println("READ CLIENT 2 " + messageFromClient);
                StringTokenizer tokenizer = new StringTokenizer(messageFromClient, "@@");
                String key = tokenizer.nextToken();
                if (key.equalsIgnoreCase("CONFIRM")) {
                    Fragment fragment = new SyncClientFragment();
                    SyncActivity.replaceFragment(fragment);
                }
                if (key.equalsIgnoreCase("SEND")) {
                    File File = new File(Environment.getExternalStorageDirectory() + "/FPTShop/" + "Sync" );
                    if (!File.exists()){
                        File.mkdirs();
                    }
                    String keysend = inputStream.readUTF();
                    String[] keychoose = keysend.split("@-@");
                    Log.d("Key", keysend);
                    System.out.print("ABC");
                    for (int j=0;j<keychoose.length;j++)
                    {
                        if (keychoose[j].equalsIgnoreCase("SENDContact"))
                        {
                            int data = inputStream.readInt();
                            Gson gson = new Gson();
//                        DataContacts dataContacts = gson.fromJson(data, DataContacts.class);
                            List<ContactManager.PhoneContact> contacts = new ArrayList<>();

                            System.out.println("CONTACTS 1 " + data);
//                        String sizeText = inputStream.readUTF();
                            AppSetting.constactSize = data;
                            AppSetting.constactInsert = 0;
                            for (int i = 0; i < data; i++) {
                                String temp = inputStream.readUTF();
                                ContactManager.PhoneContact contact = gson.fromJson(temp, ContactManager.PhoneContact.class);
                                contacts.add(contact);
                                AppSetting.contactManager.addContacts(contact);
                            }
                            System.out.println("CONTACTS 2 " + contacts.size());
                        }
                        if (keychoose[j].equalsIgnoreCase("SENDImage"))
                        {
                            File fileImage = new File(File.getPath() +"/Image");
                            if (!fileImage.exists())
                            {
                                fileImage.mkdirs();
                            }
                            ReceiveFile(inputStream,fileImage);
                        }if (keychoose[j].equalsIgnoreCase("SENDVideo"))
                    {
                        File fileVideo = new File(File.getPath() +"/Videos");
                        if (!fileVideo.exists())
                        {
                            fileVideo.mkdirs();
                        }
                        ReceiveFile(inputStream,fileVideo);
                    }if (keychoose[j].equalsIgnoreCase("SENDMp3"))
                    {
                        File fileMp3 = new File(File.getPath() +"/MP3");
                        if (!fileMp3.exists())
                        {
                            fileMp3.mkdirs();
                        }
                        ReceiveFile(inputStream,fileMp3);
                    }
                    }

                    AppSetting.outputStream.writeUTF("SUCCESS@@OK");
                    AppSetting.outputStream.flush();
                    Fragment fragment = new SuccessFragment();
                    SyncActivity.replaceFragment(fragment);
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

        } while (!messageFromClient.equalsIgnoreCase("CAO"));
    }
    public static void ReceiveFile(DataInputStream dataInputStream,File file)
    {
        try {
            int filesize = dataInputStream.readInt();
            Log.d("FILE", String.valueOf(filesize));
            ArrayList<String> filess = new ArrayList<>();
            int n = 0;
            byte[]buf = new byte[1024];
            for (int i=0;i<filesize;i++) {
                String filee = dataInputStream.readUTF();
                filess.add(filee);
            }

            if (filess.size() == filesize)
            {
                int count;
                for (int i=0;i<filesize;i++)
                {
                    count = i;
                    String b = filess.get(i);
                    String[] a = b.split("@@");
                    String filename = a[0];
                    long fileLenght = Long.parseLong(a[1]);
                    System.out.println("Receiving file: " + filename + " / file lenght " + fileLenght);
                    //create a new fileoutputstream for each new file
                    FileOutputStream fos = new FileOutputStream(file +"/"+ filename);
                    //read file
                    long size = 0;
                    while (fileLenght > 0 && (n = dataInputStream.read(buf,0, (int) Math.min(buf.length,fileLenght))) != -1) {
                        size += n;
                        Log.d("FILE", n + "bytes");
                        fos.write(buf, 0, n);
                        fileLenght -= n;
                    }
                    AppSetting.showFile.DisplaySync(filename,count,filesize);
                    fos.flush();
                    fos.close();
                    Log.d("FILE", ".....");

                    Log.d("FILE","OK");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}