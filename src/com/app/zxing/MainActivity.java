package com.app.zxing;

import com.app.navi.sdkdemo.JuQiMapMainActivity;
import com.app.navi.sdkdemo.LocationDemo;
import com.baidu.mapapi.SDKInitializer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
public class MainActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	
	Button btn_qrcode,btn_map,btn_inquiry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.loading);   	
    	initView();
    	SDKInitializer.initialize(getApplicationContext());  
    	setListener();
    }
	private void setListener() {
		setQRListener();
    	setInquiryListener();
    	setMapListener();
	}
	private void setMapListener() {
		btn_map.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,LocationDemo.class);
				// Intent intent = new Intent(JuQiMapMainActivity.this,RoutePlanDemo.class);
				startActivity(intent);
			}
		});
	}
	private void setInquiryListener() {
		btn_inquiry.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void setQRListener() {
		btn_qrcode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MipcaActivityCapture.class);	
				startActivity(intent);
				//finish();
			}
		});
	}
	private void initView() {
		btn_qrcode=(Button)findViewById(R.id.btn_qrcode);
    	btn_inquiry=(Button) findViewById(R.id.btn_inquiry);
    	btn_map=(Button) findViewById(R.id.btn_map);
	}
   

}
