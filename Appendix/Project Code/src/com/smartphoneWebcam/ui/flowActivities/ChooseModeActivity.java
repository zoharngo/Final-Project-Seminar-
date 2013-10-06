package com.smartphoneWebcam.ui.flowActivities;


import com.smartphoneWebcam.R;
import com.smartphoneWebcam.R.drawable;
import com.smartphoneWebcam.R.id;
import com.smartphoneWebcam.R.layout;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ChooseModeActivity extends Activity implements OnClickListener {

	private ImageButton transBtn;
	private ImageButton recBtn;
	private ProgressDialog progressDialog;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_mode);
		context = this;
		transBtn = (ImageButton) findViewById(R.id.transBtn);
		recBtn = (ImageButton) findViewById(R.id.recBtn);
		transBtn.setOnClickListener(this);
		recBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.transBtn:
			transBtn.setImageResource(R.drawable.transmit_btn_press);

			AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					progressDialog = new ProgressDialog(context);
					progressDialog.setCancelable(true);
					progressDialog.setMessage("Please While Changing mode ...");
					progressDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.show();

				}

				@Override
				protected void onProgressUpdate(Void... values) {
					super.onProgressUpdate(values);

				}

				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					Intent intent = new Intent(context, MainTransmitterActivity.class);
					startActivity(intent);
					progressDialog.dismiss();
				}

			};

			asyncTask.execute();

			break;
		case R.id.recBtn:
			recBtn.setImageResource(R.drawable.receiver_btn_press);
			intent = new Intent(this, ReceiverMenu.class);
			startActivity(intent);
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		transBtn.setImageResource(R.drawable.transmit_btn);
		recBtn.setImageResource(R.drawable.receiver_btn);

	}

}
