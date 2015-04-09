package com.example.mainactivity;

import java.lang.reflect.Method;

import com.example.mainactivity.MainActivity.emNotifyType;
import com.example.test2.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.internal.telephony.ITelephony;


public class MainActivity extends Activity
{
	public enum emNotifyType
	{
		emtype_Sleep,
		emtype_outHome
	}
	
    public MyPhoeStatusChange oMyPhoeStatus = null;
	private  emNotifyType m_CurNotifyType= emNotifyType.emtype_Sleep;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);        
        setContentView(R.layout.main_linearlayout);
        TelephonyManager oManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        oMyPhoeStatus = new MyPhoeStatusChange(this, m_CurNotifyType);
        oManager.listen(oMyPhoeStatus, PhoneStateListener.LISTEN_CALL_STATE);
          
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		MenuInflater oInflater = new MenuInflater(this);
		oInflater.inflate(R.menu.notifytypemenu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {  
        switch (item.getItemId()) { 
        case R.id.IamSleeping:
        	m_CurNotifyType = emNotifyType.emtype_Sleep;
        case R.id.IamNotHome:
        	m_CurNotifyType = emNotifyType.emtype_outHome;
     
        }
        oMyPhoeStatus.SetCurNotifyType(m_CurNotifyType);
        return true;  
  
    }
	
}



class MyPhoeStatusChange extends PhoneStateListener{
	
	public Context context;
	public emNotifyType myemNotifyType;
	public MyPhoeStatusChange(Context context, emNotifyType memNotifyType)
	{
		this.context = context;
		this.myemNotifyType = memNotifyType;
	}
	
	public void SetCurNotifyType(emNotifyType memNotifyType)
	{
		myemNotifyType = memNotifyType;
	}
	
	private static ITelephony getITelephony(Context context) {
	    TelephonyManager mTelephonyManager = (TelephonyManager)context
	            .getSystemService(context.TELEPHONY_SERVICE);
	    Class<TelephonyManager> c = TelephonyManager.class;
	    Method getITelephonyMethod = null;
		ITelephony iTelephony = null;
	    try {
	        getITelephonyMethod = c.getDeclaredMethod("getITelephony",
	                (Class[]) null); // 获取声明的方法
	        getITelephonyMethod.setAccessible(true);
	    } catch (SecurityException e) {
	        e.printStackTrace();
	    } catch (NoSuchMethodException e) {
	        e.printStackTrace();
	    }

	    try {
	         iTelephony = (ITelephony) getITelephonyMethod.invoke(
	                mTelephonyManager, (Object[]) null); // 获取实例
	        return iTelephony;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return iTelephony;
	}

    public void onCallStateChanged(int state, String incomingNumber) {
    	super.onCallStateChanged(state, incomingNumber);
        switch (state) {
        case TelephonyManager.CALL_STATE_IDLE:
            break;
        case TelephonyManager.CALL_STATE_RINGING:
        	try {
				getITelephony(context).endCall();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	SmsManager smsManager = SmsManager.getDefault();

        	if (myemNotifyType == emNotifyType.emtype_Sleep) {
                
            	smsManager.sendTextMessage(incomingNumber, null, "主人在睡觉哦，一会就睡醒了【来自火星的智回】", null, null);
			} else {
	            
	        	smsManager.sendTextMessage(incomingNumber, null, "主人不在家哦，我自己在家呢【来自火星的智回】", null, null);
			}

            break;
        case TelephonyManager.CALL_STATE_OFFHOOK:
        default:
            break;
        }
    }
    
}


