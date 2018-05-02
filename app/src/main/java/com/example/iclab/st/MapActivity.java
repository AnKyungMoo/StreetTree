package com.example.iclab.st;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

// 지도 액티비티
public class MapActivity extends AppCompatActivity implements MapView.MapViewEventListener {
    Button gpsButton = null;
    MapView mapView = null;
    MapPoint mapPoint = null;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("e95ede72416f09346c75c0acb52472ed");
        RelativeLayout container = (RelativeLayout) findViewById(R.id.map);
        container.addView(mapView);

        mapView.setMapViewEventListener(this);

        gpsButton = (Button) findViewById(R.id.gps);

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveMapViewCurrentPosition();
            }
        });
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        this.mapView = mapView;
        mapPoint = MapPoint.mapPointWithGeoCoord(36.770598, 126.931647);
        this.mapView.setMapCenterPoint(mapPoint, true);
    }

    public void moveMapViewCurrentPosition() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);

        // 권한이 허용되어있지 않은 경우
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /* TODO: 권한 부여 */
            return;
        }
        locationManager.requestLocationUpdates("gps", 0, 0, locationListener);

        mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        mapView.setMapCenterPoint(mapPoint, true);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        //Log.d("mapPoint", mapPoint.getMapPointGeoCoord().latitude + "");
        Intent intent = new Intent(MapActivity.this, SurveyActivity.class);

        intent.putExtra("latitude", mapPoint.getMapPointGeoCoord().latitude);
        intent.putExtra("longitude", mapPoint.getMapPointGeoCoord().longitude);

        startActivity(intent);
        /*
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("SoonChunHyaung");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
        */
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            latitude = location.getLatitude();   //위도
            longitude = location.getLongitude(); //경도

            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };
}
