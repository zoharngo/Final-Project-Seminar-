package com.smartphoneWebcam.ui.flowActivities;

import com.smartphoneWebcam.ui.AppUtils;
import com.smartphoneWebcam.ui.flowActivities.adpter.Transmitter;
import com.smartphoneWebcam.ui.flowActivities.adpter.TransmitterDataModel;

import com.smartphoneWebcam.R;
import com.smartphoneWebcam.R.drawable;
import com.smartphoneWebcam.R.id;
import com.smartphoneWebcam.R.layout;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class NewTransmitterActivity extends Activity implements OnClickListener {
	private static final String ERROR_MESSAGE_TITLE = "Problem Occur!";
	private static final String TEST_FAILED = "Test Failed!";
	private static final String TEST_SUCCSEED = "Test Succeed!";
	private static final String TARNSMITTER_CREATED = "Transmitter created!";
	private static final String INVALID_IP_ERROR_MESSAGE = "Invalid IP address !,\nPlease verify the address.";
	private static final String CONNCETION_ERROR_MESSAGE = "Connection To Host Faild !,\nCheck Network Connectivity";
	private static final String CONNCETION_SUCCSEED_MESSAGE = "Connection To Host Succseed ,YOU READY TO GO!...";
	private static final String TARNSMITTER_CREATED_MESSAGE = "New Transmitter created Successfully!";

	private EditText ip = null;
	private EditText nickName = null;
	private TransmitterDataModel dataModel;

	private ProgressDialog progressDialog;

	private boolean connStatus = false;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_transmitter);
		dataModel = TransmitterDataModel.getInstance(this);
		context = this;
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/BEBASNEUE.OTF");
		TextView titleTextView = (TextView) findViewById(R.id.title_new_trans);
		TextView ipTextView = (TextView) findViewById(R.id.title_new_ip);
		TextView nickTextView = (TextView) findViewById(R.id.title_new_nick_name);

		titleTextView.setTypeface(face);
		ipTextView.setTypeface(face);
		nickTextView.setTypeface(face);

		ImageButton testBtn = (ImageButton) findViewById(R.id.testConnBtn);
		ImageButton subBtn = (ImageButton) findViewById(R.id.saveBtn);
		ImageButton canBtn = (ImageButton) findViewById(R.id.cancelBtn_1);
		ip = (EditText) findViewById(R.id.ip_address);
		nickName = (EditText) findViewById(R.id.nick_name);

		ip.setTypeface(face);
		nickName.setTypeface(face);
		testBtn.setOnClickListener(this);
		subBtn.setOnClickListener(this);
		canBtn.setOnClickListener(this);

	}

	private boolean saveNewTransmitter() throws SQLiteException {

		try {
			Transmitter transmitter = new Transmitter();
			transmitter.setIp(ip.getText().toString());
			transmitter.setNickName(nickName.getText().toString());
			dataModel.addTransmitter(transmitter);
			return true;
		} catch (SQLiteException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public void onClick(final View v) {
		final String ipStr = ip.getText().toString();
		AsyncTask<Void, Void, Void> asyncTask = null;
		switch (v.getId()) {
		case R.id.cancelBtn_1:
			((ImageButton) v).setImageResource(R.drawable.cancel_press);
			finish();
			break;
		case R.id.saveBtn:

			asyncTask = new AsyncTask<Void, Void, Void>() {

				private boolean isSaved = false;
				private boolean isValidIp = false;

				@Override
				protected Void doInBackground(Void... params) {
					if (isValidIp = AppUtils.validateIp(ipStr)) {
						try {
							isSaved = saveNewTransmitter();
						} catch (SQLiteException e) {
							java.lang.System.exit(1);
						}
					}
					return null;
				}

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					((ImageButton) v).setImageResource(R.drawable.save_press);
				}

				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					if (isValidIp) {
						if (isSaved) {
							AppUtils.showAlertBox(context, TARNSMITTER_CREATED,
									TARNSMITTER_CREATED_MESSAGE, true);
						}
					} else {
						AppUtils.showAlertBox(context, ERROR_MESSAGE_TITLE,
								INVALID_IP_ERROR_MESSAGE, false);
					}
					((ImageButton) v).setImageResource(R.drawable.save);
				}

			};
			asyncTask.execute();
			break;
		case R.id.testConnBtn:

			asyncTask = new AsyncTask<Void, Void, Void>() {

				private boolean isServerReady = false;
				private boolean isValidIp = false;

				@Override
				protected Void doInBackground(Void... params) {

					if (isValidIp = AppUtils.validateIp(ipStr)) {
						isServerReady = AppUtils.checkIfServerReady(ipStr);
					}
					return null;
				}

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					((ImageButton) v)
							.setImageResource(R.drawable.test_connection_press);
					progressDialog = new ProgressDialog(context);
					progressDialog.setCancelable(false);
					progressDialog
							.setMessage("Verifing connection please wait ...");
					progressDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.show();
				}

				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					if (isValidIp) {
						if (isServerReady) {
							AppUtils.showAlertBox(context, TEST_SUCCSEED,
									CONNCETION_SUCCSEED_MESSAGE, false);
						} else {
							AppUtils.showAlertBox(context, TEST_FAILED,
									CONNCETION_ERROR_MESSAGE, false);
						}
					} else {
						AppUtils.showAlertBox(context, ERROR_MESSAGE_TITLE,
								INVALID_IP_ERROR_MESSAGE, false);
					}
					progressDialog.dismiss();
					((ImageButton) v)
							.setImageResource(R.drawable.test_connection);
				}

			};
			asyncTask.execute();

			/*
			 * if (AppUtils.validateIp(ipStr)) { progressDialog = new
			 * ProgressDialog(this); progressDialog.setCancelable(false);
			 * progressDialog
			 * .setMessage("Verifing connection please wait ...");
			 * progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			 * progressDialog.show();
			 * 
			 * Thread background = new Thread(new Runnable() {
			 * 
			 * @Override public void run() { connStatus =
			 * AppUtils.checkIfServerReady(ipStr);
			 * progressHandler.sendMessage(progressHandler .obtainMessage()); }
			 * }); background.start(); } else { AppUtils.showAlertBox(this,
			 * TEST_FAILED, INVALID_IP_ERROR_MESSAGE, false); }
			 */
			break;
		}

	}

	Handler progressHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			progressDialog.dismiss();
			if (connStatus) {
				AppUtils.showAlertBox(context, TEST_SUCCSEED,
						CONNCETION_SUCCSEED_MESSAGE, false);
			} else {
				AppUtils.showAlertBox(context, TEST_FAILED,
						CONNCETION_ERROR_MESSAGE, false);
			}
		};
	};

}