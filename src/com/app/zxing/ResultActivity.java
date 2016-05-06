package com.app.zxing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity {
	public static String QR_result;
	private static final int ACTION_CAMERA=1;
    TextView resultview,image_string;
    ImageView result_image;
    Button cameraButton, backButton;
    
    Bundle saveBundle;
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	saveBundle=savedInstanceState;
    	setContentView(R.layout.result);
    	initView();
    	getQRResult();      
       	initListener();    	
    	}

	private void getQRResult() {
		Intent intent=getIntent();
    	String result=intent.getStringExtra("result");   
    	QR_result=result;
    	resultview.setText(result);
    	
	}

	private void initView() {
		cameraButton=(Button) findViewById(R.id.btn_camera);
    	resultview=(TextView)findViewById(R.id.result_string);
    	backButton=(Button) findViewById(R.id.btn_back);
    	result_image=(ImageView) findViewById(R.id.result_image);
    	image_string=(TextView) findViewById(R.id.image_string);
	}

	private void initListener() {
		setCameraListener();
    	setBackListener();
	}

	private void setBackListener() {
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ResultActivity.this.finish();
			}
		});
	}
    
	private void setCameraListener() {
		cameraButton.setOnClickListener(new View.OnClickListener() {
		String imagePath=Environment.getExternalStorageDirectory().getPath()+"/juqi/new.jpg";		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
				 File file=new File(imagePath);//暂存路径
				 if (file.exists()) {
					file.delete();
				}
				 try {
					 file.createNewFile();
					 file.setWritable(true,false);
				} catch (Exception e) {
					// TODO: handle exception
				}
				 Uri u=Uri.fromFile(file);
			
				takePictureIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				
				startActivityForResult(takePictureIntent, ACTION_CAMERA);//因为有多个intent,所以要区分返回的结果  
			}
		});
	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	Intent intent=new Intent(ResultActivity.this,PictureSave.class);
 
    	startActivity(intent);
    	ResultActivity.this.finish();
    	
    }
}
