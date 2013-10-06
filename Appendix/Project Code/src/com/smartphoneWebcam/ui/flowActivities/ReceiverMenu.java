	package com.smartphoneWebcam.ui.flowActivities;

	import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import com.smartphoneWebcam.ui.flowActivities.adpter.ListViewBaseAdapter;
import com.smartphoneWebcam.ui.flowActivities.adpter.Transmitter;
import com.smartphoneWebcam.ui.flowActivities.adpter.TransmitterDataModel;

import com.smartphoneWebcam.R;
import com.smartphoneWebcam.R.drawable;
import com.smartphoneWebcam.R.id;
import com.smartphoneWebcam.R.layout;

	public class ReceiverMenu extends Activity implements OnClickListener,
			OnCheckedChangeListener {
		private ListViewBaseAdapter baseAdapter = null;
		private ListView transmitterList = null;
		private CheckBox checkAllBox = null;
		private ImageButton submitButton = null;
		protected Context context;

		@Override
		protected void onCreate(Bundle savedInstanceState) {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.menu);
			context = this;
			Typeface face = Typeface.createFromAsset(getAssets(),
					"fonts/BEBASNEUE.OTF");
			// TextView selectTextView = (TextView) findViewById(R.id.select);
			TextView infoTextView = (TextView) findViewById(R.id.info);
			checkAllBox = (CheckBox) findViewById(R.id.check_all);
			TransmitterDataModel.getInstance(this);
			// selectTextView.setTypeface(face);
			checkAllBox.setTypeface(face);
			infoTextView.setTypeface(face);
			transmitterList = (ListView) findViewById(R.id.translist);
			baseAdapter = new ListViewBaseAdapter(this);
			transmitterList.setAdapter(baseAdapter);
			ImageButton cancelButton = (ImageButton) findViewById(R.id.cancelBtn_0);
			ImageButton removeButton = (ImageButton) findViewById(R.id.removeBtn);
			submitButton = (ImageButton) findViewById(R.id.submitBtn);
			cancelButton.setOnClickListener(this);
			removeButton.setOnClickListener(this);
			submitButton.setOnClickListener(this);
			checkAllBox.setOnCheckedChangeListener(this);
		}

		@Override
		public void onClick(final View v) {
			Intent intent = null;
			switch (v.getId()) {
			case R.id.cancelBtn_0:
				((ImageButton) v).setImageResource(R.drawable.cancel_press);
				finish();
				break;
			case R.id.submitBtn:
				((ImageButton) v).setImageResource(R.drawable.submit_press);
				intent = new Intent(getApplicationContext(),
						NewTransmitterActivity.class);
				startActivity(intent);
				break;
			case R.id.removeBtn:
				AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

					private boolean deleted = false;

					@Override
					protected Void doInBackground(Void... params) {
						ArrayList<Transmitter> transmitters = baseAdapter
								.getChecksTransmitters();

						if (deleted = transmitters.size() > 0) {
							TransmitterDataModel.getInstance(context)
									.removeTransmitter(transmitters);
							baseAdapter.clearCheckTrasmitters();

						}
						return null;
					}

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						((ImageButton) v)
								.setImageResource(R.drawable.remove_selected_press);
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						if (deleted) {
							baseAdapter.notifyDataSetInvalidated();
							Toast.makeText(context,
									"Transmitters Removed Successfully!",
									Toast.LENGTH_SHORT).show();
						}
						((ImageButton) v)
								.setImageResource(R.drawable.remove_selected);
					}
				};
				asyncTask.execute();
				break;
			}
		}

		@Override
		protected void onResume() {
			super.onResume();
			submitButton.setImageResource(R.drawable.submit);
			checkAllBox.setChecked(false);
			baseAdapter.checkAllTransmitters(false);
			baseAdapter.notifyDataSetChanged();

		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			baseAdapter.checkAllTransmitters(isChecked);
		}

	}
