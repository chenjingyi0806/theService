package com.app.navi.sdkdemo;

import com.app.zxing.R;
import com.baidu.mapapi.SDKInitializer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
//import juqimap.demo.R;
//import com.baidu.mapapi;
//import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.VersionInfo;

public class JuQiMapMainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.juqimap);
		SDKInitializer.initialize(getApplicationContext());  
		 Button bn = (Button) findViewById(R.id.bn);
		 bn .setOnClickListener(new View.OnClickListener() {
			 @Override
			public void onClick(View source)
			{
				Intent intent = new Intent(JuQiMapMainActivity.this,LocationDemo.class);
				// Intent intent = new Intent(JuQiMapMainActivity.this,RoutePlanDemo.class);
				startActivity(intent);
			}
		 });		
	}	
}
