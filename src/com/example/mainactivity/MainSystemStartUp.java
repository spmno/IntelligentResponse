package com.example.mainactivity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;

import com.example.mainactivity.MainActivity;
import com.example.test2.R;


public class MainSystemStartUp extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);        
        setContentView(R.layout.main_systemstartup);
        
    	Timer timerAutoToNextActivity = new Timer();
        
    	TimerTask tastAutoToNextActivity = new TimerTask() {
    		
    		@Override
    		public void run() {
    			// TODO Auto-generated method stub
    			Intent	oIntent = new Intent();
    			oIntent.setClass(MainSystemStartUp.this, MainActivity.class);
    			startActivity(oIntent);
    			finish();
    		}
    	};
    	
        timerAutoToNextActivity.schedule(tastAutoToNextActivity, 3000);
	}
	
}