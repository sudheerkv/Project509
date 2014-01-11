package org.appanalysis;

import java.io.*;
import java.net.*;
import android.location.*;
import dalvik.system.ReferenceMonitor;
import android.telephony.*;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import dalvik.system.Taint; 

public class TaintDroidNotifyController extends Activity {
    private static final String TAG = TaintDroidNotifyController.class.getSimpleName();
    
    private static Button startButton;
    private static Button stopButton;
    private static Button GPSButton;
    private static Button IMEIButton;

    public Activity a = null;
	public String stemp;
    public LocationManager mloc = null;
    public LocationListener locationListener;
    public double latitude, longitude;

    private OnClickListener onClickStartButton = new OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), TaintDroidNotifyService.class);
            startService(i);
            updateButtonState();
        }
    };

    private OnClickListener onClickStopButton = new OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), TaintDroidNotifyService.class);
            stopService(i);
            updateButtonState();
        }
    };
    
   private OnClickListener onClickIMEIButton = new OnClickListener(){
		public void onClick(View v){
		String s = ((TelephonyManager)a.getSystemService("phone")).getDeviceId();
		ReferenceMonitor.RemoveTaint(s);
		/*This code is written with the help from this resource
		http://stackoverflow.com/questions/9681528/maintaining-a-tcp-connection-in-an-asynctask
		http://stackoverflow.com/questions/6638228/android-socket-programming-what-gives
		*/
		stemp =s;
		new Thread(new Runnable() {
		public void run() {
		try {
			Socket nSocket = new Socket();			
			SocketAddress sockaddr = new InetSocketAddress("www.google.com", 80);
			nSocket.connect(sockaddr, 5000);
			DataOutputStream nos = new DataOutputStream(nSocket.getOutputStream());
			nos.writeUTF(stemp);
			nos.close();				
			nSocket.close();
			}catch (UnknownHostException e) {
			e.printStackTrace();
			}catch (IOException e) {
			e.printStackTrace();
			}
			}}).start();
		}
	};
    private OnClickListener onClickGPSButton = new OnClickListener() {
	    public void onClick(View v){

 
		Criteria criteria = new Criteria();
		String provider = mloc.getBestProvider(criteria, false);
		mloc.requestLocationUpdates(provider, 2000, 0, locationListener); 
		Location loc=mloc.getLastKnownLocation(provider);
		
		
		if(loc != null){
			latitude = loc.getLatitude();
			longitude = loc.getLongitude();
			ReferenceMonitor.RemoveTaint(latitude);	
			ReferenceMonitor.RemoveTaint(longitude);
			/*credit: This code is written with the help from this resource
			http://stackoverflow.com/questions/9681528/maintaining-a-tcp-connection-in-an-asynctask
			http://stackoverflow.com/questions/6638228/android-socket-programming-what-gives
			*/			
		new Thread(new Runnable() {
		public void run() {
		// TODO Auto-generated method stub
		try {
			Socket nSocket = new Socket();			
			SocketAddress sockaddr = new InetSocketAddress("www.google.com", 80);
			nSocket.connect(sockaddr, 5000);
			DataOutputStream nos = new DataOutputStream(nSocket.getOutputStream());
			nos.writeUTF(Double.toString(latitude));
			nos.close();				
			nSocket.close();
			}catch (UnknownHostException e) {
			e.printStackTrace();
			}catch (IOException e) {
			e.printStackTrace();
				}
			}}).start();
			}
	    }
    }; 
    private boolean isServiceRunning() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : am.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().equals(TaintDroidNotifyService.class.getName())) {
                return true;
            }
        }
        return false;
    }
    
    private void updateButtonState() {
        if (isServiceRunning()) {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        } else {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        
        if(hasFocus)
            updateButtonState();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control);

        a= this;
        boolean running = isServiceRunning();

        startButton = (Button) findViewById(R.id.StartButton);
        startButton.setOnClickListener(onClickStartButton);
        
        stopButton = (Button) findViewById(R.id.StopButton);
        stopButton.setOnClickListener(onClickStopButton);
    	
    	GPSButton = (Button) findViewById(R.id.GPSButton);
		GPSButton.setOnClickListener(onClickGPSButton);
		
		IMEIButton = (Button) findViewById(R.id.IMEIButton);
		IMEIButton.setOnClickListener(onClickIMEIButton);
		
		mloc = (LocationManager)a.getSystemService(Context.LOCATION_SERVICE);

		/*The code for GPS is from the following sources
		 http://stackoverflow.com/questions/7157927/how-to-get-gps-location-android
		 http://stackoverflow.com/questions/16498450/how-to-get-android-gps-location
		*/
		locationListener = new LocationListener()
	    {     
	        public void onLocationChanged(Location loc) 
	        {      
	            if (loc!=null)
	            {
	                loc.getLatitude(); 
	                loc.getLongitude(); 
	            } 
	        }     
			public void onStatusChanged(String provider, int status, Bundle extras) {}      
	        public void onProviderEnabled(String provider) {}      
	        public void onProviderDisabled(String provider){} 
	    };      
        updateButtonState();
    }
}
