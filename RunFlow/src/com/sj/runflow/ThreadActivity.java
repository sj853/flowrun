package com.sj.runflow;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ThreadActivity extends Activity {

	//流量大小 MB
	public static int TOTAL_RUN= 1;
	//已读取流量
	public int CURRENT_RUN = 0 ;
	
	private boolean isRunning = false;
	private Button btn1;
	private Button btn2;
	private TextView txt;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			txt.setText(msg.obj.toString());
		}

	};
	private Runnable countRunnable = new Runnable() {

		
		DecimalFormat df = new DecimalFormat("#.00"); 
		@Override
		public void run() {
			isRunning = true;
			while (!Thread.currentThread().isInterrupted() && isRunning) {
				// txt.setText(String.valueOf(Math.random()));
				SystemClock.sleep(1000);
				Message msg = new Message();
				msg.obj = String.valueOf(TrafficStats.getTotalTxBytes()+"-目前消耗了 "+df.format((double)CURRENT_RUN/1024)+" KB 流量-"+System.currentTimeMillis());
				handler.sendMessage(msg);
			}
		}
	};
	private Runnable httpRunnable = new Runnable() {
		
		@Override
		public void run() {

			HttpURLConnection connection = null;
			try {
				URL url = new URL("http://192.168.13.12/data.txt");
				connection = (HttpURLConnection) url
						.openConnection();
				connection.setConnectTimeout(30000);
				connection.setReadTimeout(30000);
				InputStream inputStream = connection.getInputStream();
				while(inputStream.read() != -1){
					CURRENT_RUN++;
				}
				SystemClock.sleep(1000);
				Log.i("debug", "has read: "+CURRENT_RUN);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(connection !=null)
				connection.disconnect();
				connection = null;
			}
			
		
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thread);

		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		txt = (TextView) findViewById(R.id.textView2);

		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(countRunnable).start();
				new Thread().start();
			}
		});

		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ThreadActivity.this.isRunning = false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.thread, menu);
		return true;
	}

}
