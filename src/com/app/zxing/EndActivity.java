package com.app.zxing;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.System;
import android.view.View;
import android.widget.Button;

public class EndActivity extends Activity {
	Button btn_back,btn_finish;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.end);
		btn_back=(Button) findViewById(R.id.btn_back);
	
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(EndActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		
	}

}
