/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vn.com.fptshop.fmusic.Synchronous_Contacts;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.internal.widget.DialogTitle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import vn.com.fptshop.fmusic.FirstActivity;
import vn.com.fptshop.fmusic.MainActivity;
import vn.com.fptshop.fmusic.R;

/**
 * A fragment that manages a particular peer and allows interaction with device
 * i.e. setting up network connection and transferring data.
 */
public class DeviceDetailFragment extends Fragment implements ConnectionInfoListener {

	public static File filevcf;
	public static final String IP_SERVER = "192.168.49.1";
	public static int PORT = 8988;
	private static boolean server_running = false;

	protected static final int CHOOSE_FILE_RESULT_CODE = 20;
	private View mContentView = null;
	private WifiP2pDevice device;
	private WifiP2pInfo info;
	public static ProgressDialog progressDialog = null;
	Gson gson = new Gson();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mContentView = inflater.inflate(R.layout.device_detail, null);
		mContentView.findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				WifiP2pConfig config = new WifiP2pConfig();
				config.deviceAddress = device.deviceAddress;
				config.wps.setup = WpsInfo.PBC;
				progressDialog = ProgressDialog.show(getActivity(), "Nhấn trở lại để hủy",
						"Đang kết nối đến :" + device.deviceName, true, true
				);
				((DeviceListFragment.DeviceActionListener) getActivity()).connect(config);

			}
		});

		mContentView.findViewById(R.id.btn_disconnect).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						((DeviceListFragment.DeviceActionListener) getActivity()).disconnect();
					}
				});

		mContentView.findViewById(R.id.btn_sync).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (null != device) {
							List<ContactManager.PhoneContact> phoneContacts = MainActivity.phoneContacts;
							if (phoneContacts.size() > 0) {
								Toast.makeText(getActivity(), "Đang chuyễn " + phoneContacts.size() + " danh ba", Toast.LENGTH_LONG);
								ArrayList<String> strings = new ArrayList<String>();
								for (ContactManager.PhoneContact phoneContact : phoneContacts) {
									String json = gson.toJson(phoneContact);
									Log.d("abcdef", json);
									strings.add(json);
								}

								Log.d("1234", String.valueOf(strings.size()));


								String contact = "";
								for (int j = 0; j < strings.size(); j++) {
									contact += strings.get(j) + "abcdefghklmnoprst123456789";
								}
								Log.d("abc", contact);
										/*String client_mac_fixed = new String(device.deviceAddress).replace("99", "19");
										String clientIP = Utils.getIPFromMac(client_mac_fixed);
										Intent serviceIntent = new Intent(getActivity(), FileTransferService.class);
										serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
										serviceIntent.putExtra(FileTransferService.EXTRAS_URI_CONTACT, contact);
										serviceIntent.putExtra(FileTransferService.EXTRAS_ADDRESS, clientIP);
										serviceIntent.putExtra(FileTransferService.EXTRAS_PORT, PORT);
										getActivity().startService(serviceIntent);*/
							} else {
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getActivity(), "Máy không có danh bạ hoạc chưa được quét danh bạ", Toast.LENGTH_LONG).show();
									}
								});
							}
						} else {
							Toast.makeText(getActivity(), "Kiểm tra lại kết nối hoặc chọn lại thiết bị", Toast.LENGTH_LONG).show();
						}
					}
				});



		return mContentView;
	}

	@Override
	public void onConnectionInfoAvailable(final WifiP2pInfo info) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		this.info = info;
		this.getView().setVisibility(View.VISIBLE);

		// The owner IP is now known.
		TextView view = (TextView) mContentView.findViewById(R.id.group_owner);
		view.setText(getResources().getString(R.string.group_owner_text)
				+ ((info.isGroupOwner == true) ? getResources().getString(R.string.yes)
				: getResources().getString(R.string.no)));

		// InetAddress from WifiP2pInfo struct.
		view = (TextView) mContentView.findViewById(R.id.device_info);
		view.setText("Group Owner IP - " + info.groupOwnerAddress.getHostAddress());

		mContentView.findViewById(R.id.btn_sync).setVisibility(View.VISIBLE);

		if (!server_running){
			new ServerAsyncTask(getActivity()).execute();
			server_running = true;
		}

		}

	/**
	 * Updates the UI with device data
	 * 
	 * @param device the device to be displayed
	 */
	public void showDetails(WifiP2pDevice device) {
		this.device = device;
		this.getView().setVisibility(View.VISIBLE);
		TextView view = (TextView) mContentView.findViewById(R.id.device_address);
		view.setText(device.deviceAddress);
		view = (TextView) mContentView.findViewById(R.id.device_info);
		view.setText(device.toString());

	}

	/**
	 * Clears the UI fields after a disconnect or direct mode disable operation.
	 */
	public void resetViews() {
		mContentView.findViewById(R.id.btn_connect).setVisibility(View.VISIBLE);
		TextView view = (TextView) mContentView.findViewById(R.id.device_address);
		view.setText(R.string.empty);
		view = (TextView) mContentView.findViewById(R.id.device_info);
		view.setText(R.string.empty);
		view = (TextView) mContentView.findViewById(R.id.group_owner);
		view.setText(R.string.empty);
		this.getView().setVisibility(View.GONE);
	}

	/**
	 * A simple server socket that accepts connection and writes some data on
	 * the stream.
	 */
	public static class ServerAsyncTask extends AsyncTask<Void, Void, String> {

		private final Context context;

		/**
		 * @param context
		 * @param
		 */
		public ServerAsyncTask(Context context) {
			this.context = context;
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Void... params) {
			String result = "";
			try {
				ServerSocket serverSocket = new ServerSocket(PORT);
				Log.d(SysnContacts.TAG, "Server: Socket opened");
				Socket client = serverSocket.accept();
				Log.d(SysnContacts.TAG, "Server: connection done");
				InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
				BufferedReader reader = new BufferedReader(inputStreamReader);
				result = reader.readLine();
				reader.close();
				inputStreamReader.close();
				serverSocket.close();
				server_running = false;
				return result;
			} catch (IOException e) {
				Log.e(SysnContacts.TAG, e.getMessage());
				return null;
			}
		}

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(final String result) {
			if (result != null)
			{
				ImportContacts importContacts = new ImportContacts(context);
				importContacts.execute(result);
			}
			else
			{
				Toast.makeText(context,"Fail",Toast.LENGTH_LONG).show();
			}
		}
	}

	private static class ImportContacts extends AsyncTask<String,String,Void>
	{
		Context mcontext;
		ProgressDialog progressBar;
		int contact ;
		public ImportContacts(Context context) {
			this.mcontext = context;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			contact = 0;
			progressBar = new ProgressDialog(mcontext);
			progressBar.setTitle("Đồng bộ danh bạ");
			progressBar.setMessage("Đang đồng bộ . Vui lòng dợi trong giây lát");
			progressBar.setCancelable(false);
			progressBar.show();
		}


		@Override
		protected Void doInBackground(String... strings) {
			Gson gson = new Gson();
			List<ContactManager.PhoneContact> phoneContacts = new ArrayList<>();
			Log.d("abcd", strings[0]);
			String a = strings[0];
				/*Toast.makeText(context,"OK " + result,Toast.LENGTH_LONG).show();*/
			String[] results = a.split("abcdefghklmnoprst123456789");
			//Toast.makeText(context,"Size" + results.length,Toast.LENGTH_LONG);
			for (int i=0;i<results.length;i++)
			{
				Log.d("abcde",results[i]);
			}
			Log.d("12345", String.valueOf(results.length));
			ArrayList<String> abc = Utils.convertArrayStringToArrayList(results);
			Log.d("abcdef", String.valueOf(abc.size()));

			for (int j = 0;j<abc.size();j++)
			{
				ContactManager.PhoneContact phoneContact = gson.fromJson(abc.get(j),ContactManager.PhoneContact.class);
				phoneContacts.add(phoneContact);
			}
			ContactManager contactManager = new ContactManager(mcontext);
			if (phoneContacts.size()>0)
			{
				for (int i = 0;i<phoneContacts.size();i++)
				{
					publishProgress("" + i +"/" + phoneContacts.size());
					contactManager.addContacts(phoneContacts.get(i));
				}

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			progressBar.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			progressBar.dismiss();
			Toast.makeText(mcontext,"Đồng bộ thành công !!!",Toast.LENGTH_LONG);
		}
	}



}
