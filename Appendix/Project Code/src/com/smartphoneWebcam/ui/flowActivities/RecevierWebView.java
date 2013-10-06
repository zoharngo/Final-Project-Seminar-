package com.smartphoneWebcam.ui.flowActivities;

import com.smartphoneWebcam.R;
import com.smartphoneWebcam.ui.flowActivities.adpter.ListViewBaseAdapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

public class RecevierWebView extends Activity {

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.smartphoneWebcam.R.layout.receiver_web_view);

		WebView webView = (WebView) findViewById(com.smartphoneWebcam.R.id.web_view);
		webView.setInitialScale(1);
		WebSettings settings = webView.getSettings();
		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);
		settings.setJavaScriptEnabled(true);
		settings.setPluginState(PluginState.ON);
		webView.clearCache(true);

		settings.setAllowFileAccess(true);
		webView.loadUrl("http://"
				+ getIntent().getStringExtra(ListViewBaseAdapter.IP_EXTRA)
				+ ":8080/");
		/*
		 * Toast.makeText(this,
		 * this.getIntent().getStringExtra(ListViewBaseAdapter.IP_EXTRA),
		 * Toast.LENGTH_LONG).show();
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.web_view_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return super.onOptionsItemSelected(item);
	}
}
