package com.app.navi.sdkdemo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.app.zxing.R;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;

public class BNDemoActivity extends Activity {

	public static final String TAG = "NaviSDkDemo";
	private static final String APP_FOLDER_NAME = "BNSDKDemo";
	private Button mWgsNaviBtn = null;
	private Button mGcjNaviBtn = null;
	private Button mBdmcNaviBtn = null;
	private String mSDCardPath = null;
	private String endadress = null;
	private double elongitude = 0;
	private double elantitude = 0;
	private double slongitude = 0;
	private double slantitude = 0;
	
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		Bundle bundle = getIntent().getExtras(); 
		String endadress = bundle.getString("endadress");
//		elongitude = (int)((double)bundle.getDouble("elongitude"))*1E5;
//		elantitude = (int)((double)bundle.getDouble("elantitude"))*1E5;
//		slongitude = (int)((double)bundle.getDouble("slongitude"))*1E5;
//		slantitude = (int)((double)bundle.getDouble("slantitude"))*1E5;
		
		elongitude = (double)bundle.getDouble("elongitude");
		elantitude = (double)bundle.getDouble("elantitude");
		slongitude = (double)bundle.getDouble("slongitude");
		slantitude = (double)bundle.getDouble("slantitude");
		
		Toast.makeText(BNDemoActivity.this, "目的经纬度："+elongitude+elantitude, Toast.LENGTH_SHORT).show();

		
		mWgsNaviBtn = (Button) findViewById(R.id.wgsNaviBtn);
		mGcjNaviBtn = (Button) findViewById(R.id.gcjNaviBtn);
		mBdmcNaviBtn = (Button) findViewById(R.id.bdmcNaviBtn);
		
		initListener();
		if ( initDirs() ) {
			initNavi();
		}
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
	}
	
	private void initListener() {
		if ( mWgsNaviBtn != null ) {
			mWgsNaviBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if ( BaiduNaviManager.isNaviInited() ) {
						routeplanToNavi(CoordinateType.WGS84);
					}
				}
			});
		}
		if ( mGcjNaviBtn != null ) {
			mGcjNaviBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if ( BaiduNaviManager.isNaviInited() ) {
						routeplanToNavi(CoordinateType.GCJ02);
					}
				}
			});
		}
		if ( mBdmcNaviBtn != null ) {
			mBdmcNaviBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if ( BaiduNaviManager.isNaviInited() ) {
						routeplanToNavi(CoordinateType.BD09_MC);
					}
				}
			});
		}
	}
	
	private boolean initDirs() {
		mSDCardPath = getSdcardDir();
		if ( mSDCardPath == null ) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);
		if ( !f.exists() ) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	String authinfo = null;
	
	
	//�ٶȷ�����Ȩ�������ʼ��
	private void initNavi() {
	    BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath + "/BaiduNaviSDK_SO");
		BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME,
			new NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败," + msg;
                }
                BNDemoActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(BNDemoActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });

            }
				public void initSuccess() {
					Toast.makeText(BNDemoActivity.this, "导航引擎初始化成功", Toast.LENGTH_SHORT).show();
				}
				
				public void initStart() {
					Toast.makeText(BNDemoActivity.this, "导航引擎初始化开始ʼ", Toast.LENGTH_SHORT).show();
				}
				
				public void initFailed() {
					Toast.makeText(BNDemoActivity.this,  "导航引擎初始化失败", Toast.LENGTH_SHORT).show();
				}
		}, null /*mTTSCallback*/);
	}
	
	
	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}
	
	//��·�������յ㣬��·ƫ�ã��Ƿ�ģ�⵼���Ȳ�����Ȼ���ڻص�������������ת���յ��� 
	private void routeplanToNavi(CoordinateType coType) {
		
		//�������һ�����յ�ʾ����ʵ��Ӧ���п���ͨ��POI����
		//�ⲿPOI��Դ�ȷ�ʽ��ȡ���յ�����
		
	    BNRoutePlanNode sNode = null;
	    BNRoutePlanNode eNode = null;
		switch(coType) {
			case GCJ02: {
				sNode = new BNRoutePlanNode(slongitude,slantitude,
						"当前位置", null, coType);
				eNode = new BNRoutePlanNode(elongitude,elantitude, 
						endadress, null, coType);
				break;
			}
			case WGS84: {
				sNode = new BNRoutePlanNode(slongitude,slantitude,
						"当前位置", null, coType);
				eNode = new BNRoutePlanNode(elongitude,elantitude, 
						endadress, null, coType);
				break;
			}
			case BD09_MC: {
				sNode = new BNRoutePlanNode(slongitude,slantitude,
						"当前位置", null, coType);
				eNode = new BNRoutePlanNode(elongitude,elantitude, 
						endadress, null, coType);
				break;
			}
			default : ;
		}
		if (sNode != null && eNode != null) {
	        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
	        list.add(sNode);
	        list.add(eNode);
	        BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
		}
	}
	//·�߹滮���������滮�ɹ���һ����ת����������ҳ��
	public class DemoRoutePlanListener implements RoutePlanListener {
		
		private BNRoutePlanNode mBNRoutePlanNode = null;
		public DemoRoutePlanListener(BNRoutePlanNode node){
			mBNRoutePlanNode = node;
		}
		
		//·�߹滮�ɹ�����Ҫ��ת����������ҳ��
		@Override
		public void onJumpToNavigator() {
			 Intent intent = new Intent(BNDemoActivity.this, BNDemoGuideActivity.class);
			 Bundle bundle = new Bundle();
			 bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
			 intent.putExtras(bundle);
             startActivity(intent);
		}
		//·�߹滮ʧ��
		@Override
		public void onRoutePlanFailed() {
			// TODO Auto-generated method stub
			
		}
	}
	
	private BNOuterTTSPlayerCallback mTTSCallback = new BNOuterTTSPlayerCallback() {
		
		@Override
		public void stopTTS() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void resumeTTS() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void releaseTTSPlayer() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public int playTTSText(String speech, int bPreempt) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public void phoneHangUp() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void phoneCalling() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void pauseTTS() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void initTTSPlayer() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public int getTTSState() {
			// TODO Auto-generated method stub
			return 0;
		}
	};
}
