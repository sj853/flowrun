package com.sj.runflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.SynchronousQueue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private URL url;
	private HttpURLConnection connection;
	private BufferedReader in;
	private final int UPDATE_VIEW = 1;
	private boolean istrack = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btn = (Button) findViewById(R.id.btn);
		Button btn2 = (Button) findViewById(R.id.btn2);
		show = (TextView) findViewById(R.id.show);

		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		

		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				istrack = false;
				Intent intent = new Intent(MainActivity.this, ThreadActivity.class);
				startActivity(intent);
			}
		});

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// handler.post(flowRunThread);
//				new Thread() {
//					@Override
//					public void run() {
//						try {
//							flowRun();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//
//				}.start();
				istrack = true;
				new Thread() {
					@Override
					public void run() {
						while (istrack) {
//							flowData = TrafficStats.getTotalRxBytes();
							flowRun();	
//							Message msg = new Message();
//							msg.what = UPDATE_VIEW;
//							msg.obj = Process.myUid() + " - 总大小： " + fileLen
//									+ "网络类型 : "
//									+ cm.getActiveNetworkInfo().getTypeName()
//									+ " 网络流量： " + flowData + "-time:"
//									+ System.currentTimeMillis();

							handler.sendEmptyMessage(UPDATE_VIEW);

							// show.post(new Runnable() {
							//
							// @Override
							// public void run() {
							// show.setText(" - 总大小： " + fileLen + "网络类型 : "
							// + cm.getActiveNetworkInfo().getTypeName() +
							// " 网络流量： "
							// + flowData+"-time:"+System.currentTimeMillis());
							// }
							// });

						}
					}

				}.start();

			}
		});
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_VIEW:
				MainActivity.this.show.setText(String.valueOf(fileLen));
				break;

			default:
				MainActivity.this.show.setText("nothing");	
				break;
			}

		}

	};
	private TextView show;
	private double fileLen;
	private String content;
	private ConnectivityManager cm;
	private long flowData;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void flowRun() {
//		try {
//			url = new URL("http://www.baidu.com/");
//			// url = new URL("http://www.baidu.com");
//			connection = (HttpURLConnection) url.openConnection();
//			connection.setConnectTimeout(30000);
//			connection.setReadTimeout(30000);
//			fileLen = connection.getContentLength();
			fileLen =  Math.random();
			// in = new BufferedReader(new InputStreamReader(
			// connection.getInputStream()));
			// if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
			// while ((content = in.readLine()) != null) {
			// content += content;
			// // handler.sendEmptyMessage(UPDATE_VIEW);
			// }
			// }

//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} finally {
			// if (in != null) {
			// in.close();
			// }
//			connection.disconnect();
//		}

	}

}
