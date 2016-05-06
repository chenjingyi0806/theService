package com.app.login;







import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.mysql.jdbc.Driver;
import com.app.zxing.MainActivity;
import com.app.zxing.PictureSave;
import com.app.zxing.R;

import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import android.util.Log;
import android.view.View;
import android.widget.*;

import com.app.global.*;
import com.app.manager.managerActivity;
import com.app.navi.sdkdemo.LocationDemo;

public class LoginActivity extends Activity {
	Button btn_login;
	EditText lg_usr,lg_pass;
	RadioGroup identify;
	RadioButton radioButton;
	int m_name;
	String m_pass;
	int m_isLead;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);   
		initView();
		setListener();	
		
	}
	
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data=msg.getData();
			String valString=data.getString("value");
			//Toast.makeText(getApplicationContext(), valString, Toast.LENGTH_LONG).show();
			if (valString.equals("true")&&m_isLead==0) {
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				// Intent intent = new Intent(JuQiMapMainActivity.this,RoutePlanDemo.class);
				startActivity(intent);
				finish();
			}else if (valString.equals("true")&&m_isLead==1) {
				Intent intent = new Intent(LoginActivity.this,managerActivity.class);
				startActivity(intent);
				finish();
			}
			else {
				Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG).show();
			}
		};
	}	;
	

	private void setListener() {
		// TODO Auto-generated method stub
		setLoginListener();
	}

	private void setLoginListener() {
		// TODO Auto-generated method stub
		btn_login.setOnClickListener(new View.OnClickListener() {
		Connection connection;	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
					m_name=Integer.valueOf(lg_usr.getText().toString());
					m_pass=lg_pass.getText().toString();					
					
					if (radioButton.getId()==R.id.staff) {
						m_isLead=0;
					}else {
						m_isLead=1;
					}
					/*************/
					new Thread(network).start();
				   
				   
				
			}
		});
	}
	
	Runnable network=new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			   SoapObject soapObject=new SoapObject(Global.namespace, Global.webmethod);
			   soapObject.addProperty("arg0", m_name);
			   soapObject.addProperty("arg1", m_pass);
			   soapObject.addProperty("arg2", m_isLead);
			   SoapSerializationEnvelope serializationEnvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			   serializationEnvelope.bodyOut=soapObject;
			   HttpTransportSE httpTransportSE=new HttpTransportSE(Global.wsdl);
			   try {
				httpTransportSE.call(null, serializationEnvelope);
				Log.i("mytag", "success");
				if (serializationEnvelope.getResponse()!=null) {
					Object obj=serializationEnvelope.getResponse();
					//Toast.makeText(getApplicationContext(), "结果为："+obj.toString(), Toast.LENGTH_LONG).show();
					Log.i("mytag", "success");
					Message msg = new Message();  
			        Bundle data = new Bundle();  
			        data.putString("value",obj.toString() );  
			        msg.setData(data);  
			        handler.sendMessage(msg);  
				}
				else {
					Log.i("mytag", "null");	
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.i("mytag", "fail1");
				e.printStackTrace();
			}
			   
			    
		}
	};

	private void initView() {
		// TODO Auto-generated method stub
		btn_login=(Button) findViewById(R.id.btn_login);
		lg_usr=(EditText) findViewById(R.id.usr);
		lg_pass=(EditText) findViewById(R.id.pass);
		identify=(RadioGroup) findViewById(R.id.identify);
		radioButton=(RadioButton) findViewById(identify.getCheckedRadioButtonId());
	}
	

}
