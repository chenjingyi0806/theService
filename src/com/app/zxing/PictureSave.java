package com.app.zxing;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.baidu.lbsapi.auth.n;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PictureSave extends Activity {
	ImageView image;
	Button btn_back,btn_save;
	EditText image_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picturesave);	
		initView();
		showImage();		
		setBackeListener();
		setSaveListener();
	
	}


	private void showImage() {
		String imagePath=Environment.getExternalStorageDirectory().getPath()+"/juqi/new.jpg";
		File file=new File(imagePath);
		try {
			FileInputStream is=new FileInputStream(file);
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inJustDecodeBounds=false;
			options.inSampleSize=3;
			Bitmap bitmap=BitmapFactory.decodeStream(is, null, options);
			image.setImageBitmap(bitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void setSaveListener() {
		// TODO Auto-generated method stub
		btn_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String string=image_text.getText().toString();
				if(string.isEmpty()){
					Toast.makeText(PictureSave.this, "请输入图片信息",Toast.LENGTH_SHORT).show();
					return;
				}
				String newQR_result=ResultActivity.QR_result;
				File newFile=new File(Environment.getExternalStorageDirectory().getPath()+"/juqi/"+newQR_result+".jpg");		
				if (!newFile.exists()) {
					try {
						newFile.getParentFile().mkdir();
						newFile.createNewFile();
						newFile.setReadable(true,false);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
				
				String imagePath=Environment.getExternalStorageDirectory().getPath()+"/juqi"+"/new.jpg";
				
				copyFile(imagePath,newFile.getAbsolutePath());
				Intent intent=new Intent(PictureSave.this, EndActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	private void copyFile(String oldpath,String newpath){
		try {   
	           int bytesum = 0;   
	           int byteread = 0;   
	           File oldfile = new File(oldpath);
	           if (oldfile.exists()) { //文件存在时   
	               InputStream inStream = new FileInputStream(oldpath); //读入原文件   
	               FileOutputStream fs = new FileOutputStream(newpath);   
	               byte[] buffer = new byte[1024];   
	               int length;   
	               while ( (byteread = inStream.read(buffer)) != -1) {   
	                   bytesum += byteread; //字节数 文件大小   
	                  // Log.i("mytag","读取字节数"+ bytesum);   
	                   fs.write(buffer, 0, byteread);   
	               }   
	               inStream.close(); 
	               fs.close();
	           }   
	       }   
	       catch (Exception e) {   
	           System.out.println("复制单个文件操作出错");   
	           e.printStackTrace();   
	  
	       }   
	  
	   }   
	


	private void setBackeListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}


	private void initView() {
		image=(ImageView) findViewById(R.id.image);
		btn_back=(Button) findViewById(R.id.btn_back);
		btn_save=(Button) findViewById(R.id.btn_save);
		image_text=(EditText) findViewById(R.id.image_text);
	}
	
	
	/*********decodeSampledBitmapFromResource 用于做缩略图，相机返回的数据也是缩略图
	 * 1.这种方法缩略图图片并不清晰，但是可以大大减少内存，inSampleSize是采样率
	 * 2.一般直接使用options.inSampleSize=3比较清晰，无需计算insamplesize
	 * 3.bitmap.compress方法是图片质量压缩，不会减少像素，占用内存不会变
	 * 4.还有一种matrix压缩，createBitmap就可以了
	 * **************/
	public static int calculateInSampleSize(  
            BitmapFactory.Options options, int reqWidth, int reqHeight) {  
    // Raw height and width of image  
    final int height = options.outHeight;  
    final int width = options.outWidth;  
    int inSampleSize = 1;  
  
    if (height > reqHeight || width > reqWidth) {  
  
        // Calculate ratios of height and width to requested height and width  
        final int heightRatio = Math.round((float) height / (float) reqHeight);  
        final int widthRatio = Math.round((float) width / (float) reqWidth);  
  
        // Choose the smallest ratio as inSampleSize value, this will guarantee  
        // a final image with both dimensions larger than or equal to the  
        // requested height and width.  
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;  
    }  
  
    return inSampleSize;  
} 
	public static Bitmap decodeSampledBitmapFromResource(String pathName,  
	        int reqWidth, int reqHeight) {  
	  
	    // First decode with inJustDecodeBounds=true to check dimensions  
	    final BitmapFactory.Options options = new BitmapFactory.Options();  
	    options.inJustDecodeBounds = true;  
	    BitmapFactory.decodeFile(pathName, options);  
	  
	    // Calculate inSampleSize  
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);  
	  
	    // Decode bitmap with inSampleSize set  
	    options.inJustDecodeBounds = false;  
	    return BitmapFactory.decodeFile(pathName, options);  
	}  
	

	
	
	

}
