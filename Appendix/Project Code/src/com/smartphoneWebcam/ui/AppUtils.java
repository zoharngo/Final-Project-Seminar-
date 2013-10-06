package com.smartphoneWebcam.ui;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

public class AppUtils {
	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static void showAlertBox(Context context, String title,
			String message, boolean finish) {
		final Activity parentActivity = ((Activity) context);
		final boolean finishAfter = finish;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder.setMessage(message)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (finishAfter) {
							parentActivity.finish();
						}
					}
				}).create().show();
	}

	public static boolean validateIp(String ip) {
		Pattern ipVal = Pattern.compile(IPADDRESS_PATTERN);
		return ipVal.matcher(ip).matches();
	}

	public static boolean checkIfServerReady(String ip) {

		HttpURLConnection connURL = null;

		boolean isOK = false;
		try {

			URL url = new URL("http://" + ip + ":8080/");
			connURL = (HttpURLConnection) url.openConnection();
			connURL.connect();
			isOK = connURL.getResponseCode() == 200;
			if (!isOK) {
				return false;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;

		} finally {
			if (connURL != null) {
				connURL.disconnect();
			}
		}
		return true;
	}

}
