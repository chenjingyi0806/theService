package com.app.navi.sdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.zxing.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 *  ��demo����չʾ��ν��е�������������õ�ַ�������꣩����������������������������ַ��
 */
public class GeoCoderDemo extends Activity implements
		OnGetGeoCoderResultListener {
	GeoCoder mSearch = null; //  ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	BaiduMap mBaiduMap = null;
	MapView mMapView = null;
	
	public String key = null;
	public float longitude = 0;
	public float lantitude = 0;
	public float accuracy = 0;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.activity_geocoder);
		CharSequence titleLable = "地理编码功能";
		setTitle(titleLable);
		
		Bundle bundle = getIntent().getExtras(); 
		String searchkey = bundle.getString("editsearchkey");
		key = searchkey;
		longitude = (float)bundle.getDouble("longitude");
		lantitude = (float)bundle.getDouble("lantitude");
		accuracy = bundle.getFloat("accuracy");
		
		MyLocationData locData = new MyLocationData.Builder()
        .accuracy(accuracy)
                // �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
        .direction(100)
        .latitude(lantitude)
        .longitude(longitude).build();
       //���ö�λ����
			
		Toast.makeText(getBaseContext(), "当前位置："+"经度："+longitude+"纬度:"+lantitude+"精确度"+accuracy, 1)
		.show();

		//// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		
	//	mBaiduMap.setMyLocationData(locData);
		
			

		// ��ʼ������ģ�飬ע���¼�����
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		SearchButtonProcess();
	}

	/**
	 * ��������
	 * 
	 * @param v
	 */
	public void SearchButtonProcess() {
		
		LatLng ptCenter = new LatLng(lantitude, longitude);
			// Geo����
			mSearch.geocode(new GeoCodeOption().city(
					"����").address(key));	
			// ��Geo����
			mSearch.reverseGeoCode(new ReverseGeoCodeOption()
								.location(ptCenter));
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
		mMapView.onDestroy();
		mSearch.destroy();
		super.onDestroy();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(GeoCoderDemo.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		String strInfo = String.format("纬度：%f  经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(GeoCoderDemo.this, strInfo, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(GeoCoderDemo.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		Toast.makeText(GeoCoderDemo.this, result.getAddress(),
				Toast.LENGTH_LONG).show();

	}

}
