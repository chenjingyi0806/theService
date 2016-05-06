package com.app.mapapi.overlayutil;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnPolylineClickListener;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;

/**
 * �����ṩһ���ܹ���ʾ�͹�����Overlay�Ļ���
 * <p>
 * ��д{@link #getOverlayOptions()} ��������ʾ�͹����Overlay�б�
 * </p>
 * <p>
 * ͨ��
 * {@link com.baidu.mapapi.map.BaiduMap#setOnMarkerClickListener(com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener)}
 * �����������¼����ݸ�OverlayManager��OverlayManager������Ӧ����¼���
 * <p>
 * ��д{@link #onMarkerClick(com.baidu.mapapi.map.Marker)} ����Marker����¼�
 * </p>
 */
public abstract class OverlayManager implements OnMarkerClickListener, OnPolylineClickListener {

    BaiduMap mBaiduMap = null;
    private List<OverlayOptions> mOverlayOptionList = null;

    List<Overlay> mOverlayList = null;

    /**
     * ͨ��һ��BaiduMap ������
     * 
     * @param baiduMap
     */
    public OverlayManager(BaiduMap baiduMap) {
        mBaiduMap = baiduMap;
        // mBaiduMap.setOnMarkerClickListener(this);
        if (mOverlayOptionList == null) {
            mOverlayOptionList = new ArrayList<OverlayOptions>();
        }
        if (mOverlayList == null) {
            mOverlayList = new ArrayList<Overlay>();
        }
    }

    /**
     * ��д�˷�������Ҫ�����Overlay�б�
     * 
     * @return �����Overlay�б�
     */
    public abstract List<OverlayOptions> getOverlayOptions();

    /**
     * ������Overlay ��ӵ���ͼ��
     */
    public final void addToMap() {
        if (mBaiduMap == null) {
            return;
        }

        removeFromMap();
        List<OverlayOptions> overlayOptions = getOverlayOptions();
        if (overlayOptions != null) {
            mOverlayOptionList.addAll(getOverlayOptions());
        }

        for (OverlayOptions option : mOverlayOptionList) {
            mOverlayList.add(mBaiduMap.addOverlay(option));
        }
    }

    /**
     * ������Overlay �� ��ͼ������
     */
    public final void removeFromMap() {
        if (mBaiduMap == null) {
            return;
        }
        for (Overlay marker : mOverlayList) {
            marker.remove();
        }
        mOverlayOptionList.clear();
        mOverlayList.clear();

    }

    /**
     * ���ŵ�ͼ��ʹ����Overlay���ں��ʵ���Ұ��
     * <p>
     * ע�� �÷���ֻ��Marker���͵�overlay��Ч
     * </p>
     * 
     */
    public void zoomToSpan() {
        if (mBaiduMap == null) {
            return;
        }
        if (mOverlayList.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Overlay overlay : mOverlayList) {
                // polyline �еĵ����̫�ֻ࣬��marker ����
                if (overlay instanceof Marker) {
                    builder.include(((Marker) overlay).getPosition());
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }
    }

}

