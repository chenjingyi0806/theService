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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.app.zxing.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.PlanNode;
import com.app.navi.sdkdemo.BNDemoActivity.DemoRoutePlanListener;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;

import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;



public class LocationDemo extends Activity {
	// ��λ���
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		private LocationMode mCurrentMode;
		
		BitmapDescriptor mCurrentMarker;

		MapView mMapView;
		BaiduMap mBaiduMap;
		
		GeoCoder mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
		
		/**
		 * ��ǰ�ҵ�λ�õľ�γ��
		 */
		private LatLng mCurrentLocation;
		private double mCurrentLantitude;
		private double mCurrentLongitude;
		private double mCurrentLantitude1;
		private double mCurrentLongitude1;
		private float mCurrentaccuracy;
		private String mCurrentaddr = null;
		/**
		 * ����Ŀ�ĵصľ�γ��
		 */
		private LatLng mEndLocation;
		private double mEndLantitude;
		private double mEndLongitude;
		private double mEndLantitude1;
		private double mEndLongitude1;
		
		private String mSDCardPath = null;
		
		public String key = null;
		public static final String ROUTE_PLAN_NODE = "routePlanNode";
		private static final String APP_FOLDER_NAME = "BNSDKDemo";

		// UI���
		OnCheckedChangeListener radioButtonListener;
		Button requestLocButton;
		boolean isFirstLoc = true;// �Ƿ��״ζ�λ

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_location);
	        
	        requestLocButton = (Button) findViewById(R.id.button1);
	        mCurrentMode = LocationMode.NORMAL;
	        requestLocButton.setText("普通");
	        OnClickListener btnClickListener1 = new OnClickListener() {
	            public void onClick(View v) {
	                switch (mCurrentMode) {
	                    case NORMAL:
	                        requestLocButton.setText("跟随");
	                        mCurrentMode = LocationMode.FOLLOWING;
	                        mBaiduMap
	                                .setMyLocationConfigeration(new MyLocationConfiguration(
	                                        mCurrentMode, true, mCurrentMarker));
	                        break;
	                    case COMPASS:
	                        requestLocButton.setText("普通");
	                        mCurrentMode = LocationMode.NORMAL;
	                        mBaiduMap
	                                .setMyLocationConfigeration(new MyLocationConfiguration(
	                                        mCurrentMode, true, mCurrentMarker));
	                        break;
	                    case FOLLOWING:
	                        requestLocButton.setText("罗盘");
	                        mCurrentMode = LocationMode.COMPASS;
	                        mBaiduMap
	                                .setMyLocationConfigeration(new MyLocationConfiguration(
	                                        mCurrentMode, true, mCurrentMarker));
	                        break;
	                    default:
	                        break;
	                }
	            }
	        };
	        requestLocButton.setOnClickListener(btnClickListener1);
           //��ʼ������ť
			Button searchbn = (Button) findViewById(R.id.search);
			 searchbn.setOnClickListener(new View.OnClickListener() {
				 @Override
				public void onClick(View source)
				{	 //�����Ŀ�ĵص�ַ			 
					 EditText editsearchkey = (EditText) findViewById(R.id.searchkey);			 
					 key = editsearchkey.getText().toString();					 				 				 
					 initGeoCoderEvent();	//��Ŀ�ĵ�ַ����POI�������									
				}
			 });
			 //·�߹滮��ť
			 Button routebn = (Button) findViewById(R.id.button2);
			 routebn.setOnClickListener(new View.OnClickListener() {
				 @Override
				public void onClick(View source)
				{	 
					 Intent intent = new Intent(LocationDemo.this,RoutePlanDemo.class);					
					 startActivity(intent);
				}
			 });

			// ��ͼ��ʼ��
			mMapView = (MapView) findViewById(R.id.bmapView);
			mBaiduMap = mMapView.getMap();
			// ������λͼ��
			mBaiduMap.setMyLocationEnabled(true);
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));//�������ż���  500m
			// ��λ��ʼ��
			mLocClient = new LocationClient(this);
			mLocClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// ��gps
			option.setIsNeedAddress(true);//��õ�ַ��Ϣ��Ĭ��Ϊ��
			option.setCoorType("bd09ll"); // ������������
			option.setScanSpan(1000); 
			mLocClient.setLocOption(option);
			mLocClient.start();	
			
			if ( initDirs() ) {//��ɵ�����ʼ��
				initNavi();
			}

		}
		/**
		 * �������ת��ģ��,ע�����
		 */
		private void initGeoCoderEvent() {
			mSearch = GeoCoder.newInstance();
			mSearch
					.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

						@Override
						public void onGetReverseGeoCodeResult(
								ReverseGeoCodeResult result) {
							return;
						}

						@Override
						public void onGetGeoCodeResult(GeoCodeResult result) {
							if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
								Toast.makeText(LocationDemo.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
										.show();
								return;
							}							
							mEndLantitude = result.getLocation().latitude;
							mEndLongitude = result.getLocation().longitude;
							//String strInfo = String.format("γ�ȣ�%f ���ȣ�%f",
									//result.getLocation().latitude, result.getLocation().longitude);
							//Toast.makeText(LocationDemo.this, strInfo, Toast.LENGTH_LONG).show();
													
							//����Ϊ����ת������λ�Լ������������궼��bd09ll���꣬ת��Ϊ���ڵ�����GCJ02���꣡��������Ҫ
						    BDLocation bdLocStartBefore = new BDLocation();
					        bdLocStartBefore.setLatitude(mCurrentLantitude);
					        bdLocStartBefore.setLongitude(mCurrentLongitude);
					        BDLocation bdLocStartAfter = LocationClient.getBDLocationInCoorType(
					        bdLocStartBefore, BDLocation.BDLOCATION_BD09LL_TO_GCJ02);
					        mCurrentLantitude1 = bdLocStartAfter.getLatitude();
					        mCurrentLongitude1 = bdLocStartAfter.getLongitude();
					        
					        BDLocation bdLocEndBefore = new BDLocation();
					        bdLocStartBefore.setLatitude(mEndLantitude);
					        bdLocStartBefore.setLongitude(mEndLongitude);
					        BDLocation bdLocEndAfter = LocationClient.getBDLocationInCoorType(
					        bdLocStartBefore, BDLocation.BDLOCATION_BD09LL_TO_GCJ02);
					        mEndLantitude1 = bdLocEndAfter.getLatitude();
					        mEndLongitude1 = bdLocEndAfter.getLongitude();
					        
					        //Toast.makeText(LocationDemo.this, "����ת�����", Toast.LENGTH_LONG).show();
					        
					        //��ʼ����
					        startNavi();
						        
//							 Bundle data = new Bundle();				
//							 data.putString("endadress",key);
//							 data.putDouble("elantitude", mEndLantitude1);
//							 data.putDouble("elongitude", mEndLongitude1);
//							 
//							 data.putDouble("slantitude", mCurrentLantitude1);
//							 data.putDouble("slongitude", mCurrentLongitude1);
	
				    		 //Intent intent = new Intent(LocationDemo.this,BNDemoActivity.class);
						     //intent.putExtras(data);				     
							// startActivity(intent);

						}
					});
			
			mSearch.geocode(new GeoCodeOption().city(
					"杭州").address(key));	

		}
		
		/**
		 * ��λSDK��������
		 */
		public class MyLocationListenner implements BDLocationListener {

	        @Override
	        public void onReceiveLocation(BDLocation location) {
	            // map view ���ٺ��ڴ����½��յ�λ��
	            if (location == null || mMapView == null) {
	                return;
	            }

	            MyLocationData locData = new MyLocationData.Builder()
	                    .accuracy(location.getRadius())
	                            // �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
	                    .direction(100)
	                    .latitude(location.getLatitude())
	                    .longitude(location.getLongitude()).build();
	           // ���ö�λ����
	            mBaiduMap.setMyLocationData(locData);
	            
	            mCurrentaddr = location.getAddress().address;
                //��õ�ǰλ�þ�γ�����꣬���ڵ���
	            mCurrentaccuracy = location.getRadius();
	            mCurrentLantitude = location.getLatitude();
				mCurrentLongitude = location.getLongitude();
					
				//�ҵĵ�ǰλ������
				mCurrentLocation = new LatLng(mCurrentLantitude, mCurrentLongitude);
				
				// ��һ�ζ�λʱ������ͼλ���ƶ�����ǰλ��            
	            if (isFirstLoc) {
	                isFirstLoc = false;
	                LatLng ll = new LatLng(location.getLatitude(),
	                        location.getLongitude());
	                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
	                mBaiduMap.animateMapStatus(u);
	            }
	        }

			public void onReceivePoi(BDLocation poiLocation) {
			}
		}
		
		public void startNavi(){
		
			if ( BaiduNaviManager.isNaviInited() ) {
				routeplanToNavi();  //��ʼ����ɣ�����е�����·�滮
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
		private String getSdcardDir() {
			if (Environment.getExternalStorageState().equalsIgnoreCase(
					Environment.MEDIA_MOUNTED)) {
				return Environment.getExternalStorageDirectory().toString();
			}
			return null;
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
	                LocationDemo.this.runOnUiThread(new Runnable() {

	                    @Override
	                    public void run() {
	                        Toast.makeText(LocationDemo.this, authinfo, Toast.LENGTH_LONG).show();
	                    }
	                });

	            }
					public void initSuccess() {
						Toast.makeText(LocationDemo.this, "导航引擎初始化成功", Toast.LENGTH_SHORT).show();
					}
					
					public void initStart() {
						Toast.makeText(LocationDemo.this, "导航引擎初始化开始", Toast.LENGTH_SHORT).show();
					}
					
					public void initFailed() {
						Toast.makeText(LocationDemo.this,  "导航引擎初始化失败", Toast.LENGTH_SHORT).show();
					}
			}, null /*mTTSCallback*/);
		}
		
		//��·�������յ㣬��·ƫ�ã��Ƿ�ģ�⵼���Ȳ�����Ȼ���ڻص�������������ת���յ��� 
		private void routeplanToNavi() {
			
			//�������һ�����յ�ʾ����ʵ��Ӧ���п���ͨ��POI����
			//�ⲿPOI��Դ�ȷ�ʽ��ȡ���յ�����
			
		    BNRoutePlanNode sNode = null;
		    BNRoutePlanNode eNode = null;
			
			sNode = new BNRoutePlanNode(mCurrentLongitude1,mCurrentLantitude1,
							"当前位置", null);
		    eNode = new BNRoutePlanNode(mEndLongitude1,mEndLantitude1, 
							key, null);
			
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
				 Intent intent = new Intent(LocationDemo.this, BNDemoGuideActivity.class);
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
	    

		@Override
		protected void onPause() {
			mMapView.onPause();
			super.onPause();
		}

		@Override
		protected void onResume() {
			mMapView.onResume();
			super.onResume();
		}

		@Override
		protected void onDestroy() {
			// �˳�ʱ���ٶ�λ
			mLocClient.stop();
			// �رն�λͼ��
			mBaiduMap.setMyLocationEnabled(false);
			mMapView.onDestroy();
			mMapView = null;
			super.onDestroy();
		}

	}

