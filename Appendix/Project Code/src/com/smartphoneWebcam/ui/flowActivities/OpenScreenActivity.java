package com.smartphoneWebcam.ui.flowActivities;

import com.smartphoneWebcam.R;
import com.smartphoneWebcam.R.anim;
import com.smartphoneWebcam.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class OpenScreenActivity extends Activity {
	 private static final int SPLASH_TIME = 3 * 1000;// 3 seconds
	 
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.open_screen_activity);
	        try {
	        new Handler().postDelayed(new Runnable() {
	 
	            public void run() {
	                 
	                Intent intent = new Intent(OpenScreenActivity.this,
	                    ChooseModeActivity.class);
	                startActivity(intent);
	 
	                OpenScreenActivity.this.finish();
	 
	                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	 
	            }
	             
	             
	        }, SPLASH_TIME);
	         
	        new Handler().postDelayed(new Runnable() {
	              public void run() {
	                     } 
	                }, SPLASH_TIME);
	        } catch(Exception e){}
	    }
}
