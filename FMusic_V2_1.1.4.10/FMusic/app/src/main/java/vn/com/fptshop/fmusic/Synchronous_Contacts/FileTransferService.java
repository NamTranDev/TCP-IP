// Copyright 2011 Google Inc. All Rights Reserved.

package vn.com.fptshop.fmusic.Synchronous_Contacts;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * A service that process each file transfer request i.e Intent by opening a
 * socket connection with the WiFi Direct Group Owner and writing the file
 */
public class FileTransferService extends IntentService {

	Handler mHandler;
	private static final int SOCKET_TIMEOUT = 5500;
	public static final String ACTION_SEND_FILE = "com.example.android.wifidirect.SEND_FILE";
	public static final String EXTRAS_URI_CONTACT = "file_url_contact";
	public static final String EXTRAS_ADDRESS = "go_host";
	public static final String EXTRAS_PORT = "go_port";
	public FileTransferService(String name) {
		super(name);
	}

	public FileTransferService() {
		super("FileTransferService");
		mHandler = new Handler();
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		Context context = getApplicationContext();
		if (intent.getAction().equals(ACTION_SEND_FILE)) {
			String host = intent.getExtras().getString(EXTRAS_ADDRESS);
			Socket socket = new Socket();
			int port = intent.getExtras().getInt(EXTRAS_PORT);
			String url_contact = intent.getExtras().getString(EXTRAS_URI_CONTACT);
			if (url_contact != null)
			{
				try {
					socket.bind(null);
					socket.connect((new InetSocketAddress(host, port)), SOCKET_TIMEOUT);
					Log.d(SysnContacts.TAG, "Client socket - " + socket.isConnected());
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
					BufferedWriter writer = new BufferedWriter(outputStreamWriter);
					writer.write(url_contact);
					writer.close();
					outputStreamWriter.close();
					mHandler.post(new DisplayToast(this,"Đã chuyễn thành công . Kiểm tra lại thiết bị nhận"));
				} catch (IOException e) {
					Log.e(SysnContacts.TAG, e.getMessage());
				}catch (IllegalArgumentException e)
				{
					mHandler.post(new DisplayToast(this, "Hãy kết nối đến thiết bị"));
				}finally {
					if (socket != null) {
						if (socket.isConnected()) {
							try {
								socket.close();
							} catch (IOException e) {
								// Give up
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	public class DisplayToast implements Runnable {
		private final Context mContext;
		String mText;

		public DisplayToast(Context mContext, String text){
			this.mContext = mContext;
			mText = text;
		}

		public void run(){
			Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
		}
	}
}
