package com.smartphoneWebcam.ui.flowActivities.adpter;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smartphoneWebcam.R;
import com.smartphoneWebcam.ui.AppUtils;
import com.smartphoneWebcam.ui.flowActivities.RecevierWebView;

public class ListViewBaseAdapter extends BaseAdapter implements
		OnCheckedChangeListener, OnClickListener {
	public static final String IP_EXTRA = "teaonly.droideye.ListViewBaseAdapter.IP_EXTRA";

	private static final String CONNCETION_ERROR_MESSAGE_1 = "Connection To Transmitter Faild !.\nPlease Check host Connectivity.";

	protected static final String CONNCETION_FAILD = "Connection Faild!";

	private LayoutInflater l_Inflater;
	private ArrayList<Transmitter> checksTransmitters;
	private ArrayList<Integer> itemPosChecked;
	private Context context;
	private Typeface face;
	private boolean connStatus = false;
	private ProgressDialog progressDialog;

	private String ipStr;

	private ImageButton imageButton = null;

	public ListViewBaseAdapter(Context context) {
		this.context = context;
		l_Inflater = LayoutInflater.from(context);

		itemPosChecked = new ArrayList<Integer>();
		checksTransmitters = new ArrayList<Transmitter>();
		face = Typeface.createFromAsset(context.getAssets(),
				"fonts/BEBASNEUE.OTF");
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please While Trying to Connect ...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

	}

	@Override
	public int getCount() {
		return TransmitterDataModel.getInstance(context).getSize();
	}

	@Override
	public Object getItem(int position) {
		return TransmitterDataModel.getInstance(context).getItem(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.gc();
		TransmitterHolder holder = null;

		if (convertView == null) {
			convertView = this.l_Inflater.inflate(R.layout.list_item, null);
			holder = new TransmitterHolder();
			holder.details = (TextView) convertView
					.findViewById(R.id.tras_text_view);
		//	holder.details.setTypeface(face);
			holder.connectBtn = (ImageButton) convertView
					.findViewById(R.id.ConnectBtn);
			holder.removeCheckBtn = (CheckBox) convertView
					.findViewById(R.id.removeBtn);

			holder.connectBtn.setOnClickListener(this);
			holder.removeCheckBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Integer pos = (Integer) v.getTag();
					CheckBox checkBox = (CheckBox) v;
					if (checkBox.isChecked()) {
						checksTransmitters.add((Transmitter) getItem(pos));
						itemPosChecked.add(pos);
					} else {
						checksTransmitters.remove((Transmitter) getItem(pos));
						itemPosChecked.remove(pos);
					}

				}
			});
			convertView.setTag(holder);
		} else {
			holder = (TransmitterHolder) convertView.getTag();
		}
		if (itemPosChecked.contains(position)) {
			holder.removeCheckBtn.setChecked(true);
			System.out.println(true);
		} else {
			holder.removeCheckBtn.setChecked(false);
			System.out.println(false);
		}
		holder.removeCheckBtn.setTag(position);
		holder.connectBtn.setTag(position);
		holder.connectBtn.setImageResource(R.drawable.connect_button_3);
		StringBuilder sb = new StringBuilder(
				((Transmitter) getItem(position)).getNickName());
		sb.append(" - ").append(((Transmitter) getItem(position)).getIp());
		holder.details.setText(sb.toString());
		return convertView;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Integer pos = (Integer) buttonView.getTag();
		if (isChecked) {
			checksTransmitters.add((Transmitter) getItem(pos));
			itemPosChecked.add(pos);
		} else {
			checksTransmitters.remove((Transmitter) getItem(pos));
			itemPosChecked.remove(pos);
		}
	}

	public void checkAllTransmitters(boolean check) {
		if (check) {
			for (int pos = 0; pos < getCount(); pos++) {
				checksTransmitters.add((Transmitter) getItem(pos));
				itemPosChecked.add(pos);
			}
		} else {
			clearCheckTrasmitters();
		}
		notifyDataSetChanged();
	}

	static class TransmitterHolder {
		TextView details;
		CheckBox removeCheckBtn;
		ImageButton connectBtn;
	}

	public ArrayList<Transmitter> getChecksTransmitters() {
		return checksTransmitters;
	}

	public void clearCheckTrasmitters() {
		checksTransmitters.clear();
		itemPosChecked.clear();
	}

	@Override
	public void onClick(View v) {
		imageButton = (ImageButton) v;
		imageButton.setImageResource(com.smartphoneWebcam.R.drawable.connect_button_5);
		// notifyDataSetChanged();
		progressDialog.show();
		while (!progressDialog.isShowing()) {

		}

		Integer pos = (Integer) v.getTag();
		Transmitter transmitter = (Transmitter) getItem(pos);

		ipStr = transmitter.getIp();

		Thread background = new Thread(new Runnable() {

			@Override
			public void run() {
				connStatus = AppUtils.checkIfServerReady(ipStr);
				progressHandler.sendMessage(progressHandler.obtainMessage());
			}
		});
		background.start();

	}

	Handler progressHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			progressDialog.dismiss();
			if (connStatus) {
				Intent intent = new Intent(context, RecevierWebView.class);
				intent.putExtra(IP_EXTRA, ipStr);
				context.startActivity(intent);
			} else {
				AppUtils.showAlertBox(context, CONNCETION_FAILD,
						CONNCETION_ERROR_MESSAGE_1, false);
				if (imageButton != null) {
					imageButton.setImageResource(com.smartphoneWebcam.R.drawable.connect_button_4);
				}
			}
		};
	};

}
