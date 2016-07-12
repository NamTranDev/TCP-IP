package vn.com.fptshop.fmusic.Synchronous_Contacts;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class Utils {

	private final static String p2pInt = "p2p-p2p0";

	static SharedPreferences prefs;
	/**
	 * set String Preference Value
	 *
	 * @param context
	 * @param prefName Preference name
	 * @param Value Preference value
	 */
	public static void setStringPrefrences(Context context, String prefName, String Value, String PREFS_FILE_NAME) {
		prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(prefName, Value);
		editor.commit();
	}
	/**
	 * get String Preference Value
	 *
	 * @param context
	 * @param prefName
	 * @return
	 */
	public static String getStringPrefrences(Context context, String prefName, String PREFS_FILE_NAME) {
		prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
		if (prefs.contains(prefName))
			return prefs.getString(prefName, null);
		else
			return "";
	}

	/**
	 * remove all the preferences of your app. Note: only remove all which set
	 * by using this sdk.
	 *
	 * @param context
	 */
	public static void removeAllPrefrences(Context context, String PREFS_FILE_NAME) {
		prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}

	public static String getIPFromMac(String MAC) {
		/*
		 * method modified from:
		 * 
		 * http://www.flattermann.net/2011/02/android-howto-find-the-hardware-mac-address-of-a-remote-host/
		 * 
		 * */
		String mac = MAC.substring(0,11);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("/proc/net/arp"));
			String line;
			while ((line = br.readLine()) != null) {
				Log.d("123", line);
				String[] splitted = line.split(" +");
				if ( splitted.length >= 4 )
				{
					if (splitted[3] != null)
					{
						String mac1 = splitted[3];
						Log.d("12345", mac1);
						if (mac1.length() > 11)
						{
							String mac2 = mac1.substring(0,11);
							Log.d("435435", mac2);
							if (mac.equals(mac2)) {
								// Basic sanity check
								return splitted[0];
							}

						}
				}
					/*String device = splitted[5];
					if (device.matches(".*" +p2pInt)){
						String mac = splitted[3];
						if (mac.matches(MAC)) {
							return splitted[0];
						}
					}*/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * NetworkInfo is class describes the status of a network interface.
	 * Use getActiveNetworkInfo() to get an instance that represents the current network connection.
	 * @param context
	 * @return
	 */
	public static NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager.getActiveNetworkInfo();
	}

	/**
	 * Check if there is any connectivity to a Mobile network
	 * @param context
	 * @return
	 */
	public static boolean isConnectWify(Context context)
	{
		NetworkInfo networkInfo = Utils.getNetworkInfo(context);
		return (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
	}




	@SuppressLint("LongLogTag")
	public static String getLocalIPAddress() {
		/*
		 * modified from:
		 * 
		 * http://thinkandroid.wordpress.com/2010/03/27/incorporating-socket-programming-into-your-applications/
		 * 
		 * */
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();

					String iface = intf.getName();
					if(iface.matches(".*" +p2pInt+ ".*")){
						if (inetAddress instanceof Inet4Address) { // fix for Galaxy Nexus. IPv4 is easy to use :-)
							return getDottedDecimalIP(inetAddress.getAddress());
						}
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("AndroidNetworkAddressFactory", "getLocalIPAddress()", ex);
		} catch (NullPointerException ex) {
			Log.e("AndroidNetworkAddressFactory", "getLocalIPAddress()", ex);
		}
		return null;
	}

	private static String getDottedDecimalIP(byte[] ipAddr) {
		/*
		 * ripped from:
		 * 
		 * http://stackoverflow.com/questions/10053385/how-to-get-each-devices-ip-address-in-wifi-direct-scenario
		 * 
		 * */
		String ipAddrStr = "";
		for (int i=0; i<ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i]&0xFF;
		}
		return ipAddrStr;
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @author paulburke
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	public static void synchronous (Context context)
	{
		Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				null, null, null);
		phones.moveToFirst();
		String lookupKey = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
		Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
		AssetFileDescriptor fd;
		try {
			fd = context.getContentResolver().openAssetFileDescriptor(uri, "r");
			FileInputStream fis = fd.createInputStream();
			byte[] buf = new byte[(int) fd.getDeclaredLength()];
			fis.read(buf);
			String vCard = new String(buf);
			String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "abc";
			FileOutputStream mFileOutputStream = new FileOutputStream(path, false);
			mFileOutputStream.write(vCard.toString().getBytes());
			Log.d("Vcard", vCard);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Convert String[] to Arraylist<String>
	 * @param a
	 * @return
	 */
	public static ArrayList<String> convertArrayStringToArrayList (String[] a)
	{
		ArrayList<String> arrayString = new ArrayList<String>(Arrays.asList(a));
		return arrayString;
	}

	public static void deletePersistentGroups(WifiP2pManager wifiP2pManager,WifiP2pManager.Channel mChannel){
		try {
			Method[] methods = WifiP2pManager.class.getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equals("deletePersistentGroup")) {
			// Delete any persistent group
					for (int netid = 0; netid < 32; netid++) {
						methods[i].invoke(wifiP2pManager, mChannel, netid, null);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
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
			else
			if (extensionsNameFilter.accept(fileChild,fileChild.getName()))
			{
				files.add(fileChild);
			}
		}
		return files;
	}
}
